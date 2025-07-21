import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../modules/auth/HomeView.vue'),
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../modules/auth/AboutView.vue'),
    },
    {
      path: '/contact',
      name: 'contact',
      component: () => import('../modules/auth/ContactView.vue'),
    },
    {
      path: '/services',
      name: 'services',
      component: () => import('../modules/auth/ServiceView.vue'),
    },
  ],
})

export default router
