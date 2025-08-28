<script setup lang="ts">
import { computed } from 'vue'
import type { Offer } from '@/models/OfferModel'
import { type Task, intervalMap, getIconOfCategory, getColorOfCategory } from '@/models/TaskModel'

const props = defineProps<{
  offer: Offer
  task?: Task | null
  taskTitle?: string
  private?: boolean
}>()

const emit = defineEmits<{
  (e: 'open-task', offer: Offer): void
  (e: 'delete-offer', id: number): void
}>()

const validUntilText = computed(() => {
  const s = props.offer.validUntil
  if (!s) return 'nicht angegeben'
  const d = new Date(`${s}T00:00:00`)
  return isNaN(+d) ? s : d.toLocaleDateString()
})

enum OfferStatusLocal {
  SUBMITTED = 'SUBMITTED',
  REJECTED = 'REJECTED',
  ACCEPTED = 'ACCEPTED',
}
const statusLabel = computed(() => {
  switch (props.offer.status) {
    case OfferStatusLocal.ACCEPTED:
      return 'Angenommen'
    case OfferStatusLocal.REJECTED:
      return 'Abgelehnt'
    default:
      return 'Eingereicht'
  }
})
const statusColor = computed(() => {
  switch (props.offer.status) {
    case OfferStatusLocal.ACCEPTED:
      return 'green'
    case OfferStatusLocal.REJECTED:
      return 'red'
    default:
      return 'blue'
  }
})
const activeLabel = computed(() => (props.offer.active ? 'Aktiv' : 'Inaktiv'))
const activeColor = computed(() => (props.offer.active ? 'teal' : 'grey'))
const canDelete = computed(() => props.offer.status !== OfferStatusLocal.ACCEPTED)

const taskData = computed<Task | null>(() => props.task ?? null)
const borderColor = computed(() =>
  taskData.value ? getColorOfCategory(taskData.value.category) : '#ddd',
)
const headerIcon = computed(() =>
  taskData.value ? getIconOfCategory(taskData.value.category) : 'description',
)
const cardTitle = computed(() => props.offer.taskId)
const deadlineText = computed(() => {
  const dl = taskData.value?.deadline
  return dl ? new Date(dl * 1000).toLocaleDateString() : 'nicht angegeben'
})
</script>

<template>
  <v-card
    variant="elevated"
    class="offer-card"
    elevation="5"
    :style="{ border: `2px solid ${borderColor}` }"
    @click="emit('open-task', offer)"
  >
    <template #title>
      <div class="d-flex justify-space-between align-center">
        <span>{{ 'Angebot für ' + cardTitle }}</span>
        <v-icon class="mr-2" small>{{ headerIcon }}</v-icon>
      </div>
    </template>

    <template #subtitle>
      <div class="d-flex align-center gap-2">
        <v-chip size="small" :color="statusColor" class="text-white" label>{{
          statusLabel
        }}</v-chip>
        <v-chip size="small" :color="activeColor" class="text-white" label>{{
          activeLabel
        }}</v-chip>
      </div>
    </template>

    <template #text>
      <div class="mb-2"><strong>Gültig bis:</strong> {{ validUntilText }}</div>
      <div class="mb-2"><strong>Angebot text:</strong> {{ offer.text }}</div>
      <div v-if="offer.title"><strong>Angebot titel:</strong> {{ offer.title }}</div>

      <v-divider class="my-3" />

      <template v-if="taskData">
        <div class="mb-2"><strong>Auftragstitel:</strong> {{ taskData.title }}</div>
        <div class="mb-2"><strong>Beschreibung:</strong> {{ taskData.description }}</div>

        <div class="d-flex flex-wrap align-center" style="gap: 8px; margin: 8px 0">
          <v-chip size="small" variant="tonal" label>
            <strong class="mr-1">Zu erledigen bis:</strong>
            {{ deadlineText }}
          </v-chip>

          <v-chip size="small" variant="tonal" label>
            <strong class="mr-1">Wiederholung:</strong>
            {{ taskData.taskInterval ? intervalMap.get(taskData.taskInterval) : 'Nicht angegeben' }}
          </v-chip>
        </div>
      </template>

      <template v-else>
        <div class="text-medium-emphasis">
          Zugehöriger Auftrag wurde nicht übergeben (Task-ID: {{ offer.taskId ?? '—' }}).
        </div>
      </template>

      <div class="mb-8"></div>
    </template>

    <v-card-actions class="justify-center position-bottom" @click.stop>
      <v-btn
        variant="elevated"
        class="mx-1"
        color="red-accent-1"
        :disabled="!canDelete || !offer.id"
        @click.stop="emit('delete-offer', offer.id!)"
      >
        Löschen
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<style scoped>
.offer-card {
  position: relative;
}
.position-bottom {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1;
}
.gap-2 {
  gap: 8px;
}
</style>
