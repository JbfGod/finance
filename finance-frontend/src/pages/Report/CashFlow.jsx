import React, {useEffect, useRef, useState} from "react"
import GlobalPageContainer from "@/components/PageContainer";
import moment from "moment";
import commonNumberColumn from "@/pages/Report/commonNumberColumn";
import styles from "@/pages/VoucherList/index.less";
import {EditableProTable} from "@ant-design/pro-table";
import commonColumn from "@/pages/Report/commonColumn";
import {Badge, Button, Col, DatePicker, Modal, Row, Space, Table, Tabs, Tag} from "antd";
import {useModalWithParam} from "@/utils/hooks";
import {useModel} from "umi"
import {listCashFlowOfMonthUsingGET, saveCashFlowReportUsingPOST} from "@/services/swagger/cashFlowReportWeb";
import ExProTable from "@/components/Table/ExtProTable";
import {voucherItemBySubjectUsingGET} from "@/services/swagger/voucherWeb";
import VoucherForm from "@/pages/VoucherList/VoucherForm";

export default function () {
  const actionRef = useRef()
  const formulaModal = useModalWithParam()
  const [yearMonth, setYearMonth] = useState(moment())
  const [editableKeys, setEditableRowKeys] = useState([])
  const [dataSource, setDataSource] = useState([])
  const [saving, setSaving] = useState(false)
  const [loading, setLoading] = useState(false)
  const columns = [
    commonColumn("项目", "name", {
      render: (_, row) => {
        const content = row.name || ""
        return <div style={{textAlign: "left", whiteSpace: "pre"}} title={content}>{content}</div>
      }
    }),
    commonColumn("行次", "rowNum", {
      width: 95, valueType: "digital"
    }),
    commonNumberColumn("金额", "amount", {editable: false, width: 155}),
    {
      title: "操作", editable: false, width: 125,
      render: (_, row) => (
        <Space size={12}>
          <Badge dot={row.formula}>
            <a onClick={() => formulaModal.open({
              rowNum: row.rowNum, changeFormula: formulaHandler(row.id),
              formulaValue: row.formula
            })}>设置公式</a>
          </Badge>
          <a onClick={() => delById(row.id)}>删除</a>
        </Space>
      )
    }
  ]

  const delById = (id) => {
    setDataSource(oldDs => oldDs.filter(ele => ele.id !== id))
  }
  const formulaHandler = (id) => (formula) => setDataSource(dataSource.map(ele => {
    if (ele.id === id) {
      return {...ele, formula}
    }
    return ele
  }))
  const onSave = () => {
    setSaving(true)
    saveCashFlowReportUsingPOST({
      yearMonthNum: yearMonth.format("YYYYMM"),
      rows: dataSource
    }).finally(_ => {
      setSaving(false)
      setEditableRowKeys([])
      loadCashFlow()
    })
  }
  const loadCashFlow = () => {
    setLoading(true)
    listCashFlowOfMonthUsingGET({yearMonth: yearMonth.format("YYYY-MM")})
      .then(res => setDataSource(res.data))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    loadCashFlow()
  }, [yearMonth])
  return (
    <GlobalPageContainer>
      <EditableProTable value={dataSource} actionRef={actionRef} columns={columns} bordered loading={loading}
                        controlled size="small" className={styles.voucherTbl} rowKey="id"
                        scroll={{y: window.innerHeight - 325}}
                        headerTitle={(
                          <Space>月份：<DatePicker picker="month" value={yearMonth} onChange={setYearMonth}
                                                allowClear={false}/></Space>
                        )}
                        toolBarRender={() => {
                          return [
                            <Button type="primary" key="copy" onClick={() => {
                              window.localStorage.setItem("cashFlowTemplate", JSON.stringify(dataSource))
                            }}>复制本月模板</Button>,
                            <Button type="primary" key="paste" onClick={() => {
                              const tpl = window.localStorage.getItem("cashFlowTemplate")
                              tpl && setDataSource(JSON.parse(tpl))
                            }}>粘贴模板</Button>,
                            <Button type="primary" key="save" loading={saving}
                                    onClick={onSave}
                            >保存</Button>
                          ];
                        }}
                        recordCreatorProps={{
                          newRecordType: 'dataSource',
                          record: () => ({
                            id: `new-${Date.now()}`,
                          }),
                        }}
                        onRow={(record, index) => ({
                          onClick: (e) => {
                            e.stopPropagation()
                            setEditableRowKeys([record.id])
                          }
                        })}
                        editable={{
                          type: 'multiple',
                          editableKeys,
                          actionRender: (row, config, defaultDoms) => {
                            return [defaultDoms.delete];
                          },
                          onValuesChange: (record, recordList) => {
                            setDataSource(recordList);
                          },
                          onChange: (keys) => {
                            setEditableRowKeys([keys.lastItem])
                          },
                        }}
      />
      <SetFormulaModal yearMonth={yearMonth} modal={formulaModal} records={dataSource}/>
    </GlobalPageContainer>
  )
}

