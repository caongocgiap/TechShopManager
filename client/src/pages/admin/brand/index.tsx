/* eslint-disable @typescript-eslint/no-explicit-any */
import { useCallback, useState } from "react";
  import  DialogAddBrand  from "./components/dialog-add-brand";
import { Button } from "@/components/ui/button";
import SpinnerLoading from "@/components/ui/SpinnerLoading";
import { useBrand } from "./hooks/useBrand";
import BrandTable from "./components/brand-table";
import useToggle from "@/hooks/useToggle";
import { ResponseFetchListBrand } from "@/type/index.t";
import DialgoUpdateBrand from "./components/dialog-update-brand";
import BrandFilter from "./components/brand-filter";


const ManageBrand = () => {
  const {
    brandData,
    totalPages,
    totalElements,
    isLoading,
    isPending,
    // setPaginationParams,
    paginationParams,
    setSearchValue,
    handlePageChange,
    handleToggleStatus,
    handleCreateBrand,
    ConfirmDialogComponent,
    handleUpdateBrand,
  } = useBrand();

  const { value: isOpenAddBrandDialog, setValue: setIsOpenAddBrandDialog } = useToggle();
  const { value: isOpenEditBrandDialog, setValue: setIsOpenUpdateBrandDialog } = useToggle();
  const [dataUpdate, setDataUpdate] = useState<ResponseFetchListBrand | null>();
  
  
  const handleAddBrand = useCallback(() => {
    setIsOpenAddBrandDialog(true);
  }, [setIsOpenAddBrandDialog]);

  const openUpdateBrandDialog = useCallback(
    (data: any, idBrand: string) => {
      setIsOpenUpdateBrandDialog(true);
      setDataUpdate({
        brandId: idBrand,
        brandCode: data.brandCode,
        brandName: data.brandName,
      });
    },
    [setIsOpenUpdateBrandDialog]
  );

  return (
    <>
      {(isLoading || isPending) && <SpinnerLoading />}
      <div className="px-4">
        <h1 className="text-4xl font-bold text-center text-foreground uppercase mb-5">
          Quản lý Thương hiệu
        </h1>
        <div className="flex items-center justify-between mb-5">
          <BrandFilter searchValue={setSearchValue} />
          <Button onClick={handleAddBrand} variant="outline">
            Thêm thương hiệu
          </Button>
        </div>
        <div className="mx-auto">
          <BrandTable
            data={brandData}
            paginationParams={paginationParams}
            totalPages={totalPages}
            totalElements={totalElements}
            isLoading={isLoading}
            handlePageChange={handlePageChange}
            handleEditBrand={openUpdateBrandDialog}
            handleToggleStatus={handleToggleStatus}
          />
        </div>
        <DialogAddBrand 
          open={isOpenAddBrandDialog} 
          setOpen={setIsOpenAddBrandDialog} 
          onSubmit={handleCreateBrand}
          isPending={isPending}
        />
        <DialgoUpdateBrand 
          isOpen={isOpenEditBrandDialog} 
          setIsOpen={setIsOpenUpdateBrandDialog} 
          dataUpdate={dataUpdate} 
          onSubmit={handleUpdateBrand}
          isPending={isPending}
        />
        <ConfirmDialogComponent />
      </div>
    </>
  );
};

export default ManageBrand;
