<script setup>
    import { ref } from 'vue'
    import { useRouter } from 'vue-router'

    const email = ref('')
    const password = ref('')
    const showPassword = ref(false)

    const errors = ref({
        email: '',
        password: ''
    })

    const router = useRouter()

    function handleSubmit() {
        errors.value.email = ''
        errors.value.password = ''

        let isValid = true

        if (!email.value) {
            errors.value.email = 'O e-mail é obrigatório.'
            isValid = false
        } else if (!email.value.includes('@')) {
            errors.value.email = 'Insira um e-mail válido.'
            isValid = false
        }

        if (!password.value) {
            errors.value.password = 'A senha é obrigatória.'
            isValid = false
        }

        if (isValid) {
            router.push('/app/dashboard')
        }
    }
</script>

<template>
    <div class="login-page">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-sm-10 col-md-8 col-lg-5">
                    <div class="card-wrapper">
                        <div class="glow-orb"></div>
                        <div class="card shadow">
                            <div class="card-body p-4 p-md-5">
                                <h1 class="text-center brand-title text-nowrap">StudyFlow</h1>

                                <p class="text-center mb-4">Faça login para continuar</p>

                                <form @submit.prevent="handleSubmit" novalidate>
                                    <div class="mb-3">
                                        <label class="form-label">
                                            E-mail <span class="text-danger">*</span>
                                        </label>
                                        <input 
                                            type="email" 
                                            class="form-control" 
                                            :class="{ 'is-invalid': errors.email }"
                                            v-model="email" 
                                            placeholder="Digite seu e-mail" 
                                            required
                                        >
                                        <div class="invalid-feedback" v-if="errors.email">
                                            {{ errors.email }}
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            Senha <span class="text-danger">*</span>
                                        </label>
                                        <div class="input-group">
                                            <input 
                                                :type="showPassword ? 'text' : 'password'"
                                                class="form-control" 
                                                :class="{ 'is-invalid': errors.password }"
                                                v-model="password" 
                                                placeholder="Digite sua senha" 
                                                required
                                            >
                                            <button 
                                                type="button" 
                                                class="btn btn-outline-secondary" 
                                                @click="showPassword = !showPassword"
                                                tabindex="-1"
                                            >
                                                <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                                            </button>
                                            <div class="invalid-feedback" v-if="errors.password">
                                                {{ errors.password }}
                                            </div>
                                        </div>
                                    </div>

                                    <button type="submit" class="btn btn-primary w-100">
                                        Entrar
                                    </button>

                                    <p class="text-center p-3">
                                        <a href="">Criar uma conta</a>
                                    </p>
                                </form>
                            </div>
                        </div> 
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .login-page {
        min-height: 100vh;
        display: flex;
        align-items: center;
    }

    .card-wrapper {
        position: relative;
    }

    .glow-orb {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 80vw;
        height: 90vh;
        border-radius: 50%;
        /* Gradiente radial combinando tons de roxo, rosa ou azul */
        background: radial-gradient(circle, rgba(128, 18, 69, 0.4) 0%, rgba(217, 64, 82, 0.3) 50%, rgba(255, 255, 255, 0) 70%);
        filter: blur(60px); /* Aplica o desfoque suave */
        z-index: 0;
        pointer-events: none;
    }

    .bg-img {
        position: absolute;
        bottom: 0;
        height: auto;
        pointer-events: none; 
        z-index: 0;     
    }

    .img-left {
        max-width: 25rem; 
        left: 20px;
    }

    .img-right {
        max-width: 30rem; 
        right: 20px;
    }

    .container {
        position: relative;
        z-index: 1;
    }

    .card {
        border: none;
    }

    .form-control:focus {
        border-color: var(--color-body-dark);
        box-shadow: 0 0 0 .25rem rgba(217, 64, 82, .25);
    }

    .btn {
        border: 1px solid transparent;
        background-color: var(--color-body);
        color: white;
    }

    .btn:hover {
        filter: brightness(0.92);
    }
</style>