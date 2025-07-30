<template>
  <v-container fluid class="pa-0">
    <!-- Hero Section -->
    <v-parallax src="https://picsum.photos/id/41/600/900" height="600">
      <div class="d-flex flex-column fill-height justify-center align-center text-white">
        <h1 class="text-h2 font-weight-bold mb-4 text-center">
          Finden Sie den perfekten Helfer für Ihre Aufgaben
        </h1>
        <h2 class="text-h5 mb-8 text-center">
          Professionelle Unterstützung in Ihrer Nachbarschaft
        </h2>
        <div class="d-flex">
          <v-btn size="x-large" color="#D05663" class="mx-2" to="/services">
            Dienstleistungen entdecken
          </v-btn>
          <v-btn size="x-large" color="#2a403D" class="mx-2" to="/services">
            Dienstleistung anbieten
          </v-btn>
          <v-btn size="x-large" color="#748B6F" class="mx-2" to="/contact">
            Kontakt aufnehmen
          </v-btn>
        </div>
      </div>
    </v-parallax>

    <v-container class="py-12">
      <h2 class="text-h4 mb-8 text-center">Unsere Dienstleistungen</h2>
      <v-row>
        <v-col v-for="service in services" :key="service.title" cols="12" md="4">
          <v-hover v-slot="{ isHovering, props }">
            <v-card
              v-bind="props"
              :elevation="isHovering ? 8 : 2"
              :class="{ 'on-hover': isHovering }"
              height="100%"
              @click="navigateToService(service.path)"
            >
              <v-img :src="service.image" height="250" cover class="align-end">
                <v-card-title class="text-white bg-black bg-opacity-50">
                  {{ service.title }}
                </v-card-title>
              </v-img>
              <v-card-text>
                <p class="text-body-1">{{ service.description }}</p>
              </v-card-text>
              <v-card-actions>
                <v-btn variant="text" :to="service.path" color="primary">
                  Mehr erfahren
                  <v-icon class="ml-2">mdi-arrow-right</v-icon>
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-hover>
        </v-col>
      </v-row>
    </v-container>
  </v-container>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const services = [
  {
    title: 'Gartenarbeit',
    description: 'Professionelle Gartenpflege für einen gepflegten und schönen Garten.',
    image: 'https://picsum.photos/id/94/800/600',
    path: '/services/gardening',
  },
  {
    title: 'Babysitting',
    description: 'Zuverlässige und liebevolle Betreuung für Ihre Kleinen.',
    image: 'https://picsum.photos/id/822/800/600',
    path: '/services/babysitting',
  },
  {
    title: 'Platten legen',
    description: 'Fachmännische Verlegung von Fliesen und Natursteinen.',
    image: 'https://picsum.photos/id/240/800/600',
    path: '/services/tiling',
  },
]

const navigateToService = (path: string) => {
  router.push(path)
}
</script>

<style scoped>

.bg-opacity-50 {
  background-color: rgba(0, 0, 0, 0.5) !important;
}
</style>
