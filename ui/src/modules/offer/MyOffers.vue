<script setup lang="ts">
import { provide, ref } from 'vue'
import { OfferFactory } from '@/core/factory/OfferFactory'
import { ViewFactoryToken } from '@/core/factory/view-factory.token'
import List from '@/components/List.vue'
import apiService from '@/service/apiService'
import SubmissionForm from '@/components/SubmissionForm.vue'

provide(ViewFactoryToken, new OfferFactory())

const offerDialog = ref(false)
const offerConfig = ref()
const offerContext = ref(null)

async function onDelete(id: number) {
  await apiService.deleteOffer(id)
}

function openOffer(offer: any) {
  offerContext.value = offer
  offerConfig.value = new OfferFactory().getFormConfig(offer)
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
      :readonly="true"
    />
  </v-dialog>
</template>
