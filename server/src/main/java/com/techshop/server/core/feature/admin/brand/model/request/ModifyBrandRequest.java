package com.techshop.server.core.feature.admin.brand.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyBrandRequest {

    @NotNull(message = "Mã thương hiệu không được để trống")
    @NotBlank(message = "Mã thương hiệu không được để trống")
    @Size(min = 2, max = 255, message = "Mã thương hiệu phải từ 1 đến 255 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Mã thương hiệu chỉ được chứa chữ và số, không dấu và không có ký tự đặc biệt")
    private String brandCode;

    @NotNull(message = "Tên thương hiệu không được để trống")
    @NotBlank(message = "Tên thương hiệu không được để trống")
    @Size(min = 2, max = 255, message = "Tên thương hiệu phải từ 1 đến 255 ký tự")
    @Pattern(regexp = "^[\\p{L}0-9 .,&\\-()]+$", message = "Tên thương hiệu chỉ được chứa chữ cái, số và các ký tự đặc biệt: khoảng trắng, dấu chấm, phẩy, gạch ngang, ngoặc")
    private String brandName;

}
