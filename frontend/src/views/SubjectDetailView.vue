<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSubjectById } from '../services/subjectService'

const route = useRoute()
const router = useRouter()

const subject = ref(null)
const loading = ref(false)
const error = ref('')

async function loadSubject() {
    loading.value = true
    error.value = ''

    try {
        subject.value = await getSubjectById(route.params.id)
    } catch (err) {
        error.value = 'Não foi possível carregar a disciplina.'
    } finally {
        loading.value = false
    }
}

function formatDateTime(dateString) {
    if (!dateString) return 'Data não informada'
    const date = new Date(dateString)
    if (isNaN(date.getTime())) return dateString

    const dateFormatted = date.toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    })
    const timeFormatted = date.toLocaleTimeString('pt-BR', {
        hour: '2-digit',
        minute: '2-digit'
    })

    return `${dateFormatted} às ${timeFormatted}`
}

function getSituationLabel(situation) {
    const map = {
        ACTIVE: 'Ativa',
        COMPLETED: 'Concluída',
        ARCHIVED: 'Arquivada'
    }
    return map[situation] || 'Desconhecida'
}

function getSituationBadgeClass(situation) {
    const map = {
        ACTIVE: 'bg-success-subtle text-success border border-success-subtle',
        COMPLETED: 'bg-primary-subtle text-primary border border-primary-subtle',
        ARCHIVED: 'bg-secondary-subtle text-secondary border border-secondary-subtle'
    }
    return map[situation] || 'bg-light text-dark'
}

function goBack() {
    router.push('/app/subjects')
}

function openColorPicker() {
    // Lógica para abrir modal/popover de alteração de cor futuramente
    console.log('Abrir seletor de cor')
}

onMounted(() => {
    loadSubject()
})
</script>

<template>
    <div class="w-100">

        <!-- Carregando -->
        <div v-if="loading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Carregando...</span>
            </div>
        </div>

        <!-- Erro -->
        <div v-else-if="error" class="alert alert-danger shadow-sm">
            {{ error }}
        </div>

        <!-- Disciplina -->
        <div v-else-if="subject">

            <!-- Botão Voltar -->
            <button
                type="button"
                class="btn btn-link text-decoration-none p-0 mb-3 text-secondary"
                @click="goBack"
            >
                <i class="bi bi-arrow-left me-2"></i>
                Voltar para disciplinas
            </button>

            <!-- Card de Destaque com o Banner de Cor atrás do Título -->
            <div class="card subject-hero-card shadow-sm border-0 mb-4">
                
                <!-- Banner de Fundo (Usa a cor da disciplina) -->
                <div 
                    class="subject-banner p-4 p-md-5 d-flex flex-column justify-content-end"
                    :style="{ backgroundColor: subject.color || '#1a73e8' }"
                >
                    <div class="d-flex justify-content-between align-items-end flex-wrap gap-3">
                        <div class="text-white">
                            <h1 class="display-6 fw-bold mb-1">{{ subject.name }}</h1>
                            <p class="text-white-50 mb-0 fs-6">
                                {{ subject.description || 'Sem descrição.' }}
                            </p>
                        </div>

                        <!-- Ações rápidas no banner: Mudar Cor + Badge de Situação -->
                        <div class="d-flex align-items-center gap-2">
                            <button 
                                type="button" 
                                class="btn btn-sm btn-light text-dark rounded-circle p-2 shadow-sm"
                                title="Alterar cor do tema"
                                @click="openColorPicker"
                            >
                                <i class="bi bi-palette fs-5"></i>
                            </button>

                            <span
                                class="badge rounded-pill px-3 py-2 fw-normal fs-6 bg-white text-dark shadow-sm"
                            >
                                {{ getSituationLabel(subject.situation) }}
                            </span>
                        </div>
                    </div>
                </div>

                <!-- Detalhes no corpo do Card -->
                <div class="card-body p-3 p-md-4 bg-white">
                    <div class="d-flex align-items-center text-muted small">
                        <i class="bi bi-clock me-2"></i>
                        <span>Criada em: <strong class="text-dark fw-medium">{{ formatDateTime(subject.creationDate) }}</strong></span>
                    </div>
                </div>
            </div>

            <!-- Seção de Tarefas -->
            <div>
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <h3 class="mb-1 fw-normal">Tarefas</h3>
                        <p class="text-muted mb-0 small">Tarefas vinculadas a esta disciplina.</p>
                    </div>

                    <button type="button" class="btn btn-primary rounded-pill px-3">
                        <i class="bi bi-plus-lg me-2"></i>
                        Nova tarefa
                    </button>
                </div>

                <!-- Lista vazia -->
                <div v-if="!subject.task || subject.task.length === 0" class="card border-0 bg-light rounded-3">
                    <div class="card-body text-center py-5">
                        <i class="bi bi-check2-square display-5 text-muted"></i>
                        <h5 class="mt-3 fw-normal">Nenhuma tarefa</h5>
                        <p class="text-muted mb-0 small">Ainda não existem tarefas nesta disciplina.</p>
                    </div>
                </div>

                <!-- Lista de tarefas -->
                <div v-else class="row g-3">
                    <div v-for="task in subject.task" :key="task.id" class="col-12">
                        <div class="card shadow-sm border-0">
                            <div class="card-body">
                                <h5 class="mb-1 fs-6">{{ task.title }}</h5>
                                <p v-if="task.description" class="text-muted mb-0 small">
                                    {{ task.description }}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </div>
</template>

<style scoped>
.subject-hero-card {
    border-radius: 12px;
    overflow: hidden;
}

.subject-banner {
    min-height: 160px;
    transition: background-color 0.3s ease;
}

.btn-light:hover {
    background-color: #f8f9fa;
    transform: scale(1.05);
}
</style>