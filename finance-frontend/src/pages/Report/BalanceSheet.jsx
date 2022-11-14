import React, {useEffect, useRef, useState} from "react"
import GlobalPageContainer from "@/components/PageContainer";
import moment from "moment";
import commonNumberColumn from "@/pages/Report/commonNumberColumn";
import styles from "@/pages/VoucherList/index.less";
import {EditableProTable} from "@ant-design/pro-table";
import commonColumn from "@/pages/Report/commonColumn";
import {Badge, Button, DatePicker, Modal, Space, Table, Tabs, Tag} from "antd";
import {useModalWithParam} from "@/utils/hooks";
import {useModel} from "umi"
import {
  listBalanceSheetOfMonthUsingGET,
  saveBalanceSheetReportUsingPOST
} from "@/services/swagger/balanceSheetReportWeb";

export default function () {
  const actionRef = useRef()
  const formulaModal = useModalWithParam()
  const [yearMonth, setYearMonth] = useState(moment())
  const [editableKeys, setEditableRowKeys] = useState([])
  const [dataSource, setDataSource] = useState([])
  const [saving, setSaving] = useState(false)
  const [loading, setLoading] = useState(false)
  const columns = [
    commonColumn("资产", "assetsName", {
      render: (_, row) => {
        const content = row.assetsName || ""
        return <div style={{textAlign: "left", whiteSpace: "pre"}} title={content}>{content}</div>
      }
    }),
    commonColumn("行次", "assetsRowNum", {
      width: 95, valueType: "digital"
    }),
    commonNumberColumn("年初余额", "assetsOpeningAmount", {editable: false, width: 155}),
    commonNumberColumn("年末余额", "assetsClosingAmount", {editable: false, width: 155}),
    {
      title: "操作", editable: false, width: 125,
      render: (_, row, idx) => (
        <Space size={12}>
          <Badge dot={row.assetsFormula}>
            <a onClick={() => formulaModal.open({
              rowNum: row.assetsRowNum, changeFormula: formulaHandler(row.id, "assetsFormula"),
              formulaValue: row.assetsFormula
            })}>设置公式</a>
          </Badge>
          <a onClick={() => delById(row.id, "assets")}>删除</a>
        </Space>
      )
    },
    commonColumn("负债及所有者权益", "equityName", {
      render: (_, row) => {
        const content = row.equityName || ""
        return <div style={{textAlign: "left", whiteSpace: "pre"}} title={content}>{content}</div>
      }
    }),
    commonColumn("行次", "equityRowNum", {
      width: 95, valueType: "digital"
    }),
    commonNumberColumn("年初余额", "equityOpeningAmount", {editable: false, width: 155}),
    commonNumberColumn("年末余额", "equityClosingAmount", {editable: false, width: 155}),
    {
      title: "操作", editable: false, width: 125,
      render: (_, row, idx) => (
        <Space size={12}>
          <Badge dot={row.equityFormula}>
            <a onClick={() => formulaModal.open({
              rowNum: row.equityRowNum, changeFormula: formulaHandler(row.id, "equityFormula"),
              formulaValue: row.equityFormula
            })}>设置公式</a>
          </Badge>
          <a onClick={() => delById(row.id, "equity")}>删除</a>
        </Space>
      )
    }
  ]

  const delById = (id, type) => {
    setDataSource(oldDs => {
      let isUpdate = false
      let newDs = oldDs.filter(ele => {
        if (ele.id === id) {
          if (type === "equity" && !ele.assetsName && ele.assetsRowNumber == null && ele.assetsFormula == null) {
            return false
          } else if (type === "assets" && !ele.equityName && ele.equityRowNumber == null && ele.equityFormula == null) {
            return false
          }
          isUpdate = true
        }
        return true
      })
      if (isUpdate) {
        return oldDs.map(ele => {
          if (ele.id !== id) {
            return ele
          }
          const newEle = {...ele}
          if (type === "assets") {
            newEle.assetsName = null
            newEle.assetsRowNumber = null
            newEle.assetsFormula = null
            newEle.assetsOpeningAmount = null
            newEle.assetsClosingAmount = null
          } else {
            newEle.equityName = null
            newEle.equityRowNumber = null
            newEle.equityFormula = null
            newEle.equityOpeningAmount = null
            newEle.equityClosingAmount = null
          }
          return newEle
        })
      }
      return newDs
    })
  }
  const formulaHandler = (id, formulaKey) => (formula) => setDataSource(dataSource.map(ele => {
    if (ele.id === id) {
      return {...ele, [formulaKey]: formula}
    }
    return ele
  }))
  const onSave = () => {
    setSaving(true)
    saveBalanceSheetReportUsingPOST({
      yearMonthNum: yearMonth.format("YYYYMM"),
      rows: dataSource
    }).finally(_ => {
      setSaving(false)
      setEditableRowKeys([])
      loadBalanceSheet()
    })
  }
  const loadBalanceSheet = () => {
    setLoading(true)
    listBalanceSheetOfMonthUsingGET({yearMonth: yearMonth.format("YYYY-MM")})
      .then(res => setDataSource(res.data))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    loadBalanceSheet()
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
                              window.localStorage.setItem("balanceSheetTemplate", JSON.stringify(dataSource))
                            }}>复制本月模板</Button>,
                            <Button type="primary" key="paste" onClick={() => {
                              const tpl = window.localStorage.getItem("balanceSheetTemplate")
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
  const rowNumDataSource = records.flatMap(ele => [
    {rowNum: ele.assetsRowNum, formula: ele.assetsFormula},
    {rowNum: ele.equityRowNum, formula: ele.equityFormula}
  ]).filter(ele => ele.rowNum && ele.rowNum !== rowNum
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
