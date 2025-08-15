<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view v-if="!$route.meta.link" :key="key"/>
      </keep-alive>
    </transition>
    <div class="background-image"></div>
    <iframe-toggle/>
  </section>
</template>

<script>
import iframeToggle from "./IframeToggle/index"

export default {
  name: 'AppMain',
  components: {iframeToggle},
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.path
    },
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header + .app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header + .app-main {
    padding-top: 84px;
  }
}
.background-image{
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('~@/assets/images/background_image.png') no-repeat center center;
  background-size: auto 75vh;
  z-index: -1;
}

.water-marker {
  position: absolute;
  width:fit-content;
  height: fit-content;
  transform: rotate(-24deg);
  font-weight: 500;
  font-size: calc(100vw * 128 /1920);
  color: white;
  text-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  opacity: 0.27;
  pointer-events: none;
  z-index: 1000;
}
</style>
