import { createRouter, createWebHistory } from 'vue-router'

import AppLayout from '../layouts/AppLayout.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views//RegisterView.vue'
import DashboardView from '../views/DashboardView.vue'
import SubjectsView from '@/views/SubjectsView.vue'
import SubjectDetailView from '@/views/SubjectDetailView.vue'

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        component: LoginView,
        meta: { requiresGuest: true }
    },
    {
        path: '/register',
        component: RegisterView,
        meta: { requiresGuest: true }
    },
    {
        path: '/app',
        component: AppLayout,
        meta: { requiresAuth: true },
        children: [
            {
                path: 'dashboard',
                component: DashboardView
            },
            {
                path: 'subjects',
                component: SubjectsView
            },
            {
                path: 'subjects/:id',
                component: SubjectDetailView
            }
        ]
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: '/login'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from) => {
    const token = localStorage.getItem('token')
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
    const requiresGuest = to.matched.some(record => record.meta.requiresGuest)

    if (requiresAuth && !token) {
        return '/login'
    } 
    
    if (requiresGuest && token) {
        return '/app/dashboard'
    }
})

export default router