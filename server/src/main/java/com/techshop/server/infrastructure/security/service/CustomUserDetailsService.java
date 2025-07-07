package com.techshop.server.infrastructure.security.service;

import com.techshop.server.entity.Staff;
import com.techshop.server.infrastructure.security.repository.RoleAuthRepository;
import com.techshop.server.infrastructure.security.repository.StaffAuthRepository;
import com.techshop.server.infrastructure.security.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final StaffAuthRepository staffAuthRepository;

    private final RoleAuthRepository roleAuthRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Staff> staff = staffAuthRepository.findByEmail(email);
        if (staff.isPresent()) {
            List<String> roles = roleAuthRepository.findRoleByStaffId(staff.get().getId());
            return UserPrincipal.create(staff.get(), roles);
        }
        throw new UsernameNotFoundException("User not found with email : " + email);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        Optional<Staff> staff = staffAuthRepository.findById(id);
        if (staff.isPresent()) {
            List<String> roles = roleAuthRepository.findRoleByStaffId(id);
            return UserPrincipal.create(staff.get(), roles);
        }
        throw new UsernameNotFoundException("User not found with id : " + id);
    }

}
