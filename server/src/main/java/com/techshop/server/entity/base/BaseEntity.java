package com.techshop.server.entity.base;

import java.io.Serializable;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.techshop.server.infrastructure.constant.EntityStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "soft_delete")
    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Long createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Long updatedAt;

}
