package com.techshop.server.repository;

import com.techshop.server.entity.HardDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardDriveRepository extends JpaRepository<HardDrive, String> {
}
