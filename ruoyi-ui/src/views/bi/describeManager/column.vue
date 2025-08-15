<template>
  <div class="app-container">
    <el-card v-loading.fullscreen.lock="fullscreenLoading">
      <div class="flex-layout align-center start-end" style="margin-bottom: 10px;">
        <div class="flex-layout align-center gap8">
          <!--数据源选择-->
          <el-select v-model="selectInitData.dataSourceId"
                     :placeholder="$t('dataTable.dataSourceSelect')"
                     @change="getTableSelectData"
                     style="width: 200px;" size="small"
                     clearable>
            <el-option
              v-for="item in dataSources"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>

          <!--数据表选择-->
          <el-select v-model="selectInitData.dataSourceTableId"
                     :placeholder="$t('dataTable.pleaseTableSelect')"
                     @change="getSelectOperation"
                     style="width: 200px;" size="small"
                     clearable>
            <el-option
              v-for="item in tables"
              :key="item.id"
              :label="item.tableName"
              :value="item.id"
            />
          </el-select>
        </div>

        <div class="flex-layout align-center gap8">
          <el-input
            v-model="searchValue"
            :placeholder="$t('dataBase.searchValuePlaceholder')"
            style="width: 200px;"
            size="small"
            clearable
            @input="handleInputSearch"
            prefix-icon="el-icon-search"
          />
          <div>
            <el-button type="primary" class="green" :disabled="multiple" @click="handleTableSelect">{{ $t('dataTable.intelligent') }}</el-button>
          </div>

          <el-popconfirm
            :title="$t('dataBase.deleteSelectedContent')"
            @confirm="deleteTableIds"
          >
            <el-button class="red" disabled
                       plain
                       slot="reference"
                       icon="el-icon-delete"
                       :disabled="multiple"
            >{{ $t('deleted') }}
            </el-button>
          </el-popconfirm>
        </div>
      </div>
      <el-table
        v-loading="loading"
        class="color-table"
        :data="dataList"
        :row-style="{height: '50px'}"
        @selection-change="handleSelectionChange">
        <template slot="empty">
          <Empty/>
        </template>
        <el-table-column
          type="selection"
          width="50"
          align="center"/>
        <el-table-column
          type="index"
          :label="$t('chatBI.index')"
          width="60"
          align="center"/>
        <el-table-column
          :label="$t('column.columnName')"
          align="center"
          prop="fieldName"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('column.columnType')"
          align="center"
          prop="fieldType"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('column.columnDesc')"
          align="center"
          prop="fieldDescribe"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('kbManager.createTime')"
          align="center"
          prop="createTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('kbManager.createBy')"
          align="center"
          prop="createBy"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('operation')"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
          style=""
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >{{ $t('edit') }}
            </el-button>
<!--            <el-popconfirm
              style="margin-left: 12px"
              title="确定删除此条数据吗？"
              @confirm="handleDelete(scope.row)"
            >
              <el-button
                size="mini"
                type="text"
                slot="reference"
                icon="el-icon-delete"
              >删除
              </el-button>
            </el-popconfirm>-->
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 10px;" align="right">
        <pagination
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

    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="60%"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <el-form :model="form" :label-width="$i18n.locale === 'zh-CN' ? '100px' : '160px'" :rules="rules" ref="form">
        <el-form-item :label="$t('column.columnName')" prop="fieldName">
          <el-input v-model="form.fieldName" :placeholder="$t('column.columnNamePlaceholder')"/>
        </el-form-item>
        <el-form-item :label="$t('column.columnType')" prop="fieldType">
          <el-input v-model="form.fieldType" :placeholder="$t('column.columnTypePlaceholder')"/>
        </el-form-item>
        <el-form-item :label="$t('column.columnDesc')" prop="fieldDescribe">
          <el-input v-model="form.fieldDescribe" :placeholder="$t('column.columnDescPlaceholder')"/>
        </el-form-item>
      </el-form>

      <div slot="footer">
        <el-button @click="dialogVisible = false">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>

import {
  pageListColumn,
  addColumn,
  updateColumn,
  deleteColumn,
  findSelectList,
  findTableSelectData,
  syncTableSelect,
  deletedTableIds
} from '@/api/bi/describeManager/column';
import {Message} from 'element-ui';
import {pageListChatBiTableData} from "@/api/bi/describeManager/api";

