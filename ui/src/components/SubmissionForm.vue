<script setup lang="ts" generic="T">
import { ref, watch } from 'vue'
import type { SubmissionFormConfig } from '@/core/factory/SubmissionFormConfig';
import { translate as t } from '@/service/translationService'

const props = defineProps<{
  modelValue: boolean
  config: SubmissionFormConfig<T>
  readonly?: boolean
  context?: any   //  task, offer, assignments
}>()
const emit = defineEmits(['update:modelValue', 'save'])

const localDialog = ref(props.modelValue)
const formData = ref<T>(props.config.getInitialData(props.context))

watch(() => props.modelValue, val => localDialog.value = val)
watch(localDialog, val => emit('update:modelValue', val))

function close() {
  localDialog.value = false
}
async function save() {
  await props.config.save(formData.value)
  emit('save', formData.value)
  close()
}
</script>

<template>
  <v-dialog v-model="localDialog" max-width="600">
    <v-card>
      <v-card-title class="text-h6">{{ props.config.title }}</v-card-title>
      <v-card-text>
        <template v-for="field in props.config.fields" :key="field.key">
          <v-text-field
            v-if="field.type === 'text'"
            v-model="formData[field.key]"
            :label="t(field.label)"
            :readonly="props.readonly || field.readonly"
          />
          <v-textarea
            v-else-if="field.type === 'textarea'"
            v-model="formData[field.key]"
            :label="t(field.label)"
            :readonly="props.readonly || field.readonly"
            auto-grow
          />
        </template>
      </v-card-text>
      <v-card-actions>
        <v-spacer/>
        <v-btn text @click="close">{{ t('CANCEL_BUTTON') }}</v-btn>
        <v-btn color="primary" v-if="!props.readonly" @click="save">
          {{ t('SAVE_BUTTON') }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
