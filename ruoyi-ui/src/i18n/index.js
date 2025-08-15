import Vue from 'vue'
import VueI18n from 'vue-i18n'
// 引入自定义的各个语言配置文件
import zh from './langs/zh';
import en from './langs/en';
//引入UI框架语言配置---elementui
import ElementLocale from 'element-ui/lib/locale'
import enLocale from 'element-ui/lib/locale/lang/en'
import zhLocale from 'element-ui/lib/locale/lang/zh-CN'
Vue.use(VueI18n)

const i18n = new VueI18n({
    locale: localStorage.getItem('language') || "en",
    messages: {
        // 中文语言包
        'zh-CN': {
            ...zh,
            ...zhLocale
        },
        //英文语言包
        'en': {
            ...en,
            ...enLocale
        }
    },
    globalInjection: true,
    fallbackLocale: 'en',
});

export default i18n

