import React, {useRef, useState} from "react";
import {PageContainer} from "@ant-design/pro-layout";
import * as userWeb from "@/services/swagger/userWeb";
import {ModalForm, ProFormItem, ProFormRadio, ProFormText} from "@ant-design/pro-form";
import * as hooks from "@/utils/hooks";
import ExProTable from "@/components/Table/ExtProTable";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";
import FunctionDrawerForm from "@/pages/FunctionDrawerForm";
import * as customerWeb from "@/services/swagger/customerWeb";

const nameRules = [
  {required: true, message: "用户姓名不能为空！"},
  {min: 2, max: 20, message: "用户姓名只允许有2-20个字符！"}
]

export default () => {
  const [createModalVisible, handleModalVisible] = useState(false)
  const [updateModalVisible, handleUpdateModalVisible] = useState(false)
  const [grantDrawer, setGrantDrawer] = useState({visible: false, selectedFunctionIds: null, user: null})
  const handleGrantDrawerVisible = (visible) => {
    const newValue = visible ? {...grantDrawer, visible} : {visible}
    setGrantDrawer(newValue)
  }
  const [grantFunctionDrawerVisible, handleGrantFunctionDrawerVisible] = useState(false)
  const [tmpOperateUser, setTmpOperateUser] = useState()
  const actionRef = useRef()
  const isSuperCustomer = hooks.useCurrentUser().customerId === 0

  const columns = [
    {
      title: "客户账号", dataIndex: "customerAccount", editable: false,
      hideInTable: !isSuperCustomer, search: isSuperCustomer
    },
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
            const {data: selectedFunctionIds} = await userWeb.functionIdsOfUserUsingGET({userId: row.id})
            const {data : functionData} = await customerWeb.treeFunctionOfCustomerUsingGET({customerId: row.customerId})
            setGrantDrawer({
              user: row, visible: true,
              functionData, selectedFunctionIds
            })
            handleGrantFunctionDrawerVisible(true)
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
  return (
    <PageContainer>
      <ExProTable actionRef={actionRef} columns={columns}
                  request={userWeb.pageUserUsingGET}
                  onNew={() => handleModalVisible(true)}
                  editable={editable}
      />
      <ModalForm title="新增用户" width="400px"
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
        <ProFormText name="name" label="用户姓名" rules={nameRules}/>
        <ProFormText name="account" label="登录账号"
                     rules={[
                       {required: true, message: "用户账号不能为空！"},
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
      {grantDrawer.visible && (
        <FunctionDrawerForm title="功能授权" width="500px" visible={true}
                            drawerProps={{destroyOnClose: true}} functionData={grantDrawer.functionData}
                            initialValues={{functionIds: grantDrawer.selectedFunctionIds}}
                            onVisibleChange={handleGrantDrawerVisible}
                            onFinish={async (v) => {
                              return userWeb.grantFunctionsToUserUsingPOST({...v, userId: grantDrawer.user.id})
                            }}
        />
      )}
    </PageContainer>
  )
}
