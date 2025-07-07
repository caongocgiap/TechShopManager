export interface ResponseObject<T> {
  data: T;
  message: string;
  status: string;
  success: boolean;
}

export interface PageableObject<T> {
  data: T;
  totalPages: number;
  totalElements: number;
  currentPage: number;
}

export interface PaginationParams {
  page: number;
  size: number;
  orderBy?: string;
  sortBy?: string;
  q?: string;
}

export interface Brand {
  brandId: string;
  brandCode: string;
  brandName: string;
}