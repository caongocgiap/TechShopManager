import { Button } from "@/components/ui/button";
import { Search } from "lucide-react";

const ComputerSearch = () => {

  return (
    <>
      <Button
        variant={"outline"}
        className="relative w-full justify-start text-sm text-muted-foreground sm:pr-12 md:w-40 lg:w-64"
      >
        <Search className="mr-2 h-4 w-4" />
          Tìm kiếm...
      </Button>
    </>
  );
};

export default ComputerSearch;
