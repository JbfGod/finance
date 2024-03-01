// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** addIndustry POST /api/industry/add */
export async function addIndustryUsingPOST(
  body: API.AddIndustryRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/industry/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteIndustry DELETE /api/industry/delete/${param0} */
export async function deleteIndustryUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteIndustryUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/industry/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** listIndustry GET /api/industry/list */
export async function listIndustryUsingGET(options?: { [key: string]: any }) {
  return request<API.RListIndustryVO_>('/api/industry/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** treeIndustry GET /api/industry/tree */
export async function treeIndustryUsingGET(options?: { [key: string]: any }) {
  return request<API.RListTreeIndustryVO_>('/api/industry/tree', {
    method: 'GET',
    ...(options || {}),
  });
}

/** updateIndustry PUT /api/industry/update */
export async function updateIndustryUsingPUT(
  body: API.UpdateIndustryRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/industry/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
