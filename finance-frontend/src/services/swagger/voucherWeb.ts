// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** voucherDetail GET /api/voucher/${param0} */
export async function voucherDetailUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.voucherDetailUsingGETParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.RVoucherDetailVO_>(`/api/voucher/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** nextVoucherDetail GET /api/voucher/${param0}/next */
export async function nextVoucherDetailUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.nextVoucherDetailUsingGETParams,
  options?: { [key: string]: any },
) {
  const { serialNumber: param0, ...queryParams } = params;
  return request<API.RVoucherDetailVO_>(`/api/voucher/${param0}/next`, {
    method: 'GET',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** prevVoucherDetail GET /api/voucher/${param0}/prev */
export async function prevVoucherDetailUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.prevVoucherDetailUsingGETParams,
  options?: { [key: string]: any },
) {
  const { serialNumber: param0, ...queryParams } = params;
  return request<API.RVoucherDetailVO_>(`/api/voucher/${param0}/prev`, {
    method: 'GET',
    params: {
      ...queryParams,
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

/** addVoucher POST /api/voucher/add */
export async function addVoucherUsingPOST(
  body: API.AddVoucherRequest,
  options?: { [key: string]: any },
) {
  return request<API.RVoucherDetailVO_>('/api/voucher/add', {
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
  body: API.AuditingVoucherRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/voucher/auditing', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
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

/** voucherItemBySubject GET /api/voucher/bySubject */
export async function voucherItemBySubjectUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.voucherItemBySubjectUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RListVoucherItemVO_>('/api/voucher/bySubject', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** currentPeriodOutlineOfVoucher GET /api/voucher/currentPeriodOutline */
export async function currentPeriodOutlineOfVoucherUsingGET(options?: { [key: string]: any }) {
  return request<API.RCurrentPeriodOutlineOfVoucherVO_>('/api/voucher/currentPeriodOutline', {
    method: 'GET',
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

/** batchDeleteVoucher DELETE /api/voucher/delete */
export async function batchDeleteVoucherUsingDELETE(
  body: API.BatchDeleteVoucherRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/voucher/delete', {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
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
  return request<API.RVoucherDetailVO_>(`/api/voucher/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** firstVoucherDetail GET /api/voucher/first */
export async function firstVoucherDetailUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.firstVoucherDetailUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RVoucherDetailVO_>('/api/voucher/first', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** lastVoucherDetail GET /api/voucher/last */
export async function lastVoucherDetailUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.lastVoucherDetailUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RVoucherDetailVO_>('/api/voucher/last', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** pageVoucher GET /api/voucher/page */
export async function pageVoucherUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.pageVoucherUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.RPageVoucherDetailVO_>('/api/voucher/page', {
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
  body: API.UnAuditingVoucherRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/voucher/unAuditing', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
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
export async function usableSerialNumberUsingGET(options?: { [key: string]: any }) {
  return request<API.RInt_>('/api/voucher/usableSerialNumber', {
    method: 'GET',
    ...(options || {}),
  });
}
