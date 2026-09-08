import api from './api'

export async function listSubjects() {
    const response = await api.get('/api/subjects')
    return response.data
}

export async function createSubject(subject) {
    const response = await api.post('/api/subjects', subject)
    return response.data
}

export async function getSubjectById(id) {
    const response = await api.get(`/api/subjects/${id}`)
    return response.data
}

export async function deleteSubject(id) {
    const response = await api.delete(`/api/subjects/${id}`)

    if (response.status === 204) {
        return true
    }

    return response.data
}

export async function updateSubject(id, subject) {
    const response = await api.put(`/api/subjects/${id}`, subject)
    return response.data
}