import {
  ScreenResolutionPaginationParams,
  ScreenResolution,
  createScreenResolution,
  getPaginationScreenResolutionApi,
  updateScreenResolution,
  updateScreenResolutionStatus,
} from "@/service/api/screen-resolution.api";
import { queryKey } from "@/type/queryClientKey";
import { sortObjectKeys } from "@/utils/common.helper";
import {
  UseQueryOptions,
  UseQueryResult,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";

export const useGetPaginationScreenResolution = (
  params: ScreenResolutionPaginationParams,
  options?: UseQueryOptions
) => {
  return useQuery({
    queryKey: [queryKey.screenResolution, sortObjectKeys(params)],
    queryFn: () => getPaginationScreenResolutionApi(params),
    ...options,
  }) as UseQueryResult<
    Awaited<ReturnType<typeof getPaginationScreenResolutionApi>>
  >;
};

export const useCreateScreenResolution = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: ScreenResolution) => createScreenResolution(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.screenResolution] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateScreenResolution = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ data, id }: { data: ScreenResolution; id: string }) =>
      updateScreenResolution(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.screenResolution] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};

export const useUpdateScreenResolutionStatus = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => updateScreenResolutionStatus(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [queryKey.screenResolution] });
    },
    onError: (error) => {
      console.log(error);
    },
  });
};
