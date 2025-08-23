import { ref, computed } from 'vue'
import router from '@/router'
import type { AuthResponse } from '@/models/UserModel.ts'

const user = ref<{
  id: number
  email: string
  name: string
  token: string
  avatar: string
} | null>(null)

const storedUser = localStorage.getItem('user')
if (storedUser) {
  try {
    user.value = JSON.parse(storedUser)
  } catch (e) {
    console.error('User JSON parse error:', e)
  }
}

const isLoggedIn = computed(() => !!user.value?.token)

function login(payload: AuthResponse) {
  const avatar = payload.imgBlob
    ? `data:image/png;base64,${payload.imgBlob}`
    : payload.imgUrl || 'https://www.gravatar.com/avatar?d=mp'

  user.value = {
    token: payload.jwt,
    id: payload.id,
    avatar,
    name: payload.name,
    email: payload.email,
  }
  localStorage.setItem('user', JSON.stringify(user.value))
}

function logout() {
  user.value = null
  localStorage.removeItem('user')
  router.push('/')
}

function getCurrentUser() {
  return user.value
}

function getCurrentUserAvatar() {
  return user.value?.avatar || 'https://www.gravatar.com/avatar?d=mp'
}

function getCurrentUserEmail() {
  return user.value?.email || null
}

function getCurrentUserId() {
  return user.value?.id || null
}

export function useAuth() {
  return {
    user,
    isLoggedIn,
    login,
    logout,
    getCurrentUser,
    getCurrentUserAvatar,
    getCurrentUserEmail,
    getCurrentUserId,
  }
}
