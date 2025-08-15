<template>
  <div class="app-container">
    <el-card>
      <el-form :model="queryParams" ref="queryForm" size="small" v-show="showSearch" label-width="88px">
        <el-row :gutter="10">
          <el-col :span="8" style="height: 50px;">
            <el-form-item :label="$t('historyList.question')" label-width="auto" style="display: flex" prop="createBy">
              <el-input v-model="queryParams.createBy" :placeholder="$t('historyList.questionPlaceholder')" clearable
                style="width: 300px" @keyup.enter.native="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8" style="height: 50px;">
            <el-form-item :label="$t('historyList.questionContent')" label-width="auto" style="display: flex"
              prop="question">
              <el-input v-model="queryParams.question" :placeholder="$t('historyList.questionContentPlaceholder')"
                clearable style="width: 300px" @keyup.enter.native="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8" style="height: 50px;">
            <el-form-item :label="$t('historyList.answer')" label-width="auto" style="display: flex" prop="answer">
              <el-input v-model="queryParams.answer" :placeholder="$t('historyList.answerPlaceholder')" clearable
                style="width: 300px" @keyup.enter.native="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col v-if="false" :span="8" style="height: 50px;" class="no-bottom">
            <el-form-item :label="$t('historyList.model')" label-width="auto" style="display: flex" prop="model">
              <el-input v-model="queryParams.model" :placeholder="$t('historyList.modelPlaceholder')" clearable style=""
                @keyup.enter.native="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8" style="height: 50px;" class="no-bottom">
            <el-form-item :label="$t('historyList.accuracyEvaluation')" label-width="auto" style="display: flex"
              prop="isAccuracy">
              <el-select v-model="queryParams.isAccuracy" :placeholder="$t('historyList.accuracyEvaluationPlaceholder')"
                clearable style="width: 300px" @change="handleQuery">
                <el-option v-for="item in accuracyTypes" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" style="height: 50px;">
            <el-form-item :label="$t('historyList.time')" style="display: flex" prop="createTime"
              class="no-bottom">
              <el-date-picker style="width: 100%;" v-model="createTime" type="datetimerange" :range-separator="$t('or')"
                :start-placeholder="$t('startTime')" :end-placeholder="$t('endTime')"
                :default-time="['00:00:00', '23:59:59']" value-format="yyyy-MM-dd HH:mm:ss" clearable
                @change="(val) => handleChangeTime(['startTime', 'endTime'], val)" />
            </el-form-item>
          </el-col>
          <el-col :span="8" style="height: 50px;">
            <el-form-item class="no-bottom">
              <el-button class="secondary" icon="el-icon-refresh" size="medium" @click="resetQuery"
                style="color:white;">{{ $t('rest') }}
              </el-button>
              <el-button type="primary" icon="el-icon-search" size="medium" @click="handleQuery" style="">{{
                $t('search') }}
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <el-card class="mt20">
      <el-table v-loading="loading" class="color-table" :data="authList" :row-style="{ height: '50px' }">
        <el-table-column :label="$t('historyList.sessionIdentifier')" align="center" prop="sessionId"
          show-overflow-tooltip />
        <el-table-column :label="$t('historyList.putQuestion')" align="center" prop="question" show-overflow-tooltip />
        <el-table-column :label="$t('historyList.rewrittenQuery')" align="center" prop="rewrittenQuery">
          <template v-slot="scope">
            <div @mouseenter="mouseentera(true, $event)" @mouseleave="mouseentera(false, $event)" @click="openMet(scope.row.rewrittenQuery, $t('historyList.rewrittenQuery'))">
              {{truncateText(scope.row.rewrittenQuery)}}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('historyList.answer')" align="center" prop="answer">
          <template v-slot="scope">
<!--            <div @mouseenter="mouseentera(true, $event)" @mouseleave="mouseentera(false, $event)"@click="openMet(scope.row.answer, $t('historyList.answer'))">-->
            <div @mouseenter="mouseentera(true, $event)" @mouseleave="mouseentera(false, $event)" @click="handleShowDetail(scope.row)">
              {{truncateText(scope.row.answer)}}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('historyList.materialsRetrieved')" align="center" prop="chunkSearch" min-width="200" >
          <template v-slot="scope">
