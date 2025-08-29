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
import { md, aliases } from 'vuetify/iconsets/md'

await loadTranslations()
setLanguage('de')

const vuetify = createVuetify({
  components: {
    VDateInput,
    ...components,
  },
  directives,
  icons: {
    defaultSet: 'md',
    aliases,
    sets: {
      md,
    },
  },
})

const app = createApp(App)

app.use(router)
app.use(vuetify)
app.mount('#app')
