<template>
  <v-container>
    <h1 class="text-h3 mb-6">Kontakt</h1>
    <v-form @submit.prevent="onSubmit">
      <v-text-field v-model="form.name" label="Name" required variant="outlined"></v-text-field>

      <v-text-field
        v-model="form.email"
        label="E-Mail"
        type="email"
        required
        variant="outlined"
      ></v-text-field>

      <v-select
        v-model="form.subject"
        :items="subjects"
        label="Betreff"
        required
        variant="outlined"
      ></v-select>

      <v-textarea v-model="form.message" label="Nachricht" required variant="outlined"></v-textarea>

      <v-btn type="submit" color="#748B6F" block> Nachricht senden </v-btn>
    </v-form>
  </v-container>
  <Footer />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Footer from '@/components/Footer.vue'

const form = ref({
  name: '',
  email: '',
  subject: '',
  message: '',
})

const subjects = ['Allgemeine Anfrage', 'Gartenarbeit', 'Babysitting', 'Platten legen', 'Sonstiges']

const onSubmit = () => {
  console.log('Form submitted:', form.value)
}
</script>
