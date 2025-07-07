package com.techshop.server.entity;

import com.techshop.server.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "role")
@DynamicUpdate
public class Role extends BaseEntity {

    @Column(name = "code")
    @Nationalized
    private String code;

    @Column(name = "name")
    private String name;

}
