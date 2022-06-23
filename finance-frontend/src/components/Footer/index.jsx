import {GithubOutlined} from '@ant-design/icons';
import {DefaultFooter} from '@ant-design/pro-layout';
import {useSecurity} from "@/utils/hooks";

const Footer = () => {
  const security = useSecurity()
  if (!security.isAuth) {
    return null
  }
  const {proxyCustomer} = security
  const defaultMessage = '蚂蚁集团体验技术部出品'
  const currentYear = new Date().getFullYear();
  const copyright = `${currentYear} ${defaultMessage}`
  return (
    <DefaultFooter
      copyright={false}
      links={[
        {
          key: 'CurrentProxyCustomer',
          title: `当前选择的客户单位：${proxyCustomer.name}-(${proxyCustomer.number})`
        }
      ]}
    />
  );
};

export default Footer;
