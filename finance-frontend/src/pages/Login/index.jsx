import {LockOutlined, UserOutlined,} from '@ant-design/icons';
import {message} from 'antd';
import React, {useEffect} from 'react';
import {LoginForm, ProFormText} from '@ant-design/pro-form';
import {history} from 'umi';
import {login} from '@/services/login';
import styles from './index.less';
import MyIcon from "@/components/Icon";
import constants from "@/constants";
import * as common from "@/utils/common";
import {removeCurrCustomer} from "@/utils/common";

const Login = () => {
  const handleSubmit = async (values) => {
    const resp = await login({ ...values })
    common.setAccessToken(resp.data)
    localStorage.setItem(constants.LAST_LOGIN_CUSTOMER_ACCOUNT, values.customerNumber || "")
    message.success("登录成功！");
    /** 此方法会跳转到 redirect 参数所在的位置 */
    if (!history) {
      return
    }
    const { query } = history.location
    const { redirect } = query
    window.location.href = redirect || "/user/switchCustomer"
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
            /*rules={[
              {
                required: true,
                message: "请输入单位编号！"
              },
            ]}*/
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
