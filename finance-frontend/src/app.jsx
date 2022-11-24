import {history} from '@umijs/max';
import {message} from 'antd';
import RightContent from '@/components/RightContent';
import defaultSettings from '../config/defaultSettings';
import * as userWeb from "@/services/swagger/userWeb";
import * as common from "@/utils/common";
import {getUserIdentity} from "@/utils/common";

import moment from 'moment';
import 'moment/locale/zh-cn';
import Footer from "@/components/Footer";

moment.locale('zh-cn');

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
    return undefined;
  }
  const currentUser = await fetchUserInfo()
  let initialValue = {
    fetchUserInfo,
    currentUser,
    settings: defaultSettings,
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
        {path: switchCustomer, key: switchCustomer},
      ]
    } else {
      const {data: treeMenus} = await userWeb.selfMenusUsingGET();
      initialValue.treeMenus = treeMenus
    }
    const {data : selfPermissions} = await userWeb.selfPermissionUsingGET()
    initialValue.selfPermissions = selfPermissions
  }
  return initialValue
}
// ProLayout 支持的api https://procomponents.ant.design/components/layout

export const layout = ({ initialState, setInitialState, ...other }) => {
  return {
    title: <span style={{color: "#fff"}}>慧记账平台</span>,
    token: {
      header: {
        colorBgHeader: 'rgb(0, 21, 41)',
        colorHeaderTitle: '#fff',
        colorTextMenu: '#dfdfdf',
        colorTextMenuSecondary: '#dfdfdf',
        colorTextMenuSelected: '#fff',
        colorBgMenuItemSelected: 'rgb(0, 21, 41)',
        heightLayoutHeader: 48
      },
      sider: {
        colorMenuBackground: '#fff',
        colorMenuItemDivider: '#dfdfdf',
        colorTextMenu: '#595959',
        colorTextMenuSelected: 'rgba(42,122,251,1)',
        colorBgMenuItemSelected: 'rgba(230,243,254,1)',
      },
    },
    rightContentRender: () => <RightContent />,
    disableContentMargin: true,
    access: {
      strictMode: true,
    },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history; // 如果没有登录，重定向到 login
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
        userId: initialState?.currentUser?.id,
      },
      request: async (params, defaultMenuData) => {
        if (!params.userId) {
          return []
        }
        const {treeMenus} = initialState
        return treeMenus
      },
    },
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children, props) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
        </>
      );
    },
    ...initialState?.settings,
  };
};
