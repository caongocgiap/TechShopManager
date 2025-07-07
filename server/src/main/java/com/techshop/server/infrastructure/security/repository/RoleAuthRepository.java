package com.techshop.server.infrastructure.security.repository;

import com.techshop.server.entity.Role;
import com.techshop.server.repository.RoleRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleAuthRepository extends RoleRepository {

    @Query("SELECT r FROM Role r WHERE r.code = :roleCode")
    Optional<Role> findRoleStaff(String roleCode);

    @Query(value = """
            SELECT DISTINCT
                r.code
            FROM
                role r
            LEFT JOIN
             staff_role sr ON r.id = sr.id_role
            WHERE
                sr.id_staff = :id
            """, nativeQuery = true
    )
    List<String> findRoleByStaffId(String id);

}
