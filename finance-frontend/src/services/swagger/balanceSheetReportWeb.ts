// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** listBalanceSheetOfMonth GET /api/balanceSheetReport/list */
export async function listBalanceSheetOfMonthUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listBalanceSheetOfMonthUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListBalanceSheetOfMonthVO_>('/api/balanceSheetReport/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** saveBalanceSheetReport POST /api/balanceSheetReport/save */
export async function saveBalanceSheetReportUsingPOST(
  body: API.SaveBalanceSheetReportRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/balanceSheetReport/save', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
