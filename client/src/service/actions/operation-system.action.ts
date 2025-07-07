import {
  OperationSystemPaginationParams,
  OperationSystem,
  createOperationSystem,
  getPaginationOperationSystemApi,
  updateOperationSystem,
  updateOperationSystemStatus,
} from "@/service/api/operation-system.api";
import { queryKey } from "@/type/queryClientKey";
import { sortObjectKeys } from "@/utils/common.helper";
import {
  UseQueryOptions,
  UseQueryResult,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";

export const useGetPaginationOperationSystem = (
  params: OperationSystemPaginationParams,
  options?: UseQueryOptions
) => {
  return useQuery({
    queryKey: [queryKey.operationSystem, sortObjectKeys(params)],
    queryFn: () => getPaginationOperationSystemApi(params),
    ...options,
  }) as UseQueryResult<
    Awaited<ReturnType<typeof getPaginationOperationSystemApi>>
  >;
};

export const useCreateOperationSystem = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: OperationSystem) => createOperationSystem(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.operationSystem] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateOperationSystem = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ data, id }: { data: OperationSystem; id: string }) =>
      updateOperationSystem(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.operationSystem] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateOperationSystemStatus = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => updateOperationSystemStatus(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.operationSystem] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};
