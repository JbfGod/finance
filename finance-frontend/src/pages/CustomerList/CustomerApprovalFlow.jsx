import React, {useEffect, useRef, useState} from 'react';
import PageContainer from "@/components/PageContainer";
import {Badge, Button, Card, Col, Empty, message, Modal, Row, Space, Transfer, Tree} from "antd";
import {history, useModel} from "umi"
import ProCard from "@ant-design/pro-card";
import ExProTable from "@/components/Table/ExtProTable";
import * as customerCategoryWeb from "@/services/swagger/customerCategoryWeb";
import * as customerWeb from "@/services/swagger/customerWeb";
import {flowItemsUsingGET, saveFlowItemUsingPOST} from "@/services/swagger/approvalFlowWeb";
import {useModalWithParam} from "@/utils/hooks";
import {EditableProTable} from "@ant-design/pro-table";
import {ApprovalBusinessModules} from "@/constants";

export default () => {
  const [selectedCategory, setSelectedCategory] = useState({id: 0, number: "0"})
  const selectedCategoryId = selectedCategory.id
  const [customerCategoryTreeData, setCustomerCategoryTreeData] = useState([])
  const [selectedRowKeys, setSelectedRowKeys] = useState([])
  const [selectedCustomerId] = selectedRowKeys

  // 加载客户分类
  const fetchTreeCustomerCategory = async () => {
    const {data} = await customerCategoryWeb.treeCustomerCategoryUsingGET()
    setCustomerCategoryTreeData([{id: 0, number: "0", name: "全部分类", children: data}])
  }
  useEffect(() => {
    fetchTreeCustomerCategory()
  }, [])

  const actionRef = useRef()
  const columns = [
    {
      title: "客户名称", dataIndex: "name",
      width: 125
    },
    {
      title: "客户编号", dataIndex: "number",
      width: 125
    },
  ]
  const hasCustomerCategory = !!customerCategoryTreeData?.[0]?.children?.[0]
  return (
    <PageContainer>
      {hasCustomerCategory ? (
        <Row gutter={[8, 0]}>
          <Col span={5}>
            <Card bordered className="cardCommon">
              <Tree showLine={{showLeafIcon: false}}
                    selectedKeys={[selectedCategoryId]} defaultExpandAll
                    fieldNames={{title: "name", key: "id"}} treeData={customerCategoryTreeData}
                    onSelect={(keys, {node}) => {
                      setSelectedCategory(node)
                      if (node.id !== selectedCategoryId) {
                        actionRef.current?.reload()
                      }
                    }}
              />
            </Card>
          </Col>
          <Col span={selectedCustomerId ? 10 : 19}>
            <ExProTable actionRef={actionRef} columns={columns} editable={false} toolBarRender={false}
                        tableAlertRender={false}
                        onRow={(record) => ({
                          onClick: () => setSelectedRowKeys([record.id])
                        })}
                        rowSelection={{
                          type: 'radio',
                          selectedRowKeys: selectedRowKeys,
                          onChange: (keys) => {
                            setSelectedRowKeys(keys)
                          }
                        }}
                        params={{categoryId: selectedCategoryId || undefined}}
                        expandable={{expandRowByClick: true}}
                        request={(params) => {
                          setSelectedRowKeys([])
                          return customerWeb.pageCustomerUsingGET(params)
                        }}
            />
          </Col>

          {selectedCustomerId && (
            <Col span={9}>
              <ApprovalFlowItemTable customerId={selectedCustomerId}/>
            </Col>
          )}
        </Row>
      ) : (
        <ProCard colSpan={24} bordered>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}
                 description={<span>暂无客户分类数据</span>}>
            <Button type="primary" onClick={() => history.push("/base/customerCategory")}>前往客户分类管理添加客户分类</Button>
          </Empty>
        </ProCard>
      )}
    </PageContainer>
  )
}

