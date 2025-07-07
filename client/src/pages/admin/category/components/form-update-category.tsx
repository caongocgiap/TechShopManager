import {
  FormFieldType,
  GenericForm,
} from "@/components/common/GenericFormCustom";
import z from "zod";
import { useEffect, useState } from "react";
import { UseFormReturn } from "react-hook-form";

const formSchema = z.object({
  categoryCode: z.string().min(2, {
    message: "Mã thể loại phải từ 2 đến 255 ký tự!",
  }),
  categoryName: z.string().min(2, {
    message: "Tên thể loại phải từ 2 đến 255 ký tự!",
  }),
});

type FormData = z.infer<typeof formSchema>;

type Props = {
  onSubmit: (data: FormData) => void;
  onCancel: () => void;
  dataUpdate?: FormData;
  handleValueChangeInput: (name: string, value: string) => void;
};

export function FormUpdateCategory({
  onSubmit,
  onCancel,
  dataUpdate,
  handleValueChangeInput,
}: Props) {
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

  const defaultValues = dataUpdate || {
    categoryCode: "",
    categoryName: "",
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
      submitButtonText="Cập nhật thể loại"
      formRef={setForm}
    />
  );
}
