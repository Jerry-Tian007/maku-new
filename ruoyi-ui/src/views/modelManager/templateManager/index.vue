<template>
  <div class="app-container">
    <el-card>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
               label-width="100px">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item :label="$t('templateManager.modelType')" prop="type" class="no-bottom" label-width="100px">
              <el-input
                v-model="queryParams.type"
                :placeholder="$t('templateManager.modelTypePlaceholder')"
                clearable
                style=""
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('templateManager.modelScene')" prop="scene" class="no-bottom" label-width="110px">
              <el-select
                v-model="queryParams.scene"
                :placeholder="$t('templateManager.modelScenePlaceholder')"
                clearable
                style=""
                @change="handleQuery"
              >
                <el-option
                  v-for="item in dict.type.model_scene"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
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
        :data="templateList"
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
          :label="$t('templateManager.modelType')"
          align="center"
          prop="type"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('templateManager.modelIcon')"
          align="center"
          prop="icon"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <img v-if="scope.row.icon" :src="scope.row.icon" width="20px" height="20px"
                 style="border-radius: 50%;object-fit: cover;"/>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('templateManager.modelScene')"
          align="center"
          prop="scene"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <dict-tag :value="scope.row.scene" :options="dict.type.model_scene"/>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('templateManager.modelTemplate')"
          align="center"
          prop="template"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <el-button type="text" @click="handleShowTemplates(scope.row.template)" v-if="scope.row.template">
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
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item :label="$t('templateManager.modelType')" prop="modelType">
          <el-input v-model="form.type" style="width:220px;" :placeholder="$t('templateManager.modelTypePlaceholder')"
                    clearable/>
          <el-popover
            v-show="!form.id"
            placement="bottom-start"
            width="240"
            trigger="hover">

            <div style="max-height:200px;overflow-y: auto;">
              <div
                v-for="item in modelList"
                :key="item.type"
                style="padding: 8px 12px;border-radius:4px;cursor: pointer;"
                class="model-item"
                :title="item.type"
                @click="handleCommand(item.type)"
              >
                <div class="flex-layout align-center gap8">
                  <img width="14" height="14" style="border-radius: 10px;" :src="item.icon || placeholder"/>
                  <div class="text-over flex-1">{{ item.type }}</div>
                </div>
              </div>
            </div>

            <el-button
              slot="reference"
              style="margin-left:10px;"
              class="purple"
              icon="el-icon-files">
              {{ $t('templateManager.copyFromExistingTemplate') }}
            </el-button>

          </el-popover>
        </el-form-item>
        <el-form-item :label="$t('templateManager.modelIcon')" prop="icon">
          <el-upload
            v-show="false"
            action=""
            :auto-upload="false"
            :show-file-list="false"
            :accept="imageTypes.join(',')"
            :on-change="handleChange"
          >
            <div ref="uploadButton"></div>
          </el-upload>
          <div class="image-show" v-if="form.icon">
            <div class="image-contaner">
              <el-image :src="form.icon" style="width: 64px; height: 64px;border-radius: 10px;"/>
              <el-image :src="form.icon" style="width: 42px; height: 42px;border-radius: 50%;margin-left: 10px;"/>
            </div>
            <el-button type="text" class="image-change" @click="handleChangeImage">{{
                $t('templateManager.replaceImage')
              }}
            </el-button>
          </div>
          <div class="image-add" @click="handleChangeImage" v-else>
            <i class="el-icon-plus" style="font-size: 32px;"/>
          </div>
        </el-form-item>
        <el-form-item :label="$t('templateManager.modelScene')" prop="scene">
          <el-select class="color-select" v-model="form.scene"
                     :placeholder="$t('templateManager.modelScenePlaceholder')" clearable>
            <el-option
              v-for="item in dict.type.model_scene"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('templateManager.modelTemplate')" prop="template">
          <div
            class="flex-layout align-center gap10 pd-v6">
            <div class="flex-1" align="center">{{ $t('templateManager.parameter') }}</div>
            <div class="flex-1" align="center">{{ $t('templateManager.parameterKeyName') }}</div>
            <div class="flex-1" align="center">{{ $t('templateManager.inputMethod') }}</div>
            <div class="flex-1" align="center">{{ $t('templateManager.encrypt') }}</div>
            <div class="flex-1"></div>
          </div>

          <div
            class="flex-layout align-center gap10 pd-v6"
            v-for="(row, rowIndex) in form.template"
            :key="rowIndex"
          >
            <el-input class="flex-1" v-model="row.label" :placeholder="$t('templateManager.parameter')" clearable/>
            <el-input class="flex-1" v-model="row.name" :placeholder="$t('templateManager.parameterKeyName')" clearable/>
            <!--            <el-input class="flex-1" v-model="row.type" placeholder="输入方式" clearable/>-->
            <el-select
              class="flex-1"
              v-model="row.type"
              :placeholder="$t('templateManager.inputMethod')"
              clearable>
              <el-option
                v-for="item in dict.type.input_type"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <el-switch
              class="flex-1"
              v-model="row.encrypt"
              :active-text="$t('correct')"
              :inactive-text="$t('deny')"
              :active-value="1"
              :inactive-value="0"
              style="width:88px;"
            ></el-switch>
            <el-button
              @click="handleDeleteRow(rowIndex)"
              class="red"
              size="small"
              icon="el-icon-delete">
              {{ $t('templateManager.currentLine') }}
            </el-button>
          </div>
          <div style="width:100%;" class="pd-v6">
            <el-button
              style="width:100%;"
              @click="handleAddRow"
              class="blue"
              icon="el-icon-plus">
              {{ $t('add') }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
        <el-button @click="cancel">{{ $t('cancel') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t('templateManager.modelTemplate')" :visible.sync="showTemplates" width="50%" append-to-body>
      <div
        class="flex-layout align-center gap10 pd-v6">
        <div class="flex-1" align="center">{{ $t('templateManager.parameter') }}</div>
        <div class="flex-1" align="center">{{ $t('templateManager.parameterKeyName') }}</div>
        <div style="width: 110px;" align="center">{{ $t('templateManager.inputMethod') }}</div>
        <div style="width: 110px;" align="center">{{ $t('templateManager.encrypt') }}</div>
      </div>

      <Empty v-if="templates.length < 1"/>

      <div
        class="flex-layout align-center gap10 pd-v6"
        v-for="(row, rowIndex) in templates"
        :key="rowIndex"
      >
        <el-input readonly class="flex-1" v-model="row.label" :placeholder="$t('templateManager.parameter')" clearable/>
        <el-input readonly class="flex-1" v-model="row.name" :placeholder="$t('templateManager.parameterKeyName')" clearable/>
        <!--            <el-input class="flex-1" v-model="row.type" placeholder="输入方式" clearable/>-->
        <el-input readonly style="width: 110px;" v-model="row.type" :placeholder="$t('templateManager.inputMethod')" clearable/>
        <el-tag :type="row.encrypt ? 'success' : 'primary'" style="width: 110px;text-align: center;">
          {{ row.encrypt ? $t('correct') : $t('deny') }}
        </el-tag>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  addModelResourceTemplate, deleteModelResourceTemplate, getModelResourceTemplate,
  listModelResourceTemplate,
  updateModelResourceTemplate
} from "@/api/modelManager/templateManager";
import placeholder from "@/assets/logo/logo-placeholder.jpg";

export default {
  name: "TemplateManager",
  dicts: ['model_scene', 'input_type'],
  computed: {
    modelList() {
      return this.$store.state.user.botList;
    }
  },
  data() {
    return {
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
      templateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],

      // 表单参数
      form: {
        template: [
          {
            label: '',
            name: '',
            type: '',
            encrypt: 0,
            value: []
          }
        ],
      },
      defaultProps: {
        children: "children",
        label: "label"
      },

      // 查询参数
      queryParams: {
        type: null,
        scene: null
      },
      pageParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单校验
      rules: {},

      imageTypes: ['image/jpeg', 'image/png', 'image/jpg', 'image/gif', 'image/svg+xml'],

      templates: [],
      showTemplates: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listModelResourceTemplate(this.pageParams, this.queryParams).then(response => {
          this.templateList = response.rows;
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
        template: [
          {
            label: '',
            name: '',
            type: '',
            encrypt: 0,
            value: []
          }
        ]
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
      this.title = this.$t('templateManager.addModelTemplate');
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
      if (data.template === 'null') data.template = null;
      data.template = JSON.parse(data.template || '[]');
      this.form = data;
      this.open = true;
      this.title = this.$t('templateManager.modifyModelTemplate');
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          let list = this.form.template.filter(item => {
            return item.label || item.name || item.type
          });
          if (list.length === 0) {
            this.$message.warning(this.$t('templateManager.pleaseAddModelTemplate'));
            return;
          }
          this.form.template = list;

          let data = {...this.form};
          data.template = JSON.stringify(this.form.template);
          if (data.template === 'null') data.template = null;
          if (this.form.id != undefined) {
            // 修改
            updateModelResourceTemplate(data).then(res => {
              if (res.code === 200) {
                this.getList();
                this.$modal.msgSuccess(this.$t('updateSuccess'));
                this.open = false;
              } else {
                this.$modal.msgError(res.msg);
                console.log(res.msg);
              }
            }).catch(err => {
              this.$modal.msgError(this.$t('updateError'));
              console.log(err);
            });
          } else {
            // 新增
            addModelResourceTemplate(data).then(res => {
              if (res.code === 200) {
                this.getList();
                this.$modal.msgSuccess(this.$t('addSuccess'));
                this.open = false;
              } else {
                this.$modal.msgError(res.msg);
                console.log(res.msg);
              }
            }).catch(err => {
              this.$modal.msgError(this.$t('addError'));
              console.log(err);
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm(this.$t('kbManager.areYouDelete')).then(function () {
        return deleteModelResourceTemplate(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('deletedSuccess'));
      }).catch(() => {
      });
    },
    handleShowTemplates(templates) {
      this.templates = JSON.parse(templates);
      this.showTemplates = true;
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
    handleAddRow() {
      this.form.template.push({
        label: '',
        name: '',
        type: '',
        encrypt: 0,
        value: []
      });
    },
    handleDeleteRow(rowIndex) {
      this.form.template.splice(rowIndex, 1);
    },
    handleCommand(command) {
      let model = this.modelList.find(item => item.type === command);
      if (model) {
        getModelResourceTemplate(model.id).then(res => {
          let data = res.data;
          if (data.template === 'null') data.template = null;
          data.template = JSON.parse(data.template || '[]');
          this.form.template = data.template;
          this.form.scene = data.scene;
          this.form.icon = model.icon;
        });
      }
    }
  },
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

