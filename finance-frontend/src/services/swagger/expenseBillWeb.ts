// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** expenseBillById GET /api/expense/bill/${param0} */
export async function expenseBillByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.expenseBillByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.RExpenseBillDetailVO_>(`/api/expense/bill/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** printContentOfExpenseBill GET /api/expense/bill/${param0}/printContent */
export async function printContentOfExpenseBillUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.printContentOfExpenseBillUsingGETParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.RExpenseBillPrintContentVO_>(`/api/expense/bill/${param0}/printContent`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** addExpenseBill POST /api/expense/bill/add */
export async function addExpenseBillUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.addExpenseBillUsingPOSTParams,
  body: string,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/expense/bill/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/octet-stream',
    },
    params: {
      ...params,
    },
    data: body,
    ...(options || {}),
  });
}

/** auditingExpenseBill PUT /api/expense/bill/auditing/${param0} */
export async function auditingExpenseBillUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.auditingExpenseBillUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/expense/bill/auditing/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** deleteExpenseBill DELETE /api/expense/bill/delete/${param0} */
export async function deleteExpenseBillUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteExpenseBillUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/expense/bill/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** getBillNumber GET /api/expense/bill/getBillNumber */
export async function getBillNumberUsingGET(options?: { [key: string]: any }) {
  return request<API.RString_>('/api/expense/bill/getBillNumber', {
    method: 'GET',
    ...(options || {}),
  });
}

/** pageExpenseBill GET /api/expense/bill/page */
export async function pageExpenseBillUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageExpenseBillUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RPageExpenseBillVO_>('/api/expense/bill/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** pageCanApprovedExpenseBill GET /api/expense/bill/page/approval */
export async function pageCanApprovedExpenseBillUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageCanApprovedExpenseBillUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RPageExpenseBillVO_>('/api/expense/bill/page/approval', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** searchExpenseBillCue GET /api/expense/bill/searchBillCue */
export async function searchExpenseBillCueUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.searchExpenseBillCueUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListString_>('/api/expense/bill/searchBillCue', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** searchExpenseItemCue GET /api/expense/bill/searchItemCue */
export async function searchExpenseItemCueUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.searchExpenseItemCueUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListString_>('/api/expense/bill/searchItemCue', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** unAuditingExpenseBill PUT /api/expense/bill/unAuditing/${param0} */
export async function unAuditingExpenseBillUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.unAuditingExpenseBillUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/expense/bill/unAuditing/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** updateExpenseBill PUT /api/expense/bill/update */
export async function updateExpenseBillUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateExpenseBillUsingPUTParams,
  body: string,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/expense/bill/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/octet-stream',
    },
    params: {
      ...params,
    },
    data: body,
    ...(options || {}),
  });
}
