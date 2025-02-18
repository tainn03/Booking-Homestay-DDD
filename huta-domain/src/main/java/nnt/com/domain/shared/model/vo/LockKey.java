package nnt.com.domain.shared.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum LockKey {
    HOMESTAY("LOCK:HOMESTAY:"),
    BOOKING("LOCK:BOOKING:");

    String key;
}
