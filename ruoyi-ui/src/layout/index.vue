<template>
  <div :class="classObj" class="app-wrapper" :style="{'--current-color': theme}">
      <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>
      <sidebar v-if="!sidebar.hide" class="sidebar-container"/>
      <div :class="{hasTagsView:needTagsView,sidebarHide:sidebar.hide}" class="main-container">
        <el-scrollbar>
          <div :class="{'fixed-header':fixedHeader}">
            <navbar/>
            <tags-view v-if="needTagsView"/>
          </div>
          <div class="top-star"></div>
          <div class="bottom-star"></div>
          <app-main/>
          <right-panel>
            <settings/>
          </right-panel>
        </el-scrollbar>
      </div>
  </div>
</template>

<script>
import RightPanel from '@/components/RightPanel'
import { AppMain, Navbar, Settings, Sidebar, TagsView } from './components'
import ResizeMixin from './mixin/ResizeHandler'
import { mapState } from 'vuex'
import variables from '@/assets/styles/variables.scss'

export default {
  name: 'Layout',
  components: {
    AppMain,
    Navbar,
    RightPanel,
    Settings,
    Sidebar,
    TagsView
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapState({
      theme: state => state.settings.theme,
      sideTheme: state => state.settings.sideTheme,
      sidebar: state => state.app.sidebar,
      device: state => state.app.device,
      needTagsView: state => state.settings.tagsView,
      fixedHeader: state => state.settings.fixedHeader
    }),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    },
    variables() {
      return variables;
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/assets/styles/mixin.scss";
  @import "~@/assets/styles/variables.scss";

  .top-star{
    position: absolute;
    top: 1vh;
    right: 0;
    width: 96%;
    height: 32vh;
    background: url('~@/assets/images/top_star.png') no-repeat center center;
    background-size: contain;
    pointer-events: none;
    z-index: 0;
  }

  .bottom-star{
    position: absolute;
    bottom: 10vh;
    left: 1vh;
    width: 32vh;
    height: 32vh;
    background: url('~@/assets/images/bottom_star.png') no-repeat center center;
    background-size: contain;
    pointer-events: none;
    opacity: 0.71;
    z-index: 1000;
  }

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    .el-scrollbar{
      height: 100%;
    }

    ::v-deep .el-scrollbar__bar.is-vertical {
      z-index: 10;
    }

    ::v-deep .el-scrollbar__wrap {
      overflow-x: hidden;
    }

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$base-sidebar-width});
    transition: width 0.28s;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 72px);
  }

  .sidebarHide .fixed-header {
    width: 100%;
  }

  .mobile .fixed-header {
    width: 100%;
  }
</style>
