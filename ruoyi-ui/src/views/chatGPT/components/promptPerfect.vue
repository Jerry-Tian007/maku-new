<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="12" style="position: relative;">
        <div class="perfect-title">原始提示词</div>
        <el-input
          v-model="originText"
          placeholder="请输入原始提示词，并点击 [生成] 按钮"
          type="textarea"
          :rows="16"
          :disabled="isLoading"
          class="input-area"
          resize="none">
        </el-input>
        <el-button
          style="position: absolute;bottom:10px;right: 20px;"
          size="mini"
          type="primary"
          plain
          :disabled="isLoading || originText === ''"
          @click="handleGenerate"
        >生成
        </el-button>
      </el-col>
      <el-col :span="12" style="position: relative;">
        <div class="perfect-title">优化提示词</div>
        <el-input
          v-model="perfectResult"
          placeholder="请按左侧 [生成] 按钮，生成优化后的提示词"
          type="textarea"
          :rows="16"
          :disabled="isLoading || perfectResult === ''"
          v-loading="isLoading"
          class="input-area"
          resize="none"/>
        <el-button
          style="position: absolute;bottom:10px;right: 20px;"
          size="mini"
          type="success"
          plain
          :disabled="isLoading || perfectResult === ''"
          @click="handleUse"
        >使用
        </el-button>
        <el-button
          style="position: absolute;top:40px;right: 16px;"
          plain
          type="info"
          icon="el-icon-document-copy"
          size="mini"
          circle
          :disabled="isLoading || perfectResult === ''"
          @click="handleDbClickCard(perfectResult)"
        >
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import axios from "axios";
import {getToken} from "@/utils/auth";

export default {
  name: "PromptPerfect",
  props: {
    text: {
      type: String,
      default: '',
    }
  },
  data() {
    return {
      originText: '',
      perfectResult: '',
      isLoading: false,
    };
  },
  watch: {
    text: function (oldVal, newVal){
      this.originText = newVal;
    }
  },
  mounted() {
    this.originText = this.text;
  },
  methods: {
    init(text){
      this.perfectResult = '';
      this.originText = text;
    },
    handleGenerate(){
      let data = {
        "text": this.originText,
        "deptId": this.$store.state.user.deptId,
        "userId": this.$store.state.user.userId
      }
      this.isLoading = true;
      axios.post(process.env.VUE_APP_GPT_URL+'/promptPerfect',
        data,
        {headers:{'Authorization': 'Bearer ' + getToken()}, timeout: 3 * 60 * 1000}).then(res => {
          if (res.data.code === 200){
            this.perfectResult = res.data.data;
          } else {
            this.$message.error(res.data.msg);
            console.log(res.data.msg);
          }
        this.isLoading = false;
      }).catch(err => {
        this.$message.error(err);
        console.log(err);
        this.isLoading = false;
      })
    },
    handleUse(){
      this.$emit('promptPerfect', this.perfectResult);
    },
    handleDbClickCard(item) {
      if (item === '') {
        return;
      }
      navigator.clipboard.writeText(item).then(() => {
        this.$message.success('复制成功');
      }).catch(() => {
        this.$message.warning('复制失败');
      })
    },
  },
};
</script>

<style lang="scss" scoped>
.input-area ::v-deep .el-textarea__inner {
  border: 1px solid #DCDFE6;
  border-radius: 10px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
  font-size: 14px;
  line-height: 1.5em;
  background-color: #F2F6FC40;
}

.perfect-title {
  width: 100%;
  text-align: center;
  font-size: 16px;
  margin-bottom: 8px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
}

.input-area {
  margin: 4px 2px;
}
</style>
