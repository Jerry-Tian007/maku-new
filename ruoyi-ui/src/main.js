import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive
import plugins from './plugins' // plugins
import {download, jsonDownload} from '@/utils/request'

import './assets/icons' // icon
import './permission' // permission control
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree, copyContent } from "@/utils/ruoyi";
// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'

import JsonViewer from 'vue-json-viewer'

// 引入 json-viewer 样式（可选，但推荐）
import 'vue-json-viewer/style.css'

Vue.use(JsonViewer)

// 字典数据组件
import DictData from '@/components/DictData'

import mavonEditor from 'mavon-editor'
// import 'mavon-editor/dist/css/index.css'

import MenuLine from "@/components/menuLine/index.vue";

import Empty from "@/components/Empty/index.vue";  // npm install emoji-regex

import Vant from 'vant';
import 'vant/lib/index.css';

import i18n from '@/i18n/index'
Vue.use(Element,
  {
    i18n: (key, value) => i18n.t(key, value)
  }
)

// 移除el-radio的aria-hidden属性
Vue.directive('removeAriaHidden', {
  bind(el, binding) {
    const ariaEls = el.querySelectorAll('.el-radio__original')
    ariaEls.forEach((item) => {
      item.removeAttribute('aria-hidden')
    })
  }
})

Vue.use(Vant);

import MarkdownItVue from 'markdown-it-vue'
import 'markdown-it-vue/dist/markdown-it-vue.css'

Vue.component('MarkdownItVue', MarkdownItVue);

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.jsonDownload = jsonDownload
Vue.prototype.handleTree = handleTree
Vue.prototype.copyContent = copyContent

// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)
Vue.component('ImagePreview', ImagePreview)
Vue.component('MenuLine', MenuLine)
Vue.component('Empty', Empty)

Vue.use(directive)
Vue.use(plugins)
Vue.use(VueMeta)
Vue.use(mavonEditor)
DictData.install()

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})


import emojiRegex from 'emoji-regex';
Vue.directive('clearEmoij', {
  bind(el, binding, vnode, oldVnode) {
    const regex = emojiRegex();
    const obj = el.querySelectorAll('.el-input__inner,.el-textarea__inner')[0];
    const zclearNoNum = function(e) {
      if (e.target.composing) return;
      const match = regex.exec(obj.value); // 也可以直接用正则表达式判断
      if (match) {
        /* for (let i=0;i<match.length;i++) {
          obj.value = obj.value.replace(match[i],"");
        }*/
        obj.value = obj.value.replace(/(\ud83c[\udc00-\udfff])|(\ud83d[\udc00-\udfff])|(\ud83e[\udc00-\udfff])|[\u2100-\u32ff]|[\u0030-\u007f][\u20d0-\u20ff]|[\u0080-\u00ff]/g, '');
        zclearNoNum(e);
      }
      // 触发v-model的更新
      obj.dispatchEvent(new Event('input'));
    }
    const zblur = function(e) {
      zclearNoNum(e);
      setTimeout(() => {
        const match = regex.exec(obj.value);
        if (match) {
          obj.value = obj.value.replace(/(\ud83c[\udc00-\udfff])|(\ud83d[\udc00-\udfff])|(\ud83e[\udc00-\udfff])|[\u2100-\u32ff]|[\u0030-\u007f][\u20d0-\u20ff]|[\u0080-\u00ff]/g, '');
          zclearNoNum(e);
        }
        // 触发v-model的更新
        obj.dispatchEvent(new Event('change'));
      }, 50)
    }
    obj.onkeyup = zclearNoNum;
    obj.onblur = zblur;


    function onCompositionStart(e) {
      e.target.composing = true
    }
    function onCompositionEnd(e) {
      e.target.composing = false
      obj.dispatchEvent(new Event('change'));
      setTimeout(() => {
        if (obj.value) {
          obj.value = obj.value.replace(/(\ud83c[\udc00-\udfff])|(\ud83d[\udc00-\udfff])|(\ud83e[\udc00-\udfff])|[\u2100-\u32ff]|[\u0030-\u007f][\u20d0-\u20ff]|[\u0080-\u00ff]/g, '');
          zclearNoNum(e);
        }
        // 触发v-model的更新
        obj.dispatchEvent(new Event('change'));
      }, 50)
    }
    obj.addEventListener('compositionstart', onCompositionStart)
    obj.addEventListener('compositionend', onCompositionEnd)
  },
  update(el, binding, vnode, oldVnode) {}
})


Vue.config.productionTip = false


new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
