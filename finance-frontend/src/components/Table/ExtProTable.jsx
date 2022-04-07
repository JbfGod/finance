import {Button} from "antd";
import {PlusOutlined} from "@ant-design/icons";
import ProTable from "@ant-design/pro-table";
import React from "react";

export default function ExProTable(props) {
  const newProps = {
    rowKey: "id",
    ...(props.onNew? {
      toolBarRender:() => [
        <Button type="primary" key="primary" onClick={()=> props.onNew()}>
          <PlusOutlined /> 新增
        </Button>,
      ]
    } : {}),
    ...props
  }
  return (
    <ProTable {...newProps} />
  )
}
