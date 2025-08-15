<template>
  <el-card class="mt20" v-loading.fullscreen.lock="fullscreenLoading">
    <div class="flex-layout align-center start-end" style="margin-bottom: 10px;">
      <el-button class="blue" icon="el-icon-plus"
                 @click="handleAdd"
      >{{ $t('dataBase.addDataBaseConnection') }}
      </el-button>

      <div class="flex-layout align-center gap8">
        <el-input
          v-model="searchValue"
          :placeholder="$t('dataBase.searchValuePlaceholder')"
          style="width: 200px;"
          size="small"
          clearable
          @clear="handleInputSearch"
          @input="handleInputSearch"
          prefix-icon="el-icon-search"
        />
        <el-popconfirm
          :title="$t('dataBase.deleteSelectedContent')"
          @confirm="deletedTableIds"
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
        :label="$t('dataBase.connectionName')"
        align="center"
        prop="name"
        show-overflow-tooltip/>
      <el-table-column
        :label="$t('dataBase.connectionType')"
        align="center"
        prop="type"
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
        :label="$t('dataBase.lastModified')"
        align="center"
        prop="updateTime"
        show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('dataBase.updateUser')"
        align="center"
        prop="updateBy"
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
          >{{$t('edit')}}
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-refresh"
            @click="handleFetch(scope.row)"
          >{{ $t('synchronous') }}
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
      @close="closeDialog"
      :close-on-click-modal="false"
    >
      <!--      <div class="main-color fs-16 pd-v6 pd-h8" style="border-bottom: 1px solid #E4E7ED;">
              基础信息
            </div>-->
      <el-tabs v-model="activeName" class="pd-h20">
        <el-tab-pane :label="$t('dataBase.BasicInformation')" name="basic">
        </el-tab-pane>
        <el-tab-pane :label="$t('dataBase.SSHConfiguration')" name="ssh">
        </el-tab-pane>
        <el-tab-pane :label="$t('dataBase.SSLConfiguration')" name="ssl">
        </el-tab-pane>
        <el-tab-pane :label="$t('description')" name="remark">
        </el-tab-pane>
      </el-tabs>

      <div style="margin-top: 10px;min-height: 360px;" class="pd-h20">
        <el-form
          v-show="activeName === 'basic'"
          :model="form"
          :label-width="$i18n.locale === 'zh-CN' ? '100px' : '150px'"
          :rules="rules"
          ref="form"
        >
          <el-form-item :label="$t('dataBase.connectionName')" prop="name">
            <el-input
              v-model="form.name"
              :placeholder="$t('dataBase.connectionNamePlaceholder')"
            />
          </el-form-item>
          <el-form-item :label="$t('dataBase.username')" prop="username">
            <el-input
              v-model="form.username"
              :placeholder="$t('dataBase.usernamePlaceholder')"
            />
          </el-form-item>
          <el-form-item :label="$t('dataBase.password')" prop="password">
            <el-input
              v-model="form.password"
              :placeholder="$t('dataBase.usernamePlaceholder')"
            />
          </el-form-item>
          <!--根据form继续渲染-->
          <el-form-item :label="$t('dataBase.dataBaseType')" prop="type">
            <el-select
              v-model="form.type"
              :placeholder="$t('dataBase.usernamePlaceholder')"
            >
              <el-option
                v-for="item in databaseTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('dataBase.host')" prop="host">
            <el-input
              v-model="form.host"
              :placeholder="$t('dataBase.usernamePlaceholder')"
            />
          </el-form-item>
          <el-form-item :label="$t('dataBase.port')" prop="port">
            <el-input
              v-model="form.port"
              :placeholder="$t('dataBase.usernamePlaceholder')"
            />
          </el-form-item>
          <el-form-item :label="$t('dataBase.username')" prop="databaseName">
            <el-input
              v-model="form.databaseName"
              :placeholder="$t('dataBase.usernamePlaceholder')"
            />
          </el-form-item>
          <el-form-item :label="$t('dataBase.securitySetting')">
            <el-checkbox v-model="form.enableSsh">{{ $t('dataBase.sshTunnel') }}</el-checkbox>
            <el-checkbox v-model="form.enableSsl">{{ $t('dataBase.sslEncryption') }}</el-checkbox>
          </el-form-item>
        </el-form>

        <div
          v-show="activeName==='ssh'">
          <div style="margin-left: 104px;margin-bottom: 20px;">
            <el-checkbox v-model="form.enableSsh">{{ $t('dataBase.enableSshTunnel') }}</el-checkbox>
          </div>
          <el-form
            :disabled="!form.enableSsh"
            :title="form.enableSsh?'':$t('dataBase.disEnable')"
            :model="form"
            :label-width="$i18n.locale === 'zh-CN' ? '100px' : '120px'"
            :rules="rules"
            ref="sshForm">
            <el-form-item :label="$t('dataBase.sshHost')" prop="sshHost">
              <el-input
                v-model="form.sshHost"
                :placeholder="$t('dataBase.sshHostPlaceholder')"
              />
            </el-form-item>
            <el-form-item :label="$t('dataBase.sshPort')" prop="sshPort">
              <el-input
                v-model="form.sshPort"
                :placeholder="$t('dataBase.sshPortPlaceholder')"
              />
            </el-form-item>
            <el-form-item :label="$t('dataBase.sshSecretKey')" prop="sshSecretKey">
              <el-input
                v-model="form.sshSecretKey"
                :placeholder="$t('dataBase.sshSecretKeyPlaceholder')"
              />
            </el-form-item>
          </el-form>
        </div>
        <div
          v-show="activeName==='ssl'">
          <div style="margin-left: 104px;margin-bottom: 20px;">
            <el-checkbox v-model="form.enableSsl">{{ $t('dataBase.enableSslEncryption') }}</el-checkbox>
          </div>
          <el-form
            :disabled="!form.enableSsl"
            :title="form.enableSsl?'':$t('dataBase.sslDisEnable')"
            :model="form"
            :label-width="$i18n.locale === 'zh-CN' ? '100px' : '120px'"
            :rules="rules"
            ref="sslForm">
            <el-form-item :label="$t('dataBase.sslCertificate')" prop="sslCertificate">
              <el-input
                v-model="form.sslCertificate"
                :placeholder="$t('dataBase.sslCertificatePlaceholder')"
              />
            </el-form-item>
            <el-form-item :label="$t('dataBase.sslSecretKey')" prop="sslSecretKey">
              <el-input
                v-model="form.sslSecretKey"
                :placeholder="$t('dataBase.sslSecretKeyPlaceholder')"
              />
            </el-form-item>
            <!--CA证书-->
            <el-form-item :label="$t('dataBase.caCertificate')" prop="sslCaCertificate">
              <el-input
                v-model="form.sslCaCertificate"
                :placeholder="$t('dataBase.caCertificatePlaceholder')"
              />
            </el-form-item>
          </el-form>
        </div>

        <el-form
          :model="form"
          v-show="activeName==='remark'"
          label-width="100px"
          label-position="top"
          ref="remarkForm">

          <el-form-item :label="$t('dataBase.describe')" prop="remark">
            <el-input
              type="textarea"
              v-model="form.remark"
              :rows="8"
              resize="none"
              :placeholder="$t('dataBase.describePlaceholder')"
            />
          </el-form-item>
        </el-form>

      </div>

      <div slot="footer" class="flex-layout align-center start-end">
        <el-button class="purple" @click="dataBaseTest">{{ $t('dataBase.testConnection') }}</el-button>
        <div>
          <el-button @click="dialogVisible = false">{{ $t('cancel') }}</el-button>
          <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
        </div>
      </div>
    </el-dialog>
  </el-card>
