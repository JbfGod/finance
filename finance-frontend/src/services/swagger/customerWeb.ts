// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** resourceIdsOfCustomer GET /api/customer/${param0}/resourceIds */
export async function resourceIdsOfCustomerUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.resourceIdsOfCustomerUsingGETParams,
  options?: { [key: string]: any },
) {
  const { customerId: param0, ...queryParams } = params;
  return request<API.RListResourceIdentifiedVO_>(`/api/customer/${param0}/resourceIds`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** treeResourceWithOperate GET /api/customer/${param0}/treeResourceWithOperate */
export async function treeResourceWithOperateUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.treeResourceWithOperateUsingGETParams,
  options?: { [key: string]: any },
) {
  const { customerId: param0, ...queryParams } = params;
  return request<API.RListTreeResourceWithOperateVO_>(
    `/api/customer/${param0}/treeResourceWithOperate`,
    {
      method: 'GET',
      params: { ...queryParams },
      ...(options || {}),
    },
  );
}

/** addCustomer POST /api/customer/add */
export async function addCustomerUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.addCustomerUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/customer/add', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** deleteCustomer DELETE /api/customer/delete/${param0} */
export async function deleteCustomerUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteCustomerUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/customer/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** grantResourceToCustomer POST /api/customer/grantResources */
export async function grantResourceToCustomerUsingPOST(
  body: API.GrantResourcesToCustomerRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/customer/grantResources', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** ownedApprovalCustomers GET /api/customer/owned/approval */
export async function ownedApprovalCustomersUsingGET(options?: { [key: string]: any }) {
  return request<API.RListOwnedApprovalCustomerVO_>('/api/customer/owned/approval', {
    method: 'GET',
    ...(options || {}),
  });
}

/** pageCustomer GET /api/customer/page */
export async function pageCustomerUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageCustomerUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RPageCustomerListVO_>('/api/customer/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** searchCustomerCue GET /api/customer/searchCustomerCue */
export async function searchCustomerCueUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.searchCustomerCueUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListCustomerCueVO_>('/api/customer/searchCustomerCue', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** updateCustomer PUT /api/customer/update */
export async function updateCustomerUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateCustomerUsingPUTParams,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/customer/update', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
