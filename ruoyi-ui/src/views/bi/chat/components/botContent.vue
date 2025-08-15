<template>
  <div style="min-width:400px;margin-bottom: 6px;overflow:hidden;">
    <div>
      <div class="sub-color fs-12 pd-v4">
        <i class="el-icon-data-analysis"/>
        <span style="margin-left: 4px;">{{ $t('chatBI.data') }}</span>
      </div>
      <!--        <markdown-it-vue v-loading="chartLoading" class="md-body" :content="table" :options="options" align="left"/>-->
      <div style="width: 100%;" class="flex-layout align-center start-end fs-14 pd-v6" v-if="tableData.length > 0">
        <span class="fw-6 pd-h4">{{ tableTitle }}</span>
        <div class="normal-tool-bar">
          <div class="tool-bar-item" :title="$t('export')" @click="handleDownloadTable" v-if="false">
            <i class="el-icon-download"/>
          </div>
          <div class="tool-bar-item" :title="$t('amplify')" @click="handleShowBigTable">
            <i class="el-icon-full-screen"/>
          </div>
        </div>
      </div>
      <el-table
        v-if="tableData.length > 0"
        style="width: 100%;border-radius: 8px;"
        height="200px"
        :data="tableData"
        stripe
      >
        <el-table-column
          type="index"
          :label="$t('chatBI.index')"
          width="55"
        >
        </el-table-column>
        <el-table-column
          v-for="(col, index) in columnList"
          :prop="col"
          :label="col"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
      </el-table>

      <div v-loading="chartLoading" style="width: 100%;height:200px;background: #ECF2FD;" v-else></div>
    </div>

    <div style="margin-top: 10px;">
      <div class="sub-color fs-12 pd-v4">
        <i class="el-icon-chat-line-square"/>
        <span style="margin-left: 4px;">{{ $t('chatBI.summarize') }}</span>
      </div>
      <el-card class="blue" style="min-height: 100px;" v-loading="contentLoading">
        <markdown-it-vue class="md-body" :content="content" :options="options" align="left"/>
      </el-card>
    </div>

    <!--    <div class="flex-layout workflow" :class="{hide: !showWorkFlow}" style="overflow:auto;position:relative;" >
          <work-flow :work-list="workList" style="position:absolute;z-index:1;height: 100%;width:100%;" />
        </div>-->

    <el-dialog
      :title="tableTitle"
      :visible.sync="showBigTable"
      center
      width="70%">

      <el-table
        style="width: 100%;border-radius: 8px;"
        :data="tableData"
        stripe
      >
        <Empty slot="empty" :description="$t('chatBI.noneData')"></Empty>
        <el-table-column
          type="index"
          :label="$t('chatBI.index')"
          width="55"
        >
        </el-table-column>
        <el-table-column
          v-for="(col, index) in columnList"
          :prop="col"
          :label="col"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
import WorkFlow from "@/views/bi/chat/components/workFlow.vue";

export default {
  name: "BIBotContent",
  components: {WorkFlow},
  props: {
    content: {
      type: String,
      default: ''
    },
    showWorkFlow: {
      type: Boolean,
      default: false
    },
    chartLoading: {
      type: Boolean,
      default: false
    },
    contentLoading: {
      type: Boolean,
      default: false
    },
    chartData: {
      type: Object,
      default: () => {
        return {}
      }
    },
  },
  data() {
    return {
      options: {
        markdownIt: {
          linkify: true,
          html: true,
        },
      },
      table: `| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | int | 主键 |`,

      tableTitle: '',
      tableData: [],
      columnList: [''],

      workList: [

        {
          name: '工作项',
          status: 'success',
        },
        {
          name: '工作项',
          status: 'error',
        },
        {
          name: '工作项',
          status: 'loading',
        },
        {
          name: '工作项',
          status: 'waiting',
        },
        {
          name: '工作项',
          status: 'waiting',
        },
        {
          name: '工作项',
          status: 'waiting',
        },
      ],

      showBigTable: false,
    }
  },
  watch: {
    chartData: {
      handler(val) {
        this.handleShowChart(val);
      },
      immediate: true
    }
  },
  mounted() {

  },
  methods: {
    handleShowChart(val) {
      if (!val) return;
      switch (val.type) {
        case 'table':
          this.tableData = val.ori_data;
          this.tableTitle = val.title;
          this.columnList = val.ori_data[0] ? Object.keys(val.ori_data[0]) : [];
          break;
        default:

      }
    },
    handleDownloadTable() {
      this.$message.success(this.$t('downloadSuccess'));
    },
    handleShowBigTable() {
      this.showBigTable = true;
    }
  }
}
</script>
<style scoped lang="scss">
.workflow {
  width: 240px;
  margin-left: 10px;
  transition: all 0.25s;

  &.hide {
    margin-left: 0;
    opacity: 0;
    width: 0;
    transition: all 0.25s;
  }
}
</style>
