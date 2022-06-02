import React, {useEffect, useMemo, useState} from "react";
import {
  DrawerForm,
  ProFormDatePicker,
  ProFormGroup,
  ProFormItem,
  ProFormList,
  ProFormSelect,
  ProFormText
} from "@ant-design/pro-form";
import {Button, Empty, Form, InputNumber, Row} from "antd";
import styles from "./index.less"
import {searchExpenseItemCueUsingGET} from "@/services/swagger/expenseBillWeb";
import ExtTreeSelect from "@/components/Common/ExtTreeSelect";
import {CURRENCY_TYPE, LENDING_DIRECTION} from "@/constants";
import AutoCompleteInput from "@/components/Common/AutoCompleteInput";
import {addVoucherUsingPOST, updateVoucherUsingPUT, voucherDetailUsingGET} from "@/services/swagger/voucherWeb";
import {flatArrayToMap} from "@/utils/common";
import {currencyOfYearMonthUsingGET} from "@/services/swagger/currencyWeb";
import {history} from "umi";
import ProCard from "@ant-design/pro-card";

export default ({modal, onSuccess, subjects, subjectById, ...props}) => {
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
  const [formRef] = Form.useForm()
  const [selectedRowIndex, setSelectedRowIndex] = useState(0)

  const [currencies, setCurrencies] = useState([])
  const currencyById = useMemo(() => flatArrayToMap(currencies), [currencies])
  const [voucherDetail, setVoucherDetail] = useState({})

  // 加载货币列表
  const loadCurrencyByYearMonth = (yearMonth) => {
    isForeignCurrency && yearMonth && (
      currencyOfYearMonthUsingGET({yearMonth}).then(({data}) => setCurrencies(data))
    )
  }
  const initialBillDetail = () => {
    voucherDetailUsingGET({id: voucherId})
      .then(({data}) => {
        setVoucherDetail(data)
        formRef.setFieldsValue(data)
      })
  }
  const getDeleteItemIds = (formValues) => {
    const {items} = voucherDetail
    const {items: currItems} = formValues
    return items.map(item => item.id).filter(itemId =>
      currItems.findIndex(tmp => tmp.id === itemId) === -1
    )
  }
  const voucherDate = Form.useWatch("voucherDate", formRef)
  const yearMonth = voucherDate && voucherDate.format("YYYYMM")
  useEffect(() => {
    loadCurrencyByYearMonth(yearMonth)
  }, [yearMonth])
  useEffect(() => {
    if (isAddMode) {
      formRef.setFieldsValue({items: [{}]})
    }
    if (voucherId != null) {
      initialBillDetail()
    }
  }, [])
  const title = isForeignCurrency ? "外币凭证" : "本币凭证"
  const currencyId = Form.useWatch('currencyId', formRef);
  const currencyLabel = `币种(汇率：${currencyById[currencyId]?.rate || 1})`
  if (!visible) {
    return null
  }
  return (
    <DrawerForm width={1000} form={formRef}
                title={`${isAddMode ? "添加" : isEditMode ? "编辑" : "预览"}${title}`}
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
                    items: f.items.map(item => ({
                      ...item,
                      subjectName: subjectById[item?.subjectId]?.name,
                      subjectNumber: subjectById[item?.subjectId]?.number,
                      lendingDirection: item.lendingDirection
                    }))
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
                visible={visible}
                drawerProps={{
                  destroyOnClose: true,
                  className: styles.voucherDrawer,
                  placement: "right"
                }}
                {...props}
    >
      <div style={{width: 950}}>
        <ProFormGroup size={8}>
          {isForeignCurrency && (
            <>
              <ProFormDatePicker name="voucherDate" label="凭证日期" placeholder="凭证日期" width={120} disabled={isViewMode}/>
              <ProFormSelect name="currencyId" label={currencyLabel} placeholder="币种" width={200}
                             fieldProps={{
                               notFoundContent: (
                                 <ProCard colSpan={24} bordered>
                                   {yearMonth? (
                                     <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}
                                            description={<span>{yearMonth}月暂无外可用的汇率数据，无法添加外币凭证</span>}>
                                       <Button type="primary" onClick={() => history.push("/voucher/currency")}>去添加外币汇率</Button>
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
        <ProFormList name="items" min={1}
                     creatorButtonProps={{
                       type: 'primary',
                       disabled: isViewMode,
                       creatorButtonText: '添加凭证记录',
                     }}
                     deleteIconProps={!isViewMode}
                     copyIconProps={false}
                     itemRender={({listDom, action}, {record, index}) => {
                       return (
                         <div onClick={() => setSelectedRowIndex(index)} style={{padding: "12px 0 0 12px"}}
                              className={index === selectedRowIndex ? styles["selectedRow"] : ""}>
                           <Row align="middle">
                             {listDom}
                             {action}
                           </Row>
                         </div>
                       )
                     }}
        >
          {(f, index, action) => {
            return (
              <ProFormGroup size={8}>
                <ProFormItem name="summary" label="摘要" style={{width: 290}}>
                  <AutoCompleteInput placeholder="摘要" disabled={isViewMode} request={(keyword) => {
                    return searchExpenseItemCueUsingGET({column: 'SUMMARY', keyword})
                  }}/>
                </ProFormItem>
                <ProFormItem name="subjectId" label="科目" style={{width: 180}}>
                  <ExtTreeSelect options={subjects} placeholder="只能选择费用类科目"
                                 onlySelectedLeaf={true} disabled={isViewMode}
                                 style={{width: '100%'}}/>
                </ProFormItem>
                <Form.Item noStyle shouldUpdate={(prev, next) => {
                  const shouldUpdate = prev.items[index]?.subjectId !== next.items[index]?.subjectId
                  if (shouldUpdate) {
                    let {lendingDirection} = subjectById[next.items[index]?.subjectId] || {}
                    lendingDirection = lendingDirection === LENDING_DIRECTION.DEFAULT.value
                      ? LENDING_DIRECTION.BORROW.value
                      : lendingDirection
                    formRef.setFields([{name: ["items", index, "lendingDirection"], value: lendingDirection}])
                  }
                  return shouldUpdate
                }}>
                  {(form) => {
                    /*const subjectId = form.getFieldValue(["items", index, "subjectId"])
                    const {lendingDirection} = subjectById[subjectId] || {}
                    const disabled = lendingDirection == null || lendingDirection !== LENDING_DIRECTION.DEFAULT.value*/
                    return (
                      <ProFormSelect name="lendingDirection" allowClear={false} label="借贷方向" disabled={isViewMode}
                                     options={Object.values(LENDING_DIRECTION).filter(v => v !== LENDING_DIRECTION.DEFAULT)}/>
                    )
                  }}
                </Form.Item>
                <ProFormItem name="amount" label="原币金额">
                  <InputNumber placeholder="原币金额" min={0} disabled={isViewMode} width={200}/>
                </ProFormItem>
                {isForeignCurrency ? (
                  <Form.Item noStyle shouldUpdate={(prev, next) => {
                    const currAmount = next.items[index]?.amount
                    const shouldUpdate = prev.items[index]?.amount !== currAmount || prev.currencyId !== next.currencyId
                    const {rate} = currencyById[next.currencyId] || {}
                    if (shouldUpdate) {
                      formRef.setFields([{name: ["items", index, "billAmount"], value: currAmount * (rate || 1)}])
                    }
                  }}>
                    {(f) => {
                      return (
                        <ProFormItem name="billAmount" label="本币">
                          <InputNumber placeholder="本币" min={0} disabled={true}/>
                        </ProFormItem>
                      )
                    }}
                  </Form.Item>
                ) : null}
              </ProFormGroup>
            )
          }}
        </ProFormList>
      </div>
    </DrawerForm>
  )
}
