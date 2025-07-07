/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";

import { ColumnDef, createColumnHelper } from "@tanstack/react-table";
import { Button } from "@/components/ui/button";
import { Pencil, ArrowLeftRight } from "lucide-react";
import { BrandDataTable, ResponseFetchListBrand } from "@/type/index.t";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";
import { Checkbox } from "@/components/ui/checkbox";
import DefaultHeader from "@/components/common/default-header";
import { BrandPaginationParams } from "@/service/api/brand.api";
import { DataTable } from "@/components/common/data-table";
import SpinnerLoading from "@/components/ui/SpinnerLoading";

const getStatusColor = (status: string) => {
  switch (status.toUpperCase()) {
    case "NOT_DELETED":
      return "bg-green-500/10 text-green-500 hover:bg-green-500/20";
    case "DELETED":
      return "bg-red-500/10 text-red-500 hover:bg-red-500/20";
    default:
      return "bg-gray-500/10 text-gray-500 hover:bg-gray-500/20";
  }
};

const getStatusText = (status: string) => {
  switch (status.toUpperCase()) {
    case "NOT_DELETED":
      return "Đang kinh doanh";
    case "DELETED":
      return "Ngừng kinh doanh";
    default:
      return status;
  }
};

const BrandTable = ({
  data,
  paginationParams,
  totalPages,
  totalElements,
  isLoading,
  handlePageChange,
  handleEditBrand,
  handleToggleStatus,
}: {
  data: ResponseFetchListBrand[] | undefined;
  paginationParams: BrandPaginationParams;
  totalPages: number | undefined;
  totalElements: number | undefined;
  isLoading: boolean;
  handlePageChange: (page: number) => void;
  handleEditBrand: (data: any, idBrand: string) => void;
  handleToggleStatus: (id: string) => void;
}) => {
  const columnHelper = createColumnHelper<BrandDataTable>();

  const columns = [
    columnHelper.display({
      id: "select",
      header: ({ table }) => (
        <Checkbox
          checked={
            table.getIsAllPageRowsSelected() ||
            (table.getIsSomePageRowsSelected() && "indeterminate")
          }
          onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
          aria-label="Select all"
        />
      ),
      cell: ({ row }) => (
        <Checkbox
          checked={row.getIsSelected()}
          onCheckedChange={(value) => row.toggleSelected(!!value)}
          aria-label="Select row"
        />
      ),
      enableSorting: false,
      enableHiding: false,
    }),
    columnHelper.accessor("orderNumber", {
      header: (info) => <DefaultHeader info={info} name="STT" />,
      cell: (info) => info.getValue(),
    }),
    columnHelper.accessor("brandCode", {
      header: (info) => <DefaultHeader info={info} name="Mã thương hiệu" />,
      cell: (info) => info.getValue(),
    }),
    columnHelper.accessor("brandName", {
      header: (info) => <DefaultHeader info={info} name="Tên thương hiệu" />,
      cell: (info) => info.getValue(),
    }),
    columnHelper.accessor("brandStatus", {
      header: (info) => <DefaultHeader info={info} name="Trạng thái" />,
      cell: (info) => {
        const status = info.getValue() as string;
        return (
          <Badge
            variant="secondary"
            className={cn("font-medium", getStatusColor(status))}
          >
            {getStatusText(status)}
          </Badge>
        );
      },
    }),
    columnHelper.display({
      id: "actions",
      header: "Hành động",
      cell: ({ row }) => {
        const brand = row.original;
        const isActive = brand.brandStatus.toUpperCase() === "NOT_DELETED";
        return (
          <div className="flex items-center gap-2">
            <Button
              variant="ghost"
              size="icon"
              className={cn(
                isActive
                  ? "text-green-500 hover:text-green-600"
                  : "text-red-500 hover:text-red-600"
              )}
              onClick={() => handleToggleStatus(brand.brandId)}
            >
              <ArrowLeftRight className="h-4 w-4" />
            </Button>
            <Button
              variant="ghost"
              size="icon"
              onClick={() =>
                handleEditBrand(
                  {
                    brandCode: brand.brandCode,
                    brandName: brand.brandName,
                  }, brand.brandId)
              }
            >
              <Pencil className="h-4 w-4" />
            </Button>
          </div>
        );
      },
      enableSorting: false,
      enableHiding: false,
    }),
  ];

  return (
    <>
      {isLoading && <SpinnerLoading />}
      <DataTable
        columns={columns as ColumnDef<ResponseFetchListBrand, unknown>[]}
        data={data || []}
        totalPages={totalPages || 1}
        totalElements={totalElements || 0}
        currentPage={paginationParams.page || 1}
        onPageChange={handlePageChange}
      />
    </>
  );
};

export default BrandTable;
