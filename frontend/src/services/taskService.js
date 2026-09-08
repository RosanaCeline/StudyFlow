import api from './api'

export async function listTasks(params = {}) {
    const response = await api.get('/api/tasks', {
        params: {
            subjectId: params.subjectId,
            status: params.status,
            priority: params.priority
        }
    })

    return response.data
}

export async function createTask(task) {
    const response = await api.post('/api/tasks', task)

    return response.data
}

export async function updateTask(id, task) {
    const response = await api.put(`/api/tasks/${id}`, task)

    return response.data
}

export async function deleteTask(id) {
    const response = await api.delete(`/api/tasks/${id}`)

    if (response.status === 204) {
        return true
    }

    return response.data
}

export async function updateTaskStatus(id, status) {
    const response = await api.patch(
        `/api/tasks/${id}/status`,
        null,
        {
            params: {
                status
            }
        }
    )

    return response.data
}
