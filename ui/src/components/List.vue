<!-- src/components/common/List.vue -->
<script setup lang="ts">
import {computed, inject, onMounted, ref, watch} from 'vue'
import { ViewFactoryToken } from '@/core/factory/view-factory.token.ts'
import type { ViewFactory } from '@/core/factory/view-factory.types.ts'
import Card from './Card.vue'
import {useRoute, useRouter} from "vue-router";
import {translate} from "@/service/translationService.ts";
import {TaskCategory} from "@/models/TaskModel.ts";

const props = withDefaults(defineProps<{ context?: any }>(), { context: undefined })

const factory = inject<ViewFactory<any>>(ViewFactoryToken)!
const items = ref<any[]>([])
const loading = ref(false)
const emptyText = ref(factory.getEmptyText())
const adapter = ref(factory.getAdapter(props.context))
const route = useRoute()
const router = useRouter()
const t = translate

const showFilters = computed(() => (factory as any)?.kind === 'task')

const q = ref<string>((route.query.q as string) ?? '')
const category = ref<string>((route.query.category as string) ?? '')
const zip = ref<string>((route.query.zip as string) ?? '')

const ctx = computed(() => ({
  ...(props.context ?? {}),
  q: q.value || undefined,
  category: category.value || undefined,
  zip: zip.value || undefined,
}))

async function load() {
  loading.value = true
  try {
    items.value = await factory.loadItems(ctx.value)
  }
  finally { loading.value = false }
}

onMounted(load)

const emit = defineEmits<{
  (e:'accept', id:number): void
  (e:'reject', id:number): void
  (e:'delete', id:number): void
  (e:'open', p:any): void
  (e:'addOffer', p:any): void
  (e:'submitted', p:any): void
}>()

function onAction(e:{name:string; id:number; item:any}) {
  if (e.name === 'open') emit('open', e.item)
  else if (e.name === 'accept') emit('accept', e.item)
  else if (e.name === 'reject') emit('reject', e.item)
  else if (e.name === 'delete') emit('delete', e.id)
  else if (e.name === 'addOffer') emit('addOffer', e.item)
  else if (e.name === 'submitted') emit('submitted', e.item)
}

function applyFilters() {
  const next = { ...route.query }
  q.value ? next.q = q.value.trim() : delete (next as any).q
  category.value ? next.category = category.value : delete (next as any).category
  zip.value ? next.zip = zip.value.trim() : delete (next as any).zip
  router.replace({ name: route.name as string, query: next })
  load()
}

const categoryOptions = [
  { value: '', text: t('ALL_CATEGORIES') },
  ...Object.keys(TaskCategory).map(k => ({ value: k, text: t(k) })),
]
</script>

<template>
  <v-container>
    <div v-if="showFilters" class="d-flex flex-column flex-md-row align-center ga-2 mb-4">
      <v-text-field
          v-model="q"
          :label="t('SEARCH')"
          density="comfortable"
          clearable
          hide-details
          @keyup.enter="applyFilters"
      />
      <v-select
          v-model="category"
          :items="categoryOptions"
          item-title="text"
          item-value="value"
          :label="t('CATEGORY')"
          density="comfortable"
          hide-details
          @update:modelValue="applyFilters"
      />
      <v-text-field
          v-model="zip"
          :label="t('ZIP_CODE')"
          density="comfortable"
          clearable
          hide-details
          inputmode="numeric"
          @keyup.enter="applyFilters"
      />
      <v-btn class="ml-md-2" variant="flat" color="primary" @click="applyFilters">
        {{ t('FILTER') }}
      </v-btn>
    </div>
    <v-progress-linear v-if="loading" indeterminate class="mb-4" />
    <v-row dense>
      <v-col v-for="it in items" :key="adapter.getId(it)" cols="12" sm="6" md="4" lg="3" xl="3">
        <Card :item="it" :adapter="adapter" @action="onAction" />
      </v-col>
    </v-row>
    <v-alert v-if="!loading && items.length === 0" type="info">{{ emptyText }}</v-alert>
  </v-container>
</template>
