"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, UseFormReturn } from "react-hook-form";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import {
  Select,
  SelectTrigger,
  SelectContent,
  SelectItem,
  SelectValue,
} from "@/components/ui/select";

import { Input } from "@/components/ui/input";
import { Path } from "react-hook-form";
import React from "react";

export type FormFieldType = {
  name: string;
  label: string;
  placeholder: string;
  type?: "text" | "number" | "email" | "password";
  fieldType?: "input" | "select";
  options?: { label: string; value: string }[];
  step?: number;
  validation?: {
    min?: number;
    max?: number;
    required?: boolean;
    pattern?: RegExp;
    message?: string;
  };
  onChange?: (value: string) => void;
};

export type GenericFormProps<T extends z.ZodType> = {
  fields: FormFieldType[];
  schema: T;
  defaultValues: z.infer<T>;
  onSubmit: (data: z.infer<T>) => void;
  onCancel: () => void;
  submitButtonText?: string;
  formRef?: (form: UseFormReturn<z.infer<T>>) => void;
};

export function GenericForm<T extends z.ZodType>({
  fields,
  schema,
  defaultValues,
  onSubmit,
  onCancel,
  submitButtonText = "Submit",
  formRef,
}: GenericFormProps<T>) {
  const form = useForm<z.infer<T>>({
    resolver: zodResolver(schema as never),
    defaultValues,
  });

  React.useEffect(() => {
    if (formRef) {
      formRef(form);
    }
  }, [form, formRef]);

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="w-full space-y-6 max-h-[800px] overflow-y-auto pr-2">
        {fields.map((field) => (
          <FormField
            key={field.name}
            control={form.control}
            name={field.name as Path<z.infer<T>>}
            render={({ field: formField }) => (
              <FormItem>
                <FormLabel>{field.label}</FormLabel>
                <FormControl>
                {field.fieldType === "select" && field.options ? (
                    <Select
                      value={formField.value}
                      onValueChange={(val) => {
                        formField.onChange(val);
                        field.onChange?.(val);
                      }}
                    >
                      <SelectTrigger className="w-full">
                        <SelectValue placeholder={field.placeholder} />
                      </SelectTrigger>
                      <SelectContent>
                        {field.options.map((option) => (
                          <SelectItem key={option.value} value={option.value}>
                            {option.label}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  ) : (
                    <Input
                      type={field.type || "text"}
                      placeholder={field.placeholder}
                      {...formField}
                    />
                  )}
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        ))}
        <div className="flex gap-2">
          <Button type="submit">{submitButtonText}</Button>
          <Button type="button" variant="outline" onClick={onCancel}>Cancel</Button>
        </div>
      </form>
    </Form>
  );
}
