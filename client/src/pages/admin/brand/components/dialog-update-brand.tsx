/* eslint-disable @typescript-eslint/no-explicit-any */
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useEffect } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import SpinnerLoading from "@/components/ui/SpinnerLoading";

const formSchema = z.object({
  brandCode: z.string().min(2, {
    message: "Mã thương hiệu phải từ 2 đến 255 ký tự!",
  }),
  brandName: z.string().min(2, {
    message: "Tên thương hiệu phải từ 2 đến 255 ký tự!",
  }),
});

export function DialgoUpdateBrand({
  isOpen,
  setIsOpen,
  dataUpdate,
  onSubmit,
  isPending,
}: {
  isOpen: boolean;
  setIsOpen: (open: boolean) => void;
  dataUpdate: any;
  onSubmit?: (data: any, brandId: string) => Promise<boolean>;
  isPending: boolean;
}) {
  const form = useForm({
    resolver: zodResolver(formSchema),
    defaultValues: {
      brandCode: dataUpdate ? dataUpdate.brandCode : null,
      brandName: dataUpdate ? dataUpdate.brandName : null,
    },
  });

  useEffect(() => {
    if (dataUpdate) {
      form.reset({
        brandCode: dataUpdate.brandCode,
        brandName: dataUpdate.brandName,
      });
    }
  }, [dataUpdate, form]);

  async function handleSubmit(value: any) {
    if (onSubmit) {
      const success = await onSubmit(value, dataUpdate.brandId);
      if (success) {
        form?.reset();
        setIsOpen(false);
      }
    }
  }

  return (
    <>
      {isPending && <SpinnerLoading />}
      <Dialog open={isOpen} onOpenChange={setIsOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Cập nhật thương hiệu</DialogTitle>
          </DialogHeader>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-4">
              <FormField
                control={form.control}
                name="brandCode"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Mã thương hiệu</FormLabel>
                    <FormControl>
                      <Input {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="brandName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Tên thương hiệu</FormLabel>
                    <FormControl>
                      <Input {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <div className="flex justify-end gap-2">
                <Button
                  type="button"
                  onClick={() => setIsOpen(false)}
                  variant="outline"
                >
                  Hủy
                </Button>
                <Button type="submit">Cập nhật</Button>
              </div>
            </form>
          </Form>
        </DialogContent>
      </Dialog>
    </>
  );
}

export default DialgoUpdateBrand;
