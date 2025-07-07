package com.techshop.server.core.feature.admin.manufacturer.model.request;

import com.techshop.server.core.common.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerPaginationRequest extends PageableRequest {

    private String searchValues;

}
