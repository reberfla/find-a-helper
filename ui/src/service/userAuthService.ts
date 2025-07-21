import { computed, ref } from 'vue'

const storedUser = localStorage.getItem('user')
const token = ref<string | null>(storedUser ? JSON.parse(storedUser).token : null)
const isLoggedIn = computed(() => !!token.value)

function login(payload: any) {
  console.log(payload)
  token.value = payload.token.JWT
  const avatar = payload.imgBlob
    ? `data:image/png;base64,${payload.imgBlob}`
    : payload.imgUrl || 'https://www.gravatar.com/avatar?d=mp'
  const user = {
    token: payload.token.JWT,
    id: payload.userId,
    avatar: avatar,
    name: payload.name,
    email: payload.email,
  }
  localStorage.setItem('user', JSON.stringify(user))
}

function logout() {
  token.value = null
  localStorage.removeItem('user')
}

function getCurrentUser(): {
  id: number
  email: string
  name: string
  token: string
  avatar: string
} | null {
  const raw = localStorage.getItem('user')
  if (!raw) return null

  try {
    console.log(JSON.parse(raw))
    return JSON.parse(raw)
  } catch (e) {
    console.error('User JSON parse error:', e)
    return null
  }
}

function getCurrentUserId(): number | null {
  return getCurrentUser()?.id || null
}

function getCurrentUserEmail(): string | null {
  return getCurrentUser()?.email || null
}

function getCurrentUserAvatar(): string {
  return getCurrentUser()?.avatar || 'https://www.gravatar.com/avatar?d=mp'
}

export function useAuth() {
  return {
    token,
    isLoggedIn,
    login,
    logout,
    getCurrentUserId,
    getCurrentUserEmail,
    getCurrentUserAvatar,
    getCurrentUser,
  }
}
