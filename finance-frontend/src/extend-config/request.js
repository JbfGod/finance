import {message, notification} from "antd";
import * as common from "@/utils/common";

const ErrorShowType = {
  SILENT : 0,
  WARN_MESSAGE : 1,
  ERROR_MESSAGE : 2,
  NOTIFICATION : 4,
  REDIRECT : 9,
}
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
        loadingKey = [options.url, options.method, Date.now()]
        message.loading({content: "操作中...", key: loadingKey})
      }
  }
  return {
    url,
    options: {...options, interceptors: true, headers: headers, loadingKey},
  };
};
const responseInterceptor = (response) => {
  if (response.status === 504) {
    notification.error({
      description: "服务器请求超时",
      message: "网络异常",
    });
    return response
  }
  const {data, config} = response;
  if (data?.success && !IGNORE_URL.includes(config.url)) {
    switch (config?.method.toLowerCase()) {
      case "post":
      case "delete":
      case "put":
        message.success({content: data.message || "操作成功", key: config.loadingKey})
    }
  }
  return response
}
export const request = {
  requestInterceptors: [authHeaderInterceptor],
  responseInterceptors: [responseInterceptor],
  errorConfig: {
    adaptor: () => ({}),
    errorThrower(e){
      console.log("errorThrower", e)
    },
    errorHandler: (error) => {
      const {response, config} = error
      if (!response || !response.data) {
        console.error("errorHandler", error)
        throw error
      }
      const loadingKey = config?.loadingKey
      const {data} = response
      let errorMessage = data?.message || "未知的服务异常！"
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
          if (data.errorCode === "401") {
            common.logoutStorageHandler()
            pathname = "/login"
          }
          message.error({content: errorMessage, key: loadingKey})
          pathname && (window.location.href = pathname)
          break
        default:
          message.error({content: errorMessage, key: loadingKey})
          break
      }
      throw error
    }
  },
}
