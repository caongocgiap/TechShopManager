/* eslint-disable @typescript-eslint/no-explicit-any */
import { SearchInput } from "@/components/common/search-input";
import { Button } from "@/components/ui/button";
import useToggle from "@/hooks/useToggle";
import { Category } from "@/type/index.t";
import { CategoryPaginationParams } from "@/service/api/category.api";
import React, { useCallback, useMemo, useState } from "react";
import { useImmer } from "use-immer";
import { CreateCategoryDialog } from "./components/dialog-add-category";
import { DataTable } from "@/components/common/data-table";
import { createColumns } from "./components/columns";
import { useConfirm } from "@/hooks/useConfirm";
import {
  useGetPaginationCategory,
  useUpdateCategoryStatus,
} from "@/service/actions/category.action";
import { toast } from "sonner";
import { INTERNAL_SERVER_ERROR } from "@/constants/message.constant";
import SpinnerLoading from "@/components/ui/SpinnerLoading";
import { ConfirmDialog } from "@/components/common/confirm-dialog";
import { UpdateCategoryDialog } from "./components/dialog-update-category";

export interface CategoryDetail {
  categoryId: string;
  categoryCode: string;
  categoryName: string;
}

const CategoryPage = () => {
  const [searchValue, setSearchValue] = React.useState("");
  const [dataUpdateCategory, setDataUpdateCategory] =
    useState<CategoryDetail | null>();
  const { value: isOpenCreateDialog, setValue: setIsOpenCreateDialog } =
    useToggle();
  const { value: isOpenUpdateDialog, setValue: setIsOpenUpdateDialog } =
    useToggle();

  const { mutateAsync: changeCategoryStatus, isPending: isLoading } =
    useUpdateCategoryStatus();
  const confirm = useConfirm();

  const [paginationParams, setPaginationParams] =
    useImmer<CategoryPaginationParams>({
      page: 1,
      size: 10,
    });

  const { data: dataFetchingCategory } =
    useGetPaginationCategory(paginationParams);

  const totalPages = useMemo(() => {
    return dataFetchingCategory?.data?.totalPages || 0;
  }, [dataFetchingCategory]);

  const categoryData = useMemo(() => {
    return (
      dataFetchingCategory?.data?.data?.map((item) => ({
        ...item,
        key: item.categoryId,
      })) || []
    );
  }, [dataFetchingCategory]);

  const handleAddCategory = useCallback(() => {
    setIsOpenCreateDialog(true);
  }, [setIsOpenCreateDialog]);

  const handleEditCategory = useCallback(
    (category: Category) => {
      setIsOpenUpdateDialog(true);
      setDataUpdateCategory(category);
    },
    [setIsOpenUpdateDialog]
  );

  const handleDeleteCategory = useCallback(
    async (category: Category) => {
      const isConfirmed = await confirm.confirm({
        title: "Xóa thể loại",
        description: "Bạn có chắc chắn muốn xóa thể loại này?",
      });

      if (isConfirmed) {
        console.log(category);
        toast.info("Chức năng xóa đang được phát triển");
      }
    },
    [confirm]
  );

  const handleToggleStatus = useCallback(
    async (category: Category) => {
      const isConfirmed = await confirm.confirm({
        title: "Thay đổi trạng thái",
        description:
          "Bạn có chắc chắn muốn thay đổi trạng thái của thể loại này?",
      });

      if (isConfirmed) {
        try {
          const res = await changeCategoryStatus(category.categoryId);
          toast.success(res?.message);
        } catch (error: any) {
          toast.error(error?.response?.data?.message || INTERNAL_SERVER_ERROR);
        }
      }
    },
    [confirm, changeCategoryStatus]
  );

  const columns = useMemo(
    () =>
      createColumns({
        onEdit: handleEditCategory,
        onDelete: handleDeleteCategory,
        onToggleStatus: handleToggleStatus,
      }),
    [handleEditCategory, handleDeleteCategory, handleToggleStatus]
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
          Quản lý thể loại
        </h1>
        <div className="flex items-center justify-between">
          <SearchInput
            value={searchValue}
            onChange={handleSearch}
            className="w-full max-w-[200px]"
            placeholder="Tìm theo tên hoặc mã..."
          />
          <Button onClick={handleAddCategory} variant="outline">
            Thêm thể loại
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
        <CreateCategoryDialog
          {...{
            isOpenDialog: isOpenCreateDialog,
            setIsOpenDialog: setIsOpenCreateDialog,
          }}
        />
        <UpdateCategoryDialog
          {...{
            isOpenDialog: isOpenUpdateDialog,
            setIsOpenDialog: setIsOpenUpdateDialog,
            dataUpdate: dataUpdateCategory || ({} as CategoryDetail),
            setDataUpdate: setDataUpdateCategory,
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

export default CategoryPage;
