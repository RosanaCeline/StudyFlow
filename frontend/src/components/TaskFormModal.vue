<script setup>
    import { ref, watch } from 'vue'

    const props = defineProps({
        subjectId: {
            type: [Number, String],
            required: true
        },
        editingTask: {
            type: Object,
            default: null
        },
        initialStatus: {
            type: String,
            default: 'PENDING'
        }
    })

    const emit = defineEmits(['submit', 'delete'])

    const taskForm = ref({
        title: '',
        description: '',
        deadline: '',
        priority: 'MEDIUM',
        status: 'PENDING'
    })

    const taskErrors = ref({
        title: '',
        description: '',
        priority: ''
    })

    function resetForm() {
        taskErrors.value = { title: '', description: '', priority: '' }

        if (props.editingTask) {
            taskForm.value = {
                title: props.editingTask.title || '',
                description: props.editingTask.description || '',
                deadline: props.editingTask.deadline ? props.editingTask.deadline.substring(0, 16) : '',
                priority: props.editingTask.priority || 'MEDIUM',
                status: props.editingTask.status || 'PENDING'
            }
        } else {
            taskForm.value = {
                title: '',
                description: '',
                deadline: '',
                priority: 'MEDIUM',
                status: props.initialStatus || 'PENDING'
            }
        }
    }

    watch(() => [props.editingTask, props.initialStatus], resetForm, { immediate: true })

    function validate() {
        taskErrors.value = { title: '', description: '', priority: '' }
        let isValid = true

        const titleTrimmed = taskForm.value.title ? taskForm.value.title.trim() : ''

        if (!titleTrimmed) {
            taskErrors.value.title = 'O título da tarefa é obrigatório.'
            isValid = false
        } else if (titleTrimmed.length > 60) {
            taskErrors.value.title = 'O título deve possuir no máximo 60 caracteres.'
            isValid = false
        }

        if (taskForm.value.description && taskForm.value.description.length > 100) {
            taskErrors.value.description = 'A descrição deve possuir no máximo 100 caracteres.'
            isValid = false
        }

        if (!taskForm.value.priority) {
            taskErrors.value.priority = 'A prioridade é obrigatória.'
            isValid = false
        }

        return isValid
    }

    function handleSubmit() {
        if (!validate()) return

        const payload = {
            ...taskForm.value,
            subjectId: props.subjectId,
            title: taskForm.value.title.trim(),
            deadline: taskForm.value.deadline || null
        }

        emit('submit', payload)
    }

    defineExpose({ resetForm })
</script>

<template>
  <div
    class="modal fade"
    id="createTaskModal"
    tabindex="-1"
    aria-labelledby="createTaskModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content border-0 shadow-lg rounded-4">
        <div class="modal-header border-0 pb-0">
          <h5 id="createTaskModalLabel" class="modal-title fw-normal fs-4">
            {{ editingTask ? 'Editar Tarefa' : 'Nova Tarefa' }}
          </h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
        </div>

        <form @submit.prevent="handleSubmit" novalidate>
          <div class="modal-body py-4">
            <div class="form-floating mb-3">
              <input
                type="text"
                class="form-control rounded-3"
                :class="{ 'is-invalid': taskErrors.title }"
                id="taskTitle"
                v-model="taskForm.title"
                placeholder="Título da tarefa"
                maxlength="60"
              >
              <label for="taskTitle">Título da tarefa (obrigatório)</label>
              <div class="invalid-feedback" v-if="taskErrors.title">
                {{ taskErrors.title }}
              </div>
            </div>

            <div class="form-floating mb-3">
              <textarea
                class="form-control rounded-3"
                :class="{ 'is-invalid': taskErrors.description }"
                id="taskDesc"
                v-model="taskForm.description"
                placeholder="Descrição"
                maxlength="100"
                style="height: 100px; resize: none;"
              ></textarea>
              <label for="taskDesc">Descrição</label>
              <div class="invalid-feedback" v-if="taskErrors.description">
                {{ taskErrors.description }}
              </div>
            </div>

            <div class="mb-3">
              <label for="taskDeadline" class="form-label small text-muted mb-1">Prazo de entrega</label>
              <input
                type="datetime-local"
                class="form-control rounded-3"
                id="taskDeadline"
                v-model="taskForm.deadline"
              >
            </div>

            <div>
              <label class="form-label small text-muted mb-1">Prioridade</label>
              <select class="form-select rounded-3" v-model="taskForm.priority">
                <option value="LOW">Baixa</option>
                <option value="MEDIUM">Média</option>
                <option value="HIGH">Alta</option>
              </select>
            </div>
          </div>

          <div class="modal-footer border-0 pt-0 d-flex justify-content-between align-items-center">
            <button
                v-if="editingTask"
                type="button"
                class="btn btn-delete-icon rounded-circle d-flex align-items-center justify-content-center"
                @click="emit('delete', editingTask.id)"
                title="Excluir tarefa"
                aria-label="Excluir tarefa"
            >
                <i class="bi bi-trash fs-5"></i>
            </button>

            <div class="d-flex gap-2 ms-auto">
                <button
                    type="button"
                    class="btn btn-link text-decoration-none text-secondary"
                    data-bs-dismiss="modal"
                >
                    Cancelar
                </button>

                <button type="submit" class="btn btn-primary px-4">
                    {{ editingTask ? 'Salvar' : 'Criar' }}
                </button>
            </div>
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

    .btn-delete-icon {
        width: 38px;
        height: 38px;
        color: #dc3545;
        background-color: transparent;
        border: none;
        transition: background-color 0.2s ease, color 0.2s ease, transform 0.1s ease;
    }

    .btn-delete-icon:hover {
        background-color: #f8d7da;
        color: #b02a37;
    }

    .btn-delete-icon:active {
        transform: scale(0.95);
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