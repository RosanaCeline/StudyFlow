<script setup>
import { onMounted, onUnmounted, ref, onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { Modal } from 'bootstrap'
import { listSubjects, createSubject, updateSubject, deleteSubject } from '../services/subjectService'
import SubjectCard from '../components/SubjectCard.vue'
import SubjectFormModal from '../components/SubjectFormModal.vue'
import ConfirmDeleteModal from '../components/ConfirmDeleteModal.vue'

const router = useRouter()

const subjects = ref([])
const loading = ref(false)
const error = ref('')

const editingSubject = ref(null)
const subjectToDelete = ref(null)
const modalFormRef = ref(null)

const activeMenuSubjectId = ref(null)

function navigateToSubject(id) {
    closeAllMenus()
    router.push(`/app/subjects/${id}`)
}

function toggleSubjectMenu(subjectId) {
    if (activeMenuSubjectId.value === subjectId) {
        activeMenuSubjectId.value = null
    } else {
        activeMenuSubjectId.value = subjectId
    }
}

function closeAllMenus() {
    activeMenuSubjectId.value = null
}

async function loadSubjects(isSilent = false) {
    if (!isSilent) {
        loading.value = true
    }
    error.value = ''

    try {
        subjects.value = await listSubjects()
    } catch (err) {
        if (!isSilent) {
            error.value = 'Não foi possível carregar as disciplinas.'
        }
    } finally {
        if (!isSilent) {
            loading.value = false
        }
    }
}

function openCreateModal() {
    closeAllMenus()
    editingSubject.value = null
    if (modalFormRef.value) {
        modalFormRef.value.resetForm()
    }

    const modalEl = document.getElementById('createSubjectModal')
    if (modalEl) {
        const modalInstance = Modal.getInstance(modalEl) || new Modal(modalEl)
        modalInstance.show()
    }
}

function openEditModal(subject) {
    closeAllMenus()
    editingSubject.value = { ...subject }

    const modalEl = document.getElementById('createSubjectModal')
    if (modalEl) {
        const modalInstance = Modal.getInstance(modalEl) || new Modal(modalEl)
        modalInstance.show()
    }
}

function openDeleteModal(subject) {
    closeAllMenus()
    subjectToDelete.value = subject

    const modalEl = document.getElementById('deleteSubjectModal')
    if (modalEl) {
        const modalInstance = Modal.getInstance(modalEl) || new Modal(modalEl)
        modalInstance.show()
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
    } else {
        const newModal = new Modal(modalEl)
        newModal.hide()
    }
}

async function handleFormSubmit(payload) {
    try {
        if (editingSubject.value?.id) {
            const updated = await updateSubject(editingSubject.value.id, payload)
            const index = subjects.value.findIndex(s => s.id === editingSubject.value.id)
            if (index !== -1) subjects.value[index] = updated
        } else {
            const created = await createSubject(payload)
            subjects.value.unshift(created)
        }

        closeModal('createSubjectModal')
        editingSubject.value = null
    } catch (err) {
        error.value = editingSubject.value?.id
            ? 'Não foi possível atualizar a disciplina.' 
            : 'Não foi possível cadastrar a disciplina.'
    }
}

async function handleDelete() {
    if (!subjectToDelete.value) return

    try {
        await deleteSubject(subjectToDelete.value.id)
        subjects.value = subjects.value.filter(s => s.id !== subjectToDelete.value.id)
        subjectToDelete.value = null
        closeModal('deleteSubjectModal')
    } catch (err) {
        error.value = 'Não foi possível excluir a disciplina.'
    }
}

function handleDocumentClick(event) {
    if (!event.target.closest('.dropdown')) {
        closeAllMenus()
    }
}

onMounted(() => {
    loadSubjects(false)
    document.addEventListener('click', handleDocumentClick)
})

onActivated(() => {
    loadSubjects(true)
})

onUnmounted(() => {
    document.removeEventListener('click', handleDocumentClick)
})
</script>

<template>
    <div class="classroom-container">
        <div class="mb-4 pb-2 border-bottom d-flex flex-column flex-sm-row justify-content-between align-items-start align-items-sm-center gap-2">
            <div>
                <h2 class="fw-normal mb-1">Minhas Disciplinas</h2>
                <p class="text-muted mb-0 small">Gerencie e acesse seus materiais de estudo</p>
            </div>
            <button
                type="button"
                class="btn btn-primary rounded-3 d-flex align-items-center gap-1 mt-1 mt-sm-0"
                @click="openCreateModal"
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
                @click="openCreateModal"
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
                <SubjectCard
                    :subject="subject"
                    :is-menu-open="activeMenuSubjectId === subject.id"
                    @click="navigateToSubject"
                    @toggle-menu="toggleSubjectMenu"
                    @edit="openEditModal"
                    @delete="openDeleteModal"
                />
            </div>
        </div>

        <SubjectFormModal
            ref="modalFormRef"
            :editing-subject="editingSubject"
            @submit="handleFormSubmit"
        />

        <ConfirmDeleteModal
            :subject-name="subjectToDelete?.name"
            @confirm="handleDelete"
        />
    </div>
</template>

<style scoped>
    .btn-primary {
        background-color: var(--color-body) !important;
    }
</style>