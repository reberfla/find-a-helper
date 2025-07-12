import { ref } from 'vue'

const isLoggedIn = ref<boolean>(!!localStorage.getItem('token'))
const userEmail = ref<string | null>(localStorage.getItem('userEmail'))
const userName = ref(localStorage.getItem('name') || 'User');
const userAvatar = ref(localStorage.getItem('avatar') || 'https://www.gravatar.com/avatar?d=mp');

function login(token: string, email: string, avatar:any='', name:string='') {
  localStorage.setItem('token', token)
  localStorage.setItem('userEmail', email)
  localStorage.setItem('avatar', avatar)
  localStorage.setItem('name', name)
  isLoggedIn.value = true
  userEmail.value = email
  userName.value = name
  userAvatar.value = avatar
}

function logout() {
  localStorage.clear()
  isLoggedIn.value = false
  userEmail.value = null
  userEmail.value = null;
  userName.value = '';
  userAvatar.value = '';
}

export function useAuth() {
  return {
    isLoggedIn,
    userEmail,
    userName,
    userAvatar,
    login,
    logout
  }
}
