import {Select, Space} from 'antd';
import React, {useEffect, useMemo, useState} from 'react';
import {debounce} from "lodash";
import {searchCustomerCueUsingGET} from "@/services/swagger/customerWeb";
import {getCurrCustomer, removeCurrCustomer, setCurrCustomer} from "@/utils/common";
import styles from "./index.less"

const SwitchCustomer = (props) => {
  const [value, setValue] = useState(getCurrCustomer())
  const [options, setOptions] = useState([])
  const loadCustomerOptions = useMemo(() => debounce((searchText) => {
    if (!searchText || searchText.length < 2) {
      setOptions([])
      return
    }
    searchCustomerCueUsingGET({keyword: searchText}).then(({data}) => {
      setOptions(data.map(d => ({label: d.number, value: d.id})))
    })
  }, 200), [])
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
      <Select style={{width: 180}} size="small" options={options}
              value={value}
              labelInValue allowClear onClear={onClear}
              onSelect={setCurrCustomer} showSearch={true} onSearch={loadCustomerOptions}
              placeholder="输入关键字搜索客户编号" filterOption={false}
      />
    </Space>
  );
};

export default SwitchCustomer;
