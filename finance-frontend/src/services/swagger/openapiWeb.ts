// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** getDocumentation GET /api/downloadOpenapi */
export async function getDocumentationUsingGET(options?: { [key: string]: any }) {
  return request<API.Json>('/api/downloadOpenapi', {
    method: 'GET',
    ...(options || {}),
  });
}
