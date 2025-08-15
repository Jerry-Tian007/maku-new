<template>
  <div class="back">
    <div
      class="color-background"
      style="position: absolute;width:100%;height:100%;"
    ></div>

    <div class="backgrond">
      <div class="chat-background">
        <div class="content-card">
          <div
            class="row"
            style="height:40px;font-size:24px;margin-bottom:40px;justify-content: space-between;"
          >
            <div>账单概览</div>
            <div>
              <el-button
                type="primary"
                @click="checkOverview(true)"
                size="small"
                plain
                icon="el-icon-refresh-right"
              >刷新</el-button>
            </div>
          </div>
          <div
            class="row"
            style="align-items: center;height:60px;margin-bottom:40px;"
          >
            <div style="flex:4;display:flex;flex-direction:column;font-size:20px;color:#507BB6;">
              <div style="">
                <span style="font-size: 18px;color:#87A5CF">账户名称：
                </span>{{ data.userName }}
              </div>
            </div>
            <div
              style="flex:1;display:flex;flex-direction:column;align-items:center;font-size:20px;color:#507BB6;"
            >
              <div style="color:#87A5CF;font-size: 14px;">
                总Token消耗量
              </div>
              <div style="color:#2D9CDB;">
                {{ data.totalTokenConsumption }}
              </div>
            </div>
            <div
              style="flex:1;display:flex;flex-direction:column;align-items:center;font-size:20px;color:#507BB6;"
            >
              <div style="color:#87A5CF;font-size: 14px;">
                总消耗金额
              </div>
              <div style="color:#2D9CDB;">
                ￥{{ data.totalConsumptionAmount }}
              </div>
            </div>
            <div
              style="flex:1;display:flex;flex-direction:column;align-items:center;font-size:20px;color:#507BB6;"
            >
              <div style="color:#FCBC97;font-size: 14px;">
                未结算Token量
              </div>
              <div style="color:#F2994A;">
                {{ data.currentBillTokenConsumption }}
              </div>
            </div>
            <div
              style="flex:1;display:flex;flex-direction:column;align-items:center;font-size:20px;color:#507BB6;"
            >
              <div style="color:#FCBC97;font-size: 14px;">
                未结算金额
              </div>
              <div style="color:#F2994A;">
                ￥{{ data.currentBillConsumptionAmount }}
              </div>
            </div>
          </div>

          <div style="flex:1;display:flex;">
            <div
              style="flex:1;width: 100%;display:flex;flex-direction: column;align-items: center;padding-right: 10px;"
            >
              <div
                style="flex:1;width: 100%;padding-bottom:20px;"
                ref="detail"
              >
                <div style="padding:10px 0;font-size: 18px;">当前账单详情</div>
                <el-table
                  :row-style="{height:'60px',backgroundColor:'#ffffff80'}"
                  :data="detailList"
                  :height="detailTableHeight-48"
                  style="width: 100%;"
                >
                  <el-table-column
                    prop="model"
                    label="模型"
                    align="center"
                    min-width="120"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    prop="tokenConsumption"
                    label="token消耗量"
                    align="center"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    prop="tokenUnitPrice"
                    label="Token单价"
                    align="center"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    prop="amount"
                    label="金额"
                    align="center"
                    show-overflow-tooltip
                  />
                </el-table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 添加或修改GPT消费概览对话框 -->
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
            v-model="form.userId"
            placeholder="请输入用户ID"
          />
        </el-form-item>
        <el-form-item
          label="部门ID"
          prop="deptId"
        >
          <el-input
            v-model="form.deptId"
            placeholder="请输入部门ID"
          />
        </el-form-item>
        <el-form-item
          label="toten总消耗量"
          prop="totalTokenConsumption"
        >
          <el-input
            v-model="form.totalTokenConsumption"
            placeholder="请输入toten总消耗量"
          />
        </el-form-item>
        <el-form-item
          label="总消耗金额"
          prop="totalConsumptionAmount"
        >
          <el-input
            v-model="form.totalConsumptionAmount"
            placeholder="请输入总消耗金额"
          />
        </el-form-item>
        <el-form-item
          label="当前账单消耗token量"
          prop="currentBillTokenConsumption"
        >
          <el-input
            v-model="form.currentBillTokenConsumption"
            placeholder="请输入当前账单消耗token量"
          />
        </el-form-item>
        <el-form-item
          label="当前账单消耗金额"
          prop="currentBillConsumptionAmount"
        >
          <el-input
            v-model="form.currentBillConsumptionAmount"
            placeholder="请输入当前账单消耗金额"
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
import {
  listGptconsumptionoverview,
  getGptconsumptionoverview,
  delGptconsumptionoverview,
  addGptconsumptionoverview,
  updateGptconsumptionoverview,
  generateBill,
  overviewPersonalBill,
} from "@/api/gptbill/gptconsumptionoverview";
import { listBill } from "@/api/gptbill/bill";

