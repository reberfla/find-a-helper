<script setup lang="ts">
import { provide, ref } from 'vue'
import { ViewFactoryToken } from '@/core/factory/view-factory.token'
import List from '@/components/List.vue'
import type { Task } from '@/models/TaskModel'
import {TaskFactory} from "@/core/factory/TaskFactory.ts";
import apiService from "@/service/apiService.ts";

provide(ViewFactoryToken, new TaskFactory())

const selectedTask = ref<Task | null>(null)
function onOpen(task: Task) {
  selectedTask.value = task
}
async function onDelete(id: number) {
  await apiService.deleteOffer(id)
}
</script>

<template>
  <List :context="{mine:true}" @open="onOpen" @delete="onDelete" />
</template>
