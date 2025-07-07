const DOMAIN = import.meta.env.VITE_APP_URL_BACKEND_OAUTH2;

const DOMAIN_FRONTEND = import.meta.env.VITE_APP_FRONT_DOMAIN;

export const URL_BACKEND_OAUTH2 = `${DOMAIN}/oauth2/authorize/google?redirect_uri=`;

export const URL_FRONTEND = `${DOMAIN_FRONTEND}/redirect`;

export const PERMISSIONS_STORAGE_KEY = "permissions";

export const USER_STORAGE_KEY = "user";

export const ACCESS_TOKEN_STORAGE_KEY = "access_token";

export const REFRESH_TOKEN_STORAGE_KEY = "refresh_token";

export const USER_TYPE_STORAGE_KEY = "user_type";

export const USER_ID_STORAGE_KEY = "user_id";

export const URL_WEBSOCKET = `${DOMAIN}/ws-message`;

export enum PERMISSIONS {
  ADMIN = "ADMIN",
  MEMBER = "MEMBER",
}

export const ALL_PERMISSIONS = [PERMISSIONS.ADMIN, PERMISSIONS.MEMBER];

export const HTTP_STATUS = {
  OK: "OK",
  CREATED: "CREATED",
  NO_CONTENT: "NO_CONTENT",
  BAD_REQUEST: "BAD_REQUEST",
  UNAUTHORIZED: "UNAUTHORIZED",
  FORBIDDEN: "FORBIDDEN",
  NOT_FOUND: "NOT_FOUND",
  INTERNAL_SERVER_ERROR: "INTERNAL_SERVER_ERROR",
};
