"use client";

import {
  ArrowRightLeft,
  BadgeCheck,
  Bell,
  ChevronsUpDown,
  CreditCard,
  LogOut,
} from "lucide-react";
import { useConfirmDialog } from "@/hooks/useConfirmDialog";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  useSidebar,
} from "@/components/ui/sidebar";
import { RootState } from "@/context/redux/store";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { removeAuthorization } from "@/context/redux/slice/AuthSlice";
import { toast } from "sonner";
import { logout } from "@/service/api/auth.api";
import { useState } from "react";

export function NavUser() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { isMobile } = useSidebar();
  const { userInfo } = useSelector(
    (state: RootState) => state.auth.authorization
  );
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const { confirm, ConfirmDialogComponent } = useConfirmDialog();

  const handleLogout = async () => {
    const confirmed = await confirm({
      title: "Xác nhận đăng xuất",
      description: "Bạn có chắc chắn muốn đăng xuất?",
    });

    if (confirmed) {
      try {
        const response = await logout({
          userId: userInfo?.userId,
          userType: userInfo?.userType,
        });

        if (response?.status === 200) {
          dispatch(removeAuthorization());
          navigate("/");
          toast.success("Đăng xuất thành công!");
        } else {
          toast.error("Đăng xuất thất bại!");
        }
      } catch (e) {
        console.error(e);
        toast.error("Đã xảy ra lỗi trong quá trình đăng xuất!");
      }
    }
  };

  return (
    <SidebarMenu>
      <SidebarMenuItem>
        <DropdownMenu open={isDropdownOpen} onOpenChange={setIsDropdownOpen}>
          <DropdownMenuTrigger asChild>
            <SidebarMenuButton
              size="lg"
              className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
            >
              <Avatar className="h-8 w-8 rounded-lg">
                <AvatarImage
                  src={userInfo?.pictureUrl}
                  alt={userInfo?.fullName}
                />
                <AvatarFallback className="rounded-lg">CN</AvatarFallback>
              </Avatar>
              <div className="grid flex-1 text-left text-sm leading-tight">
                <span className="truncate font-semibold">
                  {userInfo?.fullName}
                </span>
                <span className="truncate text-xs">{userInfo?.email}</span>
              </div>
              <ChevronsUpDown className="ml-auto size-4" />
            </SidebarMenuButton>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            className="w-[--radix-dropdown-menu-trigger-width] min-w-56 rounded-lg"
            side={isMobile ? "bottom" : "right"}
            align="end"
            sideOffset={4}
          >
            <DropdownMenuLabel className="p-0 font-normal">
              <div className="flex items-center gap-2 px-1 py-1.5 text-left text-sm">
                <Avatar className="h-8 w-8 rounded-lg">
                  <AvatarImage
                    src={userInfo?.pictureUrl}
                    alt={userInfo?.fullName}
                  />
                  <AvatarFallback className="rounded-lg">CN</AvatarFallback>
                </Avatar>
                <div className="grid flex-1 text-left text-sm leading-tight">
                  <span className="truncate font-semibold">
                    {userInfo?.fullName}
                  </span>
                  <span className="truncate text-xs">{userInfo?.email}</span>
                </div>
              </div>
            </DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuGroup>
              <DropdownMenuItem onClick={() => navigate("/")}>
                <ArrowRightLeft />
                Chuyển sang màn hình khách hàng
              </DropdownMenuItem>
            </DropdownMenuGroup>
            <DropdownMenuSeparator />
            <DropdownMenuGroup>
              <DropdownMenuItem>
                <BadgeCheck />
                Account
              </DropdownMenuItem>
              <DropdownMenuItem>
                <CreditCard />
                Billing
              </DropdownMenuItem>
              <DropdownMenuItem>
                <Bell />
                Notifications
              </DropdownMenuItem>
            </DropdownMenuGroup>
            <DropdownMenuSeparator />
            <DropdownMenuItem 
              onClick={() => {
                setIsDropdownOpen(false);
                handleLogout();
              }}
            >
              <LogOut />
              Log out
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </SidebarMenuItem>
      <ConfirmDialogComponent />
    </SidebarMenu>
  );
}
