import {DrawerForm, ProFormItem} from "@ant-design/pro-form";
import React, {useEffect, useState} from "react";
import {Tree} from "antd";

function TreeInput({value = [], onChange, ...props}) {
  const [checkedKeys, setCheckedKeys] = useState(value)

  const triggerChange = (checkedKeys, {halfCheckedKeys}) => {
    setCheckedKeys(checkedKeys)
    onChange([...checkedKeys, ...halfCheckedKeys])
  }
  return <Tree checkable {...props} onCheck={triggerChange}
               checkedKeys={checkedKeys}/>
}

export default function FunctionDrawerForm({selectedFunctionIds, functionData, ...props}) {
  return (
    <DrawerForm {...props}>
      <ProFormItem name="functionIds">
        <TreeInput defaultExpandAll={true} fieldNames={{title: "name", key: "id"}} treeData={functionData}/>
      </ProFormItem>
    </DrawerForm>
  )
}
