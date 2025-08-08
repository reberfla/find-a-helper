<script setup lang="ts">
import {provide, ref} from 'vue'
import { OfferFactory } from '@/core/factory/OfferFactory'
import { ViewFactoryToken } from '@/core/factory/view-factory.token'
import List from '@/components/List.vue'
import apiService from '@/service/apiService'
import type {Task} from "@/models/TaskModel.ts";

provide(ViewFactoryToken, new OfferFactory())

const selectedTask = ref<Task | null>(null)
function onOpen(task: Task) {
  selectedTask.value = task
}

async function onDelete(id: number) {
  await apiService.deleteOffer(id)
}
</script>

<template>
  <List :context="{ mine: true }" @open="onOpen" @delete="onDelete" />
</template>
