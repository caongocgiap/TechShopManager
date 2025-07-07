package com.techshop.server.entity;

import com.techshop.server.entity.base.BaseEntity;
import com.techshop.server.infrastructure.constant.HardDriveType;
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
@Table(name = "hard_drive")
@DynamicUpdate
public class HardDrive extends BaseEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private HardDriveType type;

    @Column(name = "capacity")
    private Long capacity;

    @Column(name = "read_speedMBps")
    private Long readSpeedMbps;

    @Column(name = "write_speedMBps")
    private Long writeSpeedMbps;

    @ManyToOne
    @JoinColumn(name = "id_manufacturer")
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "id_product_detail")
    private ProductDetail productDetail;

}
