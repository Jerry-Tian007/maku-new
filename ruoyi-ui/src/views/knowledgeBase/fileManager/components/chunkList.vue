<template>
  <div style="height: 80vh;overflow: auto;">
    <div v-for="(item, index) in data" :key="index" class="chunk-item">
      <div class="flex-layout align-center gap4">
        <span class="chunk-item__label">{{ $t('fileManager.chunk') }}-{{ item.title_index }}</span>
        <span class="chunk-item__label">{{ $t('fileManager.level', {level: item.title_level}) }}</span>
      </div>
      <div class="pd-v8 pd-h2">
        {{ item.title_content }}
      </div>
      <div class="pd-v4 content-list" v-if="item.contentArray">
        <div class="content-detail-button" @click="handleShowContents(item)">
          <i class="el-icon-arrow-down" v-if="item.showContents"/>
          <i class="el-icon-arrow-right" v-else/>
          <span style="margin-left: 4px;" class="fw-6">
            {{ item.contentArray.length || 0 }} {{ $t('fileManager.subchunk') }}
          </span>
        </div>
        <el-collapse-transition>
          <div class="content-container" v-show="item.showContents">
            <div class="content-item flex-layout gap8" v-for="content in item.contentArray" :key="content.key">
              <div class="flex-layout gap4">
                <div class="content-key">C-{{ content.key }}</div>
              </div>
              <div class="flex-1">
                {{ content.content }}
                <span class="content-key">Page {{ content.page }}</span>
              </div>
            </div>
          </div>
        </el-collapse-transition>
      </div>
      <div class="pd-v4 content-list" style="margin-top: 4px;" v-if="item['chapter_summary'] || item['chapter_summary:']">
        <div class="content-detail-button" @click="handleShowSummary(item)">
          <i class="el-icon-arrow-down" v-if="item.showSummary"/>
          <i class="el-icon-arrow-right" v-else/>
          <span style="margin-left: 4px;" class="fw-6">
            {{ $t('fileManager.summary') }}
          </span>
        </div>
        <el-collapse-transition>
          <div class="content-container" v-show="item.showSummary">
            {{ item['chapter_summary'] || item['chapter_summary:'] }}
          </div>
        </el-collapse-transition>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'ChunkList',
  props: {
    chunks: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      data: []
    }
  },
  watch: {
    chunks: {
      handler(newVal) {
        this.formatData(newVal);
      },
      immediate: true
    }
  },
  mounted() {
    this.formatData(this.chunks);
  },
  methods: {
    formatData(list) {
      let data = [];
      list.forEach(item => {
        let obj = {...item};
        // content 和 page_number 的键值对处理 (两者有对应关系)
        const contentKeys = Object.keys(obj.content);
        const pageKeys = Object.keys(obj.page_number);
        obj.contentArray = contentKeys.map(key => ({
          key: key,
          content: item.content[key],
          page: item.page_number[key] || 1 // 默认页码为1
        }));
        obj.showContents = false; // 初始化子分段内容显示状态
        obj.showSummary = false; // 初始化总结显示状态
        data.push(obj);
      });

      this.data = data;
    },
    handleShowContents(item) {
      item.showContents = !item.showContents; // 切换子分段内容显示状态
    },
    handleShowSummary(item) {
      item.showSummary = !item.showSummary; // 切换总结显示状态
    },
  },
}
</script>
<style lang="scss" scoped>
.chunk-item__label {
  font-size: 12px;
  background: #EBEEF5;
  color: #909399;
  padding: 4px 8px;
  border-radius: 4px;
}

.chunk-item {
  margin-bottom: 10px;
  border-bottom: 1px solid #EBEEF5;
  padding-bottom: 10px;

  &:last-child {
    border-bottom: none;
    margin-bottom: 0;
  }
}

.content-container {
  padding-bottom: 8px;
}

.content-list {
  background: linear-gradient(to right, #f0f2f4, #FFFFFF00 20%);
  border-radius: 8px;
  padding: 4px 8px;
}

.content-detail-button {
  font-size: 12px;
  cursor: pointer;
  line-height: 24px;
}

.content-item {
  padding: 4px 0 4px 8px;
  border-left: 2px solid #E4E7ED;
  margin-left: 5px;
}

.content-key {
  //小标签样式
  min-width: 36px;
  text-align: center;
  height: fit-content;
  background: #E4E7ED;
  border-radius: 4px;
  padding: 0 4px;
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>
