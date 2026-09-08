import { createRouter, createWebHistory } from 'vue-router'

import AppLayout from '../layouts/AppLayout.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views//RegisterView.vue'
import DashboardView from '../views/DashboardView.vue'
import SubjectsView from '@/views/SubjectsView.vue'
import SubjectDetailView from '@/views/SubjectDetailView.vue'


function isTokenExpired(token) {
    if (!token) return true
    try {
        const payloadBase64 = token.split('.')[1]
        const decodedPayload = JSON.parse(atob(payloadBase64))
    
        const currentTime = Math.floor(Date.now() / 1000)
        return decodedPayload.exp < currentTime
    } catch (e) {
        return true
    }
}

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

    const tokenExpired = isTokenExpired(token)

    if (requiresAuth && tokenExpired) {
        localStorage.clear()
        return '/login'
    } 
    
    if (requiresGuest && !tokenExpired) {
        return '/app/dashboard'
    }
})

export default router