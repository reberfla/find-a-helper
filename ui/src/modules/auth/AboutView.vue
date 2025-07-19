<template>
  <v-container>
    <!-- Hero Section -->
    <v-row class="mb-8">
      <v-col cols="12" class="text-center">
        <h1 class="text-h2 mb-4">Über Find A Helper</h1>
        <p class="text-subtitle-1">
          Wir verbinden Menschen, die Hilfe suchen, mit denen, die helfen möchten.
        </p>
      </v-col>
    </v-row>

    <!-- Mission Section -->
    <v-row class="mb-12">
      <v-col cols="12" md="6">
        <v-img
          src="/src/assets/mission-image.jpg"
          height="400"
          cover
          class="rounded-lg"
        ></v-img>
      </v-col>
      <v-col cols="12" md="6" class="d-flex align-center">
        <div>
          <h2 class="text-h4 mb-4">Unsere Mission</h2>
          <p class="text-body-1 mb-4">
            Find A Helper wurde mit dem Ziel gegründet, Menschen in ihrer Nachbarschaft
            zusammenzubringen. Wir glauben daran, dass gegenseitige Hilfe und
            Unterstützung das Fundament einer starken Gemeinschaft sind.
          </p>
          <p class="text-body-1">
            Unser Netzwerk ermöglicht es Menschen, schnell und unkompliziert
            qualifizierte Hilfe für alltägliche Aufgaben zu finden.
          </p>
        </div>
      </v-col>
    </v-row>

    <!-- Values Section -->
    <v-row class="mb-12">
      <v-col cols="12">
        <h2 class="text-h4 mb-6 text-center">Unsere Werte</h2>
      </v-col>
      <v-col v-for="value in values" :key="value.title" cols="12" md="4">
        <v-card height="100%" variant="elevated" class="pa-4">
          <v-card-item>
            <v-icon
              :color="value.color"
              size="x-large"
              class="mb-4"
            >
              {{ value.icon }}
            </v-icon>
            <v-card-title class="text-h5 mb-2">
              {{ value.title }}
            </v-card-title>
            <v-card-text>
              {{ value.description }}
            </v-card-text>
          </v-card-item>
        </v-card>
      </v-col>
    </v-row>

    <!-- Team Section -->
    <v-row class="mb-12">
      <v-col cols="12">
        <h2 class="text-h4 mb-6 text-center">Unser Team</h2>
      </v-col>
      <v-col v-for="member in team" :key="member.name" cols="12" sm="6" md="3">
        <v-card class="text-center">
          <v-img
            :src="member.image"
            height="200"
            cover
            class="bg-grey-lighten-2"
          ></v-img>
          <v-card-title class="justify-center">{{ member.name }}</v-card-title>
          <v-card-subtitle>{{ member.position }}</v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>

    <!-- Statistics Section -->
    <v-row class="mb-12 bg-grey-lighten-4 rounded-lg pa-4">
      <v-col v-for="stat in statistics" :key="stat.value" cols="12" md="4" class="text-center">
        <h3 class="text-h3 mb-2" style="color: #2a403D">{{ stat.value }}</h3>
        <p class="text-subtitle-1">{{ stat.label }}</p>
      </v-col>
    </v-row>

    <!-- Call to Action -->
    <v-row>
      <v-col cols="12" class="text-center">
        <h2 class="text-h4 mb-4">Werden Sie Teil unserer Community</h2>
        <v-btn
          color="primary"
          size="x-large"
          to="/contact"
          class="mx-2"
        >
          Jetzt Kontakt aufnehmen
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
const values = [
  {
    title: 'Vertrauen',
    description: 'Wir schaffen eine sichere Plattform, auf der sich Menschen gegenseitig vertrauen können.',
    icon: 'mdi-shield-check',
    color: 'success'
  },
  {
    title: 'Gemeinschaft',
    description: 'Wir fördern den Zusammenhalt in der Nachbarschaft und schaffen neue Verbindungen.',
    icon: 'mdi-account-group',
    color: 'primary'
  },
  {
    title: 'Qualität',
    description: 'Wir setzen hohe Standards für unsere Dienstleistungen und deren Vermittlung.',
    icon: 'mdi-star',
    color: 'warning'
  }
]

const team = [
  {
    name: 'Max Mustermann',
    position: 'Geschäftsführer',
    image: '/src/assets/team/person1.jpg'
  },
  {
    name: 'Anna Schmidt',
    position: 'Kundenbetreuung',
    image: '/src/assets/team/person2.jpg'
  },
  {
    name: 'Tom Weber',
    position: 'Entwicklung',
    image: '/src/assets/team/person3.jpg'
  },
  {
    name: 'Lisa Meyer',
    position: 'Marketing',
    image: '/src/assets/team/person4.jpg'
  }
]

const statistics = [
  {
    value: '5000+',
    label: 'Zufriedene Kunden'
  },
  {
    value: '1000+',
    label: 'Aktive Helfer'
  },
  {
    value: '15+',
    label: 'Städte'
  }
]
</script>

<style scoped>
.v-container {
  max-width: 1200px;
}

/* Animationen für Statistiken */
.text-h3 {
  transition: all 0.3s ease;
}

.text-h3:hover {
  transform: scale(1.1);
}
</style>
