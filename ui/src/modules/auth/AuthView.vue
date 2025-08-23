<script lang="ts" setup>
import { ref, shallowRef } from 'vue'
import { translate } from '@/service/translationService.js'
import apiService from '@/service/apiService.js'
import GoogleOneTap from '@/modules/auth/GoogleOneTap.vue'
import SnackBar from '@/components/Snackbar.vue'
import { useAuth } from '@/service/userAuthService.ts'
import {
  type AuthProvider,
  type AuthRequest,
  type AuthResponse,
  UserModel,
} from '@/models/UserModel.ts'
import ConfirmDialog from '@/components/ConfirmDialog.vue'

const { login } = useAuth()

const snackBar = ref<InstanceType<typeof SnackBar> | null>(null)

const props = defineProps<{ mode: 'login' | 'register' }>()
const mode = ref(props.mode)
const birthdate = ref(new Date())

const user = ref<Partial<UserModel>>({})

const confirmDialogVisible = ref(false)
const confirmDialogTitle = ref('')
const confirmDialogMessage = ref('')
const confirmDialogConfirmLabel = ref('')
const confirmDialogCancelLabel = ref('')
let onConfirmFn = () => {}
let onCancelFn = () => {}

const t = translate
const dialogVisible = ref(true)
const googleRegister = ref(false)
const emit = defineEmits(['logged-in', 'google_user', 'close'])

const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+{}\[\]:;<>,.?~\\/-]).{8,}$/

function isPasswordStrong(password: string): boolean {
  // return passwordRegex.test(password)
  return true
}

const passwordRules = [(v: string) => isPasswordStrong(v) || t('ERROR_PASSWORD_WEAK')]

function handleGoogleLoginSuccess(res: AuthResponse) {
  login(res)
  dialogVisible.value = false
  emit('logged-in', true)
  emit('close')
}

async function handleLogin(authProvider: AuthProvider) {
  apiService
    .authUser({
      email: user.value.email || '',
      password: user.value.password || '',
      authProvider: authProvider || 'LOCAL',
    } as AuthRequest)
    .then((res: AuthResponse) => {
      login(res)
      dialogVisible.value = false
      emit('logged-in', true)
      emit('close')
    })
    .catch((e: any) => {
      console.error(e)
      if (e?.status === 404) {
        mode.value = 'register'
        showConfirmDialog(
          t('CONFIRM_REGISTER_TITLE'),
          t('CONFIRM_REGISTER_MESSAGE'),
          t('YES'),
          t('CANCEL'),
          confirmRegistration,
          cancelAuth,
        )
      } else {
        snackBar.value?.show(t('ERROR_AUTHENTIFICATION_ERROR'))
      }
    })
}

async function handleRegister(authProvider: AuthProvider) {
  user.value.authProvider = authProvider
  apiService
    .registerUser(user.value)
    .then((res: AuthResponse) => {
      login(res)
      dialogVisible.value = false
      emit('logged-in', true)
      emit('close')
    })
    .catch((e: any) => {
      console.error(e)
      if (e.status === 409) {
        showConfirmDialog(
          t('CONFIRM_LOGIN_TITLE'),
          t('CONFIRM_LOGIN_MESSAGE'),
          t('YES'),
          t('CANCEL'),
          confirmLogin,
          cancelAuth,
        )
      } else {
        snackBar.value?.show(t('ERROR_REGISTRATION_FAILED'))
      }
    })
}

function showConfirmDialog(
  title: string,
  message: string,
  confirmLabel: string,
  cancelLabel: string,
  onConfirm: () => void,
  onCancel: () => void,
) {
  confirmDialogTitle.value = title
  confirmDialogMessage.value = message
  confirmDialogConfirmLabel.value = confirmLabel
  confirmDialogCancelLabel.value = cancelLabel
  onConfirmFn = onConfirm
  onCancelFn = onCancel
  confirmDialogVisible.value = true
}

function confirmLogin() {
  confirmDialogVisible.value = false
  mode.value = 'login'
}

