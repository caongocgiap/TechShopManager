import { localStorageAction } from "./storage";
import { decrypt } from "./storageProtection";

export default function permissionVerify(
  requiredPermissions?: string[]
): boolean {
  const userPermissions = localStorageAction.get("permissions")
    ? decrypt(localStorageAction.get("permissions"))
    : [];
  if (!Array.isArray(requiredPermissions)) {
    return false;
  }
  if (requiredPermissions.includes("*")) {
    return true;
  }
  return requiredPermissions.some((permission) => {
    return Array.isArray(userPermissions)
      ? userPermissions.includes(permission)
      : permission === userPermissions;
  });
}
