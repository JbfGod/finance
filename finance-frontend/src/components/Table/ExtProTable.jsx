import {AutoComplete, Button, Input} from "antd";
import {PlusOutlined} from "@ant-design/icons";
import ProTable from "@ant-design/pro-table";
import ProProvider from "@ant-design/pro-provider";
import React, {useContext} from "react";
import AutoCompleteInput from "@/components/Common/AutoCompleteInput";

export default function ExProTable({columns, pagination, ...props}) {
  const values = useContext(ProProvider);
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
    ...(props.params ? {
        params : {
          ...props.params
        }
      } : {}
    ),
    scroll: {y: 600},
    columns: enhanceColumns,
    pagination,
    ...props
  }
  if (pagination == null) {
    newProps.pagination = {defaultPageSize: 10}
  }

  return (
    <ProProvider.Provider
      value={{
        ...values,
        valueTypeMap: {
          autoComplete: {
            render: (v) => <span>{v}</span>,
            renderFormItem: (text, fieldProps = {}) => (
              <AutoCompleteInput placeholder="请输入" {...fieldProps} />
            ),
          },
        },
      }}
    >
      <ProTable {...newProps} />
    </ProProvider.Provider>
  )
}
