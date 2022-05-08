import {Button} from "antd";
import {PlusOutlined} from "@ant-design/icons";
import ProTable from "@ant-design/pro-table";
import React from "react";

export default function ExProTable({columns, ...props}) {
  const enhanceColumns = columns.map(c => ({textWrap: 'word-break', ellipsis: {showTitle: true}, ...c}))
  const newProps = {
    rowKey: "id",
    ...(props.onNew ? {
      toolBarRender: () => [
        <Button type="primary" key="primary" onClick={() => props.onNew()}>
          <PlusOutlined/> 新增
        </Button>,
      ]
    } : {}),
    scroll: {y: 600},
    columns: enhanceColumns,
    ...props
  }
  return (
    <ProTable {...newProps} />
  )
}
