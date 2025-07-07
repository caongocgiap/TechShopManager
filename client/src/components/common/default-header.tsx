import { HeaderContext } from "@tanstack/react-table";
import { FaSortAlphaDown, FaSortAlphaUp } from "react-icons/fa";
import {
  ContextMenu,
  ContextMenuCheckboxItem,
  ContextMenuContent,
  ContextMenuTrigger,
} from "@/components/ui/context-menu";

interface DefaultHeaderProps<TData, TValue> {
  info: HeaderContext<TData, TValue>;
  name: string;
}

export function DefaultHeader<TData, TValue>({
  info,
  name,
}: DefaultHeaderProps<TData, TValue>) {
  const { table } = info;
  const sorted = info.column.getIsSorted();

  return (
    <ContextMenu>
      <ContextMenuTrigger
        onPointerDown={(e) => {
          e.preventDefault();
          info.column.toggleSorting(info.column.getIsSorted() === "asc");
        }}
        className="w-full h-full flex flex-row items-center justify-start gap-4 cursor-default"
      >
        {name}
        {sorted === "asc" && <FaSortAlphaDown />}
        {sorted === "desc" && <FaSortAlphaUp />}
      </ContextMenuTrigger>
      <ContextMenuContent
        onCloseAutoFocus={(e) => e.preventDefault()}
        onContextMenu={(e) => e.preventDefault()}
      >
        {table
          .getAllColumns()
          .filter((column) => column.getCanHide())
          .map((column) => (
            <ContextMenuCheckboxItem
              key={column.id}
              className="capitalize"
              checked={column.getIsVisible()}
              onCheckedChange={(value) => column.toggleVisibility(!!value)}
            >
              {column.id}
            </ContextMenuCheckboxItem>
          ))}
      </ContextMenuContent>
    </ContextMenu>
  );
}

export default DefaultHeader;