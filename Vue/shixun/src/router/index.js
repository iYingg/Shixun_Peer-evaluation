import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/login.vue')
  },
  {
    path: '/home',
    name: '首页',
    component: () => import('../layout'),
    redirect:'/home/index',
    children:[
      {
        path: '/home/index',
        name: '主页',
        component: () => import('../views/index/index')
      },
      // {
      //   path: '/home/activ/assignCourse',
      //   name:'发布作业',
      //   component:()=> import('../views/activ/assignCourse')
      // }
    ]
  },
  {
    path: '/test',
    name: 'test',
    component: () => import('../views/test.vue'),
  },

]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
