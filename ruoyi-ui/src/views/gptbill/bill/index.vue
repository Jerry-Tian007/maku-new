<template>
  <div class="back">
    <div
      class="color-background"
      style="position: absolute;width:100%;height:100%;"
    ></div>

    <div class="backgrond">
      <div class="chat-background">
        <div class="content-card">
          <div style="padding-bottom:20px;">
            <el-form
              :model="queryParams"
              ref="queryForm"
              size="small"
              :inline="true"
              v-show="showSearch"
              label-width="68px"
            >
              <el-form-item
                label="用户名称"
                prop="userName"
                v-if="getIsAdmin()"
              >
                <el-input
                  v-model="queryParams.userName"
                  placeholder="请输入用户名称"
                  clearable
                  @keyup.enter.native="handleQuery"
                />
              </el-form-item>
              <el-form-item
                label="部门名称"
                prop="deptName"
                v-if="getIsAdmin()"
              >
                <el-input
                  v-model="queryParams.deptName"
                  placeholder="请输入部门名称"
                  clearable
                  @keyup.enter.native="handleQuery"
                />
              </el-form-item>
              <el-form-item
                label="创建时间"
                prop="createTime"
              >
                <el-date-picker
                  v-model="selectTime"
                  type="datetimerange"
                  @change="handleSelectTime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  size="mini"
                  @click="handleQuery"
                >搜索</el-button>
                <el-button
                  icon="el-icon-refresh"
                  size="mini"
                  @click="resetQuery"
                >重置</el-button>
              </el-form-item>
            </el-form>

            <el-row
              :gutter="10"
              class="mb8"
            >
              <el-col :span="1.5">
                <el-button
                  type="warning"
                  plain
                  icon="el-icon-download"
                  size="mini"
                  @click="handleExport"
                  v-hasPermi="['bill:bill:export']"
                >导出
                </el-button>
              </el-col>
              <right-toolbar
                :showSearch.sync="showSearch"
                @queryTable="getList"
              ></right-toolbar>
            </el-row>
          </div>
          <el-table
            v-loading="loading"
            :data="billList"
            @selection-change="handleSelectionChange"
            height="100%"
            :row-style="{height:'60px',backgroundColor:'#ffffff80'}"
          >
            <el-table-column
              type="selection"
              width="55"
              align="center"
            />
            <el-table-column
              label="序号"
              align="center"
              type="index"
              width="55"
            />
            <el-table-column
              label="用户名称"
              align="center"
              prop="userName"
            />
            <el-table-column
              label="部门名称"
              align="center"
              prop="deptName"
            />
            <el-table-column
              label="账单金额"
              align="center"
              prop="amountOfBill"
            />
            <el-table-column
              label="支付状态"
              align="center"
              prop="billingStatus"
            >
              <template slot-scope="scope">
                <el-tag
                  :type="scope.row.billingStatus == 'paid'? 'success' : 'danger'"
                >{{ billingStatusFilter(scope.row) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column
              label="创建时间"
              align="center"
              prop="createTime"
            >
            </el-table-column>
            <el-table-column
              label="备注"
              align="center"
              prop="remark"
            />
          </el-table>

          <pagination
            v-show="total>0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getList"
          />
        </div>
      </div>
    </div>

    <!-- 添加或修改GPT账单对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item
          label="用户ID"
          prop="userId"
        >
          <el-input
            v-model="form.userName"
            placeholder="请输入用户名称"
          />
        </el-form-item>
        <el-form-item
          label="部门ID"
          prop="deptId"
        >
          <el-input
            v-model="form.deptName"
            placeholder="请输入部门名称"
          />
        </el-form-item>
        <el-form-item
          label="账单金额"
          prop="amountOfBill"
        >
          <el-input
            v-model="form.amountOfBill"
            placeholder="请输入账单金额"
          />
        </el-form-item>
        <el-form-item
          label="备注"
          prop="remark"
        >
          <el-input
            v-model="form.remark"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="submitForm"
        >确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBill, getBill, delBill, addBill, updateBill } from "@/api/gptbill/bill";

export default {
  name: "Bill",
  data () {
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
      // GPT账单表格数据
      billList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        deptName: null,
        billingStatus: null,
        startTime: null,
        endTime: null
      },
      selectTime:[],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        deptId: [
          { required: true, message: "部门ID不能为空", trigger: "blur" }
        ],
        amountOfBill: [
          { required: true, message: "账单金额不能为空", trigger: "blur" }
        ],
        billingStatus: [
          { required: true, message: "unpaid:不能为空", trigger: "change" }
        ],
      }
    };
  },
  created () {
    this.getList();
  },
  methods: {
    getIsAdmin(){
      return this.$store.state.user.admin;
    },
    /** 查询GPT账单列表 */
    getList () {
      this.loading = true;
      listBill(this.queryParams).then(response => {
        this.billList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel () {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        id: null,
        userId: null,
        deptId: null,
        amountOfBill: null,
        billingStatus: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange (selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd () {
      this.reset();
      this.open = true;
      this.title = "添加GPT账单";
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset();
      const id = row.id || this.ids
      getBill(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改GPT账单";
      });
    },
    /** 提交按钮 */
    submitForm () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBill(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBill(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除GPT账单编号为"' + ids + '"的数据项？').then(function () {
        return delBill(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport () {
      this.download('bill/export', {
        ...this.queryParams
      }, `bill_${new Date().getTime()}.xlsx`)
    },
    billingStatusFilter (row, column) {
      const status = row.billingStatus;
      switch (status) {
        case 'unpaid':
          return '未支付';
        case 'paid':
          return '已支付';
        default:
          return '未知';
      }
    },
    handleSelectTime(val){
      console.log(val)
      this.queryParams.startTime = val[0];
      this.queryParams.endTime = val[1];
    }
  }
};
</script>
<style lang="scss" scoped>
.back {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.chat-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #ffffff40;
}

.content-card {
  width: 90%;
  height: 90%;
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: auto;
  padding: 30px;
  background-color: #ffffff40;
  border: 1px solid #ffffffc0;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
}

.card {
  width: max-content;
  max-width: 70%;
  min-width: 60px;
  min-height: 40px;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
  background-color: #ffffffc0;
  border: 1px solid #202020;
}

.card-text {
  width: 100%;
  font-size: 16px;
  padding-top: 6px;
}

.box-card {
  overflow: auto; //10.20日添加，但是无效
}

#el-container {
  margin-top: 10px;

  ::v-deep .el-tabs__item.is-disabled {
    color: #000 !important;
  }
}

/* 定义滚动条高宽及背景
 高宽分别对应横竖滚动条的尺寸 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

/* 定义滚动条轨道
内阴影+圆角 */
::-webkit-scrollbar-track {
  background-color: transparent;
}

/* 定义滑块
内阴影+圆角 */
::-webkit-scrollbar-thumb {
  border-radius: 8px;
  background-color: rgba(221, 221, 221, 0.5);
}

.content-card ::v-deep .el-table,
.content-card ::v-deep .el-table__expanded-cell {
  background-color: #ffffff20;
}

/* 表格内背景颜色 */
.content-card ::v-deep .el-table th,
.content-card ::v-deep .el-table tr,
.content-card ::v-deep .el-table td {
  background-color: #ffffff20 !important;
}

::v-deep .el-table__body-wrapper::-webkit-scrollbar {
}

// 滚动条的滑块
::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
  background-color: #d0d0d040;
  border-radius: 3px;
}

::v-deep .detail .el-dialog {
  background-color: #ffffffc0;
  border-radius: 20px;
}
</style>
<style scoped>


:root {
  font-size: 15px;
}

.color-background {
  font-family: "Quicksand", sans-serif;
  margin: 0;
  min-height: 100vh;
  background-color: #ffffff;
  background-image: radial-gradient(
      closest-side,
      rgb(58, 174, 211),
      rgba(243, 11, 164, 0)
    ),
    radial-gradient(closest-side, #b0e0edff, #b0e0ed00),
    radial-gradient(closest-side, #ccdcad, #ccdcad00);
  background-size: 80vmax 80vmax, 70vmax 70vmax, 80vmax 80vmax;
  background-position: -20vmax -20vmax, 40vmax -10vmax, 0vmax 0vmax;
  background-repeat: no-repeat;
  animation: 10s movement linear infinite;
}

@keyframes movement {
  0%,
  100% {
    background-position: -50vmax -50vmax, 60vmax -30vmax, 10vmax 10vmax;
  }
  25% {
    background-position: -40vmax -40vmax, 50vmax -40vmax, 0vmax -20vmax;
  }
  50% {
    background-position: -40vmax -60vmax, 40vmax -30vmax, 10vmax 0vmax;
  }
  75% {
    background-position: -40vmax -30vmax, 50vmax -30vmax, 20vmax 0vmax;
  }
}
</style>
