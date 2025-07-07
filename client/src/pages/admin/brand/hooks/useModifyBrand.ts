import { INTERNAL_SERVER_ERROR } from "@/constants/message.constant";
import { useCreateBrand, useUpdateBrand } from "@/service/actions/brand.action";
import { ModifyBrand } from "@/type/index.t";
import { useForm } from "react-hook-form";
import { toast } from "sonner";

export const useModifyBrand = () => {
  const form = useForm();
  const {
    mutateAsync: createBrand,
    isPending: loadingModifyBrand,
    isError: isErrorModifyBrand,
  } = useCreateBrand();

  const { mutateAsync: updateBrand } = useUpdateBrand();

  const handleCreateBrand = async () => {
    try {
      const values = form.getValues();

      const brand: ModifyBrand = {
        brandCode: values.brandCode,
        brandName: values.brandName,
      };

      const res = await createBrand(brand);
      if (res?.success) {
        toast.success("Thêm thương hiệu thành công");
      } else {
        toast.error(res?.message || "Thêm thương hiệu thất bại");
      }
    } catch (e) {
      toast.error(e?.response?.data?.message || INTERNAL_SERVER_ERROR);
    }
  };

  return {
    form,
    handleCreateBrand,
    loadingModifyBrand,
    isErrorModifyBrand,
  };
};
