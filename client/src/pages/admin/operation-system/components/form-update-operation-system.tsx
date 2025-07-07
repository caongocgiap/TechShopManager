import {
  FormFieldType,
  GenericForm,
} from "@/components/common/GenericFormCustom";
import z from "zod";
import { useEffect, useState } from "react";
import { UseFormReturn } from "react-hook-form";

const formSchema = z.object({
  operationSystemCode: z.string().min(2, {
    message: "Mã hệ điều hành phải từ 2 đến 255 ký tự!",
  }),
  operationSystemName: z.string().min(2, {
    message: "Tên hệ điều hành phải từ 2 đến 255 ký tự!",
  }),
});

type FormData = z.infer<typeof formSchema>;

type Props = {
  onSubmit: (data: FormData) => void;
  onCancel: () => void;
  dataUpdate?: FormData;
  handleValueChangeInput: (name: string, value: string) => void;
};

export function FormUpdateOperationSystem({
  onSubmit,
  onCancel,
  dataUpdate,
  handleValueChangeInput,
}: Props) {
  const fields: FormFieldType[] = [
    {
      name: "operationSystemCode",
      label: "Mã hệ điều hành",
      placeholder: "WINDOWS, MACOS, LINUX, ...",
      validation: {
        min: 2,
        max: 255,
        required: true,
        message: "Mã hệ điều hành phải từ 2 đến 255 ký tự!",
      },
    },
    {
      name: "operationSystemName",
      label: "Tên hệ điều hành",
      placeholder: "Windows, MacOS, Linux, ...",
      validation: {
        min: 2,
        max: 255,
        required: true,
        message: "Tên hệ điều hành phải từ 2 đến 255 ký tự!",
      },
    },
  ];

  const defaultValues = dataUpdate || {
    operationSystemCode: "",
    operationSystemName: "",
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
      submitButtonText="Cập nhật hệ điều hành"
      formRef={setForm}
    />
  );
}
