import React, {useEffect, useMemo, useRef, useState} from "react"
import PageContainer from "@/components/PageContainer"
import ExProTable from "@/components/Table/ExtProTable"
import {Badge, Button, Empty, Popconfirm} from "antd"
import {PlusOutlined} from "@ant-design/icons"
import {useModalWithParam, usePrint, useSecurity} from "@/utils/hooks"
import {
  auditingVoucherUsingPUT,
  bookkeepingVoucherUsingPUT,
  deleteVoucherUsingDELETE,
  pageVoucherUsingGET,
  unAuditingVoucherUsingPUT,
  unBookkeepingVoucherUsingPUT
} from "@/services/swagger/voucherWeb";
import VoucherForm from "@/pages/VoucherList/VoucherForm";
import {AuditStatus, CURRENCY_TYPE} from "@/constants";
import VoucherPrint from "@/pages/VoucherList/VoucherPrint";
import moment from "moment";
import AutoDropdown from "@/components/Common/AutoDropdown";
import {flatArrayToMap, flatTreeToMap} from "@/utils/common";
import {treeSubjectUsingGET} from "@/services/swagger/subjectWeb";
import {currencyOfCurrentMonthUsingGET} from "@/services/swagger/currencyWeb";
import {history} from "umi";
import ProCard from "@ant-design/pro-card";

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

const yearMonthNumTransform = v => ({
  yearMonthNum: moment(v).format("YYYYMM")
})

export default () => {
  const actionRef = useRef()
  const security = useSecurity("voucher")
  const [formModal, handleFormModal, openFormModal] = useModalWithParam()
  const [print, onPrint] = usePrint()
  const [currencyType, setCurrencyType] = useState(CURRENCY_TYPE.LOCAL)

  const [subjects, setSubjects] = useState([])
  const subjectById = useMemo(() => flatTreeToMap(subjects), [subjects])

  const loadSubjects = () => {
    treeSubjectUsingGET().then(({data}) => {
      setSubjects(data)
    })
  }

  useEffect(() => {
    loadSubjects()    // 加载科目数据
  }, [])

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
      title: "年-月", dataIndex: "yearMonthNum", search: {transform: yearMonthNumTransform},
      valueType: "dateMonth", fieldProps: {defaultValue: moment()}, hideInTable: true
    },
    {
      title: "凭证单号", dataIndex: "serialNumber", search: false
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
      width: 200, valueType: 'option',
      render: (dom, row) => (
        <AutoDropdown overlay={[
          <a key="detail"
             onClick={() => openFormModal({mode: "view", voucherId: row.id, currentType: row.currentType})}>
            详情
          </a>,
          ...security.canOperating && row.auditStatus === AuditStatus.TO_BE_AUDITED ? [
            <a key="edit"
               onClick={() => openFormModal({mode: "edit", currencyType: row.currencyType, voucherId: row.id})}>
              编辑
            </a>,
            <Popconfirm key="delete" title="确认删除该凭证？"
                        onConfirm={() => deleteVoucherUsingDELETE({id: row.id}).then(actionRef.current?.reload)}>
              <a>删除</a>
            </Popconfirm>
          ] : [],
          security.canPrint && (
            <a key="print" onClick={() => onPrint({voucherId: row.id, currencyType: row.currencyType})}>打印</a>
          ),
          row.auditStatus === AuditStatus.AUDITED && (
            row.bookkeeping ? (
              security.canUnBookkeeping && (
                <Popconfirm key="bookkeeping" title="确认反记账该凭证？"
                            onConfirm={() => unBookkeepingVoucherUsingPUT({id: row.id}).then(actionRef.current?.reload)}>
                  <a>反记账</a>
                </Popconfirm>
              )
            ) : (
              security.canBookkeeping && (
                <Popconfirm key="unBookkeeping" title="确认记账该凭证？"
                            onConfirm={() => bookkeepingVoucherUsingPUT({id: row.id}).then(actionRef.current?.reload)}>
                  <a>记账</a>
                </Popconfirm>
              )
            )
          ),
          security.canAuditing && (
            row.auditStatus === AuditStatus.TO_BE_AUDITED ? (
              security.canAuditing && (
                <Popconfirm key="auditing" title="确认审核该凭证？"
                            onConfirm={() => auditingVoucherUsingPUT({id: row.id}).then(actionRef.current?.reload)}>
                  <a>审核</a>
                </Popconfirm>
              )
            ) : (
              security.canUnAuditing && (
                <Popconfirm key="unAuditing" title="确认弃审该凭证？"
                            onConfirm={() => unAuditingVoucherUsingPUT({id: row.id}).then(actionRef.current?.reload)}>
                  <a>弃审</a>
                </Popconfirm>
              )
            )
          ),
        ]}/>
      )
    },
  ]
  return (
    <PageContainer>
      <ExProTable actionRef={actionRef} columns={columns}
                  toolbar={toolbar} params={{currencyType}}
                  request={(params) => {
                    const {yearMonthNum = moment().format("YYYYMM")} = params
                    return pageVoucherUsingGET({
                      ...params, yearMonthNum
                    })
                  }}
                  toolBarRender={() => security.canOperating && (
                    <Button type="primary" onClick={() => openFormModal({mode: "add", currencyType})}>
                      <PlusOutlined/>
                      新增凭证单
                    </Button>
                  )}
                  editable={false}
      />
      <VoucherPrint print={print}/>
      <VoucherForm modal={formModal} onSuccess={actionRef.current?.reload}
                   subjects={subjects} subjectById={subjectById}
                   onVisibleChange={handleFormModal}/>
    </PageContainer>
  )
}