package nnt.com.application.service.room;

import nnt.com.domain.aggregates.model.dto.request.DiscountRequest;
import nnt.com.domain.aggregates.model.dto.request.RoomRequest;
import nnt.com.domain.aggregates.model.dto.response.RoomResponse;

import java.util.List;

public interface RoomAppService {
    List<RoomResponse> getRoomsByHomestayId(Long homestayId);

    RoomResponse createRoom(RoomRequest roomRequest);

    RoomResponse updateRoom(Long roomId, RoomRequest roomRequest);

    void deleteRoom(Long roomId);

    RoomResponse updateRoomPrice(Long roomId, int dailyPrice, int weekendPrice);

    RoomResponse updateRoomDiscount(Long roomId, int weeklyDiscount, int monthlyDiscount);

    RoomResponse addCustomDiscount(Long roomId, DiscountRequest request);

    RoomResponse getRoomById(Long roomId);

    List<RoomResponse> getAvailableRoomsByHomestayId(Long homestayId, String checkIn, String checkOut);
}
