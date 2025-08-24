<script setup lang="ts">
import { onMounted, ref, shallowRef } from 'vue'
import { useRouter } from 'vue-router'
import offerService from '@/service/OfferService.ts'
import type { Offer } from '@/models/OfferModel.ts'
import OfferCard from '@/components/offer/OfferCard.vue'
import OfferEditDialog from '@/components/offer/OfferEditDialog.vue'

const offers = ref<Offer[]>([])
const selectedOffer = ref<Offer | null>(null)
const editDialog = shallowRef(false)
const router = useRouter()

async function loadMyOffers() {
  offers.value = await offerService.getMyOffers()
}

function canEdit(o: Offer) {
  return o.active !== false && o.status === 'SUBMITTED' // nur solange noch eingereicht
}

function editOffer(o: Offer) {
  if (!canEdit(o)) return
  selectedOffer.value = o
  editDialog.value = true
}

function deleteOffer(id: number) {
  const o = offers.value.find(x => x.id === id)
  if (o && !canEdit(o)) return
  offerService.deleteOffer(id).then(() => {
    offers.value = offers.value.filter((x) => x.id !== id)
    if (selectedOffer.value?.id === id) selectedOffer.value = null
  })
}

// Auf Task klicken -> Task-Detail öffnen
function openTask(o: Offer) {
  router.push({ name: 'task-detail', params: { id: o.taskId } })
}

onMounted(loadMyOffers)
</script>

<template>
  <v-dialog v-model="editDialog" class="dialog">
    <OfferEditDialog :offer="selectedOffer || undefined" @close="editDialog = false" :update="true" />
  </v-dialog>

  <div v-if="offers.length === 0" class="no-offers-banner">
    <v-alert type="info" color="blue">
      Sie haben noch kein Angebot abgegeben.
    </v-alert>
  </div>

  <div class="d-flex flex-wrap justify-space-evenly">
    <OfferCard
      v-for="offer in offers"
      :key="offer.id"
      class="offer"
      :offer="offer"
      :private="true"
      :can-edit="canEdit(offer)"
    @edit-offer="editOffer"
    @delete-offer="deleteOffer"
    @open-task="openTask"
    />
  </div>
</template>

<style scoped>
.offer { width: 300px; margin: 10px; }
.dialog { max-width: 1000px; width: 100%; }
</style>
