import React, {useMemo, useState} from "react";
import {debounce} from "lodash";
import {AutoComplete, Input} from "antd";
import styles from "./index.less"

export default function AutoCompleteInput({request, onChange, onFocus, ...props}) {
  const [value, setValue] = useState('')
  const [options, setOptions] = useState([])
  const onSearch = useMemo(() => debounce((searchText) => {
    if (!searchText) {
      setOptions([])
      return
    }
    request(searchText).then(res => setOptions(res.data.map(v => ({value: v}))))
  }, 500), []);
  const onEnhanceChange = (data) => {
    setValue(data);
    onChange && onChange(data)
  };
  return (
    <AutoComplete value={value} options={options}
                  onBlur={() => setOptions([])} className={styles.autoComplete}
                  onSearch={onSearch} onChange={onEnhanceChange} {...props}>
      <Input bordered={false} onFocus={onFocus}/>
    </AutoComplete>
  )
}
