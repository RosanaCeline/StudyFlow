<script setup>
    defineProps({
    title: {
        type: String,
        required: true
    },
    value: {
        type: [Number, String],
        required: true
    },
    icon: {
        type: String,
        required: true
    },
    valueClass: {
        type: String,
        default: 'text-dark'
    },
    bgIconClass: {
        type: String,
        default: 'bg-light text-primary'
    }
    })
</script>

<template>
    <div class="card border-0 shadow-sm rounded-4 p-3 bg-white h-100">
      <div class="d-flex align-items-center justify-content-between">
        <div>
          <span class="text-muted small fw-medium d-block mb-1">{{ title }}</span>
          <h3 class="fw-bold mb-0" :class="valueClass">{{ value }}</h3>
        </div>
        <div class="metric-icon rounded-circle d-flex align-items-center justify-content-center" :class="bgIconClass">
          <i :class="[icon, 'fs-4']"></i>
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
</style>