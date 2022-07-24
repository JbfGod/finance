import {Button, Card, Form, Popconfirm} from "antd";
import {ProForm, ProFormItem} from "@ant-design/pro-form";
import ProFormDatePickerMonth from "@ant-design/pro-form/es/components/DatePicker/MonthPicker";
import moment from "moment";
import React from "react";
import GlobalPageContainer from "@/components/PageContainer";
import {cancelClosedAccountUsingPOST, closeAccountUsingPOST} from "@/services/swagger/accountCloseListWeb";

const getFormValues = (values) => {
  const {yearMonthDate} = values
  return {
    yearMonthDate: yearMonthDate.format("YYYY-MM")
  }
}
export default function AccountClose() {
  const [form] = Form.useForm()

  const onCloseAccount = () => {
    form.validateFields().then((values) => {
      return closeAccountUsingPOST(getFormValues(values))
    })
  }

  const onCancelClosedAccount = () => {
    form.validateFields().then((values) => {
      return cancelClosedAccountUsingPOST(getFormValues(values))
    })
  }
  const submitter = {
    render: () => [
      <Popconfirm key="closeAccount" title="确认月结账？" onConfirm={onCloseAccount}>
        <Button type="primary">
          月结账
        </Button>
      </Popconfirm>,
      <Popconfirm key="cancelClosedAccount" title="确认取消结账？" onConfirm={onCancelClosedAccount}>
        <Button>
          取消结账
        </Button>
      </Popconfirm>,
    ]
  }
  return (
    <GlobalPageContainer>
      <Card>
        <ProForm submitter={submitter} form={form} initialValues={{yearMonthDate: moment()}}>
          <ProFormDatePickerMonth label="月份" name="yearMonthDate" rules={[{required:true}]} allowClear={false}/>
          <ProFormItem shouldUpdate={(prev, curr) => prev.yearMonthDate !== curr.yearMonthDate}>
            {() => {
              const yearMonthDate = form.getFieldValue(["yearMonthDate"])
              return (
                <div>
                  周期：
                  {yearMonthDate.startOf('month').format("YYYY-MM-DD")}
                  ~
                  {yearMonthDate.endOf('month').format("YYYY-MM-DD")}
                </div>
              )
            }}
          </ProFormItem>
        </ProForm>
      </Card>
    </GlobalPageContainer>
  )
}
