/* eslint-disable @typescript-eslint/no-explicit-any */
import { ConfirmDialog } from "@/components/common/confirm-dialog";
import { DataTable } from "@/components/common/data-table";
import { SearchInput } from "@/components/common/search-input";
import { Button } from "@/components/ui/button";
import SpinnerLoading from "@/components/ui/SpinnerLoading";
import { INTERNAL_SERVER_ERROR } from "@/constants/message.constant";
import { useConfirm } from "@/hooks/useConfirm";
import useToggle from "@/hooks/useToggle";
import {
  useGetPaginationOperationSystem,
  useUpdateOperationSystemStatus,
} from "@/service/actions/operation-system.action";
import { OperationSystemPaginationParams } from "@/service/api/operation-system.api";
import { OperationSystem } from "@/type/index.t";
import React, { useCallback, useMemo, useState } from "react";
import { toast } from "sonner";
import { useImmer } from "use-immer";
import { createColumns } from "./components/columns";
import { CreateOperationSystemDialog } from "./components/dialog-add-operation-system";
import { UpdateOperationSystemDialog } from "./components/dialog-update-operation-system";

export interface OperationSystemDetail {
  operationSystemId: string;
  operationSystemCode: string;
  operationSystemName: string;
}

const OperationSystemPage = () => {
  const [searchValue, setSearchValue] = React.useState("");
  const [dataUpdateOperationSystem, setDataUpdateOperationSystem] =
    useState<OperationSystemDetail | null>();
  const { value: isOpenCreateDialog, setValue: setIsOpenCreateDialog } =
    useToggle();
  const { value: isOpenUpdateDialog, setValue: setIsOpenUpdateDialog } =
    useToggle();

  const { mutateAsync: changeOperationSystemStatus, isPending: isLoading } =
    useUpdateOperationSystemStatus();
  const confirm = useConfirm();

  const [paginationParams, setPaginationParams] =
    useImmer<OperationSystemPaginationParams>({
      page: 1,
      size: 10,
    });

  const { data: dataFetchingOperationSystem } =
    useGetPaginationOperationSystem(paginationParams);

  const totalPages = useMemo(() => {
    return dataFetchingOperationSystem?.data?.totalPages || 0;
  }, [dataFetchingOperationSystem]);

  const categoryData = useMemo(() => {
    return (
      dataFetchingOperationSystem?.data?.data?.map((item) => ({
        ...item,
        key: item.operationSystemId,
      })) || []
    );
  }, [dataFetchingOperationSystem]);

  const handleAddOperationSystem = useCallback(() => {
    setIsOpenCreateDialog(true);
  }, [setIsOpenCreateDialog]);

  const handleEditOperationSystem = useCallback(
    (operationSystem: OperationSystem) => {
      setIsOpenUpdateDialog(true);
      setDataUpdateOperationSystem(operationSystem);
    },
    [setIsOpenUpdateDialog]
  );

  const handleDeleteOperationSystem = useCallback(
    async (operationSystem: OperationSystem) => {
      const isConfirmed = await confirm.confirm({
        title: "Xóa hệ điều hành",
        description: "Bạn có chắc chắn muốn xóa hệ điều hành này?",
      });

      if (isConfirmed) {
        console.log(operationSystem);
        toast.info("Chức năng xóa đang được phát triển");
      }
    },
    [confirm]
  );

  const handleToggleStatus = useCallback(
    async (operationSystem: OperationSystem) => {
      const isConfirmed = await confirm.confirm({
        title: "Thay đổi trạng thái",
        description:
          "Bạn có chắc chắn muốn thay đổi trạng thái của hệ điều hành này?",
      });

      if (isConfirmed) {
        try {
          const res = await changeOperationSystemStatus(
            operationSystem.operationSystemId
          );
          toast.success(res?.message);
        } catch (error: any) {
          toast.error(error?.response?.data?.message || INTERNAL_SERVER_ERROR);
        }
      }
    },
    [confirm, changeOperationSystemStatus]
  );

  const columns = useMemo(
    () =>
      createColumns({
        onEdit: handleEditOperationSystem,
        onDelete: handleDeleteOperationSystem,
        onToggleStatus: handleToggleStatus,
      }),
    [handleEditOperationSystem, handleDeleteOperationSystem, handleToggleStatus]
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
          Quản lý Hệ điều hành
        </h1>
        <div className="flex items-center justify-between">
          <SearchInput
            value={searchValue}
            onChange={handleSearch}
            className="w-full max-w-[200px]"
            placeholder="Tìm theo tên hoặc mã..."
          />
          <Button onClick={handleAddOperationSystem} variant="outline">
            Thêm hệ điều hành
          </Button>
        </div>
        <div className="mx-auto">
          <DataTable
            columns={columns}
            data={categoryData}
            totalPages={totalPages}
            currentPage={paginationParams.page}
            onPageChange={handlePageChange}
          />
        </div>
        <CreateOperationSystemDialog
          {...{
            isOpenDialog: isOpenCreateDialog,
            setIsOpenDialog: setIsOpenCreateDialog,
          }}
        />
        <UpdateOperationSystemDialog
          {...{
            isOpenDialog: isOpenUpdateDialog,
            setIsOpenDialog: setIsOpenUpdateDialog,
            dataUpdate:
              dataUpdateOperationSystem || ({} as OperationSystemDetail),
            setDataUpdate: setDataUpdateOperationSystem,
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

export default OperationSystemPage;
