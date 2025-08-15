<template>
  <div style="height:fit-content;position: absolute;top:0;bottom:0;left:0;right:0;margin:auto;padding-bottom: 60px;">
    <el-empty :image-szie="100" description="很抱歉，当前账号没有相应的访问权限。">
      <span style="font-size: 14px;color: #909399;margin-top: 10px;">如需访问请联系管理员</span>
    </el-empty>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "PermissionError",
  data() {
    return {
    }

  },
  mounted() {

  },
  methods: {
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-empty__bottom{
  margin-top: 12px;
}
</style>
