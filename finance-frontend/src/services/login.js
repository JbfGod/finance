import {request} from "umi";

export async function login(body) {
  return request('/api/login', {
    method: 'POST',
    params: body
  });
}

export async function logout() {
  return request('/api/logout', {
    method: 'POST'
  });
}
