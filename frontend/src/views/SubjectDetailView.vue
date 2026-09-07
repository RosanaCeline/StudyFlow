<script setup>
    import { onMounted, ref } from 'vue'
    import { useRoute, useRouter } from 'vue-router'
    import { getSubjectById, updateSubject, deleteSubject } from '../services/subjectService'

    const route = useRoute()
    const router = useRouter()

    const subject = ref(null)
    const loading = ref(false)
    const error = ref('')

    const selectedColor = ref('')
    const updatingColor = ref(false)

    const editForm = ref({
        name: '',
        description: '',
        color: '',
        situation: 'ACTIVE'
    })
    const errors = ref({
        name: '',
        description: '',
        color: '',
        situation: ''
    })
    const updatingSubject = ref(false)

    const subjectColors = [
        '#DDEBFF',
        '#E4D9FF',
        '#F8D7DA',
        '#FFE5B4',
        '#FFF3BF',
        '#DFF5E1',
        '#D5F5F6',
        '#F3D9E8'
    ]

    async function loadSubject() {
        loading.value = true
        error.value = ''

        try {
            subject.value = await getSubjectById(route.params.id)
            selectedColor.value = subject.value.color || subjectColors[0]
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
        const classes = {
            ACTIVE: 'bg-success-subtle text-success',
            COMPLETED: 'bg-primary-subtle text-primary',
            ARCHIVED: 'bg-secondary-subtle text-secondary'
        }
        return classes[situation] || 'bg-light text-dark'
    }

    function goBack() {
        router.push('/app/subjects')
    }

    function openColorPicker() {
        if (subject.value) {
            selectedColor.value = subject.value.color || subjectColors[0]
        }
    }

    async function handleUpdateColor() {
        if (!subject.value || updatingColor.value) return

        updatingColor.value = true

        try {
            const updatedData = {
                name: subject.value.name,
                description: subject.value.description,
                situation: subject.value.situation,
                color: selectedColor.value
            }

            const updatedSubject = await updateSubject(subject.value.id, updatedData)
            subject.value.color = updatedSubject.color || selectedColor.value

            document.getElementById('changeColorModal').querySelector('.btn-close').click()
        } catch (err) {
            alert('Não foi possível alterar a cor da disciplina.')
        } finally {
            updatingColor.value = false
        }
    }

    function openEditModal() {
        if (!subject.value) return

        editForm.value = {
            name: subject.value.name,
            description: subject.value.description || '',
            color: subject.value.color || subjectColors[0],
            situation: subject.value.situation
        }

        errors.value = {
            name: '',
            description: '',
            color: '',
            situation: ''
        }
    }

    async function handleEditSubject() {
        errors.value = {
            name: '',
            description: '',
            color: '',
            situation: ''
        }

        let isValid = true
        const nameTrimmed = editForm.value.name ? editForm.value.name.trim() : ''

        if (!nameTrimmed) {
            errors.value.name = 'O nome da disciplina é obrigatório.'
            isValid = false
        } else if (nameTrimmed.length > 100) {
            errors.value.name = 'O nome deve possuir no máximo 100 caracteres.'
            isValid = false
        }

        if (editForm.value.description && editForm.value.description.length > 250) {
            errors.value.description = 'A descrição deve possuir no máximo 250 caracteres.'
            isValid = false
        }

        if (!editForm.value.color) {
            errors.value.color = 'Selecione uma cor para a disciplina.'
            isValid = false
        }

        if (!isValid) return

        updatingSubject.value = true

        try {
            const updated = await updateSubject(subject.value.id, {
                ...editForm.value,
                name: nameTrimmed
            })

            subject.value = updated

            document.getElementById('editSubjectModal').querySelector('.btn-close').click()
        } catch (err) {
            alert('Não foi possível atualizar a disciplina.')
        } finally {
            updatingSubject.value = false
        }
    }

    async function handleDeleteSubject() {
        if (!subject.value) return

        try {
            await deleteSubject(subject.value.id)
            
            document.getElementById('deleteSubjectModal').querySelector('.btn-close').click()
            router.push('/app/subjects')
        } catch (err) {
            alert('Não foi possível excluir a disciplina.')
        }
    }

    onMounted(() => {
        loadSubject()
    })
</script>

<template>
    <div class="w-100">
        <div v-if="loading" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Carregando...</span>
            </div>
        </div>

        <div v-else-if="error" class="alert alert-danger shadow-sm">
            {{ error }}
        </div>

        <div v-else-if="subject">
            <button
                type="button"
                class="btn btn-link text-decoration-none p-0 mb-3 text-secondary"
                @click="goBack"
            >
                <i class="bi bi-arrow-left me-2"></i>
                Voltar para disciplinas
            </button>

            <div class="card subject-hero-card shadow-sm border-0 mb-4">
                <div 
                    class="subject-banner p-4 p-md-5 d-flex flex-column justify-content-between"
                    :style="{ backgroundColor: subject.color || '#DDEBFF' }"
                >
                    <div class="d-flex justify-content-end align-items-center">
                        <div class="d-flex align-items-center gap-2">
                            <button 
                                type="button" 
                                class="btn-palette btn btn-sm btn-light text-dark rounded-circle d-flex align-items-center justify-content-center p-0 shadow-sm"
                                title="Alterar cor do tema"
                                data-bs-toggle="modal"
                                data-bs-target="#changeColorModal"
                                @click="openColorPicker"
                            >
                                <i class="bi bi-palette fs-5"></i>
                            </button>

                            <span
                                class="badge rounded-pill px-3 py-2 fw-normal fs-6 shadow-sm"
                                :class="getSituationBadgeClass(subject.situation)"
                            >
                                {{ getSituationLabel(subject.situation) }}
                            </span>
                        </div>

                        <div class="dropdown">
                            <button 
                                class="btn btn-dots p-0 opacity-75 hover-opacity-100"
                                data-bs-toggle="dropdown"
                                aria-expanded="false"
                            >
                                <i class="btn-ic bi bi-three-dots-vertical fs-5"></i>
                            </button>

                            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 rounded-3">
                                <li>
                                    <button 
                                        type="button"
                                        class="dropdown-item d-flex align-items-center gap-2 py-2"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editSubjectModal"
                                        @click="openEditModal"
                                    >
                                        <i class="bi bi-pencil text-secondary"></i>
                                        <span>Editar disciplina</span>
                                    </button>
                                </li>
                                <li>
                                    <button 
                                        type="button"
                                        class="dropdown-item text-danger d-flex align-items-center gap-2 py-2"
                                        data-bs-toggle="modal"
                                        data-bs-target="#deleteSubjectModal"
                                    >
                                        <i class="bi bi-trash"></i>
                                        <span>Excluir disciplina</span>
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </div> 
                    
                    <div class="d-flex justify-content-between align-items-end flex-wrap gap-3 mt-3">
                        <div>
                            <h1 class="display-6 fw-bold mb-1 text-break">{{ subject.name }}</h1>
                            <p class="mb-0 fs-6 text-break">
                                {{ subject.description || 'Sem descrição.' }}
                            </p>
                        </div>
                    </div>
                </div>

                <div class="card-body p-3 p-md-4 bg-white">
                    <div class="d-flex align-items-center text-muted small">
                        <i class="bi bi-clock me-2"></i>
                        <span>Criada em: <strong class="text-dark fw-medium">{{ formatDateTime(subject.creationDate) }}</strong></span>
                    </div>
                </div>
            </div>

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

                <div v-if="!subject.task || subject.task.length === 0" class="card border-0 bg-light rounded-3">
                    <div class="card-body text-center py-5">
                        <i class="bi bi-check2-square display-5 text-muted"></i>
                        <h5 class="mt-3 fw-normal">Nenhuma tarefa</h5>
                        <p class="text-muted mb-0 small">Ainda não existem tarefas nesta disciplina.</p>
                    </div>
                </div>

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

            <div
                class="modal fade"
                id="changeColorModal"
                tabindex="-1"
                aria-labelledby="changeColorModalLabel"
                aria-hidden="true"
            >
                <div class="modal-dialog modal-dialog-centered modal-sm">
                    <div class="modal-content border-0 shadow-lg rounded-4">
                        <div class="modal-header border-0 pb-0">
                            <h5 id="changeColorModalLabel" class="modal-title fw-normal fs-5">
                                Cor do tema
                            </h5>
                            <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Fechar"
                            ></button>
                        </div>

                        <div class="modal-body py-3">
                            <div class="d-flex flex-wrap gap-2 justify-content-center">
                                <button
                                    v-for="color in subjectColors"
                                    :key="color"
                                    type="button"
                                    class="color-option rounded-circle border-0 p-0 d-flex align-items-center justify-content-center"
                                    :class="{ selected: selectedColor === color }"
                                    :style="{ backgroundColor: color }"
                                    :title="`Selecionar cor ${color}`"
                                    @click="selectedColor = color"
                                >
                                    <i
                                        v-if="selectedColor === color"
                                        class="bi bi-check2 text-dark fs-5"
                                    ></i>
                                </button>
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
                            <button
                                type="button"
                                class="btn btn-primary px-3 rounded-3"
                                :disabled="updatingColor"
                                @click="handleUpdateColor"
                            >
                                <span v-if="updatingColor" class="spinner-border spinner-border-sm me-1"></span>
                                Salvar
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <div
                class="modal fade"
                id="editSubjectModal"
                tabindex="-1"
                aria-labelledby="editSubjectModalLabel"
                aria-hidden="true"
            >
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content border-0 shadow-lg rounded-4">
                        <div class="modal-header border-0 pb-0">
                            <h5 id="editSubjectModalLabel" class="modal-title fw-normal fs-4">
                                Editar disciplina
                            </h5>
                            <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Fechar"
                            ></button>
                        </div>

                        <form @submit.prevent="handleEditSubject" novalidate>
                            <div class="modal-body py-4">
                                <div class="form-floating mb-3">
                                    <input
                                        type="text"
                                        class="form-control rounded-3"
                                        :class="{ 'is-invalid': errors.name }"
                                        id="editSubjectName"
                                        v-model="editForm.name"
                                        placeholder="Nome da disciplina"
                                    >
                                    <label for="editSubjectName">Nome da disciplina (obrigatório)</label>
                                    <div class="invalid-feedback" v-if="errors.name">
                                        {{ errors.name }}
                                    </div>
                                </div>

                                <div class="form-floating mb-3">
                                    <textarea
                                        class="form-control rounded-3"
                                        :class="{ 'is-invalid': errors.description }"
                                        id="editSubjectDesc"
                                        v-model="editForm.description"
                                        placeholder="Descrição"
                                        style="height: 100px; resize: none;"
                                    ></textarea>
                                    <label for="editSubjectDesc">Descrição ou seção</label>
                                    <div class="invalid-feedback" v-if="errors.description">
                                        {{ errors.description }}
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label small text-muted mb-2">Cor do tema</label>
                                    <div class="d-flex flex-wrap gap-2">
                                        <button
                                            v-for="color in subjectColors"
                                            :key="color"
                                            type="button"
                                            class="color-option rounded-circle border-0 p-0 d-flex align-items-center justify-content-center"
                                            :class="{ selected: editForm.color === color }"
                                            :style="{ backgroundColor: color }"
                                            :title="`Selecionar cor ${color}`"
                                            @click="editForm.color = color"
                                        >
                                            <i
                                                v-if="editForm.color === color"
                                                class="bi bi-check2 text-dark fs-5"
                                            ></i>
                                        </button>
                                    </div>
                                    <div class="invalid-feedback d-block" v-if="errors.color">
                                        {{ errors.color }}
                                    </div>
                                </div>

                                <div>
                                    <label class="form-label small text-muted mb-1">Situação</label>
                                    <select class="form-select rounded-3" v-model="editForm.situation">
                                        <option value="ACTIVE">Ativa</option>
                                        <option value="COMPLETED">Finalizada</option>
                                        <option value="ARCHIVED">Arquivada</option>
                                    </select>
                                    <div class="invalid-feedback" v-if="errors.situation">
                                        {{ errors.situation }}
                                    </div>
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
                                <button type="submit" class="btn btn-primary px-4" :disabled="updatingSubject">
                                    <span v-if="updatingSubject" class="spinner-border spinner-border-sm me-1"></span>
                                    Salvar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div
                class="modal fade"
                id="deleteSubjectModal"
                tabindex="-1"
                aria-labelledby="deleteSubjectModalLabel"
                aria-hidden="true"
            >
                <div class="modal-dialog modal-dialog-centered modal-md">
                    <div class="modal-content border-0 shadow-lg rounded-4">
                        <div class="modal-header border-0 pb-0">
                            <h5 id="deleteSubjectModalLabel" class="modal-title fw-normal fs-5">
                                Excluir disciplina
                            </h5>
                            <button
                                type="button"
                                class="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Fechar"
                            ></button>
                        </div>

                        <div class="modal-body py-3">
                            <p class="mb-2">
                                Tem certeza que deseja excluir <strong class="text-break text-wrap" style="word-break: break-all;">"{{ subject?.name }}"</strong>?
                            </p>
                            <small class="text-muted d-block">
                                Essa ação não poderá ser desfeita.
                            </small>
                        </div>

                        <div class="modal-footer border-0 pt-0">
                            <button
                                type="button"
                                class="btn btn-link text-decoration-none text-secondary"
                                data-bs-dismiss="modal"
                            >
                                Cancelar
                            </button>
                            <button
                                type="button"
                                class="btn btn-danger px-3 rounded-3"
                                @click="handleDeleteSubject"
                            >
                                <i class="bi bi-trash me-1"></i>
                                Excluir
                            </button>
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
        min-height: 180px;
        transition: background-color 0.3s ease;
    }

    .btn-palette {
        width: 38px;
        height: 38px;
        border: transparent;
    }

    .btn-palette:hover {
        background-color: var(--color-background-hover);
    }

    .btn-dots {
        background: transparent !important;
        border: none !important;
        box-shadow: none !important;
    }

    .hover-opacity-100:hover {
        opacity: 1 !important;
    }

    .color-option {
        width: 36px;
        height: 36px;
        cursor: pointer;
        transition: transform 0.15s ease, box-shadow 0.15s ease;
    }

    .color-option:hover {
        transform: scale(1.1);
    }

    .color-option.selected {
        box-shadow: 0 0 0 3px white, 0 0 0 5px var(--color-body-light);
    }

    .btn-primary {
        background-color: var(--color-body) !important;
    }

    textarea.form-control {
        resize: none;
    }

    .form-floating > .form-control:focus ~ label,
    .form-floating > .form-control:not(:placeholder-shown) ~ label {
        color: var(--color-text-light);
    }

    .form-control:focus,
    .form-select:focus {
        border-color: var(--color-body-light);
        box-shadow: 0 0 0 1px var(--color-body-light);
    }
</style>