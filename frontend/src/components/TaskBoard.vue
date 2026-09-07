<script setup>
    import { computed } from 'vue'

    const props = defineProps({
        tasks: {
            type: Array,
            default: () => []
        }
    })

    const emit = defineEmits(['create-task', 'update-status'])

    // Mapeamento das colunas do Kanban
    const columns = [
        { key: 'PENDING', label: 'A Fazer', badgeClass: 'bg-secondary-subtle text-secondary' },
        { key: 'IN_PROGRESS', label: 'Em Andamento', badgeClass: 'bg-primary-subtle text-primary' },
        { key: 'COMPLETED', label: 'Concluído', badgeClass: 'bg-success-subtle text-success' }
    ]

    // Agrupa as tarefas recebidas por situação/status
    const tasksByStatus = computed(() => {
        const grouped = { PENDING: [], IN_PROGRESS: [], COMPLETED: [] }
        
        props.tasks.forEach(task => {
            const status = task.status || 'PENDING'
            if (grouped[status]) {
            grouped[status].push(task)
            } else {
            grouped.PENDING.push(task)
            }
        })
        
        return grouped
    })

    function handleStatusChange(task, newStatus) {
        if (task.status === newStatus) return
        emit('update-status', { taskId: task.id, status: newStatus })
    }

    function getPriorityBadge(priority) {
        const map = {
            HIGH: { label: 'Alta', class: 'bg-danger-subtle text-danger' },
            MEDIUM: { label: 'Média', class: 'bg-warning-subtle text-warning-emphasis' },
            LOW: { label: 'Baixa', class: 'bg-info-subtle text-info-emphasis' }
        }
        return map[priority] || { label: priority, class: 'bg-light text-dark' }
    }

    function formatDate(dateString) {
        if (!dateString) return null
        const date = new Date(dateString)
        if (isNaN(date.getTime())) return null
        return date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' })
    }
</script>

<template>
    <div class="task-board">
        <!-- Cabeçalho da Seção -->
        <div class="mb-4">
        <h3 class="mb-1 fw-normal">Quadro de Tarefas</h3>
        <p class="text-muted mb-0 small">Acompanhe o progresso do seu plano de estudo</p>
        </div>

        <!-- Colunas Kanban (3 colunas estilo Trello) -->
        <div class="row g-3">
        <div 
            v-for="col in columns" 
            :key="col.key" 
            class="col-12 col-md-4"
        >
            <div class="kanban-column bg-light rounded-4 p-3 h-100 d-flex flex-column border">
            
            <!-- Cabeçalho da Coluna -->
            <div class="d-flex align-items-center justify-content-between mb-3 px-1">
                <span class="fw-medium text-dark small">{{ col.label }}</span>
                <span class="badge rounded-pill" :class="col.badgeClass">
                {{ tasksByStatus[col.key].length }}
                </span>
            </div>

            <!-- Lista de Cards da Coluna -->
            <div class="kanban-cards d-flex flex-column gap-2 flex-grow-1">
                <div 
                v-for="task in tasksByStatus[col.key]" 
                :key="task.id" 
                class="card task-card border-0 shadow-sm rounded-3 bg-white p-3"
                >
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <h6 class="mb-0 fw-medium text-dark fs-6 text-break">{{ task.title }}</h6>
                        
                        <!-- Menu de Opções/Mudar Status -->
                        <div class="dropdown ms-2">
                        <button 
                            class="btn btn-dots p-0 opacity-75" 
                            type="button" 
                            data-bs-toggle="dropdown" 
                            aria-expanded="false"
                        >
                            <i class="bi bi-three-dots-vertical"></i>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 rounded-3 small">
                            <li v-if="col.key !== 'PENDING'">
                            <button class="dropdown-item" @click="handleStatusChange(task, 'TODO')">
                                Mover para A Fazer
                            </button>
                            </li>
                            <li v-if="col.key !== 'IN_PROGRESS'">
                            <button class="dropdown-item" @click="handleStatusChange(task, 'IN_PROGRESS')">
                                Mover para Em Andamento
                            </button>
                            </li>
                            <li v-if="col.key !== 'COMPLETED'">
                            <button class="dropdown-item text-success" @click="handleStatusChange(task, 'DONE')">
                                Marcar como Concluído
                            </button>
                            </li>
                        </ul>
                        </div>
                    </div>

                    <p v-if="task.description" class="text-muted small text-break mb-0">
                        {{ task.description }}
                    </p>
                </div>

                <!-- Estado Vazio da Coluna -->
                <div 
                v-if="tasksByStatus[col.key].length === 0" 
                class="empty-column-state text-center text-muted py-4 small"
                >
                Nenhuma tarefa
                </div>
            </div>
            <button 
                type="button" 
                class="btn btn-add-card w-100 mt-2 py-2 px-3 text-start text-secondary rounded-3 d-flex align-items-center gap-2 small"
                data-bs-toggle="modal"
                data-bs-target="#createTaskModal"
                @click="$emit('create-task', col.key)"
                >
                <i class="bi bi-plus-lg"></i>
                <span>Adicionar um cartão</span>
            </button>

        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
    .kanban-column {
    min-height: 380px;
    }

    .task-card {
    transition: transform 0.15s ease, box-shadow 0.15s ease;
    }

    .task-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 0.25rem 0.75rem rgba(0, 0, 0, 0.08) !important;
    }

    .btn-dots {
    background: transparent !important;
    border: none !important;
    box-shadow: none !important;
    }

    .empty-column-state {
    border: 2px dashed #dee2e6;
    border-radius: 0.5rem;
    }

    /* Estilo do botão do rodapé da coluna */
    .btn-add-card {
    background-color: transparent;
    border: none;
    transition: background-color 0.15s ease, color 0.15s ease;
    }

    .btn-add-card:hover {
    background-color: var(--color-background-hover, #e9ecef);
    color: var(--color-text-light, #212529) !important;
    }
</style>