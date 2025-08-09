<script setup lang="ts">
import { provide } from 'vue'
import { ViewFactoryToken } from '@/core/factory/view-factory.token'
import { OfferFactory } from '@/core/factory/OfferFactory'
import List from '@/components/List.vue'
import apiService from '@/service/apiService'

const props = defineProps<{ taskId: number }>()
const emit = defineEmits<{ (e:'changed'): void }>()

provide(ViewFactoryToken, new OfferFactory())

async function onAccept(id: number) {
  await apiService.acceptOffer(id)
  emit('changed')
}
async function onReject(id: number) {
  await apiService.rejectOffer(id)
  emit('changed')
}
</script>

<template>
  <List :context="{ taskId: props.taskId }"
        @accept="onAccept"
        @reject="onReject" />
</template>
