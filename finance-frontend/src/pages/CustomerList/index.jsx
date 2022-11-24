import React, {useEffect, useRef, useState} from 'react';
import PageContainer from "@/components/PageContainer";
import {Button, Col, Empty, message, Tree} from "antd";
import {history} from "umi"
import * as subjectWeb from "@/services/swagger/subjectWeb";
import {useModalWithParam} from "@/utils/hooks";
import ProCard from "@ant-design/pro-card";
import {
  ModalForm,
  ProFormDateRangePicker,
  ProFormItem,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea
} from "@ant-design/pro-form";
import ExProTable from "@/components/Table/ExtProTable";
import {ExtConfirmDel} from "@/components/Table/ExtPropconfirm";
import * as customerCategoryWeb from "@/services/swagger/customerCategoryWeb";
import {CUSTOMER_TYPE} from "@/constants";
import * as customerWeb from "@/services/swagger/customerWeb";
import * as industryWeb from "@/services/swagger/industryWeb";
import ExtTreeSelect from "@/components/Common/ExtTreeSelect";
import {listUserFromSuperCustomerUsingGET} from "@/services/swagger/userWeb";

export default () => {
  const [selectedCategory, setSelectedCategory] = useState({id: 0, number: "0"})
  const selectedCategoryId = selectedCategory.id
  const [industryTreeData, setIndustryTreeData] = useState([])
  const [customerCategoryTreeData, setCustomerCategoryTreeData] = useState([])
  const formModal = useModalWithParam()

  const openModalWithCheck = (params) => {
    if (selectedCategory.hasLeaf || selectedCategoryId === 0) {
      return message.warn("新增客户只能选择叶子节点的分类！")
    }
    formModal.open(params)
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
      title: "客户编号", dataIndex: "number", editable: false,
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
      title: "联系人", dataIndex: "contactName", search: false,
      width: 85,
    },
    {
      title: "联系电话", dataIndex: "telephone", search: false,
      width: 100,
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
          <a key="edit" onClick={(e) => {
            e.stopPropagation()
            formModal.open({mode: "edit", initialValues: row})
          }}>编辑</a>,
          <ExtConfirmDel key="del" onConfirm={async () => {
            await subjectWeb.deleteSubjectUsingDELETE({id: row.id})
            actionRef.current?.reload()
          }}/>,
        ]
      }
    },
  ]
  const hasCustomerCategory = !!customerCategoryTreeData?.[0]?.children?.[0]
  return (
    <PageContainer>
      {hasCustomerCategory ? (
        <ProCard ghost gutter={[8, 0]}>
          <ProCard colSpan={5} bordered className="cardCommon">
            <Tree showLine={{showLeafIcon: false}}
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
          <Col span={19}>
            <ExProTable actionRef={actionRef} columns={columns}
                        scroll={{x: 1200, y: 600}} editable={false}
                        params={{categoryId: selectedCategoryId || undefined}}
                        expandable={{expandRowByClick: true}}
                        onNew={() => openModalWithCheck({mode: "add"})}
                        request={customerWeb.pageCustomerUsingGET}
            />
            <AddOrUpdateFormModal modal={formModal} onVisibleChange={formModal.handleVisible}
                                  categoryId={selectedCategoryId}
                                  industryTreeData={industryTreeData} onSuccess={() => actionRef.current?.reload()}
            />
          </Col>
        </ProCard>
      ) : (
        <ProCard colSpan={24} bordered>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}
                 description={<span>暂无客户分类数据，无法添加客户</span>}>
            <Button type="primary" onClick={() => history.push("/base/customerCategory")}>前往客户分类管理添加客户分类</Button>
          </Empty>
        </ProCard>
      )}
    </PageContainer>
  )
}

