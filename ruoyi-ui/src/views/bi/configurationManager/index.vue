<template>
  <div class="app-container">
    <div class="top_div">
      <span style="margin-right: 12px">
        {{ $t('chatBI.selectDatasource') }}
      </span>
      <el-select
        v-model="dataSource"
        value-key="id"
        clearable
        @close="handleChangeRole"
        @change="handleChangeRole">
        <el-option
          v-for="(item, index) in dataSources"
          :key="index"
          :label="item.name"
          :value="item.id"/>
      </el-select>
    </div>

    <el-card v-loading.fullscreen.lock="fullscreenLoading">
      <div class="flex-layout align-center start-end" style="margin-bottom: 10px;">
        <div class="flex-layout align-center gap8">
          <el-button class="blue" icon="el-icon-plus" @click="handleAdd">
            {{ $t('add') }}
          </el-button>
        </div>

        <div class="flex-layout align-center gap8">
          <el-input
            v-model="searchValue"
            :placeholder="$t('dataBase.searchValuePlaceholder')"
            style="width: 200px;"
            size="small"
            clearable
            @clear="getList"
            @keyup.enter="getList"
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
          :label="$t('configuration.configName')"
          align="center"
          prop="functionName"
          show-overflow-tooltip/>
        <el-table-column
          :label="$t('configuration.configRemark')"
          align="center"
          prop="functionDescribe"
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
          width="200"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleConfiguration(scope.row)"
            >{{ $t('configuration.authConfig') }}
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >{{ $t('update') }}
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
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="resetForm"
    >
      <div class="dialog_content_div">
        <div class="content_left_div">
          <div class="left_top_div">
            <div class="wen_ti_input_div">
              <span class="span_title" :style="{width: $i18n.locale === 'zh-CN' ? '90px' : '160px'}">
                {{ $t('configuration.problemInput') }}
              </span>
              <el-input class="input_text"
                        v-model="form.question"
                        :autosize="{ minRows: 3, maxRows: 3}"/>
              <el-button type="text" style="width: 30px">{{ $t('configuration.giveExample') }}</el-button>
            </div>
            <div class="wen_ti_input_div">
              <span class="span_title" :style="{width: $i18n.locale === 'zh-CN' ? '90px' : '160px'}">
                {{ $t('configuration.sqlStatementInput') }}
              </span>
              <el-input class="input_text"
                        v-model="form.sqlText"
                        :autosize="{ minRows: 3, maxRows: 3}"/>
              <div style="width: 30px"/>
            </div>
            <el-button class="blue" @click="functionCall">{{ $t('configuration.intelligentGeneration') }}</el-button>
          </div>
          <div class="left_bottom_div">
            <div class="bottom_top_div">
              <div class="bottom_input_span">
                <span class="span_title" :style="{width: $i18n.locale === 'zh-CN' ? '90px' : '160px'}">
                {{ $t('configuration.function') }}
              </span>
                <el-input class="input_mo_ren" v-model="form.functionName"/>
              </div>
              <div class="bottom_input_span">
                <span class="span_title" :style="{width: $i18n.locale === 'zh-CN' ? '90px' : '160px'}">
                {{ $t('description') }}
              </span>
                <el-input class="input_mo_ren" v-model="form.functionDescribe"/>
              </div>
              <div class="bottom_input_span">
                <span class="span_title" :style="{width: $i18n.locale === 'zh-CN' ? '90px' : '160px'}">
                {{ $t('configuration.chartType') }}
              </span>
                <el-select v-model="form.chartType"
                           value-key="id"
                           clearable
                           class="input_mo_ren"
                           @close="handleChangeChartType"
                           @change="handleChangeChartType">
                  <el-option
                    v-for="(item, index) in chartTypeList"
                    :key="index"
                    :label="item.dictLabel"
                    :value="item.dictValue"/>
                </el-select>
              </div>
            </div>
            <div class="bottom_bottom_div">
              <el-card class="plain">
                <el-tabs v-model="windowState" class="is-center">
                  <el-tab-pane :label="$t('configuration.functionCodeDisplay')" name="code">
                    <el-input class="input_text_textarea"
                              v-model="form.functionCode"
                              type="textarea" :autosize="{ minRows: 9, maxRows: 9}"/>
                  </el-tab-pane>
                  <el-tab-pane :label="$t('configuration.schemaDisplay')" name="schema">
                    <el-input class="input_text_textarea"
                              v-model="form.functionSchema"
                              type="textarea" :autosize="{ minRows: 9, maxRows: 9}"/>
                  </el-tab-pane>
                </el-tabs>
              </el-card>
              <el-button class="blue" style="margin-top: 12px" @click="functionAddTest">{{ $t('test') }}</el-button>
            </div>
          </div>
        </div>
        <!--        <div class="content_right_div">
                  <div class="right_content_div">
                    <span style="width: 100%; padding: 12px 12px">
                      可见角色
                    </span>
                    <div class="select_div">
                      <span>
                        部门
                      </span>
                      <el-select>

                      </el-select>
                      <span>
                        角色名称
                      </span>
                      <el-select>

                      </el-select>
                    </div>
                    <el-table>
                      <template slot="empty">
                        <Empty/>
                      </template>
                      <el-table-column
                        type="selection"
                        width="50"
                        align="center"/>
                      <el-table-column
                        type="index"
                        label="序号"
                        width="50"
                        align="center"/>
                      <el-table-column
                        label="角色名称"
                        align="center"
                        prop="tableName"
                        show-overflow-tooltip/>
                      <el-table-column
                        label="归属部门"
                        align="center"
                        prop="tableDescription"
                        show-overflow-tooltip/>
                      <el-table-column
                        label="创建时间"
                        align="center"
                        prop="tableName"
                        show-overflow-tooltip/>
                      <el-table-column
                        label="操作"
                        align="center"
                        width="160"
                        class-name="small-padding fixed-width"
                      >
                        <template slot-scope="scope">
                          <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)"
                          >删除
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>-->
      </div>
      <div slot="footer">
        <el-button @click="dialogVisible = false">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="showRole" width="50%" append-to-body>
      <div style="width: 100%;" align="center">
        <span class="dialog-title">{{ $t('configuration.roleAuthorization') }}</span>
      </div>
      <k-b-select :kbList="kbList" :kbAllList="kbAllList" v-on:update:kbList="updateKbList"
                  style="height: 50vh;margin-top: 20px;"/>

      <div slot="footer" class="dialog-footer" align="center">
        <el-button class="secondary" @click="updateShowRole">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitAuthKB">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  pageTable,
  add,
  update,
  deleteds,
  authorize,
  noAuthorize,
  addAuthorize,
  findDataSource,
  functionGenerate,
  functionType,
  functionTest,
  findItem
} from '@/api/bi/configuration/api';
import {Message} from 'element-ui';
import KBSelect from "@/components/RoleSelect/index.vue";

