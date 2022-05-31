import React, {useRef} from "react"
import PageContainer from "@/components/PageContainer"
import ExProTable from "@/components/Table/ExtProTable"
import {pageVoucherBookUsingGET} from "@/services/swagger/voucherWeb";
import moment from "moment";

const yearMonthNumTransform = v => ({
  yearMonthNum: moment(v).format("YYYYMM")
})

export default () => {
  const actionRef = useRef()
  const columns = [
    {
      title: "年-月", dataIndex: "yearMonthNum", search: {transform: yearMonthNumTransform},
      valueType: "dateMonth", fieldProps: {defaultValue:moment()}, hideInTable: true
    },
    {
      title: "科目编号", dataIndex: "subjectNumber", search: false
    },
    {
      title: "凭证日期", dataIndex: "voucherTime", search: false
    },
    {
      title: "凭证单号", dataIndex: "serialNumber", search: false
    },
    {
      title: "摘要", dataIndex: "summary", search: false
    },
    {
      title: "科目名称", dataIndex: "subjectName", search: false
    },
    {
      title: "借方金额", dataIndex: "debitAmount", search: false
    },
    {
      title: "贷方金额", dataIndex: "creditAmount", search: false
    }
  ]
  return (
    <PageContainer>
      <ExProTable actionRef={actionRef} columns={columns}
                  request={(params) => {
                    const {yearMonthNum = moment().format("YYYYMM")} = params
                    return pageVoucherBookUsingGET({
                      ...params, yearMonthNum
                    })
                  }}
                  editable={false}
      />
    </PageContainer>
  )
}
