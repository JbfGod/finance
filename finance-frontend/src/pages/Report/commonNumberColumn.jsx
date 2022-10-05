import React from "react";

export default (title, dataIndex, props) => ({
  title, dataIndex, align: "center", render: (_, row) => {
    const content = Number(row[dataIndex]) || ""
    return <span title={content}>{content}</span>
  },
  valueType: "text", search: false, ...props
})
