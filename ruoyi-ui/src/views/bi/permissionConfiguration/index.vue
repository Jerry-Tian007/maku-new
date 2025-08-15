<template>
  <div class="app-container">
    <div class="top_div">
      <span>
        {{ $t('permissionConfig.selectDept') }}
      </span>
      <treeselect v-model="deptId"
                  style="width: 180px; margin-left: 12px"
                  :options="deptOptions"
                  :show-count="true"
                  :loading="deptLoading"
                  :placeholder="$t('roleManager.belongDeptPlaceholder')"/>
      <!--      <el-cascader
              v-model="deptId"
              clearable
              @change="handleChangeDept"
              :props="initOptionProps"
              :options="deptOptions"/>-->
      <span style="margin-left: 12px">
        {{ $t('permissionConfig.selectRole') }}
      </span>
      <el-select
        v-model="roleId"
        value-key="id"
        clearable
        @close="handleChangeRole"
        @change="handleChangeRole">
        <el-option
          v-for="(item, index) in roleOptions"
          :key="index"
          :label="item.roleName"
          :value="item.id"/>
      </el-select>
    </div>

    <el-card>
      <div class="flex-layout align-center start-end" style="margin-bottom: 10px;">
        <div class="flex-layout align-center gap8">
          <el-button class="blue" icon="el-icon-plus" @click="handleAdd">
            {{ $t('push') }}
          </el-button>
        </div>

        <div class="flex-layout align-center gap8">
          <el-input
            v-model="searchValue"
            :placeholder="$t('dataBase.searchValuePlaceholder')"
            style="width: 200px;"
            size="small"
            clearable
            @input="handleChangeTable"
            prefix-icon="el-icon-search"
          />
          <el-popconfirm
            :title="$t('dataBase.deleteSelectedContent')"
            @confirm="handleDeletedTable"
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
          :label="$t('authManager.roleName')"
          align="center"
          prop="roleName"
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
          :label="$t('roleManager.peopleNumber')"
          align="center"
          prop="headcount"
          show-overflow-tooltip/>
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
          :label="$t('roleManager.dept')"
          align="center"
          prop="deptName"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('operation')"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
          style=""
        >
          <template slot-scope="scope">
            <router-link :to="'/Chatbi/permissionConfigurationItem/' + scope.row.id" class="apply-link-type">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
              >{{ $t('detail') }}
              </el-button>
            </router-link>
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
      <div class="dialog_content_div">
        <div style="padding: 12px 0; display: flex; align-items: center; flex-wrap: wrap; gap: 12px">
          <div style="min-width: 250px">
            <div style="float:left;height: 35px;line-height: 35px; text-align: center">
              {{ $t('permissionConfig.selectDept') }}
            </div>
            <treeselect v-model="addSelectData.deptId"
                        style="width: 180px; margin-left: 6px;float:left"
                        :options="addSelectDataSource.deptData"
                        :show-count="true"
                        :placeholder="$t('roleManager.deptPlaceholder')"/>
          </div>
          <div style="min-width: 250px">
            <span>
            {{ $t('permissionConfig.selectRole') }}
          </span>
            <el-select
              v-model="addSelectData.roleId"
              value-key="id"
              :placeholder="$t('permissionConfig.pleaseSelectRole')"
              style="width: 180px; margin-left: 6px"
              clearable
              @change="handleChangeRoleAndDataSource"
              class="select_mo_ren_style">
              <el-option
                v-for="(item, index) in addSelectDataSource.roleData"
                :key="index"
                :label="item.roleName"
                :value="item.id"/>
            </el-select>
          </div>
          <div style="min-width: 250px">
            <span>
            {{ $t('chatBI.selectDatasource') }}
          </span>
            <el-select
              v-model="addSelectData.dataSourceId"
              value-key="id"
              style="width: 180px; margin-left: 6px"
              clearable
              @change="handleChangeRoleAndDataSource"
              :placeholder="$t('dataTable.dataSourceSelect')"
              class="select_mo_ren_style">
              <el-option
                v-for="(item, index) in addSelectDataSource.dataSources"
                :key="index"
                :label="item.name"
                :value="item.id"/>
            </el-select>
          </div>
          <div style="min-width: 250px">
            <el-input :placeholder="$t('permissionConfig.searchKeyword')"
                      style="width: 200px;margin-left: 12px; border: 1px solid #bababa; border-radius: 6px">
              <el-button slot="append" icon="el-icon-search"/>
            </el-input>
          </div>
        </div>
        <el-table
          v-loading="dialogLoading"
          :data="addTableData"
          @selection-change="handleAddTableSelectionChange">
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
            :label="$t('chatBI.tableName')"
            align="center"
            prop="tableName"
            show-overflow-tooltip/>
        </el-table>
        <div style="margin-top: 10px;" align="right">
          <pagination
            :total="dialogTotal"
            :page.sync="dialogPageParams.pageNum"
            :limit.sync="dialogPageParams.pageSize"
            @pagination="handleDialogPage"
            popper-class="color-pagination"
            background
            layout="total, sizes, prev, pager, next"
          />
        </div>
      </div>
      <div slot="footer">
        <el-button @click="dialogVisible = false">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  findDept,
  findPermissionRole,
  rolePageList,
  findSelectList,
  add,
  roleList,
  deletedTableIds
} from '@/api/bi/permissionConfiguration/api';
import {Message} from 'element-ui';
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {deletedRole} from "@/api/bi/roleManager/api";


