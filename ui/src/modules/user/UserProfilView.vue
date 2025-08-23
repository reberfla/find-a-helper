<script setup lang="ts">
import { onMounted, ref, toRaw, watch } from 'vue'
import apiService from '@/service/apiService.js'
import { useAuth } from '@/service/userAuthService.ts'
import { UserModel } from '@/models/UserModel.ts'
import SnackBar from '@/components/Snackbar.vue'
import { translate } from '@/service/translationService.ts'

const { getCurrentUser, getCurrentUserAvatar, login } = useAuth()
const token = getCurrentUser()?.token

const user = ref<UserModel>(new UserModel('', ''))
const originalUser = ref<Partial<UserModel>>({})
const changedFields = ref<Partial<UserModel>>({})
const newAvatar = ref<File | null>(null)
const editMode = ref(false)
const snackBar = ref<InstanceType<typeof SnackBar> | null>(null)
const t = translate
const fileInput = ref<HTMLInputElement | null>(null)

async function loadUserData() {
  if (!token) return
  await apiService
    .getUser()
    .then((userData: UserModel) => {
      user.value = userData
      originalUser.value = { ...userData }
    })
    .catch((e: any) => {
      console.error('Fehler beim Laden des Benutzers:', e)
    })
}

watch(
  user,
  (newVal) => {
    if (!newVal || !originalUser.value) return

    const updatedFields: Partial<UserModel> = {}
    for (const key of Object.keys(newVal) as (keyof UserModel)[]) {
      const newValue = newVal[key]
      const originalValue = originalUser.value[key]
      if (newValue !== originalValue) {
        updatedFields[key] = newValue as any
      }
    }

    changedFields.value = updatedFields
  },
  { deep: true, immediate: true },
)

function onImageSelected(event: Event) {
  const file = (event.target as HTMLInputElement).files?.[0]
  if (file) {
    newAvatar.value = file
    const reader = new FileReader()
    reader.onload = async () => {
      const result = reader.result?.toString()
      if (result) {
        changedFields.value.imgBase64 = result.split(',')[1]
        await saveChanges()
      }
    }
    reader.readAsDataURL(file)
  }
}

async function saveChanges() {
  changedFields.value.id = user.value.id

  const { id, googleToken, imageUrl, authProvider, ...rest } = toRaw(changedFields.value)

  if (rest.zipCode !== undefined && rest.zipCode !== null) {
    rest.zipCode = Number(rest.zipCode)
  } else {
    delete rest.zipCode
  }

  apiService
    .updateUser(rest)
    .then(async (res: any) => {
      editMode.value = false
      snackBar.value?.show(t('SAVE_SUCESS'), 'info')
      changedFields.value = {}
      await loadUserData()
    })
    .catch((e: any) => {
      snackBar.value?.show(t('ERROR_SAVE_FAILED'), 'error')
      console.error('Fehler beim Speichern:', e)
    })
}

onMounted(loadUserData)
</script>

<template>
  <v-container class="text-center mt-10">
    <!-- Profilbild mit Overlay -->
    <v-row justify="center">
      <v-col cols="12" md="6" class="position-relative">
        <div class="avatar-wrapper">
          <img :src="getCurrentUserAvatar()" alt="Profilbild" class="profile-img" />
          <div class="overlay-circle" @click="fileInput?.click()">
            <v-icon color="white" size="30">camera</v-icon>
          </div>
        </div>

        <input
          ref="fileInput"
          type="file"
          accept="image/*"
          class="d-none"
          @change="onImageSelected"
        />
      </v-col>
    </v-row>

    <!-- Benutzerdaten -->
    <v-row justify="center" class="mt-4">
      <v-col cols="12" md="6">
        <v-form>
          <v-text-field v-model="user.name" label="Name" :readonly="!editMode" />
          <v-text-field v-model="user.email" label="E-Mail" :readonly="!editMode" />
          <v-text-field v-model="user.zipCode" label="PLZ" :readonly="!editMode" />
          <v-text-field
            v-model="user.birthdate"
            label="Geburtsdatum"
            :readonly="!editMode"
            type="date"
          />
          <v-text-field
            v-if="editMode"
            v-model="user.password"
            label="Neues Passwort (optional)"
            type="password"
          />

          <v-btn color="primary" class="mt-4" @click="editMode ? saveChanges() : (editMode = true)">
            {{ editMode ? 'Speichern' : 'Bearbeiten' }}
          </v-btn>
        </v-form>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.avatar-wrapper {
  position: relative;
  display: inline-block;
  width: 140px;
  height: 140px;
  border-radius: 50%;
  overflow: hidden;
  margin: auto;
}

.profile-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
  transition: transform 0.3s ease;
}

.avatar-wrapper:hover .profile-img {
  transform: scale(1.05);
}

.overlay-circle {
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 40%;
  background: rgba(0, 0, 0, 0.4);
  border-bottom-left-radius: 70px;
  border-bottom-right-radius: 70px;
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  pointer-events: none;
}

.avatar-wrapper:hover .overlay-circle {
  opacity: 1;
  pointer-events: auto;
  cursor: pointer;
}
</style>
