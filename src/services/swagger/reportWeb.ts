// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** accountBalance GET /api/report/accountBalance */
export async function accountBalanceUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.accountBalanceUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListAccountBalanceVO_>('/api/report/accountBalance', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** assetsLiabilities GET /api/report/assetsLiabilities */
export async function assetsLiabilitiesUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.assetsLiabilitiesUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListAssetsLiability_>('/api/report/assetsLiabilities', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** cashFlow GET /api/report/cashFlow */
export async function cashFlowUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.cashFlowUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListCashFlow_>('/api/report/cashFlow', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** dailyBank GET /api/report/dailyBank */
export async function dailyBankUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.dailyBankUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListDailyBankVO_>('/api/report/dailyBank', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** dailyCash GET /api/report/dailyCash */
export async function dailyCashUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.dailyCashUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListDailyCashVO_>('/api/report/dailyCash', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** generalLedger GET /api/report/generalLedger */
export async function generalLedgerUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.generalLedgerUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListGeneralLedgerVO_>('/api/report/generalLedger', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** profit GET /api/report/profit */
export async function profitUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.profitUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListProfit_>('/api/report/profit', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** subLedger GET /api/report/subLedger */
export async function subLedgerUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.subLedgerUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/report/subLedger', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
