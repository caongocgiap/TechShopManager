package com.techshop.server.repository;

import com.techshop.server.entity.Imei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImeiRepository extends JpaRepository<Imei, String> {
}
