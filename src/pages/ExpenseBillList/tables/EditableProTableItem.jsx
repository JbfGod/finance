import React, {useEffect, useState} from "react";
import {EditableProTable} from "@ant-design/pro-table";
import styles from "@/pages/ExpenseBillList/index.less";

export default function ExtEditableProTable({name, columns, isViewMode, actionRef, onEditRowChange, ...props}) {
    const [editableKeys, setEditableKeys] = useState([])
    useEffect(() => {
        setEditableKeys([])
    }, [name])
    return (
        <EditableProTable actionRef={actionRef} columns={columns} bordered
                          onRow={(record, index) => ({
                              onClick: (e) => {
                                  e.stopPropagation()
                                  if (!editableKeys.includes(index)) {
                                      setEditableKeys([index])
                                      onEditRowChange && onEditRowChange([index])
                                  }
                              }
                          })}
                          rowClassName={(_, index) => editableKeys.includes(index) ? styles.selected : ""}
                          editable={isViewMode ? false : {editableKeys}} controlled
                          size="small" className={styles.expenseBill} rowKey="index"
                          recordCreatorProps={false} name={name} {...props}
        />
    )
}
