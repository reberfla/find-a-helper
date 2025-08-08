<script setup lang="ts">
import { useRouter } from 'vue-router'
const router = useRouter()

function goHome() {
  router.push('/')
}
</script>

<template>
  <v-footer app color="#2a403D" dark>
    <v-container class="d-flex flex-column flex-sm-row align-center justify-space-between py-4">
      <div class="d-flex align-center mb-3 mb-sm-0" style="cursor: pointer" @click="goHome">
        <v-icon class="mr-2">home</v-icon>
        <span class="text-h6 font-weight-bold">Find A Helper</span>
      </div>

      <!-- Kontaktinfos -->
      <div class="d-flex flex-column flex-sm-row align-center">
        <v-list-item class="px-0">
          <v-icon class="mr-2">email</v-icon>
          <span>info@findahelper.ch</span>
        </v-list-item>

        <v-list-item class="px-0 ml-sm-6">
          <v-icon class="mr-2">map_marker</v-icon>
          <span>Sesamstraße 187, 3141 Entenhausen</span>
        </v-list-item>

        <v-list-item class="px-0 ml-sm-6">
          <v-icon class="mr-2">phone</v-icon>
          <span>+41 011 222 33 44</span>
        </v-list-item>
      </div>
    </v-container>
  </v-footer>
</template>

<style scoped>
.v-footer {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}
</style>
