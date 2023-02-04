

export default (dataIndex, title, props) => ({
  dataIndex, title: () => (
    <div style={{textAlign: "center"}}>{title}</div>
  ), render: (_, row) => {
    if (!row[dataIndex]) {
      return ""
    }
    if (row[dataIndex] == "0") {
      return ""
    }
    return row[dataIndex]
  }, search: false, ...props
})
