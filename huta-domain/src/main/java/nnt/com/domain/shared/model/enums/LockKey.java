package nnt.com.domain.shared.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum LockKey {
    HOMESTAY("LOCK:HOMESTAY:");

    String key;
}
