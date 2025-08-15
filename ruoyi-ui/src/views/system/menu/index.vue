<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('systemManager.menuName')" prop="menuName">
        <el-input
          v-model="queryParams.menuName"
          :placeholder="$t('systemManager.menuNamePlaceholder')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('systemManager.state')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('systemManager.menuState')" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('search') }}</el-button>
        <el-button class="secondary" icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('rest') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          class="blue"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:menu:add']"
        >{{ $t('add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >{{ $t('systemManager.expandOrCollapse') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="menuList"
      row-key="menuId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="menuName" :label="$t('systemManager.menuName')" :show-overflow-tooltip="true" width="160"></el-table-column>
      <el-table-column prop="icon" :label="$t('systemManager.icon')" align="center" width="100">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" :label="$t('systemManager.showOrder')" width="60"></el-table-column>
      <el-table-column prop="perms" :label="$t('systemManager.authIdentification')" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="component" :label="$t('systemManager.componentUrl')" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="status" :label="$t('systemManager.state')" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('systemManager.createTime')" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:menu:edit']"
          >{{ $t('update') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:menu:add']"
          >{{ $t('add') }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:menu:remove']"
          >{{ $t('deleted') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" :label-width="$i18n.locale === 'zh-CN' ? '100px' : '130px'">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('systemManager.previousMenu')" prop="parentId">
              <treeselect
                v-model="form.parentId"
                :options="menuOptions"
                :normalizer="normalizer"
                :show-count="true"
                :placeholder="$t('systemManager.selectPreviousMenu')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('systemManager.menuType')" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">{{ $t('systemManager.catalogue') }}</el-radio>
                <el-radio label="C">{{ $t('systemManager.menu') }}</el-radio>
                <el-radio label="F">{{ $t('systemManager.button') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.menuType != 'F'">
            <el-form-item :label="$t('systemManager.menuIcon')" prop="icon">
              <el-popover
                placement="bottom-start"
                width="460"
                trigger="click"
                @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="selected" :active-icon="form.icon"/>
                <el-input slot="reference" v-model="form.icon" :placeholder="$t('systemManager.clickSelectIcon')" readonly>
                  <svg-icon
                    v-if="form.icon"
                    slot="prefix"
                    :icon-class="form.icon"
                    style="width: 25px;"
                  />
                  <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="form.menuType != 'F'">
            <el-form-item :label="$t('systemManager.iconColor')" prop="iconColor">
              <div style="width: 100%;display: flex;gap:12px;">
                <el-color-picker v-model="form.iconColor"></el-color-picker>
                <el-input v-model="form.iconColor" style="flex:1;" :placeholder="$t('systemManager.pleaseIconColor')"/>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.menuName')" prop="menuName">
              <el-input v-model="form.menuName" :placeholder="$t('systemManager.menuNamePlaceholder')"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item :label="$t('systemManager.menuNameEng')" :label-width="$i18n.locale === 'zh-CN' ? '100px' : '170px'" prop="menuNameEn">
              <el-input v-model="form.menuNameEn" :placeholder="$t('systemManager.menuNameEngPlaceholder')"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('systemManager.order')" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item prop="isFrame" >
              <span slot="label">
                <el-tooltip :content="$t('systemManager.routingAddress')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('systemManager.externalLink')}}
              </span>
              <el-radio-group v-model="form.isFrame">
                <el-radio label="0">{{ $t('correct') }}</el-radio>
                <el-radio label="1">{{ $t('deny') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item prop="path" :label-width="$i18n.locale === 'zh-CN' ? '100px' : '160px'">
              <span slot="label">
                <el-tooltip :content="$t('systemManager.routingAddressText')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('systemManager.routerUrl')}}
              </span>
              <el-input v-model="form.path" :placeholder="$t('systemManager.pleaseRouterUrl')"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item prop="component" :label-width="$i18n.locale === 'zh-CN' ? '100px' : '160px'">
              <span slot="label">
                <el-tooltip :content="$t('systemManager.componentContent')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('systemManager.componentUrl')}}
              </span>
              <el-input v-model="form.component" :placeholder="$t('systemManager.pleaseComponentUrl')"/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'M'">
            <el-form-item prop="perms" :label-width="$i18n.locale === 'zh-CN' ? '100px' : '180px'">
              <el-input v-model="form.perms" :placeholder="$t('systemManager.pleaseAuthIdentification')" maxlength="100" />
              <span slot="label">
                <el-tooltip :content="$t('systemManager.controllerPermission')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('systemManager.roleKey')}}
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item prop="query" :label-width="$i18n.locale === 'zh-CN' ? '100px' : '170px'">
              <el-input v-model="form.query" :placeholder="$t('systemManager.pleaseRouterParam')" maxlength="255"/>
              <span slot="label">
                <el-tooltip :content="$t('systemManager.routerParamText')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('systemManager.routerParam')}}
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item prop="isCache">
              <span slot="label">
                <el-tooltip :content="$t('systemManager.cachedContent')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('systemManager.cached')}}
              </span>
              <el-radio-group v-model="form.isCache">
                <el-radio label="0">{{$t('systemManager.cache')}}</el-radio>
                <el-radio label="1">{{$t('systemManager.unCache')}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item prop="visible">
              <span slot="label">
                <el-tooltip :content="$t('systemManager.showStateText')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('systemManager.showState')}}
              </span>
              <el-radio-group v-model="form.visible">
                <el-radio
                  v-for="dict in dict.type.sys_show_hide"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType != 'F'">
            <el-form-item prop="status">
              <span slot="label">
                <el-tooltip :content="$t('systemManager.menuStateText')" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                {{$t('systemManager.menuState')}}
              </span>
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('ok') }}</el-button>
        <el-button @click="cancel">{{ $t('cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listMenu, getMenu, delMenu, addMenu, updateMenu} from "@/api/system/menu";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import IconSelect from "@/components/IconSelect";

export default {
  name: "Menu",
  dicts: ['sys_show_hide', 'sys_normal_disable'],
  components: {Treeselect, IconSelect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 菜单表格树数据
      menuList: [],
      // 菜单树选项
      menuOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部折叠
      isExpandAll: false,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        menuName: undefined,
        visible: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        menuName: [
          {required: true, message: this.$t('systemManager.menuNameNoNull'), trigger: "blur"}
        ],
        orderNum: [
          {required: true, message: this.$t('systemManager.menuOrderNoNull'), trigger: "blur"}
        ],
        path: [
          {required: true, message: this.$t('systemManager.routerUrlNoNull'), trigger: "blur"}
        ],
        iconColor: [
          // { required: true, message: "图标颜色不能为空", trigger: "blur" },
          {pattern: /^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$/, message: this.$t('systemManager.pleaseCorrectColor'), trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 选择图标
    selected(name) {
      this.form.icon = name;
    },
    /** 查询菜单列表 */
    getList() {
      this.loading = true;
      listMenu(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data, "menuId");
        this.loading = false;
      });
    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children
      };
    },
    /** 查询菜单下拉树结构 */
    getTreeselect() {
      listMenu().then(response => {
        this.menuOptions = [];
        const menu = {menuId: 0, menuName: this.$t('systemManager.mainCategory'), children: []};
        menu.children = this.handleTree(response.data, "menuId");
        this.menuOptions.push(menu);
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
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: "M",
        orderNum: undefined,
        isFrame: "1",
        isCache: "0",
        visible: "0",
        status: "0"
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      if (row != null && row.menuId) {
        this.form.parentId = row.menuId;
      } else {
        this.form.parentId = 0;
      }
      this.open = true;
      this.title = this.$t('systemManager.pushMenu');
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      getMenu(row.menuId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t('systemManager.updateMenu');
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.iconColor = this.form.iconColor || null;

          if (this.form.menuId != undefined) {
            updateMenu(this.form).then(response => {
              this.$modal.msgSuccess(this.$t('updateSuccess'));
              this.open = false;
              this.getList();
            });
          } else {
            addMenu(this.form).then(response => {
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
      this.$modal.confirm(this.$t('systemManager.confirmDeleted') + row.menuName + this.$t('systemManager.deletedData')).then(function () {
        return delMenu(row.menuId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('deletedSuccess'));
      }).catch(() => {
      });
    }
  }
};
</script>
