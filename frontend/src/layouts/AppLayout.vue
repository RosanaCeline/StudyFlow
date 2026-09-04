<script setup>
    import Sidebar from '../components/SideBar.vue'
</script>

<template>
    <div class="d-flex min-vh-100">

        <Sidebar />

        <main class="grow p-4">
            <RouterView />
        </main>

    </div>
</template>