function AddOrUpdateFormModal({modal, categoryId, onSuccess, industryTreeData, ...props}) {
  const {state: {mode, initialValues}} = modal
  const isAddMode = mode === "add", isViewMode = mode === "view", isEditMode = mode === "edit"
  const title = isAddMode ? "新增客户" : isEditMode ? "编辑客户" : "客户详情"
  const [directors, setDirectors] = useState([])
  const directorOptions = directors.map(user => ({label: user.name, value: user.id}))
  const loadDirectors = () => {
    listUserFromSuperCustomerUsingGET().then(({data}) => setDirectors(data || []))
  }
  useEffect(() => {
    loadDirectors()
  }, [])
  return (
    <ModalForm width={750} title={title} open={modal.visible}
               grid={true} layout="inline" rowProps={{gutter: [0, 12]}}
               modalProps={{
                 destroyOnClose: true
               }}
               onFinish={(values) => {
                 if (isViewMode) {
                   return true
                 }
                 const {dateRange} = values;
                 const formValue = {
                   ...values,
                   categoryId,
                   effectTime: dateRange?.[0],
                   expireTime: dateRange?.[1],
                 }
                 if (isAddMode) {
                   return customerWeb.addCustomerUsingPOST(formValue).then(() => {
                     onSuccess && onSuccess()
                     return true
                   })
                 }
                 return customerWeb.updateCustomerUsingPUT({...formValue, id: initialValues.id}).then(() => {
                   onSuccess && onSuccess()
                   return true
                 })
               }}
               initialValues={initialValues?{
                 ...initialValues,
                 dateRange: [initialValues.effectTime, initialValues.expireTime]
               } : {
                 type: "RENT",
                 enabled: true,
                 useForeignExchange: false
               }}
               {...props}
    >
      <ProFormSelect name="type" label="客户类型" options={Object.values(CUSTOMER_TYPE)}
                     allowClear={false} colProps={{span: 12}}
                     rules={[{required: true, message: "客户类型不能为空！"}]}/>
      <Col span={12}>
        <ProFormItem name="industryId" label="行业分类" rules={[{required: true, message: "行业分类不能为空！"}]}>
          <ExtTreeSelect options={industryTreeData} placeholder="只能选择叶子节点"
                         treeLine={{showLeafIcon: false}} style={{width: '100%'}}
                         onlySelectedLeaf={true}
          />
        </ProFormItem>
      </Col>
      <ProFormText name="number" label="客户编号" colProps={{span: 12}} rules={[
        {required: true, message: "客户编号不能为空！"},
        {min: 5, max: 25, message: "客户编号只允许有5-25个字符！"},
        {pattern: /[\da-zA-Z]{5,25}/, message: "客户编号只允许包含数字和字母！"}
      ]}/>
      <ProFormText name="name" label="客户名称" colProps={{span: 12}}
                   rules={[{required: true, message: "客户名称不能为空！"}]}/>
      <ProFormText name="contactName" label="联系人" colProps={{span: 12}}/>
      <ProFormText name="telephone" label="联系电话" colProps={{span: 12}}/>
      <ProFormText name="bankAccountName" label="开户银行" colProps={{span: 12}}/>
      <ProFormText name="bankAccount" label="银行账号" colProps={{span: 12}}/>
      <ProFormDateRangePicker name="dateRange" label="代理(租用)日期"
                              placeholder={["开始日期", "过期日期"]}
                              format="yyyy-MM-DD" colProps={{span: 12}}/>
      <ProFormSwitch name="enabled" label="客户状态" colProps={{span: 6}} checkedChildren="启用" unCheckedChildren="停用"/>
      <ProFormSwitch name="useForeignExchange" colProps={{span: 6}} label="是否使用外汇" checkedChildren="是"
                     unCheckedChildren="否"/>
      <ProFormSelect name="businessUserId" label="业务负责人" showSearch options={directorOptions}/>
      <ProFormTextArea name="remark" label="备注" fieldProps={{showCount: true, maxLength: 255}}/>
    </ModalForm>
  )
}
