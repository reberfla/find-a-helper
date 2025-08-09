export {}

declare global {
  interface Window {
    particlesJS: {
      load: (tagId: string, pathOrConfig: string | object, cb?: () => void) => void
    }
  }
}
