<script setup lang="ts">
import {provide, ref} from 'vue'
import { OfferFactory } from '@/core/factory/OfferFactory'
import { ViewFactoryToken } from '@/core/factory/view-factory.token'
import List from '@/components/List.vue'
import apiService from '@/service/apiService'
import type {Task} from "@/models/TaskModel.ts";
import SubmissionForm from "@/components/SubmissionForm.vue";

provide(ViewFactoryToken, new OfferFactory())

const offerDialog = ref(false)
const offerConfig = ref()
const offerContext = ref(null)
const readonlyForm = ref(true)

async function onDelete(id: number) {
  await apiService.deleteOffer(id)
}

function openOffer(task: any) {
  console.log(offer)
  offerContext.value = { task }
  offerConfig.value = new OfferFactory().getFormConfig({
    taskId: task.id,
    task: task
  })
  readonlyForm.value = false
  offerDialog.value = true
}

</script>

<template>
  <List :context="{ mine: true }" @open="openOffer" @delete="onDelete" />

  <v-dialog v-model="offerDialog" max-width="600">
    <SubmissionForm
      v-if="offerConfig"
      v-model="offerDialog"
      :config="offerConfig"
      :context="offerContext"
      :readonly="readonlyForm"
    />
  </v-dialog>
</template>
