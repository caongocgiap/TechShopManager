package com.techshop.server.repository;

import com.techshop.server.entity.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRoleRepository extends JpaRepository<StaffRole, String> {
}
