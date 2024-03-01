const CAN = "can"

const menus = [
  {
    path: '/login',
    name: '登录',
    layout: false,
    hideInMenu: true,
    component: './Login',
    access: CAN
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
      }
    ]
  },
  {
    component: './Welcome',
  },
  {
    path: '/Manage',
    access: CAN,
    component: './Welcome',
  },
  {
    path: '/Finance',
    access: CAN,
    component: './Welcome',
  },
  {
    path: '/Manage/customer',
    name: '客户管理',
    routes: [
      {
        path: '/Manage/customer/category',
        name: '客户分类管理',
        access: 'MENU:/Manage/customer/category',
        component: './CustomerCategoryList/index',
      },
      {
        name: '客户档案',
        path: '/Manage/customer/archive',
        access: 'MENU:/Manage/customer/archive',
        component: 'CustomerList/index'
      },
      {
        path: '/Manage/customer/approvalFlow',
        name: '审批流程配置',
        icon: '',
        access: 'MENU:/Manage/customer/approvalFlow',
        component: './CustomerList/CustomerApprovalFlow',
      },
    ],
  },
  {
    path: '/Manage/setting',
    name: '设置',
    routes: [
      {
        path: '/Manage/setting/user',
        name: '用户管理',
        icon: 'smile',
        access: 'MENU:/Manage/setting/user',
        component: './UserList/index',
      }
    ],
  },
  {
    path: '/Finance/expense',
    name: '费用报销单管理',
    routes: [
      {
        path: "/Finance/expense/bill",
        name: '费用报销单管理',
        access: "MENU:/Finance/expense/bill",
        component: './ExpenseBillList',
      }
    ],
  },
  {
    path: '/Finance/setting',
    name: '设置',
    routes: [
      {
        name: '科目',
        path: '/Finance/setting/subject',
        access: "MENU:/Finance/setting/subject",
        component: './SubjectList'
      },
      {
        name: '初始余额',
        path: '/Finance/setting/initialBalance',
        access: "MENU:/Finance/setting/initialBalance",
        component: './InitialBalance'
      },
    ],
  },
  {
    path: '/switchIdentity',
    name: '选择用户身份',
    layout: false,
    hideInMenu: true,
    component: './SwitchUserIdentity',
    access: 'isAuth'
  },
  {
    path: '/Finance/voucher',
    name: '凭证管理',
    icon: 'simple',
    routes: [
      {
        path: "/Finance/voucher/record",
        name: '录凭证',
        access: "MENU:/Finance/voucher/record",
        component: './Voucher/RecordVoucher',
      },
      {
        path: "/Finance/voucher/list",
        name: '查凭证',
        access: "MENU:/Finance/voucher/list",
        component: './Voucher',
      },
      {
        path: "/Finance/voucher/book",
        name: '科目账簿',
        access: "MENU:/Finance/voucher/book",
        component: './Voucher/VoucherBook',
      },
      {
        path: "/Finance/voucher/currency",
        name: '外币汇率管理',
        access: "MENU:/Finance/voucher/currency",
        component: './Voucher/CurrencyList',
      },
      {
        path: "/Finance/voucher/accountClose",
        name: '结账',
        access: "MENU:/Finance/voucher/accountClose",
        component: './Voucher/AccountClose',
      }
    ]
  },
  {
    path: '/Finance/book',
    name: '账簿',
    icon: 'simple',
    routes: [
      {
        path: '/Finance/book/accountBalance',
        name: '科目余额表',
        icon: 'smile',
        access: 'MENU:/Finance/book/accountBalance',
        component: './Report/AccountBalance',
      },
      {
        path: '/Finance/book/generalLedger',
        name: '总分类账',
        icon: 'smile',
        access: 'MENU:/Finance/book/generalLedger',
        component: './Report/GeneralLedger',
      },
      {
        path: '/Finance/book/subLedger',
        name: '明细分类账',
        icon: 'smile',
        access: 'MENU:/Finance/book/subLedger',
        component: './Report/SubLedger',
      },
      {
        path: '/Finance/book/dailyCash',
        name: '现金日报表',
        icon: 'smile',
        access: 'MENU:/Finance/book/dailyCash',
        component: './Report/DailyCash',
      },
      {
        path: '/Finance/book/dailyBank',
        name: '银行存款日报表',
        icon: 'smile',
        access: 'MENU:/Finance/book/dailyBank',
        component: './Report/DailyBank',
      }
    ]
  },
  {
    path: '/Finance/report',
    name: '报表',
    icon: 'simple',
    routes: [
      {
        path: '/Finance/report/profit',
        name: '利润表',
        icon: 'smile',
        access: 'MENU:/Finance/report/profit',
        component: './Report/Profit',
      },
      {
        path: '/Finance/report/cashFlow',
        name: '现金流量表',
        icon: 'smile',
        access: 'MENU:/Finance/report/cashFlow',
        component: './Report/CashFlow',
      },
      {
        path: '/Finance/report/balanceSheet',
        name: '资产负债表',
        icon: 'smile',
        access: 'MENU:/Finance/report/balanceSheet',
        component: './Report/AssetsLiabilities',
      },
    ]
  }
]

export default menus
