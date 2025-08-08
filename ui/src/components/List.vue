<!-- src/components/common/List.vue -->
<script setup lang="ts">
import { inject, onMounted, ref, watch } from 'vue'
import { ViewFactoryToken } from '@/core/factory/view-factory.token.ts'
import type { ViewFactory } from '@/core/factory/view-factory.types.ts'
import Card from './Card.vue'

const props = withDefaults(defineProps<{ context?: any }>(), { context: undefined })

const factory = inject<ViewFactory<any>>(ViewFactoryToken)!
const items = ref<any[]>([])
const loading = ref(false)
const title = ref(factory.getTitle(props.context))
const emptyText = ref(factory.getEmptyText())
const adapter = ref(factory.getAdapter(props.context))

async function load() {
  loading.value = true
  try { items.value = await factory.loadItems(props.context) }
  finally { loading.value = false }
}

watch(() => props.context, () => {
  title.value = factory.getTitle(props.context)
  emptyText.value = factory.getEmptyText()
  adapter.value = factory.getAdapter(props.context)
  load()
}, { deep: true })

onMounted(load)

const emit = defineEmits<{ (e:'accept',id:number):void; (e:'reject',id:number):void; (e:'delete',id:number):void; (e:'open',p:any):void }>()
function onAction(e:{name:string; id:number; item:any}) {
  if (e.name === 'open') emit('open', e.item)
  else if (e.name === 'accept') emit('accept', e.id)
  else if (e.name === 'reject') emit('reject', e.id)
  else if (e.name === 'delete') emit('delete', e.id)
}
</script>

<template>
  <v-container>
    <h3 class="mb-4">{{ title }}</h3>
    <v-progress-linear v-if="loading" indeterminate class="mb-4" />
    <v-row dense>
      <v-col v-for="it in items" :key="adapter.getId(it)" cols="12" sm="6" md="4" lg="3" xl="3">
        <Card :item="it" :adapter="adapter" @action="onAction" />
      </v-col>
    </v-row>
    <v-alert v-if="!loading && items.length === 0" type="info">{{ emptyText }}</v-alert>
  </v-container>
</template>
