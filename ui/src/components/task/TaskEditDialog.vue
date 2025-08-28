<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { categories, interval, type Task, TaskInterval, Weekday } from '@/models/TaskModel.ts'
import { translate } from '@/service/translationService.ts'
import taskService from '@/service/TaskService.ts'
import TaskEditDialog from '@/components/task/TaskEditDialog.vue'
import router from '@/router'

const valid = ref(false)
const required = (value: any) => {
  if (!value) {
    return 'Dieses Feld ist erforderlich'
  } else {
    return true
  }
}

const coordinatesPattern = (value: string) => {
  const regex = /^-?\d+(\.\d+)?,-?\d+(\.\d+)?$/
  if (!regex.test(value)) {
    return 'Bitte geben Sie gültige Koordinaten im Format "Breitengrad,Längengrad" ein'
  }
  return true
}

const editTask = ref<Partial<Task>>({})

const toUpdateTask = computed(() => {
  return {
    title: editTask.value.title,
    coordinates: editTask.value.coordinates,
    zipCode: editTask.value.zipCode,
    description: editTask.value.description,
    category: editTask.value.category,
    taskInterval: editTask.value.taskInterval,
    weekdays: editTask.value.weekdays || [],
    deadline: editTask.value.deadline,
    status: editTask.value.status,
    active: editTask.value.active,
  }
})

const props = defineProps<{
  task: Task
  update: boolean
}>()

const date = ref(props.task.deadline ? new Date(props.task.deadline * 1000) : undefined)

async function createTask(this: typeof TaskEditDialog, task: Partial<Task>) {
  if (!valid.value) {
    return
  }
  await taskService.createTask(task).then(() => {
    router.push({ path: '/tasks/my' })
    this.emit('save')
  })
}

async function updateTask(this: typeof TaskEditDialog, task: Partial<Task>, id: number) {
  await taskService.updateTask(task, id)
  this.emit('update')
}

onMounted(() => {
  if (props.task) {
    editTask.value = props.task
  }
})

const emit = defineEmits(['save', 'update', 'close', 'delete'])
</script>

<template>
  <v-card>
    <template v-slot:title v-if="props.task">Aufgabe bearbeiten</template>
    <template v-slot:title v-else>Aufgabe erstellen</template>
    <template v-slot:text>
      <v-form
        @submit.prevent="update ? updateTask(toUpdateTask, task.id) : createTask(editTask)"
        v-model="valid"
        class="d-flex flex-column gap-space"
      >
        <v-text-field
          density="compact"
          name="title"
          label="Titel*"
          :rules="[required]"
          v-model="editTask.title"
          variant="outlined"
        ></v-text-field>
        <v-text-field
          density="compact"
          name="coordinates"
          label="Koordinaten*"
          :rules="[required, coordinatesPattern]"
          v-model="editTask.coordinates"
          variant="outlined"
        ></v-text-field>
        <v-text-field
          density="compact"
          name="zipCode"
          label="Postleitzahl*"
          :rules="[required]"
          v-model="editTask.zipCode"
          variant="outlined"
        ></v-text-field>
        <v-textarea
          name="description"
          label="Beschreibung*"
          :rules="[required]"
          v-model="editTask.description"
          variant="outlined"
        ></v-textarea>
        <v-select
          name="category"
          label="Kategorie*"
          :rules="[required]"
          v-model="editTask.category"
          :items="categories"
          density="compact"
          variant="outlined"
        ></v-select>
        <v-date-input
          name="deadline"
          label="Deadline"
          v-model="date"
          density="compact"
          variant="outlined"
          prepend-icon=""
          clearable
          @update:modelValue="
            () => {
              editTask.deadline = Math.floor(date!.getTime() / 1000)
            }
          "
          @click:clear="
            () => {
              editTask.deadline = null
            }
          "
        >
        </v-date-input>
        <v-select
          name="interval"
          label="Aufgaben wiederholung*"
          :rules="[required]"
          v-model="editTask.taskInterval"
          :items="interval"
          density="compact"
          variant="outlined"
        ></v-select>
        <v-chip-group
          multiple
          column
          v-if="editTask.taskInterval == TaskInterval.RECURRING"
          v-model="editTask.weekdays"
          variant="outlined"
        >
          <v-chip
            v-for="day in Weekday"
            :text="translate(day.toString())"
            :key="day"
            :value="day"
            selected-class="day-active"
          ></v-chip>
        </v-chip-group>
        <p>*erforderlich</p>
        <div class="d-flex justify-end mt-4">
          <v-btn type="submit" color="success" class="mr-4">Speichern</v-btn>
          <v-btn @click="$emit('close')" color="error">Abbrechen</v-btn>
        </div>
      </v-form>
    </template>
    <template v-slot:actions></template>
  </v-card>
</template>

<style scoped>
.day-active {
  background-color: green;
  opacity: 100%;
  color: white;
}
.gap-space {
  row-gap: 8px;
}
</style>
