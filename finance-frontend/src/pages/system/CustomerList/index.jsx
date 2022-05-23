import React, {useEffect, useRef, useState} from 'react';
import PageContainer from "@/components/PageContainer";
import {Button, Col, Empty, message, Modal, Tree, TreeSelect} from "antd";
import {history} from "umi"
import * as subjectWeb from "@/services/swagger/subjectWeb";
import {useModalWithParam} from "@/utils/hooks";
import ProCard from "@ant-design/pro-card";
import {
  ModalForm,
  ProForm,
  ProFormDateTimeRangePicker,
  ProFormItem,
  ProFormRadio,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
  ProFormTreeSelect,
  StepsForm
} from "@ant-design/pro-form";
import ExProTable from "@/components/Table/ExtProTable";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";
import * as customerCategoryWeb from "@/services/swagger/customerCategoryWeb";
import constants, {CUSTOMER_TYPE, SUBJECT_TYPE} from "@/constants";
import * as customerWeb from "@/services/swagger/customerWeb";
import * as industryWeb from "@/services/swagger/industryWeb";
import ResourceDrawerForm from "@/pages/ResourceDrawerForm";
import ExtTreeSelect from "@/components/Common/ExtTreeSelect";

export default () => {
  const [selectedCategory, setSelectedCategory] = useState({id: 0, number: "0"})
  const selectedCategoryId = selectedCategory.id
  const [industryTreeData, setIndustryTreeData] = useState([])
  const [customerCategoryTreeData, setCustomerCategoryTreeData] = useState([])
  const [expandCustomerCategoryKeys, setExpandCustomerCategoryKeys] = useState([0])
  const [grantDrawer, handleGrantDrawerVisible, openGrantDrawer] = useModalWithParam(false, {
    resourceData: [], selectedResourceIds: [], customer: null
  })
  const [createModal, handleModal, openModal] = useModalWithParam()

  const [editModal, handleEditModal, openEditModal] = useModalWithParam()

  const openModalWithCheck = (params) => {
    if (selectedCategory.hasLeaf || selectedCategoryId === 0) {
      return message.warn("新增客户只能选择叶子节点的分类！")
    }
    openModal(params)
  }

  const fetchTreeCustomerCategory = async () => {
    const {data} = await customerCategoryWeb.treeCustomerCategoryUsingGET()
    setCustomerCategoryTreeData([{id: 0, number: "0", name: "全部分类", children: data}])
  }
  const fetchIndustryTreeData = async () => {
    const {data} = await industryWeb.treeIndustryUsingGET()
    setIndustryTreeData(data)
  }
  useEffect(() => {
    fetchTreeCustomerCategory()
    fetchIndustryTreeData()
  }, [])
  const actionRef = useRef()
  const columns = [
    {
      title: "客户名称", dataIndex: "name", editable: false,
      width: 125
    },
    {
      title: "所属分类", dataIndex: "category", editable: false, search: false,
      width: 125
    },
    {
      title: "所属行业", dataIndex: "industryId", editable: false, search: false,
      valueType: "treeSelect",
      width: 125,
      fieldProps: {
        allowClear: true,
        options: industryTreeData,
        fieldNames: {
          label: 'name',
          value: 'id',
        },
      },
    },
    {
      title: "客户类型", dataIndex: "type", valueType: "select",
      width: 85,
      fieldProps: {
        options: Object.values(CUSTOMER_TYPE)
      }
    },
    {
      title: "代理(租用)日期", dataIndex: "dateRange", search: false,
      width: 180, valueType: "dateRange",
      render: (dom, row) => {
        return [row.effectTime, "至", row.expireTime]
      }
    },
    {
      title: "联系人", dataIndex: "contactName", search: false,
      width: 85,
    },
    {
      title: "联系电话", dataIndex: "telephone", search: false,
      width: 100,
    },
    {
      title: "银行账号", dataIndex: "bankAccount", search: false,
      width: 85,
    },
    {
      title: "开户人", dataIndex: "bankAccountName", search: false
    },
    {
      title: "是否外汇", dataIndex: "useForeignExchange", search: false
      , render: (dom, row) => row.useForeignExchange ? "是" : "否"
    },
    {
      title: "备注", dataIndex: "remark", valueType: "textarea",
      fieldProps: {showCount: true, maxLength: 255}, search: false
    },
    {
      title: "创建日期", dataIndex: "createTime", width: 150
      , editable: false, search: false
    },
    {
      title: '操作', dataIndex: 'id', fixed: "right",
      width: 150, valueType: 'option',
      render: (dom, row, index, action) => {
        return [
          <ExtConfirmDel key="del" onConfirm={async () => {
            await subjectWeb.deleteSubjectUsingDELETE({id: row.id})
            actionRef.current?.reload()
          }}/>,
          <a key="edit" onClick={(e) => {
            e.stopPropagation()
            console.log(row)
            openEditModal({initialValues: row})
          }}>编辑</a>,
          <a key="grant" onClick={async () => {
            const {data: selectedResourceIds} = await customerWeb.resourceIdsOfCustomerUsingGET({customerId: row.id})
            const {data: resourceData} = await resourceWeb.treeResourcesUsingGET()
            openGrantDrawer({
              customer: row, resourceData, selectedResourceIds
            })
          }}>授权</a>,
        ]
      }
    },
  ]
  const hasCustomerCategory = !!customerCategoryTreeData?.[0]?.children?.[0]
  return (
    <PageContainer>
      {hasCustomerCategory ? (
        <ProCard ghost gutter={[8, 0]}>
          <ProCard colSpan={4} bordered className="cardCommon">
            <Tree showLine={{showLeafIcon: false}}
              // titleRender={(node) => (node.id ? `[${node.number}] ` : "") + node.name}
                  selectedKeys={[selectedCategoryId]} defaultExpandAll={true}
                  fieldNames={{title: "name", key: "id"}} treeData={customerCategoryTreeData}
                  onSelect={(keys, {node}) => {
                    setSelectedCategory(node)
                    if (node.id !== selectedCategoryId) {
                      actionRef.current?.reload()
                    }
                  }}
            />
          </ProCard>
          <Col span={20}>
            <ExProTable actionRef={actionRef} columns={columns}
                        scroll={{x: 1200, y: 600}} editable={false}
                        params={{categoryId: selectedCategoryId || undefined}}
                        expandable={{expandRowByClick: true}}
                        onNew={() => openModalWithCheck({parentId: 0})}
                        request={customerWeb.pageCustomerUsingGET}
            />
            <AddFormModal createModal={createModal} handleModal={handleModal}
                          categoryId={selectedCategoryId} actionRef={actionRef}
                          industryTreeData={industryTreeData}
            />
            <EditFormModal modal={editModal} handleModal={handleEditModal} actionRef={actionRef}
                           industryTreeData={industryTreeData}/>
            <ResourceDrawerForm title="功能授权" width="500px" visible={grantDrawer.visible}
                                drawerProps={{destroyOnClose: true}} resourceData={grantDrawer.resourceData}
                                initialValues={{resourceIds: grantDrawer.selectedResourceIds}}
                                onVisibleChange={handleGrantDrawerVisible}
                                onFinish={async (v) => {
                                  return customerWeb.grantResourceToCustomerUsingPOST({
                                    ...v,
                                    customerId: grantDrawer.customer.id
                                  })
                                }}
            />
          </Col>
        </ProCard>
      ) : (
        <ProCard colSpan={24} bordered>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}
                 description={<span>暂无客户分类数据，无法添加客户</span>}>
            <Button type="primary" onClick={() => history.push("/system/customerCategory")}>前往客户分类管理添加客户分类</Button>
          </Empty>
        </ProCard>
      )}
    </PageContainer>
  )
}

