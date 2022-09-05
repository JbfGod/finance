import React, {useRef} from 'react';
import PageContainer from "@/components/PageContainer";
import {Col} from "antd";
import * as industryWeb from "@/services/swagger/industryWeb";
import * as hooks from "@/utils/hooks";
import {useModalWithParam, useTableExpandable} from "@/utils/hooks";
import ProCard from "@ant-design/pro-card";
import {ModalForm, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import ExProTable from "@/components/Table/ExtProTable";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";

export default () => {
  const createModal = useModalWithParam()
  const [expandable, onLoad] = useTableExpandable()
  const actionRef = useRef()
  const columns = [
    {
      title: "行业编号", dataIndex: "number", editable: false
    },
    {
      title: "行业名称", dataIndex: "name"
    },
    {
      title: "备注", dataIndex: "remark", valueType: "textarea",
      fieldProps: {showCount: true, maxLength: 255}
    },
    {
      title: '操作', dataIndex: 'id',
      width: 255, valueType: 'option',
      render: (dom, row, index, action) => {
        return [
          <a key="addSub" onClick={(e) => {
            e.stopPropagation()
            createModal.open({parentId : row.id})
          }}>新增子级</a>,
          <a key="edit" onClick={(e) => {
            e.stopPropagation()
            action?.startEditable(row.id)
          }}>编辑</a>,
          <ExtConfirmDel key="del" onConfirm={async () => {
            await industryWeb.deleteIndustryUsingDELETE({id: row.id})
            actionRef.current?.reload()
          }}/>,
        ]
      }
    },
  ]
  const {editable} = hooks.useTableForm({
    onSave: (key, row) => {
      return industryWeb.updateIndustryUsingPUT(row)
    }
  })
  return (
    <PageContainer>
      <ProCard ghost gutter={[8, 0]}>
        <Col span={24}>
          <ExProTable pagination={false} actionRef={actionRef} columns={columns}
                      onLoad={onLoad} expandable={expandable}
                      search={false} onNew={() => createModal.open({parentId : 0})}
                      editable={editable}
                      request={industryWeb.treeIndustryUsingGET}
          />
          <ModalForm title="新增客户行业" width="400px" visible={createModal.visible} modalProps={{destroyOnClose: true}}
                     onVisibleChange={createModal.handleVisible}
                     onFinish={async (value) => {
                       industryWeb.addIndustryUsingPOST({
                         ...value,
                         parentId: createModal.state.parentId
                       }).then(() => {
                         createModal.close()
                         actionRef.current?.reload()
                       })
                     }}
          >
            <ProFormText name="number" label="行业编号"
                         rules={[
                           {required: true, message: "行业编号不能为空！"},
                         ]}
            />
            <ProFormText name="name" label="行业名称"
                         rules={[
                           {required: true, message: "行业名称不能为空！"},
                         ]}
            />
            <ProFormTextArea name="remark" fieldProps={{showCount: true, maxLength: 255}} label="备注"/>
          </ModalForm>

        </Col>
      </ProCard>
    </PageContainer>
  );
};
