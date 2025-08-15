<template>
  <div
    class="sidebar-logo-container"
    :class="{ 'collapse': collapse ,'invisible': invisible}"
    :style="{ backgroundColor: '#FFFFFF00'}"
  >
    <transition enter-active-class="animate__animated animate__jackInTheBox">
      <router-link
        v-show="collapse"
        key="collapse"
        class="sidebar-logo-link"
        to="/"
      >
        <img
          :src="logoIcon"
          class="sidebar-collapse-logo"
          style="border-radius: 10px;"
        />
        <!-- <img v-if="sideTheme !== 'theme-dark'" :src="logoIconDark" class="sidebar-collapse-logo" /> -->
        <!--<h1 v-else class="sidebar-title"
          :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">{{
              title
          }} </h1> -->
      </router-link>
    </transition>
    <transition enter-active-class="animate__animated animate__bounceInDown">
      <router-link
        v-show="!collapse"
        key="expand"
        class="sidebar-logo-link"
        to="/"
      >
        <!--        <img
                  :src="/*logo*/"
                  class="sidebar-logo"
                />-->
        <div class="flex-layout center-center gap6">
          <img :src="/*logoPlaceholder*/logoIcon"
               class="sidebar-logo"
               style="width:53px;height:53px;border-radius: 10px;"/>
          <div class="sidebar-logo-title">
            <div>南洋万邦</div>
            <div class="sub">星宫AI平台</div>
          </div>
        </div>
        <!-- <img v-if="sideTheme !== 'theme-dark'" :src="logoDark" class="sidebar-logo" /> -->
      </router-link>
    </transition>
  </div>
</template>

<script>
import logoImg from '@/assets/logo/logo1.png'
import logoImgDark from '@/assets/logo/logoDark1.png'
import logoIcon from '@/assets/logo/logoIcon1.png'
import logoIconDark from '@/assets/logo/logoIconDark1.png'
import logoPlaceholder from '@/assets/logo/logo-placeholder.jpg'
import variables from '@/assets/styles/variables.scss'
import 'animate.css';

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },

  computed: {
    variables() {
      return variables;
    },
    sideTheme() {
      return this.$store.state.settings.sideTheme
    },
  },
  data() {
    return {
      title: 'DA-Linker管理平台',
      logo: logoImg,
      logoDark: logoImgDark,
      logoIcon: logoIcon,
      logoIconDark: logoIconDark,
      logoPlaceholder: logoPlaceholder,
      invisible: true,
    }
  },
  watch: {
    $route(to, from) {
      if (this.$route.path === '/index') {
        this.invisible = true
      } else {
        this.invisible = false
      }
    }
  },
  mounted() {
    if (this.$route.path !== '/index') {
      this.invisible = false;
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar-logo-container {
  position: relative;
  width: 100%;
  margin-top: 20px;
  height: 120px;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;
  transition: all 0.2s;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo {
      width: 175px;
      margin-top: 0px;
      margin-right: 10px;
      vertical-align: middle;
      color: #fff;
    }

    & .sidebar-collapse-logo {
      width: 40px;
      height: 40px;
      margin: 16px 0px;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
    }
  }

  &.invisible {
    opacity: 0;
    transition: all 0.2s;
  }
}

//修改菜单标题title颜色
::v-deep .sidebar-title {
  color: #fff !important;
}

</style>
