<template>
  <el-card class="feedback" @click="handleJumpFeedback">
    <div class="line" @click="handleJumpFeedback">
      <div style="display: flex;align-items: center;">
        <i class="el-icon-question icon"/>
        <span v-show="$store.state.app.sidebar.opened" class="title">反馈</span>
      </div>
      <div style="display: flex;align-items: center;">
        <span v-show="$store.state.app.sidebar.opened" style="font-size: 14px;margin-right: 16px;">Feedback</span>
      </div>
    </div>
  </el-card>
</template>

<script>

export default {
  data() {
    return {
      url: 'https://gitee.com/cncabbagedjw/nyllm/issues',
    }
  },
  methods: {
    handleJumpFeedback() {
      window.open(this.url, "_blank");
    }
  }
}
</script>

<style scoped lang="scss">
.feedback {
  width: 100%;
  height: 50px;
  padding: 0px;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;

  .line {
    width: 100%;
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-left: 20px;
    color: #458da5;
    cursor: pointer;

    .icon {
      font-size: 16px;
    }

    .title {
      margin-left: 8px;
      font-size: 14px;
      font-weight: 500;
    }
  }
}

.feedback:hover {
  box-shadow: 0 2px 16px 0 rgba(0, 0, 0, 0.2);
}

::v-deep .el-card__body {
  padding: 0px;
}
</style>
