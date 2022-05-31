import React, {useEffect, useRef, useState} from 'react';
import {Button, Col, Empty, message, Tree} from "antd";
import {history} from "umi"
import * as subjectWeb from "@/services/swagger/subjectWeb";
import * as hooks from "@/utils/hooks";
import {useModalWithParam, useTableExpandable} from "@/utils/hooks";
import ProCard from "@ant-design/pro-card";
import {ModalForm, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import ExProTable from "@/components/Table/ExtProTable";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";
import * as industryWeb from "@/services/swagger/industryWeb";
import {SUBJECT_ASSIST_SETTLEMENT, LENDING_DIRECTION, SUBJECT_TYPE} from "@/constants";
import PageContainer from "@/components/PageContainer";

export default () => {
  const [expandable, onLoad] = useTableExpandable()
  const [selectedIndustry, setSelectedIndustry] = useState({id: 0, number: "0"})
  const [industryTreeData, setIndustryTreeData] = useState([])
  const [createModal, handleModal, openModal] = useModalWithParam()

  const openModalWithCheck = (params) => {
    if (params.parentId === 0 && (selectedIndustry.hasLeaf || selectedIndustry.id === 0)) {
      return message.warn("新增科目只能选择叶子节点的行业！")
    }
    openModal(params)
  }

  // 加载行业数据
  const fetchTreeIndustry = async () => {
    const {data} = await industryWeb.treeIndustryUsingGET()
    setIndustryTreeData([{id: 0, number: "0", name: "全部行业", children: data}])
  }
  // 初始化行业数据
  useEffect(() => {
    fetchTreeIndustry()
  }, [])
  const actionRef = useRef()
  const columns = [
    {
      title: "科目编号", dataIndex: "number", editable: false
    },
    {
      title: "级数", dataIndex: "level", editable: false
    },
    {
      title: "所属行业", dataIndex: "industry", editable: false,
    },
    {
      title: "科目名称", dataIndex: "name"
    },
    {
      title: "科目类型", dataIndex: "type", valueType: "select"
      , fieldProps: {
        allowClear: false,
        options: Object.values(SUBJECT_TYPE)
      }
    },
    {
      title: "科目方向", dataIndex: "lendingDirection", valueType: "select"
      , fieldProps: {
        allowClear: false,
        options: Object.values(LENDING_DIRECTION)
      }
    },
    {
      title: "辅助结算", dataIndex: "assistSettlement", valueType: "select"
      , fieldProps: {
        allowClear: false,
        options: Object.values(SUBJECT_ASSIST_SETTLEMENT)
      }
    },
    {
      title: "备注", dataIndex: "remark", valueType: "textarea",
      fieldProps: {showCount: true, maxLength: 255}
    },
    {
      title: '操作', dataIndex: 'id',
      width: 180, valueType: 'option',
      render: (dom, row, index, action) => {
        return [
          <a key="addSub" onClick={(e) => {
            e.stopPropagation()
            openModal({parentId : row.id, industryId: row.industryId})
          }}>新增子级</a>,
          <a key="edit" onClick={() => action?.startEditable(row.id)}>编辑</a>,
          <ExtConfirmDel key="del" onConfirm={async () => {
            await subjectWeb.deleteSubjectUsingDELETE({id: row.id})
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
  const hasIndustry = !!industryTreeData?.[0]?.children?.[0]
  return (
    <PageContainer>
      {hasIndustry ? (
        <ProCard ghost gutter={[8, 0]}>
          <ProCard colSpan={5} bordered className="cardCommon">
            <Tree showLine={{showLeafIcon: false}} selectedKeys={[selectedIndustry.id]} defaultExpandAll
                  fieldNames={{title: "name", key: "id"}} treeData={industryTreeData}
                  onSelect={(keys, {node}) => {
                    setSelectedIndustry(node)
                    if (node.id !== selectedIndustry.id) {
                      actionRef.current?.reload()
                    }
                  }}
            />
          </ProCard>
          <Col span={19}>
            <ExProTable pagination={false} actionRef={actionRef} columns={columns}
                        expandable={expandable} onLoad={onLoad}
                        search={false} onNew={() => openModalWithCheck({parentId: 0})}
                        editable={editable}
                        request={async () => subjectWeb.treeSubjectUsingGET({industryId: selectedIndustry.id || undefined})}
            />
            <ModalForm title="新增科目" width="400px" visible={createModal.visible}
                       initialValues={{type: "SUBJECT", assistSettlement: "NOTHING", direction: "NOTHING"}}
                       modalProps={{destroyOnClose: true}}
                       onVisibleChange={handleModal}
                       onFinish={async (value) => {
                         await subjectWeb.addSubjectUsingPOST({
                           ...value,
                           industryId: selectedIndustry.id || createModal.industryId,
                           parentId: createModal.parentId
                         }).then(() => {
                           handleModal(false)
                           actionRef.current?.reload()
                         })
                       }}
            >
              <ProFormText name="number" label="科目编号"
                           rules={[
                             {required: true, message: "科目编号不能为空！"},
                           ]}
              />
              <ProFormText name="name" label="科目名称"
                           rules={[
                             {required: true, message: "科目名称不能为空！"},
                           ]}
              />
              <ProFormSelect name="type" allowClear={false} label="类型" options={Object.values(SUBJECT_TYPE)}/>
              <ProFormSelect name="lendingDirection" allowClear={false} label="科目方向" options={Object.values(LENDING_DIRECTION)}/>
              <ProFormSelect name="assistSettlement" allowClear={false} label="辅助结算"
                             options={Object.values(SUBJECT_ASSIST_SETTLEMENT)}/>
              <ProFormTextArea name="remark" fieldProps={{showCount: true, maxLength: 255}} label="备注"/>
            </ModalForm>
          </Col>
        </ProCard>
      ) : (
        <ProCard colSpan={24} bordered>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}
                 description={<span>暂无行业数据，无法添加科目</span>}>
            <Button type="primary" onClick={() => history.push("/base/industry")}>前往行业管理添加行业</Button>
          </Empty>
        </ProCard>
      )}
    </PageContainer>
  );
};
