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

export function GrantResourceForm({resourceData, resourceIdentifies, ...props}) {
  const [form] = useForm()
  useEffect(() => {
    form.setFieldsValue({resourceIds: resourceIdentifies.map(v => v.id)})
  }, [resourceIdentifies])
  return (
    <ProForm form={form} {...props}>
      <ProFormItem name="resourceIds">
        <TreeInput defaultExpandAll={true}
                   initialValue={resourceIdentifies.filter(v => !v.hasLeaf).map(v => v.id)}
                   fieldNames={{title: "name", key: "id"}}
                   treeData={resourceData}/>
      </ProFormItem>
    </ProForm>
  )
}

export default function ResourceDrawerForm({resourceData, initialValues, ...props}) {
  const [form] = useForm()
  useEffect(() => {
    form.setFieldsValue(initialValues)
  }, [initialValues])
  return (
    <DrawerForm form={form} {...props}>
      <ProFormItem name="resourceIds">
        <TreeInput defaultExpandAll={true} fieldNames={{title: "name", key: "id"}} treeData={resourceData}/>
      </ProFormItem>
    </DrawerForm>
  )
}