function AddFormModal({createModal, handleModal, categoryId, actionRef, industryTreeData}) {
  return (
    <StepsForm
      onFinish={(values) => {
        const {dateRange, user} = values;
        customerWeb.addCustomerUsingPOST({
          ...values,
          user: {...user, password: user.password || "123456", role: "ADMIN"},
          categoryId: categoryId,
          effectTime: dateRange[0],
          expireTime: dateRange[1],
        }).then(() => {
          handleModal(false)
          actionRef.current?.reload()
        })
      }}
      formProps={{
        initialValues: {
          type: "RENT",
          enabled: true,
          useForeignExchange: false
        }
      }}
      stepsFormRender={(dom, submitter) => {
        return (
          <Modal title="新增客户" width={600}
                 visible={createModal.visible} onCancel={() => handleModal(false)}
                 footer={submitter} destroyOnClose
          >{dom}</Modal>
        )
      }}
    >
      <StepsForm.StepForm name="customer" title="客户基本信息">
        <ProForm.Group>
          <ProFormText name="account" label="客户编号" colon={8} rules={[
            {required: true, message: "客户编号不能为空！"},
            {min: 5, max: 25, message: "客户编号只允许有5-25个字符！"},
            {pattern: /[\da-zA-Z]{5,25}/, message: "客户编号只允许包含数字和字母！"}
          ]}/>
          <ProFormText name="name" label="客户名称" rules={[{required: true, message: "客户名称不能为空！"}]}/>
        </ProForm.Group>
        <ProForm.Group>
          <ProFormSelect name="type" label="客户类型" options={Object.values(CUSTOMER_TYPE)}
                         allowClear={false}
                         rules={[{required: true, message: "客户类型不能为空！"}]}/>
          <ProFormSwitch name="enabled" label="客户状态" checkedChildren="启用" unCheckedChildren="停用" />
          <ProFormRadio.Group name="useForeignExchange" label="是否使用外汇"
                              options={[{label: "是", value: true}, {label: "否", value: false}]}/>
        </ProForm.Group>
        <ProFormItem name="industryId" label="行业分类" >
          <ExtTreeSelect options={industryTreeData} placeholder="只能选择叶子节点"
                         treeLine={{showLeafIcon: false}} style={{ width: '100%' }}
                         onlySelectedLeaf={true} rules={[{required: true, message: "行业分类不能为空！"}]}
          />
        </ProFormItem>
        <ProForm.Group>
          <ProFormText name="contactName" label="联系人" rules={[{required: true, message: "联系人不能为空！"}]}/>
          <ProFormText name="telephone" label="联系电话" rules={[{required: true, message: "联系电话不能为空！"}]}/>
        </ProForm.Group>
        <ProFormDateTimeRangePicker name="dateRange" label="代理(租用)日期"
                                    placeholder={["开始日期", "过期日期"]}
                                    format="yyyy-MM-DD"
                                    rules={[{required: true, message: "科目名称不能为空！"}]}/>
        <ProForm.Group>
          <ProFormText name="bankAccount" label="银行账号" rules={[{required: true, message: "银行账号不能为空！"}]}/>
          <ProFormText name="bankAccountName" label="开户人" rules={[{required: true, message: "开户人不能为空！"}]}/>
        </ProForm.Group>
        <ProFormTextArea name="remark" label="备注" fieldProps={{showCount: true, maxLength: 255}}/>
      </StepsForm.StepForm>
      <StepsForm.StepForm name="user" title="创建客户管理员">
        <ProFormText name={["user", "name"]} label="用户姓名" rules={[{required: true, message: "用户姓名不能为空！"}]}/>
        <ProFormText name={["user", "account"]} label="登录账号"
                     rules={[
                       {required: true, message: "登录账号不能为空！"},
                       {min: 5, max: 25, message: "登录账号只允许有5-25个字符！"},
                       {pattern: /[0-9a-zA-Z]{5,25}/, message: "登录账号只允许包含数字和字母！"}
                     ]}
        />
        <ProFormRadio.Group name="pwdType" label="登录密码" initialValue="默认" tooltip="默认密码：123456"
                            options={["默认", "自定义"]}/>
        <ProFormItem noStyle shouldUpdate={(prev, curr) => prev.pwdType !== curr.pwdType}>
          {({getFieldValue}) =>
            getFieldValue("pwdType") === "自定义" ? (
              <ProFormText.Password name="pwd" label="登录密码"
                                    rules={[
                                      {required: true, message: "密码不能为空！"},
                                      {min: 6, max: 20, message: "密码只允许有6-20个字符！"}
                                    ]}
              />
            ) : null
          }
        </ProFormItem>
        <ProFormItem noStyle shouldUpdate={(prev, curr) => prev.pwdType !== curr.pwdType}>
          {({getFieldValue}) =>
            getFieldValue("pwdType") === "自定义" ? (
              <ProFormText.Password name={["user", "password"]} label="确认密码"
                                    rules={[{
                                      validator: async (_, v) => {
                                        if (v !== getFieldValue("pwd")) {
                                          return Promise.reject("两次密码不一致！")
                                        }
                                      }
                                    }]}
              />
            ) : null
          }
        </ProFormItem>
      </StepsForm.StepForm>
    </StepsForm>
  )
}


