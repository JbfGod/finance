import {DrawerForm, ProForm, ProFormItem} from "@ant-design/pro-form";
import React, {useEffect, useState} from "react";
import {Tabs, Tree} from "antd";
import {useForm} from "antd/es/form/Form";

export function TreeInput({value = [], initialValue, onChange, fieldNames, treeData, ...props}) {
  const [checkedKeys, setCheckedKeys] = useState(value)
  const [activeTabKey, setActiveTabKey] = useState('MANAGE')

  const triggerChange = ({checked}) => {
    setCheckedKeys(checked)
    onChange(checked)
  }
  useEffect(() => {
    initialValue && setCheckedKeys(initialValue)
  }, [initialValue])

  const overCheckedKeys = value || checkedKeys
  const children = (
    <Tree checkable checkStrictly treeData={treeData.filter(d => d.module === activeTabKey)}
          fieldNames={fieldNames} {...props} onCheck={triggerChange}
          checkedKeys={overCheckedKeys}/>
  )
  return (
    <Tabs defaultActiveKey={"Manage"} items={[
      {
        key: "MANAGE", label: "CRS", children
      },
      {key: "FINANCE", label: "记账模块", children},
    ]} onChange={setActiveTabKey}/>
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
