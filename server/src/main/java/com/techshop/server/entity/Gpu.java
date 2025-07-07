package com.techshop.server.entity;

import com.techshop.server.entity.base.BaseEntity;
import com.techshop.server.infrastructure.constant.MemoryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gpu")
@DynamicUpdate
public class Gpu extends BaseEntity {

    @Column(name = "model")
    private String model;

    @Column(name = "series")
    private String series;

    @Column(name = "memory_size")
    private Long memorySize;

    @Column(name = "memory_type")
    private MemoryType memoryType;

    @Column(name = "is_integrated")
    private boolean isIntegrated;

    @ManyToOne
    @JoinColumn(name = "id_manufacturer")
    private Manufacturer manufacturer;

}
