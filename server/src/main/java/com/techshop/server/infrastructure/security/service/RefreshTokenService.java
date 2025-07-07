package com.techshop.server.infrastructure.security.service;

import com.techshop.server.entity.RefreshToken;
import com.techshop.server.entity.Staff;
import com.techshop.server.infrastructure.constant.UserType;
import com.techshop.server.infrastructure.security.repository.RefreshTokenAuthRepository;
import com.techshop.server.infrastructure.security.repository.StaffAuthRepository;
import com.techshop.server.infrastructure.security.user.UserPrincipal;
import com.techshop.server.util.DateTimeUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    // 6 hours
    private final long REFRESH_EXPIRED_TIME = 6 * 60 * 60 * 1000;

    private final RefreshTokenAuthRepository refreshTokenAuthRepository;

    private final StaffAuthRepository staffAuthRepository;

    @Value("${email.admin}")
    private String EMAIL_ADMIN;

    @Autowired
    public RefreshTokenService(
            RefreshTokenAuthRepository refreshTokenAuthRepository,
            StaffAuthRepository staffAuthRepository
    ) {
        this.refreshTokenAuthRepository = refreshTokenAuthRepository;
        this.staffAuthRepository = staffAuthRepository;
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        return refreshTokenAuthRepository.findByRefreshToken(refreshToken);
    }

    public RefreshToken createRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserType userType = UserType.ADMIN;

//        Optional<Staff> staff = staffAuthRepository.findByEmail(userPrincipal.getEmail());
//        if (staff.isEmpty()) {
//            userType = UserType.MEMBER;
//        }
        if(!userPrincipal.getEmail().equals(EMAIL_ADMIN)) {
            userType = UserType.MEMBER;
        }

        Optional<RefreshToken> optionalRefreshToken = refreshTokenAuthRepository.findByUserIdAndUserType(userPrincipal.getId(), userType);

        if (optionalRefreshToken.isPresent()) {
            if (optionalRefreshToken.get().getRevokedAt() != null) {
                optionalRefreshToken.get().setRevokedAt(null);
                optionalRefreshToken.get().setUserType(userType);
                optionalRefreshToken.get().setUpdatedAt(DateTimeUtil.convertDateToTimeStampSecond(new Date()));
                optionalRefreshToken.get().setExpiredAt(REFRESH_EXPIRED_TIME);
                optionalRefreshToken.get().setRefreshToken(UUID.randomUUID().toString());
                return refreshTokenAuthRepository.save(optionalRefreshToken.get());
            }
            RefreshToken refreshToken = optionalRefreshToken.get();
            refreshToken.setUpdatedAt(DateTimeUtil.convertDateToTimeStampSecond(new Date()));
            refreshToken.setExpiredAt(REFRESH_EXPIRED_TIME);
            refreshToken.setRefreshToken(UUID.randomUUID().toString());
            optionalRefreshToken.get().setUserType(userType);
            refreshToken = refreshTokenAuthRepository.save(refreshToken);
            return refreshToken;
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreatedAt(DateTimeUtil.convertDateToTimeStampSecond(new Date()));
        refreshToken.setUserId(userPrincipal.getId());
        refreshToken.setUserType(userType);
        refreshToken.setExpiredAt(REFRESH_EXPIRED_TIME);
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenAuthRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken createRefreshToken(String userId, UserType userType) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenAuthRepository.findByUserIdAndUserType(userId, userType);
        if (optionalRefreshToken.isPresent()) {
            if (optionalRefreshToken.get().getRevokedAt() != null) {
                optionalRefreshToken.get().setRevokedAt(null);
                optionalRefreshToken.get().setUpdatedAt(DateTimeUtil.convertDateToTimeStampSecond(new Date()));
                optionalRefreshToken.get().setExpiredAt(REFRESH_EXPIRED_TIME);
                optionalRefreshToken.get().setRefreshToken(UUID.randomUUID().toString());
                return refreshTokenAuthRepository.save(optionalRefreshToken.get());
            }
            RefreshToken refreshToken = optionalRefreshToken.get();
            refreshToken.setUpdatedAt(DateTimeUtil.convertDateToTimeStampSecond(new Date()));
            refreshToken.setExpiredAt(REFRESH_EXPIRED_TIME);
            refreshToken.setRefreshToken(UUID.randomUUID().toString());
            return refreshTokenAuthRepository.save(refreshToken);
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreatedAt(DateTimeUtil.convertDateToTimeStampSecond(new Date()));
        refreshToken.setUserId(userId);
        refreshToken.setExpiredAt(REFRESH_EXPIRED_TIME);
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenAuthRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiredAt() < DateTimeUtil.convertDateToTimeStampSecond(new Date())) {
            refreshTokenAuthRepository.delete(token);
            return null;
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(String userId) {
        return refreshTokenAuthRepository.deleteByUserId(userId);
    }

}
