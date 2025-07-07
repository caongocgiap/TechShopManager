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
import { OperationSystemDetail } from "..";
import SpinnerLoading from "@/components/ui/SpinnerLoading";
import { FormUpdateOperationSystem } from "./form-update-operation-system";
import { ConfirmDialog } from "@/components/ui/confirm-dialog";
import { INTERNAL_SERVER_ERROR } from "@/constants/message.constant";
import { useUpdateOperationSystem } from "@/service/actions/operation-system.action";

interface Props {
  isOpenDialog: boolean;
  setIsOpenDialog: (value: boolean) => void;
  dataUpdate: OperationSystemDetail;
  setDataUpdate: (value: OperationSystemDetail) => void;
}

export function UpdateOperationSystemDialog({
  isOpenDialog,
  setIsOpenDialog,
  dataUpdate,
  setDataUpdate,
}: Props) {
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const { mutateAsync: update, isPending: loadingUpdate } =
    useUpdateOperationSystem();

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

  const handleUpdateOperationSystem = async () => {
    try {
      const res = await update({
        data: {
          operationSystemCode: dataUpdate.operationSystemCode,
          operationSystemName: dataUpdate.operationSystemName,
        },
        id: dataUpdate.operationSystemId,
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
            <DialogTitle>Cập nhật hệ điều hành</DialogTitle>
          </DialogHeader>
          <FormUpdateOperationSystem
            onSubmit={handleOpenConfirmDialog}
            onCancel={() => setIsOpenDialog(false)}
            dataUpdate={{
              operationSystemCode: dataUpdate.operationSystemCode,
              operationSystemName: dataUpdate.operationSystemName,
            }}
            handleValueChangeInput={handleValueChangeInput}
          />
        </DialogContent>
      </Dialog>
      <ConfirmDialog
        open={showConfirmDialog}
        onOpenChange={setShowConfirmDialog}
        title="Xác nhận cập nhật hệ điều hành"
        description="Bạn có chắc chắn muốn cập nhật hệ điều hành này không?"
        onConfirm={handleUpdateOperationSystem}
        onCancel={handleCancelConfirmDialog}
      />
    </>
  );
}
