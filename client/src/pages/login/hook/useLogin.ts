import { PERMISSIONS, URL_BACKEND_OAUTH2, URL_FRONTEND } from "@/constants/index.constant";
import { RootState } from "@/context/redux/store";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export const useLogin = () => {
  const [urlRedirect, setUrlRedirect] = useState<string>("");

  const [isLoginProcessing, setIsLoginProcessing] = useState<boolean>(false);

  const authorization = useSelector(
    (state: RootState) => state?.auth?.authorization
  );

  const navigate = useNavigate();

  const handleLogin = () => {
    if (urlRedirect === "") {
      toast("Đang kết nối, vui lòng thử lại sau!")
      return;
    }
    setIsLoginProcessing(true);
    window.location.href = URL_BACKEND_OAUTH2 + urlRedirect;
  };

  useEffect(() => {
    if (
      authorization?.userInfo,
      authorization?.permissions
    ) {
      console.log(authorization?.permissions);
      
      if (authorization?.permissions.includes(PERMISSIONS.ADMIN)) navigate("/dashboard");
      else if (authorization?.permissions.includes(PERMISSIONS.MEMBER)) navigate("/member")
    }

    setUrlRedirect(URL_FRONTEND);
  }, [navigate, authorization?.userInfo, authorization?.permissions]);

  return {
    urlRedirect,
    setUrlRedirect,
    handleLogin,
    isLoginProcessing,
  };
};
