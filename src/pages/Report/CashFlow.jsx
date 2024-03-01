import React from "react"
import GlobalPageContainer from "@/components/PageContainer";
import ProTable from "@ant-design/pro-table";
import {profitUsingGET} from "@/services/swagger/reportWeb";
import {useSecurity} from "@/utils/hooks";
import dayjs from "dayjs";
import NameColumn from "@/pages/Report/NameColumn";
import RowNumColumn from "@/pages/Report/RowNumColumn";
import NumberColumn from "@/pages/Report/NumberColumn";

export default function() {
  const {proxyCustomer} = useSecurity()
  const {enablePeriod, currentPeriod} = proxyCustomer
  const columns = [
    {
      dataIndex: "yearMonth", title: "", valueType: "dateMonth",
      initialValue: dayjs(`${proxyCustomer.currentPeriod}`, "YYYYMM").format("YYYY-MM"),
      fieldProps: {
        allowClear: false,
        disabledDate: date => {
          return date < dayjs(`${enablePeriod}`, "YYYYMM").endOf("day")
            ||
            date > dayjs(`${currentPeriod}`, "YYYYMM").endOf("day")
        }
      }, hideInTable: true
    },
    NameColumn("name", "项目", "level", {width: 200}),
    RowNumColumn("rowNum", {align: "center", width: 60}),
    NumberColumn("annualAmount", "本年累计金额", {align: "right"}),
    NumberColumn("currentPeriodAmount", "本期金额", {align: "right"}),
  ]
  return (
    <GlobalPageContainer>
      <ProTable columns={columns} rowKey="reportId"
                request={profitUsingGET} bordered pagination={false}
                toolBarRender={false} scroll={{y: 650}}
      />
    </GlobalPageContainer>
  )
}
