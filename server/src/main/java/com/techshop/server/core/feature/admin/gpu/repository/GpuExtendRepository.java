package com.techshop.server.core.feature.admin.gpu.repository;

import com.techshop.server.core.feature.admin.gpu.model.request.GpuPaginationRequest;
import com.techshop.server.core.feature.admin.gpu.model.response.GpuResponse;
import com.techshop.server.entity.Gpu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GpuExtendRepository extends JpaRepository<Gpu, String> {

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER(
            	ORDER BY g.created_at DESC) AS orderNumber,
            	g.id AS gpuId,
            	g.model AS gpuModel,
                m.name AS gpuManufacturer,
                g.series AS gpuSeries,
                g.memory_size AS gpuMemorySize,
                g.memory_type AS gpuMemoryType,
                g.is_integrated AS gpuIsIntegrated
            	g.soft_delete AS gpuStatus
            FROM 
            	gpu g
            LEFT JOIN manufacturer m ON g.id_manufacturer = m.id
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(g.model) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(g.manufacturer) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, countQuery = """
            SELECT
            	COUNT(g.id)
            FROM
            	gpu g
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(g.model) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(m.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, nativeQuery = true)
    Page<GpuResponse> search(Pageable pageable, @Param("req") GpuPaginationRequest req);

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER( 
            	ORDER BY c.created_at DESC) AS orderNumber, 
            	g.id AS gpuId,
            	g.model AS gpuModel,
                m.name AS gpuManufacturer,
                g.series AS gpuSeries,
                g.memory_size AS gpuMemorySize,
                g.memory_type AS gpuMemoryType,
                g.is_integrated AS gpuIsIntegrated
            	g.soft_delete AS gpuStatus
            FROM 
            	gpu g
            LEFT JOIN manufacturer m ON g.id_manufacturer = m.id
            """, countQuery = """
            SELECT 
            	COUNT(g.id) 
            FROM 
            	gpu g
            """, nativeQuery = true)
    Page<GpuResponse> search(Pageable pageable);

    Boolean existsByModel(String model);

}
