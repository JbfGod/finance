import {AdvancedSubjectSelect} from "@/components/AdvancedSubjectSelect";
import {ClearOutlined} from "@ant-design/icons";
import React, {useRef} from "react";
import {useModel} from "@/.umi/plugin-model/useModel";
import ExtEditableProTable from "@/pages/ExpenseBillList/tables/EditableProTableItem";

const DEFAULT_CHAR = ""

export default function ExpenseBillSubsidyTable({formRef, itemIndex, isViewMode, ...props}) {
  const {subjectById} = useModel("useSubjectModel", ({subjectById}) => ({subjectById}))
  const actionRef = useRef()
  const columns = [
    {
      title: "补助费用名称", dataIndex: "subjectId", width: 150,
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
      title: "补助明细天数",
      valueType: "digit",
      dataIndex: "days",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.days || DEFAULT_CHAR
      }
    },
    {
      title: "元/天",
      valueType: "digit",
      dataIndex: "amountForDay",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.amountForDay || DEFAULT_CHAR
      }
    },
    {
      title: "补助金额",
      valueType: "digit",
      dataIndex: "amount",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.amount || DEFAULT_CHAR
      }
    },
    ...(isViewMode ? [] : [{
      title: "操作", dataIndex: "operate", editable: false, width: 45,
      render: (_, __, index) => {
        return (
          <a title="清空" onClick={() => clearByIndex(index)}>
            <ClearOutlined />
          </a>
        )
      }
    }])
  ]
  const clearByIndex = (index) => {
    const items = formRef.getFieldValue(["items"])
    const subsidies = items[itemIndex].subsidies
    formRef.setFieldsValue({items: items.map((item, idx) => (
        idx === itemIndex ? {
          ...item,
          subsidies: subsidies.map((subsidy, idx2) => (
            idx2 === index ? {index} : subsidy
          ))
        } : item
      ))
    })
  }
  return (
    <ExtEditableProTable name={["items", itemIndex, "subsidies"]} columns={columns} actionRef={actionRef} isViewMode={isViewMode} {...props}/>
  )
}
