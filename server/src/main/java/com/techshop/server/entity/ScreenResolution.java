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

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "screen_resolution")
@DynamicUpdate
public class ScreenResolution extends BaseEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "width")
    private Long width;

    @Column(name = "height")
    private Long height;

    @Column(name = "aspect_ratio")
    private String aspectRatio;

    public String calculateAspectRatio() {
        if (width == null || height == null) return null;
        long gcd = gcd(width, height);
        long wRatio = width / gcd;
        long hRatio = height / gcd;

        if (wRatio == 256 && hRatio == 135) return "17:9";
        if (wRatio == 16 && hRatio == 9) return "16:9";
        if (wRatio == 21 && hRatio == 9) return "21:9";
        if (wRatio == 4 && hRatio == 3) return "4:3";
        if (wRatio == 3 && hRatio == 2) return "3:2";

        return wRatio + ":" + hRatio;
    }

    private long gcd(long a, long b) {
        while (b != 0) {
            long tmp = b;
            b = a % b;
            a = tmp;
        }
        return a;
    }

}
