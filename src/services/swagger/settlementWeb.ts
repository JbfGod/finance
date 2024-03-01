// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** closingToNextPeriod PUT /api/settlement/closingToNextPeriod */
export async function closingToNextPeriodUsingPUT(options?: { [key: string]: any }) {
  return request<API.R>('/api/settlement/closingToNextPeriod', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** unClosingToPrevPeriod PUT /api/settlement/unClosingToPrevPeriod */
export async function unClosingToPrevPeriodUsingPUT(options?: { [key: string]: any }) {
  return request<API.R>('/api/settlement/unClosingToPrevPeriod', {
    method: 'PUT',
    ...(options || {}),
  });
}
