import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, ShieldAlert } from "lucide-react";

export default function NotAuthorized() {
  const navigate = useNavigate();

  return (
    <div className="min-h-[calc(100vh-4rem)] flex items-center justify-center">
      <div className="text-center space-y-6 px-4">
        <div className="space-y-2">
          <div className="flex justify-center">
            <ShieldAlert className="w-24 h-24 text-destructive" />
          </div>
          <h1 className="text-9xl font-bold text-destructive">403</h1>
          <h2 className="text-2xl font-semibold text-foreground">
            Truy cập bị từ chối
          </h2>
          <p className="text-muted-foreground">
            Bạn không có quyền truy cập vào trang này. Vui lòng liên hệ với quản
            trị viên nếu bạn nghĩ đây là lỗi.
          </p>
        </div>
        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          <Button
            variant="outline"
            onClick={() => navigate(-1)}
            className="gap-2"
          >
            <ArrowLeft className="w-4 h-4" />
            Quay lại trang trước
          </Button>
          <Button onClick={() => navigate("/")} className="gap-2">
            Về trang chủ
          </Button>
        </div>
      </div>
    </div>
  );
}
