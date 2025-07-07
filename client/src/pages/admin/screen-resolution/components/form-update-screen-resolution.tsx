import {
  FormFieldType,
  GenericForm,
} from "@/components/common/GenericFormCustom";
import z from "zod";
import { useEffect, useState } from "react";
import { UseFormReturn } from "react-hook-form";

const formSchema = z.object({
  screenResolutionCode: z.string().min(2, {
    message: "Mã độ phân giải phải từ 2 đến 255 ký tự!",
  }),
  screenResolutionName: z.string().min(2, {
    message: "Độ phân giải phải từ 2 đến 255 ký tự!",
  }),
  screenResolutionWidth: z.coerce.number()
    .min(320, { message: "Chiều rộng phải lớn hơn hoặc bằng 320px" })
    .max(7680, { message: "Chiều rộng không được vượt quá 7680px" }),
  screenResolutionHeight: z.coerce.number()
    .min(240, { message: "Chiều cao phải lớn hơn hoặc bằng 240px" })
    .max(4320, { message: "Chiều cao không được vượt quá 4320px" }),
});

type FormData = z.infer<typeof formSchema>;

type Props = {
  onSubmit: (data: FormData) => void;
  onCancel: () => void;
  dataUpdate?: FormData;
  handleValueChangeInput: (name: string, value: string) => void;
};

export function FormUpdateScreenResolution({
  onSubmit,
  onCancel,
  dataUpdate,
  handleValueChangeInput,
}: Props) {
  const fields: FormFieldType[] = [
    {
      name: "screenResolutionCode",
      label: "Mã độ phân giải",
      placeholder: "FHD, QHD, ...",
      validation: {
        min: 2,
        max: 255,
        required: true,
        message: "Mã độ phân giải phải từ 2 đến 255 ký tự!",
      },
    },
    {
      name: "screenResolutionName",
      label: "Độ phân giải",
      placeholder: "Full HD, 4K, ...",
      validation: {
        min: 2,
        max: 255,
        required: true,
        message: "Độ phân giải phải từ 2 đến 255 ký tự!",
      },
    },
    {
      name: "screenResolutionWidth",
      label: "Số điểm ảnh theo chiều ngang",
      placeholder: "1920, 2560, ...",
      validation: {
        min: 320,
        max: 7680,
        required: true,
        message: "Chiều rộng phải từ 320px đến 7680px",
      },
    },
    {
      name: "screenResolutionHeight",
      label: "Số điểm ảnh theo chiều dọc",
      placeholder: "1080, 1440, ...",
      validation: {
        min: 240,
        max: 4320,
        required: true,
        message: "Chiều cao phải từ 240px đến 4320px",
      },
    },
  ];

  const defaultValues = dataUpdate || {
    screenResolutionCode: "",
    screenResolutionName: "",
    screenResolutionWidth: 0,
    screenResolutionHeight: 0,
  };

  const [form, setForm] = useState<UseFormReturn<FormData>>();

  useEffect(() => {
    if (form) {
      const subscription = form.watch((value, { name, type }) => {
        if (name && type === "change") {
          handleValueChangeInput(name, value[name as keyof FormData] as string);
        }
      });
      return () => subscription.unsubscribe();
    }
  }, [form, handleValueChangeInput]);

  return (
    <GenericForm
      fields={fields}
      schema={formSchema}
      defaultValues={defaultValues}
      onSubmit={onSubmit}
      onCancel={onCancel}
      submitButtonText="Cập nhật độ phân giải"
      formRef={setForm}
    />
  );
}
