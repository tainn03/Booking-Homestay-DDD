package nnt.com.application.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // code convention: http status code + sequence number
    SUCCESS(20000, "Thành công", HttpStatus.OK),

    // SYSTEM ERRORS
    INVALID_CREDENTIALS(40101, "Thông tin đăng nhập không chính xác", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(40301, "Không có quyền truy cập", HttpStatus.FORBIDDEN),
    NO_RESOURCE_FOUND(40401, "Không tìm thấy địa chỉ api", HttpStatus.NOT_FOUND),
    UNCATEGORIZED(50001, "Lỗi hệ thống, vui lòng kiểm tra logs", HttpStatus.INTERNAL_SERVER_ERROR),

    // SERVICE ERRORS
    SERVICE_UNAVAILABLE(50301, "Dịch vụ không khả dụng, ngắt kết nối", HttpStatus.SERVICE_UNAVAILABLE),
    FALLBACK(50302, "Fallback", HttpStatus.SERVICE_UNAVAILABLE),

    // VALIDATION ERRORS
    INVALID_KEY(40001, "Lỗi không xác định", HttpStatus.BAD_REQUEST),
    LOCAL_CACHE_ERROR(40002, "Lỗi cache local", HttpStatus.BAD_REQUEST),
    INVALID_ENUM_PATTERN(40003, "Mẫu enum không hợp lệ", HttpStatus.BAD_REQUEST),

    // LOCK ERRORS
    DISTRIBUTED_LOCKING(40901, "Đối tượng đang được sử dụng, vui lòng thử lại sau", HttpStatus.CONFLICT),
    OPTIMISTIC_LOCKING(40902, "Đối tượng đã được cập nhật, vui lòng thử lại sau", HttpStatus.CONFLICT),

    // RATE LIMIT ERRORS
    TOO_MANY_REQUESTS(42901, "Truy cập quá thường xuyên, vui lòng thử lại sau", HttpStatus.TOO_MANY_REQUESTS),

    // BUSINESS ERRORS
    OBJECT_NOT_FOUND(40402, "Đối tượng không tồn tại", HttpStatus.NOT_FOUND),

    NAME_REQUIRED(40004, "Tên không được để trống", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}
