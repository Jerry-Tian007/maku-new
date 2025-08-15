<template>
  <div class="app-container" v-loading.fullscreen.lock="fullscreenLoading">
    <el-card>
      <el-form :model="pageParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="58px">
        <el-row :gutter="10">
          <el-col :span="9">
            <el-form-item :label="$t('name')" prop="name" class="no-bottom">
              <el-input v-model="pageParams.cnName" :placeholder="$t('knowledgeGraph.knowledgeNamePlaceholder')" clearable
                style="width: 300px" @keyup.enter.native="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item :label="$t('knowledgeGraph.remark')" prop="cnName" class="no-bottom">
              <el-input v-model="pageParams.des" :placeholder="$t('knowledgeGraph.knowledgeRemarkPlaceholder')"
                clearable style="width: 300px" @keyup.enter.native="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item class="no-bottom">
              <el-button class="secondary" icon="el-icon-refresh" size="medium" @click="resetQuery"
                style="color:white;">{{ $t('rest') }}
              </el-button>
              <el-button type="primary" icon="el-icon-search" size="medium" @click="handleQuery" style="">{{
                $t('search') }}
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <el-card class="mt20">
      <div class="flex-layout align-center start-end" style="margin-bottom: 10px;">
        <el-button class="blue" icon="el-icon-refresh" @click="handleSync">{{ $t('knowledgeGraph.syncKnowledge') }}
        </el-button>
      </div>
      <el-table v-loading="loading" class="color-table" :data="kbList" :row-style="{ height: '50px' }"
        @selection-change="handleSelectionChange">
        <template slot="empty">
          <Empty />
        </template>
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column :label="$t('knowledgeGraph.knowledgeId')" align="center" prop="spaceId"
          show-overflow-tooltip />
        <el-table-column :label="$t('knowledgeGraph.name')" align="center" prop="cnName" show-overflow-tooltip />
        <el-table-column :label="$t('knowledgeGraph.remark')" align="center" prop="des" show-overflow-tooltip />
        <el-table-column :label="$t('knowledgeGraph.syncTime')" align="center" prop="createTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 10px;" align="right">
        <pagination v-show="total > 0" :total="total" :page.sync="pageParams.pageNum" :limit.sync="pageParams.pageSize"
          @pagination="getList" popper-class="color-pagination" background layout="total, sizes, prev, pager, next" />
      </div>
    </el-card>


    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('kbManager.kbName')" prop="kbName">
          <el-input v-model="form.kbName" :placeholder="$t('kbManager.namePlaceholder')" maxlength="30" />
        </el-form-item>
        <el-form-item :label="$t('kbManager.kbDescription')" prop="kbDescription">
          <el-input :rows="4" resize="none" type="textarea" v-model="form.kbDescription"
            :placeholder="$t('kbManager.kbDescriptionPlaceholder')"></el-input>
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
import { getToken } from "@/utils/auth";
import { addKB, delKB, delMutiKB, editKB, getKB, listKB } from "@/api/knowledgeBase/kbManager";

import { findList, syncGraph } from '@/api/knowledgeBase/knowledgeGraph';

export default {
  name: "knowledgeGraphManager",
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

      pageParams: {
        pageNum: 1,
        pageSize: 200,
        des: '',
        cnName: ''
      },
      // 查询参数
      queryParams: {
        kbName: null,
      },
      // 表单校验
      rules: {
        kbName: [
          { required: true, message: this.$t('kbManager.namePlaceholder'), trigger: "blur" }
        ],
        kbDescription: [
          { required: true, message: this.$t('kbManager.kbDescriptionPlaceholder'), trigger: "blur" }
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
      findList(this.pageParams).then(response => {
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
    handleSync() {
      syncGraph().then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess(this.$t('knowledgeGraph.syncSuccess'));
          this.getList();
        }
      }
      ).catch(() => {
        this.$modal.msgSuccess(this.$t('knowledgeGraph.syncError'));
      });
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
            let data = {
              id: this.form.id,
              kbName: this.form.kbName,
              kbDescription: this.form.kbDescription,
            }
            this.fullscreenLoading = true;
            editKB(data).then(() => {
              this.getList();
              this.$modal.msgSuccess(this.$t('updateSuccess'));
              this.open = false;
              this.fullscreenLoading = false;
            }).catch(err => {
              this.fullscreenLoading = false;
            });
          } else {
            addKB(this.form).then(() => {
              this.getList();
              this.$modal.msgSuccess(this.$t('addSuccess'));
              this.open = false;
              this.fullscreenLoading = false;
            }).catch(err => {
              this.fullscreenLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id;
      this.$modal.confirm(this.$t('kbManager.areYouDelete')).then(() => {
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
      this.$modal.confirm(this.$t('kbManager.areYouDelete')).then(() => {
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
  }
};
</script>
