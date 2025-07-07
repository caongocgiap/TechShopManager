import { PREFIX_API_ADMIN_CPU } from "@/service/base/api.constant";
import request from "@/service/base/request";
import { ModifyCpu, PageableObject, ResponseObject } from "@/type/index.i";
import { PaginationParams } from "@/type/index.t";
import { AxiosResponse } from "axios";

export interface ResponseFetchListCpu {
  orderNumber: number;
  cpuId: string;
  cpuModel: string;
  cpuManufacturerName: string;
  cpuSeries: string;
  cpuGeneration: string;
  cpuCores: number;
  cpuThreads: number;
  cpuBaseClock: number;
  cpuTurboClock: number;
  cpuCacheSize: number;
  cpuTdpWatt: number;
  cpuIntegratedGpu: string;
  cpuArchitecture: string;
  cpuReleaseYear: number;
  cpuStatus: string;
}

export interface CpuPaginationParams extends PaginationParams {
  searchValues?: string;
}

export type Cpu = {
  cpuModel: string;
  manufacturerId: string;
  cpuSeries: string;
  cpuGeneration: string;
  cpuCores: number;
  cpuThreads: number;
  cpuBaseClock: number;
  cpuTurboClock: number;
  cpuCacheSize: number;
  cpuTdpWatt: number;
  cpuIntegratedGpu: string;
  cpuArchitecture: string;
  cpuReleaseYear: number;
};

export const getPaginationCpuApi = async (
  params: CpuPaginationParams
) => {
  const res = (await request({
    method: "GET",
    url: `${PREFIX_API_ADMIN_CPU}`,
    params: params,
  })) as AxiosResponse<
    ResponseObject<PageableObject<Array<ResponseFetchListCpu>>>
  >;

  return res.data;
};

export const createCpu = async (data: ModifyCpu) => {
  const res = (await request({
    method: "POST",
    url: `${PREFIX_API_ADMIN_CPU}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateCpu = async (data: ModifyCpu, id: string) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_CPU}/${id}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateCpuStatus = async (id: string) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_CPU}/status/${id}`,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};
