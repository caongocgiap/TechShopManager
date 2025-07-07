"use client";

import { ColumnDef } from "@tanstack/react-table";
import { Button } from "@/components/ui/button";
import { Pencil, Trash2, ArrowLeftRight, ArrowUpDown } from "lucide-react";
import { OperationSystem } from "@/type/index.t";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";
import { Checkbox } from "@/components/ui/checkbox";

interface ColumnProps {
  onEdit: (operationSystem: OperationSystem) => void;
  onDelete: (operationSystem: OperationSystem) => void;
  onToggleStatus: (operationSystem: OperationSystem) => void;
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
}: ColumnProps): ColumnDef<OperationSystem>[] => [
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
    accessorKey: "operationSystemCode",
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Mã hệ điều hành
          <ArrowUpDown className="h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => <div>{row.getValue("operationSystemCode")}</div>,
  },
  {
    accessorKey: "operationSystemName",
    header: "Tên hệ điều hành",
  },
  {
    accessorKey: "operationSystemStatus",
    header: "Trạng thái",
    cell: ({ row }) => {
      const status = row.getValue("operationSystemStatus") as string;
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
      const operationSystem = row.original;
      const isActive =
        operationSystem.operationSystemStatus.toUpperCase() === "NOT_DELETED";
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
            onClick={() => onToggleStatus(operationSystem)}
          >
            <ArrowLeftRight className="h-4 w-4" />
          </Button>
          <Button
            variant="ghost"
            size="icon"
            onClick={() => onEdit(operationSystem)}
          >
            <Pencil className="h-4 w-4" />
          </Button>
          <Button
            variant="ghost"
            size="icon"
            onClick={() => onDelete(operationSystem)}
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        </div>
      );
    },
  },
];
