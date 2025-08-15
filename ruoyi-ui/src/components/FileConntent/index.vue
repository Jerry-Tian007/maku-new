<template>
  <div class="file-content" :title="loading ? '文件处理中..' : file.fileName">
    <svg-icon v-if="status === 'error'" icon-class="file-error" style="width: 72px;height: 72px;"/>
    <template v-else-if="status === 'success'">
      <el-image
        style="width: 72px;height: 72px;background-color: #f6f7fa;"
        :src="url"
        fit="cover"
        :preview-src-list="[url]"
        lazy
        v-if="fileTypes.includes(type)"
      />
      <svg-icon v-else icon-class="file-success" style="width: 72px;height: 72px;"/>
    </template>
    <div v-if="loading" v-loading="true" style="width: 72px;height: 72px;" class="loading-bg">
    </div>
    <div class="file-content-title" :title="file.fileName">
      <span>{{file.fileName}}</span>
    </div>
    <svg-icon icon-class="file-close" class="file-close" @click="handleClose"/>
  </div>
</template>
<script>
export default {
  name: "FileContent",
  props: {
    file: {
      type: Object,
      required: true
    },
    url: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'pdf'
    },
    loading: {
      type: Boolean,
      default: false
    },
    status: {
      type: String,
      default: 'success'
    },
  },
  data() {
    return {
      fileUrl: '',
      fileTypes: ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg']
    }
  },
  mounted() {
    this.fileUrl = this.file.url;
  },
  methods: {
    handleClose() {
      this.$emit('close', this.file);
    }
  }
}
</script>
<style lang="scss" scoped>
.file-content {
  width: 84px;
  height: 96px;
  padding: 4px 6px;
  border-radius: 6px;
  position: relative;

  .file-content-title {
    width: 72px;
    height: 20px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-size: 12px;
    margin-top: 4px;
    text-align: center;
    color: #909399;
  }
}

.loading-bg {
  background: linear-gradient(180deg, #B7F5E6 0%, #53ACB2 100%);
}

.file-close {
  position: absolute;
  right: 0;
  top: 0;
  font-size: 16px;
  z-index: 11;
  cursor: pointer;
}

::v-deep .el-loading-mask {
  z-index: 10;
}
</style>
