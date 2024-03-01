import {Space} from 'antd';
import React from 'react';
import {useModel} from 'umi';
import Avatar from './AvatarDropdown';
import styles from './index.less';

const GlobalHeaderRight = () => {
  const {initialState} = useModel('@@initialState');

  if (!initialState || !initialState.settings) {
    return null;
  }
  const {navTheme, layout} = initialState.settings;
  let className = styles.right;

  if ((navTheme === 'dark' && layout === 'top') || layout === 'mix') {
    className = `${styles.right}`;
  }
  return (
    <Space className={className}>
      <Avatar/>
    </Space>
  );
};

export default GlobalHeaderRight;