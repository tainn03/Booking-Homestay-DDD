package nnt.com.domain.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest implements Serializable {
    private String to;
    private String subject;
    private String templateId; // Optional: tên template nếu sử dụng email template
    private Map<String, String> templateParams; // Tham số cho template
}
