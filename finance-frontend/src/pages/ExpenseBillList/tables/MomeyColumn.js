export default function MoneyColumn(title, dataIndex, {...props} = {}) {
  return {
    title, dataIndex,
    valueType: "digit",
    fieldProps: {precision: 3, controls: false, formatter: v => Number(v) || "", style: {width: "100%"}},
    render: (_, row) => {
      return row[dataIndex] || ""
    },
    ...props
  }
}
