package com.techshop.server.core.authentication.repository;

import com.techshop.server.entity.RefreshToken;
import com.techshop.server.infrastructure.constant.UserType;
import com.techshop.server.repository.RefreshTokenRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenAuthEntryRepository extends RefreshTokenRepository {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Modifying
    @Query(value = """
            DELETE FROM RefreshToken rt
            WHERE rt.userId = :userId AND rt.userType = :userType
            """)
    void deleteByUserId(String userId, UserType userType);

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

}
