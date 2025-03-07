package nnt.com.controller.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.shared.exception.ErrorCode;

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
    Long executionTime = 0L;

    Object payload;
}
