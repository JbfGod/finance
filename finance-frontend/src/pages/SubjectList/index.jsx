import React, {useEffect, useRef, useState} from 'react';
import {Button, Col, Empty, message, Tree} from "antd";
import {history, useModel} from "umi"
import * as subjectWeb from "@/services/swagger/subjectWeb";
import {pageSubjectUsingGET} from "@/services/swagger/subjectWeb";
import * as hooks from "@/utils/hooks";
import {useModalWithParam, useSecurity, useTableExpandable} from "@/utils/hooks";
import ProCard from "@ant-design/pro-card";
import {ModalForm, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import ExProTable from "@/components/Table/ExtProTable";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";
import * as industryWeb from "@/services/swagger/industryWeb";
import {LENDING_DIRECTION, SUBJECT_ASSIST_SETTLEMENT, SUBJECT_TYPE} from "@/constants";
import styles from "@/global.less"
import GlobalPageContainer from "@/components/PageContainer";

export default ({mode, formModalProps = {}}) => {
  const isFormModal = mode === "formModal"
  const {disableFilter} = formModalProps
  const [expandable, onLoad] = useTableExpandable()
  const [selectedIndustry, setSelectedIndustry] = useState({id: 0, number: "0"})
  const [industryTreeData, setIndustryTreeData] = useState([])
  const createModal = useModalWithParam()
  const {fetchSubjects} = useModel("useSubjectModel")

  const security = useSecurity()

  const openModalWithCheck = (params) => {
    if (security.isSuperProxyCustomer
        && params.parentId === 0
        && (selectedIndustry.hasLeaf || selectedIndustry.id === 0)) {
      return message.warn("新增科目只能选择叶子节点的行业！")
    }
    createModal.open(params)
  }

  // 加载行业数据
  const fetchTreeIndustry = async () => {
    const {data} = await industryWeb.treeIndustryUsingGET()
    setIndustryTreeData([{id: 0, number: "0", name: "全部行业", children: data}])
  }
  // 初始化行业数据
  useEffect(() => {
    if (security.isSuperProxyCustomer) {
      fetchTreeIndustry()
    }
  }, [])
  const actionRef = useRef()
  const columns = [
    ...(security.isSuperProxyCustomer? [
      {
        title: "所属行业", dataIndex: "industry", editable: false, search: false, width: 125
      }
    ] : []),
    {
      title: "科目编号", dataIndex: "number", editable: false, width: 125
    },
    {
      title: "科目名称", dataIndex: "name", width: 125
    },
    {
      title: "级数", dataIndex: "level", editable: false, search: false, width: 50
    },
    {
      title: "科目类型", dataIndex: "type", valueType: "select", search: false, width: 115
      , fieldProps: {
        allowClear: false,
        options: Object.values(SUBJECT_TYPE)
      }
    },
    {
      title: "科目方向", dataIndex: "lendingDirection", valueType: "select", search: false, width: 80
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
      title: "备注", dataIndex: "remark", valueType: "textarea", search: false,width: 125,
      fieldProps: {showCount: true, maxLength: 255}
    },
    {
      title: '操作', dataIndex: 'id',
      width: 180, valueType: 'option',
      fixed: "right",
      render: (dom, row, index, action) => {
        return [
          <a key="addSub" onClick={(e) => {
            e.stopPropagation()
            createModal.open({parentId : row.id, industryId: row.industryId})
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
  const hasIndustry = !!industryTreeData?.[0]?.children?.[0]
  if (!hasIndustry && security.isSuperProxyCustomer) {
    return (
        <ProCard colSpan={24} bordered>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}
                 description={<span>暂无行业数据，无法添加科目</span>}>
            <Button type="primary" onClick={() => history.push("/base/industry")}>前往行业管理添加行业</Button>
          </Empty>
        </ProCard>
    )
  }
  return (
      <GlobalPageContainer {...(isFormModal?{header:{title:null, breadcrumb: null}}:{})}>
        <ProCard ghost gutter={[8, 0]}>
          {security.isSuperProxyCustomer && (
              <ProCard bordered className={styles.cardCommon} colSpan={5}>
                <Tree showLine={{showLeafIcon: false}} selectedKeys={[selectedIndustry.id]} defaultExpandAll
                      fieldNames={{title: "name", key: "id"}} treeData={industryTreeData}
                      onSelect={(keys, {node}) => {
                        setSelectedIndustry(node)
                      }}
                />
              </ProCard>
          )}
          <Col span={security.isSuperProxyCustomer?19:24}>
            <ExProTable actionRef={actionRef} columns={columns}
                        expandable={expandable} onLoad={onLoad}
                        params={{industryId: selectedIndustry.id||undefined}}
                        onNew={() => openModalWithCheck({parentId: 0})}
                        search={{filterType: "light"}}
                        editable={editable} scroll={{y: window.innerHeight - 330}}
                        request={pageSubjectUsingGET}
                        {...(isFormModal?{
                          scroll: {y: 275},
                          tableAlertRender:false,
                          onRow: (record) => ({
                            onClick: () => {
                              if (disableFilter?.(record)) {
                                return
                              }
                              formModalProps?.setSelectedKeys([record.id])
                              formModalProps.onSelect?.([record.id])
                            }
                          }),
                          rowSelection:{
                            type: 'radio',
                            selectedRowKeys: formModalProps?.selectedKeys,
                            onChange: (keys) => {
                              formModalProps?.setSelectedKeys(keys)
                              formModalProps.onSelect?.(keys)
                            },
                            getCheckboxProps: (record) => ({
                              disabled: disableFilter?.(record)
                            })
                          }
                        }:{})}
            />
            <ModalForm title="新增科目" width="420px" visible={createModal.visible}
                       initialValues={{type: "SUBJECT", assistSettlement: "NOTHING", direction: "NOTHING"}}
                       modalProps={{destroyOnClose: true}}
                       onVisibleChange={createModal.handleVisible}
                       layout="inline"
                       grid={true}
                       rowProps={{gutter: [0,12]}}
                       onFinish={async (value) => {
                         await subjectWeb.addSubjectUsingPOST({
                           ...value,
                           industryId: selectedIndustry.id || createModal.state.industryId,
                           parentId: createModal.state.parentId
                         }).then(() => {
                           createModal.close()
                           fetchSubjects()
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
              <ProFormSelect name="type"
                             allowClear={false} label="类型" options={Object.values(SUBJECT_TYPE)}/>
              <ProFormSelect name="lendingDirection" labelCol={{span: 6}}
                             allowClear={false} label="科目方向" options={Object.values(LENDING_DIRECTION)}/>
              <ProFormSelect name="assistSettlement" allowClear={false} label="辅助结算"
                             options={Object.values(SUBJECT_ASSIST_SETTLEMENT)}/>
              <ProFormTextArea name="remark" fieldProps={{showCount: true, maxLength: 255}} label="备注"/>
            </ModalForm>
          </Col>
        </ProCard>
      </GlobalPageContainer>
  );
};
