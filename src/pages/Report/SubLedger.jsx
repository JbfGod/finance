import React, {useEffect, useRef, useState} from "react"
import GlobalPageContainer from "@/components/PageContainer";
import ExProTable from "@/components/Table/ExtProTable";
import {subLedgerUsingGET} from "@/services/swagger/reportWeb";
import moment from "moment";
import {listGroupByCurrencyNameUsingGET} from "@/services/swagger/currencyWeb";

const commonColumn = (title, dataIndex, props) => ({
  title, dataIndex, align: "center", render: (_, row) => {
    const content = row[dataIndex] || ""
    return <span title={content}>{content}</span>
  },
  valueType: "text", search: false, ...props
})

const commonNumberColumn = (title, dataIndex, props) => ({
  title, dataIndex, align: "center", render: (_, row) => {
    const content = Number(row[dataIndex]) || ""
    return <span title={content}>{content}</span>
  },
  valueType: "text", search: false, ...props
})
const DEFAULT_CURRENCY = {label:'人民币', value:'人民币'}
export default function () {
  const actionRef = useRef()
  const [currencyNames, setCurrencyNames] = useState([DEFAULT_CURRENCY])
  useEffect(() => {
    listGroupByCurrencyNameUsingGET().then(({data}) =>
      setCurrencyNames([DEFAULT_CURRENCY, ...data.map(name => ({label:name, value:name}))])
    )
  }, [])
  const balanceColumns = [
    {
      title: "科目", dataIndex: "subjectId",
      hideInTable: true, valueType: "advancedSubjectSelect",
    },
    {
      title: "期间", dataIndex: "dateMonthRange",
      search: {
        transform: (v) => ({
          startMonth: moment(v?.[0]).format("YYYY-MM"),
          endMonth: moment(v?.[1]).format("YYYY-MM")
        })
      },
      valueType: "dateMonthRange", hideInTable: true,
      fieldProps: {allowClear: false},
      initialValue: [moment(), moment()]
    },
    {
      title: "币别", dataIndex: "currencyName", valueType: "select",
      hideInTable: true, initialValue: "人民币", fieldProps: {
        allowClear: false,
        options: currencyNames
      }
    },
    commonColumn("日期", "voucherDate", {width: 110}),
    commonColumn("凭证号", "voucherNumber", {
      width: 90,
      render: (_, row) => {
        if (row.voucherNumber) {
          const content = `记  -  ${row.voucherNumber}号`
          return <span title={content}>{content}</span>
        }
        return ""
      }
    }),
    commonColumn("摘要", "summary"),
    commonNumberColumn("借方金额", "debitAmount"),
    commonNumberColumn("贷方金额", "creditAmount"),
    commonColumn("", "lendingDirection", {width: 30, ellipsis: false}),
    commonNumberColumn("余额", "balance"),

  ]

  return (
    <GlobalPageContainer>
      <ExProTable actionRef={actionRef} columns={balanceColumns}
                  pagination={false} size="small" bordered
                  toolBarRender={false}
                  scroll={{y: window.innerHeight - 250}}
                  request={async (param) => {
                    if (!param.subjectId) {
                      return {success: true, data: []}
                    }
                    return subLedgerUsingGET(param)
                  }}
      />
    </GlobalPageContainer>
  )
}
