<script lang="ts" setup>
import { ref } from 'vue'
import { translate } from '@/service/translationService.js'
import apiService from '@/service/apiService.js'
import GoogleOneTap from '@/modules/auth/GoogleOneTap.vue'
import SnackBar from '@/components/Snackbar.vue'
import { useAuth } from '@/service/userAuthService.ts'
import { UserModel } from '@/models/UserModel.ts'
import ConfirmDialog from '@/components/ConfirmDialog.vue'

const { login } = useAuth()

const snackBar = ref<InstanceType<typeof SnackBar> | null>(null)

const props = defineProps<{ mode: 'login' | 'register' }>()
const mode = ref(props.mode)
const email = ref('')
const name = ref('')
const password = ref('')

const confirmDialogVisible = ref(false)
const confirmDialogTitle = ref('')
const confirmDialogMessage = ref('')
const confirmDialogConfirmLabel = ref('')
const confirmDialogCancelLabel = ref('')
let onConfirmFn = () => {}
let onCancelFn = () => {}

const t = translate
const dialogVisible = ref(true)
const emit = defineEmits(['logged-in', 'google_user', 'close'])

const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+{}\[\]:;<>,.?~\\/-]).{8,}$/

function isPasswordStrong(password: string): boolean {
  return passwordRegex.test(password)
}

const passwordRules = [(v: string) => isPasswordStrong(v) || t('ERROR_PASSWORD_WEAK')]

function handleGoogleLoginSuccess(res: any) {
  login(res)
  dialogVisible.value = false
  emit('logged-in', true)
  emit('close')
}

async function handleLogin() {
  const user = new UserModel(email.value, name.value, password.value).toAuthenticationDto()
  apiService
    .authUser(user)
    .then((res: any) => {
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

async function handleRegister() {
  if (!isPasswordStrong(password.value)) {
    snackBar.value?.show(t('ERROR_PASSWORD_WEAK'))
    return
  }
  const user = new UserModel(email.value, name.value, password.value).toAuthenticationDto()
  apiService
    .registerLokalUser(user)
    .then((res: any) => {
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
  handleLogin()
}

function confirmRegistration() {
  confirmDialogVisible.value = false
  mode.value = 'register'
  handleRegister()
}

function cancelAuth() {
  confirmDialogVisible.value = false
}
</script>

<template>
  <v-dialog v-model="dialogVisible" max-width="400" persistent>
    <v-card>
      <v-card-title class="d-flex justify-space-between align-center">
        <span>{{ mode === 'register' ? t('LABEL_REGISTRATION') : t('LABEL_LOGIN') }}</span>
        <v-btn icon @click="((dialogVisible = false), emit('close'))">
          <v-icon>close</v-icon>
        </v-btn>
      </v-card-title>

      <v-card-text>
        <v-form @submit.prevent="mode === 'register' ? handleRegister() : handleLogin()">
          <v-text-field
            v-model="email"
            :label="t('LABEL_EMAIL')"
            prepend-inner-icon="email"
            required
            type="email"
          ></v-text-field>

          <v-text-field
            v-model="password"
            :label="t('LABEL_PASSWORD')"
            :rules="mode === 'register' ? passwordRules : []"
            prepend-inner-icon="lock"
            required
            type="password"
          ></v-text-field>

          <v-btn block class="mt-3" color="success" type="submit">
            {{ mode === 'register' ? '📧 ' + t('LABEL_CONTINUE_WITH_EMAIL') : t('LABEL_CONFIRM') }}
          </v-btn>
        </v-form>

        <v-divider class="my-4">{{ t('LABEL_OR_CONTINUE_WITH') }}</v-divider>

        <GoogleOneTap :onLogin="handleGoogleLoginSuccess" />
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
