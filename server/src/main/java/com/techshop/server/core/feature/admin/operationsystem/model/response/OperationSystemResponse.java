package com.techshop.server.core.feature.admin.operationsystem.model.response;

import com.techshop.server.core.common.HasOrderNumber;
import com.techshop.server.core.common.IsIdentify;

public interface OperationSystemResponse extends IsIdentify, HasOrderNumber {

    String getOperationSystemId();

    String getOperationSystemCode();

    String getOperationSystemName();

    String getOperationSystemStatus();

}
