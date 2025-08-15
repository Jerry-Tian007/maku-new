<template>
  <el-card class="mt20" v-loading.fullscreen.lock="fullscreenLoading">
    <div class="flex-layout align-center start-end" style="margin-bottom: 10px;">
      <el-button class="blue" icon="el-icon-plus"
                 @click="handleAdd"
      >{{ $t('roleManager.addRole') }}
      </el-button>

      <div class="flex-layout align-center gap8">
        <el-input
          v-model="searchValue"
          :placeholder="$t('dataBase.searchValuePlaceholder')"
          style="width: 200px;"
          size="small"
          clearable
          @clear="handleChangeTable"
          @input="handleChangeTable"
          prefix-icon="el-icon-search"
        />
        <el-popconfirm
          :title="$t('dataBase.deleteSelectedContent')"
          @confirm="handleSelectTable"
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
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >{{ $t('edit') }}
          </el-button>
<!--          <el-popconfirm
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

    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="60%"
      @close="resetForm"
      :close-on-click-modal="false"
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="form"
        :label-width="$i18n.locale === 'zh-CN' ? '100px' : '190px'">
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item :label="$t('authManager.roleName')" prop="roleName">
              <el-input v-model="form.roleName" :placeholder="$t('authManager.roleNamePlaceholder')"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('roleManager.belongDept')" prop="deptId">
              <treeselect v-model="form.deptId"
                          :options="deptOptions"
                          @select="handleChangeDept"
                          :show-count="true"
                          :placeholder="$t('roleManager.belongDeptPlaceholder')"/>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item :label="$t('roleManager.memberSelection')" prop="userIds">
              <el-select
                v-model="form.userIds"
                filterable
                default-first-option
                multiple
                collapse-tags
                value-key="userId"
                @change="handleChangeUser">
                <el-option
                  v-for="(item, index) in userOptions"
                  :key="index"
                  :label="item.nickName"
                  :value="item"/>
              </el-select>
              <el-button style="margin-left: 10px;" type="text" @click="handleClearUsers">{{ $t('AllInOne.clear') }}</el-button>

              <div style="position: relative;">
                <el-card class="blue selected-user" :class="{ expanded: isExpand }">
                  <Empty v-if="form.userIds.length === 0" :description="$t('roleManager.pleaseSelectMember')" :image-size="96"/>
                  <el-tag v-for="(user, index) in form.userIds" style="margin: 0 2px;" :key="index"
                          closable
                          disable-transitions
                          @close="handleCloseTag(user)">
                    {{ user.nickName }}
                  </el-tag>
                </el-card>

                <div class="show-button secondary-text" @click="isExpand = !isExpand">
                  {{ isExpand ? $t('roleManager.retract') : $t('roleManager.expand') }}
                  <i :class="{ 'el-icon-arrow-down': !isExpand, 'el-icon-arrow-up': isExpand }"/>
                </div>
              </div>
            </el-form-item>
            <el-form-item :label="$t('remark')" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="4"
                resize="none"
                :placeholder="$t('dataTable.remarkPlaceholder')"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>
