import React, {useMemo, useState} from "react";
import {debounce} from "lodash";
import {AutoComplete, Input} from "antd";

export default function AutoCompleteInput({request, onChange, ...props}) {
  const [value, setValue] = useState('')
  const [options, setOptions] = useState([])
  const onSearch = useMemo(() => debounce((searchText) => {
    if (!searchText || searchText.length < 1) {
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
    <AutoComplete style={{width: "100%"}} value={value} options={options}
                  onBlur={() => setOptions([])}
                  onSearch={onSearch} onChange={onEnhanceChange} {...props}>
      <Input/>
    </AutoComplete>
  )
}
