package nnt.com.application.model.enums;

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
    ;

    String key;
}
