import React, {useEffect, useState} from "react";
import styled from "styled-components";
import {printContentOfVoucherUsingGET} from "@/services/swagger/voucherWeb";
import {CURRENCY_TYPE, LENDING_DIRECTION} from "@/constants";
import {convertCurrency} from "@/utils/common";
import styles from "@/pages/VoucherList/index.less";

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
      <div ref={print.ref} style={{padding: 8, margin: "0 auto", width: isLocalCurrency ? 800 : 1100}}>
        <h2 style={{textAlign: "center", margin: 0}}>记账凭证单</h2>
        <div className="ant-row ant-row-space-between">
          <div>凭证号：{data.serialNumber}</div>
          <div>凭证日期：{data.voucherDate}</div>
          <div>核算单位：{data.customerName}</div>
          <div>币别：{isLocalCurrency ? '人民币' : data.currencyName}</div>
        </div>
        <table width="100%" border="border">
          {isLocalCurrency ? (
            <tbody>
            <tr>
              <LabelTh width={180} colSpan={2}>摘要</LabelTh>
              <LabelTh width={220} colSpan={2}>科目</LabelTh>
              <LabelTh width={200}>借方金额</LabelTh>
              <LabelTh width={200}>贷款金额</LabelTh>
            </tr>
            {(data.items || []).map(item => {
              const isLoan = item.lendingDirection === LENDING_DIRECTION.LOAN.value
              return (
                <tr key={item.id}>
                  <CenterTd colSpan={2}>{item.summary}</CenterTd>
                  <CenterTd colSpan={2}>{item.subjectNumber}-{item.subjectName}</CenterTd>
                  <CenterTd>{isLoan ? "" : item.amount}</CenterTd>
                  <CenterTd>{isLoan ? item.amount : ""}</CenterTd>
                </tr>
              )
            })}
            <tr>
              <LeftTh colSpan={2} align="left">附件数：{data.attachmentNum || 0}张</LeftTh>
              <LeftTh colSpan={4} align="left">合计：{convertCurrency(data.totalLocalCurrencyAmount || "")}</LeftTh>
            </tr>
            </tbody>
          ) : (
            <tbody>
            <tr>
              <LabelTh width={175} rowSpan={2} colSpan={2}>摘要</LabelTh>
              <LabelTh width={200} rowSpan={2} colSpan={2}>科目</LabelTh>
              <LabelTh colSpan={3}>借方金额</LabelTh>
              <LabelTh colSpan={3}>贷款金额</LabelTh>
            </tr>
            <tr>
              <LabelTh>原币金额</LabelTh>
              <LabelTh width={50}>汇率</LabelTh>
              <LabelTh>本币金额</LabelTh>
              <LabelTh>原币金额</LabelTh>
              <LabelTh width={50}>汇率</LabelTh>
              <LabelTh>本币金额</LabelTh>
            </tr>
            {(data.items || []).map(item => {
              const isLoan = item.lendingDirection === LENDING_DIRECTION.LOAN.value
              return (
                <tr key={item.id}>
                  <CenterTd colSpan={2}>{item.summary}</CenterTd>
                  <CenterTd colSpan={2}>{item.subjectNumber}-{item.subjectName}</CenterTd>
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
              <LeftTh colSpan={8} align="left">合计：{convertCurrency(data.totalLocalCurrencyAmount || "")}</LeftTh>
            </tr>
            </tbody>
          )}
        </table>
        <div className="ant-row ant-row-space-between">
          <div className={styles.signNameTitle}>财务主管：</div>
          <div className={styles.signNameTitle}>记账：</div>
          <div className={styles.signNameTitle}>复核：</div>
          <div className={styles.signNameTitle}>制单：</div>
        </div>
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
  font-weight: normal;
  font-size: 14px;
`
const LeftTh = styled.th`
font-weight: normal;
  text-align: left;
  font-size: 14px;
`
const CenterTd = styled.td`
  text-align: center;
`
