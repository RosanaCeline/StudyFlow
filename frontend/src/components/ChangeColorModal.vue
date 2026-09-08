<script setup>
    import { ref, watch } from 'vue'

    const props = defineProps({
        currentColor: {
            type: String,
            default: '#DDEBFF'
        },
        colors: {
            type: Array,
            default: () => [
                '#DDEBFF', '#E4D9FF', '#F8D7DA', '#FFE5B4',
                '#FFF3BF', '#DFF5E1', '#D5F5F6', '#F3D9E8'
            ]
        },
        loading: {
            type: Boolean,
            default: false
        }
    })

    const emit = defineEmits(['save'])

    const selectedColor = ref(props.currentColor)

    watch(() => props.currentColor, (newColor) => {
        selectedColor.value = newColor
    })

    function handleSave() {
        emit('save', selectedColor.value)
    }
</script>

<template>
    <div
        class="modal fade"
        id="changeColorModal"
        tabindex="-1"
        aria-labelledby="changeColorModalLabel"
        aria-hidden="true"
    >
        <div class="modal-dialog modal-dialog-centered modal-sm">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header border-0 pb-0">
                    <h5 id="changeColorModalLabel" class="modal-title fw-normal fs-5">
                        Cor do tema
                    </h5>
                    <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Fechar"
                    ></button>
                </div>

                <div class="modal-body py-3">
                    <div class="d-flex flex-wrap gap-2 justify-content-center">
                        <button
                            v-for="color in colors"
                            :key="color"
                            type="button"
                            class="color-option rounded-circle border-0 p-0 d-flex align-items-center justify-content-center"
                            :class="{ selected: selectedColor === color }"
                            :style="{ backgroundColor: color }"
                            :title="`Selecionar cor ${color}`"
                            @click="selectedColor = color"
                        >
                            <i
                                v-if="selectedColor === color"
                                class="bi bi-check2 text-dark fs-5"
                            ></i>
                        </button>
                    </div>
                </div>

                <div class="modal-footer border-0 pt-0">
                    <button
                        type="button"
                        class="btn btn-link text-decoration-none text-secondary"
                        data-bs-dismiss="modal"
                    >
                        Cancelar
                    </button>
                    <button
                        type="button"
                        class="btn btn-primary px-3 rounded-3"
                        :disabled="loading"
                        @click="handleSave"
                    >
                        <span v-if="loading" class="spinner-border spinner-border-sm me-1"></span>
                        Salvar
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .btn-primary {
        background-color: var(--color-body) !important;
    }

    .color-option {
        width: 36px;
        height: 36px;
        cursor: pointer;
        transition: transform 0.15s ease, box-shadow 0.15s ease;
    }

    .color-option:hover {
        transform: scale(1.1);
    }

    .color-option.selected {
        box-shadow: 0 0 0 3px white, 0 0 0 5px var(--color-body-light);
    }
</style>