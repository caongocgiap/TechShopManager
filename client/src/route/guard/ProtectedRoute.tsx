import { JSX, useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { removeAuthorization } from "@/context/redux/slice/AuthSlice";
import permissionVerify from "@/utils/permissionVerify";

const ProtectedRoute = ({
  children,
  requiredPermissions,
}: {
  children: JSX.Element;
  requiredPermissions: string[];
}) => {
  const navigate = useNavigate();

  const dispatch = useDispatch();

  useEffect(() => {
    if (!permissionVerify(requiredPermissions)) {
      dispatch(removeAuthorization());
      navigate("/not-authorization");
    }
  }, [requiredPermissions, navigate, dispatch]);

  return permissionVerify(requiredPermissions) ? children : null;
};

export default ProtectedRoute;
