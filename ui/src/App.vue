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
import Header from "@/components/header/Header.vue";
import Footer from "@/components/footer/Footer.vue";
</script>