export default {
  name: "TableDescription",
  data() {
    return {
      loading: false,
      fullscreenLoading: false,
      dataList: [],
      total: 0,
      pageParams: {
        pageNum: 1,
        pageSize: 10
      },
      multiple: true,
      searchValue: '',

      dataSource: '',
      dataSources: [],

      table: '',
      tables: [],

      form: {},
      rules: {
        columnName: [
          {required: true, message: this.$t('column.columnNamePlaceholder'), trigger: 'blur'}
        ],
        columnDescription: [
          {required: true, message: this.$t('column.columnDescPlaceholder'), trigger: 'blur'}
        ]
      },
      dialogVisible: false,
      dialogTitle: '',
      selectInitData: {
        id: '',
        dataSourceId: '',
        dataSourceTableId: '',
        fieldName: '',
        fieldType: '',
        fieldDescribe: ''
      },
      selectTableData: []
    };
  },
  mounted() {
    this.getList();
    this.getSelectData();
  },
  methods: {
    getList(e) {
      this.loading = true;
      let pageParams = this.pageParams;
      if (e) {
        pageParams = {
          pageNum: e.page,
          pageSize: e.limit
        };
      }
      pageListColumn(pageParams, this.selectInitData).then(res => {
        if (res.code === 200) {
          this.dataList = res.rows;
          this.total = res.total;
          this.loading = false;
        }
      }).catch(err => {
        this.loading = false;
        Message.error(this.$t('dataBase.queryListError') + err);
      });
    },
    getSelectData() {
      findSelectList({name: ''}).then(res => {
        if (res.code === 200) {
          this.dataSources = res.data;
        }
      }).catch(err => {
        Message.error(this.$t('dataTable.querySelectData') + err);
      });
    },
    getSelectOperation() {
      this.loading = true;
      pageListColumn(this.pageParams, {
        dataSourceId: this.selectInitData.dataSourceId,
        dataSourceTableId: this.selectInitData.dataSourceTableId,
        fieldName: this.searchValue
      }).then(res => {
        if (res.code === 200) {
          this.dataList = res.rows;
          this.total = res.total;
          this.loading = false;
        }
      }).catch(err => {
        Message.error(this.$t('dataBase.queryListError') + err);
      });
    },
    getTableSelectData() {
      findTableSelectData({
        tableName: '',
        dataSourceId: this.selectInitData.dataSourceId
      }).then(res => {
        if (res.code === 200) {
          this.tables = res.data;
        }
      }).catch(err => {
        Message.error(this.$t('dataTable.querySelectData') + err);
      });
    },
    handleSelectionChange(val) {
      if (val.length > 0) {
        this.multiple = false;
        this.selectTableData = [];
        val.forEach((item) => {
          this.selectTableData.push(item.id)
        });
      } else {
        this.multiple = true;
        this.selectTableData = [];
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogTitle = this.$t('column.editColumn');
      this.dialogVisible = true;
    },
    handleDelete(row) {
      deleteColumn(row.id).then(res => {
        if (res.code === 200) {
          this.getList();
          Message.success(this.$t('dataTable.deletedError'));
        }
      }).catch(err => {
        Message.error(this.$t('dataTable.deletedError') + err);
      });
    },
    resetForm() {
      this.$refs.form.clearValidate();
      this.form = {};
    },
    submitForm() {
      updateColumn(this.form).then(res => {
        if (res.code === 200) {
          this.dialogVisible = false;
          this.getList();
          Message.success(this.$t('dataTable.updateSuccess'));
        }
      }).catch(err => {
        Message.error(this.$t('dataTable.updateError') + err);
      });
    },
    handleInputSearch(val) {
      this.loading = true;
      pageListColumn(this.pageParams, {
        dataSourceId: this.selectInitData.dataSourceId,
        dataSourceTableId: this.selectInitData.dataSourceTableId,
        fieldName: val,
      }).then(res => {
        if (res.code === 200) {
          this.dataList = res.rows;
          this.total = res.total;
          this.loading = false;
        }
      }).catch(err => {
        Message.error(this.$t('dataBase.queryListError') + err);
      });
    },
    handleTableSelect() {
      if (this.selectTableData.length === 0) {
        Message.warning(this.$t('dataTable.pleaseTableData'));
        return;
      }
      syncTableSelect({ids: this.selectTableData}).then(res => {
        if (res.code === 200) {
          Message.success(res.msg);
          this.getList();
        }
      }).catch(err => {
        Message.error(this.$t('column.intelligentError') + err);
      });
    },
    deleteTableIds() {
      if (this.selectTableData.length === 0) {
        Message.warning(this.$t('dataTable.pleaseTableData'));
        return;
      }
      deletedTableIds({ids:this.selectTableData}).then(res => {
        if (res.code === 200) {
          Message.success(res.msg);
          this.getList();
        }
      }).catch(err => {
        Message.error(this.$t('dataBase.deletedError') + err);
      });
    }
  }
}
</script>
<style scoped lang="scss">

</style>
