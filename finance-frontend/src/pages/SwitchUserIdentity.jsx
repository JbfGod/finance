import React, {useState} from "react"
import {Button, Modal, Radio, Space} from "antd";
import {getUserIdentity, switchUserIdentity} from "@/utils/common";
import {history} from "@umijs/max"

export default function SwitchUserIdentity() {
  const [identity, setIdentity] = useState(getUserIdentity() || 'NORMAL')
  const onOk = () => {
    switchUserIdentity(identity)
    setTimeout(() => {
      if (identity === "NORMAL") {
        history.push("/Manage")
        return
      }
      history.push("/approval/expenseBill")
    }, 100)
  }
  return (
    <Modal title="请选择用户身份" width={350} centered
           open={true} closable={false} maskClosable={false}
           footer={[
             <Button type="primary" key="ok" onClick={onOk}>
               确认
             </Button>
           ]}>
      <Radio.Group onChange={(e) => setIdentity(e.target.value)} value={identity}>
        <Space direction="vertical">
          <Radio value="NORMAL">操作人员</Radio>
          <Radio value="APPROVER">审批人员</Radio>
        </Space>
      </Radio.Group>
    </Modal>
  )
}
