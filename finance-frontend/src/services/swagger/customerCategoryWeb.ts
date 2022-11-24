// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addCustomerCategory POST /api/customerCategory/add */
export async function addCustomerCategoryUsingPOST(
  body: API.AddCustomerCategoryRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/customerCategory/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteCustomerCategory DELETE /api/customerCategory/delete/${param0} */
export async function deleteCustomerCategoryUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteCustomerCategoryUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/customerCategory/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** listCustomerCategory GET /api/customerCategory/list */
export async function listCustomerCategoryUsingGET(options?: { [key: string]: any }) {
  return request<API.RListCustomerCategoryVO_>('/api/customerCategory/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** treeCustomerCategory GET /api/customerCategory/tree */
export async function treeCustomerCategoryUsingGET(options?: { [key: string]: any }) {
  return request<API.RListTreeCustomerCategoryVO_>('/api/customerCategory/tree', {
    method: 'GET',
    ...(options || {}),
  });
}

/** updateCustomerCategory PUT /api/customerCategory/update */
export async function updateCustomerCategoryUsingPUT(
  body: API.UpdateCustomerCategoryRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/customerCategory/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
