import axios from 'axios'
import router from '../router'

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json'
    }
})

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token')

        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }

        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

api.interceptors.response.use(
    (response) => response,
    (error) => {
        const status = error.response ? error.response.status : null
        const url = error.config ? error.config.url : ''

        const isAuthRequest = url.includes('/register') || url.includes('/login') || url.includes('/auth')

        if ((status === 401 || status === 403) && !isAuthRequest) {
            localStorage.removeItem('token')

            if (router.currentRoute.value.path !== '/login') {
                router.push('/login')
            }
        }

        return Promise.reject(error)
    }
)

export default api