export default {
  name: "Gptconsumptionoverview",
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
        userId: null,
        deptId: null,
        type: 'dept'
      },
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
      },

      detailList: [],
      billList: [],

      historyTableHeight: 200,
      detailTableHeight: 200,
      //data: {
      //  userId: null,
      //  deptId: null,
      //  deptName: null,
      //  totalTokenConsumption: null,
      //  totalConsumptionAmount: null,
      //  currentBillTokenConsumption: null,
      //  currentBillConsumptionAmount: null,
      //  createTime: null,
      //  createBy: null,
      //  updateTime: null,
      //  updateBy: null,
      //  remark: null,
      //},
      data: {
        id: null,
        userId: null,
        userName: '-',
        deptId: 1,
        deptName: '-',
        totalTokenConsumption: '-', //总消耗量
        totalConsumptionAmount: '-', //总金额
        currentBillTokenConsumption: '-', //未结算消耗量
        currentBillConsumptionAmount: '-', //未结算金额
        remark: '无',

        lastGenerateBillTime: null,
      },
      userID: null,
    };
  },
  created () {
    this.getList();
    this.userID = this.$store.state.user.userId;
  },
  methods: {
    /** 查询GPT消费概览列表 */
    getList () {
      this.loading = true;
      listGptconsumptionoverview({
        pageNum: 1,
        pageSize: 10,
        deptId: this.$store.state.user.deptId,
        userId: this.$store.state.user.userId,
        lastGenerateBillTime: this.data.lastGenerateBillTime || null,
        type: 'user'
      }).then(response => {
        this.gptconsumptionoverviewList = response.rows;
        this.data = {
          id: this.gptconsumptionoverviewList[0].id,
          deptName: this.gptconsumptionoverviewList[0].deptName,
          deptId: this.gptconsumptionoverviewList[0].deptId,
          userId: this.gptconsumptionoverviewList[0].userId,
          userName: this.gptconsumptionoverviewList[0].userName,
          remark: this.gptconsumptionoverviewList[0].remark,
          lastGenerateBillTime: this.gptconsumptionoverviewList[0].lastGenerateBillTime,
        }
        this.checkOverview(false);
        this.getDetail(response.rows[0] || {});
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel () {
      this.open = false;
      this.reset();
    },
    checkOverview (show) {
      overviewPersonalBill({
        deptId: this.$store.state.user.deptId,
        userId: this.$store.state.user.userId,
        id: this.data.id,
        lastGenerateBillTime: this.data.lastGenerateBillTime || null,
      }).then(response => {
        if (response.code == 200) {
          this.refresh(show);
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
    // 表单重置
    reset () {
      this.form = {
        id: null,
        userId: null,
        deptId: null,
        totalTokenConsumption: null,
        totalConsumptionAmount: null,
        currentBillTokenConsumption: null,
        currentBillConsumptionAmount: null,
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
      this.title = "添加GPT消费概览";
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset();
      const id = row.id || this.ids
      getGptconsumptionoverview(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改GPT消费概览";
      });
    },
    /** 提交按钮 */
    submitForm () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateGptconsumptionoverview(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGptconsumptionoverview(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除GPT消费概览编号为"' + ids + '"的数据项？').then(function () {
        return delGptconsumptionoverview(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport () {
      this.download('gpt/gptconsumptionoverview/export', {
        ...this.queryParams
      }, `gptconsumptionoverview_${new Date().getTime()}.xlsx`)
    },
    getDetail (data) {
      if (data.overviewDetail) {
        let obj = JSON.parse(data.overviewDetail);
        let keys = Object.keys(obj);
        let list = [];
        keys.forEach(item => {
          list.push({
            ...obj[item],
            model: item
          });
        });
        this.detailList = list;
        this.detailTableHeight = this.$refs['detail'].clientHeight;
      } else {
        this.detailList = [];
      }
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
    addOrder () {
      if (this.data.currentBillTokenConsumption <= 0) {
        this.$message.warning("当前账单消耗量为0，无法生成账单");
        return;
      }
      this.$confirm('是否确认生成账单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        generateBill({
          lastGenerateBillTime: this.data.lastGenerateBillTime,
          deptId: this.data.deptId,
          userId: this.data.userId,
          id: this.data.id,
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
    },
    refresh (show) {
      this.loading = true;
      listGptconsumptionoverview({
        pageNum: 1,
        pageSize: 10,
        deptId: this.$store.state.user.deptId,
        userId: this.$store.state.user.userId,
        lastGenerateBillTime: this.data.lastGenerateBillTime || null,
        type: 'user'
      }).then(response => {
        this.gptconsumptionoverviewList = response.rows;
        console.log(this.gptconsumptionoverviewList);
        this.data = {
          id: this.gptconsumptionoverviewList[0].id,
          deptName: this.gptconsumptionoverviewList[0].deptName,
          deptId: this.gptconsumptionoverviewList[0].deptId,
          userId: this.gptconsumptionoverviewList[0].userId,
          userName: this.gptconsumptionoverviewList[0].userName,
          totalTokenConsumption: this.gptconsumptionoverviewList[0].totalTokenConsumption, //总消耗量
          totalConsumptionAmount: this.gptconsumptionoverviewList[0].totalConsumptionAmount, //总金额
          currentBillTokenConsumption: this.gptconsumptionoverviewList[0].currentBillTokenConsumption, //未结算消耗量
          currentBillConsumptionAmount: this.gptconsumptionoverviewList[0].currentBillConsumptionAmount, //未结算金额
          remark: this.gptconsumptionoverviewList[0].remark,
          lastGenerateBillTime: this.gptconsumptionoverviewList[0].lastGenerateBillTime,
        }
        this.getDetail(response.rows[0] || {});
        this.total = response.total;
        if (show) this.$message.success("刷新成功");
        this.loading = false;
      });
    },
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
  justify-content: space-between;
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

.row {
  display: flex;
  flex-direction: row;
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
