import React, {useState} from "react";
import {Input, Modal} from "antd";
import SubjectList from "@/pages/SubjectList";
import {useModalWithParam} from "@/utils/hooks";
import styles from "./index.less"
import {useModel} from "@/.umi/plugin-model/useModel";

export function AdvancedSubjectSelect({disableFilter, onChange, value, ...props}) {
    const {subjectById} = useModel("useSubjectModel")
    const modal = useModalWithParam()
    const triggerChange = (newValue) => {
        onChange && onChange(newValue)
    }
    const subjectId = value
    const selectedSubject = subjectById?.[subjectId]

    return (
        <>
            <Input value={selectedSubject ? `${selectedSubject.number}-${selectedSubject.name}` : ""}
                   onClick={() => modal.open({selectedKeys: subjectId != null ? [subjectId] : []})}/>
            {modal.visible && (
                <SubjectModal modal={modal} disableFilter={disableFilter} onOk={triggerChange} />
            )}
        </>
    )
}

/**
 * 科目弹窗可以curd，可以单选
 */
function SubjectModal({modal, disableFilter, onOk, ...props}) {
    const {visible, handleVisible, state} = modal
    const [selectedKeys, setSelectedKeys] = useState(state.selectedKeys)
    return (
        <Modal width={1200} title="选择科目" visible={visible}
               className={styles.subjectModal}
               onOk={() => {
                   onOk && onOk(selectedKeys?.[0])
                   handleVisible(false)
               }}
               onCancel={() => handleVisible(false)} {...props}>
            <SubjectList mode="formModal" formModalProps={{
                selectedKeys, setSelectedKeys,
                disableFilter, onSelect: setSelectedKeys
            }}/>
        </Modal>
    )
}
