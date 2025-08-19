<script setup lang="ts">
import { computed } from 'vue'
import type {
  CardAdapter,
  CardAction,
  CardChip,
  CardLine,
} from '@/core/factory/view-factory.types.ts'

const props = defineProps<{ item: any; adapter: CardAdapter<any> }>()
const emit = defineEmits<{ (e: 'action', p: { name: string; id: number; item: any }): void }>()
const img = computed(() => props.adapter.getImage?.(props.item))
const title = computed(() => props.adapter.getTitle(props.item))
const subtitle = computed(() => props.adapter.getSubtitle?.(props.item))
const lines = computed<CardLine[]>(() => props.adapter.getLines?.(props.item) ?? [])
const chips = computed<CardChip[]>(() => props.adapter.getChips?.(props.item) ?? [])
const actions = computed<CardAction[]>(() => {
  const list = props.adapter.getActions?.(props.item) ?? []
  return list.filter(Boolean).map((a: any) => ({ visible: true, ...a }))
})
async function onAction(name: string) {
  console.log(name)
  emit('action', { name, id: props.adapter.getId(props.item), item: props.item })
}
</script>

<template>
  <v-card class="ma-2 d-flex flex-column" elevation="4" max-width="320" min-height="420">
    <v-img v-if="img" :src="img" height="180" cover>
      <v-card-title class="text-white bg-grey-darken-3 text-h6">{{ title }}</v-card-title>
      <v-card-subtitle class="text-white bg-grey-darken-3">{{ subtitle }}</v-card-subtitle>
    </v-img>
    <template v-else>
      <v-card-title class="text-h6">{{ title }}</v-card-title>
      <v-card-subtitle>{{ subtitle }}</v-card-subtitle>
    </template>

    <v-card-text class="flex-grow-1">
      <div v-for="(line, i) in lines" :key="i" class="mb-2">
        <strong v-if="line.label">{{ line.label }}: </strong>{{ line.value }}
      </div>
      <div class="d-flex flex-wrap gap-2 mt-2">
        <v-chip v-for="(c, i) in chips" :key="i" :color="c.color" small>{{ c.text }}</v-chip>
      </div>
    </v-card-text>

    <v-card-actions class="justify-end">
      <template v-for="(a, i) in actions" :key="i">
        <v-btn v-if="a && a.visible" icon :color="a.color" @click.stop="onAction(a.name)">
          <v-icon>{{ a.icon }}</v-icon>
        </v-btn>
      </template>
    </v-card-actions>
  </v-card>
</template>
