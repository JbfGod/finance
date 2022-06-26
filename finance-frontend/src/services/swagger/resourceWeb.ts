// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** treeNormalCustomerResources GET /api/resource/treeOfNormalCustomer */
export async function treeNormalCustomerResourcesUsingGET(options?: { [key: string]: any }) {
  return request<API.RListTreeResourceVO_>('/api/resource/treeOfNormalCustomer', {
    method: 'GET',
    ...(options || {}),
  });
}
