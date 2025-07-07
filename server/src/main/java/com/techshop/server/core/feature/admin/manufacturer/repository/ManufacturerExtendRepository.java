package com.techshop.server.core.feature.admin.manufacturer.repository;

import com.techshop.server.core.feature.admin.manufacturer.model.request.ManufacturerPaginationRequest;
import com.techshop.server.core.feature.admin.manufacturer.model.response.AllManufacturerResponse;
import com.techshop.server.core.feature.admin.manufacturer.model.response.ManufacturerResponse;
import com.techshop.server.repository.ManufacturerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufacturerExtendRepository extends ManufacturerRepository {

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER(
            	ORDER BY m.created_at DESC) AS orderNumber,
            	m.id as manufacturerId,
            	m.code as manufacturerCode,
            	m.name as manufacturerName,
                m.country as manufacturerCountry,
                m.website as manufacturerWebsite,
                m.description as manufacturerDescription,
            	m.soft_delete as manufacturerStatus
            FROM 
            	manufacturer m
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(m.code) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(m.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, countQuery = """
            SELECT
            	COUNT(m.id)
            FROM
            	manufacturer m
            WHERE 
                (:#{#req.searchValues} IS NULL 
                 OR :#{#req.searchValues} = ''
                 OR LOWER(m.code) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%'))
                 OR LOWER(m.name) LIKE LOWER(CONCAT('%', :#{#req.searchValues}, '%')))
            """, nativeQuery = true)
    Page<ManufacturerResponse> search(Pageable pageable, @Param("req") ManufacturerPaginationRequest req);

    @Query(value = """
            SELECT
            	ROW_NUMBER() OVER( 
            	ORDER BY m.created_at DESC) AS orderNumber, 
            	m.id as manufacturerId,
            	m.code as manufacturerCode,
            	m.name as manufacturerName,
                m.country as manufacturerCountry,
                m.website as manufacturerWebsite,
                m.description as manufacturerDescription,
            	m.soft_delete as manufacturerStatus
            FROM 
            	manufacturer m
            """, countQuery = """
            SELECT 
            	COUNT(m.id) 
            FROM 
            	manufacturer m
            """, nativeQuery = true)
    Page<ManufacturerResponse> search(Pageable pageable);

    Boolean existsByCode(String code);

    @Query(value = """
        SELECT m.id AS manufacturerId, m.name AS manufacturerName FROM manufacturer m
    """, nativeQuery = true)
    List<AllManufacturerResponse> getAllManufacturers();

}
