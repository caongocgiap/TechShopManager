package com.techshop.server.core.feature.admin.operationsystem.repository;

import com.techshop.server.core.feature.admin.operationsystem.model.request.OperationSystemPaginationRequest;
import com.techshop.server.core.feature.admin.operationsystem.model.response.OperationSystemResponse;
import com.techshop.server.entity.OperationSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationSystemExtendRepository extends JpaRepository<OperationSystem, String> {

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER(
            	ORDER BY os.created_at DESC) AS orderNumber,
            	os.id as operationSystemId,
            	os.code as operationSystemCode,
            	os.name as operationSystemName,
            	os.soft_delete as operationSystemStatus            
            FROM 
            	operation_system os
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(os.code) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(os.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, countQuery = """
            SELECT
            	COUNT(os.id)
            FROM
            	operation_system os
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(os.code) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(os.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, nativeQuery = true)
    Page<OperationSystemResponse> search(Pageable pageable, @Param("req") OperationSystemPaginationRequest req);

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER( 
            	ORDER BY os.created_at DESC) AS orderNumber, 
            	os.id as operationSystemId, 
            	os.code as operationSystemCode,
            	os.name as operationSystemName, 
            	os.soft_delete as operationSystemStatus 
            FROM 
            	operation_system os
            """, countQuery = """
            SELECT 
            	COUNT(os.id) 
            FROM 
            	operation_system os
            """, nativeQuery = true)
    Page<OperationSystemResponse> search(Pageable pageable);

    Boolean existsByCode(String code);

}
