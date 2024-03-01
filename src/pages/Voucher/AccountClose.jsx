import {Button, Card, Space, Typography} from "antd";
import moment from "moment";
import React, {useEffect, useState} from "react";
import GlobalPageContainer from "@/components/PageContainer";
import {currentPeriodOutlineOfVoucherUsingGET} from "@/services/swagger/voucherWeb";
import {closingToNextPeriodUsingPUT, unClosingToPrevPeriodUsingPUT} from "@/services/swagger/settlementWeb";

const {Title} = Typography
export default function AccountClose() {
  const [currentOutlineOfVoucher, setCurrentOutlineOfVoucher] = useState({})
  const {voucherTotal, yearMonthNum} = currentOutlineOfVoucher
  const loadCurrentYearMonth = () => {
    currentPeriodOutlineOfVoucherUsingGET().then(({data}) => setCurrentOutlineOfVoucher(data))
  }
  useEffect(() => {
    loadCurrentYearMonth()
  }, [])
  return (
    <GlobalPageContainer>
      <Card>
        <Typography>
          <Title level={3} style={{ margin: 0 }}>{moment(yearMonthNum, "YYYYMM").format("YYYY年MM期")}</Title>
          <Title level={5} style={{ margin: 0 }}>共录入凭证{voucherTotal}张</Title>
          <Space>
            <Button size="small" onClick={() => unClosingToPrevPeriodUsingPUT().then(loadCurrentYearMonth)}>反结账到上一期</Button>
            <Button size="small" onClick={() => closingToNextPeriodUsingPUT().then(loadCurrentYearMonth)}>结账到下一期</Button>
          {/*  <Button size="small">批量反结账</Button>
            <Button size="small">批量结账</Button>*/}
          </Space>
        </Typography>
      </Card>
    </GlobalPageContainer>
  )
}
