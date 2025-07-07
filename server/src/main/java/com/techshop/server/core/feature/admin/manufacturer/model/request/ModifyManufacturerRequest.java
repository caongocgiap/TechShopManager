package com.techshop.server.core.feature.admin.manufacturer.model.request;

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
public class ModifyManufacturerRequest {

    @NotNull(message = "Mã nhà sản xuất không được để trống")
    @NotBlank(message = "Mã nhà sản xuất không được để trống")
    private String manufacturerCode;

    @NotNull(message = "Tên nhà sản xuất không được để trống")
    @NotBlank(message = "Tên nhà sản xuất không được để trống")
    private String manufacturerName;

    private String manufacturerCountry;

    private String manufacturerWebsite;

    private String manufacturerDescription;

}
