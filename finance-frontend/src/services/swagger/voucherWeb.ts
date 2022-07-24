// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** addVoucher POST /api/voucher/add */
export async function addVoucherUsingPOST(
  body: API.AddVoucherRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/voucher/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** batchAuditingVoucher PUT /api/voucher/auditing */
export async function batchAuditingVoucherUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.batchAuditingVoucherUsingPUTParams,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/voucher/auditing', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** auditingVoucher PUT /api/voucher/auditing/${param0} */
export async function auditingVoucherUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.auditingVoucherUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/voucher/auditing/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** batchBookkeepingVoucher PUT /api/voucher/bookkeeping */
export async function batchBookkeepingVoucherUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.batchBookkeepingVoucherUsingPUTParams,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/voucher/bookkeeping', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** bookkeepingVoucher PUT /api/voucher/bookkeeping/${param0} */
export async function bookkeepingVoucherUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.bookkeepingVoucherUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/voucher/bookkeeping/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** defaultVoucherDate GET /api/voucher/defaultVoucherDate */
export async function defaultVoucherDateUsingGET(options?: { [key: string]: any }) {
  return request<API.RLocalDate_>('/api/voucher/defaultVoucherDate', {
    method: 'GET',
    ...(options || {}),
  });
}

/** deleteVoucher DELETE /api/voucher/delete/${param0} */
export async function deleteVoucherUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteVoucherUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/voucher/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** voucherDetail GET /api/voucher/get/${param0} */
export async function voucherDetailUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.voucherDetailUsingGETParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.RVoucherDetailVO_>(`/api/voucher/get/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** pageVoucher GET /api/voucher/page */
export async function pageVoucherUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageVoucherUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RPageVoucherVO_>('/api/voucher/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** pageVoucherBook GET /api/voucher/page/book */
export async function pageVoucherBookUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageVoucherBookUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RPageVoucherBookVO_>('/api/voucher/page/book', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** searchVoucherCue GET /api/voucher/searchItemCue */
export async function searchVoucherCueUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.searchVoucherCueUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListString_>('/api/voucher/searchItemCue', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** batchUnAuditingVoucher PUT /api/voucher/unAuditing */
export async function batchUnAuditingVoucherUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.batchUnAuditingVoucherUsingPUTParams,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/voucher/unAuditing', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** unAuditingVoucher PUT /api/voucher/unAuditing/${param0} */
export async function unAuditingVoucherUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.unAuditingVoucherUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/voucher/unAuditing/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** batchUnBookkeepingVoucher PUT /api/voucher/unBookkeeping */
export async function batchUnBookkeepingVoucherUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.batchUnBookkeepingVoucherUsingPUTParams,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/voucher/unBookkeeping', {
    method: 'PUT',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** unBookkeepingVoucher PUT /api/voucher/unBookkeeping/${param0} */
export async function unBookkeepingVoucherUsingPUT(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.unBookkeepingVoucherUsingPUTParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.R>(`/api/voucher/unBookkeeping/${param0}`, {
    method: 'PUT',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** updateVoucher PUT /api/voucher/update */
export async function updateVoucherUsingPUT(
  body: API.UpdateVoucherRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/voucher/update', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** usableSerialNumber GET /api/voucher/usableSerialNumber */
export async function usableSerialNumberUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.usableSerialNumberUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RInt_>('/api/voucher/usableSerialNumber', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** printContentOfVoucher GET /api/voucher/${param0}/printContent */
export async function printContentOfVoucherUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.printContentOfVoucherUsingGETParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.RVoucherPrintContentVO_>(`/api/voucher/${param0}/printContent`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}
