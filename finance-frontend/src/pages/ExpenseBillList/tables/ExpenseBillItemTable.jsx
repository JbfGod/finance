import AutoCompleteInput from "@/components/Common/AutoCompleteInput";
import {searchExpenseItemCueUsingGET} from "@/services/swagger/expenseBillWeb";
import {AdvancedSubjectSelect} from "@/components/AdvancedSubjectSelect";
import {ClearOutlined} from "@ant-design/icons";
import React, {useRef} from "react";
import moment from "moment";
import {useModel} from "umi";
import ExtEditableProTable from "@/pages/ExpenseBillList/tables/EditableProTableItem";
import {Table} from "antd";
import MoneyColumn from "@/pages/ExpenseBillList/tables/MomeyColumn";
import NumberColumn from "@/pages/ExpenseBillList/tables/NumberColumn";

const DEFAULT_CHAR = ""

export default function ExpenseBillItemTable({formRef, isViewMode, ...props}) {
  const actionRef = useRef()
  const {subjectById} = useModel("useSubjectModel")
  const columns = [
    {
      title: "开始时间", width: 90,
      valueType: "date",
      dataIndex: "beginTime",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.beginTime ? (
          moment.isMoment(row.beginTime) ? row.beginTime.format("YYYY-MM-DD") : row.beginTime
        ) : DEFAULT_CHAR
      }
    },
    {
      title: "结束时间", width: 90,
      valueType: "date",
      dataIndex: "endTime",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.endTime ? (
          moment.isMoment(row.endTime) ? row.endTime.format("YYYY-MM-DD") : row.endTime
        ) : DEFAULT_CHAR
      }
    },
    {
      title: "出差起讫地点", width: 150,
      valueType: "text",
      dataIndex: "travelPlace",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.travelPlace || DEFAULT_CHAR
      }
    },
    {
      title: "费用名称", dataIndex: "subjectId", width: 150,
      renderFormItem: () => (
        <AdvancedSubjectSelect placeholder="只能选择费用类科目"
                               fieldsName={{key: "id", title: (v) => `${v.number}-${v.name}`}}
                               disableFilter={(subject) => {
                                 return subject.hasLeaf
                               }}
                               onlySelectedLeaf={true} disabled={isViewMode}
                               style={{width: '100%'}}/>
      ),
      render: (_, row) => {
        const v = subjectById[row.subjectId]
        return v ? `${v.number}-${v.name}` : DEFAULT_CHAR
      }
    },
    {
      title: (<div style={{textAlign: "center"}}>摘要</div>), dataIndex: "summary", width: 90,
      renderFormItem: () => (
        <AutoCompleteInput placeholder="" disabled={isViewMode} request={(keyword) => {
          return searchExpenseItemCueUsingGET({column: 'SUMMARY', keyword})
        }}/>
      ),
      render: (_, row) => {
        return row.summary || DEFAULT_CHAR
      }
    },
    NumberColumn("票据张数", "numOfBill"),
    MoneyColumn("票据金额", "billAmount"),
    MoneyColumn("实报金额", "actualAmount"),
    MoneyColumn("补助金额", "subsidyAmount", {
      editable: false, render: (_, row) => {
        const {subsidies} = row
        return subsidies.reduce((prev, curr) => prev + Number(curr.amount || 0), 0) || ""
      }
    }),
    MoneyColumn("小计金额", "subtotalAmount", {
      editable: false, render: (_, row) => {
        const {subsidies} = row
        return subsidies.reduce((prev, curr) => prev + Number(curr.amount || 0), row.actualAmount || 0) || ""
      }
    }),
    {
      title: (<div style={{textAlign: "center"}}>备注</div>), dataIndex: "remark", width: 75,
      renderFormItem: () => (
        <AutoCompleteInput placeholder="" disabled={isViewMode} request={(keyword) => {
          return searchExpenseItemCueUsingGET({column: 'REMARK', keyword})
        }}/>
      ),
      render: (_, row) => {
        return row.remark || DEFAULT_CHAR
      }
    },
    ...(isViewMode ? [] : [{
      title: "操作", dataIndex: "operate", editable: false, width: 45,
      render: (_, __, index) => {
        return (
          <a title="清空" onClick={() => clearByIndex(index)}>
            <ClearOutlined/>
          </a>
        )
      }
    }])
  ]
  const clearByIndex = (index) => {
    const items = formRef.getFieldValue(["items"])
    formRef.setFieldsValue({
      items: items.map((item, idx) => {
        const {subsidies, attachments} = item
        return (
          idx === index ? {
            subsidies: subsidies.map((_, index) => ({index})),
            attachments: attachments.map((_, index) => ({index})),
            index
          } : item
        )
      })
    })
  }
  return (
    <ExtEditableProTable name="items" columns={columns} actionRef={actionRef} isViewMode={isViewMode}
                         summary={(rows) => {
                           let totalNumOfBill = 0
                             , totalBillAmount = 0
                             , totalActualAmount = 0
                             , totalSubtotalAmount = 0
                             , totalSubsidyAmount = 0
                           rows.forEach(row => {
                             totalNumOfBill += Number(row.numOfBill || 0)
                             totalBillAmount += Number(row.billAmount || 0)
                             totalActualAmount += Number(row.actualAmount || 0)
                             totalSubtotalAmount += Number(row.subsidyAmount || 0)
                             totalSubsidyAmount += Number(row.subtotalAmount || 0)
                           })
                           return (
                             <Table.Summary fixed>
                               <Table.Summary.Row>
                                 <Table.Summary.Cell index={0}>合计</Table.Summary.Cell>
                                 <Table.Summary.Cell index={1} colSpan={4}></Table.Summary.Cell>
                                 <Table.Summary.Cell index={2}>{totalNumOfBill || ""}</Table.Summary.Cell>
                                 <Table.Summary.Cell index={3}>{totalBillAmount || ""}</Table.Summary.Cell>
                                 <Table.Summary.Cell index={4}>{totalActualAmount || ""}</Table.Summary.Cell>
                                 <Table.Summary.Cell index={5}>{totalSubtotalAmount || ""}</Table.Summary.Cell>
                                 <Table.Summary.Cell index={6}>{totalSubsidyAmount || ""}</Table.Summary.Cell>
                                 <Table.Summary.Cell index={7}></Table.Summary.Cell>
                                 {!isViewMode && (
                                   <Table.Summary.Cell index={8}></Table.Summary.Cell>
                                 )}
                               </Table.Summary.Row>
                             </Table.Summary>
                           )
                         }}
                         {...props}/>
  )
}
