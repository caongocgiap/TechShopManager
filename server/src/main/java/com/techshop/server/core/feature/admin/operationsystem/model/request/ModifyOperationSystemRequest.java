package com.techshop.server.core.feature.admin.operationsystem.model.request;

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
public class ModifyOperationSystemRequest {

    @NotNull(message = "Mã hệ điều hành không được để trống")
    @NotBlank(message = "Mã hệ điều hành không được để trống")
    @Size(min = 2, max = 255, message = "Mã hệ điều hành phải từ 2 đến 255 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Mã hệ điều hành chỉ được chứa chữ và số, không dấu và không có ký tự đặc biệt")
    private String operationSystemCode;

    @NotNull(message = "Tên hệ điều hành không được để trống")
    @NotBlank(message = "Tên hệ điều hành không được để trống")
    @Size(min = 2, max = 255, message = "Tên hệ điều hành phải từ 2 đến 255 ký tự")
    @Pattern(regexp = "^[\\p{L}0-9 .,&\\-()]+$", message = "Tên hệ điều hành chỉ được chứa chữ cái, số và các ký tự đặc biệt: khoảng trắng, dấu chấm, phẩy, gạch ngang, ngoặc")
    private String operationSystemName;

}
