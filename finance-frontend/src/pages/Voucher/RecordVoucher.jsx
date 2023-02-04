import React, {useEffect, useMemo, useState} from "react"
import PageContainer from "@/components/PageContainer"
import {Button, Card, Col, Form, Input, InputNumber, message, Popconfirm, Row, Space} from "antd"
import styled from "styled-components";
import {CaretLeftOutlined, CaretRightOutlined, CloseOutlined, LoadingOutlined, PlusOutlined} from "@ant-design/icons";
import {searchExpenseItemCueUsingGET} from "@/services/swagger/expenseBillWeb";
import AutoCompleteInput from "@/components/Common/AutoCompleteInput";
import {AdvancedSubjectSelect} from "@/components/AdvancedSubjectSelect";
import {ProFormDatePicker, ProFormItem} from "@ant-design/pro-form";
import {convertCurrency} from "@/utils/common";
import {useSecurity} from "@/utils/hooks";
import moment from "moment";
import {
  addVoucherUsingPOST, auditingVoucherUsingPUT, deleteVoucherUsingDELETE,
  firstVoucherDetailUsingGET,
  lastVoucherDetailUsingGET,
  nextVoucherDetailUsingGET,
  prevVoucherDetailUsingGET, unAuditingVoucherUsingPUT, updateVoucherUsingPUT,
  usableSerialNumberUsingGET
} from "@/services/swagger/voucherWeb";
import {useModel} from "@umijs/max";
import dayjs from "dayjs";

const separatingDecimal = (float) => {
  const floatStr = Math.abs(Number(float || 0)).toFixed(3)
  let [integer, decimal] = floatStr.split(".")
  if (Number(float || 0)) {
    decimal = !Number(decimal) ? ["0", "0"] : decimal
    integer = integer.split("").reverse()
  } else {
    integer = ""
    decimal = null
  }
  return {integer, decimal}
}

