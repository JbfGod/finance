import {Button, message, Modal} from "antd";
import ExProTable from "@/components/Table/ExtProTable";
import React, {useRef, useState} from "react";
import {ownedCustomerUsingGET, switchProxyCustomerUsingPUT} from "@/services/swagger/userWeb";
import {history, useModel} from "umi"
import {loginOut} from "@/services/login";


export default function SwitchCustomer() {
  const actionRef = useRef()
  const { setInitialState } = useModel('@@initialState')
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
    <Modal title="选择客户单位" width={600} visible={true} closable={false}
           footer={[
             <Button key="quit" onClick={() => {
               setInitialState((v) => ({ ...v, currentUser: undefined }))
               loginOut()
             }}>
               退出登录
             </Button>,
             <Button key="submit" type="primary" onClick={handleSwitchCustomer}>
               确认
             </Button>
           ]}
    >
      <ExProTable actionRef={actionRef} columns={columns}
                  scroll={{y: 600}} editable={false}
                  toolBarRender={false}
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
                  request={ownedCustomerUsingGET}
      />
    </Modal>
  )
}
