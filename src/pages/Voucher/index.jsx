import React, {useEffect, useState} from "react"
import PageContainer from "@/components/PageContainer"
import styled from "styled-components"
import ProCard from "@ant-design/pro-card";
import {Button, Checkbox, Col, Form, message, Pagination, Row, Space} from "antd";
import {DeleteOutlined, FormOutlined} from "@ant-design/icons";
import {
  batchAuditingVoucherUsingPUT,
  batchDeleteVoucherUsingDELETE,
  batchUnAuditingVoucherUsingPUT,
  pageVoucherUsingGET
} from "@/services/swagger/voucherWeb";
import {useModel} from "@umijs/max";
import EnabledRangeMonthPicker from "@/components/Common/EnabledRangeMonthPicker";
import dayjs from "dayjs";
import {useSecurity} from "@/utils/hooks";

export default () => {
  const {proxyCustomer} = useSecurity()
  const {subjectById} = useModel("useSubjectModel")
  const [form] = Form.useForm()
  const [pagination, setPagination] = useState({
    current: 1, pageSize: 50
  })
  const [total, setTotal] = useState(0)
  const {current, pageSize} = pagination
  const [vouchers, setVouchers] = useState([])
  const [checkedVouchers, setCheckedVouchers] = useState([])
  const globalChecked = checkedVouchers.length > 0 && checkedVouchers.length === vouchers.length
  const loadVoucher = () => {
    const {dateRange} = form.getFieldsValue()

    pageVoucherUsingGET({current, pageSize,
      startPeriod: dateRange?.[0].format("YYYYMM"),
      endPeriod: dateRange?.[1].format("YYYYMM")
    }).then(res => {
      setTotal(res.total)
      setVouchers(res.data)
    })
  }
  useEffect(() => {
    loadVoucher()
  }, [pagination])
  useEffect(() => {
    const currentPeriod = dayjs(`${proxyCustomer.currentPeriod}`, "YYYYMM")
    form.setFieldsValue({dateRange: [currentPeriod, currentPeriod]})
  }, [])
  return (
    <PageContainer>
      <Container>
        <ProCard className="containerCard">
          <SearchBar>
            <Form form={form}>
              <Row justify="space-between">
                <Col>
                  <Space size={8}>
                    <Form.Item name="dateRange" noStyle>
                      <EnabledRangeMonthPicker allowClear={false}/>
                    </Form.Item>
                    <Button type="primary" onClick={() => {
                      setPagination(v => ({...v, current: 1}))
                    }}>查询</Button>
                  </Space>
                </Col>
                <Col>
                  <Space size={8}>
                    <Button onClick={() => {
                      if (checkedVouchers.length === 0) {
                        message.warn("请至少选择一条凭证数据")
                        return
                      }
                      batchAuditingVoucherUsingPUT({ids: checkedVouchers}).then(loadVoucher)
                    }}>审核</Button>
                    <Button onClick={() => {
                      if (checkedVouchers.length === 0) {
                        message.warn("请至少选择一条凭证数据")
                        return
                      }
                      batchUnAuditingVoucherUsingPUT({ids: checkedVouchers}).then(loadVoucher)
                    }}>反审核</Button>
                    <Button onClick={() => {
                      if (checkedVouchers.length === 0) {
                        message.warn("请至少选择一条凭证数据")
                        return
                      }
                      batchDeleteVoucherUsingDELETE({ids: checkedVouchers}).then(loadVoucher)
                    }}>删除</Button>
                  </Space>
                </Col>
              </Row>
            </Form>
          </SearchBar>
          <TableBox>
            <TableHead>
              <div style={{width: "3%"}}>
                <Checkbox indeterminate={checkedVouchers.length > 0 && !globalChecked} checked={globalChecked}
                          onChange={e => {
                            if (e.target.checked) {
                              setCheckedVouchers(vouchers.map(voucher => voucher.id))
                            } else {
                              setCheckedVouchers([])
                            }
                          }}/>
              </div>
              <div style={{width: "36%"}}>摘要</div>
              <div style={{width: "39%"}}>科目</div>
              <div style={{width: "11%"}}>借方金额</div>
              <div style={{width: "11%"}}>贷方金额</div>
            </TableHead>
            <TableBody>
              {vouchers.map(voucher => (
                <div key={voucher.id} className="trBox">
                  <div style={{width: "3%", textAlign: "center", border: "1px solid #C1C1C1", borderBottom: "none"}}>
                    <Checkbox checked={checkedVouchers.includes(voucher.id)} onChange={e => {
                      if (e.target.checked) {
                        !checkedVouchers.includes(voucher.id) && setCheckedVouchers(v => v.concat(voucher.id))
                      } else {
                        setCheckedVouchers(v => v.filter(id => voucher.id !== id))
                      }
                    }}/>
                  </div>
                  <div style={{width: "97%"}}>
                    <div className="tr">
                      <div className="head" style={{width: "39%"}}>
                        日期:{voucher.voucherDate}&nbsp;&nbsp;&nbsp;&nbsp;凭证号:记-{voucher.serialNumber}
                      </div>
                      <div className="head" style={{width: "39%"}}>
                        制单人:{voucher.creatorName || ""}&nbsp;&nbsp;&nbsp;&nbsp;
                        审核人:{voucher.auditorName || ""}&nbsp;&nbsp;&nbsp;&nbsp;
                        附单据:{voucher.attachmentNum}张
                      </div>
                      <div className="head" style={{width: "22%", textAlign: "right"}}>
                        <Space size={8} align="center">
                          <Button title="编辑" shape="circle" size="small"><FormOutlined/></Button>
                          <Button title="删除" shape="circle" size="small"><DeleteOutlined/></Button>
                        </Space>
                      </div>
                    </div>
                    {voucher.items.map(item => (
                      <div key={item.id} className="tr">
                        <div className="td" style={{width: "39%"}}>{item.summary}</div>
                        <div className="td"
                             style={{width: "39%"}}>{subjectById[item.subjectId]?.number}&nbsp;{subjectById[item.subjectId]?.name}</div>
                        <div className="td" style={{width: "11%"}}>{Number(item.debitAmount) || ""}</div>
                        <div className="td" style={{width: "11%"}}>{Number(item.creditAmount) || ""}</div>
                      </div>
                    ))}
                    <div className="tr">
                      <div className="td" style={{width: "78%", fontSize: 12, fontWeight: 600}}>合计</div>
                      <div className="td" style={{width: "11%"}}>{voucher.totalDebitAmount}</div>
                      <div className="td" style={{width: "11%"}}>{voucher.totalCreditAmount}</div>
                    </div>
                  </div>
                </div>
              ))}
            </TableBody>
          </TableBox>
        </ProCard>
      </Container>
      <Pagination
        style={{position: "absolute", bottom: 10, right: 10, margin: "0 auto"}}
        total={total} defaultPageSize={50}
        showSizeChanger showQuickJumper
        showTotal={(total) => `共 ${total} 条`}
        onChange={(page, pageSize) => {
          setPagination(v => ({...v, current: page, pageSize}))
        }}
      />
    </PageContainer>
  )
}
const Container = styled.div`
  .containerCard {
      .ant-pro-card-body {
        padding: 10px;
      }
  }
`
const SearchBar = styled.div`
    position: relative;
    background: #f5f7fa;
    padding: 8px 10px;
    border: 1px solid #ebebeb;
    font-size: 14px;
    color: #333333;
    border-radius: 2px;
    background-color: #f5f7fa;
    margin-bottom: 5px;
`
const TableBox = styled.div`
  height: calc(100% - 90px);
  border-bottom: 1px solid #C1C1C1;
`

const TableHead = styled.div`
  display: flex;
  background-color: #8ACFF8;
  & > div {
    text-align: center;
    line-height: 40px;
    font-size: 14px;
    font-weight: 600;
  }
`
const TableBody = styled.div`
  overflow-y: scroll;
  height:calc(100% - 60px);
  .trBox {
    display: flex;
    &:last-child {
      border-bottom: 1px solid #C1C1C1;
    }
    .tr {
      display: flex;
      border-top: 1px solid #C1C1C1;
      .td {
        padding: 0 10px;
        line-height: 41px;
        border-right: 1px solid #C1C1C1;
      }
      .head {
        text-align: left;
        height: 45px;
        line-height: 45px;
        color: #333333;
        padding: 0 10px;
        font-size: 12px;
        font-weight: 600;
        &:last-child {
          border-right: 1px solid #C1C1C1;
        }
      }
    }
  }
`
