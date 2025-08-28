<script setup lang="ts">
import type { Task } from '@/models/TaskModel.ts'
import { onMounted, ref } from 'vue'
import { useAuth } from '@/service/userAuthService.ts'
import offerService from "@/service/OfferService.ts";
import type {Offer, OfferDto} from "@/models/OfferModel.ts";

const props = defineProps<{ task: Task }>()
const emit = defineEmits(['close-offer'])

const offer = ref<Offer>({
  taskId: props.task.id,
  userId:null,
  title: '',
  text: '',
})

onMounted(() => {
  const user = useAuth().getCurrentUserId()
  offer.value.userId = user
})

async function sendOffer() {
  offer.value.userId = useAuth().getCurrentUserId()
  const payload:OfferDto = {
    userId: offer.value.userId,
    taskId: offer.value.taskId,
    title: offer.value.title || null,
    text: offer.value.text
  }
  await offerService.createOffer(payload)
  emit("close-offer")
}

</script>
<template>
  <v-card>
    <template v-slot:title> Angebot für Aufgabe: {{ props.task.title }}</template>
    <template v-slot:text>
      <div><strong>Aufgabenbeschreibung: </strong>{{ task.description }}</div>
      <div class="mt-2">
        <v-text-field
          name="title"
          label="Titel"
          variant="outlined"
          v-model="offer.title"
        ></v-text-field>
        <v-textarea
          name="text"
          label="Beschreibung"
          variant="outlined"
          v-model="offer.text"
        ></v-textarea>
      </div>
    </template>
    <template v-slot:actions>
      <v-btn @click="sendOffer"> Angebot senden </v-btn>
      <v-btn @click="$emit('close-offer')"> Schliessen </v-btn>
    </template>
  </v-card>
</template>

<style scoped></style>
