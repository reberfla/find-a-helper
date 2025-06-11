<template>
  <v-app>
    <v-main>
      <v-container>
        <v-card class="mx-auto my-12" max-width="400">
        <v-card-title>Vuetify Test</v-card-title>
        <v-card-text>
          <v-btn color="primary">Vuetify is working... Yeee !</v-btn>
        </v-card-text>
        </v-card>
      </v-container>
    </v-main>
  </v-app>
</template>
<script setup lang="ts">
</script>

<style scoped lang="scss">
$bg-color: #f5f5f5;

.v-card {
  background-color: $bg-color;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.v-btn {
  font-weight: bold;
  transition: background-color 0.3s;

  &:hover {
    background-color: darken($bg-color, 10%);
  }
}
</style>

