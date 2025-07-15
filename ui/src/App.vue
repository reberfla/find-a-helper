<template style="height: 100%">

  <v-app>
    <v-icon icon="mdi-home" />
    <v-app-bar app color="#2a403D" dark>
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>Find A Helper</v-toolbar-title>
      <v-spacer></v-spacer>
      <img src="\src\assets\hand-heart.svg" alt="fahLogo" width="60px" height="auto">

      <v-btn icon>
        <v-icon>mdi-</v-icon>
      </v-btn>
      <v-btn icon>
        <v-icon>mdi-menu</v-icon>
      </v-btn>
    </v-app-bar>
    <v-navigation-drawer v-model="drawer" app>
      <v-list>
        <v-list-item v-for="item in menuItems" :key="item.title" :to="item.path">
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main style="background-color: #C3CBD6;">
      <v-container>
        <v-row>
          <v-col v-for="card in cards" :key="card.title" cols="4" >
            <v-card elevation="2" height="100%" >
              <v-img
                :src="card.image"
                height="300px"
                cover
              ></v-img>
              <v-card-title>{{ card.title }}</v-card-title>
              <v-card-text>{{ card.text }}</v-card-text>
            </v-card>
          </v-col>
        </v-row>


      </v-container>
    </v-main>

    <v-footer app color="#2a403D" dark>
      <v-row justify="center" no-gutters>
        <v-col class="text-center" cols="12">
          {{ new Date().getFullYear() }} — <strong>Meine Vuetify Website</strong>
        </v-col>
      </v-row>
    </v-footer>
  </v-app>

  <nav>
    <RouterLink to="/">Home</RouterLink>
    <RouterLink to="/about">About</RouterLink>
  </nav>

  <RouterView />
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

const cards = [
  {
    title: 'Gartenarbeit',
    text: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloribus facere ipsum atque sequi ad error est ab incidunt vero libero facilis, nihil et cumque reiciendis iste hic, minus nam vel.',
    image: 'https://picsum.photos/id/0/500/300'
  },
  {
    title: 'Babysitting',
    text: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloribus facere ipsum atque sequi ad error est ab incidunt vero libero facilis, nihil et cumque reiciendis iste hic, minus nam vel.',
    image: 'https://picsum.photos/id/1/500/300'
  },
  {
    title: 'Platten legen',
    text: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloribus facere ipsum atque sequi ad error est ab incidunt vero libero facilis, nihil et cumque reiciendis iste hic, minus nam vel.',
    image: 'https://picsum.photos/id/2/500/300'
  }
]

</script>


/*ligth:  #C3CBD6
bright:   #D05663
green:    #748B6F
Darkgreen:#2a403D
*/
<style scoped>

h1 {
  color: #D05663;
  min-width: 200px;
  margin: 5px;
  border: 1px solid orange;
}

img {
  color: #D05663;
}

header {
  line-height: 1.5;
  max-height: 100vh;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 0.1rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;
  }
}
</style>
