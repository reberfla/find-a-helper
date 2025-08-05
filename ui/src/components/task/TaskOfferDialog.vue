<script setup lang="ts">
import type { Task } from '@/models/TaskModel.ts'

defineProps<{ task: Task }>()

const emit = defineEmits(['close-offer'])
</script>

<template>
  <v-card>
    <template v-slot:title>{{ task.title }}</template>
    <template v-slot:subtitle>{{ task.zipCode }}</template>
    <template v-slot:text>
      <div>{{ task.description }}</div>
      <v-divider class="my-3"></v-divider>
      <div>Kategorie: {{ task.category }}</div>
      <div>Wiederholung: {{ task.taskInterval }}</div>
      <div>Wochentage: {{ task.weekdays }}</div>
      <div>
        Zu erledigen bis:
        <span v-if="task.deadline">
          {{ new Date(task.deadline).toLocaleDateString() }}
        </span>
        <span v-else> Nicht angegeben </span>
      </div>
    </template>
    <v-card-actions class="justify-center">
      <v-btn @click="$emit('close-offer')">Schliessen</v-btn>
    </v-card-actions>
  </v-card>
</template>

<style scoped></style>
