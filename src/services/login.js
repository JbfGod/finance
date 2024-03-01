import {history, request} from "@umijs/max";
import * as common from "@/utils/common";
import {removeCurrCustomer} from "@/utils/common";

export async function login(body) {
  return request('/api/login', {
    method: 'POST',
    params: body
  });
}

export async function logout() {
  return request('/api/logout', {
    method: 'POST'
  }).then(_ => common.logoutStorageHandler());
}


export const loginOut = async () => {
  await logout();
  history.replace('/login')
  removeCurrCustomer()
};
