import {TreeSelect} from "antd";
import React, {useState} from "react"

const {TreeNode} = TreeSelect

export default function ExtTreeSelect(
  {
    value,
    options,
    onChange,
    multiple = false,
    itemPropsHandler,
    // function(nodeData):bool，
    disableFilter,
    // 只能选择叶子节点
    onlySelectedLeaf = false,
    labelInValue = false,
    fieldsName = {title: "name", key: "id"},
    ...props
  }
) {
  const [ctrlValue, setCtrlValue] = useState(value || (labelInValue ? {} : undefined))
  const safeValue = value || ctrlValue
  const triggerChange = (v, label, extra) => {
    const newValue = labelInValue ? {label: multiple ? label : label[0], value: v} : v
    setCtrlValue(newValue)
    onChange && onChange(newValue)
  }
  const recursion = (nodeData) => {
    if (nodeData == null) {
      return
    }
    let itemProps = {
      key: nodeData[fieldsName.key] || nodeData.value,
      value: nodeData[fieldsName.key] || nodeData.value,
      title: typeof fieldsName.title === "function" ? fieldsName.title(nodeData) : nodeData[fieldsName.title],
    }
    itemPropsHandler && (itemProps = {...itemProps, ...itemPropsHandler(nodeData)})
    disableFilter && disableFilter(nodeData) && (itemProps.disabled = true)
    const children = nodeData.children
    if (children == null || children.length === 0) {
      return (
        <TreeNode {...itemProps}/>
      )
    }
    onlySelectedLeaf &&  (itemProps.disabled = true)
    return (
      <TreeNode {...itemProps}>
        {children.map(child => recursion(child))}
      </TreeNode>
    )
  }
  return (
    <TreeSelect
      showSearch
      allowClear
      treeDefaultExpandAll
      filterTreeNode={(v, treeNode) => {
        return treeNode?.title?.startsWith(v)
      }}
      treeLine={{showLine: true}}
      onChange={triggerChange}
      value={labelInValue ? safeValue.value : safeValue}
      {...props}
    >
      {options.map(opt => recursion(opt))}
    </TreeSelect>
  )
}
