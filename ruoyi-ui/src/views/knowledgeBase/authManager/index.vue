<template>
  <div class="app-container">
    <el-card>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
               label-width="68px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('authManager.roleName')" label-width="auto" style="display: flex" prop="roleName" class="no-bottom">
              <el-input
                v-model="queryParams.roleName"
                :placeholder="$t('authManager.roleNamePlaceholder')"
                clearable
                style="width: 300px"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('authManager.createBy')" prop="createBy" class="no-bottom">
              <el-input
                v-model="queryParams.createBy"
                :placeholder="$t('authManager.createByPlaceholder')"
                clearable
                style=""
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item class="no-bottom">
              <el-button class="secondary" icon="el-icon-refresh" size="medium" @click="resetQuery"
                         style="color:white;">{{ $t('rest') }}
              </el-button>
              <el-button type="primary" icon="el-icon-search" size="medium" @click="handleQuery"
                         style="">{{ $t('search') }}
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <el-card class="mt20">
      <el-table
        v-loading="loading"
        class="color-table"
        :data="authList"
        :row-style="{height: '50px'}">
        <template slot="empty">
          <Empty/>
        </template>
        <el-table-column
          :label="$t('authManager.roleId')"
          align="center"
          prop="roleId"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('authManager.roleName')"
          align="center"
          prop="roleName"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('authManager.kbName')"
          align="center"
          prop="kbName"
          show-overflow-tooltip>

          <template slot-scope="scope">
            <div style="width: 100%;" align="center">
              <div style="width: fit-content;text-align: center;" align="center">
                <el-popover
                  placement="right"
                  width="200"
                  trigger="hover">

                  <div class="flex-layout column" style="max-height: 200px;overflow-y: auto;">
                    <k-b-radio
                      v-for="(item, index) in scope.row.kbName? scope.row.kbName.split(','):[]"
                      :key="index"
                      :value="item"
                      style="padding: 10px;"/>
                  </div>

                  <div style="width: fit-content;" slot="reference">
                    <el-button type="text">
                  <span class="main-text">{{
                      $t('fileManager.common') + ` ${scope.row.kbName ? scope.row.kbName.split(',').length : 0} ` + $t('fileManager.individual')
                    }}</span>
                      <i class="el-icon-view" style="margin-left: 4px;"/>
                    </el-button>
                  </div>
                </el-popover>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('authManager.operationTime')"
          align="center"
          prop="createTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('authManager.operationBy')"
          align="center"
          prop="createBy"
          show-overflow-tooltip/>
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

  </div>
</template>

<script>
import {getToken} from "@/utils/auth";
import {addKB, delKB, editKB, getKB, listKB} from "@/api/knowledgeBase/kbManager";
import {listAuthorize} from "@/api/modelManager/authorizationManager";
import {listKBAuth} from "@/api/knowledgeBase/kbAuthManager";
import {addDateRange, addStartDateRange} from "@/utils/ruoyi";
import KBRadio from "@/components/KBRadio/index.vue";

export default {
  name: "KBAuthManager",
  components: {KBRadio},
  data() {
    return {
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
      queryParams: {
        kbName: null,
        createBy: null,
      },
      pageParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单校验
      rules: {
        kbName: [
          {required: true, message: this.$t('kbManager.namePlaceholder'), trigger: "blur"}
        ],
        kbDescription: [
          {required: true, message: this.$t('kbManager.kbDescriptionPlaceholder'), trigger: "blur"}
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listKBAuth(this.pageParams, this.queryParams).then(response => {
          this.authList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
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
    /** 搜索按钮操作 */
    handleQuery() {
      this.pageParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
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

  }
};
</script>
<!--

<style lang="scss" scoped>

//.right-toolbar-container {
//  margin-right: -205px;
//  display: flex;
//  justify-content: flex-end;
//}


::v-deep .el-dialog .el-form-item__content {
  margin: 0 25px !important;
}

::v-deep .el-dialog .el-input__inner {
  background-color: #EBF0F9;
}

::v-deep .el-dialog .el-textarea__inner {
  border: none;
  padding: 8px 16px;
  background-color: #EBF0F9;
}

</style>
-->
