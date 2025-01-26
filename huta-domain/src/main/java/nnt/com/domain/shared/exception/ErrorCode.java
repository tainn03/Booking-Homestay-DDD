package nnt.com.domain.shared.exception;

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
    UNCATEGORIZED(50001, "Lỗi hệ thống, vui lòng kiểm tra logs", HttpStatus.INTERNAL_SERVER_ERROR),

    // NOT FOUND ERRORS
    NO_RESOURCE_FOUND(40401, "Không tìm thấy địa chỉ api", HttpStatus.NOT_FOUND),
    HOMESTAY_NOT_FOUND(40402, "Không tìm thấy homestay", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(40403, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(40404, "Không tìm thấy vai trò", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(40405, "Không tìm thấy quyền", HttpStatus.NOT_FOUND),
    TOKEN_NOT_FOUND(40406, "Không tìm thấy token", HttpStatus.NOT_FOUND),
    IMAGE_NOT_FOUND(40407, "Không tìm thấy ảnh", HttpStatus.NOT_FOUND),
    RATING_NOT_FOUND(40408, "Không tìm thấy đánh giá", HttpStatus.NOT_FOUND),
    DISTRICT_NOT_FOUND(40409, "Không tìm thấy quận, huyện", HttpStatus.NOT_FOUND),
    CITY_NOT_FOUND(40410, "Không tìm thấy tỉnh, thành phố", HttpStatus.NOT_FOUND),
    TYPE_HOMESTAY_NOT_FOUND(40411, "Không tìm thấy loại homestay", HttpStatus.NOT_FOUND),
    BOOKING_NOT_FOUND(40412, "Không tìm thấy booking", HttpStatus.NOT_FOUND),
    CONVERSATION_NOT_FOUND(40413, "Không tìm thấy cuộc trò chuyện", HttpStatus.NOT_FOUND),
    MESSAGE_NOT_FOUND(40414, "Không tìm thấy tin nhắn", HttpStatus.NOT_FOUND),
    AMENITY_NOT_FOUND(40415, "Không tìm thấy tiện ích", HttpStatus.NOT_FOUND),
    DISCOUNT_NOT_FOUND(40416, "Không tìm thấy khuyến mãi", HttpStatus.NOT_FOUND),
    PRICE_CALENDAR_NOT_FOUND(40417, "Không tìm thấy lịch giá", HttpStatus.NOT_FOUND),
    PAYMENT_NOT_FOUND(40418, "Không tìm thấy thanh toán", HttpStatus.NOT_FOUND),
    REFUND_NOT_FOUND(40419, "Không tìm thấy hoàn tiền", HttpStatus.NOT_FOUND),

    // SERVICE ERRORS
    SERVICE_UNAVAILABLE(50301, "Dịch vụ không khả dụng, ngắt kết nối", HttpStatus.SERVICE_UNAVAILABLE),
    FALLBACK(50302, "Fallback", HttpStatus.SERVICE_UNAVAILABLE),

    // VALIDATION ERRORS
    INVALID_KEY(40001, "Lỗi không xác định", HttpStatus.BAD_REQUEST),
    LOCAL_CACHE_ERROR(40002, "Lỗi cache local", HttpStatus.BAD_REQUEST),
    INVALID_ENUM_PATTERN(40003, "Mẫu enum không hợp lệ", HttpStatus.BAD_REQUEST),
    JWT_INVALID(40004, "Token không hợp lệ do private key đã thay đổi", HttpStatus.BAD_REQUEST),

    // LOCK ERRORS
    DISTRIBUTED_LOCKING(40901, "Đối tượng đang được sử dụng, vui lòng thử lại sau", HttpStatus.CONFLICT),
    OPTIMISTIC_LOCKING(40902, "Đối tượng đã được cập nhật, vui lòng thử lại sau", HttpStatus.CONFLICT),

    // RATE LIMIT ERRORS
    TOO_MANY_REQUESTS(42901, "Truy cập quá thường xuyên, vui lòng thử lại sau", HttpStatus.TOO_MANY_REQUESTS),

    // BAD REQUEST ERRORS
    EMAIL_MUST_BE_VALID(40005, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    PASSWORD_MUST_BE_STRONG(40006, "Mật khẩu phải chứa ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số", HttpStatus.BAD_REQUEST),
    EMAIL_MUST_NOT_BE_BLANK(40007, "Email không được để trống", HttpStatus.BAD_REQUEST),
    NAME_MUST_NOT_BE_BLANK(40008, "Tên không được để trống", HttpStatus.BAD_REQUEST),
    PWD_MUST_NOT_BE_BLANK(40008, "Mật khẩu không được để trống", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(40009, "Mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(40010, "Token không hợp lệ", HttpStatus.BAD_REQUEST),
    NAME_REQUIRED(40011, "Tên không được để trống", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(40012, "Email đã tồn tại", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(40013, "Token đã hết hạn", HttpStatus.BAD_REQUEST),


    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}
