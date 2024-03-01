import React, {useRef, useState} from "react"
import GlobalPageContainer from "@/components/PageContainer";
import ExProTable from "@/components/Table/ExtProTable";
import {pageCanApprovedExpenseBillUsingGET} from "@/services/swagger/expenseBillWeb";
import {useModalWithParam} from "@/utils/hooks";
import BillForm from "@/pages/ExpenseBillList/BillForm";
import * as customerWeb from "@/services/swagger/customerWeb";
import {Space} from "antd";

export default function () {
    const modal = useModalWithParam()
    const actionRef = useRef()
    const [customerId, setCustomerId] = useState(null)
    const [auditStatus, setAuditStatus] = useState('AUDITED')
    const isAudited = auditStatus === "AUDITED"
    const columns = [
        {
            title: "报销单号", dataIndex: "number"
        },
        {
            title: "报销时间", dataIndex: "expenseRangeTime", hideInTable: true,
            search: {
                transform: (v) => ({
                    startDate: v?.[0],
                    endDate: v?.[1]
                })
            },
            valueType: "dateRange",
        },
        {
            title: "报销人", dataIndex: "expensePerson", search: false
        },
        {
            title: "职别", dataIndex: "position", search: false
        },
        {
            title: "报销时间", dataIndex: "expenseTime", search: false
        },
        {
            title: "填报时间", dataIndex: "createTime", search: false
        },
        {
            title: '操作', dataIndex: 'id',
            width: 255, valueType: 'option',
            render: (dom, row) => [
                <a key="approval" onClick={() => modal.open({billId: row.id})}>{isAudited ? "审批" : "详情"}</a>
            ]
        },
    ]
    const toolbar = {
        menu: {
            type: 'tab',
            activeKey: auditStatus,
            items: [
                {
                    key: 'AUDITED',
                    label: "待审批",
                },
                {
                    key: 'APPROVED',
                    label: "已审批",
                },
            ],
            onChange: (key) => {
                setAuditStatus(key)
            }
        }
    }
    return (
        <GlobalPageContainer>
            <Space align="start">
                <CustomerSelectTable customerId={customerId} setCustomerId={setCustomerId}/>
                {customerId && (
                    <ExProTable columns={columns} params={{customerId, auditStatus}}
                                actionRef={actionRef} toolbar={toolbar}
                                request={pageCanApprovedExpenseBillUsingGET}
                                editable={false}
                    />
                )}
            </Space>
            {modal.visible &&
                <BillForm visible={true} mode={isAudited ? "approval" : "view"}
                          onVisibleChange={modal.handleVisible}
                          billId={modal.state.billId}
                          onClose={() => {
                              modal.close()
                              actionRef.current?.reload()
                          }}/>
            }
        </GlobalPageContainer>
    )
}


function CustomerSelectTable({customerId, setCustomerId}) {
    const actionRef = useRef()
    const columns = [
        {
            title: "客户名称", dataIndex: "name",
            width: 125
        },
        {
            title: "客户编号", dataIndex: "number",
            width: 125
        },
    ]
    return (
        <ExProTable actionRef={actionRef} columns={columns} editable={false} toolBarRender={false}
                    style={{width: 320}}
                    tableAlertRender={false} pagination={false} search={false}
                    onRow={(record) => ({
                        onClick: () => setCustomerId(record.id)
                    })}
                    rowSelection={{
                        type: 'radio',
                        selectedRowKeys: customerId ? [customerId] : [],
                        onChange: (keys) => {
                            setCustomerId(keys[0])
                        }
                    }}
                    expandable={{expandRowByClick: true}}
                    request={async (params) => {
                        const result = await customerWeb.ownedApprovalCustomersUsingGET(params)
                        if (result.data?.[0]) {
                            setCustomerId(result.data?.[0].id)
                        }
                        return result;
                    }}
        />
    )
}
