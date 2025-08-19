<script setup lang="ts">
import { computed, onMounted, ref, shallowRef } from 'vue'
import TaskOfferDialog from '@/components/task/TaskOfferDialog.vue'
import TaskCard from '@/components/task/TaskCard.vue'
import taskService from '@/service/TaskService.ts'
import { categories, interval, type Task, TaskCategory, TaskInterval } from '@/models/TaskModel.ts'
import TaskEditDialog from '@/components/task/TaskEditDialog.vue'
import { green } from 'vuetify/util/colors'
import { useRouter } from 'vue-router'

const route = useRouter()

async function loadTasks() {
  const category = route.currentRoute.value.query['category'] as string | null
  if (category) {
    filterCategories.value = [category.toUpperCase() as TaskCategory]
  }
  tasks.value = await taskService.getTasks(category)
}

const searchTerm = ref('')

const tasks = ref<Task[]>([])

const filterCategories = shallowRef<TaskCategory[]>([])
const filterInterval = shallowRef<TaskInterval[]>([])

const selectedTask = ref<Task>(tasks.value[0])
const offerDialog = shallowRef(false)
const createTaskDialog = shallowRef(false)

async function filterTasks(isOpen: boolean) {
  if (!isOpen) {
    tasks.value = await taskService.getFilteredTasks(filterCategories.value, filterInterval.value)
  }
}

const displayTasks = computed(() => {
  if (searchTerm.value.length > 0) {
    return tasks.value.filter((task) => {
      return (
        task.title.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
        task.description.toLowerCase().includes(searchTerm.value.toLowerCase())
      )
    })
  } else {
    return tasks.value
  }
})

function openOffer(task: Task) {
  selectedTask.value = task
  offerDialog.value = true
}

onMounted(() => loadTasks())
</script>
<template>
  <v-dialog v-model="createTaskDialog" max-width="800">
    <TaskEditDialog :task="{} as Task" @close="createTaskDialog = false" :update="false" />
  </v-dialog>
  <v-dialog v-model="offerDialog" max-width="800">
    <TaskOfferDialog :task="selectedTask" @close-offer="offerDialog = false" />
  </v-dialog>
  <div class="d-flex w-100 align-top">
    <v-text-field
      density="compact"
      v-model="searchTerm"
      placeholder="Suchen"
      variant="outlined"
    ></v-text-field>
    <v-select
      title="Kategorie"
      label="Kategorie"
      density="compact"
      v-model="filterCategories"
      :items="categories"
      variant="outlined"
      multiple
      clearable
      class="mx-2 w-30"
      @update:menu="filterTasks"
    ></v-select>
    <v-select
      title="Interval"
      label="Interval"
      density="compact"
      v-model="filterInterval"
      :items="interval"
      variant="outlined"
      multiple
      class="mx-2 w-30"
      @update:menu="filterTasks"
    ></v-select>
    <v-btn @click="() => (createTaskDialog = true)" :color="green.darken2" class="mx-auto"
      >Aufgabe erstellen
    </v-btn>
  </div>
  <div class="d-flex flex-wrap justify-space-evenly">
    <TaskCard
      v-for="task in displayTasks"
      class="task"
      v-bind:key="task.id"
      :task="task"
      :private="false"
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
