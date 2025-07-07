import {
  FormFieldType,
  GenericForm,
} from "@/components/common/GenericFormCustom";
import { UseFormReturn } from "react-hook-form";
import z from "zod";

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

const formSchema = z.object({
  operationSystemCode: z.string().min(2, {
    message: "Mã hệ điều hành phải từ 2 đến 255 ký tự!",
  }),
  operationSystemName: z.string().min(2, {
    message: "Tên hệ điều hành phải từ 2 đến 255 ký tự!",
  }),
});

type Props = {
  onSubmit: (data: z.infer<typeof formSchema>) => void;
  formRef?: (form: UseFormReturn<z.infer<typeof formSchema>>) => void;
  onCancel: () => void;
  defaultValues?: z.infer<typeof formSchema>;
};

export function FormAddOperationSystem({
  onSubmit,
  formRef,
  onCancel,
  defaultValues = {
    operationSystemCode: "",
    operationSystemName: "",
  },
}: Props) {
  return (
    <GenericForm
      fields={fields}
      schema={formSchema}
      defaultValues={defaultValues}
      onSubmit={onSubmit}
      onCancel={onCancel}
      submitButtonText="Thêm hệ điều hành"
      formRef={formRef}
    />
  );
}
