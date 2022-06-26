import {GithubOutlined} from '@ant-design/icons';
import {DefaultFooter} from '@ant-design/pro-layout';
import {useSecurity} from "@/utils/hooks";
import {switchProxyCustomerUsingPUT} from "@/services/swagger/userWeb";

const Footer = () => {
  const security = useSecurity()
  if (!security.isAuth) {
    return null
  }
  const {proxyCustomer} = security
  const itemStyle = {marginRight: 10}
  const quitProxyCustomer = () => {
    switchProxyCustomerUsingPUT().then(_ => {
      window.location.reload()
    })
  }
  return (
    <div style={{textAlign: "center", marginBottom: 10}}>
      <div>
        <span style={itemStyle}>{proxyCustomer.name}-({proxyCustomer.number})</span>
        <a  style={itemStyle} href="/user/switchCustomer">切换客户</a>
        <a  onClick={quitProxyCustomer}>返回</a>
      </div>
    </div>
  );
};

export default Footer;
