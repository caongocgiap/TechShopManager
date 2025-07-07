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
  CpuPaginationParams,
  createCpu,
  getPaginationCpuApi,
  updateCpu,
  updateCpuStatus,
} from "@/service/api/cpu.api";
import { ModifyCpu } from "@/type/index.i";

export const useGetPaginationCpu = (
  params: CpuPaginationParams,
  options?: UseQueryOptions
) => {
  return useQuery({
    queryKey: [queryKey.cpu, sortObjectKeys(params)],
    queryFn: () => getPaginationCpuApi(params),
    ...options,
  }) as UseQueryResult<Awaited<ReturnType<typeof getPaginationCpuApi>>>;
};

export const useCreateCpu = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: ModifyCpu) => createCpu(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.cpu] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateCpu = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ data, id }: { data: ModifyCpu; id: string }) =>
      updateCpu(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.cpu] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateCpuStatus = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => updateCpuStatus(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.cpu] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};
