package com.techshop.server.core.feature.admin.gpu.model.request;

import com.techshop.server.core.common.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GpuPaginationRequest extends PageableRequest {

    private String searchValues;

}
