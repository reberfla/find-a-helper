<script setup lang="ts">
import { computed, onMounted, ref, shallowRef } from 'vue'
import TaskOfferDialog from '@/components/task/TaskOfferDialog.vue'
import TaskCard from '@/components/task/TaskCard.vue'
import taskService from '@/service/TaskService.ts'
import { categories, interval, type Task, TaskCategory, TaskInterval } from '@/models/TaskModel.ts'
import TaskEditDialog from '@/components/task/TaskEditDialog.vue'
import { green } from 'vuetify/util/colors'
import { useRouter } from 'vue-router'
import { useAuth } from '@/service/userAuthService.ts'
import { drawer } from '@/utils/nav.ts'
import SnackBar from '@/components/Snackbar.vue'
import offerService from '@/service/OfferService.ts'

const route = useRouter()
const snackBar = ref<InstanceType<typeof SnackBar> | null>(null)

const { isLoggedIn, getCurrentUserId } = useAuth()

async function loadTasks() {
  const category = route.currentRoute.value.query['category'] as string | null
  if (category) {
    filterCategories.value = [category.toUpperCase() as TaskCategory]
  }
  tasks.value = await taskService.getTasks(category)
}

function canOffer(task: Task & { offerUserIds?: number[] }): boolean {
  const userId = getCurrentUserId()
  if (!isLoggedIn.value || !userId) return false
  return !(task.offerUserIds ?? []).includes(userId)
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

function onSaveOrUpdate($event: 'save' | 'update') {
  console.log($event)
  createTaskDialog.value = false
  snackBar.value?.show(`Angebot erfolgreich ${$event == 'save' ? 'abgegeben' : 'geändert'}`, 'info')
}

onMounted(() => loadTasks())
</script>
<template>
  <v-dialog v-model="createTaskDialog" max-width="800">
    <TaskEditDialog
      :task="{} as Task"
      @save="onSaveOrUpdate($event)"
      @update="onSaveOrUpdate($event)"
      @close="createTaskDialog = false"
      :update="false"
    />
  </v-dialog>
  <v-dialog v-model="offerDialog" max-width="800">
    <TaskOfferDialog :task="selectedTask" @close-offer="offerDialog = false" />
  </v-dialog>
  <div class="fixed-header" :style="{ left: drawer ? '250px' : '0px' }">
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
      <v-btn
        v-if="isLoggedIn"
        @click="() => (createTaskDialog = true)"
        :color="green.darken2"
        class="mx-auto"
        >Aufgabe erstellen
      </v-btn>
    </div>
    <div>
      <v-alert v-if="!isLoggedIn" type="warning"
        >Anmelden oder Registrieren um eine Aufgabe oder ein Angebot für eine Aufgabe zu erstellen.
      </v-alert>
    </div>
  </div>
  <v-container
    class="d-flex flex-wrap justify-space-evenly overflow-y-auto pe-3"
    style="padding-top: 120px"
  >
    <TaskCard
      v-for="task in displayTasks"
      class="task"
      v-bind:key="task.id"
      :task="task"
      :can-offer="canOffer(task)"
      :private="false"
      @open-offer="openOffer"
    />
  </v-container>

  <snackBar ref="snackBar" />
</template>

<style scoped>
.task {
  width: 300px;
  margin: 10px;
}

.fixed-header {
  position: fixed;
  top: 60px;
  right: 0;
  z-index: 10;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  padding: 16px 24px 8px 24px;
}
</style>
