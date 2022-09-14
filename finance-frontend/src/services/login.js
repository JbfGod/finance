import {history, request} from "umi";
import * as common from "@/utils/common";

export async function login(body) {
  return request('/api/login', {
    method: 'POST',
    params: body
  });
}

export async function logout() {
  return request('/api/logout', {
    method: 'POST'
  }).then(_ => common.clearAccessToken());
}


export const loginOut = async () => {
  await logout();
  history.replace('/user/login')
};
