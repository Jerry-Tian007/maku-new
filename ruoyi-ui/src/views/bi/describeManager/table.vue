<template>
  <div class="app-container">
    <el-card v-loading.fullscreen.lock="fullscreenLoading">
      <div class="flex-layout align-center start-end" style="margin-bottom: 10px;">
        <div class="flex-layout align-center gap8">
          <!--数据源选择-->
          <el-select v-model="dataSource"
                     :placeholder="$t('dataTable.dataSourceSelect')"
                     style="width: 200px;"
                     size="small"
                     @change="selectData"
                     clearable>
            <el-option
              v-for="item in dataSources"
              :key="item.id"
              :label="item.name"
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
            <el-button type="primary" class="green" :disabled="multiple" @click="handleSelectTable">{{ $t('dataTable.intelligent') }}</el-button>
          </div>
          <el-popconfirm
            :title="$t('dataBase.deleteSelectedContent')"
            @confirm="handleSelectDeleted"
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
          :label="$t('dataTable.dataTableName')"
          align="center"
          prop="tableName"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('chatBI.tableDescription')"
          align="center"
          prop="tableDescribe"
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
          :label="$t('remark')"
          align="center"
          prop="remark"
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
        <el-form-item :label="$t('dataTable.dataTableName')" prop="tableName">
          <el-input v-model="form.tableName" :placeholder="$t('dataTable.dataTableNamePlaceholder')" clearable/>
        </el-form-item>
        <el-form-item :label="$t('chatBI.tableDescription')" prop="tableDescribe">
          <el-input v-model="form.tableDescribe" :placeholder="$t('dataTable.tableDescriptionPlaceholder')" clearable/>
        </el-form-item>
        <el-form-item :label="$t('remark')" prop="remark">
          <el-input v-model="form.remark" :placeholder="$t('dataTable.remarkPlaceholder')" clearable/>
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
  pageListChatBiTableData,
  deletedPageListById,
  updateDescribe,
  findSelectList,
  syncTableSelect,
  deleteTableSelectData
} from "@/api/bi/describeManager/api";

import {Message} from 'element-ui';
import {pageListChatBiDataSource} from "@/api/bi/databaseManager/table";

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

      dialogVisible: false,
      dialogTitle: '',
      form: {
        tableName: '',
        tableDescribe: '',
      },

      rules: {
        tableName: [
          {required: true, message: this.$t('dataTable.dataTableNamePlaceholder'), trigger: 'blur'}
        ],
        tableDescribe: [
          {required: true, message: this.$t('dataTable.tableDescriptionPlaceholder'), trigger: 'blur'}
        ],
      },
      tableSelectIds: []
    };
  },
  mounted() {
    this.getList();
    this.getSelect();
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
      pageListChatBiTableData(pageParams, {
        dataSourceId: this.dataSource,
        tableName: this.searchValue
      }).then(res => {
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
    getSelect() {
      findSelectList({name: ''}).then(res => {
        if (res.code === 200) {
          this.dataSources = res.data;
        }
      }).catch(err => {
        Message.error(this.$t('dataTable.querySelectData') + err);
      });
    },
    handleSelectionChange(val) {
      if (val.length > 0) {
        this.multiple = false;
        this.tableSelectIds = [];
        for (const v of val) {
          this.tableSelectIds.push(v.id);
        }
      } else {
        this.multiple = true;
        this.tableSelectIds = [];
      }
    },
    handleDelete(row) {
      deletedPageListById(row.id).then(res => {
        if (res.code === 200) {
          Message.success(res.msg);
          this.getList();
        }
      }).catch(err => {
        Message.error(this.$t('dataTable.deletedError') + err);
      });
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogTitle = this.$t('dataTable.editTableDescription');
      this.dialogVisible = true;
    },
    resetForm() {
      this.$refs.form.clearValidate();
      this.form = {
        tableName: '',
        tableDescribe: '',
      };
    },
    submitForm() {
      if (!this.form.tableName) {
        return;
      }
      if (!this.form.tableDescribe) {
        return;
      }
      updateDescribe(this.form).then(res => {
        if (res.code === 200) {
          Message.success(this.$t('dataTable.updateSuccess'));
          this.getList();
          this.dialogVisible = false;
        }
      }).catch(err => {
        Message.error(this.$t('dataTable.updateError') + err);
      });
    },
    selectData() {
      this.loading = true;
      pageListChatBiTableData(this.pageParams, {
        dataSourceId: this.dataSource,
        tableName: this.searchValue
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
    handleInputSearch(val) {
      this.loading = true;
      pageListChatBiTableData(this.pageParams, {
        dataSourceId: this.dataSource,
        tableName: val,
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
    handleSelectTable() {
      if (this.tableSelectIds.length === 0) {
        Message.warning(this.$t('dataTable.pleaseTableData'));
        return;
      }
      syncTableSelect({ids:this.tableSelectIds}).then(res => {
        if (res.code === 200) {
          Message.success(res.msg);
          this.getList();
        }
      }).catch(err => {
        Message.error(this.$t('dataBase.syncDataSourceError') + err);
      });
    },
    handleSelectDeleted() {
      if (this.tableSelectIds.length === 0) {
        Message.warning(this.$t('dataTable.pleaseTableData'));
      }
      deleteTableSelectData({ids: this.tableSelectIds}).then(res => {
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
