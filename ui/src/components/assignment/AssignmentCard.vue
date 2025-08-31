<script setup lang="ts">
import { computed } from 'vue'
import { getIconOfCategory, getColorOfCategory } from '@/models/TaskModel'
import { intervalMap, type Task, TaskInterval, Weekday, weekdayMap } from '@/models/TaskModel'
import type { AssignmentModel } from '@/models/AssignmentModel'

const props = defineProps<{
  assignment: AssignmentModel
  canEdit?: boolean
  private?: boolean
}>()

const task = computed(() => props.assignment?.task as Task | undefined)
const offer = computed(() => props.assignment?.offer)

const boardColor = computed(() => (task.value ? getColorOfCategory(task.value.category) : '#ddd'))
const headerIcon = computed(() =>
  task.value ? getIconOfCategory(task.value.category) : 'description',
)

function toDateStringSec(ts?: number | null) {
  if (!ts) return '—'
  try {
    return new Date(ts * 1000).toLocaleDateString()
  } catch {
    return '—'
  }
}
</script>

<template>
  <v-card
    variant="elevated"
    :style="{ border: `2px solid ${boardColor}` }"
    elevation="5"
    class="offer-card"
  >
    <template #title>
      <div class="d-flex justify-space-between align-center">
        <div class="d-flex align-center">
          <v-icon class="mr-2" small>{{ headerIcon }}</v-icon>
          <span class="font-weight-medium">
            {{ `Vertrag für ${task?.title ?? 'Auftrag'}` }}
          </span>
        </div>
      </div>
    </template>

    <div class="chip">
      <v-chip style="margin-bottom: 6px" size="small" variant="tonal" label>
        <strong class="mr-1">Zu erledigen bis:</strong>
        {{ toDateStringSec(task?.deadline) ?? 'nicht angegeben' }}
      </v-chip>

      <v-chip size="small" variant="tonal" label>
        <strong class="mr-1">Wiederholung:</strong>
        {{ task?.taskInterval ? intervalMap.get(task.taskInterval) : 'Nicht angegeben' }}
      </v-chip>
    </div>

    <div
      style="margin-left: 16px"
      v-if="task?.taskInterval === TaskInterval.RECURRING"
      class="mt-2"
    >
      <strong>Wochentage:</strong>
      <v-chip-group multiple column class="mt-1">
        <v-chip
          v-for="day in Weekday"
          :key="day"
          :text="weekdayMap.get(day)"
          :class="{ 'day-active': task?.weekdays?.includes(day) }"
          :disabled="true"
          size="small"
          variant="tonal"
        />
      </v-chip-group>
    </div>

    <v-divider class="my-3" />

    <v-expansion-panels multiple>
      <v-expansion-panel>
        <v-expansion-panel-title>
          <div class="d-flex align-center">
            <v-icon class="mr-2">{{ headerIcon }}</v-icon>
            Details über Auftrag
          </div>
        </v-expansion-panel-title>
        <v-expansion-panel-text>
          <div v-if="task">
            <div><strong>Beschreibung: </strong>{{ task.description }}</div>
            <div><strong>PLZ: </strong>{{ task.zipCode }}</div>
            <div><strong>Koordinaten: </strong>{{ task.coordinates }}</div>
            <div><strong>Erstellt am: </strong>{{ toDateStringSec(task.createdAt) }}</div>
          </div>
          <div v-else class="text-medium-emphasis">
            Zugehöriger Auftrag-Datensatz ist nicht geladen.
          </div>
        </v-expansion-panel-text>
      </v-expansion-panel>

      <v-expansion-panel>
        <v-expansion-panel-title>
          <div class="d-flex align-center">
            <v-icon class="mr-2">request_quote</v-icon>
            Mein Angebot
          </div>
        </v-expansion-panel-title>
        <v-expansion-panel-text>
          <div v-if="offer">
            <div><strong>Titel: </strong>{{ offer.title }}</div>
            <div class="mb-2"><strong>Text: </strong>{{ offer.text }}</div>
          </div>
          <div v-else class="text-medium-emphasis">Angebot ist nicht geladen.</div>
        </v-expansion-panel-text>
      </v-expansion-panel>
    </v-expansion-panels>
  </v-card>
</template>

<style scoped>
.offer-card {
  overflow: hidden;
  width: 100%;
}
.chip {
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  margin: 16px;
  justify-self: flex-start;
}
.day-active {
  background-color: #2e7d32;
  color: white;
}
</style>
