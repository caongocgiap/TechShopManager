import { useLocation } from "react-router-dom";
import { useMemo } from "react";

interface BreadcrumbItem {
  label: string;
  path: string;
}

export const useBreadcrumb = () => {
  const location = useLocation();

  const breadcrumbItems = useMemo(() => {
    const pathSegments = location.pathname.split("/").filter(Boolean);
    const items: BreadcrumbItem[] = [];

    items.push({
      label: "Tech Shop Management",
      path: "/dashboard",
    });

    let currentPath = "";
    pathSegments.forEach((segment) => {
      currentPath += `/${segment}`;
      const label = segment.charAt(0).toUpperCase() + segment.slice(1);
      items.push({
        label,
        path: currentPath,
      });
    });

    return items;
  }, [location.pathname]);

  return breadcrumbItems;
};
