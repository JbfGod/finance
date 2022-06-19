import React, {useCallback} from 'react';
import {LogoutOutlined, SettingOutlined, UserOutlined} from '@ant-design/icons';
import {Avatar, Menu, Spin} from 'antd';
import {history, useModel} from 'umi';
import HeaderDropdown from '../HeaderDropdown';
import styles from './index.less';
import {loginOut} from '@/services/login';
import {useSecurity} from "@/utils/hooks";


const AvatarDropdown = ({ menu }) => {
  const { initialState, setInitialState } = useModel('@@initialState')
  const security = useSecurity()
  const onMenuClick = useCallback(
    (event) => {
      const { key } = event;

      if (key === 'logout') {
        setInitialState((s) => ({ ...s, currentUser: undefined }));
        loginOut()
        return;
      } else if (key === "switchCustomer") {
        history.push("/user/switchCustomer")
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
    ...(security.isSuperCustomer?[
      {key: "switchCustomer", label:"切换客户单位", icon:false}
    ] : []),
    {key: "logout", label:"退出登录", icon:(<LogoutOutlined />)},
  ]

  const menuHeaderDropdown = (
    <Menu className={styles.menu} selectedKeys={[]} onClick={onMenuClick} items={menuItems}/>
  );
  return (
    <HeaderDropdown overlay={menuHeaderDropdown}>
      <span className={`${styles.action} ${styles.account}`}>
        <Avatar size="small" className={styles.avatar} src={currentUser.avatar} alt="avatar" />
        <span className={`${styles.name} anticon`}>{currentUser.name}</span>
      </span>
    </HeaderDropdown>
  );
};

export default AvatarDropdown;
