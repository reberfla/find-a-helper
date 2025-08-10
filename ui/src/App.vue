<template>
  <v-app>
    <Header />
    <v-main>
      <v-container fluid>
        <router-view :key="route.path"></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import Header from '@/components/header/Header.vue'
</script>
