package com.techshop.server.repository;

import com.techshop.server.entity.OperationSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationSystemRepository extends JpaRepository<OperationSystem, String> {
}
