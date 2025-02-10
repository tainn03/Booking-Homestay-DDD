package nnt.com.controller.model.builder;

import nnt.com.controller.model.vo.ApiResponse;
import nnt.com.domain.shared.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public interface ResponseFactory {
    ApiResponse create(HttpStatus status, Object data);

    ApiResponse create(Object data);

    ApiResponse create(ErrorCode errorCode);
}
