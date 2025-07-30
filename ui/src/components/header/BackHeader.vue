<script setup lang="ts">
import { useRouter } from 'vue-router'
import { defineProps } from 'vue'

const props = defineProps({
  title: {
    type: String,
    default: 'Zurück',
  },
})

const router = useRouter()

function goHome() {
  router.push('/')
}
</script>

<template>
  <v-row align="center" class="pa-2">
    <v-btn icon @click="goHome" variant="text">
      <v-icon>arrow_back</v-icon>
    </v-btn>
    <span class="text-subtitle-1 font-weight-medium ml-2">{{ title }}</span>
  </v-row>
</template>

<style scoped></style>
