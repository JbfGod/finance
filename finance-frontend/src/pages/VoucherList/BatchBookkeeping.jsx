import {Button, Card, Form, InputNumber, Popconfirm, Space} from "antd";
import {ProForm, ProFormItem} from "@ant-design/pro-form";
import ProFormDatePickerMonth from "@ant-design/pro-form/es/components/DatePicker/MonthPicker";
import {batchBookkeepingVoucherUsingPUT, batchUnBookkeepingVoucherUsingPUT} from "@/services/swagger/voucherWeb";
import moment from "moment";
import {useSecurity} from "@/utils/hooks";
import React from "react";
import GlobalPageContainer from "@/components/PageContainer";

export default function BatchBookkeeping() {
  const [form] = Form.useForm()
  const security = useSecurity("voucher:batch")

  const onBookkeeping = () => {
    form.validateFields().then(({yearMonth, ...values}) => {
      return batchBookkeepingVoucherUsingPUT({
        ...values, yearMonth: moment(yearMonth).format("YYYYMM")
      })
    })
  }

  const onUnBookkeeping = () => {
    form.validateFields().then(({yearMonth, ...values}) => {
      return batchUnBookkeepingVoucherUsingPUT({
        ...values, yearMonth: moment(yearMonth).format("YYYYMM")
      })
    })
  }
  const submitter = {
    render: () => [
      security.canBookkeeping && (
        <Popconfirm key="bookkeeping" title="确认审核？" onConfirm={onBookkeeping}>
          <Button type="primary">
            记账
          </Button>
        </Popconfirm>
      ),
      security.canUnBookkeeping && (
        <Popconfirm key="unBookkeeping" title="确认审核？" onConfirm={onUnBookkeeping}>
          <Button>
            反记账
          </Button>
        </Popconfirm>
      ),
    ]
  }
  return (
    <GlobalPageContainer>
      <Card>
        <ProForm submitter={submitter} form={form} initialValues={{yearMonth: moment()}}>
          <ProFormDatePickerMonth label="记账月份" name="yearMonth"
                                  rules={[{required:true}]}/>
          <ProForm.Group>
            <Space>
              <ProFormItem label="凭证号范围" name="beginSerialNum">
                <InputNumber style={{width: 100, textAlign: 'center'}} min={1} placeholder="凭证号"/>
              </ProFormItem>
              至
              <ProFormItem label=" " name="endSerialNum">
                <InputNumber style={{width: 100, textAlign: 'center'}} max={2} placeholder="凭证号"/>
              </ProFormItem>
            </Space>
          </ProForm.Group>
        </ProForm>
      </Card>
    </GlobalPageContainer>
  )
}
