const CAN = "can"
const USER_PAGE = "sys:userPage"
const CUSTOMER_CATEGORY_PAGE = "sys:customer:categoryPage"
const CUSTOMER_PAGE = "sys:customerPage"
const CUSTOMER_AUTHORIZE_PAGE = "sys:customer:grantResourcePage"
const INDUSTRY_PAGE = "base:industryPage"
const SUBJECT_PAGE = "base:subjectPage"
const EXPENSE_BILL_PAGE = "expense:billPage"
const VOUCHER_LIST_PAGE = "voucher:listPage"
const VOUCHER_BOOK_PAGE = "voucher:bookPage"
const CURRENCY_PAGE = "currencyPage"

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
    path: '/voucher',
    name: '凭证管理',
    icon: 'simple',
    routes: [
      {
        path: "/voucher/list",
        name: '凭证管理',
        access: VOUCHER_LIST_PAGE,
        component: './voucher',
      },
      {
        path: "/voucher/book",
        name: '科目账簿',
        access: VOUCHER_BOOK_PAGE,
        component: './voucher',
      },
      {
        path: "/voucher/currency",
        name: '外币汇率管理',
        access: CURRENCY_PAGE,
        component: './voucher/CurrencyList',
      }
    ]
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
