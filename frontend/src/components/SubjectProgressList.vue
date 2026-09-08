<script setup>
    defineProps({
    items: {
        type: Array,
        required: true
    }
    })
</script>

<template>
    <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
      <h5 class="fw-medium text-dark mb-3 fs-6">Tarefas concluídas por Disciplina</h5>
      
      <div v-if="items.length === 0" class="text-center text-muted py-4 small">
        Nenhuma disciplina cadastrada.
      </div>

      <div v-else class="progress-list d-flex flex-column gap-3 overflow-auto pe-1" style="max-height: 280px;">
        <div v-for="item in items" :key="item.id">
          <div class="d-flex justify-content-between align-items-center mb-1">
            <span class="small fw-medium text-dark text-truncate me-2">{{ item.name }}</span>
            <span class="small text-muted">{{ item.percentage }}%</span>
          </div>
          <div class="progress rounded-pill" style="height: 8px;">
            <div
              class="progress-bar rounded-pill"
              role="progressbar"
              :style="{ width: item.percentage + '%', backgroundColor: item.color }"
              :aria-valuenow="item.percentage"
              aria-valuemin="0"
              aria-valuemax="100"
            ></div>
          </div>
        </div>
      </div>
    </div>
</template>

<style scoped>
    .progress-list::-webkit-scrollbar {
    width: 4px;
    }

    .progress-list::-webkit-scrollbar-thumb {
    background-color: #dee2e6;
    border-radius: 4px;
    }
</style>