import { API_REQUEST_LOGOUT } from "@/service/base/api.constant";
import { instanceNoAuth } from "@/service/base/request";

export const logout = async ({
  userId,
  userType,
}: {
  userId: string | undefined;
  userType: string | undefined;
}) => {
  return await instanceNoAuth.post(API_REQUEST_LOGOUT, {
    userId,
    userType,
  });
};
