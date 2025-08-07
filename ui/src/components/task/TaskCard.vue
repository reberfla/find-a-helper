<script setup lang="ts">
import { type Task, TaskInterval, Weekday } from '@/models/TaskModel.ts'
import { translate } from '@/service/translationService.ts'

const props = defineProps<{
  task: Task
}>()

const emit = defineEmits(['new-offer'])
</script>

<template>
  <v-card variant="elevated" elevation="5" class="task-card">
    <template v-slot:title>{{ task.title }}</template>
    <template v-slot:subtitle>{{ task.zipCode }}</template>
    <template v-slot:text>
      <div>{{ task.description }}</div>
      <v-divider class="my-3"></v-divider>

      <div>{{ translate('CATEGORY') }}: {{ translate(task.category) }}</div>
      <div>{{ translate('INTERVAL') }}: {{ translate(task.taskInterval) }}</div>
      <div v-if="task.taskInterval != TaskInterval.ONE_TIME">
        <p>{{ translate('WEEKDAYS') }}:</p>
        <v-chip-group multiple column>
          <v-chip
            v-for="day in Weekday"
            v-bind:class="{ 'day-active': task.weekdays.includes(day) }"
            :disabled="true"
            :text="translate(day.toString())"
            :key="day"
          ></v-chip>
        </v-chip-group>
      </div>
      <div>
        {{ translate('DEADLINE') }}:
        <span v-if="task.deadline">
          {{ new Date(task.deadline).toLocaleDateString() }}
        </span>
        <span v-else>{{ translate('NO_DEADLINE') }}</span>
      </div>
    </template>
    <v-card-actions class="justify-center align-end">
      <v-btn @click="$emit('new-offer', task)">{{ translate('MAKE_OFFER') }}</v-btn>
    </v-card-actions>
  </v-card>
</template>

<style scoped>
.task-card {
  border-color: red;
}

.day-active {
  background-color: green;
  opacity: 100%;
  color: white;
}
</style>
