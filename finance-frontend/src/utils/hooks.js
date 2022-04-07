import React, {useState} from "react";
import {useModel} from "umi";
import {Form} from "antd";

export function useBoolean(initialValue = false) {
  const [value, setValue] = useState(initialValue)
  return [
    value,
    () => setValue(true),
    () => setValue(false)
  ]
}

export function useCurrentUser() {
  const { initialState } = useModel('@@initialState')
  return initialState?.currentUser
}

export function useTableForm(editableOptions) {
  const [form] = Form.useForm()
  const editable = {
    form, type:"single",
    actionRender: (row, config, defaultDom) => [defaultDom.save, defaultDom.cancel],
    ...editableOptions
  }
  return {form, editable}
}
