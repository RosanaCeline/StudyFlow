<script setup>
    import { computed } from 'vue'
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

    const props = defineProps({
    tasks: {
        type: Array,
        required: true
    },
    subjects: {
        type: Array,
        required: true
    }
    })

    const completedTasksCount = computed(() => {
    return props.tasks.filter(t => t.status === 'COMPLETED').length
    })

    const statusChartData = computed(() => {
    const pending = props.tasks.filter(t => t.status === 'PENDING').length
    const inProgress = props.tasks.filter(t => t.status === 'IN_PROGRESS').length
    const completed = completedTasksCount.value

    return {
        labels: ['A Fazer', 'Em Andamento', 'Concluído'],
        datasets: [{
        label: 'Quantidade de Tarefas',
        backgroundColor: ['#A9ECEF', '#FFE380', '#B6EBB9'],
        data: [pending, inProgress, completed]
        }]
    }
    })

    const priorityChartData = computed(() => {
    const high = props.tasks.filter(t => t.priority === 'HIGH').length
    const medium = props.tasks.filter(t => t.priority === 'MEDIUM').length
    const low = props.tasks.filter(t => t.priority === 'LOW').length

    return {
        labels: ['Alta', 'Média', 'Baixa'],
        datasets: [{
        backgroundColor: ['#dc3545', '#ffc107', '#0dcaf0'],
        data: [high, medium, low]
        }]
    }
    })

    const subjectChartData = computed(() => {
    const labels = props.subjects.map(s => {
        const name = s.name || 'Sem nome'
        return name.length > 12 ? name.substring(0, 10) + '...' : name
    })

    const fullNames = props.subjects.map(s => s.name || 'Sem nome')

    const data = props.subjects.map(s => {
        return props.tasks.filter(t => String(t.subjectId) === String(s.id)).length
    })

    const backgroundColors = props.subjects.map(s => s.color || '#0d6efd')

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
        legend: { position: 'bottom' }
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
</script>

<template>
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
</template>

<style scoped>
    .chart-wrapper {
    position: relative;
    height: 240px;
    width: 100%;
    }
</style>