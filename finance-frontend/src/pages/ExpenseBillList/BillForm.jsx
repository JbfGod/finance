import React, {useEffect, useRef, useState} from "react";
import {DrawerForm, ProFormDatePicker, ProFormGroup, ProFormItem, ProFormText} from "@ant-design/pro-form";
import {Col, Form, Popconfirm, Row, Space, Tag} from "antd";
import {CheckCircleOutlined, SyncOutlined} from "@ant-design/icons";
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
import {
    approvalInstanceUsingGET,
    approvedUsingPUT,
    reviewRejectedUsingPUT
} from "@/services/swagger/approvalInstanceWeb";
import {EditableProTable} from "@ant-design/pro-table";

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

export default ({mode = "add", billId, visible, onVisibleChange, onClose}) => {
    const isAddMode = mode === "add", isViewMode = mode === "view", isEditMode = mode === "edit",  isApprovalMode = mode === "approval"
    const noCanEdit = isViewMode || isApprovalMode
    const {subjectById} = useModel("useSubjectModel")
    const [formRef] = Form.useForm()
    const [billDetail, setBillDetail] = useState({})
    const {approvalFlowInstanceId} = billDetail
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
                ...item, subjectNumber: subjectById[item.subjectId]?.number,
                subsidies: subsidies.filter(subsidy => subsidy.subjectId != null).map(subsidy => ({
                    ...subsidy,
                    subjectNumber: subjectById[subsidy.subjectId]?.number
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
                    title={`${isAddMode ? "添加" : isEditMode ? "编辑" : "查看"}费用报销单`}
                    onFinish={async (values,) => {
                        if (noCanEdit) {
                            return true
                        }
                        const validValues = cleanData(values)
                        const formData = jsonToFormData(validValues)
                        if (isAddMode) {
                            return addExpenseBillUsingPOST({}, null, {
                                body: formData
                            }).then(onClose)
                        }
                        formData.append("id", billId)
                        fillDeleteIdsToFormData(validValues, formData)
                        return updateExpenseBillUsingPUT({}, null, {
                            body: formData
                        }).then(onClose)
                    }}
                    visible={visible}
                    size="small"
                    onVisibleChange={onVisibleChange}
                    layout="horizontal"
                    drawerProps={{
                        destroyOnClose: true,
                        className: styles.billDrawer,
                        placement: "left"
                    }}
        >
            <Row>
                <Col span={24}>
                    <Space align="start">
                        <div style={{width: 1000}}>
                            <ProFormGroup size={8}>
                                <ProFormText name="expensePerson" label="报销人" width={90} disabled={noCanEdit}/>
                                <ProFormText name="position" label="职位" width={90} disabled={noCanEdit}/>
                                <ProFormDatePicker name="expenseTime" label="报销日期" width={120} disabled={noCanEdit}/>
                                <ProFormItem name="reason" label="事由" style={{width: 230}}>
                                    <AutoCompleteInput disabled={noCanEdit} request={(keyword) => {
                                        return searchExpenseBillCueUsingGET({column: 'REASON', keyword})
                                    }}/>
                                </ProFormItem>
                                <ProFormText label="单号" name="number" width={230} disabled={noCanEdit || isEditMode}
                                             fieldProps={{
                                                 addonAfter: isAddMode ? (
                                                     <span style={{cursor: "pointer"}}
                                                           onClick={initialAddForm}><SyncOutlined/></span>
                                                 ) : null
                                             }}/>
                            </ProFormGroup>
                            <ExpenseBillItemTable formRef={formRef} isViewMode={noCanEdit}
                                                  onEditRowChange={setEditableKeys}/>
                            {editableKeys[0] != null && (
                                <Row gutter={[8, 0]}>
                                    <Col span={12}>
                                        <ExpenseBillSubsidyTable formRef={formRef} itemIndex={editableKeys[0]}
                                                                 isViewMode={noCanEdit}/>
                                    </Col>
                                    <Col span={12}>
                                        <ExpenseBillAttachmentTable formRef={formRef} itemIndex={editableKeys[0]}
                                                                    isViewMode={noCanEdit}/>
                                    </Col>
                                </Row>
                            )}
                        </div>
                        <div style={{width: "calc(100% -1000px)"}}>
                            <div>
                                {approvalFlowInstanceId > 0 &&
                                    <ApprovalTable mode={mode} billId={billId} onClose={onClose}
                                                   approvalInstanceId={approvalFlowInstanceId}/>}
                            </div>
                        </div>
                    </Space>
                </Col>
            </Row>
        </DrawerForm>
    )
}

function ApprovalTable({mode, approvalInstanceId, billId, onClose}) {
    const commonRender = (dataIndex) => (_, row) => <span title={row[dataIndex] || ""}>{row[dataIndex] || ""}</span>
    const isViewMode = mode === "view", isApprovalMode = mode === "approval"
    const [approvalInstance, setApprovalInstance] = useState({})
    const actionRef = useRef()
    const {currentLevel, items = []} = approvalInstance
    const canEdit = !!items.find(item => item.canApproved)
    const columns = [
        {title: "审批级别", dataIndex: "level", width: 45, editable: false, render: commonRender("level")},
        {title: "审批人", dataIndex: "approver", editable: false, render: commonRender("approver")},
        {title: "审批时间", dataIndex: "approvalTime", editable: false, render: commonRender("approvalTime")},
        {title: "备注", dataIndex: "remark", render: commonRender("remark")},
        {
            title: "操作", dataIndex: "operate", editable: false,
            render: (_, row, index) => {
                if (row.passed) {
                    return (
                        <Tag icon={<CheckCircleOutlined/>} color="success">
                            已通过
                        </Tag>
                    )
                }
                if (!row.canApproved || isViewMode) {
                    return ""
                }
                return (
                    <Space size={12}>
                        <Popconfirm key="rejected" title={`确认驳回${row.level}级当前审批吗？`}
                                    onConfirm={() =>
                                        reviewRejectedUsingPUT({
                                            approvalInstanceItemId: row.id,
                                            moduleRecordId: billId,
                                            previousApprovalInstanceItemId: items[index - 1]?.id,
                                            remark: row.remark
                                        }).then(() => {
                                            if (row.level === 1) {
                                                onClose()
                                            }
                                            fetchApprovalInstance()
                                        })
                                    }>
                            <a>驳回</a>
                        </Popconfirm>
                        <Popconfirm key="pass" title={`确认通过${row.level}级当前审批吗？`}
                                    onConfirm={() =>
                                        approvedUsingPUT({
                                            approvalInstanceItemId: row.id,
                                            moduleRecordId: billId,
                                            remark: row.remark
                                        }).then(() => {
                                            if (row.lasted) {
                                                onClose()
                                            }
                                        })
                                    }>
                            <a>通过</a>
                        </Popconfirm>
                    </Space>
                )
            }
        }
    ]
    const editable = {
        type: "multiple",
        editableKeys: canEdit && isApprovalMode && currentLevel? [currentLevel] : [],
        onValuesChange: (record, recordList) => {
            setApprovalInstance({...approvalInstance, items: recordList})
        }
    }
    const fetchApprovalInstance = () => {
        approvalInstanceUsingGET({id: approvalInstanceId}).then(({data}) => {
            setApprovalInstance({...data})
        })
    }
    useEffect(() => {
        fetchApprovalInstance()
    }, [])
    return (
        <EditableProTable pagination={false} columns={columns} search={false}
                          recordCreatorProps={false} actionRef={actionRef} size="small" rowKey="level"
                          editable={editable}
                          value={items}/>
    )
}