export default () => {
  const [voucher, setVoucher] = useState(null)
  const [savingLoading, setSavingLoading] = useState(false)
  const [loading, setLoading] = useState(false)
  const isView = voucher?.reviewed || voucher?.closed
  const fillVoucher = ({data}) => {
    if (data && data.items.length < 5) {
      data.items.push(...Array(5 - data.items.length).fill(1).map(() => ({})))
    }
    setVoucher(data)
  }
  const {subjectById} = useModel("useSubjectModel")
  const {proxyCustomer} = useSecurity()
  const [form] = Form.useForm()
  const items = Form.useWatch(["items"], form) || []
  const debitAndCreditAmount = useMemo(() => {
    const balance = items.reduce((total, curr) => {
      total[0] += (Number(curr.debitAmount || 0) * 100)
      total[1] += (Number(curr.creditAmount || 0) * 100)
      return total
    }, [0, 0])
    balance[0] /= 100
    balance[1] /= 100
    return balance
  }, [items])
  const total = debitAndCreditAmount[0] === debitAndCreditAmount[1] ? debitAndCreditAmount[0] : 0

  const onNew = () => {
    form.setFieldsValue({
      attachmentNum: 0,
      voucherDate: dayjs(`${proxyCustomer.currentPeriod}`, "YYYYMM")
        .endOf('months'),
      items: Array(5).fill(1).map(() => ({}))
    })
    usableSerialNumberUsingGET().then(({data}) => form.setFieldValue("serialNumber", data))
    setVoucher(null)
  }
  const onSave = (append = false) => {
    let values = form.getFieldsValue()
    const {serialNumber} = values
    let errorMsg = ""
    if (!serialNumber) {
      errorMsg = "凭证字号不能为空或0"
    } else if (!items[0]?.summary) {
      errorMsg = "第一条分录摘要不能为空"
    } else if (debitAndCreditAmount[0] !== debitAndCreditAmount[1]) {
      errorMsg = "录入凭证借贷不平"
    } else {
      values.items.findIndex((item, idx) => {
        if ((item.debitAmount != null || item.creditAmount != null) && item.subjectId == null) {
          errorMsg = `第${idx + 1}分录科目不能为空`
        } else if (item.subjectId && !item.debitAmount && !item.creditAmount) {
          errorMsg = `第${idx + 1}分录金额不能为空或零`
        }
      })
    }
    if (errorMsg) {
      message.warn(errorMsg)
      return
    }
    values = {...values, items: values.items.filter(item => item.subjectId)
        .map(item => ({...item, subjectNumber: subjectById[item.subjectId]?.number}))
    }
    setSavingLoading(true)
    if (voucher) {
      updateVoucherUsingPUT({...voucher, ...values}).then(append?onNew : fillVoucher).finally(() => setSavingLoading(false))
    } else {
      addVoucherUsingPOST({...values}).then(append?onNew : fillVoucher).finally(() => setSavingLoading(false))
    }
  }
  useEffect(() => {
    console.log("asdhasjkdhaksjd")
    onNew()
  }, [])
  useEffect(() => {
    if (voucher) {
      form.setFieldsValue(voucher)
    }
  }, [voucher])
  useEffect(() => {
    setLoading(savingLoading)
  }, [savingLoading])
  return (
    <PageContainer>
      <Card bodyStyle={{padding: 8}}>
        <Row justify="space-between">
          <Col>
            <Space>
              <Button onClick={() => firstVoucherDetailUsingGET({period: voucher?.yearMonthNum}).then(fillVoucher)}>首张</Button>
              <Button onClick={() => {
                if (!voucher) {
                  return lastVoucherDetailUsingGET({period: voucher?.yearMonthNum}).then(fillVoucher)
                }
                prevVoucherDetailUsingGET({serialNumber: voucher.serialNumber, period: voucher?.yearMonthNum}).then(fillVoucher)
              }}><CaretLeftOutlined/></Button>
              <Button onClick={() => {
                if (!voucher) {
                  message.warn("没有下一张凭证")
                  return
                }
                nextVoucherDetailUsingGET({serialNumber: voucher.serialNumber, period: voucher?.yearMonthNum}).then(fillVoucher)
              }}><CaretRightOutlined/></Button>
              <Button onClick={() => lastVoucherDetailUsingGET({period: voucher?.yearMonthNum}).then(fillVoucher)}>末张</Button>
            </Space>
          </Col>
          <Col>
            <Space>
              {!!voucher && (
                <Button type="primary" onClick={onNew}>新增</Button>
              )}
              <Button loading={savingLoading} type="primary" onClick={() => onSave(true)}>保存并新增</Button>
              <Button loading={savingLoading} onClick={() => onSave()}>保存</Button>
              {voucher && voucher.closed === false && (
                voucher.reviewed?(
                  <Button loading={savingLoading} onClick={() => {
                    unAuditingVoucherUsingPUT({id: voucher.id}).then(() => setVoucher({...voucher, reviewed: false}))
                  }}>反审核</Button>
                ) : (
                  <Button loading={savingLoading} onClick={() => {
                    auditingVoucherUsingPUT({id: voucher.id}).then(() => setVoucher({...voucher, reviewed: true}))
                  }}>审核</Button>
                )
              )}
              {!!voucher && (
                <Popconfirm title="你确认要删除该凭证吗？" onConfirm={() => {
                  return deleteVoucherUsingDELETE({id: voucher.id})
                    .then(res => {
                      if (res.data) {
                        fillVoucher(res)
                      } else {
                        onNew()
                      }
                    })
                }}>
                  <Button>删除</Button>
                </Popconfirm>
              )}
            </Space>
          </Col>
        </Row>
      </Card>
      <Form form={form} disabled={isView}>
        <VoucherBox>
          {loading && (
            <Mask><LoadingOutlined style={{fontSize: 42}}/></Mask>
          )}
          <VoucherHead>
            {voucher?.closed ? (
              <img className="seal" src="/accountClosed.png"/>
            ) : voucher?.reviewed ? (
              <img className="seal" src="/reviewed.png"/>
            ) : null}
            <VoucherTitle>
              <p>
                记账凭证<label>{moment(proxyCustomer.currentPeriod, "YYYYMM").format("YYYY年第MM期")}</label>
              </p>
            </VoucherTitle>
            <Row justify="space-between">
              <Col style={{height: 42}}>
                <span style={{marginRight: 5}}>凭证字：记</span>
                <Form.Item name="serialNumber" noStyle>
                  <Input className="underline" onKeyPress={e => /[^[\d]/.test(e.key) && e.preventDefault()} style={{
                    width: 50, fontSize: 13, borderBottom: "1px solid #DCDFE6",
                    height: 32, lineHeight: 32, textAlign: "center"
                  }} bordered={false}/>
                </Form.Item>
                <span>号</span>
                <VoucherDateBox>日期：
                  <ProFormDatePicker noStyle name="voucherDate" label="" placeholder=""
                                     fieldProps={{bordered: false, allowClear: false}}/>
                </VoucherDateBox>
              </Col>
              <Col>
                <span>附单据</span>
                <Form.Item name="attachmentNum" noStyle>
                  <Input className="underline" onKeyPress={e => /[^[\d]/.test(e.key) && e.preventDefault()} style={{
                    width: 50, fontSize: 13,
                    height: 32, lineHeight: 32, textAlign: "center"
                  }} bordered={false}/>
                </Form.Item>
                <span>张</span>
              </Col>
            </Row>
          </VoucherHead>
          <Table>
            <div className="head">
              <div>摘要</div>
              <div>会计科目</div>
              {[{title: "借方金额"}, {title: "贷方金额"}].map(row => (
                <div key={row.title}>
                  <div className="thbox" style={{height: 34}}>
                    <p>{row.title}</p>
                    <div style={{height: 30, lineHeight: "30px"}}>
                      <div className="companyItem">十</div>
                      <div className="companyItem">亿</div>
                      <div className="companyItem">千</div>
                      <div className="companyItem" style={{borderRight: "1.5px solid rgb(189, 226, 245)"}}>百</div>
                      <div className="companyItem">十</div>
                      <div className="companyItem">万</div>
                      <div className="companyItem" style={{borderRight: "1.5px solid rgb(189, 226, 245)"}}>千</div>
                      <div className="companyItem">百</div>
                      <div className="companyItem">十</div>
                      <div className="companyItem" style={{borderRight: "1.5px solid rgb(243, 211, 211)"}}>元</div>
                      <div className="companyItem">角</div>
                      <div className="companyItem" style={{borderRight: "none"}}>分</div>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            <BodyBox>
              <Form.List name="items">
                {(fields, {add, remove}) => fields.map((field, idx) => {
                  const tmpItems = form.getFieldValue(["items"])
                  return (
                    <div key={field.key} className="amountTr" onClick={() => {
                      !isView && idx === fields.length - 1 && add({}, idx + 1)
                    }}>
                      <div className="headExtra">
                        <Button type="link" size="small" style={{padding: 0}} onClick={e => {
                          e.stopPropagation()
                          add({}, idx + 1)
                        }}>
                          <PlusOutlined size="small"/>
                        </Button>
                        <Button type="link" size="small" style={{padding: 0}} onClick={e => {
                          e.stopPropagation()
                          if (fields.length === 2) {
                            message.warn("至少保留两条分录!")
                            return
                          }
                          remove(idx)
                        }}>
                          <CloseOutlined size="small"/>
                        </Button>
                      </div>
                      <div style={{position: "relative"}}>
                        <span className="serialNumber">{idx + 1}</span>
                        <Form.Item name={[field.name, "summary"]} noStyle>
                          <AutoCompleteInput request={(keyword) => {
                            return searchExpenseItemCueUsingGET({column: 'SUMMARY', keyword})
                          }} onFocus={(e) => {
                            if (!e.target.value && idx !== 0) {
                              form.setFieldValue(["items", field.name, "summary"], tmpItems[idx - 1].summary)
                            }
                          }}/>
                        </Form.Item>
                      </div>
                      <div>
                        <Form.Item name={[field.name, "subjectId"]} noStyle>
                          <AdvancedSubjectSelect balanceItems={tmpItems} placeholder="只能选择费用类科目"
                                                 onlySelectedLeaf={true}/>
                        </Form.Item>
                      </div>
                      {[{title: "借方金额", name: "debitAmount"}, {title: "贷方金额", name: "creditAmount"}].map(row => {
                        const amount = tmpItems[field.name][row.name]
                        const {integer, decimal} = separatingDecimal(amount)
                        return (
                          <AmountItem key={row.title} className={amount < 0 ? "minus" : ""}>
                            <div style={{height: 30, lineHeight: "60px"}}>
                              <div className="companyItem">{integer[9] || ""}</div>
                              <div className="companyItem">{integer[8] || ""}</div>
                              <div className="companyItem">{integer[7] || ""}</div>
                              <div className="companyItem"
                                   style={{borderRight: "1.5px solid rgb(189, 226, 245)"}}>{integer[6] || ""}</div>
                              <div className="companyItem">{integer[5] || ""}</div>
                              <div className="companyItem">{integer[4] || ""}</div>
                              <div className="companyItem"
                                   style={{borderRight: "1.5px solid rgb(189, 226, 245)"}}>{integer[3] || ""}</div>
                              <div className="companyItem">{integer[2] || ""}</div>
                              <div className="companyItem">{integer[1] || ""}</div>
                              <div className="companyItem"
                                   style={{borderRight: "1.5px solid rgb(243, 211, 211)"}}>{integer[0] || ""}</div>
                              <div className="companyItem">{decimal?.[0] || ""}</div>
                              <div className="companyItem" style={{borderRight: "none"}}>{decimal?.[1] || ""}</div>
                            </div>
                            <ProFormItem name={[field.name, row.name]} noStyle>
                              <AmountInput shortcutEvent={{
                                "=": ({value, triggerChange}) => {
                                  const isDebit = row.name === "debitAmount"
                                  const balance = tmpItems.reduce((total, curr, idx) => {
                                    if (idx === field.name) {
                                      return total
                                    }
                                    return total + ((curr.debitAmount || 0) * 100 - (curr.creditAmount || 0) * 100)
                                  }, 0) / 100
                                  triggerChange(isDebit ? -balance : balance)
                                }
                              }} onBlur={(e) => {
                                if (!Number(e.target.value)) {
                                  return
                                }
                                const isDebit = row.name === "debitAmount"
                                if (isDebit) {
                                  Number(tmpItems[field.name].creditAmount) && form.setFieldValue(["items", field.name, "creditAmount"], 0)
                                } else {
                                  Number(tmpItems[field.name].debitAmount) && form.setFieldValue(["items", field.name, "debitAmount"], 0)
                                }
                              }}/>
                            </ProFormItem>
                          </AmountItem>
                        )
                      })}
                    </div>
                  )
                })}
              </Form.List>
            </BodyBox>
            <FootBox>
              <SummaryTotal>合计：{total ? convertCurrency(total) : ""}</SummaryTotal>
              {[{title: "借方金额"}, {title: "贷方金额"}].map((row, idx) => {
                const {integer, decimal} = debitAndCreditAmount[idx] ?
                  separatingDecimal(debitAndCreditAmount[idx])
                  :
                  {integer: 0, decimal: 0}
                return (
                  <div key={row.title} style={{height: 60, lineHeight: "60px"}}>
                    <div className="companyItem">{integer[9] || ""}</div>
                    <div className="companyItem">{integer[8] || ""}</div>
                    <div className="companyItem">{integer[7] || ""}</div>
                    <div className="companyItem"
                         style={{borderRight: "1.5px solid rgb(189, 226, 245)"}}>{integer[6] || ""}</div>
                    <div className="companyItem">{integer[5] || ""}</div>
                    <div className="companyItem">{integer[4] || ""}</div>
                    <div className="companyItem"
                         style={{borderRight: "1.5px solid rgb(189, 226, 245)"}}>{integer[3] || ""}</div>
                    <div className="companyItem">{integer[2] || ""}</div>
                    <div className="companyItem">{integer[1] || ""}</div>
                    <div className="companyItem"
                         style={{borderRight: "1.5px solid rgb(243, 211, 211)"}}>{integer[0] || ""}</div>
                    <div className="companyItem">{decimal?.[0] || ""}</div>
                    <div className="companyItem" style={{borderRight: "none"}}>{decimal?.[1] || ""}</div>
                  </div>
                )
              })}
            </FootBox>
          </Table>
          <CreatorBox>
            制单人：姜工
            {voucher?.createTime && (
              <span style={{marginLeft: 30}}>生成凭证时间：{voucher?.createTime}</span>
            )}
          </CreatorBox>
        </VoucherBox>
      </Form>
    </PageContainer>
  )
}

function AmountInput({value, onChange, shortcutEvent = {}, ...props}) {
  const equalEvent = shortcutEvent["="]
  const triggerChange = v => {
    onChange?.(v)
  }
  const onKeyPress = e => {
    if (/[^\.\d-]/.test(e.key)) {
      e.preventDefault()
    }
    if (e.key === "=") {
      equalEvent?.({value, triggerChange})
    }
  }
  return (
    <InputNumber precision={2} value={value} onChange={v => {
      triggerChange(v)
    }} className="input" onKeyPress={onKeyPress} {...props}/>
  )
}

const VoucherBox = styled.div`
  width: 1195px;
  margin: 0 auto;
  margin-top: 8px;
  padding: 35px 20px 15px 20px;
  box-sizing: border-box;
  border: 1px solid #e0e0e0;
  position: relative;
  background-color: #f7f7f7;
`

const Mask = styled.div`
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  background-color: rgba(255,255,255,.9);
  z-index: 999;
  display: inline-flex;
  justify-content: center;
  align-items: center;
`

const VoucherHead = styled.div`
  position: relative;
  font-size: 13px;
  .underline {
    border-bottom: 1px solid #DCDFE6 !important;
    &:focus {
      border-bottom-color: #409EFF !important;
    }
  }
  img.seal {
    position: absolute;
    right: 280px;
  }
`

const VoucherTitle = styled.div`
    position: absolute;
    width: 100%;
    top: -22px;
    p {
      text-align: center;
      position: relative;
      font-size: 25px;
    }
    label {
          position: absolute;
          font-size: 13px;
          top: 14px;
          margin-left: 15px;
    }
`

const VoucherDateBox = styled.div`
  margin-left: 20px;
  display: inline-flex;
  align-items: center;
  .ant-picker {
    border-bottom: 1px solid #DCDFE6 !important;
    padding-right: 0;
    &:focus-within {
      border-bottom-color: #409EFF !important;
    }
  }
`

const Table = styled.div`
  border: 1px solid #999999;
  width: 1151px;
  .head {
    display: grid;
    grid-template-columns: 260px 370px repeat(2, 260px);
    & > div {
      font-weight: normal;
      font-style: normal;
      font-size: 14px;
      color: #000;
      border-right: 1px solid #999999;
      border-bottom: 1px solid #999999;
      box-sizing: border-box;
      text-align: center;
      line-height: 60px;
    }
  }
  div.thbox {
    p {
      text-align: center;
      border-bottom: 1px solid rgb(153, 153, 153);
      height: 30px;
      line-height: 30px;
      margin: 0;
    }
  }
  .companyItem {
      float: left;
      text-align: center;
      width: 8.33333%;
      border-right: 1px solid #ebebeb;
      font-size: 13px;
      color: #333;
   }

`

const BodyBox = styled.div`
  .amountTr {
    display: grid;
    grid-template-columns: 260px 370px repeat(2, 260px);
    height: 60px;
    position: relative;
    & > div:not(.headExtra) {
       border-right: 1px solid #999999;
    }
    .companyItem {
      height: 60px;
      font-size: 16px;
      font-weight: 600;
    }
    .headExtra {
      opacity: 0;
      position: absolute;
      left: -15px;
      top: 50%;
      transform: translateY(-50%);
      display: flex;
      flex-direction: column;
      &:hover {
        opacity: 1;
      }
    }
    &:hover .headExtra {
      opacity: 1;
    }
  }
  .serialNumber {
    position: absolute;
    left: 3px;
    color: #999;
    font-size: 13px;
  }
  div.amountTr:not(:last-child) {
    & > div:not(.headExtra) {
       border-bottom: 1px solid #999999;
    }
  }
`

const AmountItem = styled.div`
  position: relative;
  &:focus-within {
    .input input{
       opacity: 1;
    }
  }
  &.minus {
    .companyItem {
      color: red;
    }
  }
  .input {
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    background: transparent;
    border-color: transparent;
    .ant-input-number-input-wrap {
      height: 100%;
    }
    .ant-input-number-handler-wrap {
      display: none;
    }
    &:focus-within {
      background: #fff;
      border: none;
    }
    input {
      outline: none;
      height: 100%;
      text-align: right !important;
      font-size: 24px;
      opacity: 0;
      background: transparent;
    }
  }
`

const FootBox = styled.div`
  display: grid;
  grid-template-columns: 630px repeat(2, 260px);
  height: 60px;
  border-top: 1px solid #999999;
  & > div:not(:last-child) {
    border-right: 1px solid #999999;
  }
  .companyItem {
      height: 60px;
      font-size: 16px;
      font-weight: 600;
  }
`
const SummaryTotal = styled.div`
  line-height: 60px;
  padding-left: 10px;
  font-weight: 600;
  font-size: 13px;
`

const CreatorBox = styled.div`
  margin-top: 15px;
  font-size: 13px;
  color: #666;
`
