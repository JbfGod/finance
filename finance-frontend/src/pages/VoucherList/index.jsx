import React, {useEffect, useMemo, useRef, useState} from "react"
import PageContainer from "@/components/PageContainer"
import ExProTable from "@/components/Table/ExtProTable"
import {Badge, Button, Card, Empty, Popconfirm} from "antd"
import {PlusOutlined} from "@ant-design/icons"
import {useModalWithParam, usePrint, useSecurity} from "@/utils/hooks"
import {
  auditingVoucherUsingPUT,
  bookkeepingVoucherUsingPUT, defaultVoucherDateUsingGET,
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
import {flatTreeToMap} from "@/utils/common";
import {treeSubjectUsingGET} from "@/services/swagger/subjectWeb";
import {initialBalanceOutlineUsingGET} from "@/services/swagger/initialBalanceWeb";
import {history} from 'umi';

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
  const security = useSecurity("voucher")
  const [defaultVoucherDate, setDefaultVoucherDate] = useState()
  const [initialBalance, setInitialBalance] = useState({})
  const {bookkeeping} = initialBalance
  const formModal = useModalWithParam()
  const [print, onPrint] = usePrint()
  const [currencyType, setCurrencyType] = useState(CURRENCY_TYPE.LOCAL)

  const [subjects, setSubjects] = useState([])
  const subjectById = useMemo(() => flatTreeToMap(subjects), [subjects])

  const loadSubjects = () => {
    treeSubjectUsingGET().then(({data}) => {
      setSubjects(data)
    })
  }
  const fetchInitialBalanceOutline = () => {
    initialBalanceOutlineUsingGET().then(({data}) => data && setInitialBalance({...data, yearMonthDate: moment(data.yearMonthDate, "YYYY-MM")}))
  }
  const getDefaultVoucherDate = () => {
    defaultVoucherDateUsingGET().then(({data}) => setDefaultVoucherDate(data))
  }

  useEffect(() => {
    loadSubjects()
    fetchInitialBalanceOutline()
    getDefaultVoucherDate()
  }, [])
  if (!bookkeeping) {
    return (
      <Card>
        <Empty description="初始余额未关账，请先创建初始余额且关账！">
          <Button type="primary" onClick={() => history.push('/base/initialBalance')}>去创建初始余额</Button>
        </Empty>
      </Card>
    )
  }
  const toolbar = {
    menu: {
      type: 'tab',
      activeKey: currencyType,
      items: [
        {
          key: CURRENCY_TYPE.LOCAL,
          label: <span>本币凭证{renderBadge(currencyType === CURRENCY_TYPE.LOCAL)}</span>,
        },
        ...(security.canAddFeign ? [
          {
            key: CURRENCY_TYPE.FOREIGN,
            label: <span>外币凭证{renderBadge(currencyType === CURRENCY_TYPE.FOREIGN)}</span>,
          }
        ] : [])
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
      title: "日期范围", dataIndex: "dateRange",
      search: {
        transform: (v) => ({
          startDate: v?.[0],
          endDate: v?.[1]
        })
      },
      valueType: "dateRange", hideInTable: true
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
             onClick={() => formModal.open({mode: "view", voucherId: row.id, currentType: row.currentType})}>
            详情
          </a>,
          ...security.canOperating && row.auditStatus === AuditStatus.TO_BE_AUDITED ? [
            <a key="edit"
               onClick={() => formModal.open({mode: "edit", currencyType: row.currencyType, voucherId: row.id})}>
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
          !row.bookkeeping && (
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
            )
          )
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
                    <Button type="primary" onClick={() => formModal.open({mode: "add", currencyType})}>
                      <PlusOutlined/>
                      新增凭证单
                    </Button>
                  )}
                  editable={false}
      />
      <VoucherPrint print={print}/>
      <VoucherForm modal={formModal} onSuccess={actionRef.current?.reload}
                   defaultVoucherDate={defaultVoucherDate && moment(defaultVoucherDate, "YYYY-MM-DD") || moment()}
                   subjects={subjects} setSubjects={setSubjects} subjectById={subjectById}/>
    </PageContainer>
  )
}
