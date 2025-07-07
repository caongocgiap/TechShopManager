import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { FormAddManufacturer } from "./form-add-manufacturer";
import { UseFormReturn } from "react-hook-form";
import { useState } from "react";
import { HTTP_STATUS } from "@/constants/index.constant";
import { toast } from "sonner";
import { AxiosError } from "axios";
import SpinnerLoading from "@/components/ui/SpinnerLoading";
import { ConfirmDialog } from "@/components/ui/confirm-dialog";
import { INTERNAL_SERVER_ERROR } from "@/constants/message.constant";
import { useCreateManufacturer } from "@/service/actions/manufacturer.action";

interface FormData {
  manufacturerCode: string;
  manufacturerName: string;
  manufacturerCountry: string;
  manufacturerWebsite: string;
  manufacturerDescription: string;
}

interface Props {
  isOpenDialog: boolean;
  setIsOpenDialog: (value: boolean) => void;
}

export function CreateManufacturerDialog(props: Props) {
  const { isOpenDialog, setIsOpenDialog } = props;
  const [form, setForm] = useState<UseFormReturn<FormData>>();
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const [formValues, setFormValues] = useState<FormData | null>(null);
  const { mutateAsync: create, isPending: loadingCreate } =
    useCreateManufacturer();

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

  const handleCreateManufacturer = async () => {
    try {
      const formValue = form?.getValues();
      if (!formValue) return;

      const res = await create({
        manufacturerCode: formValue.manufacturerCode,
        manufacturerName: formValue.manufacturerName,
        manufacturerCountry: formValue.manufacturerCountry,
        manufacturerWebsite: formValue.manufacturerWebsite,
        manufacturerDescription: formValue.manufacturerDescription,
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
            <DialogTitle>Thêm nhà sản xuất</DialogTitle>
          </DialogHeader>
          <FormAddManufacturer
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
        title="Xác nhận thêm nhà sản xuất"
        description="Bạn có chắc chắn muốn thêm nhà sản xuất này không?"
        onConfirm={handleCreateManufacturer}
        onCancel={handleCancelConfirmDialog}
      />
    </>
  );
}
