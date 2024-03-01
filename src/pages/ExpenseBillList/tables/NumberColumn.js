export default function NumberColumn(title, dataIndex, {...props} = {}) {
  return {
    title, dataIndex,
    valueType: "digit",
    fieldProps: {controls: false, formatter: v => Number(v) || "", style: {width: "100%"}},
    render: (_, row) => {
      return row[dataIndex] || ""
    },
    ...props
  }
}
