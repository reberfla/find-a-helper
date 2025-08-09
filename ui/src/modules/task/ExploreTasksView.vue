<script setup lang="ts">
import { ref, provide } from 'vue'
import { ViewFactoryToken } from '@/core/factory/view-factory.token'
import { TaskFactory } from '@/core/factory/TaskFactory'
import List from '@/components/List.vue'
import SubmissionForm from "@/components/SubmissionForm.vue";
import {OfferFactory} from "@/core/factory/OfferFactory.ts";

const offerDialog = ref(false)
const offerConfig = ref()
const offerContext = ref(null)
const readonlyForm = ref(true)
const taskDialog = ref(false)
const taskConfig = ref()
const taskContext = ref(null)

provide(ViewFactoryToken, new TaskFactory())

function openTask(task: any) {
  taskContext.value = task
  taskConfig.value = new TaskFactory().getFormConfig(task)
  readonlyForm.value = true
  taskDialog.value = true
}

function newOffer(task: any) {
  offerContext.value = { task }
  offerConfig.value = new OfferFactory().getFormConfig({
    taskId: task.id,
    task: task
  })
  readonlyForm.value = false
  offerDialog.value = true
}

function handleTaskChanges(task: any) {
  //todo implement update/add-logik
}

function handleAddOffer(offer: any) {
  //todo implement add-logik
}

</script>

<template>
  <List @addOffer="newOffer" @open="openTask" />

  <v-dialog v-model="taskDialog" max-width="600">
    <SubmissionForm
      v-if="taskConfig"
      v-model="taskDialog"
      :config="taskConfig"
      :context="taskContext"
      :readonly="readonlyForm"
      @save="handleTaskChanges"
    />
  </v-dialog>

  <v-dialog v-model="offerDialog" max-width="600">
    <SubmissionForm
      v-if="offerConfig"
      v-model="offerDialog"
      :config="offerConfig"
      :context="offerContext"
      :readonly="readonlyForm"
      @save="handleAddOffer"
    />
  </v-dialog>

</template>
