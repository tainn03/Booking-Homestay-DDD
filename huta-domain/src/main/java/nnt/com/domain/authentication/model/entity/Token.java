package nnt.com.domain.authentication.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.authentication.model.enums.TokenType;
import nnt.com.domain.base.model.entity.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token extends BaseEntity<Long> {
    @Column(length = 1024)
    private String token;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TokenType tokenType = TokenType.BEARER;

    private boolean expired;

    private boolean revoked; // Thêm trường revoked để kiểm tra token đã bị thu hồi hay chưa

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
