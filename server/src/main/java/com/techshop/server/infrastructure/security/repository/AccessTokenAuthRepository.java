package com.techshop.server.infrastructure.security.repository;

import com.techshop.server.entity.AccessToken;
import com.techshop.server.infrastructure.constant.UserType;
import com.techshop.server.repository.AccessTokenRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenAuthRepository extends AccessTokenRepository {

    @Query(
            value = """
                    SELECT revoked_at
                    FROM access_token ac
                    WHERE ac.user_id = :userId
                    """,
            nativeQuery = true
    )
    Long isRevoked(String userId);

    @Query(
            value = """
                    SELECT access_token
                    FROM access_token ac
                    WHERE ac.user_id = :userId
                    """,
            nativeQuery = true
    )
    String getAccessTokenByUserId(String userId);

    @Query(
            """
            SELECT ac
            FROM AccessToken ac
            WHERE ac.userId = :userId AND ac.userType = :userType
            """
    )
    Optional<AccessToken> findByUserId(String userId, UserType userType);

}

