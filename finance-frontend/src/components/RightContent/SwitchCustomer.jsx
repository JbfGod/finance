import {Select, Space} from 'antd';
import React, {useEffect, useMemo, useState} from 'react';
import {debounce} from "lodash";
import {searchCustomerCueUsingGET} from "@/services/swagger/customerWeb";
import {flatArrayToMap, getCurrCustomer, removeCurrCustomer, setCurrCustomer} from "@/utils/common";
import styles from "./index.less"

const SwitchCustomer = (props) => {
  const currCustomer = getCurrCustomer()
  const [value, setValue] = useState(() => (
    currCustomer ? {label: `${currCustomer.name}[${currCustomer.number}]`, value: currCustomer.id} : undefined
  ))
  const [customers, setCustomers] = useState([])
  const options = customers.map(d => ({label: `${d.name}[${d.number}]`, value: d.id}))
  const customerById = flatArrayToMap(customers)
  const loadCustomerOptions = useMemo(() => debounce((searchText) => {
    if (!searchText || searchText.length < 2) {
      setCustomers([])
      return
    }
    searchCustomerCueUsingGET({keyword: searchText}).then(({data}) => {
      setCustomers(data)
    })
  }, 200), [])
  const onSelect = (v) => {
    setCurrCustomer(customerById[v.key])
    window.location.reload()
  }
  const onClear = () => {
    removeCurrCustomer()
    setValue(undefined)
  }
  useEffect(() => {
    return () => {
      removeCurrCustomer()
    }
  }, [])
  return (
    <Space align="center" className={styles.switchContainer}>
      <span className="anticon">切换客户单位</span>
      <Select style={{width: 200}} size="small" options={options}
              value={value}
              labelInValue allowClear onClear={onClear}
              onSelect={onSelect} showSearch={true} onSearch={loadCustomerOptions}
              placeholder="输入客户编号搜索客户单位" filterOption={false}
      />
    </Space>
  );
};

export default SwitchCustomer;
