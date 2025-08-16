
<script setup lang="ts">
import {computed, ref} from 'vue'
import {serviceCategories} from "@/data/serviceCategories.ts";
import {useNav} from "@/utils/nav.ts";
import {translate} from "@/service/translationService.ts";

const t = translate

const { toTasksWith } = useNav()
const services = ref(serviceCategories)

const viewModels = computed(() =>
  services.value.map(s => ({
    ...s,
    title: t(s.titleKey),
    description: t(s.descKey),
  }))
)
</script>

<template>
  <div class="services">
    <h1>Unsere Dienstleistungen</h1>
    <v-row class="mb-12">
      <v-col
        v-for="value in viewModels"
        :key="value.slug"
        cols="12"
        md="4"
      >
        <v-card
          height="100%"
          variant="elevated"
          class="pa-4"
          @click="toTasksWith(value.slug)"
        >
          <v-card-item class="card-item">
            <v-card-title class="card-title text-h5 mb-2">
              <v-icon class="icon" :icon="value.icon" />
              <p>{{ value.title }}</p>
            </v-card-title>
            <v-card-text>
              {{ value.description }}
            </v-card-text>
          </v-card-item>
        </v-card>
      </v-col>
    </v-row>
<!--      <div v-for="service in services" :key="service.id" class="service-card">
        <v-card>
          <v-card-item>
            <v-icon size="large" :icon="service.icon" class="service-icon"></v-icon>
            <v-card-title>{{ service.title }}</v-card-title>
            <v-card-text>{{ service.description }}</v-card-text>
          </v-card-item>
        </v-card>
      </div>-->
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

.card-title{
  display: grid;
  grid-template-columns: 20% 1fr;
  grid-gap: 5px;
}

.card-item{
  height:100%;
}

</style>
