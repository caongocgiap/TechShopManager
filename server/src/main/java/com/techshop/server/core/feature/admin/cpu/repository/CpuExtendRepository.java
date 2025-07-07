package com.techshop.server.core.feature.admin.cpu.repository;

import com.techshop.server.core.feature.admin.cpu.model.request.CpuPaginationRequest;
import com.techshop.server.core.feature.admin.cpu.model.response.CpuResponse;
import com.techshop.server.entity.Cpu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuExtendRepository extends JpaRepository<Cpu, String> {

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER(
            	ORDER BY c.created_at DESC) AS orderNumber,
            	c.id AS cpuId,
            	c.model AS cpuModel,
                m.name AS cpuManufacturerName,
                c.series AS cpuSeries,
                c.generation AS cpuGeneration,
                c.cores AS cpuCores,
                c.threads AS cpuThreads,
                c.base_clock AS cpuBaseClock,
                c.turbo_clock AS cpuTurboClock,
                c.cache_size AS cpuCacheSize,
                c.tdp_watt AS cpuTdpWatt,
                c.architecture AS cpuArchitecture,
                c.release_year AS cpuReleaseYear,
            	c.soft_delete AS cpuStatus
            FROM 
            	cpu c 
            LEFT JOIN manufacturer m ON c.id_manufacturer = m.id
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(c.model) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(m.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, countQuery = """
            SELECT
            	COUNT(c.id)
            FROM
            	cpu c
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(c.model) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(m.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, nativeQuery = true)
    Page<CpuResponse> search(Pageable pageable, @Param("req") CpuPaginationRequest req);

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER( 
            	ORDER BY c.created_at DESC) AS orderNumber, 
            	c.id AS cpuId,
            	c.model AS cpuModel,
                m.name AS cpuManufacturerName,
                c.series AS cpuSeries,
                c.generation AS cpuGeneration,
                c.cores AS cpuCores,
                c.threads AS cpuThreads,
                c.base_clock AS cpuBaseClock,
                c.turbo_clock AS cpuTurboClock,
                c.cache_size AS cpuCacheSize,
                c.tdp_watt AS cpuTdpWatt,
                c.architecture AS cpuArchitecture,
                c.release_year AS cpuReleaseYear,
            	c.soft_delete AS cpuStatus
            FROM 
            	cpu c
            LEFT JOIN manufacturer m ON c.id_manufacturer = m.id  
            """, countQuery = """
            SELECT 
            	COUNT(c.id) 
            FROM 
            	cpu c
            """, nativeQuery = true)
    Page<CpuResponse> search(Pageable pageable);

    Boolean existsByModel(String model);

}