function confirmRegistration() {
  confirmDialogVisible.value = false
  mode.value = 'register'
}

function cancelAuth() {
  confirmDialogVisible.value = false
}

function handleGoogleRegistration(userData: Partial<UserModel>) {
  user.value = userData
  googleRegister.value = true
}
</script>

<template>
  <v-dialog v-model="dialogVisible" max-width="400" persistent>
    <v-card v-if="!googleRegister">
      <v-card-title class="d-flex justify-space-between align-center">
        <span>{{ mode === 'register' ? 'Registrieren' : 'Anmelden' }}</span>
        <v-btn icon @click="((dialogVisible = false), emit('close'))">
          <v-icon>close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-text>
        <v-form
          @submit.prevent="mode === 'register' ? handleRegister('LOCAL') : handleLogin('LOCAL')"
        >
          <v-text-field
            v-model="user.email"
            label="E-Mail"
            prepend-inner-icon="email"
            required
            type="email"
          ></v-text-field>
          <div v-if="mode === 'register'">
            <v-text-field
              v-model="user.name"
              label="Name"
              prepend-inner-icon="account_circle"
              required
              type="text"
            ></v-text-field>
            <v-text-field
              v-model="user.zipCode"
              label="PLZ Wohnort"
              prepend-inner-icon="home"
              required
              type="text"
            >
            </v-text-field>
            <v-date-input
              v-model="birthdate"
              label="Geburtsdatum"
              prepend-inner-icon="cake"
              prepend-icon=""
              required
              @update:modelValue="
                () => {
                  user.birthdate = birthdate.toISOString().split('T')[0]
                }
              "
            >
            </v-date-input>
          </div>
          <v-text-field
            v-model="user.password"
            label="Passwort"
            :rules="mode === 'register' ? passwordRules : []"
            prepend-inner-icon="lock"
            required
            type="password"
          ></v-text-field>
          <v-btn block class="mt-3" color="success" type="submit">
            {{ mode === 'register' ? '📧 ' + 'Registrieren' : 'Anmelden' }}
          </v-btn>
        </v-form>

        <v-divider class="my-4">oder</v-divider>

        <GoogleOneTap
          :onLogin="handleGoogleLoginSuccess"
          @register-user="handleGoogleRegistration"
        />
        <v-btn @click="googleRegister = true">Show Google Registration Finish</v-btn>
      </v-card-text>
    </v-card>

    <v-card v-if="googleRegister">
      <v-card-title class="d-flex justify-center align-center mt-2">
        <span>Zusätzliche Informationen benötigt</span>
      </v-card-title>
      <v-card-text>
        <v-text-field
          v-model="user.zipCode"
          label="PLZ Wohnort"
          prepend-inner-icon="home"
          required
          type="text"
        >
        </v-text-field>
        <v-date-input
          v-model="birthdate"
          label="Geburtsdatum"
          prepend-inner-icon="cake"
          prepend-icon=""
          required
          @update:modelValue="
            () => {
              user.birthdate = birthdate.toISOString().split('T')[0]
            }
          "
        >
        </v-date-input>
        <v-btn
          block
          class="mt-3"
          color="success"
          type="submit"
          @click="handleRegister('GOOGLE')"
          @register-user="handleGoogleRegistration"
        >
          Registrierung abschließen
        </v-btn>
        <v-btn block class="mt-3" color="error" @click="((dialogVisible = false), emit('close'))"
          >Abbrechen
        </v-btn>
      </v-card-text>
    </v-card>
  </v-dialog>

  <ConfirmDialog
    v-model:visible="confirmDialogVisible"
    :title="confirmDialogTitle"
    :message="confirmDialogMessage"
    :confirm-label="confirmDialogConfirmLabel"
    :cancel-label="confirmDialogCancelLabel"
    :onConfirm="onConfirmFn"
    :onCancel="onCancelFn"
  />

  <snackBar ref="snackBar" />
</template>

<style lang="scss" scoped></style>
