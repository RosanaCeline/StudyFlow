<script setup>
    import { useRouter } from 'vue-router'

    const router = useRouter()

    function handleLogout() {
    localStorage.clear()
    router.replace('/login')
    }

    function closeOffcanvas() {
    const offcanvasEl = document.getElementById('sidebarOffcanvas')
    if (offcanvasEl) {
        const closeBtn = offcanvasEl.querySelector('.btn-close')
        if (closeBtn) closeBtn.click()
    }
    }
</script>

<template>
    <div class="d-md-none p-3 border-bottom bg-white d-flex align-items-center justify-content-between w-100">
        <div class="d-flex align-items-center gap-2">
        <button 
            class="btn btn-nav p-2 d-flex align-items-center justify-content-center" 
            type="button" 
            data-bs-toggle="offcanvas" 
            data-bs-target="#sidebarOffcanvas" 
            aria-controls="sidebarOffcanvas"
        >
            <i class="bi bi-list fs-4"></i>
        </button>
        <span class="fw-semibold fs-5 text-dark">StudyFlow</span>
        </div>
    </div>

    <div 
        class="offcanvas-md offcanvas-start d-flex flex-column p-4 border-end sidebar bg-white" 
        tabindex="-1" 
        id="sidebarOffcanvas" 
        aria-labelledby="sidebarOffcanvasLabel"
    >
        <div class="d-flex d-md-none justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <h5 class="offcanvas-title fw-semibold" id="sidebarOffcanvasLabel">StudyFlow</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" data-bs-target="#sidebarOffcanvas" aria-label="Close"></button>
        </div>

        <h1 class="fs-4 mb-5 d-none d-md-block fw-semibold text-dark">
        StudyFlow
        </h1>

        <nav class="nav nav-pills flex-column gap-1">
        <RouterLink
            to="/app/dashboard"
            class="nav-link meu-link d-flex align-items-center"
            @click="closeOffcanvas"
        >
            <i class="bi bi-house me-2 fs-5"></i>
            <span>Dashboard</span>
        </RouterLink>

        <RouterLink
            to="/app/subjects"
            class="nav-link meu-link d-flex align-items-center"
            :class="{ 'router-link-active': $route.path.startsWith('/app/subjects') }"
            @click="closeOffcanvas"
        >
            <i class="bi bi-book me-2 fs-5"></i>
            <span>Disciplinas</span>
        </RouterLink>
        </nav>

        <div class="mt-auto pt-3 border-top">
        <button 
            type="button" 
            class="btn nav-link meu-link btn-logout w-100 text-start d-flex align-items-center p-2"
            @click="handleLogout"
        >
            <i class="bi bi-box-arrow-right me-2 fs-5"></i>
            <span>Sair</span>
        </button>
        </div>
    </div>
</template>

<style scoped>
    .sidebar {
        min-width: 230px;
        width: 230px;
        height: 100vh;
    }

    .btn-nav:active {
        border-color: transparent !important;
    }

    .meu-link {
        color: var(--color-text-light, #6c757d);
        border-radius: 8px;
        padding: 0.6rem 0.8rem;
    }

    .meu-link i {
        color: inherit;
    }

    .meu-link:not(.router-link-active):hover {
        color: var(--color-text-light, #212529);
        background-color: var(--color-background-hover, #f8f9fa);
    }

    .router-link-active:hover {
        filter: brightness(0.92);
    }

    .router-link-active,
    .router-link-active:focus,
    .router-link-active:active {
        background-color: var(--color-body, #0d6efd);
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

    @media (max-width: 767.98px) {
        .sidebar {
            height: 100%;
        }
    }
</style>