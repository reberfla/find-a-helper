<script setup lang="ts">
import { onMounted, ref, shallowRef } from 'vue'
import TaskOfferDialog from '@/components/task/TaskOfferDialog.vue'
import TaskCard from '@/components/task/TaskCard.vue'
import apiService from '@/service/apiService.ts'
import { type Task } from '../../models/TaskModel.ts'

async function loadTasks() {
  tasks.value = await apiService.getTasks()
}

const tasks = ref<Task[]>([])

const selectedTask = ref<Task>(tasks.value[0])
const offerDialog = shallowRef(false)

function openOffer(task: Task) {
  selectedTask.value = task
  offerDialog.value = true
}

onMounted(() => loadTasks())
</script>
<template>
  <v-dialog v-model="offerDialog">
    <TaskOfferDialog :task="selectedTask" @close-offer="offerDialog = false" />
  </v-dialog>
  <div class="d-flex flex-wrap justify-space-evenly">
    <TaskCard
      v-for="task in tasks"
      class="task"
      v-bind:key="task.id"
      :task="task"
      @open-offer="openOffer"
    />
  </div>
</template>

<style scoped>
.task {
  width: 300px;
  margin: 10px;
}
</style>
