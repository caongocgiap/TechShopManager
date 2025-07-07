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
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import SpinnerLoading from "@/components/ui/SpinnerLoading";
import { useForm } from "react-hook-form";

const formSchema = z.object({
  brandCode: z.string().min(2, {
    message: "Mã thương hiệu phải từ 2 đến 255 ký tự!",
  }),
  brandName: z.string().min(2, {
    message: "Tên thương hiệu phải từ 2 đến 255 ký tự!",
  }),
});

const DialogAddBrand = ({
  open,
  setOpen,
  onSubmit,
  isPending,
}: {
  open: boolean;
  setOpen: (open: boolean) => void;
  onSubmit?: (data: z.infer<typeof formSchema>) => Promise<boolean>;
  isPending: boolean;
}) => {

  const form = useForm({
    resolver: zodResolver(formSchema),
    defaultValues: {
      brandCode: "",
      brandName: "",
    },
  });

  async function handleSubmit(values: z.infer<typeof formSchema>) {
    if (onSubmit) {
      const success = await onSubmit(values);
      if (success) {
        form?.reset();
        setOpen(false);
      }
    }
  }

  return (
    <>
      {isPending && <SpinnerLoading />}
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="sm:max-w-[425px]">
          <DialogHeader>
            <DialogTitle>Thêm thương hiệu</DialogTitle>
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
                      <Input placeholder="ASUS, ACER, HP, ..." {...field} />
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
                      <Input
                        type="text"
                        placeholder="Asus, Acer, HP, ..."
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <div className="flex justify-end gap-2">
                <Button type="button" onClick={() => setOpen(false)} variant="outline">Hủy</Button>
                <Button type="submit">Lưu</Button>
              </div>
            </form>
          </Form>
        </DialogContent>
      </Dialog>
    </>
  );
};

export default DialogAddBrand;