export default {
  name: "permissionConfiguration",
  components: {
    Treeselect
  },
  data() {
    return {
      loading: false,
      dialogLoading: false,
      fullscreenLoading: false,
      deptId: null,
      deptLoading: true,
      roleId: '',
      deptOptions: [],
      initOptionProps: {
        value: "id"
      },
      roleOptions: [],
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
        tableDescription: '',
      },
      dialogTotal: 0,
      dialogPageParams: {
        pageNum: 1,
        pageSize: 10
      },

      rules: {
        tableName: [
          {required: true, message: this.$t('dataTable.dataTableNamePlaceholder'), trigger: 'blur'}
        ],
        tableDescription: [
          {required: true, message: this.$t('dataTable.tableDescriptionPlaceholder'), trigger: 'blur'}
        ],
      },
      addSelectData: {
        deptId: null,
        roleId: '',
        dataSourceId: '',
        tableIds: []
      },
      addSelectDataSource: {
        deptData: [],
        roleData: [],
        dataSources: []
      },
      addTableData: [],
      selectTableData: []
    };
  },
  mounted() {
    this.getDept();
  },
  watch: {
    deptId(newValue) {
      if (newValue) {
        this.getRole('table', newValue);
      } else {
        this.roleOptions = [];
      }
    },
    "addSelectData.deptId"(newValue) {
      if (newValue) {
        this.getRole('dialog', newValue);
      }
    }
  },
  methods: {
    getList(e) {
      this.loading = true;
      let params = {};
      if (e) {
        params = {
          pageSize: e.limit,
          pageNum: e.page
        }
      } else {
        params = this.pageParams;
      }
      rolePageList(params, {roleName: ''}).then(res => {
        if (res.code === 200) {
          this.dataList = res.rows;
          this.total = res.total;
          this.loading = false;
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.queryDataError') + err);
      });
    },
    getDept() {
      findDept().then(res => {
        if (res.code === 200) {
          this.deptOptions = res.data;
          this.deptLoading = false;
        }
      }).catch(err => {
        Message.error(this.$t('permissionConfig.queryDeptListError') + err);
      });
      this.loading = true;
      rolePageList(this.pageParams, {roleName: ''}).then(res => {
        if (res.code === 200) {
          this.dataList = res.rows;
          this.total = res.total;
          this.loading = false;
        }
      }).catch(err => {
        this.loading = false;
        Message.error(this.$t('roleManager.queryDataError') + err);
      });
    },
    getRole(type, deptId) {
      roleList({roleName: '', deptId}).then(res => {
        if (res.code === 200) {
          if (type === 'table') {
            this.roleOptions = res.rows;
          }
          if (type === 'dialog') {
            this.addSelectDataSource.roleData = res.rows;
          }
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.queryDataError') + err);
      });
    },
    handleSelectionChange(val) {
      if (val.length > 0) {
        this.multiple = false;
        this.selectTableData = [];
        val.forEach((item) => this.selectTableData.push(item.id));
      } else {
        this.multiple = true;
        this.selectTableData = [];
      }
    },
    handleDelete(row) {
      deletedRole(row.id).then(res => {
        if (res.code === 200) {
          Message.success(this.$t('roleManager.deletedDataSuccess'));
          this.getList();
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.deletedDataError') + err);
      });
    },
    handleAdd() {
      this.dialogTitle = this.$t('permissionConfig.tableSelect');
      this.dialogVisible = true;
      this.findThree();
    },
    findThree() {
      findDept().then(res => {
        if (res.code === 200) {
          this.addSelectDataSource.deptData = res.data;
        }
      }).catch(err => {
        Message.error(this.$t('permissionConfig.queryDeptListError') + err);
      })
      findSelectList({name: ''}).then(res => {
        if (res.code === 200) {
          this.addSelectDataSource.dataSources = res.data;
        }
      }).catch(err => {
        Message.error(this.$t('configuration.queryDataSourceError') + err);
      })
    },
    resetForm() {
      this.dialogVisible = false;
      this.addSelectData = {
        deptId: null,
        roleId: '',
        dataSourceId: '',
        tableIds: []
      };
      this.addSelectDataSource = {
        deptData: [],
        roleData: [],
        dataSources: []
      };
      this.addTableData = [];
    },
    submitForm() {
      if (!this.addSelectData.roleId) {
        Message.warning(this.$t('permissionConfig.roleDataNoNull'));
        return;
      }
      if (!this.addSelectData.dataSourceId) {
        Message.warning(this.$t('permissionConfig.dataNoNull'));
        return;
      }
      if (this.addSelectData.tableIds.length === 0) {
        Message.warning(this.$t('permissionConfig.pleaseSelectTableData'));
        return;
      }
      add({
        chatBiRoleId: this.addSelectData.roleId,
        chatBiDataSourceId: this.addSelectData.dataSourceId,
        chatBiDataTableIds: this.addSelectData.tableIds
      }).then(res => {
        if (res.code === 200) {
          Message.success(res.msg);
          this.resetForm();
        }
      }).catch(err => {
        Message.error(this.$t('permissionConfig.pushDataError') + err);
      });
    },
    handleChangeDept() {
      if (this.deptId) {
        this.getRole('table', this.deptId);
      }
    },
    handleChangeRole() {
      this.loading = true;
      rolePageList(this.pageParams, {id: this.roleId, deptId: this.deptId, roleName: this.searchValue}).then(res => {
        if (res.code === 200) {
          this.dataList = res.rows;
          this.total = res.total;
          this.loading = false;
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.queryDataError') + err);
      });
    },
    handleChangeRoleAndDataSource() {
      if (!this.addSelectData.roleId) {
        return;
      }
      if (!this.addSelectData.dataSourceId) {
        return;
      }
      this.dialogLoading = true;
      findPermissionRole(this.dialogPageParams, {
        chatBiRoleId: this.addSelectData.roleId,
        chatBiDataSourceId: this.addSelectData.dataSourceId
      }).then(res => {
        if (res.code === 200) {
          this.addTableData = res.rows;
          this.dialogTotal = res.total;
          this.dialogLoading = false;
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.queryDataError') + err);
      });
    },
    handleDialogPage(e) {
      if (!this.addSelectData.roleId) {
        return;
      }
      if (!this.addSelectData.dataSourceId) {
        return;
      }
      this.dialogLoading = true;
      findPermissionRole({
        pageNum: e.page,
        pageSize: e.limit
      }, {
        chatBiRoleId: this.addSelectData.roleId,
        chatBiDataSourceId: this.addSelectData.dataSourceId
      }).then(res => {
        if (res.code === 200) {
          this.addTableData = res.rows;
          this.dialogTotal = res.total;
          this.dialogLoading = false;
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.queryDataError') + err);
      });
    },
    handleAddTableSelectionChange(val) {
      val.forEach((item) => {
        this.addSelectData.tableIds.push(item.tableId)
      });
    },
    handleChangeTable(val) {
      this.dialogLoading = true;
      rolePageList(this.pageParams, {
        id: this.roleId,
        deptId: this.deptId,
        roleName: val
      }).then(res => {
        if (res.code === 200) {
          this.dataList = res.rows;
          this.total = res.total;
          this.dialogLoading = false;
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.queryDataError') + err);
      });
    },
    handleDeletedTable() {
      if (this.selectTableData.length === 0) {
        Message.warning(this.$t('dataTable.pleaseTableData'));
        return;
      }
      deletedTableIds({ids: this.selectTableData}).then(res => {
        if (res.code === 200) {
          Message.success(res.msg);
          this.getList();
        }
      }).catch(err => {
        Message.error(this.$t('dataBase.deletedError') + err);
      });
    },
    submitAuthKB() {

    },
  }
}
</script>

<style scoped lang="scss">
.dialog_content_div {
  width: 100%;
  display: flex;
  flex-direction: column;
}

.select_mo_ren_style {
  border: 1px solid #bababa;
  border-radius: 6px;
}

.top_div {
  height: 100px;
  width: 100%;
  background: white;
  border-radius: 6px;
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  padding: 12px 12px;
}
</style>
