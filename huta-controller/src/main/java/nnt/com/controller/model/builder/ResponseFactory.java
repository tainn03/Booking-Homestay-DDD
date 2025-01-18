package nnt.com.controller.model.builder;

import nnt.com.controller.model.response.ApiResponse;
import nnt.com.domain.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public interface ResponseFactory {
    ApiResponse create(HttpStatus status, Object data);

    ApiResponse create(Object data);

    ApiResponse create(ErrorCode errorCode);
}
