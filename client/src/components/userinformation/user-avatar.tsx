import {
  Sheet,
  SheetTrigger,
  SheetContent,
  SheetHeader,
  SheetTitle,
  // SheetDescription,
  SheetClose,
  SheetFooter,
} from "@/components/ui/sheet";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { logout } from "@/service/api/auth.api";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { removeAuthorization } from "@/context/redux/slice/AuthSlice";
// import { ConfirmDialog } from "../common/confirm-dialog";
import { toast } from "sonner";
import { useRef, useState } from "react";
import { RootState } from "@/context/redux/store";

export const UserAvatar = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const sheetRef = useRef<HTMLButtonElement>(null);
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);

  const { userInfo, permissions } = useSelector(
    (state: RootState) => state.auth.authorization
  );

  const initials = userInfo?.fullName
    ? userInfo.fullName
        .split(" ")
        .map((word) => word[0])
        .join("")
        .toUpperCase()
    : "?";

  const handleLogout = async () => {
    try {
      const response = await logout({
        userId: userInfo?.userId,
        userType: userInfo?.userType,
      });

      if (response?.status === 200) {
        dispatch(removeAuthorization());
        sheetRef.current?.click();
        navigate("/");
        toast.success("Đăng xuất thành công!");
      } else {
        toast.error("Đăng xuất thất bại!");
      }
    } catch (e) {
      console.error(e);
      toast.error("Đã xảy ra lỗi trong quá trình đăng xuất!");
    }
  };

  return (
    <Sheet>
      <SheetTrigger className="outline-none" ref={sheetRef}>
        <Avatar>
          {userInfo?.pictureUrl && (
            <AvatarImage src={userInfo.pictureUrl} alt={userInfo.fullName} />
          )}
          <AvatarFallback>{initials}</AvatarFallback>
        </Avatar>
      </SheetTrigger>

      <SheetContent side="right" className="w-[320px] sm:w-[400px]">
        <SheetHeader>
          <SheetTitle>Thông tin tài khoản</SheetTitle>
          {/* <SheetDescription>Chi tiết người dùng</SheetDescription> */}
        </SheetHeader>

        <div className="mt-6 space-y-4 px-5">
          {userInfo ? (
            <>
              <div className="flex items-center gap-4">
                <Avatar className="w-16 h-16">
                  {userInfo.pictureUrl && (
                    <AvatarImage
                      src={userInfo.pictureUrl}
                      alt={userInfo.fullName}
                    />
                  )}
                  <AvatarFallback className="text-xl">
                    {initials}
                  </AvatarFallback>
                </Avatar>
                <div>
                  <p className="text-lg font-semibold">{userInfo.fullName}</p>
                  <p className="text-sm text-gray-500">{userInfo.email}</p>
                  {Array.isArray(permissions) ? (
                    permissions.map((permission, index) => (
                      <span
                        key={index}
                        className="px-2 py-1 text-xs bg-muted text-muted-foreground rounded-full"
                      >
                        {permission}
                      </span>
                    ))
                  ) : (
                    <span className="px-2 py-1 text-xs bg-muted text-muted-foreground rounded-full">
                      {permissions}
                    </span>
                  )}
                </div>
              </div>

              <div className="space-y-2">
                <Button
                  variant="outline"
                  className="w-full"
                  onClick={() => navigate("/dashboard")}
                >
                  Chuyển sang màn hình quản lý
                </Button>
                <Button 
                  variant="destructive" 
                  className="w-full"
                  onClick={() => setShowConfirmDialog(true)}
                >
                  Đăng xuất
                </Button>
                {/* <ConfirmDialog
                  isOpen={showConfirmDialog}
                  onClose={() => setShowConfirmDialog(false)}
                  title="Đăng xuất tài khoản?"
                  description="Bạn có chắc chắn muốn đăng xuất không?"
                  onConfirm={handleLogout}
                /> */}
              </div>
            </>
          ) : (
            <div className="text-center">
              <p className="text-sm text-muted-foreground mb-4">Bạn chưa đăng nhập</p>
              <Button
                className="w-full"
                onClick={() => (window.location.href = "/login")}
              >
                Đăng nhập
              </Button>
            </div>
          )}
        </div>
        <SheetFooter>
          <SheetClose asChild>
            <Button type="submit">Save changes</Button>
          </SheetClose>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
};
