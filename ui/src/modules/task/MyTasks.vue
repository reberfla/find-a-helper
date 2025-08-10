<script setup lang="ts">
import { provide, ref } from 'vue'
import { ViewFactoryToken } from '@/core/factory/view-factory.token'
import List from '@/components/List.vue'
import type { Task } from '@/models/TaskModel'
import {TaskFactory} from "@/core/factory/TaskFactory.ts";
import apiService from "@/service/apiService.ts";
import SnackBar from "@/components/Snackbar.vue";
import {translate} from "@/service/translationService.ts";

provide(ViewFactoryToken, new TaskFactory())

const selectedTask = ref<Task | null>(null)
const snackBar = ref<InstanceType<typeof SnackBar> | null>(null)
const t = translate

function onOpen(task: Task) {
  console.log('open')
  selectedTask.value = task
}
async function onDelete(id: number) {
  await apiService.deleteOffer(id)
}

function onSubmitted(task:Task){
  console.log(task)
  snackBar.value?.show(t('ERROR_OFFER_EXISTS'))
  onOpen(task)
}

</script>

<template>
  <List :context="{mine:true}" @action="onSubmitted"  @open="onOpen" @delete="onDelete"/>
  <SnackBar ref="snackBar" />
</template>
