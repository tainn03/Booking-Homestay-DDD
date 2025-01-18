package nnt.com.domain.aggregates.user.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.user.model.enums.TokenType;
import nnt.com.domain.common.model.entity.BaseEntity;

@Entity
@Table(name = "tokens", indexes = {
        @Index(name = "idx_token", columnList = "token", unique = true)
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token extends BaseEntity<Long> {
    @Column(length = 1024)
    String token;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    TokenType tokenType = TokenType.BEARER;

    boolean expired;

    boolean revoked; // Thêm trường revoked để kiểm tra token đã bị thu hồi hay chưa

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
