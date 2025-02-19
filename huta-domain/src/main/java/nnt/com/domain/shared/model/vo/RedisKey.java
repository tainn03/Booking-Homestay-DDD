package nnt.com.domain.shared.model.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum RedisKey {
    HOMESTAY("HOMESTAY:"),
    ALL_HOMESTAY("HOMESTAY:ALL"),
    BOOKING("BOOKING:"),
    ROOM_AVAILABILITY("ROOM_AVAILABILITY:"),
    PRICE("PRICE:");

    String key;
}
