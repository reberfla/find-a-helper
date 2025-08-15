<script setup lang="ts">
import TaskCard from '@/components/task/TaskCard.vue'
import taskService from '@/service/TaskService.ts'
import { onMounted, ref, shallowRef } from 'vue'
import type { Task } from '@/models/TaskModel.ts'
import TaskEditDialog from '@/components/task/TaskEditDialog.vue'

async function loadMyTasks() {
  tasks.value = await taskService.getMyTasks()
}

const tasks = ref<Task[]>([])
const selectedTask = ref<Task>(tasks.value[0])
const editDialog = shallowRef(false)

function editTask(task: Task) {
  selectedTask.value = task
  editDialog.value = true
}

function deleteTask(id: number) {
  taskService.deleteTask(id).then(() => {
    tasks.value = tasks.value.filter((task) => task.id !== id)
  })
}

onMounted(() => {
  loadMyTasks()
})
</script>

<template>
  <v-dialog v-model="editDialog" class="dialog">
    <TaskEditDialog :task="selectedTask" @close="editDialog = false" :update="true" />
  </v-dialog>
  <div class="d-flex flex-wrap justify-space-evenly">
    <TaskCard
      v-for="task in tasks"
      class="task"
      v-bind:key="task.id"
      :task="task"
      :private="true"
      @edit-task="editTask"
      @delete-task="deleteTask"
    />
  </div>
</template>

<style scoped>
.task {
  width: 300px;
  margin: 10px;
}
.dialog {
  max-width: 1000px;
  width: 100%;
}
</style>
