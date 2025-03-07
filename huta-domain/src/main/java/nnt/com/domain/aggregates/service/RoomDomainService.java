package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.dto.request.DiscountRequest;
import nnt.com.domain.aggregates.model.dto.request.RoomRequest;
import nnt.com.domain.aggregates.model.dto.response.RoomResponse;
import nnt.com.domain.aggregates.model.entity.Discount;
import nnt.com.domain.aggregates.model.entity.Room;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface RoomDomainService extends BaseBehaviors<Room, Long> {
    List<RoomResponse> getByHomestayId(Long homestayId);

    RoomResponse save(RoomRequest roomRequest);

    RoomResponse updateRoomPrice(Long roomId, int dailyPrice, int weekendPrice);

    RoomResponse update(Long roomId, RoomRequest roomRequest);

    RoomResponse updateRoomDiscount(Long roomId, int weeklyDiscount, int monthlyDiscount);

    RoomResponse addCustomDiscount(Long roomId, DiscountRequest request);

    RoomResponse getRoomById(Long roomId);

    List<RoomResponse> getAvailableRoomsByHomestayId(Long homestayId, String checkIn, String checkOut);

    List<Discount> getCustomDiscounts(Long roomId);

    Discount updateCustomDiscount(Long roomId, Long discountId, DiscountRequest request);

    void deleteCustomDiscount(Long roomId, Long discountId);
}
