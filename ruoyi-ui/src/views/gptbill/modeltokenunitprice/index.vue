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
              @submit.native.prevent
            >
              <el-form-item
                label="模型"
                prop="model"
              >
                <el-input
                  v-model="queryParams.model"
                  placeholder="请输入模型"
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
                  type="primary"
                  plain
                  icon="el-icon-plus"
                  size="mini"
                  @click="handleAdd"
                  v-hasPermi="['gptbill:modeltokenunitprice:add']"
                >新增
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="success"
                  plain
                  icon="el-icon-edit"
                  size="mini"
                  :disabled="single"
                  @click="handleUpdate"
                  v-hasPermi="['gptbill:modeltokenunitprice:edit']"
                >修改
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="danger"
                  plain
                  icon="el-icon-delete"
                  size="mini"
                  :disabled="multiple"
                  @click="handleDelete"
                  v-hasPermi="['gptbill:modeltokenunitprice:remove']"
                >删除
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="warning"
                  plain
                  icon="el-icon-download"
                  size="mini"
                  @click="handleExport"
                  v-hasPermi="['gptbill:modeltokenunitprice:export']"
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
            :data="modeltokenunitpriceList"
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
              label="模型"
              align="center"
              prop="model"
            />
            <el-table-column
              label="Token单价"
              align="center"
              prop="unitModelTokenPrice"
            />
            <el-table-column
              label="备注"
              align="center"
              prop="remark"
            />
            <el-table-column
              label="操作"
              align="center"
              class-name="small-padding fixed-width"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['gptbill:modeltokenunitprice:edit']"
                >修改
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['gptbill:modeltokenunitprice:remove']"
                >删除
                </el-button>
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

    <!-- 添加或修改模型单位token价格对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="30%"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
      >
        <el-form-item
          label="模型名称"
          prop="model"
        >
          <el-input
            v-model="form.model"
            placeholder="请输入模型名称"
          />
        </el-form-item>
        <el-form-item
          label="Token单价"
          prop="unitModelTokenPrice"
        >
          <el-input
            v-model="form.unitModelTokenPrice"
            placeholder="请输入该Token单价"
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
  listModeltokenunitprice,
  getModeltokenunitprice,
  delModeltokenunitprice,
  addModeltokenunitprice,
  updateModeltokenunitprice
} from "@/api/gptbill/modeltokenunitprice";

export default {
  name: "Modeltokenunitprice",
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
      // 模型单位token价格表格数据
      modeltokenunitpriceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        model: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        model: [
          { required: true, message: "模型不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created () {
    this.getList();
  },
  methods: {
    /** 查询模型单位token价格列表 */
    getList () {
      this.loading = true;
      listModeltokenunitprice(this.queryParams).then(response => {
        this.modeltokenunitpriceList = response.rows;
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
        model: null,
        unitModelTokenPrice: null,
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
      this.title = "添加模型单价";
    },
    /** 修改按钮操作 */
    handleUpdate (row) {
      this.reset();
      const id = row.id || this.ids
      getModeltokenunitprice(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改模型单价";
      });
    },
    /** 提交按钮 */
    submitForm () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateModeltokenunitprice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addModeltokenunitprice(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除模型单位token价格编号为"' + ids + '"的数据项？').then(function () {
        return delModeltokenunitprice(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport () {
      this.download('gptbill/modeltokenunitprice/export', {
        ...this.queryParams
      }, `modeltokenunitprice_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

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
