import React from "react"
import GlobalPageContainer from "@/components/PageContainer";
import ProTable from "@ant-design/pro-table";
import {assetsLiabilitiesUsingGET} from "@/services/swagger/reportWeb";
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
    NameColumn("assetName", "资产", "assetLevel", {width: 200}),
    RowNumColumn("assetRowNum", {align: "center", width: 60}),
    NumberColumn("assetEndBalance", "期末数", {align: "right"}),
    NumberColumn("assetBeginBalance", "年初数", {align: "right"}),
    NameColumn("liabilityName", "负债和所有者（或股东）权益", "assetLevel", {width: 270}),
    RowNumColumn("liabilityRowNum", {align: "center", width: 60}),
    NumberColumn("liabilityEndBalance", "期末数", {align: "right"}),
    NumberColumn("liabilityBeginBalance", "年初数", {align: "right"}),
  ]
  return (
    <GlobalPageContainer>
      <ProTable columns={columns} rowKey="assetReportId"
                request={assetsLiabilitiesUsingGET} bordered pagination={false}
                toolBarRender={false} scroll={{y: 650}}
      />
    </GlobalPageContainer>
  )
}
