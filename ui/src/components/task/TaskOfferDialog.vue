<script setup lang="ts">
import type { Task } from '@/models/TaskModel.ts'
import { onMounted, ref } from 'vue'
import { useAuth } from '@/service/userAuthService.ts'
import { translate } from '@/service/translationService.ts'

const t = translate

const props = defineProps<{ task: Task }>()

const offer = ref<any>({
  taskId: props.task.id,
  userId: undefined,
  title: '',
  text: '',
})

onMounted(() => {
  offer.value.userId = useAuth().getCurrentUserId()
})

const emit = defineEmits(['close-offer', 'send-offer'])
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
      <v-btn @click="$emit('send-offer', offer.value)"> Angebot senden </v-btn>
      <v-btn @click="$emit('close-offer')"> Schliessen </v-btn>
    </template>
  </v-card>
</template>

<style scoped></style>
