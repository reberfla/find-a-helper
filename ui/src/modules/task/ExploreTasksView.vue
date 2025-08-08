<script setup lang="ts">
import { ref, provide } from 'vue'
import { ViewFactoryToken } from '@/core/factory/view-factory.token'
import { TaskFactory } from '@/core/factory/TaskFactory'
import List from '@/components/List.vue'
import OfferSubmissionForm from '@/modules/offer/OfferSubmissionForm.vue'

const offerDialog = ref(false)
const selectedTask = ref(null)

provide(ViewFactoryToken, new TaskFactory())

function newOffer(task:any) {
  selectedTask.value = task
  offerDialog.value = true
}
</script>

<template>
  <List @open="newOffer" />

  <v-dialog v-model="offerDialog" max-width="600">
    <OfferSubmissionForm
      v-model="offerDialog"
      :readonly="false"
      :task="selectedTask"
    />
  </v-dialog>
</template>
