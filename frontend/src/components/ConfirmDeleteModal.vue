<script setup>
defineProps({
    subjectName: {
        type: String,
        default: ''
    }
})

const emit = defineEmits(['confirm'])
</script>

<template>
    <div
        class="modal fade"
        id="deleteSubjectModal"
        tabindex="-1"
        aria-labelledby="deleteSubjectModalLabel"
        aria-hidden="true"
    >
        <div class="modal-dialog modal-dialog-centered modal-md">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header border-0 pb-0">
                    <h5 id="deleteSubjectModalLabel" class="modal-title fw-normal fs-5">
                        Excluir disciplina
                    </h5>
                    <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Fechar"
                    ></button>
                </div>

                <div class="modal-body py-3">
                    <p class="mb-2">
                        Tem certeza que deseja excluir <strong class="text-break text-wrap">{{ subjectName }}</strong>?
                    </p>
                    <small class="text-muted d-block">
                        Essa ação não poderá ser desfeita.
                    </small>
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
                        class="btn btn-danger px-3 rounded-3"
                        @click="emit('confirm')"
                    >
                        <i class="bi bi-trash me-1"></i>
                        Excluir
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>