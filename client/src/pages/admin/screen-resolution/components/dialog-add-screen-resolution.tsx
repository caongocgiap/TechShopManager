import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { FormAddScreenResolution } from "./form-add-screen-resolution";
import { UseFormReturn } from "react-hook-form";
import { useState } from "react";
import { HTTP_STATUS } from "@/constants/index.constant";
import { toast } from "sonner";
import { AxiosError } from "axios";
import SpinnerLoading from "@/components/ui/SpinnerLoading";
import { ConfirmDialog } from "@/components/ui/confirm-dialog";
import { INTERNAL_SERVER_ERROR } from "@/constants/message.constant";
import { useCreateScreenResolution } from "@/service/actions/screen-resolution.action";

interface FormData {
  screenResolutionCode: string;
  screenResolutionName: string;
  screenResolutionWidth: number;
  screenResolutionHeight: number;
}

interface Props {
  isOpenDialog: boolean;
  setIsOpenDialog: (value: boolean) => void;
}

export function CreateScreenResolutionDialog(props: Props) {
  const { isOpenDialog, setIsOpenDialog } = props;
  const [form, setForm] = useState<UseFormReturn<FormData>>();
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const [formValues, setFormValues] = useState<FormData | null>(null);
  const { mutateAsync: create, isPending: loadingCreate } = useCreateScreenResolution();

  const handleOpenConfirmDialog = () => {
    const currentValues = form?.getValues();
    if (currentValues) {
      setFormValues(currentValues);
    }
    setIsOpenDialog(false);
    setShowConfirmDialog(true);
  };

  const handleCancelConfirmDialog = () => {
    setShowConfirmDialog(false);
    setIsOpenDialog(true);
  };

  const handleCreateScreenResolution = async () => {
    try {
      const formValue = form?.getValues();
      if (!formValue) return;

      const res = await create({
        screenResolutionCode: formValue.screenResolutionCode,
        screenResolutionName: formValue.screenResolutionName,
        screenResolutionWidth: formValue.screenResolutionWidth,
        screenResolutionHeight: formValue.screenResolutionHeight,
      });

      if (res.status === HTTP_STATUS.OK) {
        toast.success(res.message);
        setIsOpenDialog(false);
        setFormValues(null);
        form?.reset();
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
      {loadingCreate && <SpinnerLoading />}
      <Dialog open={isOpenDialog} onOpenChange={setIsOpenDialog}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle>Thêm độ phân giải</DialogTitle>
          </DialogHeader>
          <FormAddScreenResolution
            onSubmit={handleOpenConfirmDialog}
            onCancel={() => {
              setIsOpenDialog(false);
              setFormValues(null);
              form?.reset();
            }}
            formRef={setForm}
            defaultValues={formValues || undefined}
          />
        </DialogContent>
      </Dialog>

      <ConfirmDialog
        open={showConfirmDialog}
        onOpenChange={setShowConfirmDialog}
        title="Xác nhận thêm độ phân giải"
        description="Bạn có chắc chắn muốn thêm độ phân giải này không?"
        onConfirm={handleCreateScreenResolution}
        onCancel={handleCancelConfirmDialog}
      />
    </>
  );
}
