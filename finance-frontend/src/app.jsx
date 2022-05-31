import {PageLoading, SettingDrawer} from '@ant-design/pro-layout';
import {history} from 'umi';
import RightContent from '@/components/RightContent';
import defaultSettings from '../config/defaultSettings';
import * as userWeb from "@/services/swagger/userWeb";
import * as common from "@/utils/common";

import moment from 'moment';
import 'moment/locale/zh-cn';

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
      const msg = await userWeb.selfInfoUsingGET();
      return msg.data
    } catch (error) {
      history.push(loginPath);
    }
    return undefined;
  }; // 如果不是登录页面，执行

  if (common.getAccessToken() || history.location.pathname !== loginPath) {
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
} // ProLayout 支持的api https://procomponents.ant.design/components/layout

export const layout = ({ initialState, setInitialState }) => {
  return {
    rightContentRender: () => <RightContent />,
    disableContentMargin: false,
    access: {
      strictMode: true,
    },
    footerRender: () => null,//<Footer />,
    onPageChange: () => {
      const { location } = history; // 如果没有登录，重定向到 login
      const currentUser = initialState?.currentUser
      if (location.pathname === loginPath) {
        if (currentUser) {
          history.push("/welcome");
        }
      } else if (!currentUser) {
        history.push(loginPath);
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
        const {data : selfPermissions} = await userWeb.selfPermissionUsingGET()
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
          {!props.location?.pathname?.includes('/login') && (
            <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({ ...preInitialState, settings }));
              }}
            />
          )}
        </>
      );
    },
    ...initialState?.settings,
  };
};
