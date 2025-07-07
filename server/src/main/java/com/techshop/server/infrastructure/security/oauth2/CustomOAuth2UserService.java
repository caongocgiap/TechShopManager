package com.techshop.server.infrastructure.security.oauth2;

import com.techshop.server.entity.Role;
import com.techshop.server.entity.Staff;
import com.techshop.server.entity.StaffRole;
import com.techshop.server.infrastructure.constant.EntityStatus;
import com.techshop.server.infrastructure.exception.OAuth2AuthenticationProcessingException;
import com.techshop.server.infrastructure.security.repository.RoleAuthRepository;
import com.techshop.server.infrastructure.security.repository.StaffAuthRepository;
import com.techshop.server.infrastructure.security.repository.StaffRoleAuthRepository;
import com.techshop.server.infrastructure.security.user.OAuth2UserInfo;
import com.techshop.server.infrastructure.security.user.OAuth2UserInfoFactory;
import com.techshop.server.infrastructure.security.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final StaffAuthRepository staffAuthRepository;

    private final RoleAuthRepository roleAuthRepository;

    private final StaffRoleAuthRepository staffRoleAuthRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(
                        oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                        oAuth2User.getAttributes()
                );
        if (oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().isBlank()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Staff> staffOptional = staffAuthRepository.findByEmail(oAuth2UserInfo.getEmail());
        if (staffOptional.isPresent()) {
            Staff staff = staffOptional.get();
            Staff staffExist = (Staff) updateExistingUser(staff, oAuth2UserInfo);
            List<String> roles = roleAuthRepository.findRoleByStaffId(staffExist.getId());
            return UserPrincipal.create(staffExist, oAuth2User.getAttributes(), roles);
        }

        return UserPrincipal.create(registerNewUser(oAuth2UserInfo));
    }

    private Staff registerNewUser(OAuth2UserInfo oAuth2UserInfo) {
        String email = oAuth2UserInfo.getEmail();
        Staff staff = new Staff();
        staff.setEmail(email);
        staff.setStaffCode(email.substring(0, email.indexOf("@")));
        staff.setAvatar(oAuth2UserInfo.getImageUrl());
        staff.setFullName(oAuth2UserInfo.getName());
        staff.setEntityStatus(EntityStatus.NOT_DELETED);
        Role role;
        if (email.equals("giaptapcode.dev@gmail.com")) {
            role = createRoleStaffIfNotFound("ADMIN");
        } else if (email.endsWith("@gmail.com")) {
            role = createRoleStaffIfNotFound("MEMBER");
        } else {
            throw new OAuth2AuthenticationProcessingException("Invalid email format");
        }
        StaffRole staffRole = new StaffRole();
        staffRole.setStaff(staffAuthRepository.save(staff));
        staffRole.setRole(role);
        staffRoleAuthRepository.save(staffRole);
        System.out.println("Staff: " + staff);
        return staffAuthRepository.save(staff);
    }

    private Object updateExistingUser(Staff existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFullName(oAuth2UserInfo.getName());
        existingUser.setAvatar(oAuth2UserInfo.getImageUrl());
        if (existingUser.getEntityStatus() == null) existingUser.setEntityStatus(EntityStatus.NOT_DELETED);
        return staffAuthRepository.save(existingUser);
    }

    private Role createRoleStaffIfNotFound(String roleCode) {
        Optional<Role> role = roleAuthRepository.findRoleStaff(roleCode);
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setCode(roleCode);
            newRole.setName(roleCode);
            return roleAuthRepository.save(newRole);
        }
        return role.get();
    }

}
