<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container"
               @toggleClick="toggleSideBar"/>

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav"/>
    <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav"/>

    <div class="right-menu">
      <el-dropdown class="right-menu-item" @command="handleCommand">
        <span class="el-dropdown-link">
          {{ reqHeader() }}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="a">{{ $t('navbar.zhcn') }}</el-dropdown-item>
          <el-dropdown-item command="b">{{ $t('navbar.en') }}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item"/>

        <screenfull id="screenfull" class="right-menu-item hover-effect"/>

        <!--        <div @click="handleRefreshAuth" :title="getStateInfo(state,'title')" class="right-menu-item hover-effect">
                  <i :class="getStateInfo(state,'icon')" style="font-size: 22px;font-weight: 600;vertical-align: -2px;"/>
                </div>-->
      </template>

      <span class="right-menu-item" style="font-size: 16px;">{{ name }}</span>
      <el-divider direction="vertical"></el-divider>
      <span class="right-menu-item" style="font-size: 16px;">{{ deptName }}</span>
      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <i class="el-icon-caret-bottom"/>
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>
            <span>{{ $t('navbar.recent') }}{{ $store.state.user.nickName }}</span>
          </el-dropdown-item><!--
          <el-dropdown-item divided @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>-->
          <el-dropdown-item divided @click.native="handleRefreshAuth">
            <template v-if="state === 'loading'">
              <span style="margin-right: 4px;">{{ $t('navbar.sync') }}</span><i class="el-icon-loading"/>
            </template>
            <template v-else-if="state === 'success'">
              <span style="margin-right: 4px;">{{ $t('navbar.syncSuccess') }}</span><i class="el-icon-circle-check"/>
            </template>
            <template v-else-if="state === 'error'">
              <span style="margin-right: 4px;">{{ $t('navbar.syncError') }}</span><i class="el-icon-circle-close"/>
            </template>
            <template v-else>
              <span style="margin-right: 4px;">{{ $t('navbar.modelSync') }}</span><i class="el-icon-refresh"/>
            </template>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>{{ $t('navbar.logout') }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import store from "@/store";

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device',
      'name',
      'nickName',
      'deptName',
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  data() {
    return {
      state: 'normal',
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    reqHeader(){
      return localStorage.getItem('language') === 'zh-CN' ? '中文' : 'English'
    },
    handleCommand(command) {
      if (command === 'a') {
        this.$i18n.locale = "zh-CN";
        localStorage.setItem('language', "zh-CN");
        this.$nextTick(() => {
          window.location.reload();
        });
      } else {
        this.$i18n.locale = "en";
        localStorage.setItem('language', "en");
        this.$nextTick(() => {
          window.location.reload();
        });
      }

    },
    async logout() {
      this.$confirm(this.$t('navbar.confirmlogout'), this.$t('navbar.promopt'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => {
      });
    },
    getStateInfo(state, key) {
      switch (state) {
        case 'loading':
          return key === 'icon' ? 'el-icon-loading' : '同步中';
        case 'success':
          return key === 'icon' ? 'el-icon-circle-check' : '同步成功';
        case 'error':
          return key === 'icon' ? 'el-icon-circle-close' : '同步失败';
        default:
          return key === 'icon' ? 'el-icon-refresh' : '模型权限同步';
      }
    },
    handleRefreshAuth() {
      if (this.state === 'loading') {
        return;
      }
      this.state = 'loading';
      store.dispatch('GetBotList').then(() => {
        this.state = 'success';
        this.$message({
          message: '同步成功',
          type: 'success'
        });
        setTimeout(() => {
          this.state = 'normal';
        }, 2000);
      }).catch(err => {
        this.$message({
          message: this.$t('navbar.syncError'),
          type: 'error'
        });
        this.state = 'error';
        setTimeout(() => {
          this.state = 'normal';
        }, 2000);
        console.log(err);
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #ffffff00;
  //box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
    display: flex;
    align-items: center;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 10px;
      height: 100%;
      font-size: 18px;
      color: rgba(35, 56, 83, 0.88);
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}

::v-deep .el-divider--vertical {
  background-color: rgba(79, 79, 79, 0.61);
  height: 14px;
}
</style>
