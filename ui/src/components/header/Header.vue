<script setup lang="ts">

import AuthView from "@/modules/auth/AuthView.vue";
import { ref, computed, onMounted } from 'vue';
import { translate, getLanguage, setLanguage } from '@/service/translationService.js';

const authDialogVisible = ref(false);
const authMode = ref<'login' | 'register'>('login');

function openAuth(mode: 'login' | 'register') {
  authMode.value = mode;
  authDialogVisible.value = true;
}

const dropdownOpen = ref(false);
const languageDropdownOpen = ref(false);
const currentLanguage = ref(getLanguage());

const userEmail = ref(localStorage.getItem('userEmail') || null);
const userName = ref(localStorage.getItem('userName') || 'User');
const userAvatar = ref(localStorage.getItem('userAvatar') || 'https://www.gravatar.com/avatar?d=mp');

const t = translate;

const emit = defineEmits(['onAuthChange'])

const isLoggedIn = computed(() => {
  return !!localStorage.getItem('token');
});

function onLogin(data:any) {
  console.log(data)
  userEmail.value = data.email;
  userName.value = data.name
  userAvatar.value = data.imgUrl || data.img || 'https://www.gravatar.com/avatar?d=mp';
  //emit('onLogin')
}

function toggleDropdown() {
  dropdownOpen.value = !dropdownOpen.value;
}

function logout() {
  localStorage.clear();
  userEmail.value = null;
  userName.value = '';
  userAvatar.value = '';
  dropdownOpen.value = false;
  location.reload();
  emit('onAuthChange')
}

function getFlagIcon(lang:any) {
  switch (lang) {
    case 'en': return '🇬🇧';
    case 'de': return '🇩🇪';
    default: return '🌐';
  }
}

function getAvailableLanguages() {
  return [ 'en', 'de'].filter(lang => lang !== currentLanguage.value);
}

function toggleLanguageDropdown() {
  languageDropdownOpen.value = !languageDropdownOpen.value;
}

function changeLanguage(lang:any) {
  currentLanguage.value = lang;
  setLanguage(lang);
  languageDropdownOpen.value = false;
}

onMounted(() => {
  console.log('component Header rendered...');
});

</script>

<template>
  <v-app-bar app>
    <v-toolbar-title>Find A Helper</v-toolbar-title>

    <v-spacer />

    <!-- Language Switch -->
    <v-menu offset-y>
      <template #activator="{ props }">
        <v-btn v-bind="props" icon>
          <span>{{ getFlagIcon(currentLanguage) }}</span>
        </v-btn>
      </template>
      <v-list>
        <v-list-item
          v-for="lang in getAvailableLanguages()"
          :key="lang"
          @click="changeLanguage(lang)"
        >
          <v-list-item-title>{{ getFlagIcon(lang) }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>

    <!-- User/Profile -->
    <v-menu offset-y>
      <template #activator="{ props }">
        <v-avatar v-bind="props" size="36">
          <img :src="userAvatar" alt="Avatar" />
        </v-avatar>
      </template>

      <v-list>
        <template v-if="!isLoggedIn">
          <v-list-item @click="openAuth('login')">
            <v-list-item-title>{{ t('LABEL_LOGIN') }}</v-list-item-title>
          </v-list-item>
          <v-list-item @click="openAuth('register')">
            <v-list-item-title>{{ t('LABEL_REGISTRATION') }}</v-list-item-title>
          </v-list-item>
        </template>
        <template v-else>
          <v-list-item>
            <v-list-item-title>{{ userName }}</v-list-item-title>
            <v-list-item-subtitle>{{ userEmail }}</v-list-item-subtitle>
          </v-list-item>
          <v-divider />
          <v-list-item @click="logout">
            <v-list-item-title>{{ t('LABEL_LOGOUT') }}</v-list-item-title>
          </v-list-item>
        </template>
      </v-list>
    </v-menu>

    <!-- Auth Dialog -->
    <v-dialog v-model="authDialogVisible" max-width="500">
      <AuthView :mode="authMode" @close="authDialogVisible = false" @logged-in="onLogin" />
    </v-dialog>
  </v-app-bar>
</template>

<style scoped>

</style>
