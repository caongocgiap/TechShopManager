package com.techshop.server.repository;

import com.techshop.server.entity.ScreenResolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenResolutionRepository extends JpaRepository<ScreenResolution, String> {
}
