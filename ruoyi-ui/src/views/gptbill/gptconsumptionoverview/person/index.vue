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
                prop="userId"
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
                prop="deptId"
              >
                <el-input
                  v-model="queryParams.deptName"
                  placeholder="请输入部门名称"
                  clearable
                  @keyup.enter.native="handleQuery"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  size="mini"
                  @click="handleQuery"
                >搜索
                </el-button>
                <el-button
                  icon="el-icon-refresh"
                  size="mini"
                  @click="resetQuery"
                >重置
                </el-button>
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
                  v-if="false"
                  v-hasPermi="['gpt:gptconsumptionoverview:export']"
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
            :data="gptconsumptionoverviewList"
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
              label="用户名称"
              align="center"
              min-width="120"
              prop="userName"
            />
            <el-table-column
              label="部门名称"
              align="center"
              min-width="120"
              prop="deptName"
            />
            <el-table-column
              label="最大额度（美元）"
              align="center"
              min-width="140"
              prop="maxConsumptionAmount"
            />
            <el-table-column
              label="总消耗金额"
              align="center"
              min-width="160"
              prop="totalConsumptionAmount"
            />
            <el-table-column
              label="当前账单消耗token量"
              align="center"
              min-width="160"
              prop="currentBillTokenConsumption"
            />
            <el-table-column
              label="当前账单消耗金额"
              align="center"
              min-width="160"
              prop="currentBillConsumptionAmount"
            />
            <el-table-column
              label="上次刷新时间"
              align="center"
              min-width="160"
              prop="lastGenerateBillTime"
            />
            <el-table-column
              label="备注"
              align="center"
              prop="remark"
              min-width="120"
              show-overflow-tooltip
            />
            <el-table-column
              label="操作"
              align="center"
              min-width="160"
              fixed="right"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-refresh"
                  @click="refreshBill(scope.row)"
                >刷新
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-money"
                  @click="handleUpdate(scope.row)"
                >额度调整
                </el-button>
                <!--<el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['gpt:gptconsumptionoverview:edit']"
                >修改
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['gpt:gptconsumptionoverview:remove']"
                >删除
                </el-button>-->
              </template>
            </el-table-column>
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

    <!-- 添加或修改GPT消费概览对话框 -->
    <el-dialog
      :title="'额度调整'"
      :visible.sync="updateVal"
      width="500px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="updateForm"
        label-width="80px"
        label-position="top"
      >
        <el-form-item
          label="调整类型"
          prop="userId"
        >
          <el-radio-group v-model="operationType">
            <el-radio-button label="increase">提高</el-radio-button>
            <el-radio-button label="decrease">降低</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="调整金额（单位：美元）"
          prop="userId"
        >
          <el-input-number
            v-model="numberValue"
            :step="10"
            :min="0"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button
          type="primary"
          @click="submitForm"
        >确 定
        </el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  overviewPersonalBill,
  listGptconsumptionoverview,
  updatePersonalMax
} from "@/api/gptbill/gptconsumptionoverview";

export default {
  name: "GptconsumptionoverviewAdmin",
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
      // GPT消费概览表格数据
      gptconsumptionoverviewList: [],
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
        type: 'user'
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          {required: true, message: "用户ID不能为空", trigger: "blur"}
        ],
        deptId: [
          {required: true, message: "部门ID不能为空", trigger: "blur"}
        ],
      },
      updateVal:false,
      updateForm: {
        maxConsumptionAmount: 0,
        totalConsumptionAmount:0,
        newValue: 0,
        id: 0,
        userId: 0,
      },
      numberValue: 10,
      operationType: 'increase'
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询GPT消费概览列表 */
    getList() {
      this.loading = true;
      listGptconsumptionoverview(this.queryParams).then(response => {
        this.gptconsumptionoverviewList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.updateVal = false;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams ={
        pageNum: 1,
        pageSize: 10,
        userId: null,
        deptId: null,
        type: 'user'
      };
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加GPT消费概览";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.updateForm = JSON.parse(JSON.stringify(row));
      this.numberValue = 10;
      this.operationType = 'increase';
      this.updateVal = true;
    },
    /** 提交按钮 */
    submitForm() {
      let data = {
        overviewId: this.updateForm.id,
        userId: this.updateForm.userId,
        consumptionAmount: this.numberValue+'',
        operationType: this.operationType,
      }
      updatePersonalMax(data).then(res => {
        if (res.code === 200){
          this.$message.success('修改成功');
          this.updateVal = false;
          this.getList();
        } else {
          this.$message.error(res.msg);
        }
      }).catch(err => {
        this.$message.error(err);
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除GPT消费概览编号为"' + ids + '"的数据项？').then(function () {
        return delGptconsumptionoverview(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('gpt/gptconsumptionoverview/export', {
        ...this.queryParams
      }, `gptconsumptionoverview_${new Date().getTime()}.xlsx`)
    },
    refreshBill(item) {
      overviewPersonalBill({
        userId: item.userId,
        deptId: item.deptId,
        id: item.id,
        lastGenerateBillTime: item.lastGenerateBillTime,
      }).then(response => {
        if (response.code == 200) {
          this.$message({
            message: '刷新成功',
            type: 'success'
          });
          this.getList();
        } else {
          this.$message({
            message: response.msg,
            type: 'error'
          });
        }
      }).catch(error => {
        this.$message({
          message: error,
          type: 'error'
        });
      });
    },
    /*addOrder(item) {
      console.log(item)
      if (item.currentBillTokenConsumption <= 0) {
        this.$message.warning("当前账单消耗量为0，无法生成账单");
        return;
      }
      this.$confirm('是否确认生成账单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        generateBill({
          lastGenerateBillTime: item.lastGenerateBillTime,
          deptId: item.deptId,
          userId: item.userId,
        }).then(response => {
          if (response.code === 200) {
            this.$modal.msgSuccess("生成账单成功");
            this.getList();
          } else {
            this.$modal.msgError(response.msg);
          }
        }).catch(error => {
          this.$modal.msgError(error);
        });
      }).catch(() => {
      });
    },*/
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
.content-card ::v-deep .el-table__fixed-right{
  background-color: #ffffffd8;
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
