import Vue from 'vue'
import Router from 'vue-router'

import error from '@/view/error/error'
import shouye from '@/view/shouye/shouye'
import login from '@/view/login/login'
import menuManger from '@/view/shouye/menuManger'
import userManger from '@/view/shouye/user/userManager'
import datamenu from '@/view/shouye/datamenu'
import datamain from '@/view/shouye/datamain'
import roleManager from '@/view/shouye/role/roleManager'
import testPage from '@/view/testPage/testPage'
import testPageFenZhuang from '@/view/testPage/testPageFenZhuang'
import tikuguanli from '@/view/tiku/tikuguanli'
import xinzengshijuan from '@/view/tiku/xinzengshijuan'
import shiJuanGuanli from '@/view/tiku/shiJuanGuanli'
import newXinZengShiJuan from '@/view/tiku/newXinZengShiJuan'
import scoreFenXi from '@/view/tiku/score/scoreFenXi'
import peiZhi from '@/view/tiku/shijuan/peiZhi'
import peiZhi2 from '@/view/tiku/shijuan/peiZhi2'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/view/shouye/shouye',
      name: 'shouye',
      component: shouye,
      meta: {
        require: true//该路径的访问需要登录
      },
      children: [
        {
          path: "/datamain",
          component: datamain,
          children: [//页面主显示区域的页面路由（采用嵌套路由的方式实现）
            {path: '/error', component: error, meta: {require: false}},
            {path: '/datamain/yewu/menuManger', component: menuManger, meta: {require: true}},
            {path: '/datamain/yewu/userManger', component: userManger, meta: {require: true}},
            {path: '/datamain/yewu/roleManager', component: roleManager, meta: {require: true}},
            {path: "/datamain/yewu/testPage", component: testPage, meta: {requireAuth: true}},
            {path: "/datamain/yewu/testPageFenZhuang", component: testPageFenZhuang, meta: {requireAuth: true}},
            {path: "/datamain/yewu/tikuguanli", component: tikuguanli, meta: {requireAuth: true}},
            {path: "/datamain/yewu/xinzengshijuan", component: xinzengshijuan, meta: {requireAuth: true}},
            {path: "/datamain/yewu/shiJuanGuanli", component: shiJuanGuanli, meta: {requireAuth: true}},
            {path: "/datamain/yewu/newXinZengShiJuan", component: newXinZengShiJuan, meta: {requireAuth: true}},
            {path: "/datamain/yewu/scoreFenXi", component: scoreFenXi, meta: {requireAuth: true}},
            {path: "/datamain/yewu/peiZhi", component: peiZhi, meta: {requireAuth: true}},
            {path: "/datamain/yewu/peiZhi2", component: peiZhi2, meta: {requireAuth: true}}
          ]
        }
      ]
    }, {
      path: '/',
      name: 'login',
      component: login,
      meta: {
        require: false//该路径的访问不需要登录
      }
    }/*,{
      path: '/error',
      name: 'error',
      component: error,
      meta:{
        require:false//该路径的访问不需要登录
      }
    }*/,
    {
      path: "/datamenu",
      component: datamenu,
      meta: {requireAuth: true}
    },

  ]
})
