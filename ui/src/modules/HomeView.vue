<template>
  <v-container fluid class="pa-0">
    <v-parallax :src="heroSrc" :height="parallaxHeight">
      <div id="particles-js"></div>
      <div class="overlay"></div>

      <div
        class="header-container d-flex flex-column fill-height justify-center align-center text-white"
      >
        <h1 class="text-h4 text-md-h2 font-weight-bold mb-3 text-center">
          Finden Sie den perfekten Helfer für Ihre Aufgaben
        </h1>
        <h2 class="text-subtitle-1 text-md-h5 mb-6 text-center">
          Professionelle Unterstützung in Ihrer Nachbarschaft
        </h2>

        <div class="btn-container d-flex flex-wrap" :class="stacked ? 'stacked' : ''">
          <v-btn :block="stacked" size="large" color="#D05663" class="mx-2 my-1" to="/services">
            Dienstleistungen entdecken
          </v-btn>
          <v-btn :block="stacked" size="large" color="#2a403D" class="mx-2 my-1" to="/services">
            Dienstleistung anbieten
          </v-btn>
          <v-btn :block="stacked" size="large" color="#748B6F" class="mx-2 my-1" to="/contact">
            Kontakt aufnehmen
          </v-btn>
        </div>
      </div>
    </v-parallax>
  </v-container>

  <v-container class="py-10 py-md-12">
    <h2 class="text-h5 text-md-h4 mb-6 mb-md-8 text-center">Unsere Dienstleistungen</h2>

    <v-row dense>
      <v-col v-for="s in viewModels" :key="s.slug" cols="12" sm="6" md="4">
        <v-hover v-slot="{ isHovering, props }">
          <v-card
            class="service-card"
            v-bind="props"
            :elevation="isHovering ? 8 : 2"
            :class="{ 'on-hover': isHovering }"
            @click="toTasksWith(s.slug)"
          >
            <v-img :src="s.image" class="align-end" cover aspect-ratio="16/9">
              <v-card-title class="text-white bg-black bg-opacity-50">
                {{ s.title }}
              </v-card-title>
            </v-img>

            <v-card-text>
              <p class="text-body-2 text-md-body-1">{{ s.description }}</p>
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
import { useDisplay } from 'vuetify'

const heroSrc = '/assets/images/home.jpg'
const { toTasksWith } = useNav()
const services = ref(serviceCategories)
const viewModels = computed(() =>
  services.value.map((s) => ({ ...s, title: s.titleKey, description: s.descKey })),
)

// Vuetify Breakpoints
const { xs, smAndDown, mdAndUp } = useDisplay()
const stacked = computed(() => smAndDown.value)
const parallaxHeight = computed(() => (mdAndUp.value ? 600 : 360))

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
  inset: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.overlay {
  position: absolute;
  inset: 0;
  background: rgba(42, 64, 61, 0.45);
  z-index: 2;
}

.header-container {
  position: relative;
  z-index: 3;
  padding: 12px;
  text-align: center;
}

.btn-container {
  width: 100%;
  justify-content: center;
  gap: 8px;
}

.btn-container.stacked .v-btn {
  max-width: 520px;
}

.service-card {
  display: flex;
  flex-direction: column;
  height: 100%;
}

@media (max-width: 600px) {
  .header-container {
    padding-inline: 8px;
  }
}
</style>
