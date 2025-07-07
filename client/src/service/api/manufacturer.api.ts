import { PREFIX_API_ADMIN_MANUFACTURER } from "@/service/base/api.constant";
import request from "@/service/base/request";
import { PageableObject, ResponseObject } from "@/type/index.i";
import { PaginationParams } from "@/type/index.t";
import { AxiosResponse } from "axios";

export interface ResponseFetchListManufacturer {
  orderNumber: number;
  manufacturerId: string;
  manufacturerCode: string;
  manufacturerName: string;
  manufacturerCountry: string;
  manufacturerWebsite: string;
  manufacturerDescription: string;
  manufacturerStatus: string;
}

export interface ManufacturerPaginationParams extends PaginationParams {
  searchValues?: string;
}

export type Manufacturer = {
  manufacturerCode: string;
  manufacturerName: string;
  manufacturerCountry: string;
  manufacturerWebsite: string;
  manufacturerDescription: string;
};

export const getPaginationManufacturerApi = async (
  params: ManufacturerPaginationParams
) => {
  const res = (await request({
    method: "GET",
    url: `${PREFIX_API_ADMIN_MANUFACTURER}`,
    params: params,
  })) as AxiosResponse<
    ResponseObject<PageableObject<Array<ResponseFetchListManufacturer>>>
  >;

  return res.data;
};

export const createManufacturer = async (data: Manufacturer) => {
  const res = (await request({
    method: "POST",
    url: `${PREFIX_API_ADMIN_MANUFACTURER}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateManufacturer = async (data, id) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_MANUFACTURER}/${id}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateManufacturerStatus = async (id: string) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_MANUFACTURER}/status/${id}`,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const getAllManufacturerApi = async () => {
  const res = (await request({
    method: "GET",
    url: `${PREFIX_API_ADMIN_MANUFACTURER}/get-all`,
  })) as AxiosResponse<
    ResponseObject<Array<ResponseFetchListManufacturer>>
  >;

  return res.data;
};
