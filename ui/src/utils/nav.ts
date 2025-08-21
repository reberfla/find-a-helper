import { useRouter } from 'vue-router'
import { ref } from 'vue'
export function useNav() {
  const router = useRouter()
  const toTasksWith = (slug?: string) =>
    router.push({ name: 'tasks', query: slug ? { category: slug } : undefined })
  return { toTasksWith }
}

export const drawer = ref(false)
