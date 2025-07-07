package com.techshop.server.infrastructure.security.repository;

import com.techshop.server.repository.StaffRoleRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRoleAuthRepository extends StaffRoleRepository {

    @Query(
            value = """
                    SELECT
                    	r.code
                    FROM
                    	staff_role sr
                    LEFT JOIN role r ON
                    	sr.id_role = r.id
                    LEFT JOIN staff s on sr.id_staff = r.id
                    WHERE
                    	r.id = :userId
                    """,
            nativeQuery = true
    )
    List<String> getListRoleCodeByUserId(String userId);

    @Query(
            value = """
                    SELECT
                    	r.name
                    FROM
                    	staff_role sr
                    LEFT JOIN role r ON
                    	sr.id_role = r.id
                    LEFT JOIN staff s on sr.id_staff = s.id
                    WHERE
                    	s.id = :userId
                    """,
            nativeQuery = true
    )
    List<String> getListRoleNameByUserId(String userId);

    @Modifying
    @Query("""
            DELETE StaffRole sr
            WHERE sr.staff.id = :staffId
            """)
    void deleteByStaffId(String staffId);

}
