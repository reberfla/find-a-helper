<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import apiService from '@/service/apiService.js';
import {useAuth} from "@/service/userAuthService.ts";
import {UserModel} from "@/models/UserModel.ts";


const { getCurrentUser, login } = useAuth();
const token = getCurrentUser()?.token;

const user = ref<UserModel>(new UserModel('', ''));
const originalUser = ref<Partial<UserModel>>({});
const changedFields = ref<Partial<UserModel>>({});
const newAvatar = ref<File | null>(null);
const avatarPreview = ref('https://www.gravatar.com/avatar?d=mp');
const editMode = ref(false);

async function loadUserData() {
  if (!token) return;
  try {
    const response:any = await apiService.getUser(token);
    const userData = response.data;
    console.log(userData)
    const avatar = userData.imgBase64
      ? `data:image/png;base64,${userData.imgBase64}`
      : userData.imageUrl || 'https://www.gravatar.com/avatar?d=mp';

    const loadedUser = new UserModel(
      userData.email,
      userData.name,
      '',
      userData.id,
      userData.token,
      userData.authenticationProvider,
      userData.zipCode,
      userData.birthdate,
      undefined,
      userData.imageUrl,
      userData.imgBase64
    );

    user.value = loadedUser;
    originalUser.value = { ...loadedUser };
    avatarPreview.value = avatar;
  } catch (err) {
    console.error("Fehler beim Laden des Benutzers:", err);
  }
}

watch(user, (newVal) => {
  if (!newVal || !originalUser.value) return;

  (Object.keys(newVal) as (keyof UserModel)[]).forEach((key) => {
    const newValue = newVal[key];
    const originalValue = originalUser.value[key];

    if (newValue !== originalValue) {
      changedFields.value[key] = newValue as any;
    }
  });
}, { deep: true });



function onImageSelected(event: Event) {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (file) {
    newAvatar.value = file;
    avatarPreview.value = URL.createObjectURL(file);
    changedFields.value.image = file;
  }
}

async function saveChanges() {
  try {
    changedFields.value.id = user.value.id;
    changedFields.value.authenticationProvider = 'LOCAL';

    const formData = UserModel.toFormDataFromPartial(changedFields.value);
    const updatedUser = await apiService.updateUser(formData);

    login(updatedUser);
    editMode.value = false;
    alert('Profil erfolgreich aktualisiert!');
    changedFields.value = {};
    await loadUserData();
  } catch (err) {
    console.error("Fehler beim Speichern:", err);
    alert("Fehler beim Speichern!");
  }
}

function openAvatarPreview() {
  if (avatarPreview.value) {
    window.open(avatarPreview.value, '_blank');
  }
}

onMounted(loadUserData);
</script>

<template>
  <v-container class="text-center mt-10">
    <!-- Profilbild mit Overlay -->
    <v-row justify="center">
      <v-col cols="12" md="6" class="position-relative">
        <div class="avatar-wrapper">
          <img
            :src="avatarPreview"
            alt="Profilbild"
            class="profile-img"
            @dblclick="openAvatarPreview"
          />
          <div class="overlay-circle" @click="$refs.fileInput.click()">
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
          <v-text-field v-model="user.birthdate" label="Geburtsdatum" :readonly="!editMode" type="date" />
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
