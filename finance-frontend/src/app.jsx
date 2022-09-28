import {PageLoading} from '@ant-design/pro-layout';
import {history} from 'umi';
import {message} from 'antd';
import RightContent from '@/components/RightContent';
import defaultSettings from '../config/defaultSettings';
import * as userWeb from "@/services/swagger/userWeb";
import * as common from "@/utils/common";

import moment from 'moment';
import 'moment/locale/zh-cn';
import Footer from "@/components/Footer";
import {getUserIdentity} from "@/utils/common";

moment.locale('zh-cn');


const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/user/login';

export {request} from "@/extend-config/request"

/** 获取用户信息比较慢的时候会展示一个 loading */
export const initialStateConfig = {
  loading: <PageLoading />,
};
/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState() {
  const fetchUserInfo = async () => {
    try {
      if (common.getAccessToken()) {
        const msg = await userWeb.selfInfoUsingGET();
        return msg.data
      }
    } catch (error) {
      history.push(loginPath)
      common.clearAccessToken()
    }
    return undefined;
  }; // 如果不是登录页面，执行

  if (history.location.pathname !== loginPath) {
    const currentUser = await fetchUserInfo();

    return {
      fetchUserInfo,
      currentUser,
      settings: defaultSettings,
    };
  }

  return {
    fetchUserInfo,
    settings: defaultSettings,
  };
}
// ProLayout 支持的api https://procomponents.ant.design/components/layout

export const layout = ({ initialState, setInitialState }) => {
  return {
    siderWidth: 180,
    rightContentRender: () => <RightContent />,
    disableContentMargin: true,
    access: {
      strictMode: true,
    },
    footerRender: () => <Footer />,//,
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
          history.push("/user/switchIdentity")
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
      if (location.pathname === loginPath) {
        history.push("/welcome");
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
        const userIdentity = getUserIdentity()
        const isApprover = userIdentity === "APPROVER"
        const {data : selfPermissions} = await userWeb.selfPermissionUsingGET()
        if (isApprover) {
          const approvalExpenseBillPage = "/approval/expenseBill"
          const switchUserIdentity = "/user/switchIdentity"
          const switchCustomer = "/user/switchCustomer"
          const treeMenus = [
            {path: approvalExpenseBillPage, key: approvalExpenseBillPage, name: "费用报销单审批"},
            {path: switchUserIdentity, key: switchUserIdentity},
            {path: switchCustomer, key: switchCustomer},
          ]
          setInitialState((preInitialState) => ({ ...preInitialState, selfPermissions, treeMenus }))
          return treeMenus
        }
        const {data: treeMenus} = await userWeb.selfMenusUsingGET();
        setInitialState((preInitialState) => ({ ...preInitialState, selfPermissions, treeMenus }))
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
          {/*{!props.location?.pathname?.includes('/login') && (
            <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({ ...preInitialState, settings }));
              }}
            />
          )}*/}
        </>
      );
    },
    ...initialState?.settings,
  };
};
