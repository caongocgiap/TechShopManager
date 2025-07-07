import { queryKey } from "@/type/queryClientKey";
import { sortObjectKeys } from "@/utils/common.helper";
import {
  UseQueryOptions,
  UseQueryResult,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import {
  Manufacturer,
  ManufacturerPaginationParams,
  createManufacturer,
  getAllManufacturerApi,
  getPaginationManufacturerApi,
  updateManufacturer,
  updateManufacturerStatus,
} from "@/service/api/manufacturer.api";

export const useGetPaginationManufacturer = (
  params: ManufacturerPaginationParams,
  options?: UseQueryOptions
) => {
  return useQuery({
    queryKey: [queryKey.manufacturer, sortObjectKeys(params)],
    queryFn: () => getPaginationManufacturerApi(params),
    ...options,
  }) as UseQueryResult<Awaited<ReturnType<typeof getPaginationManufacturerApi>>>;
};

export const useCreateManufacturer = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: Manufacturer) => createManufacturer(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.manufacturer] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateManufacturer = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ data, id }: { data: Manufacturer; id: string }) =>
      updateManufacturer(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.manufacturer] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateManufacturerStatus = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => updateManufacturerStatus(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.manufacturer] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useGetAllManufacturer = (
  options?: UseQueryOptions
) => {
  return useQuery({
    queryKey: [queryKey.manufacturer],
    queryFn: () => getAllManufacturerApi(),
    ...options,
  }) as UseQueryResult<Awaited<ReturnType<typeof getAllManufacturerApi>>>;
};
