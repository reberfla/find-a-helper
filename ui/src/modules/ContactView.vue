<template>
  <v-container>
    <v-row>
      <v-col cols="12" md="6">
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

          <v-textarea
            v-model="form.message"
            label="Nachricht"
            required
            variant="outlined"
          ></v-textarea>

          <v-btn type="submit" color="#748B6F" block> Nachricht senden </v-btn>
        </v-form>
      </v-col>
      <v-col cols="12" md="6">
        <v-card class="pa-4">
          <h2 class="text-h5 mb-4">Kontaktinformationen</h2>
          <v-list>
            <v-list-item>
              <template v-slot:prepend>
                <v-icon>mdi-map-marker</v-icon>
              </template>
              <v-list-item-title>Sesamstraße 187</v-list-item-title>
              <v-list-item-subtitle>3141 Entenhausen</v-list-item-subtitle>
            </v-list-item>

            <v-list-item>
              <template v-slot:prepend>
                <v-icon>mdi-phone</v-icon>
              </template>
              <v-list-item-title>+41 011 222 33 44</v-list-item-title>
            </v-list-item>

            <v-list-item>
              <template v-slot:prepend>
                <v-icon>mdi-email</v-icon>
              </template>
              <v-list-item-title>info@findahelper.ch</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'

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
