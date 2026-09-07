const API_URL = 'http://localhost:8080/tasks'

export async function listTasks(params = {}) {
    const query = new URLSearchParams()
    if (params.subjectId) query.append('subjectId', params.subjectId)
    if (params.status) query.append('status', params.status)
    if (params.priority) query.append('priority', params.priority)

    const url = query.toString() ? `${API_URL}?${query}` : API_URL
    const response = await fetch(url)

    if (!response.ok) {
        throw new Error('Erro ao buscar tarefas.')
    }

    return await response.json()
}

export async function createTask(task) {
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(task)
    })

    if (!response.ok) {
        throw new Error('Erro ao criar tarefa.')
    }

    return await response.json()
}

export async function updateTask(id, task) {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(task)
    })

    if (!response.ok) {
        throw new Error('Erro ao atualizar tarefa.')
    }

    return await response.json()
}

export async function deleteTask(id) {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE'
    })

    if (!response.ok) {
        throw new Error('Erro ao excluir tarefa.')
    }

    if (response.status === 204) return true
    return await response.json()
}

export async function updateTaskStatus(id, status) {
    const response = await fetch(`${API_URL}/${id}/status?status=${status}`, {
        method: 'PATCH'
    })

    if (!response.ok) {
        throw new Error('Erro ao atualizar o status da tarefa.')
    }

    return await response.json()
}