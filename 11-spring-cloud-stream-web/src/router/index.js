import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/websocket',
    name: 'websocket',
    component: () => import(/* webpackChunkName: "about" */ '../views/WebsocketView.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
