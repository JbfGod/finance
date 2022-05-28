import React, {useRef} from "react"
import PageContainer from "@/components/PageContainer"
import ExProTable from "@/components/Table/ExtProTable"
import {pageExpenseBillUsingGET} from "@/services/swagger/expenseBillWeb"
import BillForm from "@/pages/expense/BillList/BillForm"
import {Button} from "antd"
import {PlusOutlined} from "@ant-design/icons"
import {useModalWithParam, usePrint, useSecurity} from "@/utils/hooks"
import BillPrint from "@/pages/expense/BillList/BillPrint";

export default () => {
  const actionRef = useRef()
  const security = useSecurity()
  const [addModal, handleAddModal, openAddModal] = useModalWithParam()
  const [editModal, handleEditModal, openEditModal] = useModalWithParam()
  const [viewModal, handleViewModal, openViewModal] = useModalWithParam()
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
        <a key="detail" onClick={() => openViewModal({billId: row.id})}>详情</a>,
        <a key="print" onClick={() => onPrint({billId: row.id})}>打印</a>,
        ...(security.onlyRead ? [] : [
          <a key="edit" onClick={() => openEditModal({billId: row.id})}>编辑</a>,
          <a key="resetPwd">审批</a>
        ])
      ]
    },
  ]
  return (
    <PageContainer>
      <ExProTable actionRef={actionRef} columns={columns}
                  request={pageExpenseBillUsingGET}
                  toolBarRender={() => (
                    <Button type="primary" onClick={openAddModal}>
                      <PlusOutlined/>
                      新增报销单
                    </Button>
                  )}
                  editable={false}
      />
      <BillPrint print={print}/>
      {addModal.visible &&
        <BillForm visible={true} onVisibleChange={handleAddModal}/>
      }
      {editModal.visible &&
        <BillForm visible={true} mode="edit" onVisibleChange={handleEditModal} billId={editModal.billId}/>
      }
      {viewModal.visible &&
        <BillForm visible={true} mode="view" onVisibleChange={handleViewModal} billId={viewModal.billId}/>
      }
    </PageContainer>
  )
}
