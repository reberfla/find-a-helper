<script setup lang="ts">
import { ref, computed } from 'vue'

const visible = ref(false)
const message = ref('')
const type = ref<'error' | 'warning' | 'info' | 'success'>('info')

function show(msg: string, msgType: 'error' | 'warning' | 'info' | 'success' = 'info') {
  message.value = msg
  type.value = msgType
  visible.value = true
}

defineExpose({ show })

const color = computed(() => {
  switch (type.value) {
    case 'error':
      return 'red'
    case 'warning':
      return 'orange'
    case 'success':
      return 'green'
    default:
      return 'blue'
  }
})
</script>

<template>
  <v-snackbar
    v-model="visible"
    :timeout="5000"
    :color="color"
    elevation="4"
    location="bottom center"
    multi-line
    rounded="lg"
  >
    <span class="text-white font-weight-medium">{{ message }}</span>

    <template #actions>
      <v-btn icon variant="text" @click="visible = false">
        <v-icon>close</v-icon>
      </v-btn>
    </template>
  </v-snackbar>
</template>
