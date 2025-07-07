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
import { ScreenResolutionDetail } from "..";
import SpinnerLoading from "@/components/ui/SpinnerLoading";
import { FormUpdateScreenResolution } from "./form-update-screen-resolution";
import { ConfirmDialog } from "@/components/ui/confirm-dialog";
import { INTERNAL_SERVER_ERROR } from "@/constants/message.constant";
import { useUpdateScreenResolution } from "@/service/actions/screen-resolution.action";

interface Props {
  isOpenDialog: boolean;
  setIsOpenDialog: (value: boolean) => void;
  dataUpdate: ScreenResolutionDetail;
  setDataUpdate: (value: ScreenResolutionDetail) => void;
}

export function UpdateScreenResolutionDialog({
  isOpenDialog,
  setIsOpenDialog,
  dataUpdate,
  setDataUpdate,
}: Props) {
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const { mutateAsync: update, isPending: loadingUpdate } =
    useUpdateScreenResolution();

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

  const handleUpdateScreenResolution = async () => {
    try {
      const res = await update({
        data: {
          screenResolutionCode: dataUpdate.screenResolutionCode,
          screenResolutionName: dataUpdate.screenResolutionName,
          screenResolutionWidth: dataUpdate.screenResolutionWidth,
          screenResolutionHeight: dataUpdate.screenResolutionHeight,
        },
        id: dataUpdate.screenResolutionId,
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
            <DialogTitle>Cập nhật độ phân giải</DialogTitle>
          </DialogHeader>
          <FormUpdateScreenResolution
            onSubmit={handleOpenConfirmDialog}
            onCancel={() => setIsOpenDialog(false)}
            dataUpdate={{
              screenResolutionCode: dataUpdate.screenResolutionCode,
              screenResolutionName: dataUpdate.screenResolutionName,
              screenResolutionWidth: dataUpdate.screenResolutionWidth,
              screenResolutionHeight: dataUpdate.screenResolutionHeight,
            }}
            handleValueChangeInput={handleValueChangeInput}
          />
        </DialogContent>
      </Dialog>
      <ConfirmDialog
        open={showConfirmDialog}
        onOpenChange={setShowConfirmDialog}
        title="Xác nhận cập nhật độ phân giải"
        description="Bạn có chắc chắn muốn cập nhật độ phân giải này không?"
        onConfirm={handleUpdateScreenResolution}
        onCancel={handleCancelConfirmDialog}
      />
    </>
  );
}
