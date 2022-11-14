import React, {useEffect, useRef, useState} from "react"
import GlobalPageContainer from "@/components/PageContainer";
import moment from "moment";
import commonNumberColumn from "@/pages/Report/commonNumberColumn";
import styles from "@/pages/VoucherList/index.less";
import {EditableProTable} from "@ant-design/pro-table";
import commonColumn from "@/pages/Report/commonColumn";
import {Badge, Button, DatePicker, Modal, Space, Table, Tabs, Tag, message} from "antd";
import {useModalWithParam} from "@/utils/hooks";
import {useModel} from "umi"
import {listProfitOfMonthUsingGET, saveProfitReportUsingPOST} from "@/services/swagger/profitReportWeb";

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
    commonNumberColumn("本年累积金额", "annualAmount", {editable: false, width: 155}),
    commonNumberColumn("本月金额", "monthlyAmount", {editable: false, width: 155}),
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
    saveProfitReportUsingPOST({
      yearMonthNum: yearMonth.format("YYYYMM"),
      rows: dataSource
    }).finally(_ => {
      setSaving(false)
      setEditableRowKeys([])
      loadProfit()
    })
  }
  const loadProfit = () => {
    setLoading(true)
    listProfitOfMonthUsingGET({yearMonth: yearMonth.format("YYYY-MM")})
      .then(res => setDataSource(res.data))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    loadProfit()
  }, [yearMonth])
  return (
    <GlobalPageContainer>
      <EditableProTable value={dataSource} actionRef={actionRef} columns={columns} bordered loading={loading}
                        controlled size="small" className={styles.voucherTbl} rowKey="id"
                        scroll={{y: window.innerHeight - 325}}
                        headerTitle={(
                          <Space>月份：<DatePicker picker="month" value={yearMonth} onChange={setYearMonth} allowClear={false}/></Space>
                        )}
                        toolBarRender={() => {
                          return [
                            <Button type="primary" key="copy" onClick={() => {
                                window.localStorage.setItem("profitTemplate", JSON.stringify(dataSource))
                            }}>复制本月模板</Button>,
                            <Button type="primary" key="paste" onClick={() => {
                              const tpl = window.localStorage.getItem("profitTemplate")
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
      <SetFormulaModal modal={formulaModal} records={dataSource}/>
    </GlobalPageContainer>
  )
}

const formulaPattern = /(rowNum|subjectId):([+\d\-]+)/

function SetFormulaModal({modal, records}) {
  const {state = {}} = modal
  const {rowNum, changeFormula, formulaValue} = state
  const {subjects} = useModel('useSubjectModel')
  const [activeKey, setActiveKey] = useState("rowNum")
  const [formula, setFormula] = useState("")
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
      setActiveKey(formulaValue?.startsWith("subjectId") ? "subjectId" : "rowNum")
      setFormula(formulaValue || "")
    }
  }, [modal.visible])
  if (!modal.visible) {
    return null
  }
  return (
    <Modal open={true} onCancel={modal.close} onOk={onOk}>
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
                label: `科目`, key: 'subjectId',
                children: (
                  <Table size="small" rowKey="id" pagination={false} scroll={{y: 325}}
                         dataSource={subjects} columns={[
                    {title: "行号编号", dataIndex: "number"},
                    {title: "科目名称", dataIndex: "name"},
                    {
                      title: "操作", dataIndex: "operate",
                      render: (_, row) => (
                        <Space size={12}>
                          <a onClick={() => onAdd(row.number)}>加</a>
                          <a onClick={() => onSubtract(row.number)}>减</a>
                        </Space>
                      )
                    },
                  ]}/>
                )
              },
            ]}
      />
      <br/>
      {formulaParts.map((part, index) => (
        <Tag key={formulaParts.filter((_, i) => i <= index).join("")} closable
             onClose={() => setFormula(`${activeKey}:${formulaParts.filter((_, i) => i !== index).join("")}`)}
             style={{userSelect: "none"}} color={part.startsWith("+") ? "success" : "processing"} >
          {part}
        </Tag>
      ))}
    </Modal>
  )
}
