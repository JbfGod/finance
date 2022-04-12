import {Space} from 'antd';
import React, {useState} from 'react';
import {useModel, history} from 'umi';
import Avatar from './AvatarDropdown';
import HeaderSearch from '../HeaderSearch';
import styles from './index.less';
import {useFlatTree} from "@/utils/hooks";

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
  const searchOptions = useFlatTree(treeMenus, "routes")
    .filter(menu => !searchKey || menu.name.includes(searchKey))
    .map(menu => ({
      label: <div onClick={() => history.push(menu.path)}><a>{menu.name}</a></div>,
      value: menu.name
    }))
  return (
    <Space className={className}>
      <HeaderSearch
        className={`${styles.action} ${styles.search}`}
        placeholder="站内搜索"
        options={searchOptions} onSearch={value => {
        setSearchKey(value)
      }}
      />
      <Avatar/>
    </Space>
  );
};

export default GlobalHeaderRight;
