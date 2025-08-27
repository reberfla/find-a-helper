<script setup lang="ts">
import {computed, onMounted, ref, shallowRef} from 'vue'
import offerService from '@/service/OfferService.ts'
import {type Offer, OfferStatus} from '@/models/OfferModel.ts'
import OfferCard from '@/components/offer/OfferCard.vue'
import OfferEditDialog from '@/components/offer/OfferEditDialog.vue'
import taskService from "@/service/TaskService.ts";
import {type Task} from "@/models/TaskModel.ts";

const offers = ref<Offer[]>([])
const selectedOffer = ref<Offer | null>(null)
const showOfferDialog = shallowRef(false)
const tasks = ref<Task[]>([])

async function loadMyOffers() {
  offers.value = await offerService.getMyOffers().then((r)=>{
    return r.filter((o=>o.status !== OfferStatus.ACCEPTED))
  })
}
function getTaskById(taskId:number | null):Task {
  console.log(tasks.value.filter((t=>t.id== taskId))[0])
    return  tasks.value.filter(t => t.id == taskId)[0]
}

async function loadTasks() {
  tasks.value = await taskService.getTasks()
}

function canEdit(o: Offer) {
  return o.active !== false && o.status === OfferStatus.SUBMITTED
}

function deleteOffer(id: number) {
  const o = offers.value.find(x => x.id === id)
  if (o && !canEdit(o)) return
  offerService.deleteOffer(id).then(() => {
    offers.value = offers.value.filter((x) => x.id !== id)
    if (selectedOffer.value?.id === id) selectedOffer.value = null
  })
}

onMounted(async () => {
  await Promise.all([loadTasks(), loadMyOffers()])
})
</script>

<template>
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
      :task="getTaskById(offer.taskId) ?? null"
      :private="true"
      :can-edit="false"
    @delete-offer="deleteOffer"
    @click="showOfferDialog = true"
    />
  </div>

  <v-dialog v-model="showOfferDialog" class="dialog">
    <OfferEditDialog :readonly="true" :offer="selectedOffer || undefined" @close="showOfferDialog = false" />
  </v-dialog>
</template>

<style scoped>
.offer { width: 300px; margin: 10px; }
.dialog { max-width: 1000px; width: 100%; }
</style>
