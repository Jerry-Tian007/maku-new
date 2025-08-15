<template>
  <div class="app-container"
       style="background: rgba(255,255,255,0.72);border-radius: 4px;box-shadow: 0px 0px 7px 0px rgba(0,0,0,0.11);margin:30px 20px;">
    <el-row :gutter="20" align="stretch" style="display:flex;align-items:stretch;">
      <!--部门数据-->
      <el-col :span="4" :xs="24" style="background: #FFFFFF;
                box-shadow: 2px 0px 4px 0px rgba(0,0,0,0.05);
                border-radius: 4px;">
        <div class="head-container">
          <el-input class="dark"
                    v-model="deptName"
                    :placeholder="$t('systemManager.pleaseDeptName')"
                    clearable
                    size="small"
                    prefix-icon="el-icon-search"
                    style="margin:16px 0;"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            node-key="id"
            default-expand-all
            highlight-current
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">


        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="78px">
          <el-row :gutter="10">
            <el-col :span="9" style="display: flex; flex-wrap: wrap;">
              <el-form-item :label="$t('systemManager.userName')" prop="userName">
                <el-input
                  v-model="queryParams.userName"
                  :placeholder="$t('systemManager.pleaseUserName')"
                  clearable
                  @keyup.enter.native="handleQuery"
                />
              </el-form-item>
            </el-col>
            <el-col :span="9" style="display: flex; flex-wrap: wrap;">
              <el-form-item :label="$t('systemManager.phoneNumber')" prop="phonenumber"
                            :label-width="$i18n.locale === 'zh-CN' ? '100px' : '120px'">
                <el-input
                  v-model="queryParams.phonenumber"
                  :placeholder="$t('systemManager.pleasePhoneNumber')"
                  clearable
                  @keyup.enter.native="handleQuery"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="9" offset="" style="display: flex; flex-wrap: wrap;">
              <el-form-item :label="$t('systemManager.status')" prop="status">
                <el-select
                  v-model="queryParams.status"
                  :placeholder="$t('systemManager.userState')"
                  clearable
                  popper-class="optionsContent"
                >
                  <el-option
                    v-for="dict in dict.type.sys_normal_disable"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="9" style="display: flex; flex-wrap: wrap;">
              <el-form-item :label="$t('kbManager.createTime')"
                            :label-width="$i18n.locale === 'zh-CN' ? '100px' : '120px'">
                <el-date-picker
                  v-model="dateRange"
                  value-format="yyyy-MM-dd"
                  type="daterange"
                  range-separator="-"
                  :start-placeholder="$t('systemManager.startPlaceholder')"
                  :end-placeholder="$t('systemManager.endPlaceholder')"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6" style="">
              <el-form-item style="display: flex; justify-content: flex-end;">
                <el-button class="secondary" icon="el-icon-refresh" size="medium" @click="resetQuery"
                           style="color:white;">
                  {{ $t('systemManager.reset') }}
                </el-button>
                <el-button type="primary" icon="el-icon-search" size="medium" @click="handleQuery">
                  {{ $t('search') }}
                </el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>


        <el-row :gutter="10" class="mb8" style="margin-bottom: 20px;">
          <el-col :span="1.5">
            <el-button class="blue"
                       type="primary"
                       plain
                       icon="el-icon-plus"
                       size="mini"
                       @click="handleAdd"
                       v-hasPermi="['system:user:add']"
                       style="height:30px;"
            >{{ $t('add') }}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button class="green" disabled
                       type="success"
                       plain
                       icon="el-icon-edit"
                       size="mini"
                       :disabled="single"
                       @click="handleUpdate"
                       v-hasPermi="['system:user:edit']"
                       style="height:30px;"
            >{{ $t('update') }}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button class="red" disabled
                       type="danger"
                       plain
                       icon="el-icon-delete"
                       size="mini"
                       :disabled="multiple"
                       @click="handleDelete"
                       v-hasPermi="['system:user:remove']"
                       style="height:30px;"
            >{{ $t('deleted') }}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button class="purple"
                       type="info"
                       plain
                       icon="el-icon-upload2"
                       size="mini"
                       @click="handleImport"
                       v-hasPermi="['system:user:import']"
                       style="height:30px;"
            >{{ $t('import') }}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button class="blue-light"
                       type="warning"
                       plain
                       icon="el-icon-download"
                       size="mini"
                       @click="handleExport"
                       v-hasPermi="['system:user:export']"
                       style="height:30px;"
            >{{ $t('export') }}
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"
                         class="right-toolbar-container"></right-toolbar>
        </el-row>
        <div class="table-container"
             style="background: #FFFFFF;box-shadow: 0px 0px 4px 0px rgba(0,0,0,0.18);border-radius: 4px;padding-bottom: 10px;">
          <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center"/>
            <el-table-column :label="$t('systemManager.userNo')" width="120" align="center" key="userId" prop="userId" v-if="columns[0].visible"/>
            <el-table-column :label="$t('systemManager.userName')" width="120" align="center" key="userName" prop="userName" v-if="columns[1].visible"
                             :show-overflow-tooltip="true"/>
            <el-table-column :label="$t('systemManager.userNick')" width="120" align="center" key="nickName" prop="nickName" v-if="columns[2].visible"
                             :show-overflow-tooltip="true"/>
            <el-table-column :label="$t('systemManager.dept')" width="120" align="center" key="deptName" prop="dept.deptName" v-if="columns[3].visible"
                             :show-overflow-tooltip="true"/>
            <el-table-column :label="$t('systemManager.phoneNumber')" align="center" key="phonenumber" prop="phonenumber"
                             v-if="columns[4].visible" width="120"/>
            <el-table-column :label="$t('systemManager.state')" align="center" key="status" v-if="columns[5].visible">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.status"
                  active-value="0"
                  inactive-value="1"
                  @change="handleStatusChange(scope.row)"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column :label="$t('systemManager.createTime')" align="center" prop="createTime" v-if="columns[6].visible" width="160">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('systemManager.validTo')" align="center" prop="expireTime" v-if="columns[7].visible" width="160">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.expireTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :label="$t('operation')"
              align="center"
              width="160"
              class-name="small-padding fixed-width"
              style=""
            >
              <template slot-scope="scope" v-if="scope.row.userId !== 1">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['system:user:edit']"
                >{{ $t('update') }}
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['system:user:remove']"
                >{{ $t('deleted') }}
                </el-button>
                <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
                             v-hasPermi="['system:user:resetPwd', 'system:user:edit']">
                  <el-button size="mini" type="text" icon="el-icon-d-arrow-right">{{ $t('systemManager.more') }}</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <!--                    <el-dropdown-item command="handleResetPwd" icon="el-icon-key"-->
                    <!--                                      v-hasPermi="['system:user:resetPwd']">重置密码-->
                    <!--                    </el-dropdown-item>-->
                    <el-dropdown-item command="handleResetPwd"
                                      v-hasPermi="['system:user:resetPwd']">{{ $t('systemManager.resetPassword') }}
                    </el-dropdown-item>
                    <!--                    <el-dropdown-item command="handleAuthRole" icon="el-icon-circle-check"-->
                    <!--                                      v-hasPermi="['system:user:edit']">分配角色-->
                    <!--                    </el-dropdown-item>-->
                    <el-dropdown-item command="handleAuthRole"
                                      v-hasPermi="['system:user:edit']">{{ $t('update') }}
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
<!--          <div class="pagination">-->
            <pagination
              v-show="total>0"
              :total="total"
              :page.sync="queryParams.pageNum"
              :limit.sync="queryParams.pageSize"
              @pagination="getList"
              popper-class="color-pagination"
              background
              layout="total, sizes, prev, pager, next"
            />
