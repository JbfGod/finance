import React, {useEffect, useMemo, useState} from "react";
import {AutoComplete, Input, Modal} from "antd";
import styled from "styled-components";
import {debounce} from "lodash";
import {previewExpenseBillByIdUsingGET} from "@/services/swagger/expenseBillWeb";

export default ({dataSource, billId, ...props}) => {
  const [data, setData] = useState(dataSource || {})
  const loadBillPreview = () => {
    previewExpenseBillByIdUsingGET({id: billId})
      .then(({data}) => setData(data))
  }
  useEffect(() => {
    billId && loadBillPreview()
  }, [billId])

  if (!props.visible) {
    return null
  }

  const {items = []} = data
  return (
    <Modal width="100%" {...props}>
      <table width="1350" border="border">
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
          <td colSpan={5}>
            {data.reason}
          </td>
          <LabelTh>单号</LabelTh>
          <td colSpan={2}>自动生成</td>
        </tr>
        <tr>
          <LabelTh width={55}>序</LabelTh>
          <LabelTh width={100}>开始日期</LabelTh>
          <LabelTh width={100}>结束日期</LabelTh>
          <LabelTh width={150}>出差起讫地点</LabelTh>
          <LabelTh width={150}>费用名称</LabelTh>
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
            <td colSpan={2}>{item.startTime}</td>
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
          <td>系统生成</td>
          <td colSpan={2}/>
          <LabelTh>报销日期</LabelTh>
          <td colSpan={2}>{data.expenseTime}</td>
          <LabelTh>填报日期</LabelTh>
          <td colSpan={4}>{data.createTime || "系统生成"}</td>
        </tr>
        </tbody>
      </table>
    </Modal>
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
