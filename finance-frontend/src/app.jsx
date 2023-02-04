import {history} from '@umijs/max';
import {message} from 'antd';
import RightContent from '@/components/RightContent';
import defaultSettings from '../config/defaultSettings';
import * as userWeb from "@/services/swagger/userWeb";
import * as common from "@/utils/common";
import {getUserIdentity, setAccessToken} from "@/utils/common";

import moment from 'moment';
import 'moment/locale/zh-cn';
import Footer from "@/components/Footer";
import HeaderTitle from "@/components/HeaderTitle";
import {PageLoading} from "@ant-design/pro-components";
import weekday from "dayjs/plugin/weekday"
import localeData from "dayjs/plugin/localeData"
import dayjs from "dayjs";

moment.locale('zh-cn');

dayjs.extend(weekday)
dayjs.extend(localeData)

const loginPath = '/login';

export {request} from "@/extend-config/request"

/** 获取用户信息比较慢的时候会展示一个 loading */

/*export const initialStateConfig = {
  loading: <PageLoading />,
};*/
/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState() {
  const {search} = window.location
  if (search) {
    const token = search.substring(1).split("&")
      .filter(ele => ele.includes("token="))?.[0]?.split("=")?.[1]
    if (token) {
      setAccessToken(atob(decodeURIComponent(token)))
      const {origin, pathname} = window.location
      window.location.href = origin + pathname
    }
  }
  const fetchUserInfo = async () => {
    try {
      if (common.getAccessToken()) {
        const msg = await userWeb.selfInfoUsingGET()
        return msg.data
      }
    } catch (error) {
      history.push(loginPath)
      common.logoutStorageHandler()
    }
    return undefined
  }
  const currentUser = await fetchUserInfo()
  let initialValue = {
    fetchUserInfo,
    currentUser,
    settings: defaultSettings
  }
  if (currentUser) {
    const userIdentity = getUserIdentity()
    const isApprover = userIdentity === "APPROVER"
    if (isApprover) {
      const approvalExpenseBillPage = "/approval/expenseBill"
      const switchUserIdentity = "/switchIdentity"
      const switchCustomer = "/user/switchCustomer"
      initialValue.treeMenus = [
        {path: approvalExpenseBillPage, key: approvalExpenseBillPage, name: "费用报销单审批"},
        {path: switchUserIdentity, key: switchUserIdentity},
        {path: switchCustomer, key: switchCustomer}
      ]
    } else {
      const {data: treeMenus} = await userWeb.selfMenusUsingGET()
      initialValue.treeMenus = treeMenus
    }
    const {data: selfPermissions} = await userWeb.selfPermissionUsingGET()
    initialValue.selfPermissions = selfPermissions
  }
  return initialValue
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout

export const layout = ({initialState, setInitialState, ...other}) => {
  return {
    title: '慧记账平台',
    headerTitleRender: (logo, title, _) => <HeaderTitle logo={logo} title={title} />,
    headerContentRender: null,
    rightContentRender: () => <RightContent/>,
    disableContentMargin: true,
    access: {
      strictMode: true,
    },
    footerRender: () => <Footer/>,
    onPageChange: () => {
      const {location} = history; // 如果没有登录，重定向到 login
      const currentUser = initialState?.currentUser
      if (!currentUser) {
        // 如果没有认证就跳转到登录页面
        history.push(loginPath);
        return
      }
      const {customer, proxyCustomer, role} = currentUser
      const isSuperCustomer = customer.number === "HX_TOP"
      // 如果用户存在双重含义的身份，跳转身份选择页面
      const isAdvanceApprover = ["ADVANCED_APPROVER", "ADMIN"].includes(role)
      const userIdentity = getUserIdentity()
      if (isAdvanceApprover) {
        if (!userIdentity) {
          history.push("/switchIdentity")
          return
        } else if (userIdentity === "APPROVER") {
          return
        }
      }

      if (isSuperCustomer && proxyCustomer == null) {
        if (location.pathname.startsWith("/expense")
          || location.pathname.startsWith("/voucher")
          || location.pathname.startsWith("/accountClose")
          || location.pathname.startsWith("/base/initialBalance")
          || location.pathname.startsWith("/approval")
          || location.pathname.startsWith("/report")
        ) {
          message.warn("请先选择客户单位！")
          history.push("/user/switchCustomer")
          return
        }
      }
    },
    menu: {
      params: {
        userId: initialState?.currentUser?.id
      },
      request: async (params, defaultMenuData) => {
        if (!params.userId) {
          return []
        }
        const {treeMenus} = initialState
        return treeMenus
      },
    },
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children, props) => {
      if (initialState?.loading) {
        return (
          <PageLoading />
        )
      }
      return (
        <>
          {children}
        </>
      );
    },
    ...initialState?.settings,
  };
};
