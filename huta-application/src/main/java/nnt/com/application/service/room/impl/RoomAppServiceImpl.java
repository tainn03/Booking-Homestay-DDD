package nnt.com.application.service.room.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.application.service.room.RoomAppService;
import nnt.com.domain.aggregates.model.dto.request.DiscountRequest;
import nnt.com.domain.aggregates.model.dto.request.RoomRequest;
import nnt.com.domain.aggregates.model.dto.response.RoomResponse;
import nnt.com.domain.aggregates.service.RoomDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoomAppServiceImpl implements RoomAppService {
    RoomDomainService roomDomainService;

    @Override
    public List<RoomResponse> getRoomsByHomestayId(Long homestayId) {
        return roomDomainService.getByHomestayId(homestayId);
    }

    @Override
    public RoomResponse createRoom(RoomRequest roomRequest) {
        return roomDomainService.save(roomRequest);
    }

    @Override
    public RoomResponse updateRoom(Long roomId, RoomRequest roomRequest) {
        return roomDomainService.update(roomId, roomRequest);
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomDomainService.delete(roomId);
    }

    @Override
    public RoomResponse updateRoomPrice(Long roomId, int dailyPrice, int weekendPrice) {
        return roomDomainService.updateRoomPrice(roomId, dailyPrice, weekendPrice);
    }

    @Override
    public RoomResponse updateRoomDiscount(Long roomId, int weeklyDiscount, int monthlyDiscount) {
        if (weeklyDiscount < 0 || monthlyDiscount < 0 || weeklyDiscount > 100 || monthlyDiscount > 100) {
            log.info("INVALID DISCOUNT DUE TO VALUE: {}, {}", weeklyDiscount, monthlyDiscount);
            throw new BusinessException(ErrorCode.INVALID_DISCOUNT);
        }
        return roomDomainService.updateRoomDiscount(roomId, weeklyDiscount, monthlyDiscount);
    }

    @Override
    public RoomResponse addCustomDiscount(Long roomId, DiscountRequest request) {
        return roomDomainService.addCustomDiscount(roomId, request);
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        return roomDomainService.getRoomById(roomId);
    }

    @Override
    public List<RoomResponse> getAvailableRoomsByHomestayId(Long homestayId, String checkIn, String checkOut) {
        return roomDomainService.getAvailableRoomsByHomestayId(homestayId, checkIn, checkOut);
    }
}
