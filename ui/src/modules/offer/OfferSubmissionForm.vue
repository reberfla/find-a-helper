<script setup lang="ts">
import { ref, watch } from 'vue'
import { translate } from '@/service/translationService.ts'

const props = defineProps<{
  modelValue: boolean
  task?: any
  offer?: any
  readonly?: boolean
}>()

const emit = defineEmits(['update:modelValue', 'save'])

const localDialog = ref(props.modelValue)

const formTask = ref({ ...props.task })
const formOffer = ref({ ...props.offer })
const t = translate

if (!props.offer) {
  formOffer.value = {
    title: '',
    text: '',
    status: 'SUBMITTED',
    userId: null,
    task: props.task,
  }
}

watch(() => props.modelValue, val => localDialog.value = val)
watch(localDialog, val => emit('update:modelValue', val))

function close() {
  localDialog.value = false
}

function save() {
  emit('save', formOffer.value)
  close()
}
</script>

<template>
  <v-dialog v-model="localDialog" max-width="600">
    <v-card>
      <v-card-title class="text-h6">
        {{ t('TASK') }}: {{ formTask?.title || t('NEW_OFFER') }}
      </v-card-title>

      <v-card-text>
        <v-text-field
          :model-value="formTask?.title"
          :label="t('TASK')"
          readonly
        />
        <v-textarea
          :model-value="formTask?.description"
          :label="t('TASK_DESCRIPTION')"
          readonly
          auto-grow
        />

        <v-text-field
          v-model="formOffer.title"
          :label="t('OFFER_TITLE')"
          :readonly="props.readonly"
        />

        <v-textarea
          v-model="formOffer.text"
          :label="t('OFFER_TEXT')"
          :readonly="props.readonly"
          auto-grow
        />

        <v-text-field
          :model-value="t(formOffer.status)"
          :label="t('STATUS')"
          readonly
        />
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn text @click="close">{{ t('CANCEL_BUTTON') }}</v-btn>
        <v-btn
          color="primary"
          v-if="!props.readonly"
          @click="save"
        >
          {{ t('SAVE_BUTTON') }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
