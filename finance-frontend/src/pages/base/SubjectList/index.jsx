import React, {useEffect, useRef, useState} from 'react';
import {PageContainer} from '@ant-design/pro-layout';
import {Button, Col, Empty, Tree} from "antd";
import {history} from "umi"
import * as subjectWeb from "@/services/swagger/subjectWeb";
import * as hooks from "@/utils/hooks";
import {useModalWithParam} from "@/utils/hooks";
import ProCard from "@ant-design/pro-card";
import {ModalForm, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import ExProTable from "@/components/Table/ExtProTable";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";
import * as industryWeb from "@/services/swagger/industryWeb";
import constants from "@/constants";

export default () => {
  const [createModal, handleModal, openModal] = useModalWithParam()
  const [selectedIndustryId, setSelectedIndustryId] = useState(0)
  const [selectedSubjectId, setSelectedSubjectId] = useState(0)
  const [industryTreeData, setIndustryTreeData] = useState([])
  const [expandIndustryKeys, setExpandIndustryKeys] = useState([0])
  const fetchTreeIndustry = async () => {
    const {data} = await industryWeb.treeIndustryUsingGET()
    setIndustryTreeData([{id: 0, number: "0", name: "全部行业", children: data}])
    setSelectedIndustryId(data?.[0]?.id || 0)
  }
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
      title: "科目名称", dataIndex: "name"
    },
    {
      title: "科目类型", dataIndex: "type", valueType: "select"
      , fieldProps: {
        allowClear: false,
        options: constants.SUBJECT_TYPES
      }
    },
    {
      title: "辅助结算", dataIndex: "assistSettlement", valueType: "select"
      , fieldProps: {
        allowClear: false,
        options: constants.SUBJECT_ASSIST_SETTLEMENT
      }
    },
    {
      title: "备注", dataIndex: "remark", valueType: "textarea",
      fieldProps: {showCount: true, maxLength: 255}
    },
    {
      title: '操作', dataIndex: 'id',
      width: 150, valueType: 'option',
      render: (dom, row, index, action) => {
        return [
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
            <Tree showLine={true} selectedKeys={[selectedIndustryId]} expandedKeys={expandIndustryKeys}
                  titleRender={(node) => node.id ? `[${node.number}]` : "" + node.name}
                  fieldNames={{title: "name", key: "id"}} treeData={industryTreeData}
                  onExpand={(expandKeys) => {
                    setExpandIndustryKeys([...expandKeys])
                  }}
                  onSelect={(keys) => {
                    const key = keys?.[0]
                    key && (setSelectedIndustryId(keys?.[0]))
                    if (key > 0 && key !== selectedIndustryId) {
                      actionRef.current?.reload()
                    }
                  }}
            />
          </ProCard>
          <Col span={19}>
            <ExProTable pagination={false} actionRef={actionRef} columns={columns}
                        expandable={{expandRowByClick:true}}
                        search={false} onNew={() => openModal({parentId:0,industryId:selectedIndustryId})}
                        editable={editable}
                        request={async () => subjectWeb.treeSubjectUsingGET({industryId: selectedIndustryId})}
            />
            <ModalForm title="新增科目" width="400px" visible={createModal.visible}
                       initialValues={{type: "SUBJECT", assistSettlement: "NOTHING"}}
                       modalProps={{destroyOnClose: true}}
                       onVisibleChange={handleModal}
                       onFinish={async (value) => {
                         await subjectWeb.addSubjectUsingPOST({
                           ...value,
                           industryId: selectedIndustryId,
                           parentId: selectedSubjectId
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
              <ProFormSelect allowClear={false} name="type" label="类型" options={constants.SUBJECT_TYPES}/>
              <ProFormSelect allowClear={false} name="assistSettlement" label="辅助结算"
                             options={constants.SUBJECT_ASSIST_SETTLEMENT}/>
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
