<script setup>
    import { onMounted, ref } from 'vue'
    import { useRoute, useRouter } from 'vue-router'
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

    const editingTask = ref(null)
    const initialTaskStatus = ref('PENDING')

    const taskFormModalRef = ref(null)
    const modalFormRef = ref(null)

    async function loadData() {
        loading.value = true
        error.value = ''

        try {
            const subjectId = route.params.id
            subject.value = await getSubjectById(subjectId)
            tasks.value = await listTasks({ subjectId })
        } catch (err) {
            error.value = 'Não foi possível carregar os dados da disciplina.'
        } finally {
            loading.value = false
        }
    }

    function closeModal(modalId) {
        const modalEl = document.getElementById(modalId)
        const closeBtn = modalEl?.querySelector('.btn-close')
        if (closeBtn) closeBtn.click()
    }

    function goBack() {
        router.push('/app/subjects')
    }

    function openCreateTaskModal(status = 'PENDING') {
        editingTask.value = null
        initialTaskStatus.value = status
        if (taskFormModalRef.value) {
            taskFormModalRef.value.resetForm()
        }
    }

    function openEditTaskModal(task) {
    editingTask.value = { ...task }
    }

    async function handleTaskFormSubmit(payload) {
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
        }
    }

    async function handleDeleteTask(taskId) {
        try {
            await deleteTask(taskId)
            tasks.value = tasks.value.filter(t => t.id !== taskId)
            closeModal('createTaskModal')
            editingTask.value = null
        } catch (err) {
            alert('Não foi possível excluir a tarefa.')
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

    function handleOpenEditModal() {
        if (modalFormRef.value) {
            modalFormRef.value.resetForm()
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
        if (!subject.value) return

        try {
            await deleteSubject(subject.value.id)
            closeModal('deleteSubjectModal')
            router.push('/app/subjects')
        } catch (err) {
            alert('Não foi possível excluir a disciplina.')
        }
    }

    onMounted(() => {
    loadData()
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
        <SubjectHeroCard 
            :subject="subject"
            @back="goBack"
            @edit="handleOpenEditModal"
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
            @submit="handleEditSubjectSubmit"
        />

        <ConfirmDeleteModal
            :subject-name="subject?.name"
            @confirm="handleDeleteSubject"
        />
        </div>
    </div>
</template>