<script setup lang="ts">
import {ref} from "vue";
import {translate} from "@/service/translationService.js";
import apiService from '@/service/apiService.js';
import GoogleOneTap from "@/modules/auth/GoogleOneTap.vue";

defineProps<{
  mode: 'login' | 'register'
}>()

const email = ref('')
const password = ref('')

const t = translate
const dialogVisible = ref(true);
const emit = defineEmits(['logged-in', 'google_user', 'close'])

async function login() {
  try {
    const user = {
      birthdate: '1990-01-01',
      zipCode: 1234,
      email:email.value,
      password:password.value,
      authProvider:'LOCAL'
    }
    const response = await apiService.authUser(user);
    const token = (response as any)?.data.token;
    localStorage.setItem('token', token);
    localStorage.setItem('userEmail', email.value);
    emit('logged-in', email.value);
  } catch (err) {
    alert(t('ERROR_AUTHENTIFICATION_ERROR'));
  }
}

async function register() {
  try {
    const user = {
      birthdate: '1990-01-01',
      zipCode: 1234,
      email:email.value,
      password:password.value,
      authProvider:'LOCAL'
    }
    const res:any = await apiService.registerLokalUser(user);

    if (res.status === 200) {
      localStorage.setItem('token', res.data.token);
      localStorage.setItem('userEmail', res.data.value);
      emit('logged-in', res.data);
      emit('close');
    }
  } catch (err) {
    alert(t('ERROR_DUPLICATE_EMAIL'));
  }
}

function handleGoogleLoginSuccess(res:any) {
  localStorage.setItem('token', res.data.token);
  localStorage.setItem('userEmail', res.data.email);
  emit('logged-in', res.data);
  emit('close');
}

</script>

<template>
  <v-dialog v-model="dialogVisible" persistent max-width="400">
    <v-card>
      <v-card-title class="d-flex justify-space-between align-center">
        <span>{{ mode === 'register' ? t('LABEL_REGISTRATION') : t('LABEL_LOGIN') }}</span>
        <v-btn icon @click="dialogVisible = false; emit('close')">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-card-text>
        <v-form @submit.prevent="mode === 'register' ? register() : login()">
          <v-text-field
            v-model="email"
            :label="t('LABEL_EMAIL')"
            type="email"
            required
            prepend-inner-icon="mdi-email"
          ></v-text-field>

          <v-text-field
            v-model="password"
            :label="t('LABEL_PASSWORD')"
            type="password"
            required
            prepend-inner-icon="mdi-lock"
          ></v-text-field>

          <v-btn
            type="submit"
            class="mt-3"
            color="success"
            block
          >
            {{ mode === 'register' ? '📧 ' + t('LABEL_CONTINUE_WITH_EMAIL') : t('LABEL_CONFIRM') }}
          </v-btn>
        </v-form>

        <v-divider class="my-4">{{ t('LABEL_OR_CONTINUE_WITH') }}</v-divider>

        <GoogleOneTap :onLogin="handleGoogleLoginSuccess" />
      </v-card-text>
    </v-card>
  </v-dialog>
</template>



<style scoped lang="scss" >
</style>
