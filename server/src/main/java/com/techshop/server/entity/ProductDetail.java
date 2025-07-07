package com.techshop.server.entity;

import com.techshop.server.entity.base.BaseEntity;
import com.techshop.server.infrastructure.constant.ProductStatus;
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
import org.hibernate.annotations.Nationalized;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_detail")
@DynamicUpdate
public class ProductDetail extends BaseEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "stock_quantity")
    private Double stockQuantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "description")
    @Nationalized
    private String description;

    @Column(name = "status")
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_screen_resolution")
    private ScreenResolution screenResolution;

    @ManyToOne
    @JoinColumn(name = "id_gpu")
    private Gpu gpu;

    @ManyToOne
    @JoinColumn(name = "id_operation_system")
    private OperationSystem operationSystem;

    @ManyToOne
    @JoinColumn(name = "id_cpu")
    private Cpu cpu;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_brand")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "id_ram")
    private Ram ram;

    @ManyToOne
    @JoinColumn(name = "id_image")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "id_imei")
    private Imei imei;

}
