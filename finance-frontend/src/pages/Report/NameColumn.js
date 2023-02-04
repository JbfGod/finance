

export default (dataIndex, title, levelField, props) => ({
  dataIndex, title: () => (
    <div style={{textAlign: "center"}}>{title}</div>
  ), render: (_, row) => {
    if (!row[dataIndex]) {
      return ""
    }
    return (
      <span style={{marginLeft: ((row[levelField]||1) - 1) * 10}}>
      {row[dataIndex]}
     </span>
    )
  }, search: false, ...props
})
