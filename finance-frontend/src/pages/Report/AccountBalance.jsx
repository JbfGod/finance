import React, {useEffect, useRef, useState} from "react"
import GlobalPageContainer from "@/components/PageContainer";
import ExProTable from "@/components/Table/ExtProTable";
import {accountBalanceUsingGET} from "@/services/swagger/reportWeb";
import moment from "moment";
import {Col, Row, Table} from "antd";
import {voucherItemBySubjectUsingGET} from "@/services/swagger/voucherWeb";
import styles from "@/global.less";
import VoucherForm from "@/pages/VoucherList/VoucherForm";
import {useModalWithParam} from "@/utils/hooks";
import commonColumn from "@/pages/Report/commonColumn";
import commonNumberColumn from "@/pages/Report/commonNumberColumn";

export default function () {
  const actionRef = useRef()
  const [data, setData] = useState([])
  const [yearMonth, setYearMonth] = useState(null)
  const [selectedSubjectId, setSelectedSubjectId] = useState([])
  const voucherModal = useModalWithParam()
  useEffect(() => {
    setSelectedSubjectId([])
  }, [])
  const balanceColumns = [
    {
      title: "月份", dataIndex: "yearMonth", valueType: "dateMonth",
      hideInTable: true, initialValue: moment(), fieldProps: {allowClear: false}
    },
    {title: "科目编号", dataIndex: "subjectNumber", align: "center"},
    {title: "科目名称", dataIndex: "subjectName", align: "center"},
    {
      title: "期初余额", children: [
        commonNumberColumn("借方", "debitOpeningAmount"),
        commonNumberColumn("贷方", "creditOpeningAmount")
      ], search: false
    },
    {
      title: "本期余额", children: [
        commonNumberColumn("借方", "debitCurrentAmount"),
        commonNumberColumn("贷方", "creditCurrentAmount")
      ], search: false
    },
    {
      title: "年累计金额", children: [
        commonNumberColumn("借方", "debitAnnualAmount"),
        commonNumberColumn("贷方", "creditAnnualAmount")
      ], search: false
    },
    {
      title: "期末余额", children: [
        commonNumberColumn("借方", "debitClosingAmount"),
        commonNumberColumn("贷方", "creditClosingAmount")
      ], search: false
    },
  ]

  const voucherColumns = [
    {title: "凭证日期", dataIndex: "voucherDate", align: "center", width: 100},
    {title: "凭证号", dataIndex: "voucherNumber", align: "center", width: 50},
    commonColumn("摘要", "summary"),
    commonNumberColumn("借方金额", "localDebitAmount"),
    commonNumberColumn("贷方金额", "localCreditAmount"),
  ]
  return (
    <GlobalPageContainer>
      <Row>
        <Col span={selectedSubjectId.length > 0 ? 16 : 24}>
          <ExProTable actionRef={actionRef} columns={balanceColumns}
                      pagination={false} bordered
                      toolBarRender={false}
                      scroll={{y: window.innerHeight - 330}}
                      rowKey="subjectId" size="small"
                      request={async (params) => {
                        if (data.length === 0 || params.yearMonth !== yearMonth) {
                          setYearMonth(params.yearMonth)
                          return accountBalanceUsingGET(params).then(result => {
                            setData(result.data || [])
                            return result
                          })
                        }
                        const {subjectName, subjectNumber} = params
                        return {
                          success: true,
                          data: data.filter(v => (!subjectName || v.subjectName.startsWith(subjectName))
                            && (!subjectNumber || v.subjectNumber.startsWith(subjectNumber))
                          )
                        }
                      }}
                      onRow={(record) => ({
                        onClick: (e) => {
                          e.stopPropagation()
                          if (!selectedSubjectId.includes(record.subjectId)) {
                            setSelectedSubjectId([record.subjectId])
                          }
                        }
                      })}
                      rowClassName={(row) => selectedSubjectId.includes(row.subjectId) ? styles.commonSelectedRow : ""}
          />
        </Col>
        {selectedSubjectId.length > 0 && (
          <Col span={8}>
            <ExProTable columns={voucherColumns} toolBarRender={false}
                        pagination={false} bordered size="small"
                        search={false} params={{subjectId: selectedSubjectId[0], yearMonth}}
                        scroll={{y: window.innerHeight - 280}}
                        request={voucherItemBySubjectUsingGET}
                        onRow={(record) => ({
                          onClick: (e) => {
                            e.stopPropagation()
                            voucherModal.open({mode: "view", voucherId: record.voucherId})
                          }
                        })}
                        summary={(rows) => {
                          let totalDebitAmount = 0, totalCreditAmount = 0
                          rows.forEach(row => {
                            totalDebitAmount += Number(row.localDebitAmount || 0)
                            totalCreditAmount += Number(row.localCreditAmount || 0)
                          })
                          return (
                            <Table.Summary fixed>
                              <Table.Summary.Row>
                                <Table.Summary.Cell index={0}>合计</Table.Summary.Cell>
                                <Table.Summary.Cell index={1}></Table.Summary.Cell>
                                <Table.Summary.Cell index={2}></Table.Summary.Cell>
                                <Table.Summary.Cell index={3}>
                                  <div style={{textAlign: 'center'}}>{totalDebitAmount || ""}</div>
                                </Table.Summary.Cell>
                                <Table.Summary.Cell index={4}>
                                  <div style={{textAlign: 'center'}}>{totalCreditAmount || ""}</div>
                                </Table.Summary.Cell>
                              </Table.Summary.Row>
                            </Table.Summary>
                          )
                        }}
            />
            <VoucherForm modal={voucherModal}/>
          </Col>
        )}
      </Row>
    </GlobalPageContainer>
  )
}
