<template>
  <div class="app-container">
    <el-card>
      <el-table v-loading="loading"
                class="color-table"
                :data="dataList"
                :row-style="{height: '50px'}">
        <el-table-column
          type="index"
          :label="$t('chatBI.index')"
          width="60"
          align="center"/>
        <el-table-column
          :label="$t('chatBI.tableName')"
          align="center"
          prop="tableName"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('chatBI.dataSource')"
          align="center"
          prop="dataSourceName"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('kbManager.createBy')"
          prop="createBy"
          align="center"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('kbManager.createTime')"
          align="center"
          prop="createTime"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('operation')"
          align="center"
          prop="tableDescription"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="handleColumnAuth(scope.row, true)"
            >{{ $t('permissionConfig.rowDetail') }}
            </el-button>
            <el-button
              size="mini"
              type="text"
              style="margin-left: 6px"
              icon="el-icon-collection"
              @click="handleColumnAuth(scope.row, false)"
            >{{ $t('permissionConfig.rowAuth') }}
            </el-button>
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
    <el-dialog :visible.sync="showKB" width="50%" @close="cancelAuthKB" append-to-body>
      <div style="width: 100%;" align="center">
        <span class="dialog-title">{{ $t('configuration.roleAuthorization') }}</span>
      </div>
      <k-b-select v-model="kbList" :tableId="currTableId" style="height: 50vh;margin-top: 20px;"/>

      <div slot="footer" class="dialog-footer" align="center">
        <el-button class="secondary" @click="cancelAuthKB">{{ $t('cancel') }}</el-button>
        <el-button v-show="!currDialogState" type="primary" @click="submitAuthKB">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {columnAuth, findTable, addColumnAuth} from '@/api/bi/permissionConfiguration/api';
import {Message} from 'element-ui';
import KBSelect from "./columnAuth";

export default {
  name: "permissionConfigurationItem",
  components: {KBSelect},
  data() {
    return {
      dataList: [],
      total: 0,
      loading: false,
      pageParams: {
        pageNum: 1,
        pageSize: 10
      },
      showKB: false,
      kbList: [],
      submitRoleIds: [],
      currTableId: '',
      currDialogState: false
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      findTable(this.$route.params.id, this.pageParams).then(res => {
        if (res.code === 200) {
          this.dataList = res.rows;
          this.total = res.total;
          this.loading = false;
        }
      }).catch(err => {
        this.loading = false;
        Message.error(this.$t('permissionConfig.queryDetailError') + err);
      });
    },
    handleSelectionChange(val) {
    },
    handleDelete(row) {

    },
    handleAdd() {
      this.dialogTitle = this.$t('permissionConfig.tableSelect');
      this.dialogVisible = true;
    },
    resetForm() {
      this.$refs.form.clearValidate();
      this.form = {
        tableName: '',
        tableDescription: '',
      };
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.dialogVisible = false;
        }
      });
    },
    handleColumnAuth(row, state) {
      this.currDialogState = state;
      this.currTableId = row.tableId;
      columnAuth({tableId: row.tableId, roleId: this.$route.params.id, fieldName: ''}).then(res => {
        if (res.code === 200) {
          if (res.data.length > 0) {
            res.data.forEach((item) => {
              this.kbList.push(item.fieldId);
              this.submitRoleIds.push(item.fieldId);
            });
          }
          this.showKB = true;
        }
      }).catch(err => {
        Message.error(this.$t('configuration.queryUnauthorizedError') + err);
      });
    },
    submitAuthKB() {
      const subData = {
        dataSourceTableId: this.currTableId,
        chatBiRoleId: this.$route.params.id,
        dataSourceFieldIds: this.kbList
      }
      addColumnAuth(subData).then(res => {
        if (res.code === 200) {
          Message.success(res.msg);
          this.showKB = false;
        }
      }).catch(err => {
        Message.error(this.$t('permissionConfig.updateRowAuthError') + err);
      });
    },
    cancelAuthKB() {
      this.showKB = false;
      this.kbList = [];
      this.currTableId = '';
      this.submitRoleIds = [];
      this.currDialogState = false;
    }
  }
}
</script>

<style scoped>

</style>
