/* eslint-disable @typescript-eslint/no-explicit-any */
import {
  ACCESS_TOKEN_STORAGE_KEY,
  PERMISSIONS_STORAGE_KEY,
  REFRESH_TOKEN_STORAGE_KEY,
  USER_STORAGE_KEY,
} from "@/constants/index.constant";
import { localStorageAction } from "@/utils/storage";
import { decrypt } from "@/utils/storageProtection";
import {
  getPermissionsUser,
  getUserInformation,
  isTokenExpired,
} from "@/utils/token.helper";
import axios from "axios";
import { toast } from "sonner";

const request = axios.create({
  baseURL: `${import.meta.env.VITE_APP_BACKEND_DOMAIN}/api/`,
});

const instanceNoAuth = axios.create({
  baseURL: `${import.meta.env.VITE_APP_BACKEND_DOMAIN}/api/`,
});

const refreshInstance = axios.create({
  baseURL: `${import.meta.env.VITE_APP_BACKEND_DOMAIN}/api/`,
});

request.interceptors.request.use(
  async function (config) {
    const token = localStorageAction.get(ACCESS_TOKEN_STORAGE_KEY);

    if (!token) window.location.href = "/not-authentication";

    if (isTokenExpired(token)) {
      try {
        const response = await refreshInstance.post("/auth/refresh-token", {
          refreshToken: decrypt(
            localStorageAction.get(REFRESH_TOKEN_STORAGE_KEY)
          ),
          currentHost: decrypt(localStorageAction.get(USER_STORAGE_KEY))
            ?.hostId,
        });
        if (response.status === 200 && response.data.status === "OK") {
          const { accessToken, refreshToken } = response.data.data;
          const userInfo = getUserInformation(accessToken);
          const permissions = getPermissionsUser(accessToken);
          localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, accessToken);
          localStorageAction.set(REFRESH_TOKEN_STORAGE_KEY, refreshToken);
          localStorageAction.set(USER_STORAGE_KEY, userInfo);
          localStorageAction.set(PERMISSIONS_STORAGE_KEY, permissions);
          config.headers.Authorization = `Bearer ${accessToken}`;
        }
      } catch (error: any) {
        if (error.response && error.response.status === 406) {
          localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY);
          localStorageAction.remove(USER_STORAGE_KEY);
          localStorageAction.remove(PERMISSIONS_STORAGE_KEY);
          localStorageAction.remove(REFRESH_TOKEN_STORAGE_KEY);
          window.location.href = "/not-authorization";
        }

        if (error.response && error.response.status === 500) {
          toast.error('Vui lòng liên hệ nhà phát triển');
        }
      }
    }
    if (token) {
      config.headers.Authorization = `Bearer ${localStorageAction.get(
        ACCESS_TOKEN_STORAGE_KEY
      )}`;
    } else {
      window.location.href = "/not-authorization";
    }

    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY);
      localStorageAction.remove(USER_STORAGE_KEY);
      localStorageAction.remove(PERMISSIONS_STORAGE_KEY);
      localStorageAction.remove(REFRESH_TOKEN_STORAGE_KEY);
      window.location.href = "/not-authorization";
    } else if (error.response && error.response.status === 403) {
      window.location.href = "/forbidden";
    } else if (error.response && error.response.status === 406) {
      localStorageAction.remove(ACCESS_TOKEN_STORAGE_KEY);
      localStorageAction.remove(USER_STORAGE_KEY);
      localStorageAction.remove(PERMISSIONS_STORAGE_KEY);
      localStorageAction.remove(REFRESH_TOKEN_STORAGE_KEY);
      window.location.href = "/not-aceptable/status=" + error.response.data;
    } else if (error.response && error.response.status === 500) {
      toast.error("Vui lòng liên hệ nhà phát triển");
    }
    throw error;
  }
);

export const logout = async ({
  userId,
  userType,
}: {
  userId: string | undefined;
  userType: string | undefined;
}) => {
  return await instanceNoAuth.post("/auth/log-out", {
    userId,
    userType,
  });
};

export default request;

export { instanceNoAuth };
