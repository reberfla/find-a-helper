<template>
  <v-app>
    <Header />
    <v-main>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import Header from '@/components/header/Header.vue'
const route = useRoute()
</script>
