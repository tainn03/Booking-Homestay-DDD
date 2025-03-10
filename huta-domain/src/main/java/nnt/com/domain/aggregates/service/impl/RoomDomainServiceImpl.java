package nnt.com.domain.aggregates.service.impl;

import jakarta.transaction.Transactional;
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
import nnt.com.domain.aggregates.repository.RoomAvailableDomainRepository;
import nnt.com.domain.aggregates.repository.RoomDomainRepository;
import nnt.com.domain.aggregates.service.RoomDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RoomDomainServiceImpl implements RoomDomainService {
    private static final Logger log = LoggerFactory.getLogger(RoomDomainServiceImpl.class);
    RoomDomainRepository roomDomainRepository;
    HomestayDomainRepository homestayDomainRepository;
    DiscountDomainRepository discountDomainRepository;
    RoomAvailableDomainRepository roomAvailableDomainRepository;
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
        Discount discount = createDiscount(request, room);
        if (room.getDiscounts() == null) {
            room.setDiscounts(new ArrayList<>());
        }
        room.getDiscounts().add(discount);
        return getRoomResponse(update(room));
    }

    private Discount createDiscount(DiscountRequest request, Room room) {
        Discount discount = Discount.builder()
                .value(request.getValue())
                .type(DiscountType.DAILY)
                .status(request.getStatus())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .room(room)
                .build();
        return discountDomainRepository.save(discount);
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room room = roomDomainRepository.getById(roomId);
        if (room == null) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }
        return getRoomResponse(room);
    }

    @Override
    public List<RoomResponse> getAvailableRoomsByHomestayId(Long homestayId, String checkIn, String checkOut) {
        validateDate(checkIn, checkOut);
        List<Room> rooms = roomDomainRepository.getByHomestayId(homestayId);
        List<RoomResponse> response = new ArrayList<>();
        rooms.forEach(r -> {
            RoomResponse roomResponse = getRoomResponse(r);
            boolean isAvailable = true;
            if (r.getRoomAvailables() != null) {
                isAvailable = roomAvailableDomainRepository.isRoomAvailable(r.getId(), LocalDate.parse(checkIn), LocalDate.parse(checkOut));
                log.info("Room {} is available: {}", r.getId(), isAvailable);
            }
            roomResponse.setAvailable(isAvailable);
            response.add(roomResponse);
        });
        return response;
    }

    @Override
    public List<Discount> getCustomDiscounts(Long roomId) {
        Room room = roomDomainRepository.getById(roomId);
        if (room == null) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }
        return room.getDiscounts().stream().filter(d -> d.getType() == DiscountType.DAILY).toList();
    }

    @Override
    public Discount updateCustomDiscount(Long roomId, Long discountId, DiscountRequest request) {
        Room room = roomDomainRepository.getById(roomId);
        if (room == null) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }
        try {
            Discount discount = discountDomainRepository.getById(discountId);
            discount.setValue(request.getValue());
            discount.setStartDate(request.getStartDate());
            discount.setEndDate(request.getEndDate());
            discount.setStatus(request.getStatus());
            return discountDomainRepository.update(discount);
        } catch (Exception e) {
            validateRequest(room, request);
            return createDiscount(request, room);
        }
    }

    @Override
    @Transactional
    public void deleteCustomDiscount(Long roomId, Long discountId) {
        Room room = roomDomainRepository.getById(roomId);
        if (room == null) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }
        Discount discount = discountDomainRepository.getById(discountId);
        if (discount == null) {
            throw new BusinessException(ErrorCode.DISCOUNT_NOT_FOUND);
        }
        discountDomainRepository.delete(discountId);
    }

    private void validateDate(String checkIn, String checkOut) {
        try {
            LocalDate startDate = LocalDate.parse(checkIn);
            LocalDate endDate = LocalDate.parse(checkOut);
            if (startDate.isAfter(endDate)) {
                throw new BusinessException(ErrorCode.CHECKIN_AFTER_CHECKOUT);
            }
        } catch (Exception e) {
            log.warn("INVALID DATE FORMAT: {}, {}", checkIn, checkOut);
            throw new BusinessException(ErrorCode.INVALID_DATE);
        }
    }

    private void validateRequest(Room room, DiscountRequest request) {
        if (room == null) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }
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
                if (request.getStartDate().isEqual(d.getStartDate()) || request.getEndDate().isEqual(d.getEndDate())) {
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
