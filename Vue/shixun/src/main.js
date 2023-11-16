import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css'

import '@/assets/styles/boeder.css'
import '@/assets/styles/reset.css'
import '@/router/permission.js'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'




const app = createApp(App);

app.use(ElementPlus, {
    locale: zhCn,
})


app.use(store).use(ElementPlus)



for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(router).mount('#app')

