import {message, notification} from '@umijs/plugin-request/lib/ui';
import {history} from "@/.umi/core/history";
import {ErrorShowType} from "@/.umi/plugin-request/request";

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
  504: '网关超时。',
};
const DEFAULT_ERROR_PAGE = '/exception';
const authHeaderInterceptor = (url, options) => {
  const token = localStorage.getItem("AccessToken")
  const authHeader = token ? {Authorization: token} : {};
  return {
    options: {...options, interceptors: true, headers: authHeader},
  };
};
const errorAdaptor = (resData) => {
  return {
    ...resData,
    errorMessage: resData.message,
  }
}
export const request = {
  requestInterceptors: [authHeaderInterceptor],
  errorConfig: {
    adaptor: (resData) => {
      return {
        ...resData,
        errorMessage: resData.message,
      }
    }
  },
  errorHandler: (error) => {
    const status = error?.response?.status
    if (status === 504) {
      notification.error({
        description: error.response.statusText,
        message: '服务器异常',
      });
      throw error
    }
    if (error?.request?.options?.skipErrorHandler) {
      throw error;
    }
    let errorInfo;
    if (error.name === 'ResponseError' && error.data && error.request) {
      const ctx = {
        req: error.request,
        res: error.response,
      };
      errorInfo = errorAdaptor(error.data, ctx);
      error.message = errorInfo?.errorMessage || error.message;
      error.info = errorInfo;
    }
    errorInfo = error.info;

    if (errorInfo) {
      let errorMessage = errorInfo?.errorMessage;
      if (!errorMessage) {
        errorMessage = error.response.statusText
      }
      const errorCode = errorInfo?.errorCode;
      const errorPage = DEFAULT_ERROR_PAGE;

      switch (errorInfo?.showType) {
        case ErrorShowType.SILENT:
          // do nothing
          break;
        case ErrorShowType.WARN_MESSAGE:
          message.warn(errorMessage);
          break;
        case ErrorShowType.ERROR_MESSAGE:
          message.error(errorMessage);
          break;
        case ErrorShowType.NOTIFICATION:
          notification.open({
            description: errorMessage,
            message: errorCode,
          });
          break;
        case ErrorShowType.REDIRECT:
          // @ts-ignore
          history.push({
            pathname: errorPage,
            query: {errorCode, errorMessage},
          });
          // redirect to error page
          break;
        default:
          message.error(errorMessage);
          break;
      }
    } else {
      message.error(error.message || 'Request error, please retry.');
    }
    throw error;
  },
}
