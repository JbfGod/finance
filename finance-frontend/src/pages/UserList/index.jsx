import React, {useEffect, useRef, useState} from "react";
import PageContainer from "@/components/PageContainer";
import * as userWeb from "@/services/swagger/userWeb";
import {ModalForm, ProFormItem, ProFormRadio, ProFormSelect, ProFormText} from "@ant-design/pro-form";
import * as hooks from "@/utils/hooks";
import {useModalWithParam, useSecurity} from "@/utils/hooks";
import ExProTable from "@/components/Table/ExtProTable";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";
import ResourceDrawerForm from "@/pages/ResourceDrawerForm";
import * as customerWeb from "@/services/swagger/customerWeb";
import {searchCustomerCueUsingGET} from "@/services/swagger/customerWeb";
import {Badge} from "antd";
import {USER_ROLE} from "@/constants";

const nameRules = [
  {required: true, message: "用户姓名不能为空！"},
  {min: 2, max: 20, message: "用户姓名只允许有2-20个字符！"}
]
const renderBadge = (active = false) => {
  return (
      <Badge
          style={{
            marginTop: -2,
            marginLeft: 4,
            color: active ? '#1890FF' : '#999',
            backgroundColor: active ? '#E6F7FF' : '#eee',
          }}
      />
  );
};

export default () => {
  const [createModalVisible, handleModalVisible] = useState(false)
  const [updateModalVisible, handleUpdateModalVisible] = useState(false)
  const grantDrawer = useModalWithParam(false, {
    functionData: [], selectedResourceIds: [], user: null
  })
  const [tmpOperateUser, setTmpOperateUser] = useState()
  const [customers, setCustomers] = useState([])
  const customerOptions = [{name:"记账平台", number:"HX_TOP"}, ...customers]
    .map(c => ({label: `${c.name}-(${c.number})`, value: c.number}))
  const loadCustomers = () => {
    searchCustomerCueUsingGET().then(({data}) => {
      setCustomers(data)
    })
  }
  useEffect(() => {
    loadCustomers()
  }, [])
  const actionRef = useRef()
  const security = useSecurity("user")
  const columns = [
    ...(security.isSuperCustomer ? [
      {
        title: "客户编号", dataIndex: "customerNumber", valueType: "text"
      },
      {
        title: "客户名称", dataIndex: "customerName", valueType: "text"
      },
    ] : []),
    {
      title: "用户姓名", dataIndex: "name", valueType: "text"
      , formItemProps: {rules: nameRules}
    },
    {
      title: "用户账号", dataIndex: "account", editable: false
    },
    {
      title: '操作', dataIndex: 'id',
      width: 255, valueType: 'option',
      render: (dom, row, index, action) => {
        return [
          <a key="edit" onClick={() => action?.startEditable(row.id)}>编辑</a>,
          <ExtConfirmDel key="del" onConfirm={() => {
            userWeb.deleteUserUsingDELETE({id: row.id})
            actionRef.current?.reload()
          }}/>,
          <a key="grant" onClick={async () => {
            const {data: selectedResourceIds} = await userWeb.resourceIdsOfUserUsingGET({userId: row.id})
            const {data: functionData} = await customerWeb.treeResourceWithOperateUsingGET({customerId: row.customerId})
            grantDrawer.open({
              user: row, functionData, selectedResourceIds
            })
          }}>授权</a>,
          <a key="resetPwd" onClick={() => {
            setTmpOperateUser(row)
            handleUpdateModalVisible(true)
          }}>重置密码</a>
        ]
      }
    },
  ]
  const {editable} = hooks.useTableForm({
    onSave: (key, row) => {
      return userWeb.updateUserUsingPUT({
        id: key,
        name: row.name
      })
    }
  })
  const [activeTabKey, setActiveTabKey] = useState('NORMAL');
  const toolbar = {
    menu: {
      type: 'tab',
      activeKey: activeTabKey,
      multipleLine: true,
      items: [
        {
          key: 'NORMAL',
          label: <span>操作人员{renderBadge(activeTabKey === 'NORMAL')}</span>,
        },
        {
          key: 'APPROVER',
          label: <span>审批人员{renderBadge(activeTabKey === 'APPROVER')}</span>,
        }
      ],
      onChange: (key) => {
        setActiveTabKey(key);
      },
    }
  }
  return (
      <PageContainer>
        <ExProTable actionRef={actionRef} columns={columns}
                    toolbar={toolbar}
                    params={{role: activeTabKey}}
                    request={userWeb.pageUserUsingGET}
                    onNew={() => handleModalVisible(true)}
                    editable={editable}
        />
        <ModalForm title="新增用户" width="400px"
                   initialValues={{role: "NORMAL"}}
                   visible={createModalVisible} modalProps={{destroyOnClose: true}}
                   onVisibleChange={handleModalVisible}
                   onFinish={async (value) => {
                     const password = value.password || "123456"
                     userWeb.addUserUsingPOST({...value, password}).then(() => {
                       handleModalVisible(false)
                       actionRef.current?.reload()
                     })
                   }}
        >
          {security.isSuperCustomer && (
              <ProFormSelect name="customerNumber" label="客户编号" showSearch options={customerOptions}/>
          )}
          <ProFormSelect name="role" allowClear={false} label="用户类型" options={Object.values(USER_ROLE)}/>
          <ProFormText name="name" label="用户姓名" rules={nameRules}/>
          <ProFormText name="account" label="登录账号"
                       rules={[
                         {required: true, message: "用户账号不能为空！"},
                         {min: 5, max: 25, message: "登录账号只允许有5-25个字符！"},
                         {pattern: /[\da-zA-Z]{5,25}/, message: "登录账号只允许包含数字和字母！"}
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
                    <ProFormText.Password name="password" label="确认密码"
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
        </ModalForm>
        <ModalForm title="修改密码" width="400px"
                   visible={updateModalVisible} modalProps={{destroyOnClose: true}}
                   onVisibleChange={handleUpdateModalVisible}
                   onFinish={async (value) => {
                     return userWeb.resetUserPasswordUsingPUT({
                       id: tmpOperateUser.id,
                       password: value.password
                     }).then(() => {
                       handleUpdateModalVisible(false)
                       actionRef.current?.reload()
                     })
                   }}
        >
          <ProFormText.Password name="pwd" label="新密码"
                                rules={[
                                  {required: true, message: "密码不能为空！"},
                                  {min: 6, max: 20, message: "密码只允许有6-20个字符！"}
                                ]}
          />
          <ProFormItem noStyle shouldUpdate={() => false}>
            {({getFieldValue}) =>
                <ProFormText.Password name="password" label="确认密码"
                                      rules={[{
                                        validator: async (_, v) => {
                                          if (v !== getFieldValue("pwd")) {
                                            return Promise.reject("两次密码不一致！")
                                          }
                                        }
                                      }]}
                />
            }
          </ProFormItem>
        </ModalForm>
        {grantDrawer.visible?
            <ResourceDrawerForm title="功能授权" width="500px" visible={true}
                                drawerProps={{destroyOnClose: true}} resourceData={grantDrawer.state.functionData}
                                initialValues={{resourceWithOperateIds: grantDrawer.state.selectedResourceIds}}
                                onVisibleChange={grantDrawer.handleVisible}
                                onFinish={async (v) => {
                                  return userWeb.grantResourcesToUserUsingPOST({...v, userId: grantDrawer.state.user.id})
                                }}
            /> : null
        }
      </PageContainer>
  )
}
