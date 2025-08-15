<template>
  <div v-if="!item.hidden">
    <template
      v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest,'item-block': !showLine}">
          <div v-if="showLine" class="menu-content" :style="'display: flex;align-items: center;padding-left:'+(layerNum === 0? '0;' : (layerNum-1)*30 + 'px;')">
              <menu-line style="width: 16px;height: 50px;margin-right: 4px;" class="menu-line" :is-end="isEnd"/>
              <div :class="{'item-block': true}" :style="{flex:1,height:'50px',lineHeight: '50px', paddingLeft: '10px'}">
                <item :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)" :title="onlyOneChild.meta.title"
                      :show-icon="showIcon" :show-line="showLine" :is-end="isEnd"/>
              </div>
          </div>
          <item v-else style="padding-left:0;" :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)" :title="onlyOneChild.meta.title"
                :show-icon="showIcon" :show-line="showLine" :is-end="isEnd" :icon-color="onlyOneChild.meta.iconColor ||(item.meta ?item.meta.iconColor:null)"/>
        </el-menu-item>
      </app-link>
    </template>

    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
      <template slot="title">
<!--        <item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="item.meta.title" :icon-color="item.meta ?item.meta.iconColor:null"/>-->
        <div v-if="showLine" style="display: flex;align-items: center;">
          <menu-line style="width: 16px;height: 50px;margin-right: 4px;" class="menu-line" :is-end="isEnd"/>
          <div :class="{'item-block': true}" :style="{flex:1,height:'50px',lineHeight: '50px', paddingLeft: '10px'}">
            <item :icon="item.meta && item.meta.icon" :title="item.meta.title"
                  :show-icon="showIcon" :show-line="showLine" :is-end="isEnd"/>
          </div>
        </div>
        <item v-else style="padding-left:0;" :icon="item.meta && item.meta.icon" :title="item.meta.title"
              :show-icon="showIcon" :show-line="showLine" :is-end="isEnd" :icon-color="item.meta ?item.meta.iconColor:null"/>
      </template>
      <sidebar-item
        v-for="(child, index) in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        :show-icon="false"
        :show-line="true"
        :is-end="index === item.children.length - 1"
        :layer-num="layerNum+1"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'
import {isExternal} from '@/utils/validate'
import Item from './Item'
import AppLink from './Link'
import FixiOSBug from './FixiOSBug'

export default {
  name: 'SidebarItem',
  components: {Item, AppLink},
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    },
    showIcon: {
      type: Boolean,
      default: true
    },
    showLine: {
      type: Boolean,
      default: false
    },
    isEnd: {
      type: Boolean,
      default: false
    },
    layerNum:{
      type: Number,
      default: 0
    }
  },
  data() {
    this.onlyOneChild = null
    return {}
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      if (!children) {
        children = [];
      }
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = {...parent, path: '', noShowingChildren: true}
        return true
      }

      return false
    },
    resolvePath(routePath, routeQuery) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      if (routeQuery) {
        let query = JSON.parse(routeQuery);
        return {path: path.resolve(this.basePath, routePath), query: query}
      }
      return path.resolve(this.basePath, routePath)
    }
  }
}
</script>
<style scoped lang="scss">
.line-item-block{
  flex:1;height:50px;line-height: 50px;
}
</style>
