// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** approvalInstance GET /api/approvalInstance/${param0} */
export async function approvalInstanceUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.approvalInstanceUsingGETParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.RApprovalInstanceVO_>(`/api/approvalInstance/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** approved PUT /api/approvalInstance/approved */
export async function approvedUsingPUT(
  body: API.ApprovedRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/approvalInstance/approved', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** reviewRejected PUT /api/approvalInstance/rejected */
export async function reviewRejectedUsingPUT(
  body: API.ReviewRejectedRequest,
  options?: { [key: string]: any },
) {
  return request<API.R>('/api/approvalInstance/rejected', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
