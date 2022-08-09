import React, {useEffect, useMemo, useRef, useState} from "react"
import PageContainer from "@/components/PageContainer"
import ExProTable from "@/components/Table/ExtProTable"
import {Button, DatePicker, message, Popconfirm} from "antd"
import {PlusOutlined} from "@ant-design/icons"
import {useModalWithParam, useSecurity} from "@/utils/hooks"
import {AuditStatus, LENDING_DIRECTION} from "@/constants";
import moment from "moment";
import AutoDropdown from "@/components/Common/AutoDropdown";
import {flatTreeToMap} from "@/utils/common";
import {treeSubjectUsingGET} from "@/services/swagger/subjectWeb";
import {
  addInitialBalanceItemUsingPOST,
  auditingInitialBalanceUsingPUT,
  bookkeepingInitialBalanceUsingPUT,
  deleteInitialBalanceUsingDELETE,
  initialBalanceOutlineUsingGET,
  pageInitialBalanceItemUsingGET,
  unAuditingInitialBalanceUsingPUT,
  unBookkeepingInitialBalanceUsingPUT,
  updateInitialBalanceUsingPUT
} from "@/services/swagger/initialBalanceWeb";
import {ModalForm, ProFormItem, ProFormSelect, ProFormText} from "@ant-design/pro-form";
import {AdvancedSubjectSelect} from "@/components/AdvancedSubjectSelect";
import {InputNumber} from "antd/es";

export default () => {
  const actionRef = useRef()
  const security = useSecurity("initialBalance")
  const [initialBalance, setInitialBalance] = useState({
    yearMonthDate: null, auditStatus: AuditStatus.TO_BE_AUDITED, bookkeeping: false
  })
  const {id, yearMonthDate, auditStatus, bookkeeping} = initialBalance
  const setYearMonthDate = (v) => {
    setInitialBalance({...initialBalance, yearMonthDate: v})
  }
  const [formModal, handleFormModal, openFormModal] = useModalWithParam()

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

  useEffect(() => {
    loadSubjects()
    fetchInitialBalanceOutline()
  }, [])

  const columns = [
    {
      title: "科目编号", dataIndex: "subjectNumber", search: false
    },
    {
      title: "科目名称", dataIndex: "subjectName", search: false
    },
    {
      title: "科目方向", dataIndex: "lendingDirection", search: false
    },
    {
      title: "币别", dataIndex: "currencyName", search: false
    },
    {
      title: "金额", dataIndex: "amount", search: false
    },
    ...(security.canOperating && auditStatus === AuditStatus.TO_BE_AUDITED ? [{
      title: '操作', dataIndex: 'id',
      width: 200, valueType: 'option',
      render: (dom, row) => (
        <AutoDropdown overlay={[
          <a key="edit"
             onClick={() => openFormModal({mode: "edit", voucherId: row.id})}>
            编辑
          </a>,
          <Popconfirm key="delete" title="确认删除该记录？"
                      onConfirm={() => deleteInitialBalanceUsingDELETE({id: row.id}).then(actionRef.current?.reload)}>
            <a>删除</a>
          </Popconfirm>
        ]}/>
      )
    }] : [])
  ]
  return (
    <PageContainer>
      <ExProTable actionRef={actionRef} columns={columns} search={false}
                  request={pageInitialBalanceItemUsingGET}
                  toolBarRender={() => security.canOperating && ([
                    <Button type="primary" onClick={() => {
                      if (yearMonthDate == null) {
                        return message.warn("请先选择月份！")
                      } else if (bookkeeping) {
                        return message.warn("初始余额已记账！")
                      } else if (auditStatus === AuditStatus.AUDITED) {
                        return message.warn("初始余额已审核！")
                      }
                      openFormModal({mode: "add"})
                    }}>
                      <PlusOutlined/>
                      新增
                    </Button>,
                    auditStatus === AuditStatus.AUDITED && (
                      bookkeeping ? (
                        security.canUnBookkeeping && (
                          <Popconfirm key="bookkeeping" title="确认反记账？"
                                      onConfirm={() => unBookkeepingInitialBalanceUsingPUT({id}).then(fetchInitialBalanceOutline)}>
                            <Button>反记账</Button>
                          </Popconfirm>
                        )
                      ) : (
                        security.canBookkeeping && (
                          <Popconfirm key="unBookkeeping" title="确认记账？"
                                      onConfirm={() => bookkeepingInitialBalanceUsingPUT({id}).then(fetchInitialBalanceOutline)}>
                            <Button>记账</Button>
                          </Popconfirm>
                        )
                      )
                    ),
                    !bookkeeping && (
                      security.canAuditing && (
                        auditStatus === AuditStatus.TO_BE_AUDITED ? (
                          security.canAuditing && (
                            <Popconfirm key="auditing" title="确认审核？"
                                        onConfirm={() => {
                                          auditingInitialBalanceUsingPUT({yearMonthDate: yearMonthDate?.format("YYYY-MM")})
                                            .then(fetchInitialBalanceOutline)
                                        }}>
                              <Button>审核</Button>
                            </Popconfirm>
                          )
                        ) : (
                          security.canUnAuditing && (
                            <Popconfirm key="unAuditing" title="确认弃审？"
                                        onConfirm={() => unAuditingInitialBalanceUsingPUT({id}).then(fetchInitialBalanceOutline)}>
                              <Button>弃审</Button>
                            </Popconfirm>
                          )
                        )
                      )
                    )
                  ])}
                  editable={false}
                  headerTitle={(
                    <div style={{display: "flex", alignItems: "center"}}>
                      <div>月份：</div>
                      <DatePicker value={yearMonthDate} onChange={v => setYearMonthDate(v)} picker="month" />
                      {yearMonthDate && (
                        <div style={{marginLeft: 8}}>
                          周期：
                          {yearMonthDate?.startOf('month').format("YYYY-MM-DD")}
                          ~
                          {yearMonthDate?.endOf('month').format("YYYY-MM-DD")}
                        </div>
                      )}
                    </div>
                  )}
      />
      <FormModal modal={formModal} yearMonthDate={yearMonthDate} onSuccess={actionRef.current?.reload}
                   subjects={subjects} setSubjects={setSubjects} subjectById={subjectById}
                   onVisibleChange={handleFormModal}/>
    </PageContainer>
  )
}

