import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { useState } from "react";
import { HTTP_STATUS } from "@/constants/index.constant";
import { toast } from "sonner";
import { AxiosError } from "axios";
import { CategoryDetail } from "..";
import SpinnerLoading from "@/components/ui/SpinnerLoading";
import { FormUpdateCategory } from "./form-update-category";
import { ConfirmDialog } from "@/components/ui/confirm-dialog";
import { INTERNAL_SERVER_ERROR } from "@/constants/message.constant";
import { useUpdateCategory } from "@/service/actions/category.action";

interface Props {
  isOpenDialog: boolean;
  setIsOpenDialog: (value: boolean) => void;
  dataUpdate: CategoryDetail;
  setDataUpdate: (value: CategoryDetail) => void;
}

export function UpdateCategoryDialog({
  isOpenDialog,
  setIsOpenDialog,
  dataUpdate,
  setDataUpdate,
}: Props) {
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const { mutateAsync: update, isPending: loadingUpdate } = useUpdateCategory();

  const handleValueChangeInput = (name: string, value: string) => {
    setDataUpdate({ ...dataUpdate, [name]: value });
  };

  const handleOpenConfirmDialog = () => {
    setIsOpenDialog(false);
    setShowConfirmDialog(true);
  };

  const handleCancelConfirmDialog = () => {
    setShowConfirmDialog(false);
    setIsOpenDialog(true);
  };

  const handleUpdateCategory = async () => {
    try {
      const res = await update({
        data: {
          categoryCode: dataUpdate.categoryCode,
          categoryName: dataUpdate.categoryName,
        },
        id: dataUpdate.categoryId,
      });

      if (res.status === HTTP_STATUS.OK) {
        toast.success(res.message);
        setIsOpenDialog(false);
      } else {
        toast.error(res.message);
      }
    } catch (error) {
      if (error instanceof AxiosError) {
        const data = error.response?.data;
        if (data.message) {
          toast.error(data.message);
        } else if (typeof data === "object") {
          Object.values(data).forEach((msg) => {
            if (typeof msg === "string") toast.error(msg);
          });
        } else {
          toast.error(INTERNAL_SERVER_ERROR);
        }
      } else {
        toast.error(INTERNAL_SERVER_ERROR);
      }
    }
  };

  return (
    <>
      {loadingUpdate && <SpinnerLoading />}
      <Dialog open={isOpenDialog} onOpenChange={setIsOpenDialog}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle>Cập nhật thể loại</DialogTitle>
          </DialogHeader>
          <FormUpdateCategory
            onSubmit={handleOpenConfirmDialog}
            onCancel={() => setIsOpenDialog(false)}
            dataUpdate={{
              categoryCode: dataUpdate.categoryCode,
              categoryName: dataUpdate.categoryName,
            }}
            handleValueChangeInput={handleValueChangeInput}
          />
        </DialogContent>
      </Dialog>
      <ConfirmDialog
        open={showConfirmDialog}
        onOpenChange={setShowConfirmDialog}
        title="Xác nhận cập nhật thể loại"
        description="Bạn có chắc chắn muốn cập nhật thể loại này không?"
        onConfirm={handleUpdateCategory}
        onCancel={handleCancelConfirmDialog}
      />
    </>
  );
}