function EditFormModal({modal, handleModal, actionRef, industryTreeData}) {
  if (!modal.visible) {
    return null
  }
  const {initialValues} = modal
  return (
    <ModalForm title="新增客户" width={600} modalProps={{destroyOnClose: true}}
               visible={modal.visible} onVisibleChange={handleModal}
               onFinish={(values) => {
                 const {dateRange} = values;
                 customerWeb.updateCustomerUsingPUT({
                   ...values,
                   id: initialValues.id,
                   effectTime: dateRange[0],
                   expireTime: dateRange[1],
                 }).then(() => {
                   handleModal(false)
                   actionRef.current?.reload()
                 })
               }}
               initialValues={{
                 ...initialValues,
                 dateRange: [initialValues.effectTime, initialValues.expireTime]
               }}
    >
      <ProForm.Group>
        <ProFormText name="account" label="客户编号" disabled/>
        <ProFormText name="name" label="客户名称" rules={[{required: true, message: "客户名称不能为空！"}]}/>
      </ProForm.Group>
      <ProForm.Group>
        <ProFormSelect name="type" label="客户类型" options={Object.values(CUSTOMER_TYPE)} allowClear={false}
                       rules={[{required: true, message: "客户类型不能为空！"}]}/>
        <ProFormRadio.Group name="useForeignExchange" label="是否使用外汇"
                            options={[{label: "是", value: true}, {label: "否", value: false}]}/>
        <ProFormSwitch name="enabled" label="客户状态" checkedChildren="启用" unCheckedChildren="停用"/>
      </ProForm.Group>
      <ProFormItem name="industryId" label="行业分类" >
        <ExtTreeSelect options={industryTreeData} placeholder="只能选择费用类科目"
                       treeLine={{showLeafIcon: false}} style={{ width: '100%' }}
                       onlySelectedLeaf={true} rules={[{required: true, message: "行业分类不能为空！"}]}
        />
      </ProFormItem>

      <ProForm.Group>
        <ProFormText name="contactName" label="联系人" rules={[{required: true, message: "联系人不能为空！"}]}/>
        <ProFormText name="telephone" label="联系电话" rules={[{required: true, message: "联系电话不能为空！"}]}/>
      </ProForm.Group>
      <ProFormDateTimeRangePicker name="dateRange" label="代理(租用)日期"
                                  placeholder={["开始日期", "过期日期"]}
                                  format="yyyy-MM-DD"
                                  rules={[{required: true, message: "科目名称不能为空！"}]}/>


      <ProForm.Group>
        <ProFormText name="bankAccount" label="银行账号" rules={[{required: true, message: "银行账号不能为空！"}]}/>
        <ProFormText name="bankAccountName" label="开户人" rules={[{required: true, message: "开户人不能为空！"}]}/>
      </ProForm.Group>
      <ProFormTextArea name="remark" label="备注" fieldProps={{showCount: true, maxLength: 255}}/>
    </ModalForm>
  )
}
