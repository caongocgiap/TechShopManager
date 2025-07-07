package com.techshop.server.infrastructure.security.repository;

import com.techshop.server.entity.Staff;
import com.techshop.server.repository.StaffRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffAuthRepository extends StaffRepository {

    Optional<Staff> findByEmail(String email);

}
