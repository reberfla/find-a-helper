<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import UserProfilView from '@/modules/user/UserProfilView.vue'
import BackHeader from '@/components/header/BackHeader.vue'
import router from '@/router'
import { useAuth } from '@/service/userAuthService.ts'

const tab = ref('data')
const hasNewMessages = ref(false)
const { isLoggedIn } = useAuth()

const menuItems = [
  { value: 'data', label: 'Profildaten' },
  { value: 'messages', label: 'Nachrichten' },
]

const currentComponent = computed(() => {
  switch (tab.value) {
    case 'data':
      return UserProfilView
    case 'messages':
      return UserProfilView
    default:
      return null
  }
})

onMounted(async () => {
  const response = await fetch('/api/chat/has-new')
  hasNewMessages.value = await response.json()
})

watch(
  isLoggedIn,
  (val) => {
    if (!val) router.push('/')
  },
  { immediate: true },
)
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
            <v-list-item-title>{{ item.label }}</v-list-item-title>
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
