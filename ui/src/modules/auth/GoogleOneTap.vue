<script lang="ts" setup>
import apiService from '@/service/apiService.js'
import { onMounted } from 'vue'
import { translate } from '@/service/translationService.js'
import { UserModel } from '@/models/UserModel.ts'

declare global {
  interface Window {
    google: any
  }
}

const props = defineProps({
  onLogin: Function,
})

const clientId = '1030506683349-po5p0i1593ap5vlur6ffivpcfefka4d7.apps.googleusercontent.com'
const t = translate

defineExpose({
  triggerPrompt,
})

function triggerPrompt() {
  if (!window.google || !window.google.accounts || !window.google.accounts.id) {
    const script = document.createElement('script')
    script.src = 'https://accounts.google.com/gsi/client'
    script.async = true
    script.defer = true

    script.onload = () => {
      setTimeout(() => {
        if (window.google?.accounts?.id) {
          initializeGoogleSignIn()
        } else {
          console.error('Google API still not available after script load.')
        }
      }, 200)
    }

    document.head.appendChild(script)
  } else {
    initializeGoogleSignIn()
  }
}

function initializeGoogleSignIn() {
  window.google.accounts.id.initialize({
    client_id: clientId,
    callback: handleGoogleLogin,
  })

  window.google.accounts.id.renderButton(document.querySelector('.g_id_signin'), {
    theme: 'outline',
    size: 'large',
    type: 'standard',
    text: 'continue_with',
  })

  window.google.accounts.id.prompt()
}

function handleGoogleLogin(response: any) {
  const token = response.credential
  const payload: any = parseJwt(token)
  console.log(payload)

  const user = new UserModel(
    payload?.email,
    payload?.name,
    '',
    -1,
    token,
    'GOOGLE',
  ).toAuthenticationDto()
  apiService
    .authUser(user)
    .then((res: any) => {
      if (props.onLogin) {
        props.onLogin(res)
      }
    })
    .catch((e: any) => {
      console.error(e)
    })
}

function parseJwt(token: string): string {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload || ''
  } catch {
    return ''
  }
}

onMounted(() => {
  triggerPrompt()
})
</script>

<template>
  <div>
    <div id="g_id_onload" :data-client_id="clientId" data-auto_prompt="true"></div>
    <div class="g_id_signin" data-type="standard"></div>
  </div>
</template>

<style scoped></style>