function ApprovalFlowItemTable({customerId}) {
  const modal = useModalWithParam()
  const actionRef = useRef()
  const [loading, setLoading] = useState(false)
  const [activeKey, setActiveKey] = useState("EXPENSE_BILL")
  const [editableKeys, setEditableRowKeys] = useState()
  const [dataSource, setDataSource] = useState([])
  const delRowById = (id) => {
    setDataSource(dataSource.filter(data => data.id !== id))
  }
  const setFlowItemById = (id) => {
    return (value) => {
      setDataSource(dataSource.map(data => {
        if (data.id === id) {
          return {...data, ...value}
        }
        return data
      }))
      actionRef.current?.reload()
    }
  }

  const columns = [
    {title: "审批级别", dataIndex: "level", width: 50, editable: false, render: (_, row, index) => index + 1},
    {title: "部门", dataIndex: "department"},
    {
      title: "操作", dataIndex: "operate", editable: false,
      render: (_, row) => (
        <Space size={16}>
          <Badge size="small" dot={true} count={row.approverIds?.length||0}>
            <a onClick={() => modal.open({flowItem: row, setFlowItem: setFlowItemById(row.id)})}>设置审批人</a>
          </Badge>
          <a onClick={() => delRowById(row.id)}>删除</a>
        </Space>
      )
    },
  ]
  const recordCreatorProps = {
    creatorButtonText: "添加一级审批",
    newRecordType: 'dataSource',
    record: () => ({
      id: `noDb${Date.now()}`
    })
  }
  const toolbar = {
    menu: {
      type: 'tab',
      activeKey: activeKey,
      items: Object.keys(ApprovalBusinessModules).map(key => ApprovalBusinessModules[key]).map(module => ({
        key: module.value,
        label: <span>{module.label}</span>
      })),
      onChange: (key) => {
        setActiveKey(key)
      }
    }
  }
  const toolBarRender = () => {
    return [
      <Button
        type="primary"
        key="save"
        onClick={() => {
          const formValues = {
            customerId,
            businessModule: activeKey,
            flowItems: dataSource
          }
          setLoading(true)
          saveFlowItemUsingPOST(formValues).finally(_ => setLoading(false))
        }}
        loading={loading}
      >
        保存数据
      </Button>
    ]
  }
  const editable = {
    type: 'multiple',
    editableKeys,
    actionRender: (row, config, defaultDoms) => {
      return [defaultDoms.delete];
    },
    onValuesChange: (record, recordList) => {
      if (recordList.length > 6) {
        message.warn("最多添加6级审批！")
        return
      }
      setDataSource(recordList)
    },
    onChange: setEditableRowKeys,
  }
  useEffect(() => {
    flowItemsUsingGET({customerId, businessModule: activeKey}).then(({data}) => setDataSource(data))
  }, [activeKey, customerId])
  return (
    <>
      <EditableProTable rowKey="id" value={dataSource} toolbar={toolbar}
                        columns={columns} actionRef={actionRef}
                        toolBarRender={toolBarRender} editable={editable}
                        recordCreatorProps={recordCreatorProps}/>
      <ApproverModal modal={modal}/>
    </>
  )
}

function ApproverModal({modal}) {
  const [targetKeys, setTargetKeys] = useState([])
  const {visible, state} = modal
  const {flowItem, setFlowItem} = state
  const {approverIds} = flowItem || {}
  const {approvers} = useModel("useSuperCustomerUser")
  const filterOption = (inputValue, option) => option.name.indexOf(inputValue) > -1

  const handleChange = (newTargetKeys) => {
    setTargetKeys(newTargetKeys)
  }
  const onOk = () => {
    setFlowItem({approverIds: targetKeys})
    modal.close()
  }
  useEffect(() => {
    visible && setTargetKeys(approverIds || [])
  }, [visible])
  return (
    <Modal title="设置审批人员" width={655} open={visible}
           onCancel={modal.close} onOk={onOk}>
      <Transfer
        titles={["姓名-账号", "审批人员"]}
        listStyle={{width: 300, height: 455}}
        dataSource={approvers.map(user => ({...user, key: user.id, title: `${user.name}-${user.account}`}))}
        showSearch
        filterOption={filterOption}
        targetKeys={targetKeys}
        onChange={handleChange}
        render={item => item.title}
      />
    </Modal>
  )
}
