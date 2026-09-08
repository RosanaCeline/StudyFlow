<script setup>
    import { ref, computed, onMounted } from 'vue'
    import { listSubjects } from '../services/subjectService'
    import { listTasks } from '../services/taskService'

    import {
    Chart as ChartJS,
    ArcElement,
    Tooltip,
    Legend,
    CategoryScale,
    LinearScale,
    BarElement
    } from 'chart.js'
    import { Doughnut, Bar } from 'vue-chartjs'

    ChartJS.register(
    ArcElement,
    Tooltip,
    Legend,
    CategoryScale,
    LinearScale,
    BarElement
    )

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

    const statusChartData = computed(() => {
    const pending = tasks.value.filter(t => t.status === 'PENDING').length
    const inProgress = tasks.value.filter(t => t.status === 'IN_PROGRESS').length
    const completed = completedTasksCount.value

    return {
        labels: ['A Fazer', 'Em Andamento', 'Concluído'],
        datasets: [{
        label: 'Quantidade de Tarefas',
        backgroundColor: ['#6c757d', '#0d6efd', '#198754'],
        data: [pending, inProgress, completed]
        }]
    }
    })

    const priorityChartData = computed(() => {
    const high = tasks.value.filter(t => t.priority === 'HIGH').length
    const medium = tasks.value.filter(t => t.priority === 'MEDIUM').length
    const low = tasks.value.filter(t => t.priority === 'LOW').length

    return {
        labels: ['Alta', 'Média', 'Baixa'],
        datasets: [{
        backgroundColor: ['#dc3545', '#ffc107', '#0dcaf0'],
        data: [high, medium, low]
        }]
    }
    })

    const subjectChartData = computed(() => {
    const labels = subjects.value.map(s => {
        const name = s.name || 'Sem nome'
        return name.length > 12 ? name.substring(0, 10) + '...' : name
    })

    const fullNames = subjects.value.map(s => s.name || 'Sem nome')

    const data = subjects.value.map(s => {
        return tasks.value.filter(t => String(t.subjectId) === String(s.id)).length
    })

    const backgroundColors = subjects.value.map(s => s.color || '#0d6efd')

    return {
        labels,
        fullNames,
        datasets: [{
        label: 'Tarefas por Disciplina',
        backgroundColor: backgroundColors,
        data
        }]
    }
    })

    const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: {
        position: 'bottom'
        }
    }
    }

    const barOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: { display: false }
    },
    scales: {
        y: {
        beginAtZero: true,
        ticks: { precision: 0 }
        }
    }
    }

    const subjectBarOptions = computed(() => ({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: { display: false },
        tooltip: {
        callbacks: {
            title: (tooltipItems) => {
            const index = tooltipItems[0].dataIndex
            return subjectChartData.value.fullNames[index] || ''
            }
        }
        }
    },
    scales: {
        x: {
        ticks: {
            maxRotation: 45,
            minRotation: 0,
            font: { size: 11 }
        }
        },
        y: {
        beginAtZero: true,
        ticks: { precision: 0 }
        }
    }
    }))

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
          <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
            <div class="d-flex align-items-center justify-content-between">
              <div>
                <span class="text-muted small fw-medium d-block mb-1">Total de Tarefas</span>
                <h3 class="fw-bold mb-0 text-dark">{{ totalTasks }}</h3>
              </div>
              <div class="metric-icon rounded-circle bg-light text-primary d-flex align-items-center justify-content-center">
                <i class="bi bi-list-task fs-4"></i>
              </div>
            </div>
          </div>
        </div>

        <div class="col-12 col-sm-6 col-xl-4">
          <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
            <div class="d-flex align-items-center justify-content-between">
              <div>
                <span class="text-muted small fw-medium d-block mb-1">Tarefas em Atraso</span>
                <h3 class="fw-bold mb-0 text-danger">{{ overdueTasksCount }}</h3>
              </div>
              <div class="metric-icon rounded-circle bg-danger-subtle text-danger d-flex align-items-center justify-content-center">
                <i class="bi bi-exclamation-triangle fs-4"></i>
              </div>
            </div>
          </div>
        </div>

        <div class="col-12 col-sm-6 col-xl-4">
          <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
            <div class="d-flex align-items-center justify-content-between">
              <div>
                <span class="text-muted small fw-medium d-block mb-1">Taxa de Conclusão</span>
                <h3 class="fw-bold mb-0 text-info-emphasis">{{ completionPercentage }}%</h3>
              </div>
              <div class="metric-icon rounded-circle bg-info-subtle text-info d-flex align-items-center justify-content-center">
                <i class="bi bi-pie-chart fs-4"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row g-4 mb-4">
        <div class="col-12">
          <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
            <h5 class="fw-medium text-dark mb-3 fs-6">Progresso por Disciplina</h5>
            
            <div v-if="subjectProgressList.length === 0" class="text-center text-muted py-4 small">
              Nenhuma disciplina cadastrada.
            </div>

            <div v-else class="progress-list d-flex flex-column gap-3 overflow-auto pe-1" style="max-height: 280px;">
              <div v-for="item in subjectProgressList" :key="item.id">
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
        </div>
      </div>

      <div class="row g-4">
        <div class="col-12 col-md-6 col-xl-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 bg-white h-100">
            <h5 class="fw-medium text-dark mb-3 fs-6">Por Status</h5>
            <div class="chart-wrapper">
              <Bar :data="statusChartData" :options="barOptions" />
            </div>
          </div>
        </div>

        <div class="col-12 col-md-6 col-xl-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 bg-white h-100">
            <h5 class="fw-medium text-dark mb-3 fs-6">Por Prioridade</h5>
            <div class="chart-wrapper">
              <Doughnut :data="priorityChartData" :options="chartOptions" />
            </div>
          </div>
        </div>

        <div class="col-12 col-xl-4">
          <div class="card border-0 shadow-sm rounded-4 p-4 bg-white h-100">
            <h5 class="fw-medium text-dark mb-3 fs-6">Total por Disciplina</h5>
            <div class="chart-wrapper">
              <Bar v-if="subjects.length > 0" :data="subjectChartData" :options="subjectBarOptions" />
              <div v-else class="text-center text-muted py-5 small">
                Nenhuma disciplina cadastrada.
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
    .metric-icon {
    width: 48px;
    height: 48px;
    flex-shrink: 0;
    }

    .chart-wrapper {
    position: relative;
    height: 240px;
    width: 100%;
    }

    .progress-list::-webkit-scrollbar {
    width: 4px;
    }

    .progress-list::-webkit-scrollbar-thumb {
    background-color: #dee2e6;
    border-radius: 4px;
    }
</style>