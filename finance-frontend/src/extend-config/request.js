import {message, notification} from "antd";
import {ErrorShowType} from "@/.umi/plugin-request/request";
import * as common from "@/utils/common";

const DEFAULT_ERROR_PAGE = '/exception'
const IGNORE_URL = ["/api/login"]
const authHeaderInterceptor = (url, options) => {
  const token = common.getAccessToken()
  const {id: CustomerId, number: CustomerNumber} = common.getCurrCustomer() || {}
  const headers = {
    ...(token ? {Authorization: token} : {}),
    ...(CustomerId ? {CustomerId, CustomerNumber} : {})
  }
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
    options: {...options, interceptors: true, headers: headers, loadingKey},
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
  if (data?.success && !IGNORE_URL.includes(options.url)) {
    switch (options?.method.toLowerCase()) {
      case "post":
      case "delete":
      case "put":
        message.success({content: data.message || "操作成功", key: options.loadingKey})
    }
    return response
  }
  const errorCode = data?.errorCode
  let errorMessage = data?.message || "未知的服务异常！"
  const loadingKey = options?.loadingKey
  switch (data?.showType) {
    case ErrorShowType.SILENT:
      // do nothing
      break;
    case ErrorShowType.WARN_MESSAGE:
      message.warn({content: errorMessage, key: loadingKey})
      break
    case ErrorShowType.ERROR_MESSAGE:
    case ErrorShowType.NOTIFICATION:
      message.error({content: errorMessage, key: loadingKey})
      break;
    case ErrorShowType.REDIRECT:
      let pathname = DEFAULT_ERROR_PAGE;
      if (errorCode === "401") {
        common.clearAccessToken()
        pathname = "/user/login"
      }
      message.error({content: errorMessage, key: loadingKey})
      pathname && (window.location.href = pathname)
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
