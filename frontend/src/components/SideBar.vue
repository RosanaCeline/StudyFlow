<script setup>
    import { useRouter } from 'vue-router'

    const router = useRouter()

    function handleLogout() {
        localStorage.clear()
        router.replace('/login')
    }
</script>

<template>
    <aside class="d-flex flex-column vh-100 p-4 border-end sidebar">

        <h1 class="fs-4 mb-5">
            StudyFlow
        </h1>

        <nav class="nav nav-pills flex-column gap-1">

            <RouterLink
                to="/app/dashboard"
                class="nav-link meu-link"
            >
                <i class="bi bi-house me-2"></i>
                Dashboard
            </RouterLink>

            <RouterLink
                to="/app/subjects"
                class="nav-link meu-link"
            >
                <i class="bi bi-book me-2"></i>
                Disciplinas
            </RouterLink>
        </nav>

        <div class="mt-auto pt-3 border-top">
            <button 
                type="button" 
                class="btn nav-link meu-link btn-logout w-100 text-start d-flex align-items-center p-2"
                @click="handleLogout"
            >
                <i class="bi bi-box-arrow-right me-2"></i>
                Sair
            </button>
        </div>

    </aside>
</template>

<style scoped>
    .sidebar {
        min-width: 260px;
    }
    .meu-link {
        color: var(--color-text-light);
        border-radius: 8px;
    }

    .meu-link i {
        color: inherit;
    }

    .meu-link:not(.router-link-active):hover {
        color: var(--color-text-light);
        background-color: var(--color-background-hover);
    }
    
    .router-link-active:hover {
        filter: brightness(0.92);
    }

    .router-link-active,
    .router-link-active:focus,
    .router-link-active:active{
        background-color: var(--color-body);
        color: white !important;
        box-shadow: none;
    }

    .btn-logout {
        color: #dc3545 !important;
        transition: background-color 0.15s ease, color 0.15s ease;
    }

    .btn-logout:hover {
        background-color: #f8d7da !important;
        color: #b02a37 !important;
    }
</style>