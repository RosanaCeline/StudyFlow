<script setup>
    import { ref } from 'vue'
    import bgLeft from '@/assets/images/background-left.svg'
    import bgRight from '@/assets/images/background-right.svg'

    const email = ref('')
    const password = ref('')
    const showPassword = ref(false)

    const errors = ref({
        email: '',
        password: ''
    })

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
            //contece nada por enquanto
        }
    }
</script>

<template>
    <div class="login-page">
        <img :src="bgLeft" class="bg-img img-left" alt="Ilustração Esquerda">
        <img :src="bgRight" class="bg-img img-right" alt="Ilustração Direita">

        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-5">
                    <div class="card shadow">
                        <div class="card-body p-5">
                            <h1 class="text-center">StudyFlow</h1>

                            <p class="text-center mb-4">Faça login para continuar</p>

                            <form @submit.prevent="handleSubmit" novalidate>
                                <div class="mb-4">
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

                                <div class="mb-4">
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
</template>

<style scoped>
    .login-page {
        min-height: 100vh;
        display: flex;
        align-items: center;
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
    }

    .btn:hover {
        filter: brightness(0.92);
    }
</style>