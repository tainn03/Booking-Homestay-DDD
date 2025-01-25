package nnt.com.controller.aop.exception;

import nnt.com.controller.model.response.ApiResponse;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ApiResponse> handleRunTimeException(Exception e) {
        ApiResponse response = ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED.getCode())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED.getStatusCode()).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ApiResponse> handleBusinessException(BusinessException e) {
        String enumKey = e.getErrorCode().name();
        return getResponseEntity(enumKey);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return getResponseEntity(enumKey);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ApiResponse> handleNoResourceFoundException(Exception e) {
        String enumKey = ErrorCode.NO_RESOURCE_FOUND.name();
        return getResponseEntity(enumKey);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    ResponseEntity<ApiResponse> handleObjectOptimisticLockingFailureException(Exception e) {
        String enumKey = ErrorCode.OPTIMISTIC_LOCKING.name();
        return getResponseEntity(enumKey);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(Exception e) {
        ApiResponse response = ApiResponse.builder()
                .code(ErrorCode.INVALID_ENUM_PATTERN.getCode())
                .message("Thông tin không hợp lệ: " + e.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.INVALID_ENUM_PATTERN.getStatusCode()).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse> handleAuthorizationDeniedException(Exception e) {
        String enumKey = ErrorCode.UNAUTHORIZED.name();
        return getResponseEntity(enumKey);
    }

    private ResponseEntity<ApiResponse> getResponseEntity(String enumKey) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        ApiResponse response;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException _) {
        }
        response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
}
