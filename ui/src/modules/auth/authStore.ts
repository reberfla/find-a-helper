import { ref } from 'vue'

const isLoggedIn = ref<boolean>(!!localStorage.getItem('token'))
const userEmail = ref<string | null>(localStorage.getItem('userEmail'))
const userName = ref(localStorage.getItem('userName') || 'User');
const userAvatar = ref(localStorage.getItem('userAvatar') || 'https://www.gravatar.com/avatar?d=mp');

function login(token: string, email: string, avatar:any='', name:string='') {
  localStorage.setItem('token', token)
  localStorage.setItem('userEmail', email)
  localStorage.setItem('userEmail', avatar)
  localStorage.setItem('userEmail', name)
  isLoggedIn.value = true
  userEmail.value = email
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
