<template>
  <div class="app-container" v-loading.fullscreen.lock="fullscreenLoading">
    <el-card>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
               label-width="58px" @submit.native.prevent>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('name')" prop="kbName" class="no-bottom">
              <el-input
                v-model="queryParams.kbName"
                :placeholder="$t('kbManager.namePlaceholder')"
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
      <div class="flex-layout align-center start-end" style="margin-bottom: 10px;">
        <el-button class="blue" icon="el-icon-plus"
                   @click="handleAdd"
        >{{ $t('kbManager.addKb') }}
        </el-button>

        <el-button class="red" disabled
                   plain
                   icon="el-icon-delete"
                   :disabled="multiple"
                   @click="handleMutiDelete"
        >{{ $t('deleted') }}
        </el-button>
      </div>
      <el-table
        v-loading="loading"
        class="color-table"
        :data="kbList"
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
          :label="$t('kbManager.kbName')"
          align="center"
          prop="kbName"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <el-button type="text" @click="handleSearchFiles(scope.row.kbName)">
              {{ scope.row.kbName }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('kbManager.kbDescription')"
          align="center"
          prop="kbDescription"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('kbManager.fileNum')"
          align="center"
          prop="fileNum"
          show-overflow-tooltip/>
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
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >{{ $t('deleted') }}
            </el-button>
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


    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('kbManager.kbName')" prop="kbName">
          <el-input v-model="form.kbName" :placeholder="$t('kbManager.namePlaceholder')" maxlength="30" />
        </el-form-item>
        <el-form-item :label="$t('kbManager.kbDescription')" prop="kbDescription">
          <el-input :rows="4" resize="none" type="textarea" v-model="form.kbDescription" :placeholder="$t('kbManager.kbDescriptionPlaceholder')"></el-input>
        </el-form-item>
        <el-form-item :label="$t('kbManager.prompt')" prop="promptIds">
          <el-select v-model="form.promptIds" multiple :placeholder="$t('kbManager.promptPlaceholder')" style="width: 100%">
            <el-option v-for="item in promptList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
        <el-button @click="cancel">{{ $t('cancel') }}</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import {getToken} from "@/utils/auth";
import {addKB, delKB, delMutiKB, editKB, getKB, listKB} from "@/api/knowledgeBase/kbManager";
import {kbManagerSelectPromptList} from "@/api/kbChat/index";

export default {
  name: "KBManager",
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
      kbList: null,
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
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listKB(this.pageParams, this.queryParams).then(response => {
          this.kbList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      ).catch(() => {
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
        promptIds: []
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
      this.ids = selection.map(item => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      kbManagerSelectPromptList().then(response => {
          this.promptList = response.data;
        }
      ).catch(() => {
        console.error("提示词加载失败")
      });
      this.open = true;
      this.title = this.$t('kbManager.addKb');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      Promise.all([
        kbManagerSelectPromptList(row.id),
        getKB(row.id)
      ]).then(([promptResponse, kbResponse]) => {
        this.promptList = promptResponse.data;
        const kbData = kbResponse.data;
        this.form = {
          ...kbData,
          promptIds: this.promptList
            .filter(item => item.kbManagerId === kbData.id)
            .map(item => item.id)
        };
        this.open = true;
        this.title = this.$t('kbManager.updateKb');
      }).catch(() => {
        console.error("数据加载失败")
      });
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            let data = {
              id: this.form.id,
              kbName: this.form.kbName,
              kbDescription: this.form.kbDescription,
              promptIds: this.form.promptIds
            }
            this.fullscreenLoading = true;
            editKB(data).then(() => {
              this.getList();
              this.$modal.msgSuccess(this.$t('updateSuccess'));
              this.open = false;
              this.fullscreenLoading = false;
            }).catch(err =>{
              this.fullscreenLoading = false;
            });
          } else {
            addKB(this.form).then(() => {
              this.getList();
              this.$modal.msgSuccess(this.$t('addSuccess'));
              this.open = false;
              this.fullscreenLoading = false;
            }).catch(err =>{
              this.fullscreenLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id;
      this.$modal.confirm(this.$t('kbManager.areYouDelete')).then(()=>{
        this.fullscreenLoading = true;
        delKB(ids).then(() => {
          this.fullscreenLoading = false;
          this.getList();
          this.$modal.msgSuccess(this.$t('deletedSuccess'));
        }).catch(() => {
          this.fullscreenLoading = false;
        });
      }).catch(() => {
      });
    },

    /** 删除按钮操作 */
    handleMutiDelete() {
      const ids = this.ids;
      this.$modal.confirm(this.$t('kbManager.areYouDelete')).then(()=>{
        this.fullscreenLoading = true;
        delMutiKB(ids).then(() => {
          this.fullscreenLoading = false;
          this.getList();
          this.$modal.msgSuccess(this.$t('deletedSuccess'));
        }).catch(() => {
          this.fullscreenLoading = false;
        });
      }).catch(() => {
      });
    },
    handleSearchFiles(name){
      this.$router.push({path: '/data/FileManager', query: {kbName: name}})
    }
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
