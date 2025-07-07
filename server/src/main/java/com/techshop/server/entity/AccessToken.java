package com.techshop.server.entity;

import com.techshop.server.entity.base.BaseTokenEntity;
import com.techshop.server.infrastructure.constant.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "access_token")
@DynamicUpdate
public class AccessToken extends BaseTokenEntity {

    @Column(name = "access_token", length = 8000)
    private String accessToken;

    @Column(name = "expired_at")
    private Long expiredAt;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(name = "revoked_at")
    private Long revokedAt;

}
