<template>
  <div class="app-container" v-loading.fullscreen.lock="fullscreenLoading">
    <el-card>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
               label-width="68px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('authManager.roleName')" prop="roleName" class="no-bottom" label-width="100px">
              <el-input
                v-model="queryParams.roleName"
                :placeholder="$t('authManager.roleNamePlaceholder')"
                clearable
                style=""
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('authManager.createBy')" prop="createBy" class="no-bottom" >
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
      <div>
        <el-button
          class="blue"
          size="small"
          @click="handleAdd"
          icon="el-icon-plus">
          {{ $t('add') }}
        </el-button>
      </div>
      <el-table
        v-loading="loading"
        class="color-table"
        :data="resourceList"
        style="margin-top:10px;"
        @selection-change="handleSelectionChange"
        :row-style="{height: '50px'}">
        <template slot="empty">
          <Empty/>
        </template>
        <el-table-column
          type="selection"
          width="50"
          align="center"/>
        <el-table-column
          :label="$t('resourceManager.modelName')"
          align="center"
          prop="name"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('resourceManager.tpm')"
          align="center"
          prop="tpm"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('resourceManager.modelType')"
          align="center"
          prop="type"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('resourceManager.modelScene')"
          align="center"
          prop="scene"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <dict-tag :value="scope.row.scene" :options="dict.type.model_scene"/>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('resourceManager.modelParam')"
          align="center"
          prop="parameter"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <el-button type="text" @click="handleShowParams(scope.row.parameter)" v-if="scope.row.parameter">
              {{ $t('historyList.see') }}
            </el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <!--        <el-table-column
                  label="请求参数"
                  align="center"
                  prop="params"
                  show-overflow-tooltip/>-->

        <el-table-column
          :label="$t('historyList.operationTime')"
          align="center"
          prop="createTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('historyList.operator')"
          align="center"
          prop="createBy"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('operation')"
          align="center">
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


    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('resourceManager.modelName')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('resourceManager.modelNamePlaceholder')" clearable
                    style="max-width:220px;"/>
        </el-form-item>
        <el-form-item :label="$t('resourceManager.tpm')" prop="tpm">
          <el-input-number class="main" v-model="form.tpm" :placeholder="$t('resourceManager.tpmPlaceholder')" :min="1"
                           :step="1"/>
        </el-form-item>
        <el-form-item :label="$t('resourceManager.modelType')" prop="type">
          <el-select value-key="type" @change="handleChooseType" v-model="modelTypeInfo"
                     :placeholder="$t('resourceManager.modelTypePlaceholder')">
            <el-option
              v-for="item in modelList"
              :key="item.type"
              :label="item.type"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('resourceManager.modelScene')" prop="scene">
          <el-select class="readonly" v-model="form.scene" :disabled="true">
            <el-option
              v-for="item in dict.type.model_scene"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <div style="width: 100%;padding: 0 40px;">
          <el-divider/>
        </div>

        <div style="margin: 0 50px;" class="blue" v-loading="paramsLoading">
          <template v-if="!paramsList || paramsList.length <1">
            <Empty
              :description="form.type ? $t('resourceManager.noRequiredParam'):$t('resourceManager.pleaseSelectModelType')"/>
          </template>
          <el-form ref="paramsForm" label-position="top">
            <el-form-item
              v-for="(item, index) in paramsList"
              :key="index"
              :label="item.label"
              style="margin-bottom:20px;"
            >
              <el-input v-model="paramsList[index].value[0]" :placeholder="$t('resourceManager.currParamPlaceholder')" clearable/>
            </el-form-item>
          </el-form>
        </div>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
        <el-button @click="cancel">{{ $t('cancel') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t('resourceManager.modelParam')" :visible.sync="showParams" width="50%" append-to-body>
      <Empty v-if="params.length < 1"/>
      <el-form :model="params" label-position="top" style="padding: 0 20px;">
        <el-form-item
          v-for="(item, index) in params"
          :key="item.name"
          :label="item.label">
          <el-input v-model="item.value[0]" readonly/>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import {
  addModelResourceTemplate, getModelResourceTemplate,
  listModelResourceTemplate,
  updateModelResourceTemplate
} from "@/api/modelManager/templateManager";
import {addModelResource, listModelResources, updateModelResource} from "@/api/modelManager/resourceManager";

export default {
  name: "ResourceManager",
  dicts: ['model_scene', 'input_type'],
  computed: {
    modelList() {
      return this.$store.state.user.botList;
    }
  },
  data() {
    return {
      fullscreenLoading: false,
      // 遮罩层
      loading: false,
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
      resourceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],

      // 表单参数
      form: {
        parameter: [],
      },
      defaultProps: {
        children: "children",
        label: "label"
      },

      // 查询参数
      queryParams: {},
      pageParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单校验
      rules: {},

      imageTypes: ['image/jpeg', 'image/png', 'image/jpg', 'image/gif', 'image/svg+xml'],

      params: [],
      showParams: false,

      // modelList: [],

      modelTypeInfo: {},

      paramsList: [],
      paramsLoading: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listModelResources(this.pageParams, this.queryParams).then(response => {
          this.resourceList = response.rows;
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
        parameter: [],
        name: null,
        type: null,
        tpm: null,
      };
      this.paramsList = [];
      this.modelTypeInfo = {};

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
      this.title = this.$t('resourceManager.addModel');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();/*
      getKB(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改模型模板";
      });*/
      let data = {...row};
      if (data.parameter === 'null') data.parameter = null;
      this.paramsList = data.parameter = JSON.parse(data.parameter || '[]');
      this.modelTypeInfo = this.modelList.find(item => item.type === data.type);

      this.form = data;
      this.open = true;
      this.title = this.$t('resourceManager.updateModel');
    },

    /** 提交按钮 */
    submitForm: function () {
      if (this.form.name && this.form.tpm !== null && this.form.type) {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let list = this.paramsList.filter(item => {
              return item.value && item.value.length > 0 && item.value[0];
            });
            if (list.length < this.paramsList.length) {
              this.$message.warning(this.$t('resourceManager.pleaseParam'));
              return;
            }
            this.form.parameter = list;

            let data = {...this.form};
            data.parameter = JSON.stringify(this.form.parameter);
            if (data.parameter === 'null') data.parameter = null;

            if (this.form.id) {
              this.fullscreenLoading = true;
              updateModelResource(data).then(res => {
                if (res.code === 200) {
                  this.$modal.msgSuccess(this.$t('updateSuccess'));
                  this.open = false;
                  this.getList();
                } else {
                  this.$modal.msgError(res.msg);
                  console.log(res.msg);
                }
                this.fullscreenLoading = false;
              }).catch(err => {
                this.fullscreenLoading = false;
              });
            } else {
              this.fullscreenLoading = true;
              addModelResource(data).then(res => {
                if (res.code === 200) {
                  this.$modal.msgSuccess(this.$t('addError'));
                  this.open = false;
                  this.getList();
                } else {
                  this.$modal.msgError(res.msg);
                  console.log(res.msg);
                }
                this.fullscreenLoading = false;
              }).catch(err => {
                this.fullscreenLoading = false;
              });
            }
          }
        });
      } else {
        this.$message.warning(this.$t('resourceManager.pleaseResource'));
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm(this.$t('kbManager.areYouDelete')).then(function () {
        return deleteModelResource(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('deletedSuccess'));
      }).catch(() => {
      });
    },
    handleShowParams(params) {
      this.params = JSON.parse(params);
      this.showParams = true;
    },
    handleChange(file) {
      // file转成base64
      const reader = new FileReader();
      reader.readAsDataURL(file.raw);
      reader.onload = () => {
        this.form.icon = reader.result;
        this.$forceUpdate();
      };
    },
    handleChangeImage() {
      this.$refs.uploadButton.click();
    },
    handleChooseType(value) {
      this.modelTypeInfo = value;
      this.form.scene = value.scene;
      this.form.type = value.type;

      this.paramsLoading = true;
      getModelResourceTemplate(value.id).then(res => {
        if (res.code === 200) {
          this.paramsList = JSON.parse(res.data.template || '[]');
        } else {
          this.$modal.msgError(res.msg);
          console.log(res.msg);
        }
        this.paramsLoading = false;
      }).catch(err => {
        this.paramsLoading = false;
      })

    }
  }
};
</script>
<style scoped lang="scss">
.image-show {
  width: 150px;
  height: 135px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  .image-contaner {
    width: 100%;
    padding: 10px 16px;
    border: 1px dashed #d4d6da;
  }

  .image-change {

  }
}

.image-add {
  width: 150px;
  height: 135px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 1px dashed #d4d6da;
  color: #d4d6da;
  cursor: pointer;
}
</style>

