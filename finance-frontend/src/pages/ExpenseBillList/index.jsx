import React, {useRef} from "react"
import PageContainer from "@/components/PageContainer"
import ExProTable from "@/components/Table/ExtProTable"
import {
  auditingExpenseBillUsingPUT,
  deleteExpenseBillUsingDELETE,
  pageExpenseBillUsingGET,
  unAuditingExpenseBillUsingPUT
} from "@/services/swagger/expenseBillWeb"
import BillForm from "@/pages/ExpenseBillList/BillForm"
import {Button, Popconfirm} from "antd"
import {PlusOutlined} from "@ant-design/icons"
import {useModalWithParam, usePrint, useSecurity} from "@/utils/hooks"
import BillPrint from "@/pages/ExpenseBillList/BillPrint";
import {AuditStatus} from "@/constants";

export default () => {
  const actionRef = useRef()
  const security = useSecurity("expenseBill")
  const addModal = useModalWithParam()
  const editModal = useModalWithParam()
  const viewModal = useModalWithParam()
  const [print, onPrint] = usePrint()
  const columns = [
    {
      title: "报销单号", dataIndex: "number"
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
        <a key="detail" onClick={() => viewModal.open({billId: row.id})}>详情</a>,
        ...(
          row.auditStatus === AuditStatus.TO_BE_AUDITED && security.canOperating? [
            <a key="edit" onClick={() => editModal.open({billId: row.id})}>编辑</a>,
            <Popconfirm key="delete" title="确认删除该报销单？"
                        onConfirm={() => deleteExpenseBillUsingDELETE({id: row.id}).then(actionRef.current?.reload)}>
              <a>删除</a>
            </Popconfirm>
          ] : []
        ),
        security.canPrint && (
          <a key="print" onClick={() => onPrint({billId: row.id})}>打印</a>
        ),
        row.auditStatus === AuditStatus.TO_BE_AUDITED ? (
          security.canAuditing && (
            <Popconfirm key="auditing" title="确认审核该报销单？"
                        onConfirm={() => auditingExpenseBillUsingPUT({id: row.id}).then(actionRef.current?.reload)}>
              <a>审核</a>
            </Popconfirm>
          )
        ) : (
          security.canUnAuditing && (
            <Popconfirm key="unAuditing" title="确认弃审该凭证？"
                        onConfirm={() => unAuditingExpenseBillUsingPUT({id: row.id}).then(actionRef.current?.reload)}>
              <a>弃审</a>
            </Popconfirm>
          )
        )
      ]
    },
  ]
  const reload = () => {
    actionRef.current?.reload()
    return true
  }
  return (
    <PageContainer>
      <ExProTable actionRef={actionRef} columns={columns}
                  request={pageExpenseBillUsingGET}
                  toolBarRender={() => security.canOperating && (
                    <Button type="primary" onClick={() => addModal.open()}>
                      <PlusOutlined/>
                      新增报销单
                    </Button>
                  )}
                  editable={false}
      />
      <BillPrint print={print}/>
      {addModal.visible &&
        <BillForm visible={true} onVisibleChange={addModal.handleVisible}
                  onOk={reload}/>
      }
      {editModal.visible &&
        <BillForm visible={true} mode="edit" onVisibleChange={editModal.handleVisible}
                  billId={editModal.state.billId} onOk={reload}/>
      }
      {viewModal.visible &&
        <BillForm visible={true} mode="view" onVisibleChange={viewModal.handleVisible} billId={viewModal.state.billId}/>
      }
    </PageContainer>
  )
}
