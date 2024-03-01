import React, {useEffect, useRef, useState} from 'react';
import {Col, Radio} from "antd";
import {useModel} from "umi"
import * as subjectWeb from "@/services/swagger/subjectWeb";
import {listSubjectUsingGET} from "@/services/swagger/subjectWeb";
import * as hooks from "@/utils/hooks";
import {useModalWithParam, useTableExpandable} from "@/utils/hooks";
import ProCard from "@ant-design/pro-card";
import ExProTable from "@/components/Table/ExtProTable";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";
import {LENDING_DIRECTION, SUBJECT_ASSIST_SETTLEMENT, SUBJECT_CATEGORY} from "@/constants";
import GlobalPageContainer from "@/components/PageContainer";
import SubjectFormModal from "@/pages/SubjectList/SubjectFormModal";

export default () => {
  const [activeTabKey, setActiveTabKey] = useState(SUBJECT_CATEGORY.ASSETS.value)

  const [expandable, onLoad] = useTableExpandable()
  const createModal = useModalWithParam()
  const {fetchSubjects} = useModel("useSubjectModel")

  const openModalWithCheck = (params) => {
    createModal.open(params)
  }
  // 初始化行业数据
  useEffect(() => {
  }, [])
  useEffect(() => {
    actionRef.current?.reload()
  }, [activeTabKey])
  const actionRef = useRef()
  const columns = [
    {
      title: "科目编号", dataIndex: "number", editable: false, width: 125,
      render:(_, row) => {
        return (
          <span style={{marginLeft: (row.level - 1) * 10}}>{row.number}</span>
        )
      }
    },
    {
      title: "科目名称", dataIndex: "name", width: 125
    },
    {
      title: "级数", dataIndex: "level", editable: false, search: false, width: 50
    },
    {
      title: "科目类别", dataIndex: "category", valueType: "select", width: 115, editable: false, search: false,
      fieldProps: {
        allowClear: false,
        options: Object.values(SUBJECT_CATEGORY)
      }
    },
    {
      title: "余额方向", dataIndex: "lendingDirection", valueType: "select", search: false, width: 80
      , fieldProps: {
        allowClear: false,
        options: Object.values(LENDING_DIRECTION)
      }
    },
    {
      title: "辅助结算", dataIndex: "assistSettlement", valueType: "select", search: false, width: 125
      , fieldProps: {
        allowClear: false,
        options: Object.values(SUBJECT_ASSIST_SETTLEMENT)
      }
    },
    {
      title: '操作', dataIndex: 'id',
      width: 180, valueType: 'option',
      fixed: "right",
      render: (dom, row, index, action) => {
        return [
          <a key="addSub" onClick={(e) => {
            e.stopPropagation()
            createModal.open({parent: row})
          }}>新增子级</a>,
          <a key="edit" onClick={() => action?.startEditable(row.id)}>编辑</a>,
          <ExtConfirmDel key="del" onConfirm={async () => {
            await subjectWeb.deleteSubjectUsingDELETE({id: row.id})
            fetchSubjects()
            actionRef.current?.reload()
          }}/>,
        ]
      }
    },
  ]
  const {editable} = hooks.useTableForm({
    onSave: (key, row) => {
      return subjectWeb.updateSubjectUsingPUT(row)
    }
  })
  return (
      <GlobalPageContainer>
        <ProCard ghost gutter={[8, 0]}>
          <Col span={24}>
            <ExProTable actionRef={actionRef} columns={columns} pagination={false}
                        expandable={expandable} onLoad={onLoad}
                        onNew={() => openModalWithCheck({parent: {id: null, category: activeTabKey}})}
                        search={{filterType: "light"}}
                        editable={editable} scroll={{y: window.innerHeight - 330}}
                        request={(params) => listSubjectUsingGET({...params, category: activeTabKey})}
                        headerTitle={(
                          <div style={{display: "flex", alignItems: "center"}}>
                            <Radio.Group defaultValue={activeTabKey} style={{marginRight: 12}} onChange={e => setActiveTabKey(e.target.value)}>
                              {Object.values(SUBJECT_CATEGORY).map(ele => (
                                <Radio.Button key={ele.value} value={ele.value}>{ele.label}</Radio.Button>
                              ))}
                            </Radio.Group>
                          </div>
                        )}
            />
            {createModal.visible && (
              <SubjectFormModal modal={createModal} tblActionRef={actionRef}/>
            )}
          </Col>
        </ProCard>
      </GlobalPageContainer>
  )
}
