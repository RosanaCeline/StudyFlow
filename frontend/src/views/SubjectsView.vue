<script setup>
    import { onMounted, ref } from 'vue'
    import { useRouter } from 'vue-router'
    import { listSubjects, createSubject } from '../services/subjectService'

    const subjects = ref([])
    const loading = ref(false)
    const error = ref('')

    const errors = ref({
        name: '',
        description: '',
        color: '',
        situation: ''
    })

    const router = useRouter()

    const newSubject = ref({
        name: '',
        description: '',
        color: '#DDEBFF',
        situation: 'ACTIVE'
    })

    const subjectColors = [
        '##DDEBFF',
        '#E4D9FF',
        '#F8D7DA',
        '#FFE5B4',
        '#FFF3BF',
        '#DFF5E1',
        '#D5F5F6',
        '#F3D9E8'
    ]

    function selectSubjectColor(color) {
        newSubject.value.color = color
        errors.value.color = ''
    }

    function getSituationLabel(situation) {
        const labels = {
            ACTIVE: 'Ativa',
            COMPLETED: 'Finalizada',
            ARCHIVED: 'Arquivada'
        }
        return labels[situation] || 'Desconhecida'
    }

    function getSituationBadgeClass(situation) {
        const classes = {
            ACTIVE: 'bg-success-subtle text-success',
            COMPLETED: 'bg-primary-subtle text-primary',
            ARCHIVED: 'bg-secondary-subtle text-secondary'
        }
        return classes[situation] || 'bg-light text-dark'
    }

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
        errors.value = {
            name: '',
            description: '',
            color: '',
            situation: ''
        }

        let isValid = true

        const nameTrimmed = newSubject.value.name ? newSubject.value.name.trim() : ''

        if (!nameTrimmed) {
            errors.value.name = 'O nome da disciplina é obrigatório.'
            isValid = false
        } else if (nameTrimmed.length > 100) {
            errors.value.name = 'O nome deve possuir no máximo 100 caracteres.'
            isValid = false
        }

        if (newSubject.value.description && newSubject.value.description.length > 250) {
            errors.value.description = 'A descrição deve possuir no máximo 250 caracteres.'
            isValid = false
        }

        if (!newSubject.value.color) {
            errors.value.color = 'Selecione uma cor para a disciplina.'
            isValid = false
        }

        if (!isValid) return

        try {
            const createdSubject = await createSubject(newSubject.value)

            subjects.value.push(createdSubject)

            newSubject.value = {
                name: '',
                description: '',
                color: '#DDEBFF',
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
                class="btn btn-primary px-3 shadow-sm d-flex align-items-center gap-2"
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
                class="btn btn-outline-primary px-4 mt-2"
                data-bs-toggle="modal"
                data-bs-target="#createSubjectModal"
            >
                Criar disciplina
            </button>
        </div>

        <div v-else class="row g-4">
            <div
                v-for="subject in subjects"
                :key="subject.id"
                class="col-12 col-md-6 col-xl-4"
            >
                <div 
                    class="card classroom-card h-100 shadow-sm border-0 cursor-pointer"
                    @click="$router.push(`/app/subjects/${subject.id}`)"
                >
                    <div 
                        class="card-header-banner p-3 d-flex flex-column justify-content-between"
                        :style="{ backgroundColor: subject.color || '#DDEBFF' }"
                    >
                        <div class="d-flex justify-content-between align-items-start">
                            <h4 class="card-title text-truncate mb-0 fw-normal" :title="subject.name">
                                {{ subject.name }}
                            </h4>
                            <button 
                                class="btn btn-dots p-0 opacity-75 hover-opacity-100"
                                @click.stop
                            >
                                <i class="btn-ic bi bi-three-dots-vertical fs-5"></i>
                            </button>
                        </div>
                        <p class="small text-truncate mb-0">
                            {{ subject.description || 'Sem descrição' }}
                        </p>
                    </div>

                    <div class="card-body bg-white d-flex flex-column justify-content-between p-3">
                        <div class="d-flex align-items-center justify-content-between text-muted small">
                            <span>Status:</span>
                            <span 
                                class="badge rounded-pill fw-normal"
                                :class="getSituationBadgeClass(subject.situation)"
                            >
                                {{ getSituationLabel(subject.situation) }}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

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

                    <form @submit.prevent="handleCreate" novalidate>
                        <div class="modal-body py-4">
                            <div class="form-floating mb-3">
                                <input
                                    type="text"
                                    class="form-control rounded-3"
                                    :class="{ 'is-invalid': errors.name }"
                                    id="subjectName"
                                    v-model="newSubject.name"
                                    placeholder="Nome da disciplina"
                                >
                                <label for="subjectName">Nome da disciplina (obrigatório)</label>
                                <div class="invalid-feedback" v-if="errors.name">
                                    {{ errors.name }}
                                </div>
                            </div>

                            <div class="form-floating mb-3">
                                <textarea
                                    class="form-control rounded-3"
                                    :class="{ 'is-invalid': errors.description }"
                                    id="subjectDesc"
                                    v-model="newSubject.description"
                                    placeholder="Descrição"
                                    style="height: 100px"
                                ></textarea>
                                <label for="subjectDesc">Descrição ou seção</label>
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
                                        :class="{ selected: newSubject.color === color }"
                                        :style="{ backgroundColor: color }"
                                        :title="`Selecionar cor ${color}`"
                                        @click="selectSubjectColor(color)"
                                    >
                                        <i
                                            v-if="newSubject.color === color"
                                            class="bi bi-check2 text-dark fs-5"
                                        ></i>
                                    </button>
                                    <div class="invalid-feedback d-block" v-if="errors.color">
                                        {{ errors.color }}
                                    </div>
                                </div>
                            </div>

                            <div>
                                <label class="form-label small text-muted mb-1">Situação</label>
                                <select class="form-select rounded-3" v-model="newSubject.situation">
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
                                class="btn btn-link text-decoration-none text-secondary btn-cancel"
                                data-bs-dismiss="modal"
                            >
                                Cancelar
                            </button>
                            <button type="submit" class="btn btn-primary px-4">
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

    .card-header-banner {
        height: 100px;
        background-size: cover;
        background-position: center;
        position: relative;
    }

    .hover-opacity-100:hover {
        opacity: 1 !important;
    }

    .btn-primary {
        background-color: var(--color-body) !important;
    }

    .btn-dots {
        background: transparent !important;
        border: none !important;
        box-shadow: none !important;
    }

    .btn-cancel:hover {
        color: var(--color-text-light);
        background-color: var(--color-background-hover);
    }

    .cursor-pointer {
        cursor: pointer;
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
</style>