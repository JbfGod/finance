import React, {useEffect, useRef, useState} from "react"
import GlobalPageContainer from "@/components/PageContainer";
import ExProTable from "@/components/Table/ExtProTable";
import {generalLedgerUsingGET} from "@/services/swagger/reportWeb";
import moment from "moment";
import {listGroupByCurrencyNameUsingGET} from "@/services/swagger/currencyWeb";
import commonColumn from "@/pages/Report/commonColumn";
import commonNumberColumn from "@/pages/Report/commonNumberColumn";

const DEFAULT_CURRENCY = {label:'本位币', value:'人民币'}
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
    commonColumn("科目编号", "subjectNumber", {
      onCell: ({rowSpan}) => ({
        rowSpan: rowSpan || 0
      })
    }),
    commonColumn("科目名称", "subjectName", {
      onCell: ({rowSpan}) => ({
        rowSpan: rowSpan || 0
      })
    }),
    commonColumn("期间", "month", {width: 50}),
    commonColumn("凭证字号码", "maxVoucherNumber", {
      width: 100,
      render: (_, row) => {
        if (row.maxVoucherNumber) {
          const content = `记1~${row.maxVoucherNumber}号`
          return <span title={content}>{content}</span>
        }
        return ""
      }
    }),
    commonColumn("摘要", "summary"),
    commonNumberColumn("借方", "debitAmount"),
    commonNumberColumn("贷方", "creditAmount"),
    commonColumn("", "lendingDirection", {width: 30, ellipsis: false}),
    commonNumberColumn("余额", "balance")
  ]

  return (
    <GlobalPageContainer>
      <ExProTable actionRef={actionRef} columns={balanceColumns}
                  pagination={false} size="small" bordered
                  toolBarRender={false}
                  scroll={{y: window.innerHeight - 250}}
                  request={generalLedgerUsingGET}
      />
    </GlobalPageContainer>
  )
}
