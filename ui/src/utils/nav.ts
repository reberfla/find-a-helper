import { useRouter } from 'vue-router'
export function useNav() {
  const router = useRouter()
  const toTasksWith = (slug?: string) =>
    router.push({ name: 'tasks', query: slug ? { q: slug } : undefined })
  return { toTasksWith }
}
