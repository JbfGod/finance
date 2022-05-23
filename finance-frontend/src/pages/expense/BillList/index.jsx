import React, {useRef} from "react"
import PageContainer from "@/components/PageContainer"
import ExProTable from "@/components/Table/ExtProTable"
import {pageExpenseBillUsingGET} from "@/services/swagger/expenseBillWeb"
import BillForm from "@/pages/expense/BillList/BillForm"
import {Button} from "antd"
import {PlusOutlined} from "@ant-design/icons"
import {useModalWithParam} from "@/utils/hooks"
import BillPreviewTableModal from "@/pages/expense/BillList/BillPreviewTableModal";

export default () => {
  const actionRef = useRef()
  const [addModal, handleAddModal, openAddModal] = useModalWithParam()
  const [editModal, handleEditModal, openEditModal] = useModalWithParam()
  const [previewModal, handlePreviewModal, openPreviewModal] = useModalWithParam()

  const columns = [
    {
      title: "报销单号", dataIndex: "number"
    },
    {
      title: "报销人", dataIndex: "expensePerson"
    },
    {
      title: "职别", dataIndex: "position"
    },
    {
      title: "报销时间", dataIndex: "expenseTime"
    },
    {
      title: "填报时间", dataIndex: "createTime"
    },
    {
      title: '操作', dataIndex: 'id',
      width: 255, valueType: 'option',
      render: (dom, row, index, action) => {
        return [
          <a key="preview" onClick={() => openPreviewModal({billId: row.id})}>预览</a>,
          <a key="edit" onClick={() => openEditModal({billId: row.id})}>编辑</a>,
          <a key="resetPwd">审批</a>
        ]
      }
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
      <BillPreviewTableModal visible={previewModal.visible} billId={previewModal.billId}
                             onCancel={() => handlePreviewModal(false)}/>
      {addModal.visible &&
        <BillForm visible={true} onVisibleChange={handleAddModal}/>
      }
      {editModal.visible &&
        <BillForm visible={true} mode="edit" onVisibleChange={handleEditModal} billId={editModal.billId}/>
      }
    </PageContainer>
  )
}
