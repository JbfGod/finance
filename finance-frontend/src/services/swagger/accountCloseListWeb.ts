// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** cancelClosedAccount POST /api/account/cancel */
export async function cancelClosedAccountUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.cancelClosedAccountUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/account/cancel', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** closeAccount POST /api/account/close */
export async function closeAccountUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.closeAccountUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/account/close', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
