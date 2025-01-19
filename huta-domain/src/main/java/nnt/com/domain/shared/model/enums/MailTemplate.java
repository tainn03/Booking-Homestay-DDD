package nnt.com.domain.shared.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Getter
public enum MailTemplate {
    DEMO_TEMPLATE("d-2cebe65b02e545ebaaf91cd34d92a8a7");

    String value;
}
