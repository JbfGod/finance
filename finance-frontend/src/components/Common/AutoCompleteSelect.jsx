import {Select, Space} from "antd";
import styles from "@/components/RightContent/index.less";
import React, {useEffect, useMemo, useState} from "react";
import {flatArrayToMap, getCurrCustomer, removeCurrCustomer, setCurrCustomer} from "@/utils/common";
import {debounce} from "lodash";
import {searchCustomerCueUsingGET} from "@/services/swagger/customerWeb";


export default function AutoCompleteSelect({
    labelName = "label", idName = "id",
    request, debounceTime = 100,
    showOptionsOnSearch, // 仅在搜索关键字的时候显示options
    onChange, ...props
  }) {
  const [value, setValue] = useState()
  const [dataSource, setDataSource] = useState([])
  const options = dataSource.map(d => ({label: d[labelName], value: d[idName]}))
  const dataById = flatArrayToMap(dataSource)
  const loadCustomerOptions = useMemo(() => debounce((searchText) => {
    if (!searchText) {
      setDataSource([])
      return
    }
    request({keyword: searchText}).then(({data}) => {
      setDataSource(data)
    })
  }, debounceTime), [])
  const triggerChange = (v) => {
    setValue(v)
    onChange && onChange(v)
  }
  const onSelect = (v) => {
    triggerChange(v)
  }
  const onClear = () => {
    triggerChange(undefined)
  }
  useEffect(() => {

  }, [])
  return (
    <Select style={{width: 200}} size="small" options={options}
            value={value}
            labelInValue allowClear onClear={onClear}
            onSelect={onSelect} showSearch={true} onSearch={(key) => showOptionsOnSearch && loadCustomerOptions(key)}
            placeholder="输入客户编号搜索客户单位" filterOption={false}
            {...props}
    />
  );
}
