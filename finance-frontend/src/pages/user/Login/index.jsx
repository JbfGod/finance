import {LockOutlined, UserOutlined,} from '@ant-design/icons';
import {message} from 'antd';
import React from 'react';
import {LoginForm, ProFormText} from '@ant-design/pro-form';
import {history, useModel} from 'umi';
import {login} from '@/services/login';
import styles from './index.less';
import MyIcon from "@/components/Icon";

const Login = () => {
  const { initialState, setInitialState } = useModel('@@initialState');

  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();

    if (userInfo) {
      await setInitialState((s) => ({ ...s, currentUser: userInfo }));
    }
  };

  const handleSubmit = async (values) => {
    const resp = await login({ ...values })
    localStorage.setItem("AccessToken", resp.data)
    message.success("登录成功！");
    await fetchUserInfo();
    /** 此方法会跳转到 redirect 参数所在的位置 */
    if (!history) return;
    const { query } = history.location;
    const { redirect } = query;
    history.push(redirect || '/');
  };
  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <LoginForm
          logo={<MyIcon type="icon-shouzhangben"/>}
          title="慧记账平台"
          initialValues={{
            autoLogin: true,
          }}
          onFinish={async (values) => {
            await handleSubmit(values);
          }}
        >
          <ProFormText
            name="customerAccount"
            fieldProps={{
              size: 'large',
              prefix: <MyIcon type="icon-danwei1" />,
            }}
            placeholder="请输入单位账户:HX_TOP"
            rules={[
              {
                required: true,
                message: "请输入单位账号！"
              },
            ]}
          />
          <ProFormText
            name="account"
            fieldProps={{
              size: 'large',
              prefix: <UserOutlined className={styles.prefixIcon} />,
            }}
            placeholder="请输入用户名:super_admin"
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
              size: 'large',
              prefix: <LockOutlined className={styles.prefixIcon} />,
            }}
            placeholder="请输入密码:123456"
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
