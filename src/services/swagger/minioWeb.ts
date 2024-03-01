// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** persignedObjectUrl GET /api/minio/getPersignedObjectUrl */
export async function persignedObjectUrlUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.persignedObjectUrlUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RString_>('/api/minio/getPersignedObjectUrl', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