</template>
<script>
import {
  pageListChatBiDataSource,
  addChatBiDataSource,
  databaseTest,
  updateChatBiDataSource,
  deleteChatBiDataSource,
  syncDatabase,
  deletedTableIds
} from "@/api/bi/databaseManager/table";
import {Message} from 'element-ui';

export default {
  name: "DataBasePage",
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
      tableDeleteIds: [],
      // 不为多选
      multiple: true,
      openDialogType: '',
      searchValue: '',
      dialogVisible: false,
      dialogTitle: '',

      form: { // 包括：数据库类型， host， Port ，数据库名称，用户名，密码
        username: '',
        password: '',
        type: '',
        host: '',
        port: '',
        databaseName: '',
        enableSsh: false,
        enableSsl: false,
        sshHost: '',
        sshPort: '',
        sshSecretKey: '',
        sslCertificate: '',
        sslSecretKey: '',
        sslCaCertificate: ''
      },
      rules: {
        name: [
          {required: true, message: this.$t('dataBase.connectionNamePlaceholder'), trigger: 'blur'}
        ],
        username: [
          {required: true, message: this.$t('dataBase.usernamePlaceholder'), trigger: 'blur'}
        ], password: [
          {required: true, message: this.$t('dataBase.passwordPlaceholder'), trigger: 'blur'}
        ],
        type: [
          {required: true, message: this.$t('dataBase.selectDataBaseType'), trigger: 'blur'}
        ], host: [
          {required: true, message: this.$t('dataBase.hostPlaceholder'), trigger: 'blur'}
        ],
        databaseName: [
          {required: true, message: this.$t('dataBase.dataBaseNamePlaceholder'), trigger: 'blur'}
        ],
      },
      databaseTypeOptions: [
        {
          label: 'MySQL',
          value: 'mysql'
        },
        {
          label: 'Oracle',
          value: 'oracle'
        },
        {
          label: 'SQL Server',
          value: 'sqlserver'
        }
      ],
      ifList: [
        'name', 'username', 'password', 'type', 'host', 'databaseName'
      ],
      activeName: 'basic'
    };
  },
  mounted() {
    this.getList();
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
      pageListChatBiDataSource(pageParams, {name: this.searchValue}).then(res => {
        for (const r of res.rows) {
          this.updateReverseSshSslState(r);
        }
        this.dataList = res.rows;
        this.total = res.total;
      }).finally(() => {
        this.loading = false;
      });
    },
    handleSelectionChange(e) {
      if (e.length === 0) {
        this.tableDeleteIds = [];
        this.multiple = true;
      } else {
        this.tableDeleteIds = [];
        for (const t of e) {
          this.tableDeleteIds.push(t.id);
        }
        this.multiple = false;
      }
    },
    resetForm() {
      this.form = {
        username: '',
        password: '',
        type: '',
        host: '',
        port: '',
        databaseName: '',
        enableSsh: false,
        enableSsl: false,
        sshHost: '',
        sshPort: '',
        sshSecretKey: '',
        sslCertificate: '',
        sslSecretKey: '',
        sslCaCertificate: ''
      };
    },
    handleAdd() {
      this.resetForm();
      this.dialogTitle = this.$t('dataBase.addDataBaseConnection');
      this.dialogVisible = true;
      this.openDialogType = 'add';
    },
    handleUpdate(row) {
      this.form = row;
      this.dialogTitle = this.$t('dataBase.updateDataBaseConnection');
      this.dialogVisible = true;
      this.openDialogType = 'update';
    },
    handleDelete(row) {
      deleteChatBiDataSource(row.id).then(res => {
        if (res.code === 200) {
          Message.success(res.msg);
          this.getList();
        }
      }).catch(err => {
        Message.error(this.$t('dataBase.deletedDataBaseError') + err);
      });
    },
    handleFetch(row) {
      syncDatabase(row.id).then(res => {
        if (res.code === 200) {
          Message.success(this.$t('dataBase.syncSuccess'));
        }
      }).catch(err => {
        Message.error(this.$t('dataBase.syncDataSourceError') + err);
      });
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          const submitData = JSON.parse(JSON.stringify(this.form));
          this.updateSshSslState(submitData);
          if (this.openDialogType === 'add') {
            addChatBiDataSource(submitData).then(res => {
              if (res.code === 200) {
                Message.success(res.msg);
                this.closeDialog();
                this.getList();
              }
            }).catch(err => {
              Message.error(this.$t('dataBase.addDataBaseError') + err);
            });
          } else if (this.openDialogType === 'update') {
            updateChatBiDataSource(submitData).then(res => {
              if (res.code === 200) {
                Message.success(res.msg);
                this.closeDialog();
                this.getList();
              }
            }).catch(err => {
              Message.error(this.$t('dataBase.updateDataBaseError') + err);
            });
          }
        }
      });
    },
    closeDialog() {
      this.dialogVisible = false;
      this.resetForm();
      this.openDialogType = '';
    },
    updateSshSslState(submitData) {
      if (submitData.hasOwnProperty('enableSsh')) {
        submitData.enableSsh = submitData.enableSsh ? 'yes' : 'no';
      }
      if (submitData.hasOwnProperty('enableSsl')) {
        submitData.enableSsl = submitData.enableSsl ? 'yes' : 'no';
      }
    },
    updateReverseSshSslState(submitData) {
      if (submitData.hasOwnProperty('enableSsh')) {
        submitData.enableSsh = submitData.enableSsh === 'yes' ? true : false;
      }
      if (submitData.hasOwnProperty('enableSsl')) {
        submitData.enableSsl = submitData.enableSsl === 'yes' ? true : false;
      }
    },
    dataBaseTest() {
      const submitData = JSON.parse(JSON.stringify(this.form));
      this.updateSshSslState(submitData);
      databaseTest(submitData).then(res => {
        if (res.code === 200) {
          Message.success(this.$t('dataBase.testConnectionSuccess'));
        }
      }).catch(err => {
        Message.error(this.$t('dataBase.testConnectionError') + err);
      });
    },
    handleInputSearch(val) {
      pageListChatBiDataSource(this.pageParams, {name: val}).then(res => {
        this.dataList = res.rows;
        this.total = res.total;
      }).finally(() => {
        this.loading = false;
      });
    },
    deletedTableIds() {
      if (this.tableDeleteIds.length !== 0) {
        deletedTableIds({ids: this.tableDeleteIds}).then(res => {
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
};
</script>