export default {
  name: "configurationManager",
  components: {
    pageTable,
    add,
    Message,
    KBSelect
  },
  data() {
    return {
      loading: false,
      showRole: false,
      showFunctionId: '',
      fullscreenLoading: false,
      dataList: [],
      kbList: [],
      kbAllList: [],
      submitRoleIds: [],
      total: 0,
      pageParams: {
        pageNum: 1,
        pageSize: 10
      },
      selectTableData: [],
      multiple: true,
      searchValue: '',

      dataSource: '',
      dataSources: [],

      dialogVisible: false,
      dialogTitle: '',
      form: {
        question: '',
        sqlText: '',
        functionName: '',
        functionDescribe: '',
        functionCode: '',
        functionSchema: '',
        chartType: '',
        relatedTable: ''
      },
      windowState: 'code',
      chartTypeList: [],
      messageState: false
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      pageTable(this.pageParams, {}).then(res => {
        if (res.code === 200) {
          this.dataList = res.rows;
          this.total = res.total;
          this.loading = false;
        }
      }).catch(err => {
        this.loading = false;
        Message.error(this.$t('configuration.queryTableDataError') + err);
      });
      findDataSource({}).then(res => {
        if (res.code === 200) {
          this.dataSources = res.data;
        }
      }).catch(err => {
        Message.error(this.$t('configuration.queryDataSourceError') + err);
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
    handleDeletedTable() {
      deleteds({ids: this.selectTableData}).then(res => {
        if (res.code === 200) {
          this.getList();
          Message.success(res.msg);
        }
      }).catch(err => {
        Message.error(this.$t('dataTable.deletedError') + err);
      })
    },
    handleAdd() {
      functionType().then(res => {
        if (res.code === 200) {
          this.chartTypeList = res.data;
          this.dialogTitle = this.$t('configuration.addFunction');
          this.dialogVisible = true;
        }
      }).catch(err => {
        Message.error(this.$t('configuration.queryChartTypeError') + err);
      });

    },
    handleEdit(row) {
      findItem(row.id).then(resItem => {
        if (resItem.code === 200) {
          functionType().then(res => {
            if (res.code === 200) {
              this.chartTypeList = res.data;
              this.dialogTitle = this.$t('configuration.modifyFunction');
              this.dialogVisible = true;
              this.form = resItem.data;
            }
          }).catch(err => {
            Message.error(this.$t('configuration.queryChartTypeError') + err);
          });
        }
      }).catch(err => {
        Message.error(this.$t('configuration.queryFunctionItemError') + err);
      });
    },
    resetForm() {
      this.form = {
        question: '',
        sqlText: '',
        functionName: '',
        functionDescribe: '',
        functionCode: '',
        functionSchema: '',
        dataSourceId: '',
        chartType: ''
      };
    },
    functionCall() {
      let message = this.$message({
        message: this.$t('configuration.generating'),
        iconClass: 'el-icon-loading',
        type: 'success',
        duration: 0
      });
      functionGenerate({
        question: this.form.question,
        sqlText: this.form.sqlText
      }).then(res => {
        if (res.code === 200) {
          this.form.functionName = res.data.functionName;
          this.form.functionDescribe = res.data.functionDescribe;
          this.form.functionCode = res.data.functionCode;
          this.form.functionSchema = res.data.functionSchema;
          this.form.dataSourceId = res.data.dataSourceId;
          this.form.chartType = res.data.chartType;
          this.form.relatedTable = res.data.relatedTable;
          message.close();
        }
      }).catch(err => {
        Message.error(this.$t('configuration.intelligentGenerationError') + err);
        message.close();
      });
    },
    submitForm() {
      if (this.dialogTitle === this.$t('configuration.addFunction')) {
        add(this.form).then(res => {
          if (res.code === 200) {
            this.dialogVisible = false;
            Message.success(this.$t('configuration.operationSuccess'));
            this.getList();
          }
        }).catch(err => {
          Message.error(this.$t('configuration.addDataError') + err);
        });
      } else {
        update(this.form).then(res => {
          if (res.code === 200) {
            this.dialogVisible = false;
            Message.success(this.$t('configuration.operationSuccess'));
            this.getList();
          }
        }).catch(err => {
          Message.error(this.$t('dataTable.deletedError') + err);
        });
      }
    },
    handleWindowState(state) {
      this.windowState = state;
    },
    handleConfiguration(row) {
      this.showFunctionId = row.id;
      authorize({functionId: row.id, roleName: ''}).then(res => {
        if (res.code === 200) {
          this.kbList = res.data;
          noAuthorize({functionId: row.id, roleName: ''}).then(Nres => {
            if (Nres.code === 200) {
              let kbAllList = [];
              if (Nres.data.length === 0) {
                kbAllList = res.data;
              } else {
                kbAllList = Nres.data;
                if (res.data.length !== 0) {
                  res.data.forEach(item2 => {
                    const index = Nres.data.some(item1 => item1.roleId === item2.roleId);
                    if (!index) {
                      kbAllList.push(item2);
                    }
                  });
                }
              }
              this.kbAllList = kbAllList;
              this.showRole = true;
            }
          }).catch(err => {
            Message.error(this.$t('configuration.queryUnauthorizedError') + err);
          });
        }
      }).catch(err => {
        Message.error(this.$t('configuration.queryUnauthorizedError') + err);
      });

    },
    submitAuthKB() {
      addAuthorize({
        chatBiRoleIds: this.submitRoleIds,
        functionId: this.showFunctionId
      }).then(res => {
        if (res.code === 200) {
          Message.success(this.$t('configuration.authorizationSuccess'));
          this.getList();
          this.showRole = false;
        }
      }).catch(err => {
        Message.error(this.$t('configuration.roleAuthorizationError') + err);
      });
    },
    handleChangeRole() {

    },
    updateKbList(list) {
      this.kbList = list;
      this.submitRoleIds = list.map((item) => item.roleId);
    },
    updateShowRole() {
      this.showRole = false;
      this.kbList = [];
      this.kbAllList = [];
    },
    handleChangeChartType(item) {
      this.form.chartType = item;
    },
    functionAddTest() {
      let message = this.$message({
        message: this.$t('configuration.testing'),
        iconClass: 'el-icon-loading',
        type: 'success',
        duration: 0
      });
      const subData = {
        chartType: this.form.chartType,
        dataSourceId: this.form.dataSourceId,
        functionCode: this.form.functionCode,
        functionName: this.form.functionName,
        functionSchema: this.form.functionSchema,
        question: this.form.question,
        relatedTable: this.form.relatedTable
      }
      functionTest(subData).then(res => {
        if (res.code === 200) {
          Message.success(res.msg);
          message.close();
        }
      }).catch(err => {
        message.close();
        Message.error(this.$t('configuration.testRequestError') + err);
      });
    }
  }
}
</script>

<style scoped lang="scss">
.configuration_manager_div {
  width: 100%;
  height: 100%;
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

.dialog_content_div {
  width: 100%;
  display: flex;
}

.content_left_div {
  flex: 1;
}

.left_top_div {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  border-bottom: 1px dashed black;
  padding: 12px 0;
}

.span_title {
  line-height: 30px;
  font-size: 15px;
  display: flex;
  flex-direction: row-reverse;
}

.wen_ti_input_div {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
}

.left_bottom_div {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.bottom_top_div {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  gap: 12px;
  padding: 12px 0;
}

.bottom_input_span {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  padding-right: 40px;
}

.bottom_bottom_div {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.bottom_bottom_button_div {
  padding: 6px 0;
  width: 60%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.content_right_div {
  flex: 1;
  padding: 24px 12px;
}

.right_content_div {
  width: 100%;
  height: 100%;
  border: 1px solid #bababa;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.select_div {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 12px 0;
}

.input_text {
  width: 340px;
  border: 1px solid #bababa;
  border-radius: 6px
}

.input_text_textarea{
  width: 100%;
  border: 1px solid #bababa;
  border-radius: 6px
}

.input_mo_ren {
  width: 340px;
  border-radius: 6px;
  border: 1px solid #bababa
}
</style>
