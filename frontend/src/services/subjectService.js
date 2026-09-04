const API_URL = 'http://localhost:8080'

export async function listSubjects() {
    const response = await fetch(`${API_URL}/subjects`)

    if (!response.ok) {
        throw new Error('Erro ao buscar disciplinas.')
    }

    return await response.json()
}

export async function createSubject(subject) {
    const response = await fetch(`${API_URL}/subjects`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(subject)
    })

    if (!response.ok) {
        throw new Error('Erro ao cadastrar disciplina.')
    }

    return await response.json()
}

export async function getSubjectById(id) {
    const response = await fetch(`${API_URL}/subjects/${id}`)

    if (!response.ok) {
        throw new Error('Erro ao buscar disciplina.')
    }

    return await response.json()
}