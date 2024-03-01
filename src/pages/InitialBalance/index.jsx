import React, {useCallback, useEffect, useState} from "react"
import PageContainer from "@/components/PageContainer"
import {InputNumber, Radio} from "antd"
import {useSecurity} from "@/utils/hooks"
import {LENDING_DIRECTION, SUBJECT_CATEGORY} from "@/constants";
import {useModel} from "@umijs/max";
import {EditableProTable} from "@ant-design/pro-table";
import moment from "moment";
import {arrayToTree} from "@/utils/common";
import {updateSubjectInitialBalanceUsingPUT} from "@/services/swagger/subjectWeb";
import {debounce} from "lodash";
import styles from "./index.less"

const AmountColumn = ({title, dataIndex, onBlur, ...props}) => ({
  title, dataIndex, align: "center", valueType: "digit",
  renderFormItem: ({entity: item}, { type, defaultRender, formItemProps, fieldProps, ...rest }, form) => {
    return (
      <InputNumber className={styles.inputNumber} {...fieldProps} onBlur={e => onBlur?.(item)} bordered={false}
      style={{textAlign: "right"}} formatter={value => value == 0?"":value}/>
    )
  },
  render: (_, row) => <div style={{textAlign: "right", paddingRight: 11}}>{Number(row[dataIndex]) || ""}</div>,
  ...props
})

export default () => {
  const [activeTabKey, setActiveTabKey] = useState(SUBJECT_CATEGORY.ASSETS.value)
  const {proxyCustomer} = useSecurity()
  const [subjects, setSubjects] = useState([])

  const {subjectById, fetchSubjects} = useModel("useSubjectModel")

  const fetchInitialBalance = () => {
    fetchSubjects({category: activeTabKey}).then(({data}) => {
      const treeData = arrayToTree(data)
      setSubjects(treeData)
    })
  }

  const onBlur = useCallback(debounce((item) => {
    updateSubjectInitialBalanceUsingPUT({
      ...item,
      beginningBalance: item.lendingDirection === LENDING_DIRECTION.BORROW.value?
        (item.openingBalance - item.debitAnnualAmount + Number(item.creditAnnualAmount))
        :
        (item.debitAnnualAmount - item.creditAnnualAmount + Number(item.openingBalance))
    }).then(e => fetchInitialBalance())
  }, 1000), [])
  useEffect(() => {
    fetchInitialBalance()
  }, [activeTabKey])


  const columns = [
    {
      title: "科目编号", dataIndex: "number", editable: false,
      render:(_, row) => {
        return (
          <span style={{marginLeft: (row.level - 1) * 10}}>{row.number}</span>
        )
      }
    },
    {
      title: "科目名称", dataIndex: "name", editable: false
    },
    {
      title: "方向", dataIndex: "lendingDirection", editable: false, valueType: "select",
      width: 50, align: "center",
      fieldProps: {
        allowClear: false,
        options: Object.values(LENDING_DIRECTION)
      }
    },
    AmountColumn({title: "期初余额", dataIndex: "openingBalance", onBlur}),
    AmountColumn({title: "本年累计借方", dataIndex: "debitAnnualAmount", onBlur}),
    AmountColumn({title: "本年累计贷方", dataIndex: "creditAnnualAmount", onBlur}),
    AmountColumn({title: "年初余额", dataIndex: "beginningBalance",
      editable: false,
      render: (_, row) => {
        return (row.openingBalance - row.debitAnnualAmount + Number(row.creditAnnualAmount)) || ""
      }
    })
  ]
  return (
    <PageContainer>
      <EditableProTable rowKey="id" size="middle" bordered={true}
        columns={columns} scroll={{y: window.innerHeight - 185}} value={subjects}
                        expandable={{
                          expandedRowKeys: Object.keys(subjectById).map(key => Number(key)),
                          expandIcon:() => null
                        }}
        recordCreatorProps={false}
         headerTitle={(
           <div style={{display: "flex", alignItems: "center"}}>
             <Radio.Group defaultValue={activeTabKey} style={{marginRight: 12}} onChange={e => setActiveTabKey(e.target.value)}>
               {Object.values(SUBJECT_CATEGORY).map(ele => (
                  <Radio.Button key={ele.value} value={ele.value}>{ele.label}</Radio.Button>
               ))}
             </Radio.Group>
             <div>启用期间：{moment(proxyCustomer.enablePeriod, "YYYYMM").format("YYYY-MM")}</div>
           </div>
         )}
        editable={{
          editableKeys: Object.keys(subjectById).map(key => Number(key)).filter(id => !subjectById[id].hasLeaf),
          onValuesChange: (record, recordList) => {
            setSubjects(recordList)
          }
      }}
      />
    </PageContainer>
  )
}
