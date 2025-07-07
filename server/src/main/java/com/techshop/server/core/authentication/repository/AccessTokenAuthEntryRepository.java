package com.techshop.server.core.authentication.repository;

import com.techshop.server.entity.AccessToken;
import com.techshop.server.infrastructure.constant.UserType;
import com.techshop.server.repository.AccessTokenRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenAuthEntryRepository extends AccessTokenRepository {

    @Query(value = """
            SELECT revoked_at
            FROM access_token ac
            WHERE ac.user_id = :userId
            """, nativeQuery = true)
    Boolean isRevoked(String userId);

    @Query(value = """
            SELECT access_token
            FROM access_token ac
            WHERE ac.user_id = :userId
            """, nativeQuery = true)
    String getAccessTokenByUserId(String userId);

    Optional<AccessToken> findByUserId(String userId);

    @Modifying
    @Query("""
            DELETE FROM AccessToken ac
            WHERE ac.userId = :userId AND ac.userType = :userType
            """)
    void deleteByUserId(String userId, UserType userType);

}
