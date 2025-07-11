<script setup lang="ts">
import { ref, defineExpose } from 'vue'

const visible = ref(false)
const message = ref('')

function show(msg: string) {
  message.value = msg
  visible.value = true
}

defineExpose({ show })
</script>

<template>
  <v-snackbar
    v-model="visible"
    :timeout="5000"
    color="red"
    location="top center"
    multi-line
    elevation="4"
    rounded="lg"
  >
    <span class="text-white font-weight-medium">{{ message }}</span>

    <template #actions>
      <v-btn icon @click="visible = false" variant="text">
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </template>
  </v-snackbar>
</template>

<style scoped>
</style>
