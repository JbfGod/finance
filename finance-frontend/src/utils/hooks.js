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

export function useModalWithParam(visible = false, params = {}) {
  const [modal, setModal] = useState({visible, ...params})
  const handleModalVisible = (v) => {
    setModal({...modal, visible: v})
  }
  const openModal = (params) => {
    setModal({visible: true, ...params})
  }
  return [modal, handleModalVisible, openModal]
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

export function useArrayToTree(dataArray,
                               keyField = "number",
                               parentKeyField = "parentNumber",
                               childrenField = "children") {
  const dataByKey = {}
  // 创建副本
  dataArray = [...dataArray]
  dataArray.forEach(tmp => {
    // 创建副本
    const node = {...tmp}
    dataByKey[node[keyField]] = node
  })
  const treeArray = []
  Object.values(dataByKey).forEach(node => {
    const parentNode = dataByKey[node[parentKeyField]]
    if (parentNode == null) {
      treeArray.push(node)
      return
    }
    let children = parentNode[childrenField]
    if (children == null) {
      children = []
      parentNode[childrenField] = children
    }
    children.push(node)
  })
  return [dataByKey, treeArray]
}

export function useFlatTree(treeData = [], childrenField = "children") {
  const flatArray = []
  const recursion = (node)  => {
    if (node == null) {
      return
    }
    flatArray.push(node)
    const children = node[childrenField]
    if (children == null || children.length === 0) {
      return
    }
    for (let i = 0; i < children.length; i++) {
      recursion(children[i])
    }
  }
  for (let i = 0; i < treeData.length; i++) {
    recursion(treeData[i])
  }
  return flatArray
}
