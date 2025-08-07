<script setup lang="ts">
import { ref, onMounted } from 'vue'
import apiService from '@/service/apiService'
import OfferCard from './OfferCard.vue'
import {translate} from "@/service/translationService.ts";

const props = withDefaults(defineProps<{
  myOffers?: boolean
  taskId?: number
}>(), {
  myOffers: true
})

const t = translate

const offers:any = ref<any[]>([])

async function loadOffers() {
  try {
    if (props.myOffers) {
      offers.value = await apiService.getMyOffers()
    } else if (props.taskId) {
      offers.value = await apiService.getOffersForTask(props.taskId)
    }
  } catch (e) {
    console.error(e)
  }
}



async function deleteOffer(id: number) {
  try {
    await apiService.deleteOffer(id)
    offers.value = offers.value.filter((o:any) => o.id !== id)
  } catch (e) {
    console.error(e)
  }
}

async function acceptOffer(id: number) {
  try {
    await apiService.acceptOffer(id)
    await loadOffers()
  } catch (e) {
    console.error(e)
  }
}

async function rejectOffer(id: number) {
  try {
    await apiService.rejectOffer(id)
    await loadOffers()
  } catch (e) {
    console.error(e)
  }
}

onMounted(loadOffers)
</script>

<template>
  <v-container>
    <h3 class="mb-4">
      {{ t('MY_OFFERS') }}
    </h3>

    <v-row dense>
      <v-col
          v-for="offer in offers"
          :key="offer.id"
          cols="12"
          sm="6"
          md="4"
          lg="3"
          xl="3"
      >
        <OfferCard
            :offer="offer"
            :isOwnOffer="myOffers"
            @delete="deleteOffer"
            @accept="acceptOffer"
            @reject="rejectOffer"
        />
      </v-col>
    </v-row>

    <v-alert v-if="offers.length === 0" type="info">
      {{ t('NO_OFFERS_FOUND') }}
    </v-alert>
  </v-container>
</template>



