import {useSecurity} from "@/utils/hooks";
import {switchProxyCustomerUsingPUT} from "@/services/swagger/userWeb";

const Footer = () => {
    const security = useSecurity()
    if (!security.isAuth) {
        return null
    }
    const {proxyCustomer, isApprover, hasMultipleIdentities} = security
    const itemStyle = {marginRight: 10}
    const quitProxyCustomer = () => {
        switchProxyCustomerUsingPUT().then(_ => {
            window.location.reload()
        })
    }
    return (
        <div style={{textAlign: "center", marginBottom: 10}}>
            <div>
                {!isApprover && security.isSuperCustomer && (
                    <>
                        <span style={itemStyle}>{proxyCustomer.name}-({proxyCustomer.number})</span>
                        <a style={itemStyle} href="/user/switchCustomer">切换客户</a>
                    </>
                )}
                {hasMultipleIdentities && (
                    <a style={itemStyle} href="/user/switchIdentity">切换身份</a>
                )}
                <a onClick={quitProxyCustomer}>返回</a>
            </div>
        </div>
    );
};

export default Footer;
