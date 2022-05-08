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
        access: "can"
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
        access: 'system:user',
        component: './system/UserList/index',
      },
      {
        path: '/system/customerCategory',
        name: '客户分类管理',
        icon: 'smile',
        access: 'system:customerCategory',
        component: './system/CustomerCategoryList/index',
      },
      {
        path: '/system/customer',
        name: '客户档案',
        icon: 'smile',
        access: 'system:customer',
        component: './system/CustomerList/index',
      },
      {
        path: '/system/customerGrantPermissionPage',
        name: '客户授权管理',
        icon: 'smile',
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
    access: 'base',
    routes: [
      {
        path: '/base/industry',
        name: '行业管理',
        icon: 'smile',
        access: 'base:industry',
        component: './base/IndustryList',
      },
      {
        path: '/base/subject',
        name: '科目管理',
        icon: 'smile',
        access: 'base:subject',
        component: './base/SubjectList',
      },
      {
        component: './404',
      },
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
];
