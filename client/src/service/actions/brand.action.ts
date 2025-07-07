import {
  BrandPaginationParams,
  createBrand,
  getPaginationBrandApi,
  updateBrand,
  updateBrandStatus,
} from "@/service/api/brand.api";
import { ModifyBrand } from "@/type/index.t";
import { queryKey } from "@/type/queryClientKey";
import { sortObjectKeys } from "@/utils/common.helper";
import {
  UseQueryOptions,
  UseQueryResult,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";

export const useGetAllBrand = (
  params: BrandPaginationParams,
  options?: UseQueryOptions
) => {
  return useQuery({
    queryKey: [queryKey.brand, sortObjectKeys(params)],
    queryFn: () => getPaginationBrandApi(params),
    // refetchOnMount: true,
    // refetchOnWindowFocus: false,
    ...options,
  }) as UseQueryResult<Awaited<ReturnType<typeof getPaginationBrandApi>>>;
};

export const useCreateBrand = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: ModifyBrand) => createBrand(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.brand] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateBrand = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ data, id }: { data: ModifyBrand; id: string }) =>
      updateBrand(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.brand] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateBrandStatus = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => updateBrandStatus(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.brand] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};
