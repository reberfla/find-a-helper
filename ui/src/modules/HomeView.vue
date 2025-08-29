<template>
  <v-container fluid class="pa-0">
    <v-parallax src="/assets/images/home.jpg" height="600">
      <div id="particles-js"></div>
      <div class="overlay"></div>
      <div
        class="header-container d-flex flex-column fill-height justify-center align-center text-white"
      >
        <h1 class="text-h2 font-weight-bold mb-4 text-center">
          Finden Sie den perfekten Helfer für Ihre Aufgaben
        </h1>
        <h2 class="text-h5 mb-8 text-center">
          Professionelle Unterstützung in Ihrer Nachbarschaft
        </h2>
        <div class="btn-container d-flex">
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
  </v-container>

  <v-container class="py-12">
    <h2 class="text-h4 mb-8 text-center">Unsere Dienstleistungen</h2>
    <v-row>
      <v-col v-for="s in viewModels" :key="s.slug" cols="12" md="4">
        <v-hover v-slot="{ isHovering, props }">
          <v-card
            class="service-card"
            v-bind="props"
            :elevation="isHovering ? 8 : 2"
            :class="{ 'on-hover': isHovering }"
            height="100%"
            @click="toTasksWith(s.slug)"
          >
            <v-img :src="s.image" height="230" cover class="align-end">
              <v-card-title class="text-white bg-black bg-opacity-50">
                {{ s.title }}
              </v-card-title>
            </v-img>

            <v-card-text>
              <p class="text-body-1">{{ s.description }}</p>
            </v-card-text>

            <v-card-actions>
              <v-btn variant="text" color="primary" @click.stop="toTasksWith(s.slug)">
                Mehr erfahren
                <v-icon class="ml-2">arrow_right</v-icon>
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-hover>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { serviceCategories } from '@/data/serviceCategories.ts'
import { useNav } from '@/utils/nav.ts'

onMounted(() => {
  window.particlesJS?.load('particles-js', '/particles.json')
})

const { toTasksWith } = useNav()
const services = ref(serviceCategories)

const viewModels = computed(() =>
  services.value.map((s) => ({
    ...s,
    title: s.titleKey,
    description: s.descKey,
  })),
)

onMounted(() => {
  window.particlesJS?.load('particles-js', '/particles.json')
})
</script>

<style scoped>
.bg-opacity-50 {
  background-color: rgba(0, 0, 0, 0.5) !important;
}

#particles-js {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(42, 64, 61, 0.45);
  z-index: 2;
}

.header-container {
  position: relative;
  z-index: 3;
}

.btn-container {
  width: 100%;
  justify-content: center;
}

.service-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-content: space-between;
  justify-content: space-evenly;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(42, 64, 61, 0.45);
  z-index: 1;
}

.header-container {
  position: relative;
  z-index: 2;
  padding: 12px;
}
</style>