<script>
import {
  rolePageList,
  addRole,
  updateRole,
  deletedRole,
  findDept,
  findSelect,
  findItem,
  findRoleData,
  deletedTableIds
} from '@/api/bi/roleManager/api';
import {Message} from 'element-ui';
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "BIRoleManager",
  components: {
    Treeselect
  },
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

      // 不为多选
      multiple: true,

      searchValue: '',
      dialogVisible: false,
      dialogTitle: '',
      dialogState: '',
      form: {
        roleName: '',
        deptId: null,
        userIds: [],
        remark: '',
      },
      rules: {
        roleName: [
          {required: true, message: this.$t('authManager.roleNamePlaceholder'), trigger: 'blur'}
        ],
        deptId: [
          {required: true, message: this.$t('roleManager.deptPlaceholder'), trigger: 'change'}
        ],
        userIds: [
          {required: true, message: this.$t('roleManager.selectUser'), trigger: 'change'}
        ],
      },
      isExpand: false,
      deptOptions: [],
      userOptions: [],
      initOptionProps: {
        value: "id"
      },
      selectTableData: [],
    };
  },
  mounted() {
    this.getList();
  },
  watch: {
    "form.deptId"(newValue) {
      if (!newValue){
        this.form.userIds = [];
        this.userOptions = [];
      }
    }
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
      rolePageList(pageParams, {roleName: ''}).then(res => {
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
    resetForm() {
      this.form = {
        roleName: '',
        deptId: '',
        userIds: [],
        remark: '',
      };
      this.dialogVisible = false;
    },
    handleAdd() {
      this.dialogTitle = this.$t('roleManager.addRole');
      this.dialogVisible = true;
      this.dialogState = 'add';
      this.findDeptApi();
    },
    findDeptApi() {
      findDept().then(res => {
        if (res.code === 200) {
          this.deptOptions = res.data;
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.queryDeptTreeError') + err);
      });
    },
    handleUpdate(row) {
      findItem(row.id).then(res => {
        if (res.code === 200) {
          findRoleData({deptId: res.data.deptId}).then(roleRes => {
            if (res.code === 200) {
              this.userOptions = roleRes.data;
              const list = [];
              for (const u of res.data.userIds) {
                for (const role of roleRes.data) {
                  if (u === role.userId) {
                    list.push(role);
                  }
                }
              }
              this.form = {
                id: res.data.id,
                userIds: list,
                roleName: res.data.roleName,
                deptId: res.data.deptId,
                remark: row.remark
              }
              this.findDeptApi();
              this.dialogTitle = this.$t('roleManager.updateRole');
              this.dialogState = 'edit';
              this.dialogVisible = true;
            }
          }).catch(err => {
            Message.error(this.$t('roleManager.queryMemberError') + err);
          });

        }
      }).catch(err => {
        Message.error(this.$t('roleManager.queryRoleItemError') + err);
      });
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
    handleFetch(row) {
    },
    submitForm() {
      if (!this.form.deptId) {
        Message.warning(this.$t('roleManager.deptPlaceholder'));
        return;
      }
      if (!this.form.roleName) {
        Message.warning(this.$t('authManager.roleNamePlaceholder'));
        return;
      }
      if (this.form.userIds.length === 0) {
        Message.warning(this.$t('roleManager.pleaseSelectMember'));
        return;
      }
      const list = [];
      for (const u of this.form.userIds) {
        list.push(u.userId);
      }
      this.form.userIds = list;
      if (this.dialogState === 'add') {
        addRole(this.form).then(res => {
          if (res.code === 200) {
            Message.success(this.$t('roleManager.addRoleSuccess'));
            this.resetForm();
            this.getList();
          }
        }).catch(err => {
          Message.error(this.$t('roleManager.addRoleError') + err);
        })
      } else if (this.dialogState === 'edit') {
        updateRole(this.form).then(res => {
          if (res.code === 200) {
            Message.success(this.$t('roleManager.updateRoleSuccess'));
            this.resetForm();
            this.getList();
          }
        }).catch(err => {
          Message.error(this.$t('roleManager.updateRoleError') + err);
        });
      }
    },
    handleClearUsers() {
      this.$confirm(this.$t('roleManager.clearConfirm'), this.$t('warding'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.form.userIds = [];
      }).catch(() => {
      });
    },
    handleCloseTag(user) {
      let index = this.form.userIds.findIndex(item => item.id === user.id);
      this.form.userIds.splice(index, 1);
    },
    handleChangeDept(node) {
      findRoleData({deptId: node.id}).then(res => {
        if (res.code === 200) {
          this.userOptions = res.data;
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.queryMemberError') + err);
      })
    },
    handleChangeUser() {

    },
    handleChangeTable(val) {
      this.loading = true;
      rolePageList(this.pageParams, {roleName: val}).then(res => {
        if (res.code === 200) {
          this.dataList = res.rows;
          this.total = res.total;
          this.loading = false;
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.queryDataError') + err);
      });
    },
    handleSelectTable() {
      deletedTableIds({ids: this.selectTableData}).then(res => {
        if (res.code === 200) {
          Message.success(res.msg);
          this.getList();
        }
      }).catch(err => {
        Message.error(this.$t('roleManager.deletedDataError') + err);
      });
    },
    handleClearDept() {
      this.form.userIds = [];
    }
  }
};
</script>
<style scoped lang="scss">
.show-button {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background: #FFFFFF40;
  text-align: center;
  font-size: 12px;
  cursor: pointer;
}

::v-deep .selected-user {
  margin-top: 10px;
  height: 40px;
  overflow-y: auto;
  transition: all 0.3s;


  .el-card__body {
    opacity: 0;
    height: 0;
    overflow: hidden;
    transition: all 0.3s;
  }

  &.expanded {
    height: 250px;
    overflow: hidden;
    transition: all 0.3s;

    .el-card__body {
      opacity: 1;
      height: 100%;
      overflow: auto;
      transition: all 0.3s;
    }
  }
}
</style>
