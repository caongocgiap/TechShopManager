/* eslint-disable @typescript-eslint/no-explicit-any */
import { SearchInput } from "@/components/common/search-input";
import { Button } from "@/components/ui/button";
import useToggle from "@/hooks/useToggle";
import React, { useCallback, useMemo, useState } from "react";
import { useImmer } from "use-immer";
import { CreateManufacturerDialog } from "./components/dialog-add-manufacturer";
import { UpdateManufacturerDialog } from "./components/dialog-update-manufacturer";
import { DataTable } from "@/components/common/data-table";
import { createColumns } from "./components/columns";
import { useConfirm } from "@/hooks/useConfirm";
import {
  useGetPaginationManufacturer,
  useUpdateManufacturerStatus,
} from "@/service/actions/manufacturer.action";
import { toast } from "sonner";
import { INTERNAL_SERVER_ERROR } from "@/constants/message.constant";
import SpinnerLoading from "@/components/ui/SpinnerLoading";
import { ConfirmDialog } from "@/components/common/confirm-dialog";
import { Manufacturer } from "@/type/index.t";
import { ManufacturerPaginationParams } from "@/service/api/manufacturer.api";

export interface ManufacturerDetail {
  manufacturerId: string;
  manufacturerCode: string;
  manufacturerName: string;
  manufacturerCountry: string;
  manufacturerWebsite: string;
  manufacturerDescription: string;
}

const ManufacturerPage = () => {
  const [searchValue, setSearchValue] = React.useState("");
  const [dataUpdateManufacturer, setDataUpdateManufacturer] =
    useState<ManufacturerDetail | null>();
  const { value: isOpenCreateDialog, setValue: setIsOpenCreateDialog } =
    useToggle();
  const { value: isOpenUpdateDialog, setValue: setIsOpenUpdateDialog } =
    useToggle();

  const { mutateAsync: changeManufacturerStatus, isPending: isLoading } =
    useUpdateManufacturerStatus();
  const confirm = useConfirm();

  const [paginationParams, setPaginationParams] =
    useImmer<ManufacturerPaginationParams>({
      page: 1,
      size: 10,
    });

  const { data: dataFetchingManufacturer } =
    useGetPaginationManufacturer(paginationParams);

  const totalPages = useMemo(() => {
    return dataFetchingManufacturer?.data?.totalPages || 0;
  }, [dataFetchingManufacturer]);

  const manufacturerData = useMemo(() => {
    return (
      dataFetchingManufacturer?.data?.data?.map((item) => ({
        ...item,
        key: item.manufacturerId,
      })) || []
    );
  }, [dataFetchingManufacturer]);

  const handleAddManufacturer = useCallback(() => {
    setIsOpenCreateDialog(true);
  }, [setIsOpenCreateDialog]);

  const handleEditManufacturer = useCallback(
    (manufacturer: Manufacturer) => {
      setIsOpenUpdateDialog(true);
      setDataUpdateManufacturer(manufacturer);
    },
    [setIsOpenUpdateDialog]
  );

  const handleDeleteManufacturer = useCallback(
    async (manufacturer: Manufacturer) => {
      const isConfirmed = await confirm.confirm({
        title: "Xóa nhà sản xuất",
        description: "Bạn có chắc chắn muốn xóa nhà sản xuất này?",
      });

      if (isConfirmed) {
        console.log(manufacturer);
        toast.info("Chức năng xóa đang được phát triển");
      }
    },
    [confirm]
  );

  const handleToggleStatus = useCallback(
    async (manufacturer: Manufacturer) => {
      const isConfirmed = await confirm.confirm({
        title: "Thay đổi trạng thái",
        description:
          "Bạn có chắc chắn muốn thay đổi trạng thái của nhà sản xuất này?",
      });

      if (isConfirmed) {
        try {
          const res = await changeManufacturerStatus(
            manufacturer.manufacturerId
          );
          toast.success(res?.message);
        } catch (error: any) {
          toast.error(error?.response?.data?.message || INTERNAL_SERVER_ERROR);
        }
      }
    },
    [confirm, changeManufacturerStatus]
  );

  const columns = useMemo(
    () =>
      createColumns({
        onEdit: handleEditManufacturer,
        onDelete: handleDeleteManufacturer,
        onToggleStatus: handleToggleStatus,
      }),
    [handleEditManufacturer, handleDeleteManufacturer, handleToggleStatus]
  );

  const handleSearch = useCallback(
    (value: string) => {
      setSearchValue(value);
      if (value.length === 0) {
        setPaginationParams((draft) => {
          draft.page = 1;
          draft.searchValues = undefined;
        });
        return;
      }
      setPaginationParams((draft) => {
        draft.page = 1;
        draft.searchValues = value;
      });
    },
    [setPaginationParams]
  );

  const handlePageChange = useCallback(
    (page: number) => {
      setPaginationParams((draft) => {
        draft.page = page;
      });
    },
    [setPaginationParams]
  );

  return (
    <>
      {isLoading && <SpinnerLoading />}
      <div className="px-4">
        <h1 className="text-4xl font-bold text-center text-foreground uppercase mb-5">
          Quản lý nhà sản xuất
        </h1>
        <div className="flex items-center justify-between">
          <SearchInput
            value={searchValue}
            onChange={handleSearch}
            className="w-full max-w-[200px]"
            placeholder="Tìm theo tên hoặc mã..."
          />
          <Button onClick={handleAddManufacturer} variant="outline">
            Thêm nhà sản xuất
          </Button>
        </div>
        <div className="mx-auto">
          <DataTable
            columns={columns}
            data={manufacturerData}
            totalPages={totalPages}
            currentPage={paginationParams.page}
            onPageChange={handlePageChange}
          />
        </div>
        <CreateManufacturerDialog
          {...{
            isOpenDialog: isOpenCreateDialog,
            setIsOpenDialog: setIsOpenCreateDialog,
          }}
        />
        <UpdateManufacturerDialog
          {...{
            isOpenDialog: isOpenUpdateDialog,
            setIsOpenDialog: setIsOpenUpdateDialog,
            dataUpdate: dataUpdateManufacturer || ({} as ManufacturerDetail),
            setDataUpdate: setDataUpdateManufacturer,
          }}
        />
        <ConfirmDialog
          isOpen={confirm.isOpen}
          onClose={confirm.handleClose}
          onConfirm={confirm.handleConfirm}
          {...confirm.options}
        />
      </div>
    </>
  );
};

export default ManufacturerPage;
