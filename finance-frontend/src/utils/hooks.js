import React, {useMemo, useRef, useState} from "react";
import {useModel} from "umi";
import {Form} from "antd";
import * as common from "@/utils/common";
import {useReactToPrint} from "react-to-print";
import {searchCustomerCueUsingGET} from "@/services/swagger/customerWeb";
import {debounce} from "lodash";

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

export function useSecurity() {
  const {initialState} = useModel('@@initialState')
  const {currentUser = {}} = initialState || {}
  const {customerId} = currentUser
  const isSuperCustomer = customerId === 0
  const onlyRead = customerId !== (sessionStorage.getItem("CURR_CUSTOMER") || 0)
  return {
    isSuperCustomer,
    onlyRead
  }
}

export function useTableForm(editableOptions) {
  const [form] = Form.useForm()
  const editable = {
    form, type: "single",
    actionRender: (row, config, defaultDom) => [defaultDom.save, defaultDom.cancel],
    ...editableOptions
  }
  return {form, editable}
}

export function useTableExpandable(key = "id", enhanceDataChange) {
  const [firstLoadFlag, setFirstLoadFlag] = useState(false)
  const [expandedKeys, setExpandedKeys] = useState([])
  const onLoad = (dataSource) => {
    enhanceDataChange && enhanceDataChange(dataSource)
    if (firstLoadFlag) {
      setFirstLoadFlag(true)
      return
    }
    setExpandedKeys(common.flatTree(dataSource).map(data => data.id))
  }
  const expandable = {
    expandRowByClick: true,
    expandedRowKeys: expandedKeys,
    onExpandedRowsChange: setExpandedKeys}
  return [expandable, onLoad]
}

export function useArrayToTree(dataArray,
                               keyField = "number",
                               parentKeyField = "parentNumber",
                               childrenField = "children") {
  const dataByKey = {}
  // 创建副本
  const dataArrayCopy = [...dataArray]
  dataArrayCopy.forEach(tmp => {
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

export function usePrint() {
  const printRef = useRef()
  const [state, setState] = useState({
    // 打印预备状态
    prepare: false,
    params: {}
  })
  const handlePrint = useReactToPrint({
    content: () => printRef.current,
    onAfterPrint: () => {
      setState({prepare: false})
    }
  });
  const onPrint = (params = {}) => {
    setState({prepare: true, params})
  }
  return [{
    ref: printRef,
    prepare: state.prepare,
    params: state.params,
    print:  () => {
      if (state.prepare === false) {
        console.error("print only when prepare status is true")
        return
      }
      handlePrint()
    }
  }, onPrint]
}
