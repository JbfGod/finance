import React, {useEffect, useRef, useState} from 'react';
import PageContainer from "@/components/PageContainer";
import {Button, Col, Empty, message, Select, Tree} from "antd";
import {history} from "umi"
import {useModalWithParam} from "@/utils/hooks";
import ProCard from "@ant-design/pro-card";
import {ModalForm, ProFormItem, ProFormSelect, ProFormSwitch, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import ExProTable from "@/components/Table/ExtProTable";
import * as customerCategoryWeb from "@/services/swagger/customerCategoryWeb";
import {CUSTOMER_TYPE} from "@/constants";
import * as customerWeb from "@/services/swagger/customerWeb";
import * as industryWeb from "@/services/swagger/industryWeb";
import {getCustomerTokenUsingGET, listUserFromSuperCustomerUsingGET} from "@/services/swagger/userWeb";
import ProFormDatePickerMonth from "@ant-design/pro-form/es/components/DatePicker/MonthPicker";

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
      title: "客户编号", dataIndex: "number", editable: false,
    },
    {
      title: "客户名称", dataIndex: "name", editable: false,
    },
    {
      title: "启用期间", dataIndex: "enablePeriod", editable: false,
    },
    {
      title: "所属分类", dataIndex: "category", editable: false, search: false,
    },
    {
      title: "联系人", dataIndex: "contactName", search: false,
    },
    {
      title: "联系电话", dataIndex: "telephone", search: false,
    },
    {
      title: "创建日期", dataIndex: "createTime", width: 150
      , editable: false, search: false
    },
    {
      title: '操作', dataIndex: 'id', fixed: "right",
      width: 70, valueType: 'option',
      render: (dom, row) => {
        return [
          <a key="jizhang" onClick={(e) => {
            getCustomerTokenUsingGET({customerId: row.id})
              .then(({data}) => {
                window.open(`/Finance?token=${encodeURIComponent(btoa(data))}`)
              })
          }}>记账</a>,
          /*<a key="edit" onClick={(e) => {
            e.stopPropagation()
            formModal.open({mode: "edit", initialValues: row})
          }}>编辑</a>,
          <ExtConfirmDel key="del" onConfirm={async () => {
            await subjectWeb.deleteSubjectUsingDELETE({id: row.id})
            actionRef.current?.reload()
          }}/>,*/
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
                 accountingSystem: "N1",
                 useForeignExchange: false
               }}
               {...props}
    >
      <ProFormSelect name="type" label="客户类型" options={Object.values(CUSTOMER_TYPE)}
                     allowClear={false} colProps={{span: 12}}
                     rules={[{required: true, message: "客户类型不能为空！"}]}/>
      <Col span={12}>
        <ProFormItem name="accountingSystem" label="会计制度" rules={[{required: true, message: "会计制度不能为空"}]}>
          <Select>
            <Select.Option value="N1">小企业会计准则（2013年）</Select.Option>
            <Select.Option value="N2">农民专业合作社财务会计制度</Select.Option>
            <Select.Option value="N3">村集体经济组织会计制度</Select.Option>
            <Select.Option value="N4">新会计准则（2019年）</Select.Option>
          </Select>
        </ProFormItem>
      </Col>
      <ProFormText name="number" label="客户编号" colProps={{span: 12}} rules={[
        {required: true, message: "客户编号不能为空"},
        {min: 5, max: 25, message: "客户编号只允许有5-25个字符"},
        {pattern: /[\da-zA-Z]{5,25}/, message: "客户编号只允许包含数字和字母"},
        {pattern: /^[A-Za-z]/, message: "客户编号必须以字母开头"}
      ]}/>
      <ProFormText name="name" label="客户名称" colProps={{span: 12}}
                   rules={[{required: true, message: "客户名称不能为空"}]}/>
      <ProFormText name="contactName" label="联系人" colProps={{span: 12}}/>
      <ProFormText name="telephone" label="联系电话" colProps={{span: 12}}/>
      {/*<ProFormText name="contactName" label="联系人" colProps={{span: 12}}/>
      <ProFormText name="telephone" label="联系电话" colProps={{span: 12}}/>
      <ProFormText name="bankAccountName" label="开户银行" colProps={{span: 12}}/>
      <ProFormText name="bankAccount" label="银行账号" colProps={{span: 12}}/>
      <ProFormDateRangePicker name="dateRange" label="代理(租用)日期"
                              placeholder={["开始日期", "过期日期"]}
                              format="yyyy-MM-DD" colProps={{span: 24}}/>*/}
      <ProFormDatePickerMonth name="enablePeriod" label="启用期间" transform={v => ({enablePeriod: `${v}`.replace("-", "")})}
                              disabled={!isAddMode} rules={[{required: true, message: "启用期间不能为空"}]}/>
      <ProFormSwitch name="enabled" label="客户状态" colProps={{span: 6}} checkedChildren="启用" unCheckedChildren="停用"/>
      <ProFormSwitch name="useForeignExchange" colProps={{span: 6}} label="是否使用外汇" checkedChildren="是"
                     unCheckedChildren="否"/>
      <ProFormSelect name="businessUserId" label="业务负责人" showSearch options={directorOptions}/>
      <ProFormTextArea name="remark" label="备注" fieldProps={{showCount: true, maxLength: 255}}/>
    </ModalForm>
  )
}
