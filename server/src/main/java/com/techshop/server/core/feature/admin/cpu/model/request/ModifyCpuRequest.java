package com.techshop.server.core.feature.admin.cpu.model.request;

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
public class ModifyCpuRequest {

    @NotBlank(message = "Mã CPU không được để trống!")
    @NotNull(message = "Mã CPU không được để trống!")
    private String cpuModel;

    @NotBlank(message = "Dòng CPU không được để trống!")
    @NotNull(message = "Dòng CPU không được để trống!")
    private String cpuSeries;

    @NotBlank(message = "Thế hệ CPU không được để trống!")
    @NotNull(message = "Thế hệ CPU không được để trống!")
    private String cpuGeneration;

    @NotNull(message = "Số nhân không được để trống!")
    @Min(value = 1, message = "CPU phải có ít nhất 1 nhân!")
    @Max(value = 128, message = "Số nhân CPU quá lớn!")
    private Long cpuCores;

    @NotNull(message = "Số luồng không được để trống!")
    @Min(value = 1, message = "CPU phải có ít nhất 1 luồng!")
    @Max(value = 256, message = "Số luồng CPU quá lớn!")
    private Long cpuThreads;

    @NotNull(message = "Xung nhịp cơ bản không được để trống!")
    @Min(value = 1, message = "Xung nhịp cơ bản tối thiểu là 1GHz")
    @Max(value = 128, message = "Xung nhịp cơ bản quá lớn (tính theo GHz)")
    private double cpuBaseClock;

    @NotNull(message = "Xung nhịp tối đa không được để trống!")
    @Min(value = 1, message = "Xung nhịp tối đa tối thiểu là 1GHz!")
    @Max(value = 128, message = "Xung nhịp tối đa quá lớn (tính theo GHz)")
    private double cpuTurboClock;

    @NotNull(message = "Bộ nhớ đệm không được để trống!")
    @Min(value = 1, message = "Bộ nhớ đệm phải lớn hơn 0!")
    @Max(value = 128 * 1024, message = "Bộ nhớ đệm quá lớn!")
    private Long cpuCacheSize;

    @NotNull(message = "Công suất tiêu thụ không được để trống!")
    private Long cpuTdpWatt;

    @NotNull(message = "Kiến trúc CPU không được để trống!")
    private String cpuArchitecture;

    @NotNull(message = "Năm phát hành không được để trống!")
    @Min(value = 1970, message = "Năm phát hành không được nhỏ hơn 1970")
    @Max(value = 2100, message = "Năm phát hành không được vượt quá 2100")
    private Long cpuReleaseYear;

    @NotBlank(message = "Hãng CPU không được để trống!")
    @NotNull(message = "Hãng CPU không được để trống!")
    private String cpuManufacturerId;

}
