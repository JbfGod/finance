import React from "react"
import {DatePicker} from "antd";
import {useSecurity} from "@/utils/hooks";
import dayjs from 'dayjs'

const {RangePicker} = DatePicker

export default function (props) {
  const {proxyCustomer} = useSecurity()
  const {currentPeriod, enablePeriod} = proxyCustomer || {}
  return (
    <RangePicker picker="month" format="YYYY年MM期"
                 disabledDate={date => {
                   return date < dayjs(`${enablePeriod}`, "YYYYMM").endOf("day")
                     ||
                     date > dayjs(`${currentPeriod}`, "YYYYMM").endOf("day")
                 }}
                 monthCellRender={(date) => {
                   return (
                     <div className="ant-picker-cell-inner">
                       {date.month() + 1}期
                     </div>
                   );
                 }} {...props}/>
  )
}
