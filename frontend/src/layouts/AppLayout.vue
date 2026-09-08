<script setup>
    import Sidebar from '../components/SideBar.vue'
</script>

<template>
    <div class="d-flex flex-column flex-md-row min-vh-100">
        <Sidebar />

        <main class="flex-grow-1 p-3 p-md-4 bg-light overflow-auto">
        <RouterView v-slot="{ Component }">
            <KeepAlive>
            <component :is="Component" />
            </KeepAlive>
        </RouterView>
        </main>

    </div>
</template>