import { PREFIX_API_ADMIN_BRAND } from "@/service/base/api.constant";
import request from "@/service/base/request";
import {
  PageableObject,
  PaginationParams,
  ResponseObject,
} from "@/type/index.i";
import { ModifyBrand, ResponseFetchListBrand } from "@/type/index.t";
import { AxiosResponse } from "axios";

export interface BrandPaginationParams extends PaginationParams {
  searchValues?: string;
}

export const getPaginationBrandApi = async (params: BrandPaginationParams) => {
  const res = (await request({
    method: "GET",
    url: `${PREFIX_API_ADMIN_BRAND}`,
    params: params,
  })) as AxiosResponse<
    ResponseObject<PageableObject<Array<ResponseFetchListBrand>>>
  >;

  return res.data;
};

export const createBrand = async (data: ModifyBrand) => {
  const res = (await request({
    method: "POST",
    url: `${PREFIX_API_ADMIN_BRAND}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateBrand = async (data: ModifyBrand, id: string) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_BRAND}/${id}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateBrandStatus = async (id: string) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_BRAND}/status/${id}`,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};
