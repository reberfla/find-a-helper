<script setup lang="ts">
import {
  categoryMap,
  intervalMap,
  type Task,
  TaskInterval,
  Weekday,
  weekdayMap,
} from '@/models/TaskModel.ts'
import { getIconOfCategory, getColorOfCategory } from '@/models/TaskModel.ts'
import { green, red } from 'vuetify/util/colors'
import { useAuth } from '@/service/userAuthService.ts'
import {computed} from "vue";

const { isLoggedIn } = useAuth()

const props = defineProps<{
  task: Task
  private: boolean,
  hasOffer?:boolean
}>()

const actionsLocked = computed(() => props.hasOffer)
const emit = defineEmits(['open-offer', 'edit-task', 'delete-task'])
</script>

<template>
  <v-card
    variant="elevated"
    :style="{ border: `2px solid ${getColorOfCategory(task.category)}` }"
    elevation="5"
  >
    <template v-slot:title>
      <div class="d-flex justify-space-between align-center">
        <span>{{ task.title }}</span>
        <v-icon class="mr-2" small>{{ getIconOfCategory(task.category) }}</v-icon>
      </div>
    </template>
    <template v-slot:subtitle>
      <div class="d-flex align-stretch">
        <span>{{ task.zipCode }}</span>
      </div>
    </template>
    <template v-slot:text>
      <div>{{ task.description }}</div>
      <v-divider class="my-3"></v-divider>
      <template v-if="private">
        <div><strong>Erstellt von: </strong>{{ task.name }}</div>
        <div><strong>E-Mail: </strong>{{ task.email }}</div>
        <div><strong>Koordinaten: </strong>{{ task.coordinates }}</div>
        <div><strong>Status: </strong>{{ task.status }}</div>
        <div>
          <strong>Aktiv: </strong><span v-if="task.active">Ja</span><span v-else>Nein</span>
        </div>
        <div>
          <strong>Erstellt am: </strong> {{ new Date(task.createdAt * 1000).toLocaleDateString() }}
        </div>
      </template>

      <div><strong>Kategorie: </strong>{{ categoryMap.get(task.category) }}</div>
      <div>
        <strong>Zu erledigen bis: </strong
        >{{
          task.deadline ? new Date(task.deadline * 1000).toLocaleDateString() : 'nicht angegeben'
        }}
      </div>
      <div><strong>Wiederholung: </strong>{{ intervalMap.get(task.taskInterval) }}</div>
      <div v-if="task.taskInterval == TaskInterval.RECURRING">
        <strong>Wochentage:</strong>
        <v-chip-group multiple column>
          <v-chip
            v-for="day in Weekday"
            v-bind:class="{ 'day-active': task.weekdays.includes(day) }"
            :disabled="true"
            :text="weekdayMap.get(day)"
            :key="day"
          ></v-chip>
        </v-chip-group>
      </div>
      <div class="mb-8"></div>
    </template>
    <v-card-actions  class="justify-center position-bottom">
      <v-btn v-if="!private && isLoggedIn" @click="$emit('open-offer', task)">Angebot machen</v-btn>
      <div v-if="private">
        <v-btn
          variant="elevated"
          class="mx-1"
          :disabled="actionsLocked"
          :color="green.accent1"
          @click="$emit('edit-task', task)"
          >Bearbeiten
        </v-btn>
        <v-btn
          variant="elevated"
          class="mx-1"
          :disabled="actionsLocked"
          :color="red.accent1"
          @click="$emit('delete-task', task.id)"
        >
          Löschen
        </v-btn>
      </div>
    </v-card-actions>
      <slot name="offers" />
  </v-card>
</template>

<style scoped>
.day-active {
  background-color: green;
  opacity: 100%;
  color: white;
}

</style>
