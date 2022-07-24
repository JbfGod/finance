import React, {useEffect, useMemo, useRef, useState} from "react";
import {
  ModalForm,
  ProFormDatePicker,
  ProFormGroup,
  ProFormItem,
  ProFormSelect,
  ProFormText
} from "@ant-design/pro-form";
import {Button, Empty, Form, InputNumber} from "antd";
import styles from "./index.less"
import {CURRENCY_TYPE, LENDING_DIRECTION} from "@/constants";
import {
  addVoucherUsingPOST,
  updateVoucherUsingPUT,
  usableSerialNumberUsingGET,
  voucherDetailUsingGET
} from "@/services/swagger/voucherWeb";
import {flatArrayToMap} from "@/utils/common";
import {currencyOfYearMonthUsingGET} from "@/services/swagger/currencyWeb";
import {history} from "umi";
import ProCard from "@ant-design/pro-card";
import {EditableProTable} from "@ant-design/pro-table";
import AutoCompleteInput from "@/components/Common/AutoCompleteInput";
import {searchExpenseItemCueUsingGET} from "@/services/swagger/expenseBillWeb";
import moment from "moment";
import {AdvancedSubjectSelect} from "@/components/AdvancedSubjectSelect";

const CAPACITY = 5
export default ({modal, onSuccess, subjects, setSubjects, subjectById, defaultVoucherDate, ...props}) => {
  const {
    mode = "add", currencyType = CURRENCY_TYPE.LOCAL,
    voucherId, visible
  } = modal
  if (!visible) {
    return null
  }
  const isAddMode = mode === "add"
  const isViewMode = mode === "view"
  const isEditMode = mode === "edit"
  const isForeignCurrency = currencyType === CURRENCY_TYPE.FOREIGN
  const isLocalCurrency = currencyType === CURRENCY_TYPE.LOCAL
  const [formRef] = Form.useForm()
  const actionRef = useRef()
  const [editableKeys, setEditableKeys] = useState([])

  const [currencies, setCurrencies] = useState([])
  const currencyById = useMemo(() => flatArrayToMap(currencies), [currencies])
  const [voucherDetail, setVoucherDetail] = useState({})

  // 加载货币列表
  const loadCurrencyByYearMonth = (yearMonthNum) => {
    if (isForeignCurrency) {
      if (yearMonthNum) {
        currencyOfYearMonthUsingGET({yearMonthNum}).then(({data}) => setCurrencies(data))
        return
      }
      setCurrencies([])
    }
  }
  const initialBillDetail = () => {
    voucherDetailUsingGET({id: voucherId})
      .then(({data}) => {
        setVoucherDetail(data)
        const newItems = []
        for (let i = 0; i < CAPACITY; i++) {
          newItems.push({index: `${i}`, ...(data.items[i] || {})})
        }
        formRef.setFieldsValue({...data, items: newItems})
      })
  }
  const getDeleteItemIds = (formValues) => {
    const {items} = voucherDetail
    const {items: currItems} = formValues
    return items.map(item => item.id).filter(itemId =>
      currItems.findIndex(tmp => tmp.id === itemId) === -1
    )
  }
  const loadUsableSerialNumber = () => {
    usableSerialNumberUsingGET().then(({data}) => formRef.setFieldsValue({serialNumber: data}))
  }
  const currencyId = Form.useWatch('currencyId', formRef);
  const columns = [
    {
      title: (<div style={{textAlign: "center"}}>摘要</div>), dataIndex: "summary", width: 200,
      renderFormItem: () => (
        <AutoCompleteInput placeholder="摘要" disabled={isViewMode} request={(keyword) => {
          return searchExpenseItemCueUsingGET({column: 'SUMMARY', keyword})
        }}/>
      ),
    },
    {
      title: "会计科目", dataIndex: "subjectId", width: 250,
      renderFormItem: () => (
        <AdvancedSubjectSelect subjects={subjects} placeholder="只能选择费用类科目"
               fieldsName={{key: "id", title: (v) => `${v.number}-${v.name}`}}
               disableFilter={(subject) => {
                 return subject.hasLeaf
               }}
               setSubjects={setSubjects}
               onlySelectedLeaf={true} disabled={isViewMode}
               style={{width: '100%'}} />
      ),
      render: (_, row) => {
        const v = subjectById[row.subjectId]
        return v ? `${v.number}-${v.name}` : "-"
      }
    },
    ...(isForeignCurrency ? [
      {
        title: "借方金额",
        children: [
          {
            title: "原币",
            dataIndex: "debitAmount",
            valueType: "digit",
            fieldProps: {style: {width: "100%"}}
          },
          {
            title: "汇率", dataIndex: "debitAmount", editable: false, width: 50,
            renderText: (v) => v && currencyById[currencyId]?.rate || "-"
          },
          {
            title: "本币", dataIndex: "debitAmount", valueType: "digit", editable: false,
            render: (v, row) => ((currencyById[currencyId]?.rate || 1) * row.debitAmount) || "-"
          },
        ]
      },
      {
        title: "贷方金额",
        children: [
          {
            title: "原币",
            dataIndex: "creditAmount",
            valueType: "digit",
            fieldProps: {style: {width: "100%"}}
          },
          {
            title: "汇率", dataIndex: "creditAmount", editable: false, width: 50,
            renderText: (v) => v && currencyById[currencyId]?.rate || "-"
          },
          {
            title: "本币", dataIndex: "creditAmount", valueType: "digit", editable: false,
            render: (v, row) => ((currencyById[currencyId]?.rate || 1) * row.creditAmount) || "-"
          },
        ]
      }
    ] : [
      {
        title: "借方金额",
        valueType: "digit",
        dataIndex: "debitAmount",
        fieldProps: {style: {width: "100%"}}
      },
      {
        title: "贷方金额",
        valueType: "digit",
        dataIndex: "creditAmount",
        fieldProps: {style: {width: "100%"}}
      }
    ]),
  ]
  const voucherDate = Form.useWatch("voucherDate", formRef)
  const yearMonth = moment.isMoment(voucherDate) ? voucherDate.format("YYYYMM")
    :
    `${voucherDate||""}`.replaceAll(/(\d{4})-(1[0-2]|0?[1-9])-\d{1,2}/g, "$1$2")
  useEffect(() => {
    loadCurrencyByYearMonth(yearMonth)
  }, [yearMonth])
  useEffect(() => {
    if (isAddMode) {
      const newItems = []
      for (let i = 0; i < CAPACITY; i++) {
        newItems.push({index: i})
      }
      formRef.setFieldsValue({items: newItems, voucherDate: defaultVoucherDate})
      loadUsableSerialNumber()
    } else if (isEditMode || isViewMode) {
      initialBillDetail()
    }
  }, [])
  return (
    <ModalForm width={1000} form={formRef}
               title={`${isAddMode ? "添加" : isEditMode ? "编辑" : "预览"}${isForeignCurrency ? "外币凭证" : "本币凭证"}`}
               onFinish={async (f,) => {
                 if (isViewMode) {
                   return true
                 }
                 let formData = {
                   ...f,
                   ...(isForeignCurrency ? {} : {
                     rate: 1,
                     currencyId: 0,
                     currencyName: "人民币",
                   }),
                   items: f.items.filter(item => item.subjectId).map(item => {
                     const isDebit = !!item.debitAmount
                     return {
                       ...item,
                       subjectName: subjectById[item?.subjectId]?.name,
                       subjectNumber: subjectById[item?.subjectId]?.number,
                       lendingDirection: isDebit ? LENDING_DIRECTION.BORROW.value : LENDING_DIRECTION.LOAN.value,
                       amount: isDebit ? item.debitAmount : item.creditAmount
                     }
                   })
                 }
                 if (isAddMode) {
                   return addVoucherUsingPOST(formData).then(_ => {
                     onSuccess && onSuccess()
                     return true
                   })
                 }
                 formData = {...formData, deletedItemIds: getDeleteItemIds(formData), id: voucherId}
                 return updateVoucherUsingPUT(formData).then(_ => {
                   onSuccess && onSuccess()
                   return true
                 })
               }}
               onValuesChange={(changedValues, allValues) => {
                 // 监控items变化，对立借贷只能相对存在
                 const changedItems = changedValues.items
                 if (!changedItems) {
                   return
                 }
                 const {items} = allValues
                 Object.keys(changedItems).forEach((key) => {
                   const {debitAmount, creditAmount} = changedItems[key]
                   if (debitAmount) {
                     const newValue = {...items[key]}
                     formRef.setFieldsValue({
                       items: items.map(item => {
                         if (item.index === key && newValue.creditAmount) {
                           delete newValue.creditAmount
                           return newValue
                         }
                         return item
                       })
                     })
                   } else if (creditAmount) {
                     const newValue = {...items[key]}
                     formRef.setFieldsValue({
                       items: items.map(item => {
                         if (item.index === key && newValue.debitAmount) {
                           delete newValue.debitAmount
                           return newValue
                         }
                         return item
                       })
                     })
                   }
                 })
               }}
               visible={visible}
               layout="horizontal"
               modalProps={{
                 destroyOnClose: true,
                 className: styles.voucherContainer,
                 placement: "right"
               }}
               initialValues={{
                 ...(isLocalCurrency?{unit: "元"}:{}),
                 voucherDate: moment().format("YYYY-MM-DD")
               }}
               {...props}
    >
      <ProFormGroup size={isForeignCurrency ? 20 : 100}>
        <ProFormItem name="serialNumber" label="凭证号">
          <InputNumber placeholder="凭证号" min={0} disabled={isViewMode}/>
        </ProFormItem>
        <ProFormDatePicker name="voucherDate" label="凭证日期" placeholder="凭证日期" width={120} disabled={isViewMode}/>
        {isForeignCurrency && (
          <>
            <ProFormSelect name="currencyId" label="币种" placeholder="币种" width={200}
                           fieldProps={{
                             notFoundContent: (
                               <ProCard colSpan={24} bordered>
                                 {voucherDate ? (
                                   <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}
                                          description={
                                            <span>{voucherDate?.format?.("YYYY年MM月")},暂无外可用的汇率数据，无法添加外币凭证</span>}>
                                     <Button type="primary"
                                             onClick={() => history.push("/voucher/currency")}>去添加外币汇率</Button>
                                   </Empty>
                                 ) : (
                                   <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}
                                          description={<span>请先选择凭证日期</span>}>
                                   </Empty>
                                 )}
                               </ProCard>
                             )
                           }}
                           options={currencies.map(v => ({label: v.name, value: v.id}))}
                           transform={v => ({
                             currencyId: v,
                             currencyName: currencyById[v]?.name,
                             rate: currencyById[v]?.rate,
                           })}
            />
          </>
        )}
        <ProFormText name="unit" label="单位" placeholder="单位" width={125} disabled={isViewMode}/>
        <ProFormItem name="attachmentNum" label="附件张数">
          <InputNumber placeholder="附件张数" min={0} disabled={isViewMode}/>
        </ProFormItem>
      </ProFormGroup>
      <EditableProTable name="items" actionRef={actionRef} columns={columns} bordered
                        onRow={(record, index) => ({
                          onClick: (e) => {
                            e.stopPropagation()
                            editableKeys.includes(index) || setEditableKeys([index])
                            // 优化用户体验，自动同步上一行的摘要
                            if (index === 0) {
                              return
                            }
                            const items = formRef.getFieldValue(["items"])
                            if (items[index].summary) {
                              return
                            }
                            for (let i = index - 1; i >= 0; i--) {
                              if (items[i].summary) {
                                formRef.setFieldsValue({items: items.map(item => {
                                    if (item.index === index) {
                                      return {...item, summary: items[i].summary}
                                    }
                                    return item
                                })})
                                return
                              }
                            }
                          }
                        })}
                        editable={isViewMode ? false : {editableKeys}} controlled
                        size="small" className={styles.voucherTbl} rowKey="index"
                        recordCreatorProps={false}
      />
    </ModalForm>
  )
}
