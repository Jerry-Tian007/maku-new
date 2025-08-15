<template>
    <div style="padding:0 3px;display: flex;flex-direction: column;">
      <div :class="{'line-up': true, 'is-end': isEnd}"></div>
      <div :class="{'line-down': true, 'is-end': isEnd}"></div>
    </div>
</template>
<script>
export default {
  name: "MenuLine",
  props: {
    isEnd: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {

    }
  },
}
</script>

<style lang="scss" scoped>
.line-up {
  width:100%;
  flex: 1;
  border: 2px solid #5E6992;
  border-top-width: 0;
  border-right-width: 0;

  &.is-end{
    border-radius: 0 0 0 6px;
  }
}

.line-down {
  width:100%;
  flex: 1;
  border-left: 2px solid #5E6992;

  &.is-end{
    visibility: hidden;
  }
}
</style>
