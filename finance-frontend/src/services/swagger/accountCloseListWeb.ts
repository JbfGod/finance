// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** closeNextPeriod POST /api/account/closeNextPeriod */
export async function closeNextPeriodUsingPOST(options?: { [key: string]: any }) {
  return request<API.R>('/api/account/closeNextPeriod', {
    method: 'POST',
    ...(options || {}),
  });
}

/** closePreviousPeriod POST /api/account/closePreviousPeriod */
export async function closePreviousPeriodUsingPOST(options?: { [key: string]: any }) {
  return request<API.R>('/api/account/closePreviousPeriod', {
    method: 'POST',
    ...(options || {}),
  });
}
