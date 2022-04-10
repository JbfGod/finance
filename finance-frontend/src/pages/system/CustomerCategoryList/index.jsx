import React, {useEffect, useRef, useState} from 'react';
import {PageContainer} from '@ant-design/pro-layout';
import {Col, message, Tree} from "antd";
import * as customerCategoryWeb from "@/services/swagger/customerCategoryWeb";
import {useArrayToTree} from "@/utils/hooks";
import ProCard from "@ant-design/pro-card";
import {ModalForm, ProFormItem, ProFormRadio, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import ExProTable from "@/components/Table/ExtProTable";
import * as userWeb from "@/services/swagger/userWeb";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";
import * as hooks from "@/utils/hooks";
import styles from "./index.less"

export default () => {
  const [createModalVisible, handleModalVisible] = useState(false)
  const [updateModalVisible, handleUpdateModalVisible] = useState(false)
  const [categories, setCategories] = useState([])
  const [selectedCategoryId, setSelectedCategoryId] = useState(0)
  const [dataByKey, treeData] = useArrayToTree([{id: 0, number: "0", name: "全部"}, ...categories])
  const fetchCategories = async () => {
    const resp = await customerCategoryWeb.listCustomerCategoryUsingGET()
    setCategories(resp.data)
  }
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
          <a key="edit" onClick={() => action?.startEditable(row.id)}>编辑</a>,
          <ExtConfirmDel key="del" onConfirm={() => {
            customerCategoryWeb.deleteCustomerCategoryUsingDELETE({id: row.id})
            actionRef.current?.reload()
          }}/>,
        ]
      }
    },
  ]
  const {editable} = hooks.useTableForm({
    onSave: (key, row) => {
      return customerCategoryWeb.updateCustomerCategoryUsingPUT({
        id: key,
        name: row.name,
        remark: row.remark
      })
    }
  })
  return (
    <PageContainer>
      <ProCard ghost gutter={[8, 0]}>
        <ProCard colSpan={6} bordered>
          <Tree showLine={true} selectedKeys={[selectedCategoryId]}
                defaultExpandedKeys={[0]} titleRender={(node) => `[${node.number}]${node.name}`}
                fieldNames={{title: "name", key: "id"}} treeData={treeData}
                onSelect={(keys) => keys.length > 0 && (setSelectedCategoryId(keys[0]))}
          />
        </ProCard>
        <Col span={18}>
          <ExProTable pagination={false} actionRef={actionRef} columns={columns}
                      rowClassName={(row) => row.id === selectedCategoryId ? styles.selectedRow : ""}
                      search={false} onNew={() => handleModalVisible(true)}
                      editable={editable} onDataSourceChange={(data) => setCategories(data)}
                      request={customerCategoryWeb.listCustomerCategoryUsingGET}
          />
          <ModalForm title="新增客户类别" width="400px" visible={createModalVisible} modalProps={{destroyOnClose: true}}
                     onVisibleChange={handleModalVisible}
                     onFinish={async (value) => {
                       customerCategoryWeb.addCustomerCategoryUsingPOST({
                         ...value,
                         parentId: selectedCategoryId
                       }).then(() => {
                         handleModalVisible(false)
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
