import React, {useRef, useState} from "react"
import PageContainer from "@/components/PageContainer"
import ExProTable from "@/components/Table/ExtProTable"
import {Badge, Button} from "antd"
import {PlusOutlined} from "@ant-design/icons"
import {useModalWithParam, usePrint, useSecurity} from "@/utils/hooks"
import BillPrint from "@/pages/expense/BillList/BillPrint";
import {pageVoucherUsingGET} from "@/services/swagger/voucherWeb";
import VoucherForm from "@/pages/voucher/VoucherForm";
import {CURRENCY_TYPE} from "@/constants";

const renderBadge = (active = false) => {
  return (
    <Badge
      style={{
        marginTop: -2,
        marginLeft: 4,
        color: active ? '#1890FF' : '#999',
        backgroundColor: active ? '#E6F7FF' : '#eee',
      }}
    />
  )
}

export default () => {
  const actionRef = useRef()
  const security = useSecurity()
  const [formModal, handleFormModal, openFormModal] = useModalWithParam()
  const [print, onPrint] = usePrint()
  const [currencyType, setCurrencyType] = useState(CURRENCY_TYPE.LOCAL);
  const toolbar = {
    menu: {
      type: 'tab',
      activeKey: currencyType,
      items: [
        {
          key: CURRENCY_TYPE.LOCAL,
          label: <span>本币凭证{renderBadge(currencyType === CURRENCY_TYPE.LOCAL)}</span>,
        },
        {
          key: CURRENCY_TYPE.FOREIGN,
          label: <span>外币凭证{renderBadge(currencyType === CURRENCY_TYPE.FOREIGN)}</span>,
        }
      ],
      onChange: (key) => {
        setCurrencyType(key);
      },
    }
  }
  const columns = [
    {
      title: "凭证单号", dataIndex: "serialNumber"
    },
    {
      title: "凭证日期", dataIndex: "voucherTime", search: false
    },
    {
      title: "单位", dataIndex: "unit", search: false
    },
    {
      title: "附件张数", dataIndex: "attachmentNum", search: false
    },
    {
      title: "制单人", dataIndex: "creatorName", search: false
    },
    {
      title: '操作', dataIndex: 'id',
      width: 255, valueType: 'option',
      render: (dom, row) => [
        <a key="detail" onClick={() => openFormModal({mode: "view", voucherId: row.id, currentType: row.currentType})}>
          详情
        </a>,
        <a key="print" onClick={() => onPrint({voucherId: row.id})}>打印</a>,
        ...(security.onlyRead ? [] : [
          <a key="edit" onClick={() => openFormModal({mode: "edit", currencyType: row.currencyType, voucherId: row.id})}>
            编辑
          </a>,
          <a key="approve">审批</a>
        ])
      ]
    },
  ]
  return (
    <PageContainer>
      <ExProTable actionRef={actionRef} columns={columns}
                  toolbar={toolbar} params={{currencyType}}
                  request={pageVoucherUsingGET}
                  toolBarRender={() => (
                    <Button type="primary" onClick={() => openFormModal({mode: "add", currencyType})}>
                      <PlusOutlined/>
                      新增凭证单
                    </Button>
                  )}
                  editable={false}
      />
      <BillPrint print={print}/>
      <VoucherForm modal={formModal} onVisibleChange={handleFormModal}/>
    </PageContainer>
  )
}
