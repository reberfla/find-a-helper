<template>
  <v-app>
    <Header />

    <v-main>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-main>

    <Footer />
  </v-app>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import Header from '@/components/header/Header.vue'
import Footer from '@/components/footer/Footer.vue'
const route = useRoute()
</script>
