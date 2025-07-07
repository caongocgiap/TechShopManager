package com.techshop.server.core.feature.admin.brand.repository;

import com.techshop.server.core.feature.admin.brand.model.request.BrandPaginationRequest;
import com.techshop.server.core.feature.admin.brand.model.response.BrandResponse;
import com.techshop.server.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandExtendRepository extends BrandRepository {

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER(ORDER BY b.created_at DESC) AS orderNumber,
            	b.id as brandId,
            	b.code as brandCode,
            	b.name as brandName,
            	b.soft_delete as brandStatus
            FROM 
            	brand b 
            WHERE 
                (:#{#request.brandCode} IS NULL OR b.code LIKE CONCAT('%', :#{#request.brandCode}, '%'))
            OR  (:#{#request.brandName} IS NULL OR b.name LIKE CONCAT('%', :#{#request.brandName}, '%'))
            """, countQuery = """
            SELECT
            	COUNT(b.id)
            FROM
            	brand b
            WHERE 
                (:#{#request.brandCode} IS NULL OR b.code LIKE CONCAT('%', :#{#request.brandCode}, '%'))
            OR  (:#{#request.brandName} IS NULL OR b.name LIKE CONCAT('%', :#{#request.brandName}, '%'))
            """, nativeQuery = true)
    Page<BrandResponse> search(Pageable pageable, @Param("request") BrandPaginationRequest request);

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER( 
            	ORDER BY b.created_at DESC) AS orderNumber, 
            	b.id as brandId, 
            	b.code as brandCode,
            	b.name as brandName, 
            	b.soft_delete as brandStatus 
            FROM 
            	brand b 
            """, countQuery = """
            SELECT 
            	COUNT(b.id) 
            FROM 
            	brand b 
            """, nativeQuery = true)
    Page<BrandResponse> search(Pageable pageable);

    Boolean existsByCode(String code);

}
