<template>
  <div
    :class="{'has-logo':showLogo}"
    :style="{ background: settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }"
  >
    <div class="sidebar-background">

    </div>
    <logo
      v-if="showLogo"
      :collapse="isCollapse"
    />
    <el-scrollbar
      :class="settings.sideTheme"
      wrap-class="scrollbar-wrapper"
      wrap-style="overflow-x:hidden;"
      style="height: calc(100% - 150px);"
    >
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="/*settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground*/'#FFFFFF00'"
        :text-color="settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
        :unique-opened="true"
        :active-text-color="settings.theme"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="(route, index) in sidebarRouters"
          :key="route.path  + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import {mapGetters, mapState} from "vuex";
import Logo from "./Logo";
import Feedback from "./Feedback";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";

export default {
  components: {SidebarItem, Logo, Feedback},
  computed: {
    ...mapState(["settings"]),
    ...mapGetters(["sidebarRouters", "sidebar"]),
    activeMenu() {
      const route = this.$route;
      const {meta, path} = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.sidebar.opened;
    }
  },
  // created() { //重点，找到你的所有想展开的菜单，将要展开的一级路由地址写在此数组里面
  //   this.arr=['/system','/tool','/data','/AllInOne','/ct','/paper','/room','/school','/institution1']
  // },
};
</script>
<style scoped lang="scss">
::v-deep .el-scrollbar__bar.is-vertical {
  width: 0px;
}

.feedback-card {
  background-color: beige;
  height: 50px;
}

.sidebar-background{
  position: absolute;
  top: 120px;
  left:0;
  width: 256px;
  height: calc(100% - 120px);
  background-image: url("~@/assets/images/sidebar_background.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: left bottom;
}
</style>
