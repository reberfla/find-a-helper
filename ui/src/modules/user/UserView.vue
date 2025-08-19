<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { translate } from '@/service/translationService.js'
import UserProfilView from '@/modules/user/UserProfilView.vue'
import BackHeader from '@/components/header/BackHeader.vue'
import MyOffers from '@/modules/offer/MyOffers.vue'
import MyTasksWithOffers from '@/modules/task/MyTasksWithOffers.vue'

const t = translate
const tab = ref('data')
const hasNewMessages = ref(false)

const menuItems = [
  { value: 'data', label: 'PROFILE_DATA' },
  { value: 'messages', label: 'MESSAGES' },
  { value: 'offers', label: 'MY_OFFERS' },
  { value: 'orders', label: 'MY_ORDERS' },
]

const currentComponent = computed(() => {
  switch (tab.value) {
    case 'data':
      return UserProfilView
    case 'messages':
      return UserProfilView
    case 'offers':
      return MyOffers
    case 'tasks':
      return MyTasksWithOffers
    default:
      return null
  }
})

onMounted(async () => {
  const response = await fetch('/api/chat/has-new')
  hasNewMessages.value = await response.json()
})
</script>

<template>
  <v-container fluid>
    <BackHeader></BackHeader>

    <v-row>
      <v-col cols="12" md="3">
        <v-list nav>
          <v-list-item
            v-for="item in menuItems"
            :key="item.value"
            :value="item.value"
            :active="tab === item.value"
            @click="tab = item.value"
            class="cursor-pointer"
          >
            <v-list-item-title>{{ t(item.label) }}</v-list-item-title>
            <v-badge
              v-if="item.value === 'messages' && hasNewMessages"
              color="red"
              dot
              offset-x="10"
            />
          </v-list-item>
        </v-list>
      </v-col>

      <v-col cols="12" md="9">
        <component :is="currentComponent" />
      </v-col>
    </v-row>
  </v-container>
</template>
<style scoped></style>
