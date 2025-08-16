<script lang="ts" setup>
import AuthView from '@/modules/auth/AuthView.vue'
import { onMounted, ref } from 'vue'
import { getLanguage, setLanguage, translate } from '@/service/translationService.js'
import { useAuth } from '@/service/userAuthService.ts'
import SnackBar from '@/components/Snackbar.vue'
const { isLoggedIn, logout, getCurrentUser, getCurrentUserAvatar } = useAuth()
import { useRouter } from 'vue-router'

const authDialogVisible = ref(false)
const authMode = ref<'login' | 'register'>('login')

const snackBar = ref<InstanceType<typeof SnackBar> | null>(null)

function openAuth(mode: 'login' | 'register') {
  authMode.value = mode
  authDialogVisible.value = true
}
const router = useRouter()
const dropdownOpen = ref(false)
const languageDropdownOpen = ref(false)
const currentLanguage = ref(getLanguage())

const t = translate

const emit = defineEmits(['onAuthChange'])

function onLogin() {
  snackBar.value?.show(t('LABEL_LOGIN_SUCCESS'), 'info')
  dropdownOpen.value = false
  emit('onAuthChange')
}

function goHome() {
  router.push('/')
}

function handleLogout() {
  logout()
  snackBar.value?.show(t('LABEL_LOGOUT_SUCCESS'), 'info')
  dropdownOpen.value = false
  emit('onAuthChange')
}

function getFlagIcon(lang: any) {
  switch (lang) {
    case 'en':
      return '🇬🇧'
    case 'de':
      return '🇩🇪'
    default:
      return '🌐'
  }
}

function getAvailableLanguages() {
  return ['en', 'de'].filter((lang) => lang !== currentLanguage.value)
}

function changeLanguage(lang: any) {
  currentLanguage.value = lang
  setLanguage(lang)
  languageDropdownOpen.value = false
}

onMounted(() => {})

const drawer = ref(false)

const menuItems = [
  { title: 'Startseite', icon: 'home', path: '/' },
  { title: 'Über uns', icon: 'info', path: '/about' },
  { title: 'Aufgaben Entdecken', icon: 'info', path: '/tasks' },
  { title: 'Dienstleistungen', icon: 'work', path: '/services' },
  { title: 'Kontakt', icon: 'email', path: '/contact' },
]
</script>

<template>
  <!-- Top Navigation Bar -->
  <v-app-bar app color="#2a403D" dark>
    <v-btn icon @click="drawer = !drawer">
      <v-icon>{{ drawer ? 'close' : 'menu' }}</v-icon>
    </v-btn>

    <v-toolbar-title class="d-flex align-center brand-link" @click="goHome">
      <span class="font-weight-bold">Find A Helper</span>
    </v-toolbar-title>


    <!-- Language Switch -->
    <v-menu offset-y>
      <template #activator="{ props }">
        <v-btn icon v-bind="props">
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
        <v-avatar size="36" v-bind="props">
          <img
            :src="getCurrentUserAvatar()"
            alt="Avatar"
            height="36"
            width="36"
            referrerpolicy="no-referrer"
          />
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
            <v-list-item-title>{{ getCurrentUser()?.name }}</v-list-item-title>
            <v-list-item-subtitle>{{ getCurrentUser()?.email }}</v-list-item-subtitle>
          </v-list-item>

          <template v-if="isLoggedIn">
            <v-list-item @click="router.push('/profil')">
              <v-list-item-title>Benutzerprofil verwalten</v-list-item-title>
            </v-list-item>
          </template>

          <v-divider />
          <v-list-item @click="handleLogout">
            <v-list-item-title>{{ t('LABEL_LOGOUT') }}</v-list-item-title>
          </v-list-item>
        </template>
      </v-list>
    </v-menu>
  </v-app-bar>

  <!-- Sidebar Navigation -->
  <v-navigation-drawer v-model="drawer" app>
    <v-list>
      <v-list-item v-for="item in menuItems" :key="item.title" :to="item.path" link>
        <template #prepend>
          <v-icon>{{ item.icon }}</v-icon>
        </template>
        <v-list-item-title>{{ item.title }}</v-list-item-title>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>

  <!-- Auth Dialog -->
  <v-dialog v-model="authDialogVisible" max-width="500">
    <AuthView :mode="authMode" @close="authDialogVisible = false" @logged-in="onLogin" />
  </v-dialog>

  <!-- Snackbar -->
  <SnackBar ref="snackBar" />
</template>

<style scoped>
.brand-link {
  user-select: none;
  cursor: pointer;
}
</style>

