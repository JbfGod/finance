const CAN = "can"

const menus = [
  {
    path: '/user',
    routes: [
      {
        path: '/user/login',
        layout: false,
        name: 'login',
        component: './Login',
        access: 'can'
      },
      {
        name: '选择用户身份',
        layout: false,
        path: '/user/switchIdentity',
        component: './SwitchUserIdentity',
        access: 'isAuth'
      },
      {
        name: '选择客户单位',
        path: '/user/switchCustomer',
        component: './SwitchCustomer',
        access: 'isAuth'
      },
      {
        component: './404',
      },
    ],
  },
  {
    path: '/approval',
    icon: 'simple',
    routes: [
      {
        name: '费用报销单审批',
        path: '/approval/expenseBill',
        component: './Approval/ExpenseBillApproval',
        access: 'isAuth'
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
        component: './ExpenseBillList',
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
        component: './VoucherList',
      },
      {
        path: "/voucher/book",
        name: '科目账簿',
        access: "MENU:/voucher/book",
        component: './VoucherList/VoucherBook',
      },
      {
        path: "/voucher/currency",
        name: '外币汇率管理',
        access: "MENU:/voucher/currency",
        component: './VoucherList/CurrencyList',
      },
      {
        path: "/voucher/batchAuditing",
        name: '批量审批',
        access: "MENU:/voucher/batchAuditing",
        component: './VoucherList/BatchAuditing',
      },
      {
        path: "/voucher/batchBookkeeping",
        name: '批量记账',
        access: "MENU:/voucher/batchBookkeeping",
        component: './VoucherList/BatchBookkeeping',
      },
      {
        path: "/voucher/accountClose",
        name: '月度关账',
        access: "MENU:/voucher/accountClose",
        component: './VoucherList/AccountClose',
      }
    ]
  },
  {
    path: 'system',
    name: '系统管理',
    icon: 'xitongguanli',
    local: false,
    routes: [
      {
        path: '/system/user',
        name: '用户管理',
        icon: 'smile',
        access: 'MENU:/system/user',
        component: './UserList/index',
      },
      {
        path: '/system/customerGrantPermissionPage',
        name: '客户授权管理',
        icon: '/system/customerGrantPermissionPage',
        access: 'MENU:/system/customerGrantPermissionPage',
        component: './CustomerList/CustomerGrantPermission',
      },
      {
        path: '/system/customerApprovalFlow',
        name: '审批流程配置',
        icon: '/system/customerGrantPermissionPage',
        access: 'MENU:/system/customerGrantPermissionPage',
        component: './CustomerList/CustomerApprovalFlow',
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
        path: '/base/customerCategory',
        name: '客户分类管理',
        icon: 'smile',
        access: 'MENU:/base/customerCategory',
        component: './CustomerCategoryList/index',
      },
      {
        path: '/base/customer',
        name: '客户档案',
        icon: 'smile',
        access: 'MENU:/base/customer',
        component: './CustomerList/index',
      },
      {
        path: '/base/industry',
        name: '行业管理',
        icon: 'smile',
        access: 'MENU:/base/industry',
        component: './IndustryList',
      },
      {
        path: '/base/subject',
        name: '科目管理',
        icon: 'smile',
        access: 'MENU:/base/subject',
        component: './SubjectList',
      },
      {
        path: "/base/initialBalance",
        name: '初始余额',
        access: "MENU:/base/initialBalance",
        component: './InitialBalance',
      },
      {
        component: './404',
      },
    ]
  },
  {
    path: '/',
    name: 'welcome',
    icon: 'icon-danwei',
    component: './Welcome',
    access: "can"
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
