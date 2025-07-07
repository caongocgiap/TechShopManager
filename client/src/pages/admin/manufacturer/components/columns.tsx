"use client";

import { ColumnDef } from "@tanstack/react-table";
import { Button } from "@/components/ui/button";
import { Pencil, Trash2, ArrowLeftRight, ArrowUpDown } from "lucide-react";
import { Manufacturer } from "@/type/index.t";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";
import { Checkbox } from "@/components/ui/checkbox";

interface ColumnProps {
  onEdit: (manufacturer: Manufacturer) => void;
  onDelete: (manufacturer: Manufacturer) => void;
  onToggleStatus: (manufacturer: Manufacturer) => void;
}

const getStatusColor = (status: string) => {
  switch (status.toUpperCase()) {
    case "NOT_DELETED":
      return "bg-green-500/10 text-green-500 hover:bg-green-500/20";
    case "DELETED":
      return "bg-red-500/10 text-red-500 hover:bg-red-500/20";
    default:
      return "bg-gray-500/10 text-gray-500 hover:bg-gray-500/20";
  }
};

const getStatusText = (status: string) => {
  switch (status.toUpperCase()) {
    case "NOT_DELETED":
      return "Đang kinh doanh";
    case "DELETED":
      return "Ngừng kinh doanh";
    default:
      return status;
  }
};

export const createColumns = ({
  onEdit,
  onDelete,
  onToggleStatus,
}: ColumnProps): ColumnDef<Manufacturer>[] => [
  {
    id: "select",
    header: ({ table }) => (
      <Checkbox
        checked={
          table.getIsAllPageRowsSelected() ||
          (table.getIsSomePageRowsSelected() && "indeterminate")
        }
        onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
        aria-label="Select all"
      />
    ),
    cell: ({ row }) => (
      <Checkbox
        checked={row.getIsSelected()}
        onCheckedChange={(value) => row.toggleSelected(!!value)}
        aria-label="Select row"
      />
    ),
    enableSorting: false,
    enableHiding: false,
  },
  {
    accessorKey: "orderNumber",
    header: "STT",
  },
  {
    accessorKey: "manufacturerCode",
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Mã nhà sản xuất
          <ArrowUpDown className="h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => <div>{row.getValue("manufacturerCode")}</div>,
  },
  {
    accessorKey: "manufacturerName",
    header: "Tên nhà sản xuất",
    cell: ({ row }) => <div>{row.original.manufacturerName}</div>,
  },
  {
    accessorKey: "manufacturerCountry",
    header: "Quốc gia",
  },
  {
    accessorKey: "manufacturerWebsite",
    header: "URL trang web",
  },
  {
    accessorKey: "manufacturerDescription",
    header: "Mô tả",
  },
  {
    accessorKey: "manufacturerStatus",
    header: "Trạng thái",
    cell: ({ row }) => {
      const status = row.getValue("manufacturerStatus") as string;
      return (
        <Badge
          variant="secondary"
          className={cn("font-medium", getStatusColor(status))}
        >
          {getStatusText(status)}
        </Badge>
      );
    },
  },
  {
    id: "actions",
    header: "Hành động",
    cell: ({ row }) => {
      const manufacturer = row.original;
      const isActive = manufacturer.manufacturerStatus.toUpperCase() === "NOT_DELETED";
      return (
        <div className="flex items-center gap-2">
          <Button
            variant="ghost"
            size="icon"
            className={cn(
              isActive
                ? "text-green-500 hover:text-green-600"
                : "text-red-500 hover:text-red-600"
            )}
            onClick={() => onToggleStatus(manufacturer)}
          >
            <ArrowLeftRight className="h-4 w-4" />
          </Button>
          <Button variant="ghost" size="icon" onClick={() => onEdit(manufacturer)}>
            <Pencil className="h-4 w-4" />
          </Button>
          <Button
            variant="ghost"
            size="icon"
            onClick={() => onDelete(manufacturer)}
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        </div>
      );
    },
  },
];
