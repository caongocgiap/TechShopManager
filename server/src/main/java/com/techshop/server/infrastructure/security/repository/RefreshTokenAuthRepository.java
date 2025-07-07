package com.techshop.server.infrastructure.security.repository;

import com.techshop.server.entity.RefreshToken;
import com.techshop.server.infrastructure.constant.UserType;
import com.techshop.server.repository.RefreshTokenRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenAuthRepository extends RefreshTokenRepository {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM refresh_token
            where user_id = :userId
            """, nativeQuery = true)
    int deleteByUserId(String userId);

    @Query(value = """
            SELECT revoked_at
            FROM refresh_token rt
            WHERE rt.user_id = :userId
            """, nativeQuery = true)
    Long isRevoked(String userId);


    @Query(value = """
            SELECT rt
            FROM RefreshToken rt
            WHERE rt.userId = :userId
            """)
    RefreshToken getRefreshTokenByUserId(String userId);

    Optional<RefreshToken> findByUserIdAndUserType(String userId, UserType userType);

}
