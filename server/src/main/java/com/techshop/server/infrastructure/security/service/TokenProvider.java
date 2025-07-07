package com.techshop.server.infrastructure.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techshop.server.entity.AccessToken;
import com.techshop.server.entity.Staff;
import com.techshop.server.infrastructure.constant.UserType;
import com.techshop.server.infrastructure.security.repository.AccessTokenAuthRepository;
import com.techshop.server.infrastructure.security.repository.StaffAuthRepository;
import com.techshop.server.infrastructure.security.repository.StaffRoleAuthRepository;
import com.techshop.server.infrastructure.security.response.TokenResponse;
import com.techshop.server.infrastructure.security.user.UserPrincipal;
import com.techshop.server.util.DateTimeUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class TokenProvider {

    @Setter
    private SecretKey tokenSecret;

    //Time: 2 hours for production
    private final long TOKEN_EXP = System.currentTimeMillis() + 2 * 60 * 60 * 1000;

    @Setter(onMethod_ = @Autowired)
    private StaffAuthRepository staffAuthRepository;

    @Setter(onMethod_ = @Autowired)
    private StaffRoleAuthRepository staffRoleAuthRepository;

    @Setter(onMethod_ = @Autowired)
    private AccessTokenAuthRepository accessTokenAuthRepository;

    @Value("${email.admin}")
    private String EMAIL_ADMIN;

    public String[] createToken(
            Authentication authentication,
            String host
    ) throws BadRequestException, JsonProcessingException {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Staff staff = getCurrentUserLogin(userPrincipal.getEmail());

        if (staff == null) throw new BadRequestException("User not found");

        TokenResponse tokenResponse = createTokenResponse(staff, host);
        Object[] tokenInfo = createJwtToken(tokenResponse);
        String accessToken = tokenInfo[0].toString();
        Date expiredAt = (Date) tokenInfo[1];
        UserType userType = userPrincipal.getEmail().equals(EMAIL_ADMIN) ? UserType.ADMIN : UserType.MEMBER;
        Optional<AccessToken> accessTokenOptional = accessTokenAuthRepository.findByUserId(userPrincipal.getId(), userType);
        if (accessTokenOptional.isPresent()) {
            AccessToken existingToken = accessTokenOptional.get();
            updateAccessToken(existingToken, accessToken, expiredAt, userType);
            return buildTokenResponse(accessToken, userType, staff);
        }
        AccessToken newToken = createAccessToken(userPrincipal.getId(), accessToken, expiredAt, userType);
        accessTokenAuthRepository.save(newToken);

        return buildTokenResponse(accessToken, userType, staff);
    }

    private TokenResponse createTokenResponse(
            Staff staff,
            String host
    ) {
        return buildStaffTokenResponse(staff, host);
    }

    private TokenResponse buildStaffTokenResponse(Staff staff, String host) {
        TokenResponse response = new TokenResponse();
        response.setFullName(staff.getFullName());
        response.setUserId(staff.getId());
        response.setUserCode(staff.getStaffCode());
        response.setPictureUrl(staff.getAvatar());
        response.setUserType(staff.getEmail().equals(EMAIL_ADMIN) ? UserType.ADMIN.name() : UserType.MEMBER.name());
        response.setEmail(staff.getEmail());

        List<String> rolesCode = staffRoleAuthRepository.getListRoleCodeByUserId(staff.getId());
        if (rolesCode.isEmpty()) {
            if(staff.getEmail().equals(EMAIL_ADMIN)) {
                response.setRolesCode(Collections.singletonList("ADMIN"));
                response.setRolesName(Collections.singletonList("ADMIN"));
            } else {
                response.setRolesCode(Collections.singletonList("MEMBER"));
                response.setRolesName(Collections.singletonList("MEMBER"));
            }
        } else {
            response.setRolesCode(rolesCode);
            response.setRolesName(staffRoleAuthRepository.getListRoleNameByUserId(staff.getId()));
        }
        response.setHost(host);
        return response;
    }

    private void updateAccessToken(AccessToken token, String accessToken, Date expiredAt, UserType userType) {
        token.setAccessToken(accessToken);
        token.setUpdatedAt(DateTimeUtil.convertDateToTimeStampSecond(new Date()));
        token.setExpiredAt(DateTimeUtil.convertDateToTimeStampSecond(expiredAt));
        token.setUserType(userType);
        token.setRevokedAt(null);
        accessTokenAuthRepository.save(token);
    }

    private AccessToken createAccessToken(String userId, String accessToken, Date expiredAt, UserType userType) {
        AccessToken token = new AccessToken();
        token.setExpiredAt(DateTimeUtil.convertDateToTimeStampSecond(expiredAt));
        token.setCreatedAt(DateTimeUtil.convertDateToTimeStampSecond(new Date()));
        token.setAccessToken(accessToken);
        token.setUserType(userType);
        token.setUserId(userId);
        return token;
    }

    private String[] buildTokenResponse(String accessToken, UserType userType, Staff staff) {
        String userStatus = staff.getEntityStatus().name();
        return new String[]{accessToken, userType.name(), userStatus};
    }

    public String createToken(String userId, String host, String userType) throws BadRequestException {

        TokenResponse tokenResponse = null;
        if (userType.equals("ADMIN")) {
            Optional<Staff> staffOptional = staffAuthRepository.findById(userId);
            if (staffOptional.isEmpty()) throw new BadRequestException("User not found");
            Staff staff = staffOptional.get();
            tokenResponse = new TokenResponse();
            tokenResponse.setFullName(staff.getFullName());
            tokenResponse.setUserId(staff.getId());
            tokenResponse.setUserCode(staff.getStaffCode());
            tokenResponse.setPictureUrl(staff.getAvatar());
            tokenResponse.setEmail(staff.getEmail());
            tokenResponse.setUserType("CAN_BO");
            List<String> rolesCode = staffRoleAuthRepository.getListRoleCodeByUserId(staff.getId());
            if (rolesCode.isEmpty()) {
                tokenResponse.setRolesCode(Collections.singletonList("ADMIN"));
                tokenResponse.setRolesName(Collections.singletonList("ADMIN"));
            } else {
                tokenResponse.setRolesCode(rolesCode);
                tokenResponse.setRolesName(staffRoleAuthRepository.getListRoleNameByUserId(staff.getId()));
            }
            tokenResponse.setHost(host);
        }

        Date now = new Date();
        Date expiryDate = new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000);
        JwtBuilder builder = Jwts.builder();
        assert tokenResponse != null;
        Map<String, Object> claims = getBodyClaims(tokenResponse);
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            builder.claim(entry.getKey(), entry.getValue());
        }
        builder.setClaims(claims);
        builder.setIssuedAt(now);
        builder.setExpiration(expiryDate);
        builder.signWith(tokenSecret);
        String accessToken = builder.compact();
        Optional<AccessToken> accessTokenOptional = accessTokenAuthRepository.findByUserId(userId, UserType.valueOf(userType));

        if (accessTokenOptional.isPresent()) {
            accessTokenOptional.get().setAccessToken(accessToken);
            accessTokenOptional.get().setUpdatedAt(DateTimeUtil.convertDateToTimeStampSecond(new Date()));
            accessTokenOptional.get().setExpiredAt(DateTimeUtil.convertDateToTimeStampSecond(expiryDate));
            accessTokenOptional.get().setUserType(UserType.valueOf(userType));
            accessTokenOptional.get().setRevokedAt(null);
            accessTokenAuthRepository.save(accessTokenOptional.get());
            return accessToken;
        }
        AccessToken token = new AccessToken();
        token.setExpiredAt(DateTimeUtil.convertDateToTimeStampSecond(expiryDate));
        token.setCreatedAt(DateTimeUtil.convertDateToTimeStampSecond(new Date()));
        token.setAccessToken(accessToken);
        token.setUserType(UserType.valueOf(userType));
        token.setUserId(userId);
        AccessToken accessTokenSaved = accessTokenAuthRepository.save(token);
        return accessTokenSaved.getAccessToken();
    }

    private static Map<String, Object> getBodyClaims(TokenResponse tokenResponse) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("fullName", tokenResponse.getFullName());
        claims.put("userId", tokenResponse.getUserId());
        claims.put("userName", tokenResponse.getUserCode());
        claims.put("userCode", tokenResponse.getUserCode());
        claims.put("email", tokenResponse.getEmail());
        claims.put("pictureUrl", tokenResponse.getPictureUrl());
        claims.put("userType", tokenResponse.getUserType());
        List<String> rolesCode = tokenResponse.getRolesCode();
        List<String> rolesName = tokenResponse.getRolesName();
        if (rolesCode.size() == 1) {
            claims.put("rolesCode", rolesCode.get(0));
        } else {
            claims.put("rolesCode", rolesCode);
        }
        if (rolesName.size() == 1) {
            claims.put("rolesName", rolesName.get(0));
        } else {
            claims.put("rolesName", rolesName);
        }
        claims.put("host", tokenResponse.getHost());
        return claims;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.get("userId").toString());
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("email").toString();
    }

    public String getHostFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        TokenResponse tokenSubjectResponse = null;
        try {
            tokenSubjectResponse = objectMapper.readValue(claims.getSubject(), TokenResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out
            );
        }
        assert tokenSubjectResponse != null;
        return tokenSubjectResponse.getHost();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(tokenSecret)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    private Staff getCurrentUserLogin(String email) {
        Optional<Staff> staffOptional = staffAuthRepository.findByEmail(email);
        return staffOptional.orElse(null);
    }

    private Object[] createJwtToken(TokenResponse tokenResponse) {
        Date now = new Date();
        Date expiryDate = new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000);
        JwtBuilder builder = Jwts.builder();
        Map<String, Object> claims = getBodyClaims(tokenResponse);
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            builder.claim(entry.getKey(), entry.getValue());
        }
        builder.setClaims(claims);
        builder.setIssuedAt(now);
        builder.setExpiration(expiryDate);
        builder.signWith(tokenSecret);
        return new Object[]{
                builder.compact(),
                expiryDate
        };
    }

}
