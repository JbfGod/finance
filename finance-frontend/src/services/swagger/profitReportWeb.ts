// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** listProfitOfMonth GET /api/profitReport/list */
export async function listProfitOfMonthUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listProfitOfMonthUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListProfitOfMonthVO_>('/api/profitReport/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** saveProfitReport POST /api/profitReport/save */
export async function saveProfitReportUsingPOST(
  body: API.SaveProfitReportRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/profitReport/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
