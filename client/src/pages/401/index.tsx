import { removeAuthorization } from "@/context/redux/slice/AuthSlice";
import { Button } from "@/components/ui/button";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, Lock } from "lucide-react";

export default function NotAuthenticated() {
  const dispatch = useDispatch();

  const navigate = useNavigate();

  const reLogin = () => {
    dispatch(removeAuthorization());
    navigate("/login");
  };

  return (
    <div className="min-h-[calc(100vh-4rem)] flex items-center justify-center">
      <div className="text-center space-y-6 px-4">
        <div className="space-y-2">
          <div className="flex justify-center">
            <Lock className="w-24 h-24 text-destructive" />
          </div>
          <h1 className="text-9xl font-bold text-destructive">401</h1>
          <h2 className="text-2xl font-semibold text-foreground">
            Yêu cầu xác thực
          </h2>
          <p className="text-muted-foreground">
            Bạn cần đăng nhập để truy cập trang này. Vui lòng đăng nhập để tiếp
            tục.
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
          <Button onClick={reLogin} className="gap-2">
            Đăng nhập
          </Button>
        </div>
      </div>
    </div>
  );
}
