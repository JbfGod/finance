import React, {useRef, useState} from "react";
import {useAccess, useModel} from "umi";
import {Form} from "antd";
import * as common from "@/utils/common";
import {useReactToPrint} from "react-to-print";
import {getUserIdentity} from "@/utils/common";

export function useBoolean(initialValue = false) {
  const [value, setValue] = useState(initialValue)
  return [
    value,
    () => setValue(true),
    () => setValue(false)
  ]
}

export function useModalWithParam(visible = false, state = {}) {
  const [modal, setModal] = useState({visible, state})
  const handleVisible = (v) => {
    setModal({...modal, visible: v})
  }
  const open = (newState = {}, mergedState = false) => {
    setModal({visible: true, state: mergedState? {...state, ...newState}: newState})
  }
  const close = (holdState = false) => {
    if (holdState) {
      handleVisible(visible)
    } else {
      setModal({visible: false, state: {}})
    }
  }
  return {
    state: modal.state, visible: modal.visible,
    open, close, handleVisible
  }
}

export function useCurrentUser() {
  const {initialState = {}} = useModel('@@initialState')
  const {currentUser} = initialState
  const isSuperCustomer = currentUser?.customer?.number === "HX_TOP"
  const isAuth = !! currentUser
  return {
    isAuth,
    currentUser,
    isSuperCustomer
  }
}

export function useSecurity(permissionPrefix = "") {
  const {initialState = {}} = useModel('@@initialState')
  const access = useAccess()
  const {currentUser = {}} = initialState || {}
  const {customer = {}, role} = currentUser || {}
  const isAuth = !! initialState.currentUser
  const isAdmin = role === "ADMIN"
  const hasMultipleIdentities = ["ADMIN", "ADVANCED_APPROVER"].includes(role)
  const isSuperCustomer = customer.number === "HX_TOP"
  const isSuperAdmin = isSuperCustomer && isAdmin
  const isApprover = getUserIdentity() === "APPROVER"

  const proxyCustomer = currentUser?.proxyCustomer || {
    id: customer.id,
    name: customer.name,
    number: customer.number
  }
  const isSuperProxyCustomer = proxyCustomer.number === "HX_TOP"

  const canAuditing = isSuperAdmin || access[`${permissionPrefix}:auditing`]
  const canUnAuditing = isSuperAdmin || access[`${permissionPrefix}:unAuditing`]
  const canBookkeeping = isSuperAdmin || access[`${permissionPrefix}:bookkeeping`]
  const canUnBookkeeping = isSuperAdmin || access[`${permissionPrefix}:unBookkeeping`]
  const canOperating = isSuperAdmin || access[`${permissionPrefix}:base`]
  const canPrint = isSuperAdmin || access[`${permissionPrefix}:print`]
  const canAddFeign = isSuperAdmin || access[`${permissionPrefix}:addForeign`]
  return {
    isAuth,
    isApprover,
    isSuperCustomer,
    hasMultipleIdentities,
    isSuperProxyCustomer,
    canAuditing,
    canUnAuditing,
    canBookkeeping,
    canUnBookkeeping,
    canOperating,
    canPrint,
    canAddFeign,
    proxyCustomer
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
