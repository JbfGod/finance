export default (dataIndex, props) => ({
  dataIndex, title: "行次", render: (_, row) => {
    return row[dataIndex] || ""
  }, search: false, ...props
})
