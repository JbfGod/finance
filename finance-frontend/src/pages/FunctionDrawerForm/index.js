import {DrawerForm, ProForm, ProFormItem} from "@ant-design/pro-form";
import React, {useEffect, useState} from "react";
import {Tree} from "antd";
import {useForm} from "antd/es/form/Form";

export function TreeInput({value = [], initialValue, onChange, ...props}) {
  const [checkedKeys, setCheckedKeys] = useState(value)

  const triggerChange = (keys, {halfCheckedKeys}) => {
    setCheckedKeys(keys)
    onChange([...keys, ...halfCheckedKeys])
  }
  useEffect(() => {
    initialValue && setCheckedKeys(initialValue)
  }, [initialValue])

  return <Tree checkable {...props} onCheck={triggerChange}
               checkedKeys={checkedKeys}/>
}

export function GrantFunctionForm({functionData, functionIdentifies, ...props}) {
  const [form] = useForm()
  useEffect(() => {
    form.setFieldsValue({functionIds: functionIdentifies.map(v => v.id)})
  }, [functionIdentifies])
  return (
    <ProForm form={form} {...props}>
      <ProFormItem name="functionIds">
        <TreeInput defaultExpandAll={true}
                   initialValue={functionIdentifies.filter(v => !v.hasLeaf).map(v => v.id)}
                   fieldNames={{title: "name", key: "id"}}
                   treeData={functionData}/>
      </ProFormItem>
    </ProForm>
  )
}

export default function FunctionDrawerForm({functionData, ...props}) {
  const [form] = useForm()
  return (
    <DrawerForm form={form} {...props}>
      <ProFormItem name="functionIds">
        <TreeInput defaultExpandAll={true} fieldNames={{title: "name", key: "id"}} treeData={functionData}/>
      </ProFormItem>
    </DrawerForm>
  )
}
