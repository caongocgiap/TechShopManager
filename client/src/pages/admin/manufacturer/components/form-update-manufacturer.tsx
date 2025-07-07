import {
  FormFieldType,
  GenericForm,
} from "@/components/common/GenericFormCustom";
import z from "zod";
import { useEffect, useState } from "react";
import { UseFormReturn } from "react-hook-form";

const fields: FormFieldType[] = [
  {
    name: "manufacturerCode",
    label: "Mã nhà sản xuất",
    placeholder: "INTEL",
  },
  {
    name: "manufacturerName",
    label: "Tên nhà sản xuất",
    placeholder: "Intel Corporation",
  },
  {
    name: "manufacturerCountry",
    label: "Quốc gia",
    placeholder: "Mỹ",
  },
  {
    name: "manufacturerWebsite",
    label: "URL trang web",
    placeholder: "https://www.intel.com",
  },
  {
    name: "manufacturerDescription",
    label: "Mô tả",
    placeholder:
      "Intel Corporation là một công ty công nghệ lớn, sản xuất chip xử lý và bộ nhớ cho các thiết bị điện tử.",
  },
];

const formSchema = z.object({
  manufacturerCode: z.string().min(1, {
    message: "Mã nhà sản xuất không được để trống!",
  }).max(255, {
    message: "Mã nhà sản xuất không được vượt quá 255 ký tự!",
  }),
  manufacturerName: z.string().min(1, {
    message: "Tên nhà sản xuất không được để trống!",
  }).max(255, {
    message: "Tên nhà sản xuất không được vượt quá 255 ký tự!",
  }),
  manufacturerCountry: z.string().min(1, {
    message: "Quốc gia không được để trống!",
  }).max(255, {
    message: "Quốc gia không được vượt quá 255 ký tự!",
  }),
  manufacturerWebsite: z.string().min(1, {
    message: "URL trang web không được để trống!",
  }).max(255, {
    message: "URL trang web không được vượt quá 255 ký tự!",
  }),
  manufacturerDescription: z.string().min(1, {
    message: "Mô tả không được để trống!",
  }).max(128, {
    message: "Mô tả không được vượt quá 128 ký tự!",
  }),
});

type FormData = z.infer<typeof formSchema>;

type Props = {
  onSubmit: (data: FormData) => void;
  onCancel: () => void;
  dataUpdate?: FormData;
  handleValueChangeInput: (name: string, value: string) => void;
};

export function FormUpdateManufacturer({
  onSubmit,
  onCancel,
  dataUpdate,
  handleValueChangeInput,
}: Props) {

  const defaultValues = dataUpdate || {
    manufacturerCode: "",
    manufacturerName: "",
    manufacturerCountry: "",
    manufacturerWebsite: "",
    manufacturerDescription: "",
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
      submitButtonText="Cập nhật nhà sản xuất"
      formRef={setForm}
    />
  );
}
