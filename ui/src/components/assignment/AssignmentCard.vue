<script setup lang="ts">
import { type AssignmentModel, AssignmentStatus } from '@/models/AssignmentModel.ts'
import {
  categoryMap,
  getColorOfCategory,
  getIconOfCategory,
  intervalMap,
  TaskCategory,
  TaskInterval,
  TaskStatus,
  Weekday,
  weekdayMap,
} from '@/models/TaskModel.ts'

const mockAssignment: AssignmentModel = {
  id: 1,
  taskCreatorUser: {
    id: 101,
    name: 'Anna Müller',
    email: 'anna.mueller@example.de',
  },
  offerCreatorUser: {
    id: 202,
    name: 'Max Schmidt',
    email: 'max.schmidt@example.de',
  },
  task: {
    id: 301,
    title: 'Datenbank aktualisieren',
    description: 'Bitte aktualisieren Sie die Kundendatenbank bis Ende der Woche.',
    category: TaskCategory.TECHHELP,
    zipCode: '1234',
    coordinates: '12,34',
    status: TaskStatus.ASSIGNED,
    active: true,
    taskInterval: TaskInterval.ONE_TIME,
    weekdays: [],
    createdAt: 1756065384,
  },
  offer: {
    id: 401,
    title: 'Datenbank-Update-Service',
    text: 'Ich kann die Aktualisierung bis Freitag erledigen.',
    active: true,
    validUntil: 1756065384,
    createdAt: 1756065384,
    taskId: 301,
    userId: 202,
    status: 'ACCEPTED',
    task: '',
  },
  createdAt: Date.now(),
  status: AssignmentStatus.IN_PROGRESS,
  active: true,
}
</script>

<template>
  <v-card
    variant="elevated"
    :style="{ border: `2px solid ${getColorOfCategory(mockAssignment.task.category)}` }"
    elevation="5"
  >
    <template v-slot:title>
      <div class="d-flex justify-space-between align-center">
        <span>{{ mockAssignment.task.title }}</span>
        <v-icon class="mr-2" small>{{ getIconOfCategory(mockAssignment.task.category) }}</v-icon>
      </div>
    </template>
    <template v-slot:text>
      <h3>Assignment</h3>
      <div><strong>Status:</strong> {{ mockAssignment.status }}</div>
      <div>
        <strong>Zugewiesen am:</strong>
        {{ new Date(mockAssignment.createdAt).toLocaleDateString() }}
      </div>
      <div>
        <strong>Zu erledigen bis:</strong> {{ new Date(mockAssignment.createdAt).toDateString() }}
      </div>
      <v-divider class="my-2"></v-divider>

      <h3 class="mt-4">Task</h3>
      <div>
        <strong>Erstellt von:</strong> {{ mockAssignment.taskCreatorUser.name }} ({{
          mockAssignment.taskCreatorUser.email
        }})
      </div>
      <div><strong>Titel:</strong> {{ mockAssignment.task.title }}</div>
      <div><strong>Beschreibung:</strong> {{ mockAssignment.task.description }}</div>
      <div><strong>Kategorie:</strong> {{ categoryMap.get(mockAssignment.task.category) }}</div>
      <div><strong>PLZ:</strong> {{ mockAssignment.task.zipCode }}</div>
      <div><strong>Koordinaten:</strong> {{ mockAssignment.task.coordinates }}</div>
      <div><strong>Status:</strong> {{ mockAssignment.task.status }}</div>
      <div><strong>Intervall:</strong> {{ intervalMap.get(mockAssignment.task.taskInterval) }}</div>
      <div>
        <strong>Erstellt am:</strong>
        {{ new Date(mockAssignment.task.createdAt * 1000).toLocaleDateString() }}
      </div>
      <div>
        <strong>Zu erledigen bis:</strong>
        {{
          mockAssignment.task.deadline
            ? new Date(mockAssignment.task.deadline * 1000).toLocaleDateString()
            : 'nicht angegeben'
        }}
      </div>
      <div v-if="mockAssignment.task.taskInterval == TaskInterval.RECURRING">
        <strong>Wochentage:</strong>
        <v-chip-group multiple column>
          <v-chip
            v-for="day in Weekday"
            v-bind:class="{ 'day-active': mockAssignment.task.weekdays.includes(day) }"
            :disabled="true"
            :text="weekdayMap.get(day)"
            :key="day"
          ></v-chip>
        </v-chip-group>
      </div>
      <v-divider class="my-2"></v-divider>

      <h3 class="mt-4">Offer</h3>
      <div><strong>Titel:</strong> {{ mockAssignment.offer.title }}</div>
      <div><strong>Text:</strong> {{ mockAssignment.offer.text }}</div>
      <div>
        <strong>Erstellt von:</strong> {{ mockAssignment.offerCreatorUser.name }} ({{
          mockAssignment.offerCreatorUser.email
        }})
      </div>
      <div><strong>Status:</strong> {{ mockAssignment.offer.status }}</div>
      <div>
        <strong>Erstellt am:</strong>
        {{ new Date(mockAssignment.offer.createdAt * 1000).toLocaleDateString() }}
      </div>
    </template>
  </v-card>
</template>

<style scoped></style>
