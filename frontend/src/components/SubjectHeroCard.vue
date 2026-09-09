<script setup>
    import { ref, onMounted, onUnmounted } from 'vue'

    const props = defineProps({
        subject: {
            type: Object,
            required: true
        }
    })

    const emit = defineEmits(['back', 'open-color-picker', 'edit', 'delete'])

    const isMenuOpen = ref(false)

    function toggleDropdown(event) {
        event.stopPropagation()
        isMenuOpen.value = !isMenuOpen.value
    }

    function handleAction(action, event) {
        event.stopPropagation()
        isMenuOpen.value = false
        emit(action, props.subject)
    }

    function handleDocumentClick(event) {
        if (!event.target.closest('.dropdown')) {
            isMenuOpen.value = false
        }
    }

    onMounted(() => {
        document.addEventListener('click', handleDocumentClick)
    })

    onUnmounted(() => {
        document.removeEventListener('click', handleDocumentClick)
    })

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
</script>

<template>
    <div>
        <button
            type="button"
            class="btn btn-link text-decoration-none p-0 mb-3 text-secondary"
            @click="emit('back')"
        >
            <i class="bi bi-arrow-left me-2"></i>
            Voltar para disciplinas
        </button>

        <div class="card subject-hero-card shadow-sm border-0 mb-4">
            <div 
                class="subject-banner p-4 p-md-5 d-flex flex-column justify-content-between position-relative"
                :style="{ backgroundColor: subject.color || '#DDEBFF' }"
            >
                <div class="d-flex justify-content-end align-items-center gap-2">
                    <button 
                        type="button" 
                        class="btn-palette btn btn-sm btn-light text-dark rounded-circle d-flex align-items-center justify-content-center p-0 shadow-sm"
                        title="Alterar cor do tema"
                        @click="emit('open-color-picker')"
                    >
                        <i class="bi bi-palette fs-5"></i>
                    </button>

                    <span
                        class="badge rounded-pill px-3 py-2 fw-normal fs-6 shadow-sm"
                        :class="getSituationBadgeClass(subject.situation)"
                    >
                        {{ getSituationLabel(subject.situation) }}
                    </span>

                    <div class="dropdown">
                        <button 
                            class="btn btn-dots p-0 opacity-75 hover-opacity-100"
                            type="button"
                            @click="toggleDropdown"
                        >
                            <i class="btn-ic bi bi-three-dots-vertical fs-5"></i>
                        </button>

                        <ul 
                            class="dropdown-menu dropdown-menu-end shadow-sm border-0 rounded-3"
                            :class="{ show: isMenuOpen }"
                        >
                            <li>
                                <button 
                                    type="button"
                                    class="dropdown-item d-flex align-items-center gap-2 py-2"
                                    @click="handleAction('edit', $event)"
                                >
                                    <i class="bi bi-pencil text-secondary"></i>
                                    <span>Editar disciplina</span>
                                </button>
                            </li>
                            <li>
                                <button 
                                    type="button"
                                    class="dropdown-item text-danger d-flex align-items-center gap-2 py-2"
                                    @click="handleAction('delete', $event)"
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
    </div>
</template>

<style scoped>
    .subject-hero-card {
        border-radius: 12px;
        overflow: visible !important;
    }

    .subject-banner {
        min-height: 180px;
        transition: background-color 0.3s ease;
        border-top-left-radius: 12px;
        border-top-right-radius: 12px;
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

    .dropdown-menu.show {
        display: block;
        position: absolute;
        right: 0;
        top: 100%;
        z-index: 1000;
    }
</style>