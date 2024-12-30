package nnt.com.controller.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.application.exception.ErrorCode;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    @Builder.Default
    int code = ErrorCode.SUCCESS.getCode();
    @Builder.Default
    String message = ErrorCode.SUCCESS.getMessage();
    @Builder.Default
    String timestamp = LocalDateTime.now().toString();

    Object payload;
}
