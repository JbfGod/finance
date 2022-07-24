// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** addInitialBalanceItem POST /api/initialBalance/add */
export async function addInitialBalanceItemUsingPOST(
  body: API.AddInitialBalanceRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/initialBalance/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** auditingInitialBalance PUT /api/initialBalance/auditing */
export async function auditingInitialBalanceUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.auditingInitialBalanceUsingPUTParams,
  options?: { [key: string]: any },
) {
  return request<API.RInitialBalanceVO_>('/api/initialBalance/auditing', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** bookkeepingInitialBalance PUT /api/initialBalance/bookkeeping/${param0} */
export async function bookkeepingInitialBalanceUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.bookkeepingInitialBalanceUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/initialBalance/bookkeeping/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** deleteInitialBalance DELETE /api/initialBalance/delete/${param0} */
export async function deleteInitialBalanceUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteInitialBalanceUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/initialBalance/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** initialBalanceOutline GET /api/initialBalance/outline */
export async function initialBalanceOutlineUsingGET(options?: { [key: string]: any }) {
  return request<API.RInitialBalanceVO_>('/api/initialBalance/outline', {
    method: 'GET',
    ...(options || {}),
  });
}

/** pageInitialBalanceItem GET /api/initialBalance/page */
export async function pageInitialBalanceItemUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageInitialBalanceItemUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RPageInitialBalanceItemVO_>('/api/initialBalance/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** unAuditingInitialBalance PUT /api/initialBalance/unAuditing/${param0} */
export async function unAuditingInitialBalanceUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.unAuditingInitialBalanceUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/initialBalance/unAuditing/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** unBookkeepingInitialBalance PUT /api/initialBalance/unBookkeeping/${param0} */
export async function unBookkeepingInitialBalanceUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.unBookkeepingInitialBalanceUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/initialBalance/unBookkeeping/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** updateInitialBalance PUT /api/initialBalance/update */
export async function updateInitialBalanceUsingPUT(
  body: API.UpdateInitialBalanceRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/initialBalance/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
