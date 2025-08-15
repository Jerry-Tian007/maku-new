<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('systemManager.systemModule')" prop="title" :label-width="$i18n.locale === 'zh-CN' ? '68px' : '140px'">
        <el-input
          v-model="queryParams.title"
          :placeholder="$t('systemManager.systemModulePlaceholder')"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('systemManager.operator')" prop="operName">
        <el-input
          v-model="queryParams.operName"
          :placeholder="$t('systemManager.operatorPlaceholder')"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('systemManager.type')" prop="businessType">
        <el-select
          v-model="queryParams.businessType"
          :placeholder="$t('systemManager.operationType')"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_oper_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('systemManager.state')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="$t('systemManager.operationState')"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_common_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('systemManager.operationDate')" :label-width="$i18n.locale === 'zh-CN' ? '68px' : '140px'">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="daterange"
          range-separator="-"
          :start-placeholder="$t('startTime')"
          :end-placeholder="$t('endTime')"
          :default-time="['00:00:00', '23:59:59']"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('search') }}</el-button>
        <el-button class="secondary" icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('rest') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          class="red" disabled
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['monitor:operlog:remove']"
        >{{ $t('deleted') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
          v-hasPermi="['monitor:operlog:remove']"
        >{{ $t('AllInOne.clear') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          class="blue-light"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['monitor:operlog:export']"
        >{{ $t('export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column :label="$t('systemManager.logNumber')" align="center" prop="operId" />
      <el-table-column :label="$t('systemManager.systemModule')" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('systemManager.operationType')" align="center" prop="businessType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_oper_type" :value="scope.row.businessType"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('systemManager.operator')" align="center" prop="operName" width="110" :show-overflow-tooltip="true" sortable="custom" :sort-orders="['descending', 'ascending']" />
      <el-table-column :label="$t('systemManager.operationAddress')" align="center" prop="operIp" width="130" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('systemManager.operationAdd')" align="center" prop="operLocation" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('systemManager.operationState')" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_common_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('systemManager.operationDate')" align="center" prop="operTime" width="160" sortable="custom" :sort-orders="['descending', 'ascending']">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('systemManager.timeConsuming')" align="center" prop="costTime" width="110" :show-overflow-tooltip="true" sortable="custom" :sort-orders="['descending', 'ascending']">
        <template slot-scope="scope">
          <span>{{ scope.row.costTime }}{{ $t('systemManager.millisecond') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
            v-hasPermi="['monitor:operlog:query']"
          >{{ $t('systemManager.detail') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      popper-class="color-pagination"
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 操作日志详细 -->
    <el-dialog :title="$t('systemManager.operationDetail')" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :label-width="$i18n.locale === 'zh-CN' ? '100px' : '170px'" size="mini">
        <el-row>
          <el-col :span="14">
            <el-form-item :label="$t('systemManager.operationModel')">{{ form.title }} / {{ typeFormat(form) }}</el-form-item>
            <el-form-item
              :label="$t('systemManager.loginInfo')"
            >{{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.requestAddress')">{{ form.operUrl }}</el-form-item>
            <el-form-item :label="$t('systemManager.requestMethod')">{{ form.requestMethod }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('systemManager.operationMethod')">{{ form.method }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('systemManager.requestParameters')">{{ form.operParam }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('systemManager.cancelParams')">{{ form.jsonResult }}</el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :label="$t('systemManager.operationStates')">
              <div v-if="form.status === 0">{{ $t('success') }}</div>
              <div v-else-if="form.status === 1">{{ $t('systemManager.fail') }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.timeConsuming')">{{ form.costTime }}{{ $t('systemManager.millisecond') }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.operationTime')">{{ parseTime(form.operTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('systemManager.abnormalInformation')" v-if="form.status === 1">{{ form.errorMsg }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">{{ $t('close') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, delOperlog, cleanOperlog } from "@/api/monitor/operlog";

export default {
  name: "Operlog",
  dicts: ['sys_oper_type', 'sys_common_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 默认排序
      defaultSort: {prop: 'operTime', order: 'descending'},
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        operName: undefined,
        businessType: undefined,
        status: undefined
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询登录日志 */
    getList() {
      this.loading = true;
      list(this.addDateRange(this.queryParams, this.dateRange)).then( response => {
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 操作日志类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.dict.type.sys_oper_type, row.businessType);
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.queryParams.pageNum = 1;
      this.$refs.tables.sort(this.defaultSort.prop, this.defaultSort.order)
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.operId)
      this.multiple = !selection.length
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.getList();
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const operIds = row.operId || this.ids;
      this.$modal.confirm(this.$t('systemManager.deletedOperation') + operIds + this.$t('systemManager.dictNoDeletedText')).then(function() {
        return delOperlog(operIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('deletedSuccess'));
      }).catch(() => {});
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal.confirm(this.$t('systemManager.clearOperation')).then(function() {
        return cleanOperlog();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('systemManager.clearSuccess'));
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('monitor/operlog/export', {
        ...this.queryParams
      }, `operlog_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

