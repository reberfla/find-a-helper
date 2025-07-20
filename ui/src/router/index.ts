import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../modules/HomeView.vue'),
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../modules/AboutView.vue'),
    },
    {
      path: '/contact',
      name: 'contact',
      component: () => import('../modules/ContactView.vue'),
    },
    {
      path: '/profil',
      name: 'profil',
      component: () => import('../modules/user/UserView.vue'),
    },
    {
      path: '/services',
      name: 'services',
      component: () => import('../modules/ServiceView.vue'),
    },
  ],
})

export default router