<!--            <div @mouseenter="mouseentera(true, $event)" @mouseleave="mouseentera(false, $event)" @click="openMet(scope.row.chunkSearch, $t('historyList.materialsRetrieved'))">-->
            <div @mouseenter="mouseentera(true, $event)" @mouseleave="mouseentera(false, $event)" @click="handleShowDetail2(scope.row)">
              {{truncateText(scope.row.chunkSearch)}}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('historyList.usedModel')" align="center" prop="modelType" show-overflow-tooltip />
        <el-table-column :label="$t('historyList.accuracyEvaluation')" align="center" prop="isAccuracy"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isAccuracy === 'yes'" type="success">{{ $t('historyList.accuracy') }}</el-tag>
            <el-tag v-else-if="scope.row.isAccuracy === 'no'" type="danger">{{ $t('historyList.inAccuracy') }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('historyList.tokenConsumption')" align="center" prop="tokenUsage"
          show-overflow-tooltip />
        <el-table-column :label="$t('historyList.timeEstimation')" align="center" prop="timeEstimation">
          <template v-slot="scope">
            <div @mouseenter="mouseentera(true, $event)" @mouseleave="mouseentera(false, $event)" @click="openMet(scope.row.timeEstimation, $t('historyList.timeEstimation'))">
              {{truncateText(scope.row.timeEstimation)}}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('historyList.questionTime')" align="center" prop="createTime" show-overflow-tooltip />
        <el-table-column :label="$t('historyList.questionPeople')" align="center" prop="createBy"
          show-overflow-tooltip />
        <el-table-column :label="$t('historyList.retrieveKb')" align="center" prop="kbName" show-overflow-tooltip />
        <el-table-column :label="$t('historyList.operationTime')" align="center" prop="createTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('historyList.operator')" align="center" prop="createBy" show-overflow-tooltip />
        <el-table-column :label="$t('operation')" align="center" width="80px" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-button class="blue" size="mini" :title="$t('historyList.see')" @click="handleView(scope.row)">
              {{ $t('historyList.see') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 10px;" align="right">
        <pagination v-show="total > 0" :total="total" :page.sync="pageParams.pageNum" :limit.sync="pageParams.pageSize"
          @pagination="getList" popper-class="color-pagination" background layout="total, sizes, prev, pager, next" />
      </div>
    </el-card>


    <el-dialog :visible.sync="open" width="600px" append-to-body>
      <div v-for="(item, index) in historyList" :key="index" class="flex-layout mb10"
        :style="{ justifyContent: item.role === 'user' ? 'flex-end' : 'flex-start', gap: '6px' }">
        <div v-if="item.role === 'bot'">
          <img :src="getBotImage(item.modelType)" class="avatar medium" />
        </div>
        <div v-else class="avatar placeholder"></div>
        <div style="overflow: hidden;">
          <el-card :class="item.role" style="margin-top: 18px;">
            <template v-if="item.loading">
              <span>{{ $t('privateDomain.thinkingAbout') }}</span><i class="el-icon-loading"
                style="margin-left: 4px;" />
            </template>
            <span v-else-if="item.isError">
              {{ item.errorContent }}
            </span>
            <span v-else>
              {{ item.content }}
            </span>

            <div class="content-area" v-if="item.citation && item.citation.length > 0">
              <!--                <div class="content-title">参考文献</div>-->
              <div class="flex-layout align-center gap8">
                <span class="content-title">{{ $t('privateDomain.quote') }}</span>
                <el-divider class="flex-1 m16" />
              </div>
              <div class="content-link" style="margin: 4px 0 4px 0;" v-for="(citation, index) in item.citation"
                @click="handleShowContent(citation)">
                <!--                  <div >{{index+1}}.</div>-->
                <svg-icon v-if="citation.suffix === 'pdf'" icon-class="pdf" class="text-red fs-14" />
                <svg-icon v-else-if="citation.suffix === 'xlsx'" icon-class="excel" class="text-green fs-14" />
                <span class="text-over">{{ citation.originalFileName }}</span>
              </div>
            </div>
            <div class="tool-bar" v-if="item.role === 'bot' && !item.loading && !item.isError"
              style="padding-top: 4px;font-size: 16px;gap:2px;">
              <div class="tool-bar-item" @click="handleCopy(item)">
                <svg-icon icon-class="copy" />
              </div>
            </div>
          </el-card>

        </div>
        <div v-if="item.role === 'user'">
          <img :src="userAvatar" class="avatar" />
        </div>
        <div v-else class="avatar placeholder"></div>
      </div>
      <div :class="{ 'blank': true }" />
    </el-dialog>

    <el-drawer :title="contentName" size="50%" :visible.sync="showContent">
      <template v-if="showContentDetail">
        <excel-component ref="excelComponent" v-if="excels.includes(suffix)" :url="contentInfo"
          style="width: 100%;flex:1;border:none;" />
        <pdf-component ref="pdfComponent" v-else-if="pdfs.includes(suffix)" :page="contentPage" :url="contentInfo" />
      </template>
    </el-drawer>

    <el-dialog
      title="回答详情"
      :visible.sync="dialogVisible"
      width="80%"
    >
      <el-descriptions
        class="margin-top"
        :column="1"
        :labelStyle="{width: '100px'}"
        border>
        <el-descriptions-item>
          <template slot="label">
            <span class="text-bold">回答</span>
          </template>
          <markdown-it-vue
            class="md-body"
            :content="qaDetail.answer" :options="options" align="left"/>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog
      title="检索到的相关资料详情"
      :visible.sync="dialogVisible2"
      width="80%">
      <div style="margin-top: 10px;">
        <json-viewer
          v-if="yamlData"
          :value="yamlData"
          :expand-depth="3"
          copyable
          boxed
          sort
          style="max-height: 70vh; overflow-y: auto;"
        />
        <pre class="yaml-container" v-else>{{ yamlData }}</pre>
      </div>
    </el-dialog>

  </div>
</template>

// 在 script 部分添加导入
<script>
import { addKB, delKB, editKB, getKB } from '@/api/knowledgeBase/kbManager'
import { listKBHistory } from '@/api/knowledgeBase/historyList'
import { addStartDateRange } from '@/utils/ruoyi'
import placeholder from '@/assets/logo/logo-placeholder.jpg'
import userAvatar from '@/assets/images/default_profile.png'
import ExcelComponent from '@/components/Excel/index.vue'
import PdfComponent from '@/components/Pdf/index.vue'
import { jsonToYaml } from '@/utils/yamlUtil';
import JsonViewer from 'vue-json-viewer';  // 新增导入


export default {
  name: "AuthorizationManager",
  components: { PdfComponent, ExcelComponent, JsonViewer },  // 注册新组件
  data() {
    return {
      userAvatar,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      authList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],

      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },

      // 查询参数
      queryParams: {},
      pageParams: {
        pageNum: 1,
        pageSize: 200,
      },
      createTime: null,
      // 表单校验
      rules: {
        kbName: [
          { required: true, message: this.$t('kbManager.namePlaceholder'), trigger: "blur" }
        ],
        kbDescription: [
          { required: true, message: this.$t('kbManager.kbDescriptionPlaceholder'), trigger: "blur" }
        ],
      },

      accuracyTypes: [
        { label: this.$t('historyList.accuracy'), value: 'yes' },
        { label: this.$t('historyList.inAccuracy'), value: 'no' }
      ],

      historyList: [],

      suffix: '',
      contentInfo: '',
      contentPage: null,
      contentName: '',
      showContent: false,
      showContentDetail: false,

      excels: ['xlsx', 'xls', 'csv'],
      pdfs: ['pdf'],

      dialogVisible: false,
      qaDetail: {},
      options: {
        markdownIt: {
          linkify: true,
          html: true,
          breaks: true,
        }
      },

      dialogVisible2: false,
      chunkSearchList: [],
      yamlData: '',
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listKBHistory(this.pageParams, this.queryParams).then(response => {
        this.authList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },


    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        kbName: null,
        kbDescription: null,
      };
      this.resetForm("form");
    },
    handleChangeTime(keys, val) {
      this.queryParams = addStartDateRange(this.queryParams, val, keys);
      this.handleQuery();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.pageParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.createTime = null;
      this.pageParams = {
        pageNum: 1,
        pageSize: 200,
      };
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t('kbManager.addKb');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getKB(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('kbManager.updateKb');
      });
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            editKB(this.form).then(() => {
              this.getList();
              this.$modal.msgSuccess(this.$t('updateSuccess'));
              this.open = false;
            });
          } else {
            addKB(this.form).then(() => {
              this.getList();
              this.$modal.msgSuccess(this.$t('addSuccess'));
              this.open = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm(this.$t('kbManager.areYouDelete')).then(function () {
        return delKB(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('deletedSuccess'));
      }).catch(() => {
      });
    },
    handleView(row) {
      this.historyList = [];
      this.formatHistoryList([row]);
      this.open = true;
    },
    getBotImage(modelType) {
      return this.$store.state.user.botList.find(item => item.modelType === modelType).icon || placeholder;
    },
    formatHistoryList(history) {
      history.forEach(row => {
        let res = JSON.parse(row.response || "{\"code\": 500, \"data\": {\"answer\": \"\", \"blobUrl\": [],\"hiddenQuesion\": [],\"originalFileNames\":[]},\"msg\": \"\",\"modelId\": \"\",\"tokenUsage\": \"\"}");


        this.historyList.push({
          role: 'user',
          content: row.question,
          type: 'text',
          loading: false,
        });


        if (res.code !== 200) {
          this.historyList.push({
            role: 'bot',
            content: row.answer || this.$t('privateDomain.modelAbnormal'),
            type: 'text',
            isError: true,
            errorContent: row.answer || this.$t('privateDomain.modelAbnormal')
          });
          return;
        }

        let data = {
          id: row.id,
          role: 'bot',
          type: 'text',
          accuracy: row.isAccuracy
        };
        data.content = res.data.answer;
        data.citation = res.data.originalFileNames.map((item, index) => {
          return {
            originalFileName: item,
            url: res.data.blobUrl[index],
            suffix: item.split('.').pop(),
            page: item.split('_').pop().split('.').shift()
          }
        });
        this.historyList.push(data);
      });
    },
    handleCopy(item) {
      if (item.isError) {
        return;
      }
      let textToCopy = item.content;
      this.copyContent(textToCopy);
    },
    handleShowContent(file) {
      const httpUrl = process.env.VUE_APP_FILE_DOWNLOAD_URL;
      const oldUrl = process.env.VUE_APP_INIT_FILE_URL;
      if (!file.url) {
        this.$message.warning(this.$t('privateDomain.fileShowError'));
        return;
      }
      let url = file.url.replace(oldUrl, httpUrl);
      /* if (file.url) {
        const split = file.url.split('/kb');
        url = httpUrl + '/kb' + split[1];
      } */
      this.contentName = file.originalFileName;
      this.suffix = file.suffix;
      // this.contentInfo = '/build/pdf/web/viewer.html?file=' + encodeURIComponent(url);
      this.contentInfo = url;
      this.contentPage = file.page;
      this.showContent = true;

      setTimeout(() => {
        this.showContentDetail = true;
      }, 500);
    },
    truncateText (v) {
      return v?.length > 10?v?.substring(0, 10) + '...' : v
    },
    openMet (v, t) {
      // 计算内容长度，设置基础宽度和最大宽度
      const contentLength = v.length;
      const baseWidth = Math.min(300 + contentLength * 8, 800); // 动态计算宽度

      this.$confirm(v, t,{
        roundButton: true,
        closeOnPressEscape: true,
        closeOnClickModal: true,
        cancelButtonText: 'close',
        showCancelButton: true,
        showConfirmButton: false,
        customClass: 'custom-alerta',
        width: baseWidth + 'px', // 应用动态宽度
      });
    },
    mouseentera(bool, e) {
      if (bool) {
        e.currentTarget.style.cursor = 'pointer';
        e.currentTarget.style.textDecoration = 'underline';
      }else{
        e.currentTarget.style.cursor = 'default';
        e.currentTarget.style.textDecoration = 'none';
      }
    },
    handleShowDetail(row) {
      this.dialogVisible = true;
      this.$nextTick(() => {
        this.qaDetail = {...row}; // Assuming you want to show the answer in the dialog
      });
    },

      handleShowDetail2(row) {
        try {
          // 保存原始 JSON 数据
          this.yamlData = typeof row.chunkSearch === 'string' ? JSON.parse(row.chunkSearch) : row.chunkSearch;
        } catch (error) {
          console.error('JSON 解析失败:', error);
          this.yamlData = '解析失败: ' + error.message;
        }
        this.dialogVisible2 = true;
      },
      formatChunkSearchList(row) {
        let res = JSON.parse(row.response || "{\"code\": 500, \"data\": {\"answer\": \"\", \"blobUrl\": [],\"hiddenQuesion\": [],\"originalFileNames\":[]},\"msg\": \"\",\"modelId\": \"\",\"tokenUsage\": \"\"}");
        this.chunkSearchList = res.data.originalFileNames.map((item, index) => {
          return {
            originalFileName: item,
            url: res.data.blobUrl[index],
            suffix: item.split('.').pop(),
            page: item.split('_').pop().split('.').shift()
          }
        });
      }
  }
};
</script>

<style>
/*.custom-alerta {*/
/*  width: 70%;*/
/*  height: 700px;*/
/*  white-space: break-spaces;*/
/*  text-align: left;*/
/*  overflow-y: auto;*/
/*  overflow-x: hidden;*/
/*  transition: background-color 0.3s ease;*/
/*}*/

.custom-alerta {
  width: auto; /* 改为自动宽度 */
  max-width: 50%; /* 最大宽度限制为视口的90% */
  min-width: 300px; /* 最小宽度保障可读性 */
  max-height: 70vh; /* 最大高度为视口的70% */
  height: auto; /* 高度自适应内容 */
  white-space: break-spaces;
  text-align: left;
  overflow-y: auto;
  overflow-x: hidden;
  transition: background-color 0.3s ease;
  padding: 20px; /* 增加内边距避免内容紧贴边框 */
  margin: 0 auto; /* 水平居中显示 */
}

/* 基础样式 */
.yaml-container {
  padding: 16px;
  background: transparent;
  overflow-x: auto;
  font-family: 'Fira Code', monospace;
  white-space: pre-wrap;
}

.vue-json-viewer .jv-container {
  font-size: 14px;
  line-height: 1.5;
}

.vue-json-viewer .jv-code {
  padding: 10px;
  border-radius: 4px;
}

</style>
