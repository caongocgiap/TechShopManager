package com.techshop.server.core.feature.admin.screenresolution.repository;

import com.techshop.server.core.feature.admin.screenresolution.model.request.ScreenResolutionPaginationRequest;
import com.techshop.server.core.feature.admin.screenresolution.model.response.ScreenResolutionResponse;
import com.techshop.server.entity.ScreenResolution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenResolutionExtendRepository extends JpaRepository<ScreenResolution, String> {

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER(
            	ORDER BY sr.created_at DESC) AS orderNumber,
            	sr.id as screenResolutionId,
            	sr.code as screenResolutionCode,
            	sr.name as screenResolutionName,
                sr.width as screenResolutionWidth,
                sr.height as screenResolutionHeight,
                sr.aspect_ratio as screenResolutionAspectRatio,
            	sr.soft_delete as screenResolutionStatus            
            FROM 
            	screen_resolution sr
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(sr.code) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(sr.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(sr.aspect_ratio) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, countQuery = """
            SELECT
            	COUNT(sr.id)
            FROM
            	screen_resolution sr
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(sr.code) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(sr.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(sr.aspect_ratio) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, nativeQuery = true)
    Page<ScreenResolutionResponse> search(Pageable pageable, @Param("req") ScreenResolutionPaginationRequest req);

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER( 
            	ORDER BY sr.created_at DESC) AS orderNumber, 
            	sr.id as screenResolutionId, 
            	sr.code as screenResolutionCode,
            	sr.name as screenResolutionName,
                sr.width as screenResolutionWidth,
                sr.height as screenResolutionHeight,
                sr.aspect_ratio as screenResolutionAspectRatio,
            	sr.soft_delete as screenResolutionStatus 
            FROM 
            	screen_resolution sr
            """, countQuery = """
            SELECT 
            	COUNT(sr.id) 
            FROM 
            	screen_resolution sr
            """, nativeQuery = true)
    Page<ScreenResolutionResponse> search(Pageable pageable);

    Boolean existsByCode(String code);

}
