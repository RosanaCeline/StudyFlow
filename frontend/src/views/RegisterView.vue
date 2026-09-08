<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { registerUser } from '../services/authService'

const name = ref('')
const email = ref('')
const password = ref('')
const showPassword = ref(false)

const loading = ref(false)
const apiError = ref('')

const errors = ref({
  name: '',
  email: '',
  password: ''
})

const router = useRouter()

function validate() {
  errors.value = { name: '', email: '', password: '' }
  apiError.value = ''
  let isValid = true

  const nameTrimmed = name.value.trim()
  if (!nameTrimmed) {
    errors.value.name = 'O nome é obrigatório.'
    isValid = false
  } else if (nameTrimmed.length > 150) {
    errors.value.name = 'O nome deve ter no máximo 150 caracteres.'
    isValid = false
  }

  const emailTrimmed = email.value.trim()
  if (!emailTrimmed) {
    errors.value.email = 'O e-mail é obrigatório.'
    isValid = false
  } else if (!emailTrimmed.includes('@') || emailTrimmed.length > 255) {
    errors.value.email = 'Insira um e-mail válido.'
    isValid = false
  }

  if (!password.value) {
    errors.value.password = 'A senha é obrigatória.'
    isValid = false
  } else if (password.value.length > 70) {
    errors.value.password = 'A senha deve ter no máximo 70 caracteres.'
    isValid = false
  }

  return isValid
}

async function handleSubmit() {
  if (!validate()) return

  loading.value = true
  apiError.value = ''

  try {
    const payload = {
      name: name.value.trim(),
      email: email.value.trim(),
      password: password.value
    }

    const tokenDto = await registerUser(payload)

    if (tokenDto?.token) {
      localStorage.setItem('token', tokenDto.token)
    }

    router.push('/app/dashboard')
  } catch (err) {
    if (err.response && err.response.data) {
      apiError.value = err.response.data.message || 'Erro ao realizar o cadastro. Verifique os dados.'
    } else {
      apiError.value = 'Não foi possível conectar ao servidor. Tente novamente mais tarde.'
    }
  } finally {
    loading.value = false
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

                <p class="text-center mb-4">Faça seu cadastro para acessar a plataforma</p>

                <div v-if="apiError" class="alert alert-danger rounded-3 small shadow-sm mb-3">
                  {{ apiError }}
                </div>

                <form @submit.prevent="handleSubmit" novalidate>
                  <div class="mb-3">
                    <label class="form-label">
                      Nome <span class="text-danger">*</span>
                    </label>
                    <input 
                      type="text" 
                      class="form-control" 
                      :class="{ 'is-invalid': errors.name }"
                      v-model="name" 
                      placeholder="Insira seu nome" 
                      maxlength="150"
                      required
                    >
                    <div class="invalid-feedback" v-if="errors.name">
                      {{ errors.name }}
                    </div>
                  </div>

                  <div class="mb-3">
                    <label class="form-label">
                      E-mail <span class="text-danger">*</span>
                    </label>
                    <input 
                      type="email" 
                      class="form-control" 
                      :class="{ 'is-invalid': errors.email }"
                      v-model="email" 
                      placeholder="Insira um e-mail" 
                      maxlength="255"
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
                        placeholder="Insira uma senha" 
                        maxlength="70"
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

                  <button 
                    type="submit" 
                    class="btn btn-primary w-100 d-flex align-items-center justify-content-center gap-2"
                    :disabled="loading"
                  >
                    <span v-if="loading" class="spinner-border spinner-border-sm" role="status"></span>
                    <span>{{ loading ? 'Cadastrando...' : 'Cadastrar' }}</span>
                  </button>

                  <p class="text-center p-3 mb-0">
                    <RouterLink to="/login">Já tem uma conta?</RouterLink>
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
    background: radial-gradient(circle, rgba(24, 76, 120, 0.45) 0%, rgba(42, 157, 143, 0.3) 50%, rgba(255, 255, 255, 0) 70%);
    filter: blur(60px);
    z-index: 0;
    pointer-events: none;
    }

    .container {
    position: relative;
    z-index: 1;
    }

    .card {
    border: none;
    }

    .form-control:focus {
    border-color: var(--color-body-blue);
    box-shadow: 0 0 0 .25rem rgba(42, 157, 144, .25);
    }

    .btn-primary {
    background-color: var(--color-body-blue) !important;
    }
</style>