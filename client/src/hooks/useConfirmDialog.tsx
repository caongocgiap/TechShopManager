import React, { useState } from "react";
import { ConfirmDialog } from "@/components/ui/confirm-dialog";

interface ConfirmDialogOptions {
  title: string;
  description: string;
  confirmText?: string;
  cancelText?: string;
}

interface ConfirmDialogHook {
  confirm: (options: ConfirmDialogOptions) => Promise<boolean>;
  ConfirmDialogComponent: React.FC;
}

export const useConfirmDialog = (): ConfirmDialogHook => {
  const [isOpen, setIsOpen] = useState(false);
  const [options, setOptions] = useState<ConfirmDialogOptions>({
    title: "",
    description: "",
    confirmText: "Xác nhận",
    cancelText: "Hủy",
  });
  const [resolvePromise, setResolvePromise] = useState<((value: boolean) => void) | null>(null);

  const confirm = (dialogOptions: ConfirmDialogOptions): Promise<boolean> => {
    setOptions(dialogOptions);
    setIsOpen(true);
    return new Promise<boolean>((resolve) => {
      setResolvePromise(() => resolve);
    });
  };

  const handleConfirm = () => {
    setIsOpen(false);
    resolvePromise?.(true);
  };

  const handleCancel = () => {
    setIsOpen(false);
    resolvePromise?.(false);
  };

  const ConfirmDialogComponent: React.FC = () => {
    return (
      <ConfirmDialog
        open={isOpen}
        onOpenChange={setIsOpen}
        title={options.title}
        description={options.description}
        onConfirm={handleConfirm}
        onCancel={handleCancel}
        confirmText={options.confirmText}
        cancelText={options.cancelText}
      />
    );
  };

  return {
    confirm,
    ConfirmDialogComponent,
  };
}; 