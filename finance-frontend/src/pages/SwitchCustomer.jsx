import {Button, Card, Col, Input, message, Row, Space} from "antd";
import ExProTable from "@/components/Table/ExtProTable";
import React, {useRef, useState} from "react";
import {ownedCustomerUsingGET, switchProxyCustomerUsingPUT} from "@/services/swagger/userWeb";
import GlobalPageContainer from "@/components/PageContainer";


export default function SwitchCustomer() {
  const actionRef = useRef()
  const [customerName, setCustomerName] = useState()
  const [customerNumber, setCustomerNumber] = useState()
  const [selectedRowKeys, setSelectedRowKeys] = useState([])
  const columns = [
    {
      title: "客户编号", dataIndex: "number", width: 100
    },
    {
      title: "客户名称", dataIndex: "name", width: 125
    }
  ]
  const handleSwitchCustomer = () => {
    if (selectedRowKeys.length === 0) {
      message.warn("请选择客户单位！")
      return
    }
    const customerId = selectedRowKeys[0]
    switchProxyCustomerUsingPUT({customerId}).then(_ => {
      window.location.href = "/"
    })
  }
  return (
    <GlobalPageContainer>
      <Card title="选择客户单位">
        <Space>
          <Row align="middle">
            <Col span={5}>编号：</Col>
            <Col span={19}>
              <Input allowClear onChange={(e) => setCustomerNumber(e.target.value || undefined)}/>
            </Col>
          </Row>
          <Row align="middle">
            <Col span={5}>名称：</Col>
            <Col span={19}>
              <Input allowClear onChange={(e) => setCustomerName(e.target.value || undefined)}/>
            </Col>
          </Row>
          <Button type="primary" onClick={actionRef.current?.reload}>查询</Button>
        </Space>
        <ExProTable actionRef={actionRef} columns={columns}
                    scroll={{y: 600}} editable={false}
                    toolBarRender={false} search={false}
                    tableAlertRender={false}
                    onRow={(record) => ({
                      onClick: () => setSelectedRowKeys([record.id])
                    })}
                    rowSelection={{
                      type: 'radio',
                      selectedRowKeys: selectedRowKeys,
                      onChange: (keys) => {
                        setSelectedRowKeys(keys)
                      }
                    }}
                    request={() => {
                      return ownedCustomerUsingGET({customerNumber, customerName})
                    }}
        />
        <Button key="submit" type="primary" onClick={handleSwitchCustomer}>
          确认
        </Button>
      </Card>
    </GlobalPageContainer>
  )
}
