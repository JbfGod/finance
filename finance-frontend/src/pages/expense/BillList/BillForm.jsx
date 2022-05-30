import React, {useEffect, useRef, useState} from "react";
import {DrawerForm, ProFormDatePicker, ProFormGroup, ProFormItem, ProFormList, ProFormText} from "@ant-design/pro-form";
import {Button, Card, Col, Form, InputNumber, Row, Space, Upload} from "antd";
import {SyncOutlined, UploadOutlined} from "@ant-design/icons";
import styles from "./index.less"
import {
  addExpenseBillUsingPOST,
  expenseBillByIdUsingGET,
  getBillNumberUsingGET,
  searchExpenseBillCueUsingGET,
  searchExpenseItemCueUsingGET,
  updateExpenseBillUsingPUT
} from "@/services/swagger/expenseBillWeb";
import {treeSubjectUsingGET} from "@/services/swagger/subjectWeb";
import ExtTreeSelect from "@/components/Common/ExtTreeSelect";
import {IMG_ACCEPT, SUBJECT_TYPE} from "@/constants";
import {jsonToFormData} from "@/utils/common";
import AutoCompleteInput from "@/components/Common/AutoCompleteInput";

export default ({mode = "add", billId, visible, onVisibleChange}) => {
  const isAddMode = mode === "add"
  const isViewMode = mode === "view"
  const isEditMode = mode === "edit"
  const [formRef] = Form.useForm()
  const attachmentsRef = useRef()
  const [selectedRowIndex, setSelectedRowIndex] = useState(0)
  const [subjects, setSubjects] = useState([])
  const [billDetail, setBillDetail] = useState({})
  const loadSubjects = () => {
    treeSubjectUsingGET().then(({data}) => {
      setSubjects(data)
    })
  }
  const reloadNumber = () => {
    getBillNumberUsingGET().then(({data}) => {
      formRef.setFieldsValue({number: data})
    })
  }
  const initialBillDetail = () => {
    expenseBillByIdUsingGET({id: billId})
      .then(({data}) => {
        setBillDetail(data)
        formRef.setFieldsValue(data)
      })
  }
  const fillDeleteIdsToFormData = (formValues, formData) => {
    const {items} = billDetail
    const {items: currItems} = formValues
    const deletedItemIds = items.map(item => item.id).filter(itemId =>
      currItems.findIndex(tmp => tmp.id === itemId) === -1
    )
    currItems.filter(item => item.id).forEach(item => {
      const {subsidies, attachments} = items.find(tmp => tmp.id === item.id)
      const {subsidies: currSubsidies, attachments: currAttachments} = item
      const deletedSubsidyIds = subsidies.map(subsidy => subsidy.id).filter(id =>
        currSubsidies.findIndex(tmp => tmp.id === id) === -1
      )
      formData.append("deletedSubsidyIds", deletedSubsidyIds)
      const deletedAttachmentIds = attachments.map(attachment => attachment.id).filter(id =>
        currAttachments.findIndex(tmp => tmp.id === id) === -1
      )
      formData.append("deletedAttachmentIds", deletedAttachmentIds)
    })
    formData.append("deletedItemIds", deletedItemIds)

  }
  useEffect(() => {
    loadSubjects()    // 加载科目数据
    if (mode === "add") {
      reloadNumber()    // 初始化报销单号
      return
    }
    if (billId != null) {
      initialBillDetail()
    }
  }, [])
  return (
    <DrawerForm width="100%" form={formRef}
                title={`${isAddMode ? "添加" : isEditMode ? "编辑" : "预览"}费用报销单`}
                onFinish={async (f,) => {
                  if (isViewMode) {
                    return true
                  }
                  const formData = jsonToFormData(f, {
                    "subject$": (key, value) => {
                      return {
                        subjectId: value.value,
                        name: value.label
                      }
                    }
                  })
                  if (isAddMode) {
                    return addExpenseBillUsingPOST({}, null, {
                      body: formData
                    })
                  }
                  formData.append("id", billId)
                  fillDeleteIdsToFormData(f, formData)
                  return updateExpenseBillUsingPUT({}, null, {
                    body: formData
                  })
                }}
                visible={visible}
                onVisibleChange={onVisibleChange}
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
          <Card title="费用报销单" style={{width: 1100}}
                extra={(
                  <Space align="center">
                    <span style={{width: 60}}>报销单号:</span>
                    <ProFormText noStyle name="number" placeholder="报销单号"
                                 disabled={isViewMode || isEditMode}
                                 fieldProps={{
                                   addonAfter: isAddMode ? (
                                     <span style={{cursor: "pointer"}} onClick={reloadNumber}><SyncOutlined/></span>
                                   ) : null
                                 }}/>
                  </Space>
                )}
          >
            <ProFormGroup size={8}>
              <ProFormText name="expensePerson" label="报销人" placeholder="报销人" width={125} disabled={isViewMode}/>
              <ProFormText name="position" label="职位" placeholder="职位" width={125} disabled={isViewMode}/>
              <ProFormDatePicker name="expenseTime" label="报销日期" placeholder="开始时间" width={120} disabled={isViewMode}/>
              <ProFormItem name="totalSubsidyAmount" label="合计">
                <InputNumber placeholder="合计" min={0} disabled={isViewMode}/>
              </ProFormItem>
              <ProFormItem name="reason" label="报销事由" style={{width: 500}}>
                <AutoCompleteInput placeholder="报销事由" disabled={isViewMode} request={(keyword) => {
                  return searchExpenseBillCueUsingGET({column: 'REASON', keyword})
                }}/>
              </ProFormItem>
            </ProFormGroup>
            <ProFormList name="items" min={1}
                         creatorButtonProps={{
                           type: 'primary',
                           disabled: isViewMode,
                           creatorButtonText: '添加报销记录',
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
                  <>
                    <ProFormGroup size={8}>
                      <ProFormItem name="subject" label="费用名称" style={{width: 200}}>
                        <ExtTreeSelect options={subjects} placeholder="只能选择费用类科目"
                                       onlySelectedLeaf={true} disabled={isViewMode}
                                       disableFilter={node => node.type === SUBJECT_TYPE.SUBJECT.value}
                                       labelInValue={true} style={{width: '100%'}}/>
                      </ProFormItem>
                      <ProFormItem name="summary" label="摘要" style={{width: 300}}>
                        <AutoCompleteInput placeholder="摘要" disabled={isViewMode} request={(keyword) => {
                          return searchExpenseItemCueUsingGET({column: 'SUMMARY', keyword})
                        }}/>
                      </ProFormItem>
                      <ProFormItem name="remark" label="备注" style={{width: 300}}>
                        <AutoCompleteInput placeholder="备注" disabled={isViewMode} request={(keyword) => {
                          return searchExpenseItemCueUsingGET({column: 'REMARK', keyword})
                        }}/>
                      </ProFormItem>
                    </ProFormGroup>
                    <ProFormGroup size={8}>
                      <ProFormDatePicker name="beginTime" label="开始时间" placeholder="开始时间" width={120}
                                         disabled={isViewMode}/>
                      <ProFormDatePicker name="endTime" label="结束时间" placeholder="结束时间" width={120}
                                         disabled={isViewMode}/>
                      <ProFormText name="travelPlace" label="出差起讫地点" placeholder="出差起讫地点" width={200}
                                   disabled={isViewMode}/>
                      <ProFormItem name="numOfBill" label="票据张数">
                        <InputNumber placeholder="票据张数" min={0} disabled={isViewMode}/>
                      </ProFormItem>
                      <ProFormItem name="billAmount" label="票据金额">
                        <InputNumber placeholder="票据金额" min={0} disabled={isViewMode}/>
                      </ProFormItem>
                      <ProFormItem name="actualAmount" label="实报金额">
                        <InputNumber placeholder="实报金额" min={0} disabled={isViewMode}/>
                      </ProFormItem>
                      <ProFormItem name="subsidyAmount" label="补助费用金额">
                        <InputNumber placeholder="补助金额" min={0} disabled={isViewMode}/>
                      </ProFormItem>
                      <ProFormItem name="subtotalAmount" label="小计金额">
                        <InputNumber placeholder="小计金额" min={0} disabled={isViewMode}/>
                      </ProFormItem>
                    </ProFormGroup>
                  </>
                )
              }}
            </ProFormList>
          </Card>
        </Col>
        <Col md={4} sm={24}>
          <ProFormList name="items"
                       creatorButtonProps={false}
                       itemRender={({listDom, action}, {record, index}) => {
                         if (index === selectedRowIndex) {
                           return (
                             <Row align="bottom">
                               {listDom}
                             </Row>
                           )
                         }
                         return <></>
                       }}
          >
            {(f, index, action) => {
              if (index === selectedRowIndex) {
                return (
                  <>
                    <Col md={24} sm={12}>
                      <Card title="出差补助明细，对应每行报销情况">
                        <ProFormList name="subsidies" noStyle
                                     copyIconProps={!isViewMode}
                                     deleteIconProps={!isViewMode}
                                     creatorButtonProps={{
                                       type: 'primary',
                                       disabled: isViewMode,
                                       creatorButtonText: '添加补助明细',
                                     }}
                        >
                          <ProFormGroup size={8}>
                            <ProFormItem name="subject" label="补助费用名称" style={{width: 200}}>
                              <ExtTreeSelect options={subjects} placeholder="只能选择费用类科目"
                                             onlySelectedLeaf={true} disabled={isViewMode}
                                             disableFilter={node => node.type === SUBJECT_TYPE.SUBJECT.value}
                                             labelInValue={true} style={{width: '100%'}}/>
                            </ProFormItem>
                            <ProFormItem name="days" label="补助明细天数">
                              <InputNumber placeholder="补助明细天数" min={0} disabled={isViewMode}/>
                            </ProFormItem>
                            <ProFormItem name="amountForDay" label="元/天">
                              <InputNumber placeholder="元/天" min={0} disabled={isViewMode}/>
                            </ProFormItem>
                            <ProFormItem name="amount" label="补助金额">
                              <InputNumber placeholder="补助金额" min={0} disabled={isViewMode}/>
                            </ProFormItem>
                          </ProFormGroup>
                        </ProFormList>
                      </Card>
                    </Col>
                    <Col md={24} sm={12}>
                      <Card title="每行报销情况对应的票据图片">
                        <ProFormList name="attachments" noStyle actionRef={attachmentsRef}
                                     creatorButtonProps={false}
                                     itemRender={({listDom, action}, {record, index}) => {
                                       return <>
                                         {listDom}
                                       </>
                                     }}
                        >
                          {(f, index, action) => {
                            // const {url, name, file} = action.getCurrentRowData()
                            const currItem = formRef.getFieldValue(["items"])[selectedRowIndex]
                            const {url, file} = currItem.attachments?.[index]
                            return (
                              <ProFormGroup size={8} align="center">
                                <ProFormText name="name" label="票据名称" placeholder="票据名称" width={175}
                                             disabled={isViewMode}/>
                                <ProFormText name="remark" label="备注" placeholder="备注" disabled={isViewMode}/>
                                <a href={`/minio${url}`}>下载</a>
                                {!isViewMode && <a onClick={() => action.remove(index)}>删除</a>}
                              </ProFormGroup>
                            )
                          }}

                        </ProFormList>
                        <Upload showUploadList={false}
                                accept={IMG_ACCEPT}
                                beforeUpload={async (file) => {
                                  let {name} = file
                                  name = name.substring(0, name.lastIndexOf(".") || name.length)
                                  attachmentsRef.current?.add?.({name, file, url: URL.createObjectURL(file)}, 0)
                                  return false
                                }}
                        >
                          <Button block type="primary" disabled={isViewMode}>
                            <UploadOutlined/>添加票据
                          </Button>
                        </Upload>
                      </Card>
                    </Col>
                  </>
                )
              }
              return null;
            }}
          </ProFormList>
        </Col>
      </Row>
    </DrawerForm>
  )
}