function FormModal({modal, yearMonthDate, subjects, setSubjects, subjectById}) {
  const {visible, handleVisible, mode = "add"} = modal
  const isAddMode = mode === "add", isViewMode = mode === "view", isEditMode = mode === "edit"
  return (
    <ModalForm title="新增初始余额" width="420px" visible={visible}
               initialValues={{type: "SUBJECT", assistSettlement: "NOTHING", direction: "NOTHING"}}
               modalProps={{destroyOnClose: true}}
               onVisibleChange={handleVisible}
               onFinish={async (value) => {
                 const {subjectId} = value
                 const newValues = {
                   ...value,
                   subjectNumber: subjectById[subjectId]?.number,
                   subjectName: subjectById[subjectId]?.name,
                   yearMonthDate: yearMonthDate?.format("YYYY-MM")
                 }
                 if (isAddMode) {
                   return await addInitialBalanceItemUsingPOST(newValues).then(() => {
                     handleVisible(false)
                     actionRef.current?.reload()
                   })
                 } else if (isEditMode) {
                   return await updateInitialBalanceUsingPUT.then(() => {
                     handleVisible(false)
                     actionRef.current?.reload()
                   })
                 }
                 return true
               }}
    >
      <ProFormItem label="科目" name="subjectId">
        <AdvancedSubjectSelect subjects={subjects} placeholder="只能选择费用类科目"
                               fieldsName={{key: "id", title: (v) => `${v.number}-${v.name}`}}
                               disableFilter={(subject) => {
                                 return subject.hasLeaf
                               }}
                               setSubjects={setSubjects}
                               onlySelectedLeaf={true} disabled={isViewMode}
                               style={{width: '100%'}} />
      </ProFormItem>
      <ProFormSelect name="lendingDirection" labelCol={{span: 6}}
                     allowClear={false} label="方向" options={Object.values(LENDING_DIRECTION)}/>
      <ProFormText label="币别" name="currencyName"/>
      <ProFormItem name="amount" label="初期余额">
        <InputNumber min={0}/>
      </ProFormItem>
    </ModalForm>
  )
}