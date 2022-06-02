import React, {useEffect, useState} from "react";
import styled from "styled-components";
import {printContentOfExpenseBillUsingGET} from "@/services/swagger/expenseBillWeb";

export default ({dataSource, billId, print = {}}) => {
  const {prepare : printPrepare, params = {}} = print
  const id = billId || params.billId
  const [data, setData] = useState(dataSource || {})
  const loadBill = () => {
    if (!id) {
      return false
    }
    printContentOfExpenseBillUsingGET({id})
      .then(({data}) => setData(data))
      .then(() => printPrepare && print.print())
    return true
  }
  useEffect(() => {
    loadBill() || printPrepare && print.print()
  }, [id])

  const {items = []} = data
  return (
    <div style={{display: "none"}}>
      <div ref={print.ref}>
        <table width={1100} border="border"
               style={{margin: 8}}>
          <tbody>
          <tr>
            <TitleTh colSpan={13}>费用报销单</TitleTh>
          </tr>
          <tr>
            <LabelTh>报销人</LabelTh>
            <td>{data.expensePerson}</td>
            <LabelTh>职位</LabelTh>
            <td>{data.position}</td>
            <LabelTh>报销事由</LabelTh>
            <td colSpan={4}>
              {data.reason}
            </td>
            <LabelTh>单号</LabelTh>
            <td colSpan={3}>{data.number}</td>
          </tr>
          <tr>
            <LabelTh width={55}>序</LabelTh>
            <LabelTh width={100}>开始日期</LabelTh>
            <LabelTh width={100}>结束日期</LabelTh>
            <LabelTh width={120}>出差起讫地点</LabelTh>
            <LabelTh width={125}>费用名称</LabelTh>
            <LabelTh width={150}>摘要</LabelTh>
            <LabelTh width={65}>票据张数</LabelTh>
            <LabelTh width={65}>票据金额</LabelTh>
            <LabelTh width={65}>实报金额</LabelTh>
            <LabelTh width={65}>补助金额</LabelTh>
            <LabelTh width={65}>小计金额</LabelTh>
            <LabelTh>备注</LabelTh>
          </tr>
          {items.map((item, index) => (
            <tr key={item.id}>
              <td align="center">{index + 1}</td>
              <td>{item.beginTime}</td>
              <td>{item.endTime}</td>
              <td>{item.travelPlace}</td>
              <td>{item.name}</td>
              <td>{item.summary}</td>
              <td>{item.numOfBill}</td>
              <td>{item.billAmount}</td>
              <td>{item.actualAmount}</td>
              <td>{item.subsidyAmount}</td>
              <td>{item.subtotalAmount}</td>
              <td>{item.remark}</td>
            </tr>
          ))}
          <tr>
            <LabelTh>合计</LabelTh>
            <td colSpan={9}/>
            <td>{data.totalSubsidyAmount}</td>
            <td></td>
          </tr>
          <tr>
            <LabelTh>填报人</LabelTh>
            <td colSpan={3}>{data.creatorName}</td>
            <LabelTh>报销日期</LabelTh>
            <td colSpan={2}>{data.expenseTime}</td>
            <LabelTh>填报日期</LabelTh>
            <td colSpan={4}>{data.createTime || "系统生成"}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  )
}

const TitleTh = styled.th`
  font-size: 22px;
  font-weight: bold;
  text-align: center;
  margin: 0;
`
const LabelTh = styled.th`
  font-size: 14px;
`
