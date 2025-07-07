import { PREFIX_API_ADMIN_SCREEN_RESOLUTION } from "@/service/base/api.constant";
import request from "@/service/base/request";
import { PageableObject, ResponseObject } from "@/type/index.i";
import { PaginationParams } from "@/type/index.t";
import { AxiosResponse } from "axios";

export interface ResponseFetchListScreenResolution {
  orderNumber: number;
  screenResolutionId: string;
  screenResolutionCode: string;
  screenResolutionName: string;
  screenResolutionWidth: number;
  screenResolutionHeight: number;
  screenResolutionAspectRatio: string;
  screenResolutionStatus: string;
}

export interface ScreenResolutionPaginationParams extends PaginationParams {
  searchValues?: string;
}

export type ScreenResolution = {
  screenResolutionCode: string;
  screenResolutionName: string;
  screenResolutionWidth: number;
  screenResolutionHeight: number;
};

export const getPaginationScreenResolutionApi = async (
  params: ScreenResolutionPaginationParams
) => {
  const res = (await request({
    method: "GET",
    url: `${PREFIX_API_ADMIN_SCREEN_RESOLUTION}`,
    params: params,
  })) as AxiosResponse<
    ResponseObject<PageableObject<Array<ResponseFetchListScreenResolution>>>
  >;

  return res.data;
};

export const createScreenResolution = async (data: ScreenResolution) => {
  const res = (await request({
    method: "POST",
    url: `${PREFIX_API_ADMIN_SCREEN_RESOLUTION}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateScreenResolution = async (data, id) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_SCREEN_RESOLUTION}/${id}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateScreenResolutionStatus = async (id: string) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_SCREEN_RESOLUTION}/status/${id}`,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};
