import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'

import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

import { loadTranslations, setLanguage } from '@/service/translationService'
import translationPlugin from '@/plugins/translationPlugin.ts'
import '@mdi/font/css/materialdesignicons.css'

await loadTranslations()
setLanguage('de')

const vuetify = createVuetify({
  components,
  directives,
})

const app = createApp(App)

app.use(router)
app.use(vuetify)
app.use(translationPlugin)
app.mount('#app')
