<script setup lang="ts">
import {ref} from "vue";
import {translate} from "@/service/translationService.js";
import apiService from '@/service/apiService.js';
import GoogleOneTap from "@/modules/auth/GoogleOneTap.vue";
import ErrorSnackbar from '@/components/ErrorSnackbar.vue'

const errorSnackbar = ref<InstanceType<typeof ErrorSnackbar> | null>(null)

const props = defineProps<{ mode: 'login' | 'register' }>()
const mode = ref(props.mode)
const email = ref('')
const password = ref('')
const confirmRegisterDialog = ref(false);

const t = translate
const dialogVisible = ref(true);
const emit = defineEmits(['logged-in', 'google_user', 'close'])

async function login() {
  try {
    const user = {
      email: email.value,
      password: password.value,
      authenticationProvider: 'LOCAL'
    };
    const response = await apiService.authUser(user);
    const token = (response as any)?.data.token;
    localStorage.setItem('token', token);
    localStorage.setItem('userEmail', email.value);
    emit('logged-in', email.value);
    dialogVisible.value = false;
    emit("close")
  } catch (err: any) {
    console.log(err)
    if (err?.status === 404) {
      mode.value="register"
      confirmRegisterDialog.value = true;
    } else {
      errorSnackbar.value?.show(t('ERROR_AUTHENTIFICATION_ERROR'));
    }
  }
}

const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+{}\[\]:;<>,.?~\\/-]).{8,}$/;

function isPasswordStrong(password: string): boolean {
  return passwordRegex.test(password);
}

const passwordRules = [
  (v: string) => isPasswordStrong(v) || t('ERROR_PASSWORD_WEAK')
];




function confirmRegistration() {
  confirmRegisterDialog.value = false;
  register();
}

function cancelRegistration() {
  confirmRegisterDialog.value = false;
}

async function register() {
  if (!isPasswordStrong(password.value)) {
    errorSnackbar.value?.show(t('ERROR_PASSWORD_WEAK'));
    return;
  }

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
          <v-icon>close</v-icon>
        </v-btn>
      </v-card-title>

      <v-card-text>
        <v-form @submit.prevent="mode === 'register' ? register() : login()">
          <v-text-field
            v-model="email"
            :label="t('LABEL_EMAIL')"
            type="email"
            required
            prepend-inner-icon="email"
          ></v-text-field>

          <v-text-field
            v-model="password"
            :label="t('LABEL_PASSWORD')"
            type="password"
            required
            :rules="mode === 'register' ? passwordRules : []"
            prepend-inner-icon="lock"
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
  <ErrorSnackbar ref="errorSnackbar" />

  <v-dialog v-model="confirmRegisterDialog" max-width="400">
    <v-card>
      <v-card-title>{{ t('CONFIRM_REGISTER_TITLE') }}</v-card-title>
      <v-card-text>{{ t('CONFIRM_REGISTER_MESSAGE') }}</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="grey" @click="cancelRegistration">{{ t('LABEL_CANCEL') }}</v-btn>
        <v-btn color="primary" @click="confirmRegistration">{{ t('LABEL_YES_REGISTER') }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <ErrorSnackbar ref="errorSnackbar" />

</template>


<style scoped lang="scss" >
</style>
