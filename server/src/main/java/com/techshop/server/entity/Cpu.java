package com.techshop.server.entity;

import com.techshop.server.entity.base.BaseEntity;
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
@Table(name = "cpu")
@DynamicUpdate
public class Cpu extends BaseEntity {

    @Column(name = "model")
    private String model; // i5-12400F,...

    @Column(name = "generation")
    private String generation; // 12th Gen, Zen 3,...

    @Column(name = "cores")
    private Long cores;

    @Column(name = "threads")
    private Long threads;

    @Column(name = "base_clock")
    private double baseClock;

    @Column(name = "turbo_clock")
    private double turboClock;

    @Column(name = "cache_size")
    private Long cacheSize;

    @Column(name = "tdp_watt")
    private Long tdpWatt; // Công suất tiêu thụ (TDP), đơn vị Watt

    @ManyToOne
    @JoinColumn(name = "id_manufacturer")
    private Manufacturer manufacturer;

}
