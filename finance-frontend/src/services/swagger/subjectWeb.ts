// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

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

/** treeSubject GET /api/subject/tree */
export async function treeSubjectUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.treeSubjectUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListTreeSubjectVO_>('/api/subject/tree', {
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
