import { PERMISSIONS } from "@/constants/index.constant";
import { setAuthorization } from "@/context/redux/slice/AuthSlice";
import { getPermissionsUser, getUserInformation } from "@/utils/token.helper";
import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate, useSearchParams } from "react-router-dom";

const StaffRedirectHandler = () => {
  const [searchParams] = useSearchParams();

  const dispatch = useDispatch();

  const navigate = useNavigate();

  useEffect(() => {
    const state = searchParams.get("state");
    const token = state ? atob(state) : null;
    const authenticToken = token ? JSON.parse(token) : null;
    const error = searchParams.get("error");

    if (authenticToken) {
      const { accessToken, refreshToken } = authenticToken;
      const userInfo = getUserInformation(accessToken);
      const permissions = getPermissionsUser(accessToken);

      if (userInfo && permissions) {
        dispatch(
          setAuthorization({
            token: accessToken,
            userInfo,
            permissions,
            refreshToken,
          })
        );
        if (permissions.includes(PERMISSIONS.ADMIN)) navigate("/dashboard");
        else navigate("/member");
        return;
      }
    }

    if (error) {
      navigate("/");
      return;
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return null;
};

export default StaffRedirectHandler;
