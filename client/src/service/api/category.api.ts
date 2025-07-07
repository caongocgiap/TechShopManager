import { PREFIX_API_ADMIN_CATEGORY } from "@/service/base/api.constant";
import request from "@/service/base/request";
import { PageableObject, ResponseObject } from "@/type/index.i";
import { PaginationParams } from "@/type/index.t";
import { AxiosResponse } from "axios";

export interface ResponseFetchListCategory {
  orderNumber: number;
  categoryId: string;
  categoryCode: string;
  categoryName: string;
  categoryStatus: string;
}

export interface CategoryPaginationParams extends PaginationParams {
  searchValues?: string;
}

export type Category = {
  categoryCode: string;
  categoryName: string;
};

export const getPaginationCategoryApi = async (
  params: CategoryPaginationParams
) => {
  const res = (await request({
    method: "GET",
    url: `${PREFIX_API_ADMIN_CATEGORY}`,
    params: params,
  })) as AxiosResponse<
    ResponseObject<PageableObject<Array<ResponseFetchListCategory>>>
  >;

  return res.data;
};

export const createCategory = async (data: Category) => {
  const res = (await request({
    method: "POST",
    url: `${PREFIX_API_ADMIN_CATEGORY}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateCategory = async (data, id) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_CATEGORY}/${id}`,
    data: data,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};

export const updateCategoryStatus = async (id: string) => {
  const res = (await request({
    method: "PUT",
    url: `${PREFIX_API_ADMIN_CATEGORY}/status/${id}`,
  })) as AxiosResponse<ResponseObject<unknown>>;

  return res.data;
};
