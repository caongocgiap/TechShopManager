import { Input } from "@/components/ui/input";

interface SearchInputProps {
  placeholder?: string;
  value: string;
  onChange: (value: string) => void;
  className?: string;
}

export function SearchInput({
  placeholder = "Tìm kiếm...",
  value,
  onChange,
  className,
}: SearchInputProps) {
  return (
    <div className="flex items-center py-4">
      <Input
        placeholder={placeholder}
        value={value}
        onChange={(event) => onChange(event.target.value)}
        className={className}
      />
    </div>
  );
} 