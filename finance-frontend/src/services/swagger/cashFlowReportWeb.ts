// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** listCashFlowOfMonth GET /api/cashFlowReport/list */
export async function listCashFlowOfMonthUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listCashFlowOfMonthUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListCashFlowOfMonthVO_>('/api/cashFlowReport/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** saveCashFlowReport POST /api/cashFlowReport/save */
export async function saveCashFlowReportUsingPOST(
  body: API.SaveCashFlowReportRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/cashFlowReport/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
