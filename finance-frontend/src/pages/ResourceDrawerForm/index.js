import {DrawerForm, ProForm, ProFormItem} from "@ant-design/pro-form";
import React, {useEffect, useMemo, useState} from "react";
import {Tree} from "antd";
import {useForm} from "antd/es/form/Form";
import {flatTree, getHasChildNode} from "@/utils/common";

export function TreeInput({value = [], initialValue, onChange, fieldNames, treeData, ...props}) {
  const [checkedKeys, setCheckedKeys] = useState(value)

  const triggerChange = ({checked}) => {
    setCheckedKeys(checked)
    onChange(checked)
  }
  useEffect(() => {
    initialValue && setCheckedKeys(initialValue)
  }, [initialValue])

  const overCheckedKeys = value || checkedKeys
  return <Tree checkable checkStrictly treeData={treeData} fieldNames={fieldNames} {...props} onCheck={triggerChange}
               checkedKeys={overCheckedKeys}/>
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
                   fieldNames={{title: "name", key: "id"}}
                   treeData={resourceData}/>
      </ProFormItem>
    </ProForm>
  )
}

export default function ResourceDrawerForm({resourceData, initialValues, ...props}) {
  const [formRef] = useForm()
  useEffect(() => {
    formRef.setFieldsValue(initialValues)
  }, [initialValues])
  return (
    <DrawerForm form={formRef} {...props}>
      <ProFormItem name="resourceWithOperateIds">
        <TreeInput defaultExpandAll={true} fieldNames={{title: "name", key: "id"}} treeData={resourceData}/>
      </ProFormItem>
    </DrawerForm>
  )
}
