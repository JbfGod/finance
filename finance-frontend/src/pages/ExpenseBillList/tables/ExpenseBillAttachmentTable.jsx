import {ClearOutlined, DownloadOutlined} from "@ant-design/icons";
import React, {useRef} from "react";
import {IMG_ACCEPT} from "@/constants";
import {Upload} from "antd";
import {preMinioUrl} from "@/utils/common";
import styles from "../index.less"
import ExtEditableProTable from "@/pages/ExpenseBillList/tables/EditableProTableItem";

const DEFAULT_CHAR = ""

export default function ExpenseBillAttachmentTable({
                                                     formRef,
                                                     itemIndex,
                                                     setSubjects,
                                                     isViewMode,
                                                     ...props
                                                   }) {
  const actionRef = useRef()
  const columns = [
    {
      title: "票据文件",
      valueType: "text",
      dataIndex: "url",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.url ? (
          <a title="下载" onClick={() => window.open(preMinioUrl(row.url))}  className={styles.commonFont}>
            下载
          </a>
        ) : DEFAULT_CHAR
      },
      renderFormItem: ({index}, row) => (
        <UploadFileItem file={row.file} isViewMode={isViewMode} onUpload={({file}) => {
          let {name} = file
          name = name.substring(0, name.lastIndexOf(".") || name.length)
          changeRowByIndex(index, {file, name})
        }}/>
      )
    },
    {
      title: "票据名称",
      valueType: "text",
      dataIndex: "name",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.name || DEFAULT_CHAR
      }
    },
    {
      title: "备注",
      valueType: "text",
      dataIndex: "remark",
      fieldProps: {style: {width: "100%"}},
      render: (_, row) => {
        return row.remark || DEFAULT_CHAR
      }
    },
    ...(isViewMode ? [] : [{
      title: "操作", dataIndex: "operate", editable: false, width: 45,
      render: (_, row, index) => (
        <a title="清空" onClick={() => changeRowByIndex(index, {index}, true)}>
          <ClearOutlined/>
        </a>
      )
    }])
  ]
  const changeRowByIndex = (index, value, replaced = false) => {
    const items = formRef.getFieldValue(["items"])
    const attachments = items[itemIndex].attachments
    formRef.setFieldsValue({
      items: items.map((item, idx) => (
        idx === itemIndex ? {
          ...item,
          attachments: attachments.map((subsidy, idx2) => (
            idx2 === index ? (replaced ? value : {...subsidy, ...value}) : subsidy
          ))
        } : item
      ))
    })
  }
  return (
    <ExtEditableProTable name={["items", itemIndex, "attachments"]} columns={columns} actionRef={actionRef}
                         isViewMode={isViewMode} {...props}/>
  )
}

function UploadFileItem({value, onChange, isViewMode, onUpload}) {
  const triggerChange = (v) => {
    onChange && onChange(v)
  }
  return (
    <>
      {value && (
        <a title="下载" onClick={() => window.open(preMinioUrl(value))}
           className={styles.commonFont} style={{marginRight: 20}}>
          下载
        </a>
      )}
      <Upload showUploadList={false}
              disabled={isViewMode}
              accept={IMG_ACCEPT}
              beforeUpload={async (file) => {
                triggerChange(URL.createObjectURL(file))
                onUpload && onUpload({file})
                return false
              }}
      >
        <a>
          {value ? "重新上传" : "上传"}
        </a>
      </Upload>
    </>
  )
}
