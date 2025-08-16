import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'

import * as components from 'vuetify/components'
import { VDateInput } from 'vuetify/labs/VDateInput'
import * as directives from 'vuetify/directives'

import { loadTranslations, setLanguage } from '@/service/translationService'
import translationPlugin from '@/plugins/translationPlugin.ts'
import { aliases } from 'vuetify/iconsets/fa'
import { md } from 'vuetify/iconsets/md'
import '@mdi/font/css/materialdesignicons.css'

await loadTranslations()
setLanguage('de')

const vuetify = createVuetify({
  components: {
    VDateInput,
    ...components,
  },
  directives,
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      md,
    },
  },
})

const app = createApp(App)

app.use(router)
app.use(vuetify)
app.use(translationPlugin)
app.mount('#app')
