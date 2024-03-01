// @ts-ignore
/* eslint-disable */
import {request} from '@umijs/max';

/** addCurrency POST /api/currency/add */
export async function addCurrencyUsingPOST(
  body: API.AddCurrencyRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/currency/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** auditingCurrency PUT /api/currency/auditing/${param0} */
export async function auditingCurrencyUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.auditingCurrencyUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/currency/auditing/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** copyCurrencyByMonth POST /api/currency/copy */
export async function copyCurrencyByMonthUsingPOST(
  body: API.CopyCurrencyRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/currency/copy', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteCurrency DELETE /api/currency/delete/${param0} */
export async function deleteCurrencyUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteCurrencyUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/currency/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** currencyById GET /api/currency/get/${param0} */
export async function currencyByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.currencyByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.RCurrencyVO_>(`/api/currency/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** currencyOfYearMonth GET /api/currency/listAuditedOfYearMonth */
export async function currencyOfYearMonthUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.currencyOfYearMonthUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListCurrencyVO_>('/api/currency/listAuditedOfYearMonth', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listGroupByCurrencyName GET /api/currency/listGroupByCurrency */
export async function listGroupByCurrencyNameUsingGET(options?: { [key: string]: any }) {
  return request<API.RListString_>('/api/currency/listGroupByCurrency', {
    method: 'GET',
    ...(options || {}),
  });
}

/** pageCurrency GET /api/currency/page */
export async function pageCurrencyUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageCurrencyUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RPageCurrencyVO_>('/api/currency/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** unAuditingCurrency PUT /api/currency/unAuditing/${param0} */
export async function unAuditingCurrencyUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.unAuditingCurrencyUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/currency/unAuditing/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** updateCurrency PUT /api/currency/update */
export async function updateCurrencyUsingPUT(
  body: API.UpdateCurrencyRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/currency/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
