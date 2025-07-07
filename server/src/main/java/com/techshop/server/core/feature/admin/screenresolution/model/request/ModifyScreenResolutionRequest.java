package com.techshop.server.core.feature.admin.screenresolution.model.request;

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
public class ModifyScreenResolutionRequest {

    @NotNull(message = "Mã độ phân giải màn hình không được để trống")
    @NotBlank(message = "Mã độ phân giải màn hình không được để trống")
    @Size(min = 2, max = 255, message = "Mã độ phân giải màn hình phải từ 2 đến 255 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Mã độ phân giải màn hình chỉ được chứa chữ và số, không dấu và không có ký tự đặc biệt")
    private String screenResolutionCode;

    @NotNull(message = "Tên độ phân giải màn hình không được để trống")
    @NotBlank(message = "Tên độ phân giải màn hình không được để trống")
    @Size(min = 2, max = 255, message = "Tên độ phân giải màn hình phải từ 2 đến 255 ký tự")
    @Pattern(regexp = "^[\\p{L}0-9 .,&\\-()]+$", message = "Tên độ phân giải màn hình chỉ được chứa chữ cái, số và các ký tự đặc biệt: khoảng trắng, dấu chấm, phẩy, gạch ngang, ngoặc")
    private String screenResolutionName;

    @NotNull(message = "Chiều rộng không được để trống!")
    @NotBlank(message = "Chiều rộng không được để trống!")
    @Pattern(regexp = "^[1-9]\\d{2,4}$", message = "Chiều rộng phải trong khoảng 100 đến 99999")
    private String screenResolutionWidth;

    @NotNull(message = "Chiều cao không được để trống!")
    @NotBlank(message = "Chiều cao không được để trống!")
    @Pattern(regexp = "^[1-9]\\d{2,4}$", message = "Chiều cao phải trong khoảng 100 đến 99999")
    private String screenResolutionHeight;

}
