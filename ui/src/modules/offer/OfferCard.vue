<script setup lang="ts">
import { computed, ref } from 'vue'
import OfferSubmissionForm from "@/modules/offer/OfferSubmissionForm.vue";
import apiService from "@/service/apiService.ts";
import {translate} from "@/service/translationService.ts";
interface Task {
  id: number
  title: string
  description: string
  imageUrl?: string
}
interface Offer {
  id: number
  title: string
  text: string
  status: 'SUBMITTED' | 'ACCEPTED' | 'REJECTED'
  userId: number
  task: Task
}

const props = defineProps<{
  offer: Offer
  isOwnOffer: boolean
}>()

const emit = defineEmits(['delete', 'accept', 'reject'])
const t = translate
const showDialog = ref(false)

const statusColor = computed(() => {
  switch (props.offer.status) {
    case 'ACCEPTED': return 'green'
    case 'REJECTED': return 'red'
    default: return 'grey'
  }
})

function submitOffer(offer:any) {
  apiService.submitOffer(offer)
    .then(() => {
    })
    .catch((e) => console.error(e))
}

const defaultImage = 'https://images.pexels.com/photos/5428830/pexels-photo-5428830.jpeg'
</script>

<template>
  <v-card class="ma-2" elevation="4" max-width="320" @click="showDialog = true">
    <v-img :src="offer.task.imageUrl || defaultImage" height="180" cover>
      <v-card-title class="text-white bg-grey-darken-3 text-h6">
        {{ offer.task.title }}
      </v-card-title>
    </v-img>

    <v-card-text>
      <div class="mb-2">{{ offer.task.description }}</div>
      <div class="text-subtitle-1 font-weight-bold">{{ offer.title }}</div>
      <div class="text-body-2">{{ offer.text }}</div>
    </v-card-text>

    <v-card-actions class="justify-space-between">
      <v-chip :color="statusColor" small v-if="isOwnOffer">
        {{ t(offer.status) }}
      </v-chip>

      <div v-else>
        <v-btn icon color="green" @click.stop="$emit('accept', offer.id)">
          <v-icon>check</v-icon>
        </v-btn>
        <v-btn icon color="red" @click.stop="$emit('reject', offer.id)">
          <v-icon>close</v-icon>
        </v-btn>
      </div>

      <v-btn icon color="error" v-if="isOwnOffer && offer.status === 'SUBMITTED'" @click.stop="$emit('delete', offer.id)">
        <v-icon>delete</v-icon>
      </v-btn>
    </v-card-actions>
  </v-card>

  <OfferSubmissionForm
    v-model="showDialog"
    :task="offer.task"
    :offer="offer"
    :readonly="true"
  />
</template>
