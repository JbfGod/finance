import React, {useEffect, useRef} from 'react'
import {PageContainer} from "@ant-design/pro-layout";
import {Button, Form, InputNumber, Popconfirm} from "antd";
import {PlusOutlined} from "@ant-design/icons";
import ExProTable from "@/components/Table/ExtProTable";
import {
  addCurrencyUsingPOST,
  auditingCurrencyUsingPUT,
  currencyByIdUsingGET,
  deleteCurrencyUsingDELETE,
  pageCurrencyUsingGET,
  unAuditingCurrencyUsingPUT,
  updateCurrencyUsingPUT
} from "@/services/swagger/currencyWeb";
import {useModalWithParam, useSecurity} from "@/utils/hooks";
import {ModalForm, ProFormItem, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import moment from "moment";
import ProFormDatePickerMonth from "@ant-design/pro-form/es/components/DatePicker/MonthPicker";
import {AuditStatus} from "@/constants";

const yearMonthNumTransform = v => ({
  yearMonthNum: moment(v).format("YYYYMM")
})

export default function CurrencyList() {
  const actionRef = useRef()
  const security = useSecurity("currency")
  const [formModal, handleFormModal, openFormModal] = useModalWithParam()
  const onSuccess = () => {
    actionRef.current?.reload()
  }
  const columns = [
    {
      title: "年-月", dataIndex: "yearMonthNum", search: {transform: yearMonthNumTransform},
      valueType: "dateMonth", fieldProps: {defaultValue:moment()}
    },
    {
      title: "货币编号", dataIndex: "number", search: false
    },
    {
      title: "货币名称", dataIndex: "name", search: false
    },
    {
      title: "费率", dataIndex: "rate", search: false
    },
    {
      title: "备注", dataIndex: "remark", search: false
    },
    ...(security.canOperating || security.canOperating? [{
      title: '操作', dataIndex: 'id',
      width: 200, valueType: 'option',
      render: (dom, row) => [
        ...security.canOperating && row.auditStatus === AuditStatus.TO_BE_AUDITED ? [
          <a key="edit"
             onClick={() => openFormModal({id: row.id, mode: "edit"})}>
            编辑
          </a>,
          <Popconfirm key="delete" title="确认删除该凭证？"
                      onConfirm={() => deleteCurrencyUsingDELETE({id: row.id}).then(actionRef.current?.reload)}>
            <a>删除</a>
          </Popconfirm>
        ] : [],
        security.canAuditing && (
          row.auditStatus === AuditStatus.TO_BE_AUDITED ? (
            security.canAuditing && (
              <Popconfirm key="auditing" title="确认审核该凭证？"
                          onConfirm={() => auditingCurrencyUsingPUT({id: row.id}).then(actionRef.current?.reload)}>
                <a>审核</a>
              </Popconfirm>
            )
          ) : (
            security.canUnAuditing && (
              <Popconfirm key="unAuditing" title="确认弃审该凭证？"
                          onConfirm={() => unAuditingCurrencyUsingPUT({id: row.id}).then(actionRef.current?.reload)}>
                <a>弃审</a>
              </Popconfirm>
            )
          )
        ),
      ]
    }] : [])
  ]
  return (
    <PageContainer>
      <ExProTable actionRef={actionRef} columns={columns}
                  request={(params) => {
                    const {yearMonthNum = moment().format("YYYYMM")} = params
                    return pageCurrencyUsingGET({
                      ...params, yearMonthNum
                    })
                  }}
                  toolBarRender={() => security.canOperating && (
                    <Button type="primary" onClick={openFormModal}>
                      <PlusOutlined/>
                      新增外币
                    </Button>
                  )}
                  editable={false}
      />
      <AddOrUpdateModal onVisibleChange={handleFormModal}
                        modal={formModal} onSuccess={onSuccess}
      />
    </PageContainer>
  )
}

function AddOrUpdateModal({modal, onSuccess, ...props}) {
  const {mode = "add", visible, id} = modal
  if (!visible) {
    return null
  }
  const isAddMode = mode === "add", isEditMode = mode === "edit"
  const title = isAddMode ? "新增货币" : isEditMode ? "编辑货币信息" : "货币详情"
  const [form] = Form.useForm()
  useEffect(() => {
    if (isEditMode) {
      currencyByIdUsingGET({id}).then(({data}) => form.setFieldsValue(data))
    }
  }, [])
  return (
    <ModalForm title={title} width="500px" initialValues={{role: "NORMAL"}}
               layout={"horizontal"} form={form}
               visible={visible} modalProps={{destroyOnClose: true}}
               onFinish={async (values) => {
                 if (isAddMode) {
                   addCurrencyUsingPOST(values).then(_ => onSuccess && onSuccess())
                   return true
                 }
                 updateCurrencyUsingPUT({...values, id}).then(_ => onSuccess && onSuccess())
               }} {...props}
    >
      <ProFormDatePickerMonth name="yearMonthNum" label="年-月" format="yyyy-MM"
                              rules={[{required: true, message: "请选择月份！"}]}
                              transform={yearMonthNumTransform}
                              fieldProps={{
                                disabledDate: current => {
                                  return current && current < moment();
                                }
                              }}

      />
      <ProFormText name="number" label="编号" rules={[{required: true, message: "请填写编号！"}]}/>
      <ProFormText name="name" label="名称" rules={[{required: true, message: "请填写名称！"}]}/>
      <ProFormItem name="rate" label="费率" rules={[{required: true, message: "请填写费率！"}]}>
        <InputNumber placeholder="费率" min={0} width={200}/>
      </ProFormItem>
      <ProFormTextArea name="remark" label="备注" fieldProps={{showCount: true, maxLength: 255}}/>
    </ModalForm>
  )
}