const formulaPattern = /(rowNum|voucherItemId):([+\d\-]+)/

function SetFormulaModal({modal, records, yearMonth}) {
  const {state = {}} = modal
  const {rowNum, changeFormula, formulaValue} = state
  const {subjects} = useModel('useSubjectModel')
  const [activeKey, setActiveKey] = useState("rowNum")
  const [formula, setFormula] = useState("")
  const [selectedSubjectId, setSelectedSubjectId] = useState([])

  const rowNumDataSource = records.map(ele => ({rowNum: ele.rowNum, formula: ele.formula}))
    .filter(ele => ele.rowNum && ele.rowNum !== rowNum
      && (
        !ele.formula?.startsWith("rowNum") || !ele.formula?.match(new RegExp(`[+-]${rowNum}\\b`))
      )
    )

  const formulaParts = formula.match(formulaPattern) ? RegExp.$2.split(/(?<=[+-]\d+)\b/) : []
  const onTabChange = (v) => {
    setActiveKey(v)
    setFormula('')
  }

  const onAdd = (item) => {
    setFormula(old => old ? (old + `+${item}`) : `${activeKey}:+${item}`)
  }
  const onSubtract = (item) => {
    setFormula(old => old ? (old + `-${item}`) : `${activeKey}:-${item}`)
  }
  const onOk = () => {
    changeFormula(formula)
    modal.close()
  }
  useEffect(() => {
    if (!modal.visible) {
      setActiveKey('rowNum')
      setFormula('')
    } else {
      setActiveKey(formulaValue?.startsWith("voucherItemId") ? "voucherItemId" : "rowNum")
      setFormula(formulaValue || "")
    }
  }, [modal.visible])
  if (!modal.visible) {
    return null
  }
  return (
    <Modal width={900} open={true} onCancel={modal.close} onOk={onOk}>
      <Tabs activeKey={activeKey} onChange={onTabChange}
            items={[
              {
                label: `行号`, key: 'rowNum',
                children: (
                  <Table size="small" rowKey="rowNum" pagination={false}
                         dataSource={rowNumDataSource}
                         columns={[
                           {title: "行号", dataIndex: "rowNum"},
                           {
                             title: "操作", dataIndex: "operate",
                             render: (_, row) => (
                               <Space size={12}>
                                 <a onClick={() => onAdd(row.rowNum)}>加</a>
                                 <a onClick={() => onSubtract(row.rowNum)}>减</a>
                               </Space>
                             )
                           },
                         ]}/>
                )
              },
              {
                label: `凭证`, key: 'voucherItemId',
                children: (
                  <Row>
                    <Col span={12}>
                      <Table size="small" rowKey="id" pagination={false} scroll={{y: 325}}
                             rowSelection={{
                               type: "radio",
                               selectedRowKeys: selectedSubjectId,
                               onChange: (keys => setSelectedSubjectId(keys))
                             }}
                             onRow={(record) => ({
                               onClick: (e) => {
                                 e.stopPropagation()
                                 setSelectedSubjectId([record.id])
                               }
                             })}
                             dataSource={subjects} columns={[
                        {title: "行号编号", dataIndex: "number", width: 125},
                        {title: "科目名称", dataIndex: "name", width: 150},
                      ]}/>
                    </Col>
                    <Col span={12}>
                      {selectedSubjectId.length > 0 && (
                        <ExProTable columns={[
                          {title: "凭证日期", dataIndex: "voucherDate", align: "center", width: 100},
                          {title: "凭证号", dataIndex: "voucherNumber", align: "center", width: 50},
                          commonColumn("摘要", "summary"),
                          commonColumn("金额", "amount", {
                            render: (_, row) => {
                              const amount = row.localDebitAmount - row.localCreditAmount
                              const content = amount || ""
                              return <span title={content}>{content}</span>
                            }
                          }),
                          {
                            title: "操作", dataIndex: "operate",
                            render: (_, row) => (
                              <Space size={12}>
                                <a onClick={() => onAdd(row.id)}>加</a>
                                <a onClick={() => onSubtract(row.id)}>减</a>
                              </Space>
                            )
                          }
                        ]} toolBarRender={false}
                                    pagination={false} bordered size="small"
                                    search={false} scroll={{y: 325}}
                                    params={{subjectId: selectedSubjectId[0], yearMonth: yearMonth.format("YYYY-MM")}}
                                    request={voucherItemBySubjectUsingGET}
                        />
                      )}
                    </Col>
                  </Row>
                )
              },
            ]}
      />
      <br/>

      {formulaParts.map((part, index) => (
        <Tag key={formulaParts.filter((_, i) => i <= index).join("")} closable
             onClose={() => setFormula(`${activeKey}:${formulaParts.filter((_, i) => i !== index).join("")}`)}
             style={{userSelect: "none"}} color={part.startsWith("+") ? "success" : "processing"}>
          {part}
        </Tag>
      ))}
    </Modal>
  )
}
