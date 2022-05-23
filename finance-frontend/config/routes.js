export const CAN = "can"
export const USER_PAGE = "sys:userPage"
export const CUSTOMER_CATEGORY_PAGE = "sys:customer:categoryPage"
export const CUSTOMER_PAGE = "sys:customerPage"
export const CUSTOMER_AUTHORIZE_PAGE = "sys:customer:grantResourcePage"
export const INDUSTRY_PAGE = "base:industryPage"
export const SUBJECT_PAGE = "base:subjectPage"
export const EXPENSE_BILL_PAGE = "expense:billPage"

export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        path: '/user/login',
        layout: false,
        name: 'login',
        component: './user/Login',
        access: CAN
      },
      {
        component: './404',
      },
    ],
  },
  {
    path: 'system',
    name: '系统管理',
    icon: 'simple',
    local: false,
    routes: [
      {
        path: '/system/user',
        name: '用户管理',
        icon: 'smile',
        access: USER_PAGE,
        component: './system/UserList/index',
      },
      {
        path: '/system/customerCategory',
        name: '客户分类管理',
        icon: 'smile',
        access: CUSTOMER_CATEGORY_PAGE,
        component: './system/CustomerCategoryList/index',
      },
      {
        path: '/system/customer',
        name: '客户档案',
        icon: 'smile',
        access: CUSTOMER_PAGE,
        component: './system/CustomerList/index',
      },
      {
        path: '/system/customerGrantPermissionPage',
        name: '客户授权管理',
        icon: CUSTOMER_AUTHORIZE_PAGE,
        access: 'system:customer:grantPermissionPage',
        component: './system/CustomerList/CustomerGrantPermission',
      },
      {
        component: './404',
      },
    ]
  },
  {
    path: '/base',
    name: '基础数据管理',
    icon: 'simple',
    routes: [
      {
        path: '/base/industry',
        name: '行业管理',
        icon: 'smile',
        access: INDUSTRY_PAGE,
        component: './base/IndustryList',
      },
      {
        path: '/base/subject',
        name: '科目管理',
        icon: 'smile',
        access: SUBJECT_PAGE,
        component: './base/SubjectList',
      },
      {
        component: './404',
      },
    ]
  },
  {
    path: '/expense',
    name: '费用报销单管理',
    icon: 'simple',
    routes : [
      {
        path: "/expense/bill",
        name: '费用报销单管理',
        access: EXPENSE_BILL_PAGE,
        component: './expense/BillList',
      },
      {
        path: '/expense/newBill',
        name: '新增费用报销单',
        access: EXPENSE_BILL_PAGE,
        component: './expense/BillList/BillForm',
      },
      {
        path: '/expense/editBill',
        name: '编辑费用报销单',
        access: EXPENSE_BILL_PAGE,
        component: './expense/BillList/BillForm',
      }
    ],

  },
  {
    path: '/welcome',
    name: 'welcome',
    icon: 'icon-danwei',
    component: './Welcome',
    access: CAN
  },
  {
    component: './404',
  },
];
