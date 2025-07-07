package com.techshop.server.core.feature.admin.gpu.model.request;

import com.techshop.server.entity.Manufacturer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyGpuRequest {

    @NotBlank(message = "Mã GPU không được để trống!")
    @NotNull(message = "Mã GPU không được để trống!")
    private String gpuModel;

    @NotBlank(message = "Dòng GPU không được để trống!")
    @NotNull(message = "Dòng GPU không được để trống!")
    private String gpuSeries;

    @NotNull(message = "Dung lượng VRAM không được để trống!")
    @Min(value = 1, message = "Dung lượng VRAM phải lớn hơn 0!")
    @Max(value = 128 * 1024, message = "Dung lượng VRAM quá lớn!")
    private Long gpuMemorySize;

    private boolean gpuIsIntegrated;

    @NotBlank(message = "Hãng GPU không được để trống!")
    @NotNull(message = "Hãng GPU không được để trống!")
    private Manufacturer gpuManufacturer;

}
