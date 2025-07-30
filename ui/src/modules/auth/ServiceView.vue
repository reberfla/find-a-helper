
<script setup lang="ts">
import { ref } from 'vue'

interface Service {
  id: number
  title: string
  description: string
  icon: string
}

const services = ref<Service[]>([
  {
    id: 1,
    title: 'Haushalthilfe',
    description: 'Unterstützung bei alltäglichen Haushaltsaufgaben',
    icon: 'mdi-home'
  },
  {
    id: 2,
    title: 'Einkaufshilfe',
    description: 'Begleitung und Unterstützung beim Einkaufen',
    icon: 'mdi-cart'
  },
  {
    id: 3,
    title: 'Gartenarbeit',
    description: 'Hilfe bei der Pflege Ihres Gartens',
    icon: 'mdi-flower'
  },
  {
    id: 4,
    title: `Babysitting`,
    description: `Unterstützung bei der Betreuung Ihrer Kleinen`,
    icon: `mdi-baby`,
  },
  {
    id: 5,
    title: `Sonstiges`,
    description: `Andere beliebige Arbeiten`,
    icon: 'mdi-wrench'
  }
])
</script>

<template>
  <div class="services">
    <h1>Unsere Dienstleistungen</h1>
    <div class="services-grid">
      <div v-for="service in services" :key="service.id" class="service-card">
        <v-card>
          <v-card-item>
            <v-icon size="large" :icon="service.icon" class="service-icon"></v-icon>
            <v-card-title>{{ service.title }}</v-card-title>
            <v-card-text>{{ service.description }}</v-card-text>
          </v-card-item>
        </v-card>
      </div>
    </div>
  </div>
</template>

<style scoped>
.services {
  padding: 2rem;
}

v-card {
  color: #748B6F;
  border-radius: 1rem;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
}

.services h1 {
  text-align: center;
  margin-bottom: 2rem;
  color: #748B6F;
}

.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.service-card {
  transition: transform 0.3s ease;
  color: #748B6F;
}

.service-card:hover {
  transform: translateY(-5px);
}

.service-icon {
  display: block;
  margin-bottom: 1rem;
  color: #D05663;
}
</style>
