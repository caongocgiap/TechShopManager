"use client";

import { ColumnDef } from "@tanstack/react-table";
import { Button } from "@/components/ui/button";
import { Pencil, Trash2, ArrowLeftRight, ArrowUpDown } from "lucide-react";
import { Category } from "@/type/index.t";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";
import { Checkbox } from "@/components/ui/checkbox";

interface ColumnProps {
  onEdit: (category: Category) => void;
  onDelete: (category: Category) => void;
  onToggleStatus: (category: Category) => void;
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
}: ColumnProps): ColumnDef<Category>[] => [
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
    accessorKey: "categoryCode",
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Mã thể loại
          <ArrowUpDown className="h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => <div>{row.getValue("categoryCode")}</div>,
  },
  {
    accessorKey: "categoryName",
    header: "Tên thể loại",
  },
  {
    accessorKey: "categoryStatus",
    header: "Trạng thái",
    cell: ({ row }) => {
      const status = row.getValue("categoryStatus") as string;
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
      const category = row.original;
      const isActive = category.categoryStatus.toUpperCase() === "NOT_DELETED";
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
            onClick={() => onToggleStatus(category)}
          >
            <ArrowLeftRight className="h-4 w-4" />
          </Button>
          <Button variant="ghost" size="icon" onClick={() => onEdit(category)}>
            <Pencil className="h-4 w-4" />
          </Button>
          <Button
            variant="ghost"
            size="icon"
            onClick={() => onDelete(category)}
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        </div>
      );
    },
  },
];
