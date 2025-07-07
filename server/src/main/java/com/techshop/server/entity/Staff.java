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
@Table(name = "staff")
@DynamicUpdate
public class Staff extends BaseEntity {

    @Column(name = "full_name")
    @Nationalized
    private String fullName;

    @Column(name = "staff_code")
    private String staffCode;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "avatar", length = 2000)
    private String avatar;

}
