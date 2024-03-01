import {useSecurity} from "@/utils/hooks";
import {switchProxyCustomerUsingPUT} from "@/services/swagger/userWeb";
import {history} from "@umijs/max"

const Footer = () => {
  return null
  const security = useSecurity()
  if (!security.isAuth) {
    return null
  }
  const {proxyCustomer, isApprover, hasMultipleIdentities} = security
  const itemStyle = {marginRight: 10}
  const quitProxyCustomer = () => {
    switchProxyCustomerUsingPUT().then(_ => {
      security.refresh()
    })
  }
  return (
    <div style={{textAlign: "center", marginBottom: 10}}>
      <div>
        {!isApprover && security.isSuperCustomer && (
          <>
            <span style={itemStyle}>{proxyCustomer.name}-({proxyCustomer.number})</span>
            <a style={itemStyle} onClick={() => window.href = "/"}>切换客户</a>
          </>
        )}
        {hasMultipleIdentities && (
          <a style={itemStyle} onClick={() => history.push("/switchIdentity")}>切换身份</a>
        )}
        <a onClick={quitProxyCustomer}>返回</a>
      </div>
    </div>
  );
};

export default Footer;
