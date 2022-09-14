// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** deleteFlowItem DELETE /api/approvalFlow/flowItem/${param0} */
export async function deleteFlowItemUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteFlowItemUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/approvalFlow/flowItem/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** flowItems GET /api/approvalFlow/flowItems */
export async function flowItemsUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.flowItemsUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListApprovalFlowItemVO_>('/api/approvalFlow/flowItems', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** saveFlowItem POST /api/approvalFlow/saveFlowItem */
export async function saveFlowItemUsingPOST(
  body: API.SaveApprovalFlowRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/approvalFlow/saveFlowItem', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
