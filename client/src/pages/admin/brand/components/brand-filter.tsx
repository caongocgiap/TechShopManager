/* eslint-disable @typescript-eslint/no-explicit-any */
import { Dispatch, SetStateAction, useState, useEffect } from "react";
import { Input } from "@/components/ui/input";
import { Search, X } from "lucide-react";

const BrandFilter = ({
  searchValue,
}: {
  searchValue: Dispatch<SetStateAction<any>>;
}) => {
  const [name, setName] = useState<string>("");

  // Tự động search khi gõ
  useEffect(() => {
    searchValue((prev: any) => ({
      ...prev,
      brandName: name.trim() || undefined,
      page: 1,
    }));
  }, [name, searchValue]);

  const handleClearFilter = () => {
    setName("");
  };

  return (
    <div className="flex gap-2 items-center">
      <div className="relative">
        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
        <Input
          placeholder="Tìm kiếm thương hiệu..."
          value={name}
          onChange={(e) => setName(e.target.value)}
          className="w-64 pl-10 pr-10"
        />
        {name && (
          <button
            type="button"
            className="absolute right-1 top-1/2 transform -translate-y-1/2 h-6 w-6 p-0 hover:bg-gray-100 rounded flex items-center justify-center"
            onClick={handleClearFilter}
          >
            <X className="h-4 w-4" />
          </button>
        )}
      </div>
    </div>
  );
};

export default BrandFilter;
