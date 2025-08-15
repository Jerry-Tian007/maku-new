<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('authManager.roleName')" prop="roleName">
        <el-input v-model="queryParams.roleName" :placeholder="$t('authManager.roleNamePlaceholder')" clearable
          style="width: 240px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('systemManager.roleKey')" prop="roleKey">
        <el-input v-model="queryParams.roleKey" :placeholder="$t('systemManager.inputRoleKey')" clearable
          style="width: 240px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('systemManager.state')" prop="status">
        <el-select popper-class="color-select" v-model="queryParams.status"
          :placeholder="$t('systemManager.roleStatus')" clearable style="width: 240px">
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('systemManager.createTime')">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
          range-separator="-" :start-placeholder="$t('systemManager.startPlaceholder')"
          :end-placeholder="$t('systemManager.endPlaceholder')"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('search') }}</el-button>
        <el-button class="secondary" icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('rest') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button class="blue" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:role:add']">{{ $t('add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button class="green" disabled type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="handleUpdate" v-hasPermi="['system:role:edit']">{{ $t('update') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button class="red" disabled type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete" v-hasPermi="['system:role:remove']">{{ $t('deleted') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button class="blue-light" type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:role:export']">{{ $t('export') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('systemManager.roleNo')" prop="roleId" width="120" />
      <el-table-column :label="$t('authManager.roleName')" prop="roleName" :show-overflow-tooltip="true" width="150" />
      <el-table-column :label="$t('systemManager.roleKey')" prop="roleKey" :show-overflow-tooltip="true" width="160" />
      <el-table-column :label="$t('systemManager.order')" prop="roleSort" width="130" />
      <el-table-column :label="$t('systemManager.state')" align="center" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
            @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column :label="$t('systemManager.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('systemManager.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <template v-if="scope.row.roleId !== 1">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
              v-hasPermi="['system:role:edit']">{{ $t('update') }}
            </el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
              v-hasPermi="['system:role:remove']">{{ $t('deleted') }}
            </el-button>
            <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
              v-hasPermi="['system:role:edit']">
              <el-button size="mini" type="text" icon="el-icon-d-arrow-right">{{ $t('systemManager.more') }}</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="handleDataScope" icon="el-icon-circle-check"
                  v-hasPermi="['system:role:edit']">{{ $t('systemManager.dataScope') }}
                </el-dropdown-item>
                <el-dropdown-item command="handleAuthUser" icon="el-icon-user" v-hasPermi="['system:role:edit']">{{
                  $t('systemManager.assignUser') }}
                </el-dropdown-item>

                <el-dropdown-item command="handleAuthKB" icon="el-icon-collection" v-hasPermi="['system:role:edit']">{{
                  $t('systemManager.knowledgeBaseAuth') }}
                </el-dropdown-item>
                <el-dropdown-item command="handleAuthModel" icon="el-icon-takeaway-box"
                  v-hasPermi="['system:role:edit']">{{ $t('systemManager.modelAuthorization') }}
                </el-dropdown-item>
                <el-dropdown-item command="handleGraphAuthModel" icon="el-icon-takeaway-box"
                  v-hasPermi="['system:role:edit']">{{ $t('knowledgeGraph.knowledgeAuth') }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button size="mini" type="text" icon="el-icon-collection" @click="handleAuthKB(scope.row)">{{
              $t('systemManager.knowledgeBaseAuth') }}
            </el-button>
            <el-button size="mini" type="text" icon="el-icon-takeaway-box" @click="handleAuthModel(scope.row)">{{
              $t('systemManager.modelAuthorization') }}
            </el-button>
            <el-button size="mini" type="text" icon="el-icon-takeaway-box" @click="handleGraphAuthModel(scope.row)">{{
              $t('knowledgeGraph.knowledgeAuth') }}
            </el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <pagination popper-class="color-pagination" background layout="total, sizes, prev, pager, next" v-show="total > 0"
      :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" :label-width="$i18n.locale === 'zh-CN' ? '100px' : '160px'">
        <el-form-item :label="$t('authManager.roleName')" prop="roleName">
          <el-input v-model="form.roleName" :placeholder="$t('authManager.roleNamePlaceholder')" />
        </el-form-item>
        <el-form-item prop="roleKey">
          <span slot="label">
            <el-tooltip :content="$t('systemManager.controllerPermission')" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            {{ $t('systemManager.permissionCharacter') }}
          </span>
          <el-input v-model="form.roleKey" :placeholder="$t('systemManager.inputRoleKey')" />
        </el-form-item>
        <el-form-item :label="$t('systemManager.roleOrder')" prop="roleSort">
          <el-input-number v-model="form.roleSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item :label="$t('systemManager.state')">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label
            }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('systemManager.menuPermissions')">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">
            {{ $t('systemManager.expandOrCollapse') }}
          </el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">
            {{ $t('systemManager.selectAllOrNot') }}
          </el-checkbox>
          <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">
            {{ $t('systemManager.parentChildLinkage') }}
          </el-checkbox>
          <el-tree class="tree-border" :data="menuOptions" show-checkbox ref="menu" node-key="id"
            :check-strictly="!form.menuCheckStrictly" :empty-text="$t('systemManager.loadingPleaseWait')"
            :props="defaultProps"></el-tree>
        </el-form-item>
        <el-form-item :label="$t('remark')">
          <el-input v-model="form.remark" type="textarea" :placeholder="$t('dataTable.remarkPlaceholder')"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
        <el-button @click="cancel">{{ $t('cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 分配角色数据权限对话框 -->
    <el-dialog :title="title" :visible.sync="openDataScope" width="500px" append-to-body>
      <el-form :model="form" :label-width="$i18n.locale === 'zh-CN' ? '80px' : '165px'">
        <el-form-item :label="$t('systemManager.roleName')">
          <el-input v-model="form.roleName" :disabled="true" />
        </el-form-item>
        <el-form-item :label="$t('systemManager.roleKey')">
          <el-input v-model="form.roleKey" :disabled="true" />
        </el-form-item>
        <el-form-item :label="$t('systemManager.purview')">
          <el-select v-model="form.dataScope" @change="dataScopeSelectChange">
            <el-option v-for="item in dataScopeOptions" :key="item.value" :label="item.label"
              :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('systemManager.dataScope')" v-show="form.dataScope == 2">
          <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event, 'dept')">
            {{ $t('systemManager.expandOrCollapse') }}
          </el-checkbox>
          <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event, 'dept')">
            {{ $t('systemManager.selectAllOrNot') }}
          </el-checkbox>
          <el-checkbox v-model="form.deptCheckStrictly" @change="handleCheckedTreeConnect($event, 'dept')">
            {{ $t('systemManager.parentChildLinkage') }}
          </el-checkbox>
          <el-tree class="tree-border" :data="deptOptions" show-checkbox default-expand-all ref="dept" node-key="id"
            :check-strictly="!form.deptCheckStrictly" :empty-text="$t('systemManager.loadingPleaseWait')"
            :props="defaultProps"></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDataScope">{{ $t('ok') }}</el-button>
        <el-button @click="cancelDataScope">{{ $t('cancel') }}</el-button>
      </div>
    </el-dialog>


    <el-dialog :visible.sync="showKB" width="50%" append-to-body>
      <div style="width: 100%;" align="center">
        <span class="dialog-title">{{ $t('systemManager.knowledgeBaseAuthorization') }}</span>
      </div>
      <k-b-select v-model="kbList" style="height: 50vh;margin-top: 20px;" />

      <div slot="footer" class="dialog-footer" align="center">
        <el-button class="secondary" @click="showKB = false;">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitAuthKB">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="showModel" width="50%" append-to-body>
      <div style="width: 100%;" align="center">
        <span class="dialog-title">{{ $t('systemManager.modelAuthorizationDialogTitle') }}</span>
      </div>
      <model-select v-model="modelList" style="height: 50vh;margin-top: 20px;" />

      <div slot="footer" class="dialog-footer" align="center">
        <el-button class="secondary" @click="showModel = false;">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitAuthModel">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="showGraphModel" width="50%" append-to-body>
      <div style="width: 100%;" align="center">
        <span class="dialog-title">{{ $t('knowledgeGraph.knowledgeAuth') }}</span>
      </div>
      <GraphModel v-model="graphList" style="height: 50vh;margin-top: 20px;" />

      <div slot="footer" class="dialog-footer" align="center">
        <el-button class="secondary" @click="showGraphModel = false;">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitGraphAuth">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listRole,
  getRole,
  delRole,
  addRole,
  updateRole,
  dataScope,
  changeRoleStatus,
  deptTreeSelect
} from "@/api/system/role";
import { treeselect as menuTreeselect, roleMenuTreeselect } from "@/api/system/menu";
import KBSelect from "@/components/KBSelect/index.vue";
import { addKBAuth, getKbListByRoleId } from "@/api/knowledgeBase/kbAuthManager";
import { getModelListByRoleId, updateModelResourceRole } from "@/api/modelManager/modelRoleManager";
import ModelSelect from "@/components/ModelSelect/index.vue";
import GraphModel from "@/components/GraphModel/index.vue";
import { authGraph, findAuthGraphByRoleId } from '@/api/knowledgeBase/knowledgeGraph';

export default {
  name: "Role",
  components: { ModelSelect, KBSelect, GraphModel },
  dicts: ['sys_normal_disable'],
  data() {
    return {
      showKB: false,
      showModel: false,
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
      // 角色表格数据
      roleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层（数据权限）
      openDataScope: false,
      menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,
      // 日期范围
      dateRange: [],
      // 数据范围选项
      dataScopeOptions: [
        {
          value: "1",
          label: this.$t('systemManager.allDataPermission')
        },
        {
          value: "2",
          label: this.$t('systemManager.customDataPermission')
        },
        {
          value: "3",
          label: this.$t('systemManager.deptDataPermission')
        },
        {
          value: "4",
          label: this.$t('systemManager.deptAndBelowDataPermission')
        },
        {
          value: "5",
          label: this.$t('systemManager.ownDataPermission')
        }
      ],
      // 菜单列表
      menuOptions: [],
      // 部门列表
      deptOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roleName: undefined,
        roleKey: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        roleName: [
          { required: true, message: this.$t('permissionConfig.roleDataNoNull'), trigger: "blur" }
        ],
        roleKey: [
          { required: true, message: this.$t('systemManager.authNoNull'), trigger: "blur" }
        ],
        roleSort: [
          { required: true, message: this.$t('systemManager.roleOrderNoNull'), trigger: "blur" }
        ]
      },

      kbList: [],
      modelList: [],
      roleId: null,
      showGraphModel: false,
      graphList: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询角色列表 */
    getList() {
      this.loading = true;
      listRole(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.roleList = response.rows;
        this.total = response.total;
        this.loading = false;
      }
      );
    },
    /** 查询菜单树结构 */
    getMenuTreeselect() {
      menuTreeselect().then(response => {
        this.menuOptions = response.data;
      });
    },
    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.menu.getCheckedKeys();
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    // 所有部门节点数据
    getDeptAllCheckedKeys() {
      // 目前被选中的部门节点
      let checkedKeys = this.$refs.dept.getCheckedKeys();
      // 半选中的部门节点
      let halfCheckedKeys = this.$refs.dept.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    /** 根据角色ID查询菜单树结构 */
    getRoleMenuTreeselect(roleId) {
      return roleMenuTreeselect(roleId).then(response => {
        this.menuOptions = response.menus;
        return response;
      });
    },
    /** 根据角色ID查询部门树结构 */
    getDeptTree(roleId) {
      return deptTreeSelect(roleId).then(response => {
        this.deptOptions = response.depts;
        return response;
      });
    },
    // 角色状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? this.$t('enable') : this.$t('deactivate');
      this.$modal.confirm(this.$t('systemManager.confirmTo') + text + '""' + row.roleName + this.$t('systemManager.theRole')).then(function () {
        return changeRoleStatus(row.roleId, row.status);
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
    // 取消按钮（数据权限）
    cancelDataScope() {
      this.openDataScope = false;
      this.reset();
    },
    // 表单重置
    reset() {
      if (this.$refs.menu != undefined) {
        this.$refs.menu.setCheckedKeys([]);
      }
      this.menuExpand = false,
        this.menuNodeAll = false,
        this.deptExpand = true,
        this.deptNodeAll = false,
        this.form = {
          roleId: undefined,
          roleName: undefined,
          roleKey: undefined,
          roleSort: 0,
          status: "0",
          menuIds: [],
          deptIds: [],
          menuCheckStrictly: true,
          deptCheckStrictly: true,
          remark: undefined
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
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roleId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleDataScope":
          this.handleDataScope(row);
          break;
        case "handleAuthUser":
          this.handleAuthUser(row);
          break;
        case "handleAuthKB":
          this.handleAuthKB(row);
          break;
        case "handleAuthModel":
          this.handleAuthModel(row);
          break;
        case "handleGraphAuthModel":
          this.handleGraphAuthModel(row);
          break;
        default:
          break;
      }
    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value, type) {
      if (type == 'menu') {
        let treeList = this.menuOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
        }
      } else if (type == 'dept') {
        let treeList = this.deptOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
        }
      }
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
      if (type == 'menu') {
        this.$refs.menu.setCheckedNodes(value ? this.menuOptions : []);
      } else if (type == 'dept') {
        this.$refs.dept.setCheckedNodes(value ? this.deptOptions : []);
      }
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value, type) {
      if (type == 'menu') {
        this.form.menuCheckStrictly = value ? true : false;
      } else if (type == 'dept') {
        this.form.deptCheckStrictly = value ? true : false;
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getMenuTreeselect();
      this.open = true;
      this.title = this.$t('systemManager.pushRole');
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const roleId = row.roleId || this.ids
      const roleMenu = this.getRoleMenuTreeselect(roleId);
      getRole(roleId).then(response => {
        this.form = response.data;
        this.open = true;
        this.$nextTick(() => {
          roleMenu.then(res => {
            let checkedKeys = res.checkedKeys
            checkedKeys.forEach((v) => {
              this.$nextTick(() => {
                this.$refs.menu.setChecked(v, true, false);
              })
            })
          });
        });
        this.title = this.$t('roleManager.updateRole');
      });
    },
    /** 选择角色权限范围触发 */
    dataScopeSelectChange(value) {
      if (value !== '2') {
        this.$refs.dept.setCheckedKeys([]);
      }
    },
    /** 分配数据权限操作 */
    handleDataScope(row) {
      this.reset();
      const deptTreeSelect = this.getDeptTree(row.roleId);
      getRole(row.roleId).then(response => {
        this.form = response.data;
        this.openDataScope = true;
        this.$nextTick(() => {
          deptTreeSelect.then(res => {
            this.$refs.dept.setCheckedKeys(res.checkedKeys);
          });
        });
        this.title = this.$t('systemManager.assignDataPermissions');
      });
    },
    /** 分配用户操作 */
    handleAuthUser: function (row) {
      const roleId = row.roleId;
      this.$router.push("/system/role-auth/user/" + roleId);
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.roleId != undefined) {
            this.form.menuIds = this.getMenuAllCheckedKeys();
            updateRole(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('updateSuccess'));
              this.open = false;
              this.getList();
            });
          } else {
            this.form.menuIds = this.getMenuAllCheckedKeys();
            addRole(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('addSuccess'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 提交按钮（数据权限） */
    submitDataScope: function () {
      if (this.form.roleId != undefined) {
        this.form.deptIds = this.getDeptAllCheckedKeys();
        dataScope(this.form).then(response => {
          this.$modal.msgSuccess(this.$t('updateSuccess'));
          this.openDataScope = false;
          this.getList();
        });
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const roleIds = row.roleId || this.ids;
      this.$modal.confirm(this.$t('systemManager.deleteConfirm') + roleIds + this.$t('systemManager.deleteConfirms')).then(function () {
        return delRole(roleIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('deletedSuccess'));
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/role/export', {
        ...this.queryParams
      }, `role_${new Date().getTime()}.xlsx`)
    },
    handleAuthKB(row) {
      this.roleId = row.roleId;
      getKbListByRoleId(row.roleId).then(response => {
        this.kbList = response.data.filter(id => id !== '');
        this.showKB = true;
      });
    },
    submitAuthKB() {
      let loading = this.$loading();
      addKBAuth({ roleId: this.roleId, kbIds: this.kbList }).then(res => {
        if (res.code === 200) {
          this.showKB = false;
          this.getList();
          this.$modal.msgSuccess(this.$t('configuration.authorizationSuccess'));
        } else {
          this.$modal.msgError(res.msg);
          console.log(res.msg)
        }
        loading.close();
      }).catch(err => {
        this.$modal.msgError(err);
        console.log(err);
        loading.close();
      });
    },
    handleAuthModel(row) {
      this.roleId = row.roleId;
      getModelListByRoleId(row.roleId).then(response => {
        this.modelList = response.data;
        this.showModel = true;
      });
    },
    handleGraphAuthModel(row) {
      this.roleId = row.roleId;
      findAuthGraphByRoleId({ roleId: row.roleId }).then(res => {
        if (res.code === 200) {
          this.graphList = res.data.map(item => item.spaceId + '');
          this.showGraphModel = true;
        }
      }).catch(err => {
        console.log(err);
      });
    },
    submitAuthModel() {
      let loading = this.$loading();
      updateModelResourceRole({ roleId: this.roleId, modelResourceTemplateIds: this.modelList }).then(res => {
        if (res.code === 200) {
          this.showModel = false;
          this.getList();
          this.$modal.msgSuccess(this.$t('configuration.authorizationSuccess'));
        } else {
          this.$modal.msgError(res.msg);
          console.log(res.msg)
        }
        loading.close();
      }).catch(err => {
        this.$modal.msgError(err);
        console.log(err);
        loading.close();
      });
    },
    submitGraphAuth() {
      let loading = this.$loading();
      authGraph({roleId: this.roleId, graphIdList: this.graphList}).then(res => {
        if(res.code === 200){
          this.showGraphModel = false;
          this.getList();
          this.$modal.msgSuccess(this.$t('configuration.authorizationSuccess'));
        } else {
          this.$modal.msgError(res.msg);
          console.log(res.msg)
        }
        loading.close();
      }).catch(err => {
        this.$modal.msgError(err);
        console.log(err);
        loading.close();
      })
    }
  }
};
</script>
