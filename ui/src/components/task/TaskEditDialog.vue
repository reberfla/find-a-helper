<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { type Task, TaskCategory, TaskInterval, Weekday } from '@/models/TaskModel.ts'
import { translate } from '@/service/translationService.ts'
import taskService from '@/service/TaskService.ts'

const categories = Object.values(TaskCategory).map((category) => ({
  title: translate(category),
  value: category,
}))
const interval = Object.values(TaskInterval).map((interval) => ({
  title: translate(interval),
  value: interval,
}))

const editTask = ref<Partial<Task>>({})

const updateTask = computed(() => {
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
  task: Task | undefined
  update: boolean
}>()

onMounted(() => {
  if (props.task) {
    editTask.value = props.task
  }
})

defineEmits(['close', 'delete'])
</script>

<template>
  <v-card>
    <template v-slot:title v-if="props.task">Aufgabe bearbeiten</template>
    <template v-slot:title v-else>Aufgabe erstellen</template>
    <template v-slot:text>
      <v-form>
        <v-text-field
          density="compact"
          name="title"
          label="Titel"
          required
          v-model="editTask.title"
          variant="outlined"
        ></v-text-field>
        <v-text-field
          density="compact"
          name="coordinates"
          label="Koordinaten"
          required
          v-model="editTask.coordinates"
          variant="outlined"
        ></v-text-field>
        <v-text-field
          density="compact"
          name="zipCode"
          label="Postleitzahl"
          required
          v-model="editTask.zipCode"
          variant="outlined"
        ></v-text-field>
        <v-textarea
          name="description"
          label="Beschreibung"
          required
          v-model="editTask.description"
          variant="outlined"
        ></v-textarea>
        <v-select
          name="category"
          label="Kategorie"
          v-model="editTask.category"
          :items="categories"
          density="compact"
          variant="outlined"
        ></v-select>
        <v-select
          name="interval"
          label="Aufgaben wiederholung"
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
        <v-date-input name="deadline" label="Deadline" v-model="editTask.deadline"></v-date-input>
      </v-form>
    </template>
    <template v-slot:actions>
      <v-btn v-if="!update" @click="taskService.createTask(editTask)">Speichern</v-btn>
      <v-btn
        v-if="update"
        @click="
          () => {
            taskService.updateTask(updateTask, task!.id)
            $emit('close')
          }
        "
        >Speichern
      </v-btn>
      <v-btn @click="$emit('close')">Abbrechen</v-btn>
    </template>
  </v-card>
</template>

<style scoped>
.day-active {
  background-color: green;
  opacity: 100%;
  color: white;
}
</style>