<!--          </div>-->
        </div>
      </el-col>

    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" :label-width="$i18n.locale === 'zh-CN' ? '80px' : '120px'">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.userNick')" prop="nickName">
              <el-input v-model="form.nickName" :placeholder="$t('systemManager.pleaseUserNick')" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('roleManager.belongDept')" prop="deptId">
              <treeselect v-model="form.deptId" :options="deptOptions" :show-count="true" :placeholder="$t('roleManager.belongDeptPlaceholder')"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.phoneNumber')" prop="phonenumber">
              <el-input v-model="form.phonenumber" :placeholder="$t('systemManager.pleasePhoneNumber')" maxlength="11"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.email')" prop="email">
              <el-input v-model="form.email" :placeholder="$t('systemManager.pleaseEmail')" maxlength="50"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" :label="$t('systemManager.userName')" prop="userName">
              <el-input v-model="form.userName" :placeholder="$t('systemManager.pleaseUserName')" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" :label="$t('systemManager.userPassword')" prop="password">
              <el-input v-model="form.password" :placeholder="$t('systemManager.pleaseUserPassword')" type="password" maxlength="20"
                        show-password/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.sex')">
              <el-select v-model="form.sex" :placeholder="$t('systemManager.selectSex')">
                <el-option
                  v-for="dict in dict.type.sys_user_sex"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.state')">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.postId')">
              <el-select v-model="form.postIds" multiple :placeholder="$t('systemManager.pleasePost')">
                <el-option
                  v-for="item in postOptions"
                  :key="item.postId"
                  :label="item.postName"
                  :value="item.postId"
                  :disabled="item.status == 1"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.role')" prop="roleIds">
              <el-select v-model="form.roleIds" multiple :placeholder="$t('permissionConfig.pleaseSelectRole')">
                <el-option
                  v-for="item in roleOptions"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId"
                  :disabled="item.status == 1"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('systemManager.validTo')">
              <el-date-picker
                v-model="form.expireTime"
                type="datetime"
                :placeholder="$t('systemManager.pleaseValidTo')"
                :picker-options="pickerOptions"
                value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('remark')">
              <el-input v-model="form.remark" type="textarea" :placeholder="$t('systemManager.pleaseRemark')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
        <el-button @click="cancel">{{ $t('cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">{{ $t('chatBI.dragTheFile') }}<em>{{ $t('AllInOne.uploading') }}</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport"/>
            {{ $t('systemManager.updateExistingUserData') }}
          </div>
          <span>{{ $t('systemManager.importFileText') }}</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
                   @click="importTemplate">{{ $t('systemManager.downModel') }}
          </el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">{{ $t('ok') }}</el-button>
        <el-button @click="upload.open = false">{{ $t('cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listUser,
  getUser,
  delUser,
  addUser,
  updateUser,
  resetUserPwd,
  changeUserStatus,
  deptTreeSelect
} from "@/api/system/user";
import {getToken} from "@/utils/auth";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "User",
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  components: {Treeselect},
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
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],

      isBackground: true,

      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken(), Language: localStorage.getItem("language")},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 100,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      // 列信息
      columns: [
        {key: 0, label: this.$t('systemManager.userNo'), visible: true},
        {key: 1, label: this.$t('systemManager.userName'), visible: true},
        {key: 2, label: this.$t('systemManager.userNick'), visible: true},
        {key: 3, label: this.$t('navbar.department'), visible: true},
        {key: 4, label: this.$t('systemManager.phoneNumber'), visible: true},
        {key: 5, label: this.$t('systemManager.state'), visible: true},
        {key: 6, label: this.$t('systemManager.createTime'), visible: true},
        {key: 7, label: this.$t('systemManager.validTo'), visible: true}
      ],
      // 表单校验
      rules: {
        roleIds: [
          {required: true, message: this.$t('systemManager.accountRoleNoNull'), trigger: "change"}
        ],
        deptId: [
          {required: true, message: this.$t('systemManager.deptNoNull'), trigger: "change"},
        ],
        userName: [
          {required: true, message: this.$t('systemManager.userNameNoNull'), trigger: "blur"},
          {min: 2, max: 20, message: this.$t('systemManager.userNameLength'), trigger: 'blur'}
        ],
        nickName: [
          {required: true, message: this.$t('systemManager.userNickNoNull'), trigger: "blur"}
        ],
        password: [
          {required: true, message: this.$t('systemManager.userPasswordNoNull'), trigger: "blur"},
          {min: 5, max: 20, message: this.$t('systemManager.userPasswordLength'), trigger: 'blur'}
        ],
        email: [
          {
            type: "email",
            message: this.$t('systemManager.pleaseCorrectEmail'),
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: this.$t('systemManager.pleaseCorrectPhone'),
            trigger: "blur"
          }
        ]
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now() - 24 * 60 * 60 * 1000;
        },
        shortcuts: [{
          text: this.$t('systemManager.anHour'),
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000);
            picker.$emit('pick', date);
          }
        }, {
          text: this.$t('systemManager.twoHour'),
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 2);
            picker.$emit('pick', date);
          }
        }, {
          text: this.$t('systemManager.twelveHour'),
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 12);
            picker.$emit('pick', date);
          }
        }, {
          text: this.$t('systemManager.tomorrow'),
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: this.$t('systemManager.oneWeekLater'),
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }, {
          text: this.$t('systemManager.thirtyLater'),
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 24 * 30);
            picker.$emit('pick', date);
          }
        }]
      },
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getDeptTree();
    this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg;
    });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? this.$t('systemManager.enable') : this.$t('systemManager.disable');
      this.$modal.confirm(this.$t('systemManager.confirmEnableDisable') + text + '""' + row.userName + this.$t('systemManager.confirmEnableDisableLast')).then(function () {
        return changeUserStatus(row.userId, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + this.$t('success'));
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0";
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
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: [],
        expireTime: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.queryParams.deptId = undefined;
      this.$refs.tree.setCurrentKey(null);
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        case "handleAuthRole":
          this.handleAuthRole(row);
          break;
        default:
          break;
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      getUser().then(response => {
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.open = true;
        this.title = this.$t('systemManager.pushUser');
        this.form.password = this.initPassword;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId || this.ids;
      getUser(userId).then(response => {
        this.form = response.data;
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.$set(this.form, "postIds", response.postIds);
        this.$set(this.form, "roleIds", response.roleIds);
        this.open = true;
        this.title = this.$t('systemManager.updateUser');
        this.form.password = "";
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt(this.$t('systemManager.pleaseInput') + row.userName + this.$t('systemManager.newPassword'), this.$t('warding'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel'),
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: this.$t('systemManager.userPasswordLength')
      }).then(({value}) => {
        resetUserPwd(row.userId, value).then(response => {
          this.$modal.msgSuccess(this.$t('systemManager.updatePasswordText') + value);
        });
      }).catch(() => {
      });
    },
    /** 分配角色操作 */
    handleAuthRole: function (row) {
      const userId = row.userId;
      this.$router.push("/system/user-auth/role/" + userId);
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('updateSuccess'));
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('addSuccess'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$modal.confirm(this.$t('systemManager.deletedUserNo') + userIds + this.$t('systemManager.deletedData')).then(function () {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('deletedSuccess'));
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = this.$t('systemManager.userImport');
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/user/importTemplate', {}, `user_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", this.$t('systemManager.importResult'), {dangerouslyUseHTMLString: true});
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>

<style lang="scss" scoped>

.right-toolbar-container {
  margin-right: -205px;
  display: flex;
  justify-content: flex-end;
}

.optionsContent .el-select-dropdown__item:hover {
  background: #DEE7F8;
  border-radius: 4px;
  color: #254F91;
  margin: 0 5px;
}

.optionsContent .el-select-dropdown__item {
  margin: 0 5px;
}


::v-deep .el-dropdown-menu__item:hover {
  background: #DEE7F8;
  border-radius: 4px;
  color: #254F91;
}

/*::v-deep .pagination {
  margin-top: 23px;
  text-align: center;
  padding: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}*/


::v-deep .el-pagination.is-background .el-pager li {
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  background-color: #F3F5FB;
  color: #000000;
  border-radius: 2px;
}

/* 激活后的样式 */
::v-deep .el-pagination.is-background .el-pager li:not(.disabled).active {
  background: linear-gradient(270deg, #6DA7CE 0%, #4E71EB 100%);
  border-radius: 2px;
}

/* 修改左右箭头样式 */
::v-deep .el-pagination .btn-next .el-icon,
::v-deep .el-pagination .btn-prev .el-icon {
  background: #F3F5FB;
  border-radius: 2px;
}

::v-deep .el-pagination .el-select .el-input .el-input__inner {
  padding-right: 25px;
  background: #F3F5FB;
  border-radius: 3px;
}

::v-deep .el-button--text {
  color: #5072CC;
}

::v-deep .el-pagination__editor.el-input .el-input__inner {
  background: #F3F5FB;
  width: 30px;
  border: none;
  border-radius: 4px;
}

.elinput ::v-deep .el-input__prefix .el-input__icon {
  color: #1B3B71;
}

.el-date-editor.el-input, .el-date-editor.el-input__inner {
  width: 220px;
}

</style>
