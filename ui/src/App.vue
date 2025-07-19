
<template>
  <v-app>
    <!-- App Bar -->
    <v-app-bar app color="#2a403D" dark>
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Find A Helper</v-toolbar-title>
      <v-spacer></v-spacer>
      <img src="/src/assets/hand-heart.svg" alt="fahLogo" width="60px" height="auto">
    </v-app-bar>

    <!-- Navigation Drawer -->
    <v-navigation-drawer v-model="drawer" app>
      <v-list>
        <v-list-item v-for="item in menuItems" :key="item.title" :to="item.path">
          <template v-slot:prepend>
            <v-icon>{{ item.icon }}</v-icon>
          </template>
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <!-- Main Content -->
    <v-main style="background-color: #C3CBD6;">
      <router-view></router-view>
    </v-main>

    <!-- Footer -->
    <v-footer app color="#2a403D" dark>
      <v-row justify="center" no-gutters>
        <v-col class="text-center" cols="12">
          {{ new Date().getFullYear() }} — <strong>Find A Helper Website</strong>
        </v-col>
      </v-row>
    </v-footer>
  </v-app>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const drawer = ref(false)

const menuItems = [
  { title: 'Startseite', icon: 'mdi-home', path: '/' },
  { title: 'Über uns', icon: 'mdi-information', path: '/about' },
  { title: 'Dienstleistungen', icon: 'mdi-briefcase', path: '/services' },
  { title: 'Kontakt', icon: 'mdi-email', path: '/contact' }
]
</script>

<style scoped>
.v-main {
  min-height: calc(100vh - 64px - 36px);
}
</style>
