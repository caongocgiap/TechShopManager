package com.techshop.server.core.feature.admin.category.repository;

import com.techshop.server.core.feature.admin.category.model.request.CategoryPaginationRequest;
import com.techshop.server.core.feature.admin.category.model.response.CategoryResponse;
import com.techshop.server.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryExtendRepository extends JpaRepository<Category, String> {

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER(
            	ORDER BY c.created_at DESC) AS orderNumber,
            	c.id as categoryId,
            	c.code as categoryCode,
            	c.name as categoryName,
            	c.soft_delete as categoryStatus
            FROM 
            	category c
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(c.code) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(c.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, countQuery = """
            SELECT
            	COUNT(c.id)
            FROM
            	category c
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(c.code) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(c.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, nativeQuery = true)
    Page<CategoryResponse> search(Pageable pageable, @Param("req") CategoryPaginationRequest req);

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER( 
            	ORDER BY c.created_at DESC) AS orderNumber, 
            	c.id as categoryId, 
            	c.code as categoryCode,
            	c.name as categoryName, 
            	c.soft_delete as categoryStatus 
            FROM 
            	category c
            """, countQuery = """
            SELECT 
            	COUNT(c.id) 
            FROM 
            	category c
            """, nativeQuery = true)
    Page<CategoryResponse> search(Pageable pageable);

    Boolean existsByCode(String code);

}
