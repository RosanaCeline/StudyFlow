<script setup>
import { computed } from 'vue'

const props = defineProps({
    subject: {
        type: Object,
        required: true
    }
})

const emit = defineEmits(['click', 'edit', 'delete'])

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
        ACTIVE: 'bg-primary-subtle text-primary',
        COMPLETED: 'bg-success-subtle text-success',
        ARCHIVED: 'bg-secondary-subtle text-secondary'
    }
    return classes[situation] || 'bg-light text-dark'
}
</script>

<template>
    <div 
        class="card classroom-card rounded-3 h-100 shadow-sm border-0 cursor-pointer"
        @click="emit('click', subject.id)"
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
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                    @click.stop
                >
                    <i class="btn-ic bi bi-three-dots-vertical fs-5"></i>
                </button>

                <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 rounded-3">
                    <li>
                        <button 
                            type="button"
                            class="dropdown-item d-flex align-items-center gap-2 py-2"
                            data-bs-toggle="modal"
                            data-bs-target="#createSubjectModal"
                            @click.stop="emit('edit', subject)"
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
                            @click.stop="emit('delete', subject)"
                        >
                            <i class="bi bi-trash"></i>
                            <span>Excluir disciplina</span>
                        </button>
                    </li>
                </ul>
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
</template>

<style scoped>
.classroom-card {
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

.btn-dots {
    background: transparent !important;
    border: none !important;
    box-shadow: none !important;
}

.cursor-pointer {
    cursor: pointer;
}
</style>