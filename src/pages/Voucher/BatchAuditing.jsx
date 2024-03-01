import {Button, Card, Form, InputNumber, Popconfirm, Space} from "antd";
import {ProForm, ProFormItem} from "@ant-design/pro-form";
import ProFormDatePickerMonth from "@ant-design/pro-form/es/components/DatePicker/MonthPicker";
import {batchAuditingVoucherUsingPUT, batchUnAuditingVoucherUsingPUT} from "@/services/swagger/voucherWeb";
import moment from "moment";
import {useSecurity} from "@/utils/hooks";
import React from "react";
import GlobalPageContainer from "@/components/PageContainer";

export default function BatchAuditing() {
  const [form] = Form.useForm()
  const security = useSecurity("voucher:batch")

  const onAuditing = () => {
    form.validateFields().then(({yearMonth, ...values}) => {
      return batchAuditingVoucherUsingPUT({
        ...values, yearMonth: moment(yearMonth).format("YYYYMM")
      })
    })
  }

  const onUnAuditing = () => {
    form.validateFields().then(({yearMonth, ...values}) => {
      return batchUnAuditingVoucherUsingPUT({
        ...values, yearMonth: moment(yearMonth).format("YYYYMM")
      })
    })
  }
  const submitter = {
    render: () => [
      security.canAuditing && (
        <Popconfirm key="auditing" title="确认审核？" onConfirm={onAuditing}>
          <Button type="primary" key="auditing">
            审核
          </Button>
        </Popconfirm>
      ),
      security.canUnAuditing && (
        <Popconfirm key="unAuditing" title="确认审核？" onConfirm={onUnAuditing}>
          <Button htmlType="button">
            弃审
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
