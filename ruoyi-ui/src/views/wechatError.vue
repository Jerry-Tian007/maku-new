<template>
  <div style="height:fit-content;position: absolute;top:0;bottom:0;left:0;right:0;margin:auto;padding-bottom: 60px;">
    <el-empty :image-szie="100" :description="text+'，请退出界面并重新进入'"/>
  </div>

</template>

<script>
import axios from "axios";

export default {
  name: "WeChatError",
  data() {
    return {
      text: this.$route.query.type === 'query' ? '参数获取异常' : this.$route.query.type === 'error' ? '登录失败' : '身份信息过期',
    }

  },
  mounted() {

  },
  methods: {
  },
}
</script>
<style lang="scss" scoped>
</style>
