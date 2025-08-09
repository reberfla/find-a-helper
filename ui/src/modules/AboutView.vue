<template>
  <v-container>
    <v-row class="mb-8">
      <v-col cols="12" class="text-center">
        <h1 class="text-h2 mb-4">Über Find A Helper</h1>
        <p class="text-subtitle-1">
          Wir verbinden Menschen, die Hilfe suchen, mit denen, die helfen möchten.
        </p>
      </v-col>
    </v-row>

    <v-row class="mb-12">
      <v-col cols="12" md="6">
        <v-img
          src='https://picsum.photos/id/240/800/600'
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

    <v-row class="mb-12">
      <v-col cols="12">
        <h2 class="text-h4 mb-6 text-center">Unsere Werte</h2>
      </v-col>
      <v-col v-for="value in values" :key="value.title" cols="12" md="4">
        <v-card height="100%" variant="elevated" class="pa-4">
          <v-card-item class="card-item">
              <v-card-title class="card-title text-h5 mb-2">
                <v-icon
                  class="icon"
                  :color="value.color"
                >
                  {{ value.icon }}
                </v-icon>
                <p>
                  {{ value.title }}
                </p>
              </v-card-title>
            <v-card-text>
              {{ value.description }}
            </v-card-text>
          </v-card-item>
        </v-card>
      </v-col>
    </v-row>

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


  </v-container>
</template>

<script setup lang="ts">
const values = [
  {
    title: 'Vertrauen',
    description: 'Wir schaffen eine sichere Plattform, auf der sich Menschen gegenseitig vertrauen können.',
    icon: 'check',
    color: 'success'
  },
  {
    title: 'Gemeinschaft',
    description: 'Wir fördern den Zusammenhalt in der Nachbarschaft und schaffen neue Verbindungen.',
    icon: 'group',
    color: 'primary'
  },
  {
    title: 'Qualität',
    description: 'Wir setzen hohe Standards für unsere Dienstleistungen und deren Vermittlung.',
    icon: 'star',
    color: 'warning'
  }
]

const team = [
  {
    name: 'Flavio Reber',
    position: 'Teamleiter/ Backend-Genie',
    image: '/src/assets/team/fre.jpg'
  },
  {
    name: 'Renata Irinyi',
    position: 'Entwicklung/ Die Akrybische',
    image: '/src/assets/team/rir.jpg'
  },
  {
    name: 'Michael Wenger',
    position: 'Entwicklung/ Kreative Birne',
    image: '/src/assets/team/mwe.jpg'
  },
  {
    name: 'Hannes Morgenthaler',
    position: 'Leerer für WebApp',
    image: '/src/assets/team/hmo.jpg'
  }
]


</script>

<style scoped>

h1 {
  color: #D05663 !important;
}
p {
  color: #2a403D !important;
}
h2 {
  color: #D05663 !important;
}

.card-title{
  display: grid;
  grid-template-columns: 20% 1fr;
  grid-gap: 5px;
}

.card-item{
  height:100%;
}


</style>
