<template>
  <el-card style="border-radius: 6px;" :body-style="{padding: '12px'}" class="border fs-14"
           :title="(work.nodeName || '工作流') + '\n\n状态：'+ nodeStatus[work.nodeStatus]">
    <div class="flex-layout align-center start-end">
      <div class="flex-layout align-center gap6 sub-color text-over">
        <i class="el-icon-data-analysis"/>
        <span class=" text-over">{{work.nodeName || '-'}}</span>
      </div>

      <div class="fs-18">
        <!-- class="el-icon-time waiting"/>
        <i v-if="work.nodeStatus === 'loading'"-->
        <i v-if="work.nodeStatus === 'waiting'" class="el-icon-loading loading"/>
        <i v-if="work.nodeStatus === 'finished'" class="el-icon-circle-check success"/>
        <i v-if="work.nodeStatus === 'error'" class="el-icon-circle-close error"/>
      </div>

    </div>
  </el-card>
</template>
<script>
export default {
  name: "WorkItem",
  props: {
    work: {
      type: Object,
      default: () => {
        return {
          name: '工作项',
          nodeStatus: 'waiting',
        }
      }
    }
  },
  data() {
    return {
      nodeStatus: {
        success: '已完成',
        error: '执行失败',
        waiting: '等待中',
        loading: '进行中',
      }
    }
  }
}
</script>
<style scoped lang="scss">
.waiting {
  color: #909399;
}
.loading{
  color: #409EFF;
}
.success {
  color: #67c23a;
}
.error {
  color: #f56c6c;
}
</style>
