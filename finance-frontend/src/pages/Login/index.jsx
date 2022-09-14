import {LockOutlined, UserOutlined,} from '@ant-design/icons';
import {message} from 'antd';
import React, {useEffect} from 'react';
import {LoginForm, ProFormText} from '@ant-design/pro-form';
import {history, useModel} from 'umi';
import {login} from '@/services/login';
import styles from './index.less';
import MyIcon from "@/components/Icon";
import constants from "@/constants";
import * as common from "@/utils/common";
import {removeCurrCustomer} from "@/utils/common";

const Login = () => {
  const { initialState, setInitialState } = useModel('@@initialState');

  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();

    if (userInfo) {
      await setInitialState((s) => ({ ...s, currentUser: userInfo }));
    }
    return userInfo
  }

  const handleSubmit = async (values) => {
    const resp = await login({ ...values })
    common.setAccessToken(resp.data)
    localStorage.setItem(constants.LAST_LOGIN_CUSTOMER_ACCOUNT, values.customerNumber)
    message.success("登录成功！");
    const currentUser = await fetchUserInfo()
    const {customerNumber, role, proxyCustomer} = currentUser || {}
    const isAdmin = role === "ADMIN"
    const isSuperCustomer = customerNumber === "HX_TOP"
    // 平台单位非管理员选择客户单位
    // const canSwitchCustomer = isSuperCustomer && !isAdmin && proxyCustomer == null
    if (isSuperCustomer) {
      history.push("/user/switchCustomer")
      return
    }
    /** 此方法会跳转到 redirect 参数所在的位置 */
    if (!history) {
      return
    }
    const { query } = history.location
    const { redirect } = query
    history.push(redirect || '/')
  }

  useEffect(() => {
    removeCurrCustomer()
  }, [])
  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <LoginForm
          className={styles.loginForm}
          title={<h2 className={styles.loginTitle}>慧记账平台</h2>}
          initialValues={{
            customerNumber: localStorage.getItem(constants.LAST_LOGIN_CUSTOMER_ACCOUNT),
            autoLogin: true,
          }}
          onFinish={async (values) => {
            await handleSubmit(values);
          }}
        >
          <ProFormText
            name="customerNumber"
            fieldProps={{
              size: 'large',
              prefix: <MyIcon type="icon-danwei1" />,
            }}
            placeholder="请输入单位编号"
            rules={[
              {
                required: true,
                message: "请输入单位编号！"
              },
            ]}
          />
          <ProFormText
            name="account"
            fieldProps={{
              autoComplete:"off",
              size: 'large',
              prefix: <UserOutlined className={styles.prefixIcon} />,
            }}
            placeholder="请输入用户名"
            rules={[
              {
                required: true,
                message: "请输入用户名!",
              },
            ]}
          />
          <ProFormText.Password
            name="password"
            fieldProps={{
              autoComplete:"off",
              size: 'large',
              prefix: <LockOutlined className={styles.prefixIcon} />,
            }}
            placeholder="请输入密码"
            rules={[
              {
                required: true,
                message: "请输入密码！",
              },
            ]}
          />
        </LoginForm>
      </div>
    </div>
  );
};

export default Login;
