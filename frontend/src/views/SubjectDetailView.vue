<script setup>
    import { onMounted, ref, onActivated, watch } from 'vue'
    import { useRoute, useRouter } from 'vue-router'
    import { Modal } from 'bootstrap'
    import { getSubjectById, updateSubject, deleteSubject } from '../services/subjectService'
    import { listTasks, createTask, updateTask, updateTaskStatus, deleteTask } from '../services/taskService'

    import SubjectHeroCard from '../components/SubjectHeroCard.vue'
    import TaskBoard from '../components/TaskBoard.vue'
    import TaskFormModal from '../components/TaskFormModal.vue'
    import ChangeColorModal from '../components/ChangeColorModal.vue'
    import SubjectFormModal from '../components/SubjectFormModal.vue'
    import ConfirmDeleteModal from '../components/ConfirmDeleteModal.vue'

    const route = useRoute()
    const router = useRouter()

    const subject = ref(null)
    const tasks = ref([])
    const loading = ref(false)
    const error = ref('')

    const updatingColor = ref(false)
    const updatingSubject = ref(false)
    const deletingSubject = ref(false)
    const savingTask = ref(false)
    const deletingTask = ref(false)

    const editingTask = ref(null)
    const initialTaskStatus = ref('PENDING')

    const taskFormModalRef = ref(null)
    const modalFormRef = ref(null)

    async function loadData(isSilent = false) {
        const subjectId = route.params.id
        
        if (!isSilent) {
            loading.value = true
        }

        error.value = ''

        try {
            const [fetchedSubject, fetchedTasks] = await Promise.all([
                getSubjectById(subjectId),
                listTasks({ subjectId })
            ])

            subject.value = fetchedSubject
            tasks.value = fetchedTasks
        } catch (err) {
            if (!isSilent) {
                error.value = 'Não foi possível carregar os dados da disciplina.'
            }
        } finally {
            if (!isSilent) {
                loading.value = false
            }
        }
    }

    function closeModal(modalId) {
        const modalEl = document.getElementById(modalId)
        if (!modalEl) return
        
        if (document.activeElement instanceof HTMLElement) {
            document.activeElement.blur()
        }

        const modalInstance = Modal.getInstance(modalEl)
        if (modalInstance) {
            modalInstance.hide()
        }
    }

    function goToSubjectList() {
        router.push('/app/subjects')
    }

    function openColorModal() {
        const modalEl = document.getElementById('changeColorModal')
        if (modalEl) {
            const modalInstance = Modal.getInstance(modalEl) || new Modal(modalEl)
            modalInstance.show()
        }
    }

    function openEditModal() {
        if (modalFormRef.value) {
            modalFormRef.value.resetForm()
        }
        
        const modalEl = document.getElementById('createSubjectModal')
        if (modalEl) {
            const modalInstance = Modal.getInstance(modalEl) || new Modal(modalEl)
            modalInstance.show()
        }
    }

    function openDeleteModal() {
        const modalEl = document.getElementById('deleteSubjectModal')
        if (modalEl) {
            const modalInstance = Modal.getInstance(modalEl) || new Modal(modalEl)
            modalInstance.show()
        }
    }

    function openCreateTaskModal(status = 'PENDING') {
        editingTask.value = null
        initialTaskStatus.value = status
        if (taskFormModalRef.value) {
            taskFormModalRef.value.resetForm()
        }

        const modalEl = document.getElementById('createTaskModal')
        if (modalEl) {
            const modalInstance = Modal.getInstance(modalEl) || new Modal(modalEl)
            modalInstance.show()
        }
    }

    function openEditTaskModal(task) {
        editingTask.value = { ...task }

        const modalEl = document.getElementById('createTaskModal')
        if (modalEl) {
            const modalInstance = Modal.getInstance(modalEl) || new Modal(modalEl)
            modalInstance.show()
        }
    }

    async function handleTaskFormSubmit(payload) {
        if (savingTask.value) return
        savingTask.value = true

        try {
            if (editingTask.value?.id) {
                const updated = await updateTask(editingTask.value.id, payload)
                const index = tasks.value.findIndex(t => t.id === editingTask.value.id)
                if (index !== -1) tasks.value[index] = updated
            } else {
                const created = await createTask(payload)
                tasks.value.unshift(created)
            }

            closeModal('createTaskModal')
            editingTask.value = null
        } catch (err) {
            alert('Erro ao salvar a tarefa.')
        } finally {
            savingTask.value = false
        }
    }

    async function handleDeleteTask(taskId) {
        if (deletingTask.value) return
        deletingTask.value = true

        try {
            await deleteTask(taskId)
            tasks.value = tasks.value.filter(t => t.id !== taskId)
            closeModal('createTaskModal')
            editingTask.value = null
        } catch (err) {
            alert('Não foi possível excluir a tarefa.')
        } finally {
            deletingTask.value = false
        }
    }

    async function handleUpdateTaskStatus({ taskId, status }) {
        const targetTask = tasks.value.find(t => t.id === taskId)
        if (!targetTask) return

        const previousStatus = targetTask.status
        targetTask.status = status

        try {
            const updatedTask = await updateTaskStatus(taskId, status)
            const index = tasks.value.findIndex(t => t.id === taskId)
            if (index !== -1) {
                tasks.value[index] = updatedTask
            }
        } catch (err) {
            targetTask.status = previousStatus
            alert('Não foi possível alterar a situação da tarefa.')
        }
    }

    async function handleUpdateColor(newColor) {
        if (!subject.value || updatingColor.value) return

        updatingColor.value = true

        try {
            const updatedData = {
                name: subject.value.name,
                description: subject.value.description,
                situation: subject.value.situation,
                color: newColor
            }

            const updatedSubject = await updateSubject(subject.value.id, updatedData)
            subject.value.color = updatedSubject.color || newColor
            closeModal('changeColorModal')
        } catch (err) {
            alert('Não foi possível alterar a cor da disciplina.')
        } finally {
            updatingColor.value = false
        }
    }

    async function handleEditSubjectSubmit(formData) {
        if (!subject.value || updatingSubject.value) return

        updatingSubject.value = true

        try {
            const updated = await updateSubject(subject.value.id, formData)
            subject.value = updated
            closeModal('createSubjectModal')
        } catch (err) {
            alert('Não foi possível atualizar a disciplina.')
        } finally {
            updatingSubject.value = false
        }
    }

    async function handleDeleteSubject() {
        if (!subject.value || deletingSubject.value) return

        deletingSubject.value = true

        try {
            await deleteSubject(subject.value.id)
            closeModal('deleteSubjectModal')
            router.push('/app/subjects')
        } catch (err) {
            alert('Não foi possível excluir a disciplina.')
        } finally {
            deletingSubject.value = false
        }
    }

    onMounted(() => {
        loadData(false)
    })

    onActivated(() => {
        loadData(true)
    })

    watch(
        () => route.params.id,
        (newId, oldId) => {
            if (newId && newId !== oldId) {
                loadData(false)
            }
        }
    )
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
            <SubjectHeroCard
                :subject="subject"
                @back="goToSubjectList"
                @open-color-picker="openColorModal"
                @edit="openEditModal"
                @delete="openDeleteModal"
            />

            <TaskBoard 
                :tasks="tasks"
                @create-task="openCreateTaskModal"
                @edit-task="openEditTaskModal"
                @update-status="handleUpdateTaskStatus"
            />

            <TaskFormModal
                ref="taskFormModalRef"
                :subject-id="subject.id"
                :editing-task="editingTask"
                :initial-status="initialTaskStatus"
                :loading="savingTask"
                :deleting="deletingTask"
                @submit="handleTaskFormSubmit"
                @delete="handleDeleteTask"
            />

            <ChangeColorModal
                :current-color="subject.color"
                :loading="updatingColor"
                @save="handleUpdateColor"
            />

            <SubjectFormModal
                ref="modalFormRef"
                :editing-subject="subject"
                :loading="updatingSubject"
                @submit="handleEditSubjectSubmit"
            />

            <ConfirmDeleteModal
                :subject-name="subject?.name"
                :loading="deletingSubject"
                @confirm="handleDeleteSubject"
            />
        </div>
    </div>
</template>