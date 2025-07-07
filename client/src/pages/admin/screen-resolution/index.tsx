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
  useGetPaginationScreenResolution,
  useUpdateScreenResolutionStatus,
} from "@/service/actions/screen-resolution.action";
import { ScreenResolution } from "@/type/index.t";
import React, { useCallback, useMemo, useState } from "react";
import { toast } from "sonner";
import { useImmer } from "use-immer";
import { createColumns } from "./components/columns";
import { CreateScreenResolutionDialog } from "./components/dialog-add-screen-resolution";
import { UpdateScreenResolutionDialog } from "./components/dialog-update-screen-resolution";
import { ScreenResolutionPaginationParams } from "@/service/api/screen-resolution.api";

export interface ScreenResolutionDetail {
  screenResolutionId: string;
  screenResolutionCode: string;
  screenResolutionName: string;
  screenResolutionWidth: number;
  screenResolutionHeight: number;
}

const ScreenResolutionPage = () => {
  const [searchValue, setSearchValue] = React.useState("");
  const [dataUpdateScreenResolution, setDataUpdateScreenResolution] =
    useState<ScreenResolutionDetail | null>();
  const { value: isOpenCreateDialog, setValue: setIsOpenCreateDialog } =
    useToggle();
  const { value: isOpenUpdateDialog, setValue: setIsOpenUpdateDialog } =
    useToggle();

  const { mutateAsync: changeScreenResolutionStatus, isPending: isLoading } =
    useUpdateScreenResolutionStatus();
  const confirm = useConfirm();

  const [paginationParams, setPaginationParams] =
    useImmer<ScreenResolutionPaginationParams>({
      page: 1,
      size: 10,
    });

  const { data: dataFetchingScreenResolution } =
    useGetPaginationScreenResolution(paginationParams);

  const totalPages = useMemo(() => {
    return dataFetchingScreenResolution?.data?.totalPages || 0;
  }, [dataFetchingScreenResolution]);

  const screenResolutionData = useMemo(() => {
    return (
      dataFetchingScreenResolution?.data?.data?.map((item) => ({
        ...item,
        key: item.screenResolutionId,
      })) || []
    );
  }, [dataFetchingScreenResolution]);

  const handleAddScreenResolution = useCallback(() => {
    setIsOpenCreateDialog(true);
  }, [setIsOpenCreateDialog]);

  const handleEditScreenResolution = useCallback(
    (screenResolution: ScreenResolution) => {
      setIsOpenUpdateDialog(true);
      setDataUpdateScreenResolution(screenResolution);
    },
    [setIsOpenUpdateDialog]
  );

  const handleDeleteScreenResolution = useCallback(
    async (screenResolution: ScreenResolution) => {
      const isConfirmed = await confirm.confirm({
        title: "Xóa độ phân giải",
        description: "Bạn có chắc chắn muốn xóa độ phân giải này?",
      });

      if (isConfirmed) {
        console.log(screenResolution);
        toast.info("Chức năng xóa đang được phát triển");
      }
    },
    [confirm]
  );

  const handleToggleStatus = useCallback(
    async (screenResolution: ScreenResolution) => {
      const isConfirmed = await confirm.confirm({
        title: "Thay đổi trạng thái",
        description:
          "Bạn có chắc chắn muốn thay đổi trạng thái của độ phân giải này?",
      });

      if (isConfirmed) {
        try {
          const res = await changeScreenResolutionStatus(
            screenResolution.screenResolutionId
          );
          toast.success(res?.message);
        } catch (error: any) {
          toast.error(error?.response?.data?.message || INTERNAL_SERVER_ERROR);
        }
      }
    },
    [confirm, changeScreenResolutionStatus]
  );

  const columns = useMemo(
    () =>
      createColumns({
        onEdit: handleEditScreenResolution,
        onDelete: handleDeleteScreenResolution,
        onToggleStatus: handleToggleStatus,
      }),
    [
      handleEditScreenResolution,
      handleDeleteScreenResolution,
      handleToggleStatus,
    ]
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
          Quản lý Độ phân giải màn hình
        </h1>
        <div className="flex items-center justify-between">
          <SearchInput
            value={searchValue}
            onChange={handleSearch}
            className="w-full max-w-[200px]"
            placeholder="Tìm theo mã..."
          />
          <Button onClick={handleAddScreenResolution} variant="outline">
            Thêm độ phân giải
          </Button>
        </div>
        <div className="mx-auto">
          <DataTable
            columns={columns}
            data={screenResolutionData}
            totalPages={totalPages}
            currentPage={paginationParams.page}
            onPageChange={handlePageChange}
          />
        </div>
        <CreateScreenResolutionDialog
          {...{
            isOpenDialog: isOpenCreateDialog,
            setIsOpenDialog: setIsOpenCreateDialog,
          }}
        />
        <UpdateScreenResolutionDialog
          {...{
            isOpenDialog: isOpenUpdateDialog,
            setIsOpenDialog: setIsOpenUpdateDialog,
            dataUpdate:
              dataUpdateScreenResolution || ({} as ScreenResolutionDetail),
            setDataUpdate: setDataUpdateScreenResolution,
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

export default ScreenResolutionPage;
