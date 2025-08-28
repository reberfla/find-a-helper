<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type {Offer, OfferDto} from '@/models/OfferModel.ts'
import offerService from '@/service/OfferService.ts'
import router from '@/router'
import {useAuth} from "@/service/userAuthService.ts";

const valid = ref(false)
const required = (value: any) => (!!value ? true : 'Dieses Feld ist erforderlich')
const editOffer = ref<Partial<Offer>>({})
const date = ref<Date | null>(null)

const toUpdateOffer = computed<OfferDto>(() => ({
  userId: Number(useAuth().getCurrentUserId()),
  taskId: Number(editOffer.value.taskId!),
  title: editOffer.value.title ?? null,
  text: String(editOffer.value.text ?? ''),
  validUntil: toYMD(date.value)
}))

const props = defineProps<{
  offer?: Offer
  readonly : boolean
}>()
const isRO = computed(() => props.readonly)
const rulesOrEmpty = computed(() => (isRO.value ? [] : [required]))

function toDateFromYMD(s?: string | null): Date | null {
  if (!s) return null
  const d = new Date(`${s}T00:00:00`)
  return isNaN(+d) ? null : d
}
function toYMD(d?: Date | null): string | null {
  if (!d) return null
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

async function createOffer() {
  if (!valid.value) return
  if (!editOffer.value.taskId) return
  await offerService.createOffer(toUpdateOffer.value)
  emit('close')
  router.push({ name: 'my-offers' })
}

onMounted(() => {
  if (props.offer) {
    editOffer.value = { ...props.offer }
    date.value = toDateFromYMD(props.offer.validUntil ?? null)
  } else {
    editOffer.value = { active: true }
    date.value = null
  }
})


const emit = defineEmits(['close'])
</script>

<template>
  <v-card>
    <template #title>
      <span v-if="isRO">Angebot ansehen</span>
      <span v-else-if="props.offer">Angebot bearbeiten</span>
      <span v-else>Angebot erstellen</span>
    </template>

    <template #text>
      <v-form
        v-model="valid"
        class="d-flex flex-column gap-space"
        @submit.prevent="createOffer()"
      >
        <v-text-field
          density="compact"
          name="title"
          label="Titel*"
          :rules="rulesOrEmpty"
          v-model="editOffer.title"
          variant="outlined"
          :readonly="isRO"
          :disabled="isRO"
        />

        <v-textarea
          name="text"
          label="Text*"
          :rules="rulesOrEmpty"
          v-model="editOffer.text"
          variant="outlined"
          :readonly="isRO"
          :disabled="isRO"
          auto-grow
        />

        <v-date-input
          name="validUntil"
          label="Gültig bis"
          v-model="date"
          density="compact"
          variant="outlined"
          :readonly="isRO"
          :disabled="isRO"
          :clearable="!isRO"
          @click:clear="() => { date = null }"
        />

        <p v-if="!isRO">*erforderlich</p>

        <div class="d-flex justify-end mt-4">
          <v-btn v-if="!isRO" type="submit" color="success" class="mr-4">Speichern</v-btn>
          <v-btn @click="$emit('close')" :color="isRO ? 'primary' : 'error'">
            {{ isRO ? 'Schließen' : 'Abbrechen' }}
          </v-btn>
        </div>
      </v-form>
    </template>
    <template #actions></template>
  </v-card>
</template>

<style scoped>
.gap-space {
  row-gap: 8px;
}
</style>
