export default [
  { path: '/user/login', name: '登录', layout: false, component: './User/Login' },
  { path: '/', name: 'API商店', icon: 'smile', component: './Index' },
  { path: '/my_interface', name: '我的接口', icon: 'StarOutlined', component: './MyInterface' },
  { path: '/interface_info/:id', name: '查看接口', icon: 'smile', component: './InterfaceInfo', hideInMenu: true },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { name: '接口管理', icon: 'table', path: '/admin/interface_info', component: './Admin/InterfaceInfo' },
      { name: '接口分析', icon: 'analysis', path: '/admin/interface_analysis', component: './Admin/InterfaceAnalysis' },
    ],
  },
  { path: '/user/profile', name: '个人中心', icon: 'UserOutlined', component: './User/Profile' },
  { path: '*', layout: false, component: './404' },
];
