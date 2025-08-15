<template>
  <el-empty :description="description">
    <template slot="image">
      <svg-icon icon-class="empty" :style="{fontSize: imageSize + 'px'}"/>
    </template>
    <template slot="description">
      <slot name="description"></slot>
    </template>
  </el-empty>
</template>
<script>
export default {
  name: 'Empty',
  props: {
    imageSize: {
      type: Number,
      default: 120
    },
    description: {
      type: String,
      default: '暂无数据'
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-empty__description{
  margin-top: 0 !important;
}
</style>
