import HashLoading from "@/components/ui/HashLoading";
import { PERMISSIONS } from "@/constants/index.constant";
import LayoutMember from "@/layouts/LayoutMember";
import LayoutAdmin from "@/layouts/LayoutAdmin";
import NotAuthenticated from "@/pages/401";
import NotAuthorized from "@/pages/403";
import NotFound from "@/pages/404";
import Error from "@/pages/error";
import Landing from "@/pages/login";
import React, { Suspense } from "react";
import { useRoutes } from "react-router-dom";
import ProtectedRoute from "./guard/ProtectedRoute";
import RedirectHandler from "./guard/RedirectHandler";

const HomePage = React.lazy(() => import("@/pages/home"));
const MemberPage = React.lazy(() => import("@/pages/user"));
const AdminPage = React.lazy(() => import("@/pages/admin"));
const BrandPage = React.lazy(() => import("@/pages/admin/brand"));
const CategoryPage = React.lazy(() => import("@/pages/admin/category"));
const CpuPage = React.lazy(() => import("@/pages/admin/cpu"));
const GpuPage = React.lazy(() => import("@/pages/admin/gpu"));
const HardDrivePage = React.lazy(() => import("@/pages/admin/hard-drive"));
const ManufacturerPage = React.lazy(() => import("@/pages/admin/manufacturer"));
const OperationSystemPage = React.lazy(() => import("@/pages/admin/operation-system"));
const RamPage = React.lazy(() => import("@/pages/admin/ram"));
const ScreenResolutionPage = React.lazy(() => import("@/pages/admin/screen-resolution"));

type RouteType = {
  path: string;
  element: React.ReactNode;
  children?: RouteType[];
};

const withLayoutMember = (Component: React.ReactNode) => {
  return (
    <LayoutMember>
      <Suspense fallback={<HashLoading />}>{Component}</Suspense>
    </LayoutMember>
  );
};

const withLayoutAdmin = (Component: React.ReactNode) => {
  return (
    <LayoutAdmin>
      <Suspense fallback={<HashLoading />}>{Component}</Suspense>
    </LayoutAdmin>
  );
};

const RouteList = () => {
  const routes: RouteType[] = [
    { path: "*", element: <NotFound /> },
    { path: "/not-authentication", element: <NotAuthenticated /> },
    { path: "/not-authorization", element: <NotAuthorized /> },
    { path: "/forbidden", element: <NotAuthorized /> },
    { path: "/error", element: <Error /> },
    { path: "/redirect", element: <RedirectHandler /> },
    { path: "/login", element: <Landing /> },
    { path: "/", element: withLayoutMember(<HomePage />)},
    {
      path: "/dashboard",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.ADMIN]}>
          {withLayoutAdmin(<AdminPage />)}
        </ProtectedRoute>
      ),
    },
    {
      path: "/brand",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.ADMIN]}>
          {withLayoutAdmin(<BrandPage />)}
        </ProtectedRoute>
      ),
    },
    {
      path: "/category",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.ADMIN]}>
          {withLayoutAdmin(<CategoryPage />)}
        </ProtectedRoute>
      ),
    },
    {
      path: "/cpu",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.ADMIN]}>
          {withLayoutAdmin(<CpuPage />)}
        </ProtectedRoute>
      ),
    },
    {
      path: "/gpu",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.ADMIN]}>
          {withLayoutAdmin(<GpuPage />)}
        </ProtectedRoute>
      ),
    },
    {
      path: "/hard-drive",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.ADMIN]}>
          {withLayoutAdmin(<HardDrivePage />)}
        </ProtectedRoute>
      ),
    },
    {
      path: "/manufacturer",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.ADMIN]}>
          {withLayoutAdmin(<ManufacturerPage />)}
        </ProtectedRoute>
      ),
    },
    {
      path: "/operation-system",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.ADMIN]}>
          {withLayoutAdmin(<OperationSystemPage />)}
        </ProtectedRoute>
      ),
    },
    {
      path: "/ram",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.ADMIN]}>
          {withLayoutAdmin(<RamPage />)}
        </ProtectedRoute>
      ),
    },
    {
      path: "/screen-resolution",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.ADMIN]}>
          {withLayoutAdmin(<ScreenResolutionPage />)}
        </ProtectedRoute>
      ),
    },
    {
      path: "/member",
      element: (
        <ProtectedRoute requiredPermissions={[PERMISSIONS.MEMBER]}>
          {withLayoutMember(<MemberPage />)}
        </ProtectedRoute>
      ),
    },
  ];

  return useRoutes(routes);
};

export default RouteList;
