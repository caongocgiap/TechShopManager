import { jwtDecode } from "jwt-decode";

export interface DecodedToken {
  fullName: string;
  userId: string;
  userCode: string;
  rolesName: string;
  host: string;
  rolesCode: string[];
  exp: number;
  email: string;
  pictureUrl: string;
  userType: string;
}

export interface UserInformation {
  fullName: string;
  userId: string;
  userCode: string;
  rolesName: string;
  hostId: string;
  email: string;
  pictureUrl: string;
  userType: string;
}

export const getUserInformation = (token: string): UserInformation => {
  const decoded = jwtDecode<DecodedToken>(token);
  return {
    fullName: decoded.fullName,
    userId: decoded.userId,
    userCode: decoded.userCode,
    rolesName: decoded.rolesName,
    hostId: decoded.host,
    email: decoded.email,
    pictureUrl: decoded.pictureUrl,
    userType: decoded.userType,
  };
};

export const getPermissionsUser = (token: string): string[] => {
  const decoded = jwtDecode<DecodedToken>(token);
  return decoded.rolesCode;
};

export const getExpireTime = (token: string): number => {
  const decoded = jwtDecode<DecodedToken>(token);
  return decoded.exp;
};

export const isTokenExpired = (token: string): boolean => {
  const expireTime = getExpireTime(token);
  return Date.now() >= expireTime * 1000;
};
