import React, {useState} from "react";
import {Divider, Modal, Select, Tabs, Tree, message} from "antd";
import {useModalWithParam} from "@/utils/hooks";
import styles from "./index.less"
import {useModel} from "@umijs/max";
import {LENDING_DIRECTION, SUBJECT_CATEGORY} from "@/constants";

/**
 *
 * @param onChange
 * @param value
 * @param balanceItems
 * @param props
 * @returns {JSX.Element|null}
 * @constructor
 */
export function AdvancedSubjectSelect({onChange, value, balanceItems = [], ...props}) {
  const {subjects} = useModel("useSubjectModel")
  const balancesBySubjectId = balanceItems.groupBy(sub => sub.subjectId)
  const leafSubjectOptions = subjects.filter(sub => !sub.hasLeaf).map(sub => {
    let balanceResult = balancesBySubjectId[sub.id]?.reduce((curr, next) => {
      return curr + ((next.debitAmount || 0) * 100 - (next.creditAmount || 0) * 100)
    }, 0) || 0
    if (sub.lendingDirection === LENDING_DIRECTION.BORROW.value) {
      balanceResult = (sub.balance * 100 + balanceResult) / 100
    } else {
      balanceResult = (sub.balance * 100 - balanceResult) / 100
    }

    return {
      label: (
        <div className={styles.selectLabel}>
          <div className={styles.name}>{sub.number}{' '}{sub.name}</div>
          <div className={styles.balance}>
            <label className={styles.balanceValue}>{balanceResult}</label>
          </div>
        </div>
      ),
      value: sub.id,
      searchText: `${sub.number}${sub.name}`
    }
  })
  const modal = useModalWithParam()
  const triggerChange = (newValue) => {
    onChange?.(newValue)
  }
  const subjectId = value
  if (subjectId && subjects.length === 0) {
    return null
  }
  return (
    <div className={styles.subjectSelectContainer}>
      <Select value={subjectId} popupClassName={styles.selectPopup} options={leafSubjectOptions}
              dropdownRender={(menu) => (
                <>
                  {menu}
                  <Divider style={{margin: '8px 0'}}/>
                  <div style={{textAlign: "center", lineHeight: "41px"}}>
                    <a>新增科目</a>
                  </div>
                </>
              )} allowClear={true} showSearch={true} filterOption={(input, option) =>
        (option?.searchText ?? '').toLowerCase().includes(input.toLowerCase())
      } onChange={v => triggerChange(v)} bordered={false}
      />
      <label className={styles.extra} onClick={() => modal.open()}>科目</label>
      <SubjectSelectModal modal={modal} onOk={(subId) => triggerChange(subId)}/>
    </div>
  )
}

function SubjectSelectModal({modal, disableFilter, onOk, ...props}) {
  const {treeSubjects, subjectById} = useModel("useSubjectModel")
  const [activeTabKey, setActiveTabKey] = useState(SUBJECT_CATEGORY.ASSETS.value)
  const [selectedKeys, setSelectedKeys] = useState()
  const treeData = treeSubjects.filter(sub => sub.category === activeTabKey)
  return (
    <Modal className={styles.modal} width={414} title="选择科目" open={modal.visible}
           onOk={() => {
             const selectedSubjectId = selectedKeys?.[0]
             if (subjectById[selectedSubjectId].hasLeaf) {
               message.warn("只能选择末级科目！")
               return
             }
             onOk && onOk(selectedSubjectId)
             modal.close()
           }}
           onCancel={() => modal.close()} {...props}>
      <Tabs centered activeKey={activeTabKey} items={Object.values(SUBJECT_CATEGORY).map(category => ({
        key: category.value,
        label: <div style={{width: 45, textAlign: "center"}}>{category.label}</div>,
        children: (
          <Tree.DirectoryTree height={408} treeData={treeData} onSelect={keys => setSelectedKeys(keys)}/>
        )
      }))} onChange={setActiveTabKey}/>
    </Modal>
  )
}
