import {
  CategoryPaginationParams,
  Category,
  createCategory,
  getPaginationCategoryApi,
  updateCategory,
  updateCategoryStatus,
} from "@/service/api/category.api";
import { queryKey } from "@/type/queryClientKey";
import { sortObjectKeys } from "@/utils/common.helper";
import {
  UseQueryOptions,
  UseQueryResult,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";

export const useGetPaginationCategory = (
  params: CategoryPaginationParams,
  options?: UseQueryOptions
) => {
  return useQuery({
    queryKey: [queryKey.category, sortObjectKeys(params)],
    queryFn: () => getPaginationCategoryApi(params),
    ...options,
  }) as UseQueryResult<Awaited<ReturnType<typeof getPaginationCategoryApi>>>;
};

export const useCreateCategory = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: Category) => createCategory(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.category] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateCategory = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ data, id }: { data: Category; id: string }) =>
      updateCategory(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.category] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateCategoryStatus = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => updateCategoryStatus(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.category] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};
