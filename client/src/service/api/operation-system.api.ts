import { PREFIX_API_ADMIN_OPERATION_SYSTEM } from "@/service/base/api.constant";
import request from "@/service/base/request";
import { PageableObject, ResponseObject } from "@/type/index.i";
import { PaginationParams } from "@/type/index.t";
import { AxiosResponse } from "axios";

export interface ResponseFetchListOperationSystem {
  orderNumber: number;
  operationSystemId: string;
  operationSystemCode: string;
  operationSystemName: string;
  operationSystemStatus: string;
}

export interface OperationSystemPaginationParams extends PaginationParams {
  searchValues?: string;
}

export type OperationSystem = {
  operationSystemCode: string;
  operationSystemName: string;
};

export const getPaginationOperationSystemApi = async (
  params: OperationSystemPaginationParams
) => {
  const res = (await request({
    method: "GET",
    url: `${PREFIX_API_ADMIN_OPERATION_SYSTEM}`,
    params: params,
  })) as AxiosResponse<
    ResponseObject<PageableObject<Array<ResponseFetchListOperationSystem>>>
  >;

  return res.data;
};

export const createOperationSystem = async (data: OperationSystem) => {
  const res = (await request({
    method: "POST",
    url: `${PREFIX_API_ADMIN_OPERATION_SYSTEM}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateOperationSystem = async (data, id) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_OPERATION_SYSTEM}/${id}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateOperationSystemStatus = async (id: string) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_OPERATION_SYSTEM}/status/${id}`,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};
