<template>
  <v-dialog
    :model-value="props.visible"
    @update:model-value="emit('update:visible', $event)"
    max-width="400"
  >
    <v-card>
      <v-card-title>{{ props.title }}</v-card-title>
      <v-card-text>{{ props.message }}</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="grey" @click="closeAndCancel">{{ props.cancelLabel }}</v-btn>
        <v-btn color="primary" @click="closeAndConfirm">{{ props.confirmLabel }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
const props = defineProps<{
  visible: boolean
  title: string
  message: string
  confirmLabel: string
  cancelLabel: string
  onConfirm: () => void
  onCancel: () => void
}>()

const emit = defineEmits(['update:visible'])

function closeAndCancel() {
  emit('update:visible', false)
  props.onCancel()
}

function closeAndConfirm() {
  emit('update:visible', false)
  props.onConfirm()
}
</script>
