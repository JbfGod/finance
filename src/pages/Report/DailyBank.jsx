import React, {useEffect, useRef, useState} from "react"
import GlobalPageContainer from "@/components/PageContainer";
import ExProTable from "@/components/Table/ExtProTable";
import {dailyBankUsingGET} from "@/services/swagger/reportWeb";
import moment from "moment";
import {listGroupByCurrencyNameUsingGET} from "@/services/swagger/currencyWeb";
import commonColumn from "@/pages/Report/commonColumn";
import commonNumberColumn from "@/pages/Report/commonNumberColumn";
import {useModel} from "umi";

const DEFAULT_CURRENCY = [
  {label:'原币和本位币', value:''},
  {label:'本位币', value:'人民币'},
]
export default function () {
  const actionRef = useRef()
  const {bankSubjects} = useModel('useSubjectModel')
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
      hideInTable: true, initialValue: bankSubjects[0]?.id, fieldProps: {
        allowClear: false,
        options: bankSubjects.map(sub => ({label: sub.name, value: sub.id}))
      }, search: bankSubjects.length > 1
    },
    {
      title: "币别", dataIndex: "currencyName", valueType: "select",
      hideInTable: true, initialValue: "人民币", fieldProps: {
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
    commonColumn("摘要", "summary"),
    commonNumberColumn("昨日余额", "localBalanceOfYesterday"),
    commonNumberColumn("今日借方", "localDebitAmountOfToday"),
    commonNumberColumn("今日贷方", "localCreditAmountOfToday"),
    commonNumberColumn("今日余额", "localBalanceOfToday"),
    commonNumberColumn("借方笔数", "debitTotal"),
    commonNumberColumn("贷方笔数", "creditTotal")
  ]

  return (
    <GlobalPageContainer>
      <ExProTable actionRef={actionRef} columns={balanceColumns}
                  params={{subjectId : bankSubjects[0]?.id}}
                  pagination={false} size="small" bordered
                  toolBarRender={false}
                  scroll={{y: window.innerHeight - 250}}
                  request={async (params) => params.subjectId && dailyBankUsingGET(params)}
      />
    </GlobalPageContainer>
  )
}
