import {
  FormFieldType,
  GenericForm,
} from "@/components/common/GenericFormCustom";
import { UseFormReturn } from "react-hook-form";
import z from "zod";

const fields: FormFieldType[] = [
  {
    name: "categoryCode",
    label: "Mã thể loại",
    placeholder: "Gaming, Văn phòng, ...",
    validation: {
      min: 2,
      max: 255,
      required: true,
      message: "Mã thể loại phải từ 2 đến 255 ký tự!",
    },
  },
  {
    name: "categoryName",
    label: "Tên thể loại",
    placeholder: "Gaming, Văn phòng, ...",
    validation: {
      min: 2,
      max: 255,
      required: true,
      message: "Tên thể loại phải từ 2 đến 255 ký tự!",
    },
  },
];

const formSchema = z.object({
  categoryCode: z.string().min(2, {
    message: "Mã thể loại phải từ 2 đến 255 ký tự!",
  }),
  categoryName: z.string().min(2, {
    message: "Tên thể loại phải từ 2 đến 255 ký tự!",
  }),
});

type Props = {
  onSubmit: (data: z.infer<typeof formSchema>) => void;
  formRef?: (form: UseFormReturn<z.infer<typeof formSchema>>) => void;
  onCancel: () => void;
  defaultValues?: z.infer<typeof formSchema>;
};

export function FormAddCategory({
  onSubmit,
  formRef,
  onCancel,
  defaultValues = {
    categoryCode: "",
    categoryName: "",
  },
}: Props) {
  return (
    <GenericForm
      fields={fields}
      schema={formSchema}
      defaultValues={defaultValues}
      onSubmit={onSubmit}
      onCancel={onCancel}
      submitButtonText="Thêm thể loại"
      formRef={formRef}
    />
  );
}
