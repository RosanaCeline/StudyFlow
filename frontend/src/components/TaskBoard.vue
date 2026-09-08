<script setup>
  import { computed } from 'vue'
  import draggable from 'vuedraggable'

  const props = defineProps({
    tasks: {
      type: Array,
      default: () => []
    }
  })

  const emit = defineEmits(['create-task', 'edit-task', 'update-status'])

  const columns = [
    { key: 'PENDING', label: 'A Fazer', badgeClass: 'bg-secondary-subtle text-secondary' },
    { key: 'IN_PROGRESS', label: 'Em Andamento', badgeClass: 'bg-primary-subtle text-primary' },
    { key: 'COMPLETED', label: 'Concluído', badgeClass: 'bg-success-subtle text-success' }
  ]

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

  function getColumnTasks(columnKey) {
    return computed({
      get() {
        return props.tasks.filter(task => (task.status || 'PENDING') === columnKey)
      },
      set(newTasks) {
        newTasks.forEach(task => {
          if (task.status !== columnKey) {
            emit('update-status', { taskId: task.id, status: columnKey })
          }
        })
      }
    })
  }
</script>

<template>
  <div class="task-board">
    <div class="mb-4">
      <h3 class="mb-1 fw-normal">Quadro de Tarefas</h3>
      <p class="text-muted mb-0 small">Arraste os cartões para mudar o status</p>
    </div>

    <div class="row g-3">
      <div 
        v-for="col in columns" 
        :key="col.key" 
        class="col-12 col-md-4"
      >
        <div class="kanban-column bg-light rounded-4 p-3 h-100 d-flex flex-column border">
          <div class="d-flex align-items-center justify-content-between mb-3 px-1">
            <span class="fw-medium text-dark small">{{ col.label }}</span>
            <span class="badge rounded-pill" :class="col.badgeClass">
              {{ getColumnTasks(col.key).value.length }}
            </span>
          </div>

          <draggable
            v-model="getColumnTasks(col.key).value"
            group="kanban-tasks"
            item-key="id"
            class="kanban-cards d-flex flex-column gap-2 flex-grow-1"
            ghost-class="ghost-card"
          >
            <template #item="{ element: task }">
              <div 
                class="card task-card border-0 shadow-sm rounded-3 bg-white p-3 cursor-grab"
                data-bs-toggle="modal"
                data-bs-target="#createTaskModal"
                @click="emit('edit-task', task)"
              >
                <h6 
                  class="mb-2 fw-medium fs-6 text-break"
                  :class="{ 'text-decoration-line-through text-muted': task.status === 'COMPLETED', 'text-dark': task.status !== 'COMPLETED' }"
                >
                  {{ task.title }}
                </h6>

                <p v-if="task.description" class="text-muted small text-break mb-3">
                  {{ task.description }}
                </p>

                <div class="d-flex align-items-center justify-content-between pt-2 border-top mt-auto">
                  <span class="badge rounded-pill fw-normal" :class="getPriorityBadge(task.priority).class">
                    {{ getPriorityBadge(task.priority).label }}
                  </span>

                  <small v-if="task.deadline" class="text-muted d-flex align-items-center gap-1" style="font-size: 0.75rem;">
                    <i class="bi bi-calendar3"></i>
                    <span>{{ formatDate(task.deadline) }}</span>
                  </small>
                </div>
              </div>
            </template>

            <template #footer>
              <div 
                v-if="getColumnTasks(col.key).value.length === 0" 
                class="empty-column-state text-center text-muted py-4 small"
              >
                Arraste uma tarefa aqui
              </div>
            </template>
          </draggable>

          <button 
            type="button" 
            class="btn btn-add-card w-100 mt-2 py-2 px-3 text-start text-secondary rounded-3 d-flex align-items-center gap-2 small"
            data-bs-toggle="modal"
            data-bs-target="#createTaskModal"
            @click="emit('create-task', col.key)"
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
    min-height: 400px;
  }

  .task-card {
    transition: transform 0.15s ease, box-shadow 0.15s ease;
    cursor: grab;
  }

  .task-card:active {
    cursor: grabbing;
  }

  .ghost-card {
    opacity: 0.4;
    border: 2px dashed #0d6efd !important;
  }

  .empty-column-state {
    border: 2px dashed #dee2e6;
    border-radius: 0.5rem;
  }

  .btn-add-card {
    background-color: transparent;
    border: none;
    transition: background-color 0.15s ease;
  }

  .btn-add-card:hover {
    background-color: var(--color-background-hover, #e9ecef);
  }
</style>