package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.dto.request.DiscountRequest;
import nnt.com.domain.aggregates.model.dto.request.RoomRequest;
import nnt.com.domain.aggregates.model.dto.response.AmenityResponse;
import nnt.com.domain.aggregates.model.dto.response.RoomResponse;
import nnt.com.domain.aggregates.model.entity.Discount;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.model.entity.Room;
import nnt.com.domain.aggregates.model.mapper.RoomMapper;
import nnt.com.domain.aggregates.model.vo.DiscountType;
import nnt.com.domain.aggregates.repository.DiscountDomainRepository;
import nnt.com.domain.aggregates.repository.HomestayDomainRepository;
import nnt.com.domain.aggregates.repository.RoomDomainRepository;
import nnt.com.domain.aggregates.service.RoomDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RoomDomainServiceImpl implements RoomDomainService {
    RoomDomainRepository roomDomainRepository;
    HomestayDomainRepository homestayDomainRepository;
    DiscountDomainRepository discountDomainRepository;
    RoomMapper roomMapper;

    @Override
    public Room save(Room room) {
        return roomDomainRepository.save(room);
    }

    @Override
    public Room update(Room room) {
        return roomDomainRepository.update(room);
    }

    @Override
    public Room getById(Long id) {
        return roomDomainRepository.getById(id);
    }

    @Override
    public Page<Room> getAll(int page, int size, String sort, String direction) {
        return roomDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(Long id) {
        roomDomainRepository.delete(id);
    }

    @Override
    public List<RoomResponse> getByHomestayId(Long homestayId) {
        List<Room> room = roomDomainRepository.getByHomestayId(homestayId);
        List<RoomResponse> response = new ArrayList<>();
        room.forEach(r -> {
            response.add(getRoomResponse(r));
        });
        return response;
    }

    @Override
    public RoomResponse save(RoomRequest roomRequest) {
        Room room = roomMapper.toEntity(roomRequest);
        Homestay homestay = homestayDomainRepository.getById(roomRequest.getHomestayId());
        homestay.setBedrooms(homestay.getBedrooms() + 1);
        homestay.setBeds(homestay.getBeds() + roomRequest.getBeds());
        homestay.setMaxGuests(homestay.getMaxGuests() + roomRequest.getSize());
        room.setHomestay(homestay);
        room = roomDomainRepository.save(room);
        return getRoomResponse(save(room));
    }

    @Override
    public RoomResponse updateRoomPrice(Long roomId, int dailyPrice, int weekendPrice) {
        Room room = roomDomainRepository.getById(roomId);
        room.setDailyPrice(dailyPrice);
        room.setWeekendPrice(weekendPrice);
        return getRoomResponse(update(room));
    }

    @Override
    public RoomResponse update(Long roomId, RoomRequest roomRequest) {
        Room room = roomDomainRepository.getById(roomId);
        room.setName(roomRequest.getName());
        room.setDailyPrice(roomRequest.getDailyPrice());
        room.setWeekendPrice(roomRequest.getWeekendPrice());
        room.setStatus(roomRequest.getStatus());
        room.setBeds(roomRequest.getBeds());
        room = roomDomainRepository.update(room);
        return getRoomResponse(room);
    }

    @Override
    public RoomResponse updateRoomDiscount(Long roomId, int weeklyDiscount, int monthlyDiscount) {
        Room room = roomDomainRepository.getById(roomId);
        Discount discount1 = Discount.builder()
                .value(weeklyDiscount)
                .type(DiscountType.WEEKLY)
                .room(room)
                .build();
        Discount discount2 = Discount.builder()
                .value(monthlyDiscount)
                .type(DiscountType.MONTHLY)
                .room(room)
                .build();
        discount1 = discountDomainRepository.save(discount1);
        discount2 = discountDomainRepository.save(discount2);
        if (room.getDiscounts() == null) {
            room.setDiscounts(new ArrayList<>());
        } else {
            room.getDiscounts().removeIf(discount ->
                    discount.getType() == DiscountType.WEEKLY || discount.getType() == DiscountType.MONTHLY);
            return getRoomResponse(update(room));
        }
        room.getDiscounts().add(discount1);
        room.getDiscounts().add(discount2);
        return getRoomResponse(update(room));
    }

    @Override
    public RoomResponse addCustomDiscount(Long roomId, DiscountRequest request) {
        Room room = roomDomainRepository.getById(roomId);
        validateRequest(room, request);
        Discount discount = Discount.builder()
                .value(request.getValue())
                .type(DiscountType.DAILY)
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .room(room)
                .build();
        discount = discountDomainRepository.save(discount);
        if (room.getDiscounts() == null) {
            room.setDiscounts(new ArrayList<>());
        }
        room.getDiscounts().add(discount);
        return getRoomResponse(update(room));
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room room = roomDomainRepository.getById(roomId);
        if (room == null) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }
        return getRoomResponse(room);
    }

    private void validateRequest(Room room, DiscountRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessException(ErrorCode.CHECKIN_AFTER_CHECKOUT);
        }
        if (request.getValue() < 0 || request.getValue() > 100) {
            throw new BusinessException(ErrorCode.INVALID_DISCOUNT);
        }
        if (room.getDiscounts() != null) {
            room.getDiscounts().forEach(d -> {
                if (request.getStartDate().isAfter(d.getStartDate().minusDays(1)) && request.getStartDate().isBefore(d.getEndDate())) {
                    throw new BusinessException(ErrorCode.OVERLAP_DISCOUNT);
                }
                if (request.getEndDate().isAfter(d.getStartDate()) && request.getEndDate().isBefore(d.getEndDate().minusDays(1))) {
                    throw new BusinessException(ErrorCode.OVERLAP_DISCOUNT);
                }
                if (request.getStartDate().isBefore(d.getStartDate()) && request.getEndDate().isAfter(d.getEndDate())) {
                    throw new BusinessException(ErrorCode.OVERLAP_DISCOUNT);
                }
            });
        }
    }

    private RoomResponse getRoomResponse(Room room) {
        RoomResponse response = roomMapper.toDTO(room);
        List<AmenityResponse> amenities = new ArrayList<>();
        if (room.getAmenities() != null) {
            room.getAmenities().forEach(a -> {
                amenities.add(AmenityResponse.builder()
                        .name(a.getName())
                        .build());
            });
        }
        response.setAmenities(amenities);
        return response;
    }
}
