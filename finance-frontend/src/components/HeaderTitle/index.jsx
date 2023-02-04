import React, {useEffect, useState} from "react"
import {Select, Space} from "antd";
import {ownedCustomerUsingGET, switchProxyCustomerUsingPUT} from "@/services/swagger/userWeb";
import {useSecurity} from "@/utils/hooks";
import dayjs from "dayjs";

export default function ({logo, title}) {
  const [customers, setCustomers] = useState([])
  const {isFinanceMode, proxyCustomer} = useSecurity()
  const selectedCustomer = proxyCustomer?.id
  useEffect(() => {
    ownedCustomerUsingGET().then(res => setCustomers(res.data))
  }, [])
  return (
    <>
      <a>
        {logo}
        {title}
      </a>
      {isFinanceMode && (
        <Space style={{marginLeft: 35}} onClick={e => e.stopPropagation()}>
          <Select style={{width: 280}} value={selectedCustomer}
                  options={customers.map(cus => ({label: `${cus.number}_${cus.name}`, value: cus.id}))}
                  onChange={v => {
                    switchProxyCustomerUsingPUT({customerId: v}).then(_ => {
                      window.location.href = "/"
                    })
                  }}/>
          <a>{proxyCustomer ? dayjs(`${proxyCustomer?.currentPeriod}`, "YYYYMM").format("YYYY年MM期") : ""}</a>
        </Space>
      )}
    </>
  )
}
