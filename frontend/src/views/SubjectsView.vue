<script setup>
import { onMounted, ref } from 'vue'
import { listSubjects, createSubject } from '../services/subjectService'

const subjects = ref([])
const loading = ref(false)
const error = ref('')

const newSubject = ref({
    name: '',
    description: '',
    color: '#494c4e',
    situation: 'ACTIVE'
})

async function loadSubjects() {
    loading.value = true
    error.value = ''

    try {
        subjects.value = await listSubjects()
    } catch (err) {
        error.value = 'Não foi possível carregar as disciplinas.'
    } finally {
        loading.value = false
    }
}

async function handleCreate() {
    try {
        const createdSubject = await createSubject(newSubject.value)

        subjects.value.push(createdSubject)

        newSubject.value = {
            name: '',
            description: '',
            color: '#1a73e8',
            situation: 'ACTIVE'
        }

        document.getElementById('createSubjectModal').querySelector('.btn-close').click()

    } catch (err) {
        error.value = 'Não foi possível cadastrar a disciplina.'
    }
}

onMounted(() => {
    loadSubjects()
})
</script>

<template>
    <div class="classroom-container">
        <div class="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
            <div>
                <h2 class="fw-normal mb-1">Minhas Disciplinas</h2>
                <p class="text-muted mb-0 small">Gerencie e acesse seus materiais de estudo</p>
            </div>
            <button
                type="button"
                class="btn btn-primary rounded-pill px-3 shadow-sm d-flex align-items-center gap-2"
                data-bs-toggle="modal"
                data-bs-target="#createSubjectModal"
            >
                <i class="bi bi-plus-lg fs-5"></i>
                <span>Criar disciplina</span>
            </button>
        </div>

        <div v-if="error" class="alert alert-danger rounded-3 shadow-sm">
            {{ error }}
        </div>

        <div v-if="loading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Carregando...</span>
            </div>
        </div>

        <div v-else-if="subjects.length === 0" class="text-center py-5 empty-state">
            <div class="empty-icon-wrapper mb-3">
                <i class="bi bi-journal-bookmark display-4 text-secondary"></i>
            </div>
            <h5 class="fw-normal">Nenhuma disciplina por aqui</h5>
            <p class="text-muted small">Crie uma nova disciplina para organizar seus tópicos e matérias.</p>
            <button
                type="button"
                class="btn btn-outline-primary rounded-pill px-4 mt-2"
                data-bs-toggle="modal"
                data-bs-target="#createSubjectModal"
            >
                Criar turma/disciplina
            </button>
        </div>

        <!-- Grid de Cards estilo Google Classroom -->
        <div v-else class="row g-4">
            <div
                v-for="subject in subjects"
                :key="subject.id"
                class="col-12 col-md-6 col-xl-4"
            >
                <div class="card classroom-card h-100 shadow-sm border-0">
                    
                    <!-- Banner Superior do Card (Estilo Classroom) -->
                    <div 
                        class="card-header-banner p-3 d-flex flex-column justify-content-between"
                        :style="{ backgroundColor: subject.color || '#1a73e8' }"
                    >
                        <div class="d-flex justify-content-between align-items-start">
                            <h4 class="card-title text-white text-truncate mb-0 fw-normal" :title="subject.name">
                                {{ subject.name }}
                            </h4>
                            <button class="btn btn-link text-white p-0 opacity-75 hover-opacity-100">
                                <i class="bi bi-three-dots-vertical fs-5"></i>
                            </button>
                        </div>
                        <p class="text-white-50 small text-truncate mb-0">
                            {{ subject.description || 'Sem descrição' }}
                        </p>
                    </div>

                    <!-- Corpo do Card -->
                    <div class="card-body bg-white d-flex flex-column justify-content-between p-3">
                        <div class="d-flex align-items-center justify-content-between text-muted small">
                            <span>Status:</span>
                            <span 
                                class="badge rounded-pill fw-normal"
                                :class="subject.situation === 'ACTIVE' ? 'bg-success-subtle text-success' : 'bg-secondary-subtle text-secondary'"
                            >
                                {{ subject.situation === 'ACTIVE' ? 'Ativa' : 'Inativa' }}
                            </span>
                        </div>
                    </div>

                    <!-- Rodapé do Card com ícones de ação -->
                    <div class="card-footer bg-white border-top-0 d-flex justify-content-end gap-3 p-3 text-secondary">
                        <button class="btn btn-sm btn-icon text-muted" title="Pasta da disciplina">
                            <i class="bi bi-folder2 fs-5"></i>
                        </button>
                        <button class="btn btn-sm btn-icon text-muted" title="Tarefas">
                            <i class="bi bi-assignment-check fs-5"></i>
                        </button>
                    </div>

                </div>
            </div>
        </div>

        <!-- Modal Estilizado -->
        <div
            class="modal fade"
            id="createSubjectModal"
            tabindex="-1"
            aria-labelledby="createSubjectModalLabel"
            aria-hidden="true"
        >
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg rounded-4">
                    
                    <div class="modal-header border-0 pb-0">
                        <h5 id="createSubjectModalLabel" class="modal-title fw-normal fs-4">
                            Criar disciplina
                        </h5>
                        <button
                            type="button"
                            class="btn-close"
                            data-bs-dismiss="modal"
                            aria-label="Fechar"
                        ></button>
                    </div>

                    <form @submit.prevent="handleCreate">
                        <div class="modal-body py-4">
                            
                            <!-- Nome -->
                            <div class="form-floating mb-3">
                                <input
                                    type="text"
                                    class="form-control rounded-3"
                                    id="subjectName"
                                    v-model="newSubject.name"
                                    placeholder="Nome da disciplina"
                                    required
                                >
                                <label for="subjectName">Nome da disciplina (obrigatório)</label>
                            </div>

                            <!-- Descrição -->
                            <div class="form-floating mb-3">
                                <textarea
                                    class="form-control rounded-3"
                                    id="subjectDesc"
                                    v-model="newSubject.description"
                                    placeholder="Descrição"
                                    style="height: 100px"
                                ></textarea>
                                <label for="subjectDesc">Descrição ou seção</label>
                            </div>

                            <!-- Seleção de Cor -->
                            <div class="mb-3">
                                <label class="form-label small text-muted mb-2">Cor do tema</label>
                                <div class="d-flex align-items-center gap-3">
                                    <input
                                        type="color"
                                        class="form-control form-control-color rounded-circle border-0 cursor-pointer"
                                        v-model="newSubject.color"
                                        title="Escolha uma cor"
                                    >
                                    <span class="small text-muted font-monospace">{{ newSubject.color }}</span>
                                </div>
                            </div>

                            <!-- Situação -->
                            <div>
                                <label class="form-label small text-muted mb-1">Situação</label>
                                <select class="form-select rounded-3" v-model="newSubject.situation">
                                    <option value="ACTIVE">Ativa</option>
                                    <option value="INACTIVE">Inativa</option>
                                </select>
                            </div>

                        </div>

                        <div class="modal-footer border-0 pt-0">
                            <button
                                type="button"
                                class="btn btn-link text-decoration-none text-secondary"
                                data-bs-dismiss="modal"
                            >
                                Cancelar
                            </button>
                            <button type="submit" class="btn btn-primary rounded-pill px-4">
                                Criar
                            </button>
                        </div>
                    </form>

                </div>
            </div>
        </div>

    </div>
</template>

<style scoped>
.classroom-card {
    border-radius: 0.75rem;
    overflow: hidden;
    transition: box-shadow 0.2s ease, transform 0.2s ease;
}

.classroom-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 0.5rem 1.25rem rgba(0, 0, 0, 0.12) !important;
}

/* Banner de cabeçalho do card */
.card-header-banner {
    height: 100px;
    background-size: cover;
    background-position: center;
    position: relative;
}

.hover-opacity-100:hover {
    opacity: 1 !important;
}

.btn-icon {
    padding: 0.25rem 0.5rem;
    border-radius: 50%;
}

.btn-icon:hover {
    background-color: #f1f3f4;
}

.cursor-pointer {
    cursor: pointer;
}

/* Inputs flutuantes estilo Google */
.form-floating > .form-control:focus ~ label,
.form-floating > .form-control:not(:placeholder-shown) ~ label {
    color: #1a73e8;
}

.form-control:focus, .form-select:focus {
    border-color: #1a73e8;
    box-shadow: 0 0 0 1px #1a73e8;
}
</style>