import AutoCompleteInput from "@/components/Common/AutoCompleteInput";
import {searchExpenseItemCueUsingGET} from "@/services/swagger/expenseBillWeb";
import {AdvancedSubjectSelect} from "@/components/AdvancedSubjectSelect";
import {ClearOutlined} from "@ant-design/icons";
import React, {useRef} from "react";
import moment from "moment";
import {useModel} from "@/.umi/plugin-model/useModel";
import ExtEditableProTable from "@/pages/ExpenseBillList/tables/EditableProTableItem";

const DEFAULT_CHAR = ""

export default function ExpenseBillItemTable({formRef, isViewMode, ...props}) {
  const actionRef = useRef()
  const {subjects, subjectById} = useModel("useSubjectModel")
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
        <AdvancedSubjectSelect subjects={subjects} placeholder="只能选择费用类科目"
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
    {
      title: "票据张数",
      valueType: "digit",
      dataIndex: "numOfBill",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.numOfBill || DEFAULT_CHAR
      }
    },
    {
      title: "票据金额",
      valueType: "digit",
      dataIndex: "billAmount",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.billAmount || DEFAULT_CHAR
      }
    },
    {
      title: "实报金额",
      valueType: "digit",
      dataIndex: "actualAmount",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.actualAmount || DEFAULT_CHAR
      }
    },
    {
      title: "补助金额",
      valueType: "digit",
      dataIndex: "subsidyAmount",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.subsidyAmount || DEFAULT_CHAR
      }
    },
    {
      title: "小计金额",
      valueType: "digit",
      dataIndex: "subtotalAmount",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.subtotalAmount || DEFAULT_CHAR
      }
    },
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
    <ExtEditableProTable name="items" columns={columns} actionRef={actionRef} isViewMode={isViewMode} {...props}/>
  )
}
