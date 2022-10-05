import React, {useEffect, useRef, useState} from "react"
import GlobalPageContainer from "@/components/PageContainer";
import ExProTable from "@/components/Table/ExtProTable";
import {dailyCashUsingGET, generalLedgerUsingGET} from "@/services/swagger/reportWeb";
import moment from "moment";
import {listGroupByCurrencyNameUsingGET} from "@/services/swagger/currencyWeb";
import commonColumn from "@/pages/Report/commonColumn";
import commonNumberColumn from "@/pages/Report/commonNumberColumn";
import {useModel} from "@/.umi/plugin-model/useModel";

const DEFAULT_CURRENCY = [
  {label:'原币和本位币', value:''},
  {label:'本位币', value:'人民币'},
]
export default function () {
  const actionRef = useRef()
  const {cashSubjects} = useModel('useSubjectModel')
  const [currencyNames, setCurrencyNames] = useState(DEFAULT_CURRENCY)
  useEffect(() => {
    // 加载币别列表
    listGroupByCurrencyNameUsingGET().then(({data}) =>
      setCurrencyNames([...DEFAULT_CURRENCY, ...data.map(name => ({label:name, value:name}))])
    )
    // 寻找现金科目
  }, [])
  const balanceColumns = [
    {
      title: "科目", dataIndex: "subjectId", valueType: "select",
      hideInTable: true, initialValue: cashSubjects[0]?.id, fieldProps: {
        allowClear: false,
        options: cashSubjects.map(sub => ({label: sub.name, value: sub.id}))
      }, search: cashSubjects.length > 1
    },
    {
      title: "币别", dataIndex: "currencyName", valueType: "select",
      hideInTable: true, initialValue: "", fieldProps: {
        allowClear: false,
        options: currencyNames
      }
    },
    {
      title: "日期", dataIndex: "voucherDate",
      valueType: "date", hideInTable: true,
      fieldProps: {allowClear: false},
      initialValue: moment()
    },
    commonColumn("科目编号", "subjectNumber"),
    commonColumn("科目名称", "subjectName"),
    commonColumn("币别", "currency", {width: 80}),
    {
      title: "昨日余额", children: [
        commonNumberColumn("原币", "balanceOfYesterday"),
        commonNumberColumn("本位币", "localBalanceOfYesterday")
      ], search: false
    },
    {
      title: "今日借方", children: [
        commonNumberColumn("原币", "debitAmountOfToday"),
        commonNumberColumn("本位币", "localDebitAmountOfToday")
      ], search: false
    },
    {
      title: "今日贷方", children: [
        commonNumberColumn("原币", "creditAmountOfToday"),
        commonNumberColumn("本位币", "localCreditAmountOfToday")
      ], search: false
    },
    {
      title: "今日余额", children: [
        commonNumberColumn("原币", "balanceOfToday"),
        commonNumberColumn("本位币", "localBalanceOfToday")
      ], search: false
    },
    commonNumberColumn("借方笔数", "debitTotal"),
    commonNumberColumn("贷方笔数", "creditTotal")
  ]

  return (
    <GlobalPageContainer>
      <ExProTable actionRef={actionRef} columns={balanceColumns}
                  params={{subjectId : cashSubjects[0]?.id}}
                  pagination={false} size="small" bordered
                  toolBarRender={false}
                  scroll={{y: window.innerHeight - 250}}
                  request={async (params) => params.subjectId && dailyCashUsingGET(params)}
      />
    </GlobalPageContainer>
  )
}
