// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** addUser POST /api/user/add */
export async function addUserUsingPOST(body: API.AddUserRequest, options?: { [key: string]: any }) {
  return request<API.R>('/api/user/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteUser DELETE /api/user/delete/${param0} */
export async function deleteUserUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUserUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/user/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** grantResourcesToUser POST /api/user/grantResources */
export async function grantResourcesToUserUsingPOST(
  body: API.GrantResourcesToUserRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/user/grantResources', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listUser GET /api/user/list */
export async function listUserUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listUserUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListUserListVO_>('/api/user/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** ownedCustomer GET /api/user/ownedCustomer */
export async function ownedCustomerUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.ownedCustomerUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListCustomerCueVO_>('/api/user/ownedCustomer', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** pageUser GET /api/user/page */
export async function pageUserUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageUserUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RPageUserListVO_>('/api/user/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** resetUserPassword PUT /api/user/resetPassword */
export async function resetUserPasswordUsingPUT(
  body: API.UpdateUserPasswordRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/user/resetPassword', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** searchUserCue GET /api/user/searchUserCue */
export async function searchUserCueUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.searchUserCueUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListUserCueVO_>('/api/user/searchUserCue', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** selfInfo GET /api/user/self */
export async function selfInfoUsingGET(options?: { [key: string]: any }) {
  return request<API.RUserSelfVO_>('/api/user/self', {
    method: 'GET',
    ...(options || {}),
  });
}

/** selfMenus GET /api/user/self/menus */
export async function selfMenusUsingGET(options?: { [key: string]: any }) {
  return request<API.RListUserOwnedMenuVO_>('/api/user/self/menus', {
    method: 'GET',
    ...(options || {}),
  });
}

/** selfPermission GET /api/user/self/permissions */
export async function selfPermissionUsingGET(options?: { [key: string]: any }) {
  return request<API.RListString_>('/api/user/self/permissions', {
    method: 'GET',
    ...(options || {}),
  });
}

/** selfUpdatePassword PUT /api/user/self/updatePassword */
export async function selfUpdatePasswordUsingPUT(
  body: API.UpdateSelfPasswordRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/user/self/updatePassword', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** switchProxyCustomer PUT /api/user/switch/proxyCustomer/${param0} */
export async function switchProxyCustomerUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.switchProxyCustomerUsingPUTParams & {
    // header
    /** Authorization */
    Authorization: string;
  },
  options?: { [key: string]: any },
) {
  const { customerId: param0, ...queryParams } = params;
  return request<API.R>(`/api/user/switch/proxyCustomer/${param0}`, {
    method: 'PUT',
    headers: {},
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** updateUser PUT /api/user/update */
export async function updateUserUsingPUT(
  body: API.UpdateUserRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/user/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** resourceIdsOfUser GET /api/user/${param0}/resources */
export async function resourceIdsOfUserUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.resourceIdsOfUserUsingGETParams,
  options?: { [key: string]: any },
) {
  const { userId: param0, ...queryParams } = params;
  return request<API.RListString_>(`/api/user/${param0}/resources`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}
