<template>
  <v-container fluid class="pa-0">
    <!-- Hero Section -->
    <v-parallax
      src="/src/assets/hero-background.jpg"
      height="600"
    >
      <div class="d-flex flex-column fill-height justify-center align-center text-white">
        <h1 class="text-h2 font-weight-bold mb-4 text-center">
          Finden Sie den perfekten Helfer für Ihre Aufgaben
        </h1>
        <h2 class="text-h5 mb-8 text-center">
          Professionelle Unterstützung in Ihrer Nachbarschaft
        </h2>
        <div class="d-flex">
          <v-btn
            size="x-large"
            color="primary"
            class="mx-2"
            to="/services"
          >
            Dienstleistungen entdecken
          </v-btn>
          <v-btn
            size="x-large"
            variant="outlined"
            color="white"
            class="mx-2"
            to="/contact"
          >
            Kontakt aufnehmen
          </v-btn>
        </div>
      </div>
    </v-parallax>

    <!-- Services Section -->
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
              <v-img
                :src="service.image"
                height="250"
                cover
                class="align-end"
              >
                <v-card-title class="text-white bg-black bg-opacity-50">
                  {{ service.title }}
                </v-card-title>
              </v-img>
              <v-card-text>
                <p class="text-body-1">{{ service.description }}</p>
              </v-card-text>
              <v-card-actions>
                <v-btn
                  variant="text"
                  :to="service.path"
                  color="primary"
                >
                  Mehr erfahren
                  <v-icon class="ml-2">mdi-arrow-right</v-icon>
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-hover>
        </v-col>
      </v-row>
    </v-container>

    <!-- How it Works Section -->
    <v-container class="py-12 bg-grey-lighten-4">
      <h2 class="text-h4 mb-8 text-center">So funktioniert's</h2>
      <v-row>
        <v-col v-for="(step, index) in steps" :key="index" cols="12" md="3">
          <v-card class="text-center" variant="flat">
            <v-card-item>
              <div class="mb-4">
                <v-avatar
                  color="primary"
                  size="64"
                  class="text-h4"
                >
                  {{ index + 1 }}
                </v-avatar>
              </div>
              <v-card-title class="justify-center text-h6">
                {{ step.title }}
              </v-card-title>
              <v-card-text>
                {{ step.description }}
              </v-card-text>
            </v-card-item>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <!-- Testimonials Section -->
    <v-container class="py-12">
      <h2 class="text-h4 mb-8 text-center">Das sagen unsere Kunden</h2>
      <v-row>
        <v-col v-for="review in testimonials" :key="review.name" cols="12" md="4">
          <v-card height="100%">
            <v-card-item>
              <div class="mb-4">
                <v-rating
                  :model-value="5"
                  color="warning"
                  density="compact"
                  readonly
                ></v-rating>
              </div>
              <v-card-text class="text-body-1">
                "{{ review.text }}"
              </v-card-text>
              <v-card-subtitle class="pt-4">
                <strong>{{ review.name }}</strong>
                <br>
                {{ review.location }}
              </v-card-subtitle>
            </v-card-item>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <!-- CTA Section -->
    <v-sheet color="primary" class="py-12">
      <v-container>
        <v-row justify="center" align="center">
          <v-col cols="12" md="8" class="text-center">
            <h2 class="text-h4 text-white mb-6">
              Bereit, Unterstützung zu finden?
            </h2>
            <v-btn
              size="x-large"
              color="white"
              variant="flat"
              to="/contact"
            >
              Jetzt starten
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-sheet>
  </v-container>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const services = [
  {
    title: 'Gartenarbeit',
    description: 'Professionelle Gartenpflege für einen gepflegten und schönen Garten.',
    image: '/src/assets/services/gardening.jpg',
    path: '/services/gardening'
  },
  {
    title: 'Babysitting',
    description: 'Zuverlässige und liebevolle Betreuung für Ihre Kleinen.',
    image: '/src/assets/services/babysitting.jpg',
    path: '/services/babysitting'
  },
  {
    title: 'Platten legen',
    description: 'Fachmännische Verlegung von Fliesen und Natursteinen.',
    image: '/src/assets/services/tiling.jpg',
    path: '/services/tiling'
  }
]

const steps = [
  {
    title: 'Dienstleistung wählen',
    description: 'Wählen Sie aus unserem vielfältigen Angebot die passende Dienstleistung.'
  },
  {
    title: 'Helfer finden',
    description: 'Finden Sie den perfekten Helfer in Ihrer Nähe.'
  },
  {
    title: 'Termin vereinbaren',
    description: 'Legen Sie einen passenden Termin für die Durchführung fest.'
  },
  {
    title: 'Bewertung abgeben',
    description: 'Teilen Sie Ihre Erfahrung mit der Community.'
  }
]

const testimonials = [
  {
    name: 'Maria Schmidt',
    location: 'München',
    text: 'Fantastischer Service! Mein Garten sieht jetzt wunderschön aus.'
  },
  {
    name: 'Thomas Weber',
    location: 'Berlin',
    text: 'Sehr professionelle Arbeit bei der Verlegung meiner Terrasse.'
  },
  {
    name: 'Julia Meyer',
    location: 'Hamburg',
    text: 'Die Babysitterin war einfach toll mit unseren Kindern.'
  }
]

const navigateToService = (path: string) => {
  router.push(path)
}
</script>

<style scoped>
.v-card {
  transition: all 0.3s ease-in-out;
}

.v-card.on-hover {
  transform: translateY(-8px);
}

.bg-opacity-50 {
  background-color: rgba(0, 0, 0, 0.5) !important;
}
</style>
