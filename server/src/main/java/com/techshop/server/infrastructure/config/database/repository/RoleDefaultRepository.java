package com.techshop.server.infrastructure.config.database.repository;

import com.techshop.server.entity.Role;
import com.techshop.server.repository.RoleRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDefaultRepository extends RoleRepository {

    Optional<Role> findByCode(String ma);

}
