
<script setup lang="ts">
import { computed } from 'vue'
import type { Offer } from '@/models/OfferModel'

const props = defineProps<{
  offer: Offer
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

// Status-Label & -Farbe
const statusLabel = computed(() => {
  switch (props.offer.status) {
    case 'ACCEPTED': return 'Angenommen'
    case 'REJECTED': return 'Abgelehnt'
    default: return 'Eingereicht'
  }
})
const statusColor = computed(() => {
  switch (props.offer.status) {
    case 'ACCEPTED': return 'green'
    case 'REJECTED': return 'red'
    default: return 'blue'
  }
})

const activeLabel = computed(() => props.offer.active ? 'Aktiv' : 'Inaktiv')
const activeColor = computed(() => props.offer.active ? 'teal' : 'grey')

const canDelete = computed(() => props.offer.status !== 'ACCEPTED')
</script>

<template>
  <v-card
    variant="elevated"
    class="offer-card"
    elevation="5"
    @click="emit('open-task', offer)"
  >
    <template #title>
      <div class="d-flex justify-space-between align-center">
        <span>{{ props.taskTitle ?? `Auftrag #${offer.taskId}` }}</span>
      </div>
    </template>

    <template #subtitle>
      <div class="d-flex align-center gap-2">
        <v-chip size="small" :color="statusColor" class="text-white" label>{{ statusLabel }}</v-chip>
        <v-chip size="small" :color="activeColor" class="text-white" label>{{ activeLabel }}</v-chip>
      </div>
    </template>

    <template #text>
      <div class="mb-2"><strong>Gültig bis:</strong> {{ validUntilText }}</div>
      <div class="mb-2"><strong>Text:</strong> {{ offer.text }}</div>
      <div v-if="offer.title"><strong>Titel:</strong> {{ offer.title }}</div>
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
.offer-card { position: relative; }
.position-bottom {
  position: absolute;
  bottom: 0; left: 0; right: 0;
  z-index: 1;
}
.gap-2 { gap: 8px; }
</style>
