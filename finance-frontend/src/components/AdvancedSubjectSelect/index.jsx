import ExtTreeSelect from "@/components/Common/ExtTreeSelect";
import React, {useState} from "react";
import {Modal} from "antd";
import SubjectList from "@/pages/SubjectList";
import {useModalWithParam} from "@/utils/hooks";
import {SearchOutlined} from "@ant-design/icons";
import styles from "./index.less"

export function AdvancedSubjectSelect({subjects, setSubjects, disableFilter, onChange, ...props}) {
    const [modal, _, openModal] = useModalWithParam()
    const [value, setValue] = useState(props?.value)
    const triggerChange = (newValue) => {
        setValue(newValue)
        onChange && onChange(newValue)
    }
    const v = props.value || value
    return (
        <>
            <ExtTreeSelect options={subjects} placeholder="只能选择费用类科目"
                           value={v}
                           disableFilter={disableFilter} onChange={triggerChange}
                           fieldsName={{key: "id", title: (v) => `${v.number}-${v.name}`}}
                           onlySelectedLeaf={true}
                           suffixIcon={<SearchOutlined onClick={openModal} style={{paddingRight: 18}}/>}
                           style={{width: '100%'}} {...props}/>
            <SubjectModal modal={modal} disableFilter={disableFilter} onOk={triggerChange}
                          onDataSourceChange={setSubjects}
            />
        </>
    )
}

/**
 * 科目弹窗可以curd，可以单选
 */
function SubjectModal({modal, disableFilter, onDataSourceChange, onOk, ...props}) {
    const {visible, handleVisible} = modal
    const [selectedKeys, setSelectedKeys] = useState()
    return (
        <Modal width={1200} title="选择科目" visible={visible}
               className={styles.subjectModal}
               onOk={() => {
                   onOk && onOk(selectedKeys?.[0])
                   handleVisible(false)
               }}
               onCancel={() => handleVisible(false)} {...props}>
            <SubjectList mode="formModal" formModalProps={{
                onDataSourceChange,
                selectedKeys, setSelectedKeys,
                disableFilter, onSelect: setSelectedKeys
            }}/>
        </Modal>
    )
}
