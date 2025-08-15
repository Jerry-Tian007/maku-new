<template>
  <div class="app-container" v-loading.fullscreen.lock="fullscreenLoading">

    <el-card class="mt20">
      <el-table
        v-loading="loading"
        class="color-table"
        :data="kbPromptList"
        :row-style="{height: '50px'}"
        @selection-change="handleSelectionChange">
        <template slot="empty">
          <Empty />
        </template>
        <el-table-column
          type="selection"
          width="50"
          align="center"/>
        <el-table-column
          :label="$t('promptManager.promptTitle')"
          align="center"
          prop="name"
          show-overflow-tooltip>
          <!--          <template slot-scope="scope">-->
          <!--            <el-button type="text" @click="handleSearchFiles(scope.row.kbName)">-->
          <!--              {{ scope.row.kbName }}-->
          <!--            </el-button>-->
          <!--          </template>-->
        </el-table-column>
        <el-table-column
          :label="$t('promptManager.promptType')"
          align="center"
          prop="queryPromptType"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('promptManager.status')"
          align="center"
          prop="releaseStatus"
          show-overflow-tooltip>

          <template slot-scope="scope">
            <span v-if="scope.row.releaseStatus === '1'">{{ $t('promptManager.activeStatus') }}</span>
            <span v-else>{{ $t('promptManager.inActiveStatus') }}</span>
          </template>
        </el-table-column>

        <el-table-column  :label="$t('promptManager.prompt')" show-overflow-tooltip>
          <template #default="{row}">
            <div @click="showPromptDetail(row)" class="prompt-preview">
              {{ formatPromptPreview(row.prompt) }}
            </div>
          </template>
        </el-table-column>


        <el-table-column
          :label="$t('promptManager.kb')"
          align="center"
          prop="kbName"
          show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.kbName || 'System' }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('kbManager.createBy')"
          align="center"
          prop="createBy"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('kbManager.createTime')"
          align="center"
          prop="createTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 10px;" align="right">
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="pageParams.pageNum"
          :limit.sync="pageParams.pageSize"
          @pagination="getList"
          popper-class="color-pagination"
          background
          layout="total, sizes, prev, pager, next"
        />
      </div>
    </el-card>

    <!-- 添加el-dialog组件 -->
    <el-dialog
      :visible.sync="dialogVisible"
      :title="'Prompt Detail: '"
      :close-on-click-modal="false"
      width="80%"
    >
      <div class="prompt-detail-container">
        <pre class="prompt-content">{{ currentPrompt }}</pre>
        <div class="dialog-footer">
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {getToken} from "@/utils/auth";
import { listQueryPrompts} from "@/api/knowledgeBase/promptManager";

export default {
  name: "PromptManager",
  data() {
    return {
      // 遮罩层
      loading: true,
      fullscreenLoading: false,
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
      kbPromptList: null,
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

      pageParams:{
        pageNum: 1,
        pageSize: 200,
      },
      // 查询参数
      queryParams: {
        kbName: null,
      },

      promptText: ``,
      // 添加缺少的属性
      dialogVisible: false,
      currentPrompt: '',

      // 表单校验
      rules: {
        kbName: [
          {required: true, message: this.$t('kbManager.namePlaceholder'), trigger: "blur"}
        ],
        kbDescription: [
          {required: true, message: this.$t('kbManager.kbDescriptionPlaceholder'), trigger: "blur"}
        ],
      },
      promptList: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** list all query_prompt data */
    getList() {
      this.loading = true;
      listQueryPrompts(this.pageParams, this.queryParams).then(response => {
          this.kbPromptList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      ).catch(() => {
        this.loading = false;
      });
    },
    formatPromptPreview(prompt) {
      // 只显示前几行作为预览
      return prompt.split('\n').slice(0, 2).join('\n') + (prompt.split('\n').length > 2 ? '\n...' : '');
    },

    showPromptDetail(row) {
      this.currentPrompt = row.prompt;
      this.dialogVisible = true;
    },

    copyPrompt() {
      navigator.clipboard.writeText(this.currentPrompt).then(() => {
        this.$message.success('已复制到剪贴板');
      });
    },

    handleSearchFiles(name){
      this.$router.push({path: '/data/FileManager', query: {kbName: name}})
    }
  }
};
</script>


<style lang="scss" scoped>


// 提示预览样式
.prompt-preview {
  color: #1890ff;
  cursor: pointer;
  //font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  &:hover {
    text-decoration: underline;
    color: #40a9ff;
  }
}

// 对话框容器样式
.prompt-detail-container {
  padding: 16px;
  //font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

// 优化对话框内容区域背景和字体 - 使用护眼颜色
.prompt-content {
  width: 100%;
  min-height: 400px;
  max-height: 600px;
  overflow: auto;
  background-color: #fafffb;  // 护眼浅绿色背景
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #e8e8e8;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  //font-family: 'Courier New', Courier, monospace;  // 等宽字体，适合显示代码
  font-size: 14px;
  line-height: 1.6;
  color: #333333;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}

.close-icon {
  cursor: pointer;
  color: #909399;
  font-size: 18px;
  &:hover {
    color: #606266;
  }
}
</style>
