import {Popconfirm} from "antd";
import {QuestionCircleOutlined} from "@ant-design/icons";
import React from "react";

export function ExtConfirmDel({onConfirm}) {
  return (
    <Popconfirm title="确认删除？" icon={<QuestionCircleOutlined />} onConfirm={onConfirm}>
      <a key="edit" onClick={e=>e.stopPropagation()}>删除</a>
    </Popconfirm>
  )
}
