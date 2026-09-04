import { createRouter, createWebHistory } from 'vue-router'

import AppLayout from '../layouts/AppLayout.vue'
import LoginView from '../views/LoginView.vue'
import DashboardView from '../views/DashboardView.vue'
import TasksView from '../views/TasksView.vue'

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        component: LoginView
    },
    {
        path: '/app',
        component: AppLayout,
        children: [
            {
                path: 'dashboard',
                component: DashboardView
            },
            {
                path: 'tasks',
                component: TasksView
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router