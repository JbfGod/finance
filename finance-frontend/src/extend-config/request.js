import {message, notification} from "antd";
import {history} from "@/.umi/core/history";
import {ErrorShowType} from "@/.umi/plugin-request/request";
import constants from "@/constants";
import * as common from "@/utils/common";

const codeMessage = {
  200: '服务器成功返回请求的数据。',
  201: '新建或修改数据成功。',
  202: '一个请求已经进入后台排队（异步任务）。',
  204: '删除数据成功。',
  400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
  401: '用户没有权限（令牌、用户名、密码错误）。',
  403: '用户得到授权，但是访问是被禁止的。',
  404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
  406: '请求的格式不可得。',
  410: '请求的资源被永久删除，且不会再得到的。',
  422: '当创建一个对象时，发生一个验证错误。',
  500: '服务器发生错误，请检查服务器。',
  502: '网关错误。',
  503: '服务不可用，服务器暂时过载或维护。',
  504: '网关超时。'
}
const DEFAULT_ERROR_PAGE = '/exception'
const IGNORE_URL = ["/api/login"]
const authHeaderInterceptor = (url, options) => {
  const token = common.getAccessToken()
  const authHeader = token ? {Authorization: token} : {};
  let loadingKey;
  switch (options?.method.toLowerCase()) {
    case "post":
    case "delete":
    case "put":
      if (!IGNORE_URL.includes(url)) {
        loadingKey = [options.url, options.method, new Date().getTime()]
        message.loading({content: "操作中...", key: loadingKey})
      }
  }
  return {
    options: {...options, interceptors: true, headers: authHeader, loadingKey},
  };
};
const responseInterceptor = async (response, options) => {
  if (response.status === 504) {
    notification.error({
      description: "服务器请求超时",
      message: "网络异常",
    });
    return response
  }
  const data = await response.clone().json();
  if (data.success && !IGNORE_URL.includes(options.url)) {
    switch (options?.method.toLowerCase()) {
      case "post":
      case "delete":
      case "put":
        if (data.showType === ErrorShowType.NOTIFICATION) {
          message.success({content: data.message || "操作成功", key: options.loadingKey})
        }
    }
    return response
  }
  // const errorCode = data?.errorCode
  let errorMessage = data?.message
  switch (data?.showType) {
    case ErrorShowType.SILENT:
      // do nothing
      break;
    case ErrorShowType.WARN_MESSAGE:
      message.warn({content: errorMessage, key: options.loadingKey})
      break
    case ErrorShowType.ERROR_MESSAGE:
    case ErrorShowType.NOTIFICATION:
      message.error({content: errorMessage, key: options.loadingKey})
      break;
    case ErrorShowType.REDIRECT:
      message.error({content: errorMessage, key: options.loadingKey})
      history.push({
        pathname: DEFAULT_ERROR_PAGE,
        query: {errorCode: data.errorCode, errorMessage: errorMessage}
      })
      break
    default:
      message.error(errorMessage)
      break
  }
  return response
}
export const request = {
  requestInterceptors: [authHeaderInterceptor],
  responseInterceptors: [responseInterceptor],
  errorConfig: {
    adaptor: () => ({})
  },
  errorHandler: (error) => {
    console.error("error", error)
    throw error
  }
}
