// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** addSubject POST /api/subject/add */
export async function addSubjectUsingPOST(
  body: API.AddSubjectRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/subject/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteSubject DELETE /api/subject/delete/${param0} */
export async function deleteSubjectUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteSubjectUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/subject/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** listSubject GET /api/subject/list */
export async function listSubjectUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listSubjectUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListSubjectVO_>('/api/subject/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** pageSubject GET /api/subject/page */
export async function pageSubjectUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageSubjectUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RPageSubjectVO_>('/api/subject/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** updateSubject PUT /api/subject/update */
export async function updateSubjectUsingPUT(
  body: API.UpdateSubjectRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/subject/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateSubjectInitialBalance PUT /api/subject/updateSubjectInitialBalance */
export async function updateSubjectInitialBalanceUsingPUT(
  body: API.UpdateSubjectInitialBalanceRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/subject/updateSubjectInitialBalance', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
