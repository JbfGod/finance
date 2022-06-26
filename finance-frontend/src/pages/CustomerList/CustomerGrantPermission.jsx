import React, {useEffect, useRef, useState} from 'react';
import PageContainer from "@/components/PageContainer";
import {Button, Col, Empty, Tree} from "antd";
import {history} from "umi"
import ProCard from "@ant-design/pro-card";
import ExProTable from "@/components/Table/ExtProTable";
import * as customerCategoryWeb from "@/services/swagger/customerCategoryWeb";
import * as customerWeb from "@/services/swagger/customerWeb";
import {GrantResourceForm} from "@/pages/ResourceDrawerForm";
import * as resourceWeb from "@/services/swagger/resourceWeb";

export default () => {
  const [selectedCategory, setSelectedCategory] = useState({id: 0, number: "0"})
  const selectedCategoryId = selectedCategory.id
  const [customerCategoryTreeData, setCustomerCategoryTreeData] = useState([])
  const [selectedRowKeys, setSelectedRowKeys] = useState([])
  const [selectedCustomerId] = selectedRowKeys
  const [resourcesData, setResourcesData] = useState([])
  const [selectedResourceIdentifies, setSelectedResourceIdentifies] = useState([])

  // 加载客户分类
  const fetchTreeCustomerCategory = async () => {
    const {data} = await customerCategoryWeb.treeCustomerCategoryUsingGET()
    setCustomerCategoryTreeData([{id: 0, number: "0", name: "全部分类", children: data}])
  }
  // 加载所有功能权限
  const fetchTreeResource = async () => {
    const {data} = await resourceWeb.treeResourcesUsingGET()
    setResourcesData(data)
  }
  // 加载选中客户拥有的功能权限
  const fetchResourceIdsOfCustomer = async () => {
    if (selectedCustomerId) {
      const {data} = await customerWeb.resourceIdsOfCustomerUsingGET({customerId: selectedCustomerId})
      setSelectedResourceIdentifies(data)
    }
  }
  useEffect(() => {
    fetchTreeCustomerCategory()
    fetchTreeResource()
  }, [])

  useEffect(() => {
    fetchResourceIdsOfCustomer()
  }, [selectedCustomerId])
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
        <ProCard ghost gutter={[8, 0]}>
          <ProCard colSpan={5} bordered className="cardCommon">
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
          </ProCard>
          <Col span={selectedCustomerId ? 11 : 19}>
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
            <Col span={8}>
              <ProCard>
                {selectedCustomerId && (
                  <GrantResourceForm resourceIdentifies={selectedResourceIdentifies}
                                     resourceData={resourcesData}
                                     onFinish={async (v) => {
                                       return customerWeb.grantResourceToCustomerUsingPOST({
                                         ...v,
                                         customerId: selectedCustomerId
                                       })
                                     }}/>
                )}
              </ProCard>
            </Col>
          )}
        </ProCard>
      ) : (
        <ProCard colSpan={24} bordered>
          <Empty image={Empty.PRESENTED_IMAGE_SIMPLE}
                 description={<span>暂无客户分类数据</span>}>
            <Button type="primary" onClick={() => history.push("/system/customerCategory")}>前往客户分类管理添加客户分类</Button>
          </Empty>
        </ProCard>
      )}
    </PageContainer>
  )
}
