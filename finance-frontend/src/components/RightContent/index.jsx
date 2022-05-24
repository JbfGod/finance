import {Space} from 'antd';
import React, {useState} from 'react';
import {history, useModel} from 'umi';
import Avatar from './AvatarDropdown';
import styles from './index.less';
import * as common from "@/utils/common";
import SwitchCustomer from "@/components/RightContent/SwitchCustomer";

const GlobalHeaderRight = () => {
  const {initialState} = useModel('@@initialState');
  const [searchKey, setSearchKey] = useState()

  if (!initialState || !initialState.settings) {
    return null;
  }

  const {navTheme, layout} = initialState.settings;
  let className = styles.right;

  if ((navTheme === 'dark' && layout === 'top') || layout === 'mix') {
    className = `${styles.right}  ${styles.dark}`;
  }
  const {treeMenus} = initialState
  const searchOptions = common.flatTree(treeMenus, "routes")
    .filter(menu => !searchKey || menu.name.includes(searchKey))
    .map(menu => ({
      label: <div onClick={() => history.push(menu.path)}><a>{menu.name}</a></div>,
      value: menu.name
    }))
  return (
    <Space className={className}>
      {initialState?.currentUser?.customerId === 0 && <SwitchCustomer />}
      {/*<HeaderSearch
        className={`${styles.action} ${styles.search}`}
        placeholder="站内搜索"
        options={searchOptions} onSearch={value => {
        setSearchKey(value)
      }}
      />*/}
      <Avatar/>
    </Space>
  );
};

export default GlobalHeaderRight;
