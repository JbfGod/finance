const CAN = "can"

const menus = [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        path: '/user/login',
        layout: false,
        name: 'login',
        component: './user/Login',
        access: 'can'
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
        access: 'MENU:/system/user',
        component: './system/UserList/index',
      },
      {
        path: '/system/customerCategory',
        name: '客户分类管理',
        icon: 'smile',
        access: 'MENU:/system/customerCategory',
        component: './system/CustomerCategoryList/index',
      },
      {
        path: '/system/customer',
        name: '客户档案',
        icon: 'smile',
        access: 'MENU:/system/customer',
        component: './system/CustomerList/index',
      },
      {
        path: '/system/customerGrantPermissionPage',
        name: '客户授权管理',
        icon: '/system/customerGrantPermissionPage',
        access: 'MENU:/system/customerGrantPermissionPage',
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
        access: 'MENU:/base/industry',
        component: './base/IndustryList',
      },
      {
        path: '/base/subject',
        name: '科目管理',
        icon: 'smile',
        access: 'MENU:/base/subject',
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
        access: "MENU:/expense/bill",
        component: './expense/BillList',
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
        access: "MENU:/voucher/list",
        component: './voucher',
      },
      {
        path: "/voucher/book",
        name: '科目账簿',
        access: "MENU:/voucher/book",
        component: './voucher/VoucherBook',
      },
      {
        path: "/voucher/currency",
        name: '外币汇率管理',
        access: "MENU:/voucher/currency",
        component: './voucher/CurrencyList',
      }
    ]
  },
  {
    path: '/welcome',
    name: 'welcome',
    icon: 'icon-danwei',
    component: './Welcome',
    access: "can"
  },
  {
    component: './404',
  },
]

export default menus
