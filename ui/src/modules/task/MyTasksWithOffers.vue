<script setup lang="ts">
import { ref } from 'vue'
import MyTasks from '@/modules/task/MyTasks.vue'
import OffersForTask from '@/modules/task/OffersForTask.vue'
import type { Task } from '@/models/TaskModel'

const selectedTask = ref<Task | null>(null)
function onOpenTask(task: Task) {
  selectedTask.value = task
}

const offerListKey = ref(0)
function bumpOfferList() {
  offerListKey.value++
}
</script>

<template>
  <v-row>
    <v-col cols="12" md="6">
      <MyTasks @open-task="onOpenTask" />
    </v-col>

    <v-col cols="12" md="6" v-if="selectedTask">
      <OffersForTask :key="offerListKey" :task-id="selectedTask.id" @changed="bumpOfferList" />
    </v-col>
  </v-row>
</template>
