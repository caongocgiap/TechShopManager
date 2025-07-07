package com.techshop.server.core.feature.admin.cpu.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyCpuSuccess {

    String cpuId;

    private String model;

    private String generation;

    private Long cores;

    private Long threads;

    private double baseClock;

    private double turboClock;

    private Long cacheSize;

    private Long tdpWatt;

    private String manufacturerName;

}
