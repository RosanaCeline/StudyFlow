<script setup>
    import { ref, computed, onMounted } from 'vue'
    import { listSubjects } from '../services/subjectService'
    import { listTasks } from '../services/taskService'

    import DashboardMetricCard from '../components/DashboardMetricCard.vue'
    import SubjectProgressList from '../components/SubjectProgressList.vue'
    import DashboardCharts from '../components/DashboardCharts.vue'

    const subjects = ref([])
    const tasks = ref([])
    const loading = ref(true)
    const error = ref('')

    async function loadDashboardData() {
      loading.value = true
      error.value = ''

      try {
        const [fetchedSubjects, fetchedTasks] = await Promise.all([
          listSubjects(),
          listTasks()
        ])

        subjects.value = fetchedSubjects
        tasks.value = fetchedTasks
      } catch (err) {
        error.value = 'Não foi possível carregar as estatísticas do painel.'
      } finally {
        loading.value = false
      }
    }

    const totalTasks = computed(() => tasks.value.length)

    const completedTasksCount = computed(() => {
      return tasks.value.filter(t => t.status === 'COMPLETED').length
    })

    const overdueTasksCount = computed(() => {
      const now = new Date()
      return tasks.value.filter(t => {
        if (t.status === 'COMPLETED' || !t.deadline) return false
        const deadlineDate = new Date(t.deadline)
        return deadlineDate < now
      }).length
    })

    const completionPercentage = computed(() => {
      if (totalTasks.value === 0) return 0
      return Math.round((completedTasksCount.value / totalTasks.value) * 100)
    })

    const subjectProgressList = computed(() => {
      return subjects.value.map(s => {
        const subjectTasks = tasks.value.filter(t => String(t.subjectId) === String(s.id))
        const total = subjectTasks.length
        const completed = subjectTasks.filter(t => t.status === 'COMPLETED').length
        const percentage = total > 0 ? Math.round((completed / total) * 100) : 0

        return {
          id: s.id,
          name: s.name,
          color: s.color || '#0d6efd',
          total,
          completed,
          percentage
        }
      })
    })

    onMounted(() => {
      loadDashboardData()
    })
</script>

<template>
  <div class="dashboard-container w-100">
    <div class="mb-4 pb-2 border-bottom d-flex justify-content-between align-items-center">
      <div>
        <h2 class="fw-normal mb-1">Painel Dashboard</h2>
        <p class="text-muted mb-0 small">Visão geral do progresso dos seus estudos e pendências</p>
      </div>
      <button 
        type="button" 
        class="btn btn-outline-secondary btn-sm rounded-3 d-flex align-items-center gap-1"
        @click="loadDashboardData"
        :disabled="loading"
      >
        <i class="bi bi-arrow-clockwise"></i>
        <span>Atualizar</span>
      </button>
    </div>

    <div v-if="error" class="alert alert-danger rounded-3 shadow-sm mb-4">
      {{ error }}
    </div>

    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Carregando painel...</span>
      </div>
    </div>

    <div v-else>
      <div class="row g-3 mb-4">
        <div class="col-12 col-sm-6 col-xl-4">
          <DashboardMetricCard 
            title="Total de Tarefas"
            :value="totalTasks"
            icon="bi bi-list-task"
            value-class="text-dark"
            bg-icon-class="bg-light text-primary"
          />
        </div>

        <div class="col-12 col-sm-6 col-xl-4">
          <DashboardMetricCard 
            title="Tarefas em Atraso"
            :value="overdueTasksCount"
            icon="bi bi-exclamation-triangle"
            value-class="text-danger"
            bg-icon-class="bg-danger-subtle text-danger"
          />
        </div>

        <div class="col-12 col-sm-6 col-xl-4">
          <DashboardMetricCard 
            title="Taxa de Conclusão"
            :value="`${completionPercentage}%`"
            icon="bi bi-pie-chart"
            value-class="text-info-emphasis"
            bg-icon-class="bg-info-subtle text-info"
          />
        </div>
      </div>

      <div class="row g-4 mb-4">
        <div class="col-12">
          <SubjectProgressList :items="subjectProgressList" />
        </div>
      </div>

      <DashboardCharts :tasks="tasks" :subjects="subjects" />
    </div>
  </div>
</template>