<script setup>
    import { ref, watch } from 'vue'

    const props = defineProps({
        editingSubject: {
            type: Object,
            default: null
        },
        subjectColors: {
            type: Array,
            default: () => [
                '#DDEBFF', '#E4D9FF', '#F8D7DA', '#FFE5B4',
                '#FFF3BF', '#DFF5E1', '#D5F5F6', '#F3D9E8'
            ]
        },
        loading: {
            type: Boolean,
            default: false
        }
    })

    const emit = defineEmits(['submit'])

    const formData = ref({
        name: '',
        description: '',
        color: '#DDEBFF',
        situation: 'ACTIVE'
    })

    const errors = ref({
        name: '',
        description: '',
        color: '',
        situation: ''
    })

    function resetForm() {
        if (props.editingSubject) {
            formData.value = {
                name: props.editingSubject.name,
                description: props.editingSubject.description || '',
                color: props.editingSubject.color || props.subjectColors[0],
                situation: props.editingSubject.situation || 'ACTIVE'
            }
        } else {
            formData.value = {
                name: '',
                description: '',
                color: props.subjectColors[0],
                situation: 'ACTIVE'
            }
        }
        
        errors.value = { name: '', description: '', color: '', situation: '' }
    }

    watch(() => props.editingSubject, resetForm, { immediate: true })

    function validate() {
        errors.value = { name: '', description: '', color: '', situation: '' }
        let isValid = true

        const nameTrimmed = formData.value.name ? formData.value.name.trim() : ''

        if (!nameTrimmed) {
            errors.value.name = 'O nome da disciplina é obrigatório.'
            isValid = false
        } else if (nameTrimmed.length > 100) {
            errors.value.name = 'O nome deve possuir no máximo 100 caracteres.'
            isValid = false
        }

        if (formData.value.description && formData.value.description.length > 250) {
            errors.value.description = 'A descrição deve possuir no máximo 250 caracteres.'
            isValid = false
        }

        if (!formData.value.color) {
            errors.value.color = 'Selecione uma cor para a disciplina.'
            isValid = false
        }

        return isValid
    }

    function handleSubmit() {
        if (props.loading) return
        if (!validate()) return

        emit('submit', { ...formData.value })
    }

    function selectColor(color) {
        if (props.loading) return
        formData.value.color = color
        errors.value.color = ''
    }

    defineExpose({ resetForm })
</script>

<template>
    <div
        class="modal fade"
        id="createSubjectModal"
        tabindex="-1"
        aria-labelledby="createSubjectModalLabel"
        aria-hidden="true"
        :data-bs-backdrop="loading ? 'static' : true"
        :data-bs-keyboard="!loading"
    >
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header border-0 pb-0">
                    <h5 id="createSubjectModalLabel" class="modal-title fw-normal fs-4">
                        {{ editingSubject ? 'Editar disciplina' : 'Criar disciplina' }}
                    </h5>
                    <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Fechar"
                        :disabled="loading"
                    ></button>
                </div>

                <form @submit.prevent="handleSubmit" novalidate autocomplete="off">
                    <div class="modal-body py-4">
                        <div class="form-floating mb-3">
                            <input
                                type="text"
                                class="form-control rounded-3"
                                :class="{ 'is-invalid': errors.name }"
                                id="subjectName"
                                v-model="formData.name"
                                placeholder="Nome da disciplina"
                                :disabled="loading"
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
                                v-model="formData.description"
                                placeholder="Descrição"
                                style="height: 100px"
                                :disabled="loading"
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
                                    :class="{ selected: formData.color === color }"
                                    :style="{ backgroundColor: color }"
                                    :title="`Selecionar cor ${color}`"
                                    :disabled="loading"
                                    @click="selectColor(color)"
                                >
                                    <i
                                        v-if="formData.color === color"
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
                            <select 
                                class="form-select rounded-3" 
                                v-model="formData.situation"
                                :disabled="loading"
                            >
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
                            :disabled="loading"
                        >
                            Cancelar
                        </button>
                        <button type="submit" class="btn btn-primary px-4 rounded-3" :disabled="loading">
                            <span v-if="loading" class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                            {{ loading ? (editingSubject ? 'Salvando...' : 'Criando...') : (editingSubject ? 'Salvar' : 'Criar') }}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .btn-primary {
        background-color: var(--color-body) !important;
    }

    .btn-cancel:hover {
        color: var(--color-text-light);
        background-color: var(--color-background-hover);
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

    .color-option:disabled {
        cursor: not-allowed;
        opacity: 0.6;
    }

    .color-option:not(:disabled):hover {
        transform: scale(1.1);
    }

    .color-option.selected {
        box-shadow: 0 0 0 3px white, 0 0 0 5px var(--color-body-light);
    }
</style>