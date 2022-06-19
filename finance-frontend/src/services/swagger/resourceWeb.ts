// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** treeResources GET /api/resource/tree */
export async function treeResourcesUsingGET(options?: { [key: string]: any }) {
  return request<API.RListTreeResourceVO_>('/api/resource/tree', {
    method: 'GET',
    ...(options || {}),
  });
}
