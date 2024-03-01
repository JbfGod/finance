import React, {useCallback} from 'react';
import {ExclamationCircleFilled, LogoutOutlined} from '@ant-design/icons';
import {Avatar, Modal, Spin} from 'antd';
import {history, useModel} from 'umi';
import HeaderDropdown from '../HeaderDropdown';
import styles from './index.less';
import {loginOut} from '@/services/login';
import {useSecurity} from "@/utils/hooks";

const { confirm } = Modal;

const AvatarDropdown = ({ menu }) => {
  const { initialState, setInitialState } = useModel('@@initialState')
  const security = useSecurity()
  const {isApprover, hasMultipleIdentities} = security
  const onMenuClick = useCallback(
    (event) => {
      const { key } = event;

      if (key === 'logout') {
        if (security.isFinanceMode) {
          confirm({
            title: '系统提示',
            icon: <ExclamationCircleFilled />,
            content: '你确认要退出吗？',
            onOk() {
              window.close()
            }
          })
        } else {
          setInitialState((s) => ({ ...s, currentUser: null }));
          loginOut()
        }
        return
      } else if (key === "switchUserIdentity") {
        history.push("/switchIdentity")
        return
      }

      history.push(`/account/${key}`);
    },
    [setInitialState],
  );
  const loading = (
    <span className={`${styles.action} ${styles.account}`}>
      <Spin
        size="small"
        style={{
          marginLeft: 8,
          marginRight: 8,
        }}
      />
    </span>
  );

  if (!initialState) {
    return loading;
  }

  const { currentUser } = initialState;
  if (!currentUser || !currentUser.name) {
    return loading;
  }
  const menuItems = [
    /*{key: "center", label:"个人中心", icon:(<UserOutlined />)},
    {key: "settings", label:"个人设置", icon:(<SettingOutlined />)},
    {type: 'divider'},*/
    ...(security.hasMultipleIdentities? [
      {key: "switchUserIdentity", label:"切换用户身份", icon:false}
    ] : []),
    {key: "logout", label:"退出登录", icon:(<LogoutOutlined />)},
  ]

  const menuHeaderDropdown = {
    items: menuItems,
    onClick: onMenuClick
  }
  return (
    <HeaderDropdown menu={menuHeaderDropdown}>
      <span>
        <Avatar size="small" src={currentUser.avatar} alt="avatar" />
        <span>{currentUser.name}</span>
      </span>
    </HeaderDropdown>
  );
};

export default AvatarDropdown;
