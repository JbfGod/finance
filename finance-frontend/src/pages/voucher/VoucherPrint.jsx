import React, {useEffect, useState} from "react";
import styled from "styled-components";
import {printContentOfExpenseBillUsingGET} from "@/services/swagger/expenseBillWeb";
import {printContentOfVoucherUsingGET} from "@/services/swagger/voucherWeb";
import {CURRENCY_TYPE, LENDING_DIRECTION} from "@/constants";
import {convertCurrency} from "@/utils/common";

export default ({dataSource, voucherId, print = {}}) => {
  const {prepare: printPrepare, params = {}} = print
  const id = voucherId || params.voucherId
  const currencyType = params.currencyType || CURRENCY_TYPE.LOCAL
  const isLocalCurrency = currencyType === CURRENCY_TYPE.LOCAL
  const [data, setData] = useState(dataSource || {})
  const loadVoucher = () => {
    if (!id) {
      return false
    }
    printContentOfVoucherUsingGET({id})
      .then(({data}) => setData(data))
      .then(() => printPrepare && print.print())
    return true
  }
  useEffect(() => {
    loadVoucher() || printPrepare && print.print()
  }, [id])

  return (
    <div style={{display: "none"}}>
      <div ref={print.ref} style={{padding: 8}}>
        <table width={isLocalCurrency ? 800 : 1100} border="border" style={{margin: "0 auto"}}>
          {isLocalCurrency ? (
            <tbody>
            <tr>
              <TitleTh colSpan={6}>记账凭证单</TitleTh>
            </tr>
            <tr>
              <td colSpan={4}></td>
              <LabelTh>本币:人民币</LabelTh>
              <LabelTh>单位:元</LabelTh>
            </tr>
            <tr>
              <LeftTh colSpan={2}>客户核算单：{data.customerName}</LeftTh>
              <LeftTh colSpan={2}>凭证日期：{data.voucherTime}</LeftTh>
              <td></td>
              <LeftTh>凭证号：{data.serialNumber}</LeftTh>
            </tr>
            <tr>
              <LabelTh width={200} colSpan={2}>摘要</LabelTh>
              <LabelTh width={150} colSpan={2}>科目</LabelTh>
              <LabelTh width={200}>借方金额</LabelTh>
              <LabelTh width={200}>贷款金额</LabelTh>
            </tr>
            {(data.items || []).map(item => {
              const isLoan = item.lendingDirection === LENDING_DIRECTION.LOAN.value
              return (
                <tr key={item.id}>
                  <CenterTd colSpan={2}>{item.summary}</CenterTd>
                  <CenterTd colSpan={2}>{item.subjectName}</CenterTd>
                  <CenterTd>{isLoan ? "" : item.amount}</CenterTd>
                  <CenterTd>{isLoan ? item.amount : ""}</CenterTd>
                </tr>
              )
            })}
            <tr>
              <LeftTh colSpan={2} align="left">附件数：{data.attachmentNum || 0}张</LeftTh>
              <LeftTh colSpan={2} align="left">合计：{convertCurrency(data.totalLocalCurrencyAmount || "")}</LeftTh>
              <td colSpan={2}></td>
            </tr>
            <tr>
              <LeftTh align="left" colSpan={2}>财务主管：</LeftTh>
              <LeftTh align="left" colSpan={2}>记账：</LeftTh>
              <LeftTh align="left">复核：</LeftTh>
              <LeftTh align="left">制单：</LeftTh>
            </tr>
            </tbody>
          ) : (
            <tbody>
            <tr>
              <TitleTh colSpan={10}>记账凭证单</TitleTh>
            </tr>
            <tr>
              <td colSpan={6}></td>
              <LabelTh>币种</LabelTh>
              <CenterTd>{data.currencyName}</CenterTd>
              <LabelTh>单位</LabelTh>
              <CenterTd>{data.unit}</CenterTd>
            </tr>
            <tr>
              <LeftTh align="left" colSpan={2}>客户核算单：{data.customerName}</LeftTh>
              <td colSpan={4}></td>
              <LabelTh>凭证日期</LabelTh>
              <CenterTd>{data.voucherTime}</CenterTd>
              <LabelTh>凭证号</LabelTh>
              <CenterTd>{data.serialNumber}</CenterTd>
            </tr>
            <tr>
              <LabelTh width={200} rowSpan={2} colSpan={2}>摘要</LabelTh>
              <LabelTh width={150} rowSpan={2} colSpan={2}>科目</LabelTh>
              <LabelTh width={200} colSpan={3}>借方金额</LabelTh>
              <LabelTh width={200} colSpan={3}>贷款金额</LabelTh>
            </tr>
            <tr>
              <LabelTh width={100}>原币金额</LabelTh>
              <LabelTh width={120}>汇率</LabelTh>
              <LabelTh width={100}>本币金额</LabelTh>
              <LabelTh width={100}>原币金额</LabelTh>
              <LabelTh width={120}>汇率</LabelTh>
              <LabelTh width={100}>本币金额</LabelTh>
            </tr>
            {(data.items || []).map(item => {
              const isLoan = item.lendingDirection === LENDING_DIRECTION.LOAN.value
              return (
                <tr key={item.id}>
                  <CenterTd colSpan={2}>{item.summary}</CenterTd>
                  <CenterTd colSpan={2}>{item.subjectName}</CenterTd>
                  <CenterTd>{isLoan ? "" : item.amount}</CenterTd>
                  <CenterTd>{isLoan ? "" : item.rate}</CenterTd>
                  <CenterTd>{isLoan ? "" : item.localAmount}</CenterTd>
                  <CenterTd>{isLoan ? item.amount : ""}</CenterTd>
                  <CenterTd>{isLoan ? item.rate : ""}</CenterTd>
                  <CenterTd>{isLoan ? item.localAmount : ""}</CenterTd>
                </tr>
              )
            })}
            <tr>
              <LeftTh colSpan={2} align="left">附件数：{data.attachmentNum || 0}张</LeftTh>
              <LeftTh colSpan={2} align="left">合计：{convertCurrency(data.totalLocalCurrencyAmount)}</LeftTh>
              <td colSpan={6}></td>
            </tr>
            <tr>
              <LeftTh align="left" colSpan={2}>财务主管：</LeftTh>
              <td colSpan={2}></td>
              <LeftTh align="left" colSpan={2}>记账：</LeftTh>
              <LeftTh align="left" colSpan={2}>复核：</LeftTh>
              <LeftTh align="left" colSpan={2}>制单：</LeftTh>
            </tr>
            </tbody>
          )}

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
const LeftTh = styled.th`
  text-align: left;
  font-size: 14px;
`
const CenterTd = styled.td`
  text-align: center;
`
