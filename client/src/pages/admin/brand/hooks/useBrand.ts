/* eslint-disable @typescript-eslint/no-explicit-any */
import { useImmer } from "use-immer";
import { BrandPaginationParams } from "@/service/api/brand.api";
import { useCallback, useMemo } from "react";
import { useGetAllBrand, useUpdateBrandStatus, useCreateBrand, useUpdateBrand } from "@/service/actions/brand.action";
import { useDebounce } from "use-debounce";
import { useConfirmDialog } from "@/hooks/useConfirmDialog";
import { toast } from "sonner";
import { INTERNAL_SERVER_ERROR } from "@/constants/message.constant";

export const useBrand = () => {
  const { mutateAsync: changeBrandStatus, isPending: isPendingToggle } = useUpdateBrandStatus();
  const { mutateAsync: createBrand, isPending: isPendingCreate } = useCreateBrand();
  const { mutateAsync: updateBrand, isPending: isPendingUpdate } = useUpdateBrand();
  const { confirm, ConfirmDialogComponent } = useConfirmDialog();
  
  const [paginationParams, setPaginationParams] =
  useImmer<BrandPaginationParams>({
      page: 1,
      size: 10,
    });
    
  const [searchValue, setSearchValue] = useImmer<BrandPaginationParams>({
    ...paginationParams,
  });
  
  const [searchValueDebounce] = useDebounce(searchValue, 500);

  const {
    data: dataFetchingBrand,
    isLoading,
    isError,
  } = useGetAllBrand({
    ...paginationParams,
    ...searchValueDebounce,
  });

  const brandData = useMemo(() => {
    return (
      dataFetchingBrand?.data?.data?.map((item) => ({
        ...item,
        key: item.brandId,
      })) || []
    );
  }, [dataFetchingBrand]);

  const totalPages = useMemo(() => {
    return dataFetchingBrand?.data?.totalPages || 0;
  }, [dataFetchingBrand]);

  const totalElements = useMemo(() => {
    return dataFetchingBrand?.data?.totalElements || 0;
  }, [dataFetchingBrand]);

  const handlePageChange = useCallback((page: number) => {
    setPaginationParams((draft) => {
      draft.page = page;
    });
  }, [setPaginationParams]);

  const handleToggleStatus = useCallback(
    async (id: string) => {
      const isConfirmed = await confirm({
        title: "Thay đổi trạng thái thương hiệu",
        description:
          "Bạn có chắc chắn muốn thay đổi trạng thái của thương hiệu này?",
      });

      if (isConfirmed) {
        try {
          const res = await changeBrandStatus(id);
          toast.success(res?.message);
        } catch (error: any) {
          toast.error(error?.response?.data?.message || INTERNAL_SERVER_ERROR);
        }
      }
    },
    [confirm, changeBrandStatus]
  );

  const handleCreateBrand = useCallback(
    async (data: any) => {
      const isConfirmed = await confirm({
        title: "Thêm thương hiệu",
        description: "Bạn có chắc chắn muốn thêm thương hiệu này?",
      });

      if (isConfirmed) {
        try {
          const res = await createBrand(data);
          toast.success(res?.message);
          // Reset về trang 1 sau khi tạo thành công
          setPaginationParams((draft) => {
            draft.page = 1;
          });
          return true;
        } catch (error: any) {
          toast.error(error?.response?.data?.message || INTERNAL_SERVER_ERROR);
          return false;
        }
      }
      return false;
    },
    [confirm, createBrand, setPaginationParams]
  );

  const handleUpdateBrand = useCallback(
    async (data: any, brandId: string) => {
      const isConfirmed = await confirm({
        title: "Cập nhật thương hiệu",
        description: "Bạn có chắc chắn muốn cập nhật thương hiệu này?",
      });
      if (isConfirmed) {
        try {
          const res = await updateBrand({ data, id: brandId });
          toast.success(res?.message);
          // Reset về trang 1 sau khi cập nhật thành công
          setPaginationParams((draft) => {
            draft.page = 1;
          });
          return true;
        } catch (error: any) {
          toast.error(error?.response?.data?.message || INTERNAL_SERVER_ERROR);
          return false;
        }
      }
      return false;
    },
    [confirm, updateBrand, setPaginationParams]
  );

  return {
    brandData,
    totalPages,
    totalElements,
    isLoading,
    isPending: isPendingToggle || isPendingCreate || isPendingUpdate,
    isError,
    setPaginationParams,
    paginationParams,
    setSearchValue,
    handlePageChange,
    handleToggleStatus,
    handleCreateBrand,
    handleUpdateBrand,
    ConfirmDialogComponent,
  };
};
