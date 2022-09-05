import React, {useRef} from 'react';
import PageContainer from "@/components/PageContainer";
import {Col} from "antd";
import * as customerCategoryWeb from "@/services/swagger/customerCategoryWeb";
import * as hooks from "@/utils/hooks";
import {useModalWithParam, useTableExpandable} from "@/utils/hooks";
import ProCard from "@ant-design/pro-card";
import {ModalForm, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import ExProTable from "@/components/Table/ExtProTable";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";

export default () => {
  const creatModal = useModalWithParam()
  const [expandable, onLoad] = useTableExpandable()
  const actionRef = useRef()
  const columns = [
    {
      title: "类别编号", dataIndex: "number", editable: false
    },
    {
      title: "类别名称", dataIndex: "name"
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
            creatModal.open({parentId : row.id})
          }}>新增子级</a>,
          <a key="edit" onClick={(e) => {
            e.stopPropagation()
            action?.startEditable(row.id)
          }}>编辑</a>,
          <ExtConfirmDel key="del" onConfirm={async () => {
            await customerCategoryWeb.deleteCustomerCategoryUsingDELETE({id: row.id})
            actionRef.current?.reload()
          }}/>,
        ]
      }
    },
  ]
  const {editable} = hooks.useTableForm({
    onSave: (key, row) => {
      return customerCategoryWeb.updateCustomerCategoryUsingPUT(row)
    }
  })
  return (
    <PageContainer>
      <ProCard ghost gutter={[8, 0]}>
        <Col span={24}>
          <ExProTable pagination={false} actionRef={actionRef} columns={columns}
                      expandable={expandable} onLoad={onLoad}
                      search={false} onNew={() => creatModal.open({parentId : 0})}
                      editable={editable}
                      request={customerCategoryWeb.treeCustomerCategoryUsingGET}
          />
          <ModalForm title="新增客户类别" width="400px" visible={creatModal.visible} modalProps={{destroyOnClose: true}}
                     onVisibleChange={creatModal.handleVisible}
                     onFinish={async (value) => {
                       customerCategoryWeb.addCustomerCategoryUsingPOST({
                         ...value,
                         parentId: creatModal.state.parentId
                       }).then(() => {
                         creatModal.close()
                         actionRef.current?.reload()
                       })
                     }}
          >
            <ProFormText name="number" label="类别编号"
                         rules={[
                           {required: true, message: "类别编号不能为空！"},
                         ]}
            />
            <ProFormText name="name" label="类别名称"
                         rules={[
                           {required: true, message: "类别名称不能为空！"},
                         ]}
            />
            <ProFormTextArea name="remark" fieldProps={{showCount: true, maxLength: 255}} label="备注"/>
          </ModalForm>

        </Col>
      </ProCard>
    </PageContainer>
  );
};
