import React, {useEffect, useState} from "react";
import {DrawerForm, ProFormDatePicker, ProFormGroup, ProFormItem, ProFormText} from "@ant-design/pro-form";
import {Col, Form, InputNumber, Row} from "antd";
import {SyncOutlined} from "@ant-design/icons";
import styles from "./index.less"
import {
  addExpenseBillUsingPOST,
  expenseBillByIdUsingGET,
  getBillNumberUsingGET,
  searchExpenseBillCueUsingGET,
  updateExpenseBillUsingPUT
} from "@/services/swagger/expenseBillWeb";
import {jsonToFormData} from "@/utils/common";
import AutoCompleteInput from "@/components/Common/AutoCompleteInput";
import ExpenseBillItemTable from "@/pages/ExpenseBillList/tables/ExpenseBillItemTable";
import ExpenseBillSubsidyTable from "@/pages/ExpenseBillList/tables/ExpenseBillSubsidyTable";
import ExpenseBillAttachmentTable from "@/pages/ExpenseBillList/tables/ExpenseBillAttachmentTable";
import {useModel} from "@/.umi/plugin-model/useModel";

const CAPACITY = 7
const fillItemData = (items = []) => {
  for (let i = 0; i < CAPACITY; i++) {
    let item = items[i]
    items[i] = item ? {...item, index: i} : {index: i, subsidies: [], attachments: []}
    for (let j = 0; j < CAPACITY; j++) {
      const {subsidies, attachments} = items[i]
      const subsidy = subsidies[j], attachment = attachments[j]
      subsidies[j] = subsidy ? {...subsidy, index: j} : {index: j}
      attachments[j] = attachment ? {...attachment, index: j} : {index: j}
    }
  }
  return items
}

export default ({mode = "add", billId, visible, onVisibleChange, onOk}) => {
  const isAddMode = mode === "add", isViewMode = mode === "view", isEditMode = mode === "edit"
  const {subjectById} = useModel("useSubjectModel")
  const [formRef] = Form.useForm()
  const [billDetail, setBillDetail] = useState({})
  // 当前编辑的行
  const [editableKeys, setEditableKeys] = useState([])
  const initialAddForm = () => {
    getBillNumberUsingGET().then(({data}) => {
      formRef.setFieldsValue({number: data})
    })
    formRef.setFieldsValue({items: fillItemData()})
  }
  const initialBillDetail = () => {
    expenseBillByIdUsingGET({id: billId})
      .then(({data}) => {
        setBillDetail({...data, items: fillItemData(data?.items)})
        formRef.setFieldsValue(data)
      })
  }
  const fillDeleteIdsToFormData = (formValues, formData) => {
    const {items} = billDetail
    const {items: currItems} = formValues
    const subjectIdIsNotNull = obj => obj.subjectId != null
    const deletedItemIds = items.filter(subjectIdIsNotNull)
      .map(item => item.id).filter(itemId =>
        currItems.findIndex(tmp => tmp.id === itemId) === -1
      )
    currItems.filter(item => item.id).forEach(item => {
      const {subsidies, attachments} = items.find(tmp => tmp.id === item.id)
      const {subsidies: currSubsidies, attachments: currAttachments} = item
      const deletedSubsidyIds = subsidies.filter(subjectIdIsNotNull)
        .map(subsidy => subsidy.id).filter(id =>
          currSubsidies.findIndex(tmp => tmp.id === id) === -1
        )
      formData.append("deletedSubsidyIds", deletedSubsidyIds)
      const deletedAttachmentIds = attachments.filter(attachment => attachment.url != null)
        .map(attachment => attachment.id).filter(id =>
          currAttachments.findIndex(tmp => tmp.id === id) === -1
        )
      formData.append("deletedAttachmentIds", deletedAttachmentIds)
    })
    formData.append("deletedItemIds", deletedItemIds)
  }
  const cleanData = (values) => {
    const {items} = values
    const newItems = items.filter(item => item.subjectId != null).map(item => {
      const {subsidies, attachments} = item
      return {
        ...item, name: subjectById[item.subjectId]?.name,
        subsidies: subsidies.filter(subsidy => subsidy.subjectId != null).map(subsidy => ({
          ...subsidy,
          name: subjectById[subsidy.subjectId]?.name
        })),
        attachments: attachments.filter(attachment => attachment.url != null),
      }
    })
    return {...values, items: newItems}
  }
  useEffect(() => {
    if (mode === "add") {
      initialAddForm()    // 初始化报销单号
      return
    }
    if (billId != null) {
      initialBillDetail()
    }
  }, [])
  return (
    <DrawerForm width="100%" form={formRef}
                title={`${isAddMode ? "添加" : isEditMode ? "编辑" : "预览"}费用报销单`}
                onFinish={async (values,) => {
                  if (isViewMode) {
                    return true
                  }
                  const validValues = cleanData(values)
                  const formData = jsonToFormData(validValues)
                  if (isAddMode) {
                    return addExpenseBillUsingPOST({}, null, {
                      body: formData
                    }).then(onOk)
                  }
                  formData.append("id", billId)
                  fillDeleteIdsToFormData(validValues, formData)
                  return updateExpenseBillUsingPUT({}, null, {
                    body: formData
                  }).then(onOk)
                }}
                visible={visible}
                size="small"
                onVisibleChange={onVisibleChange}
                layout="horizontal"
                drawerProps={{
                  destroyOnClose: true,
                  /*extra: (
                    <Space>
                      <Button type="primary" onClick={formRef.submit}>
                        <PlusOutlined/>预览打印
                      </Button>
                    </Space>
                  ),*/
                  className: styles.billDrawer,
                  placement: "left"
                }}
    >
      <Row>
        <Col>
          <div style={{width: 1000}}>
            <ProFormGroup size={8}>
              <ProFormText name="expensePerson" label="报销人" width={125} disabled={isViewMode}/>
              <ProFormText name="position" label="职位" width={125} disabled={isViewMode}/>
              <ProFormItem name="reason" label="报销事由" style={{width: 450}}>
                <AutoCompleteInput disabled={isViewMode} request={(keyword) => {
                  return searchExpenseBillCueUsingGET({column: 'REASON', keyword})
                }}/>
              </ProFormItem>
              <ProFormText label="报销单号" name="number"
                           disabled={isViewMode || isEditMode}
                           fieldProps={{
                             addonAfter: isAddMode ? (
                               <span style={{cursor: "pointer"}} onClick={initialAddForm}><SyncOutlined/></span>
                             ) : null
                           }}/>
              <ProFormDatePicker name="expenseTime" label="报销日期" width={120} disabled={isViewMode}/>
              <ProFormItem name="totalSubsidyAmount" label="合计">
                <InputNumber min={0} disabled={isViewMode}/>
              </ProFormItem>
            </ProFormGroup>
            <ExpenseBillItemTable formRef={formRef} isViewMode={isViewMode}
                                  onEditRowChange={setEditableKeys}/>
          </div>
        </Col>
        <Col md={24}>
          {editableKeys[0] != null && (
            <Row>
              <Col span={12}>
                <ExpenseBillSubsidyTable formRef={formRef} itemIndex={editableKeys[0]} isViewMode={isViewMode}/>
              </Col>
              <Col span={12}>
                <ExpenseBillAttachmentTable formRef={formRef} itemIndex={editableKeys[0]} isViewMode={isViewMode}/>
              </Col>
            </Row>
          )}
        </Col>
      </Row>
    </DrawerForm>
  )
}
