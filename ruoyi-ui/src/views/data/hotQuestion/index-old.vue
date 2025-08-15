<template>
  <div style="margin-top: 10px;" id="searchDiv">
    <el-card style="border: none;box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.1);border-radius: 8px;margin: 30px;">
      <el-row :gutter="10">
        <el-form :model="queryAnalyzeParams"
                 ref="queryAnalyzeForm"
                 size="small"
                 :inline="true"
                 v-show="showAnalyze"
                 style="position: center;">

          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="国家区域">
              <el-select
                v-model="queryAnalyzeParams.region"
                multiple
                placeholder="选择国家区域"
                clearable
                collapse-tags
              >
                <el-option
                  v-for="(item,index) in areaOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
                <!--                  <el-option-->
                <!--                    v-for="(item,index) in dict.region_type"-->
                <!--                    :key="index"-->
                <!--                    :label="item.label"-->
                <!--                    :value="item.value">-->
                <!--                  </el-option>-->
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="用户身份">
              <el-select
                v-model="queryAnalyzeParams.role"
                multiple
                placeholder="选择用户身份"
                clearable
                collapse-tags
              >
                <el-option
                  v-for="(item,index) in userRoleOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>

          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="准确性评价" >
              <el-select
                v-model="queryAnalyzeParams.isAccuracy"
                placeholder="请选择准确性评价"
                clearable
              >
                <el-option
                  label="准确"
                  value="yes"/>
                <el-option
                  label="不准确"
                  value="no"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="选择时间">
              <el-date-picker
                v-model="selectTime"
                class="inputDatePicker"
                style="width: 240px"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="handleSelectUploadTime"
                clearable
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="分析条数">
              <el-input-number v-model="queryAnalyzeParams.questionNum"
                               :value="null"
                               controls-position="right"
                               @change="handleChange"
                               :min="1"
                               :max="100"
                               placeholder="请输入1-100"
                               style="width: 100%">
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item>
              <el-button type="primary" icon="el-icon-connection" size="mini" @click="handleAnalyze">分析</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetAnalyze">重叠</el-button>
            </el-form-item>
          </el-col>
        </el-form>
        <!--                 <right-toolbar :showSearch.sync="showAnalyze" @queryTable="getList"></right-toolbar>-->
      </el-row>
    </el-card>

    <el-card style="border: none;box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.1);border-radius: 8px;margin: 30px;">
      <el-row :gutter="10">
        <el-form
          size="small"
          :inline="true"
          :model="queryParams"
          label-width="100px"
          v-show="showSearch"
          style="width:100%;height:fit-content;"
        >
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="id" prop="id">
              <el-input
                v-model="queryParams.id"
                placeholder="请输入id"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="提问" prop="question">
              <el-input
                v-model="queryParams.question"
                placeholder="请输入提问"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="回答" prop="answer">
              <el-input
                v-model="queryParams.answer"
                placeholder="请输入回答"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="创建者" prop="createBy">
              <el-input
                v-model="queryParams.createBy"
                placeholder="请输入创建者"
                clearable
                @keyup.enter.native="handleQuery"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="用户身份">
              <el-select
                v-model="queryParams.role"
                multiple
                placeholder="选择用户身份"

                clearable
              >
                <el-option
                  v-for="(item,index) in userRoleOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="状态" prop="status">
              <el-select
                v-model="queryParams.status"
                placeholder="请选择解决状态"
                clearable
                @keyup.enter.native="handleQuery"
              >
                <el-option
                  v-for="(item,index) in statusOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="国家/区域">
              <el-select
                v-model="queryParams.region"
                multiple
                placeholder="选择国家/区域"
                clearable
              >
                <el-option
                  v-for="(item,index) in areaOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>


          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="创建时间">
              <el-date-picker
                v-model="uploadTime"
                class="inputDatePicker"
                style="width: 240px"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="handleUploadTime"
                clearable
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="语言">
              <el-select
                v-model="queryParams.language"
                placeholder="选择语言"
                clearable
              >
                <el-option
                  v-for="(item,index) in languageOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>

            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">

          </el-col>
        </el-form>
      </el-row>
      <el-row :gutter="10" class="mb8">
        <el-col :span="17">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['gpt:hotQuestion:add']"
          >新增</el-button>
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['gpt:hotQuestion:edit']"
          >修改</el-button>
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['gpt:hotQuestion:remove']"
          >删除</el-button>
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['gpt:hotQuestion:export']"
          >导出</el-button>
        </el-col>

        <el-col :span="5">
          <el-button type="primary" icon="el-icon-search" size="medium" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
          <el-button icon="el-icon-upload" size="medium" @click="upload">入库</el-button>

        </el-col>
        <el-col :span="2">
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar> </el-col>
      </el-row>

      <el-table v-loading="loading" :data="hotQuestionList"
                ref="fileTable"
                height="550"
                @selection-change="handleSelectionChange"
                style="border-top:solid #E4E7ED;">
        <el-table-column type="selection" width="55" align="center" :selectable="selectable"/>
        <el-table-column label="id" align="center" prop="id" show-overflow-tooltip />
        <el-table-column label="recordId" align="center" prop="recordId" show-overflow-tooltip />
        <el-table-column label="提问" align="center" prop="question" show-overflow-tooltip/>
        <el-table-column label="回答" align="center" prop="answer"  show-overflow-tooltip/>
        <el-table-column label="语言" align="center" prop="language"  show-overflow-tooltip/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180"  value-format="yyyy-MM-dd HH:mm:ss">
          <template slot-scope="scope">
            <span>{{ (scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建者"  align="center" prop="createBy"/>
        <el-table-column label="国家" align="center" prop="region"  show-overflow-tooltip/>
        <el-table-column label="角色" align="center" prop="role"  show-overflow-tooltip/>
        <el-table-column
          prop="status"
          label="状态"
          align="center"
        >
          <template slot-scope="scope" align="enter">
            <div
              style="position: absolute;left:0;right:0;top:0;bottom:0;margin: auto;width: fit-content;height: fit-content;padding: 6px 8px;">
              <el-tag
                v-if="scope.row.status"
                style="font-size:14px;"
                :type="scope.row.status === 'success' ? 'success'
                      : scope.row.status === 'untrained' ? 'primary'
                      : scope.row.status === 'training' ? 'warning': 'info'">
                {{ statusOptions.find(item => item.value === scope.row.status).label }}
                <i class="el-icon-loading" style="font-size:16px;" v-if="scope.row.trainStatus === 'training'"/>
              </el-tag>
              <span v-else> - </span>
              <div style="position: absolute;right:0;top:0;">
                <el-tooltip v-if="scope.row.trainStatus === 'error'" effect="light"
                            :content="scope.row.trainFlow || '-'" placement="top">
                  <i class="el-icon-warning" style="font-size: 18px;color: #f3c27a"/>
                </el-tooltip>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['gpt:hotQuestion:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              class="redDot"
              icon="el-icon-delete"
              style="font-size: 14px;font-weight: bold"
              @click="handleDelete(scope.row)"
              :disabled="scope.row.isDeleting"
              v-hasPermi="['gpt:hotQuestion:remove']"
            >
              删除
            </el-button>

          </template>
        </el-table-column>
      </el-table>
      <div
        align="right"
        style="margin-top: 9px;"
      >
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </div>

      <!-- 添加或修改热点问题对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" label-width="100px">
          <el-form-item label="提问" prop="question">
            <el-input v-model="form.question" placeholder="请输入提问" />
          </el-form-item>
          <el-form-item label="回答" prop="answer">
            <el-input type="textarea" v-model="form.answer" placeholder="请输入回答" :rows="5" style="border:solid #bfbfbf;"/>
          </el-form-item>
          <el-form-item label="国家/区域" prop="region">
            <el-select
              v-model="form.region"
              filterable
              multiple
              placeholder="选择国家/区域"
              clearable
            >
              <el-option
                v-for="(item,index) in areaOptions"
                :key="index"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="用户身份" prop="role">
            <el-select
              v-model="form.role"
              filterable
              multiple
              placeholder="选择用户身份"
              clearable
            >
              <el-option
                v-for="(item,index) in userRoleOptions"
                :key="index"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import {
  listHotQuestion,
  getHotQuestion,
  delHotQuestion,
  addHotQuestion,
  updateHotQuestion,
  analyzeHotQuestion,
  trainQuestion
} from '@/api/data/hotQuestion'
import axios from 'axios'
import { getToken } from '@/utils/auth'
import { getRegion } from '@/api/data/region'
import { delFile, Trained } from '@/api/data/uploadFile'

export default {
  name: "HotQuestion",
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
      showAnalyze:true,
      // 总条数
      total: 0,
      // 热点问题表格数据
      hotQuestionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      baseurl:process.env. VUE_APP_HOT_URL,
      // 分析查询参数
      queryAnalyzeParams: {
        // pageNum: 1,
        // pageSize: 10,

        region: [],
        role: [],
        startTime: null,//开始时间
        endTime: null,//结束时间
        isAccuracy: null,//准确性
        questionNum:null,//条数
      },
      // 搜索查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id:null,
        question: null,//提问
        answer: null,//回答
        startTime: null,//开始时间
        endTime: null,//结束时间
        region: [],
        role: [],
        // roleId: null,//角色身份
        // regionId:null,//国家
        createBy: null,//创建者
        status: null,//状态
        language:'zh-CN',
        recordId:null,
      },
      selectTime:[],//分析时间
      uploadTime: [],//创建时间
      multipleSelection:[],//存储选中数据id
      multipleSelectionData:[],//存储选中数据
      //国家
      areaOptions: [
        {
          label: 'China',
          value: 'China'
        },
        {
          label: 'Germany',
          value: 'Germany'
        },
        {
          label: 'Italy',
          value: 'Italy'
        },
        {
          label: 'Japan',
          value: 'Japan'
        },
        {
          label: 'Netherlands',
          value: 'Netherlands'
        },
        {
          label: 'Spain',
          value: 'Spain'
        },
        {
          label: 'United Kingdom',
          value: 'United Kingdom'
        },

      ],
      //用户
      userRoleOptions: [
        {
          label: '服务伙伴',
          value: 'servicepartner'
        },
        {
          label: '公司管理员',
          value: 'systemmanager'
        },
        {
          label: '国内售后',
          value: 'dasw'
        },
        {
          label: '安装商',
          value: 'installer'
        },
        {
          label: '南通生产',
          value: 'sncreator'
        },
        {
          label: '国外售后',
          value: 'servicer'
        },
        {
          label: '终端用户',
          value:'customer'
        },
        {
          label: '分销商',
          value: 'reseller'
        },
        {
          label: '分享用户',
          value: 'sharer'
        },
        {
          label: '供应商',
          value: 'supplier'
        },
        {
          label: '投资者',
          value: 'investor'
        },
        {
          label: '订单角色',
          value: 'oflduser'
        },
      ],
      //状态
      statusOptions: [
        {
          label: '未入库',
          value: 'untrained'
        },
        {
          label: '入库中',
          value: 'training'
        },
        {
          label: '已入库',
          value: 'success'
        },
      ],

      languageOptions: [
        {
          label: '简体中文',
          value: 'zh-CN'
        },
        {
          label: '英语',
          value: 'en-US'
        },
        {
          label: '西班牙语',
          value: 'es-ES'
        },
        {
          label: '德语',
          value: 'de-DE'
        },
        {
          label: '意大利语',
          value: 'it-IT'
        },
        {
          label: '日语',
          value: 'ja-JP'
        },
        {
          label: '荷兰语',
          value: 'nl-NL'
        },
        {
          label: '西班牙',
          value: 'sp-SP'
        },

      ],

      // 表单参数
      form: {
        region: [],
        role: [],
        isAccuracy:null,
        question:null,
        answer:null,
        id:null,
        language: null,
        recordId: null,
      },
      // 表单校验
      rules: {
        question: [ { required: true, message: "question不能为空", trigger: "blur" }],
        answer: [ { required: true, message: "question不能为空", trigger: "blur" }],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {

    /** 查询热点问题列表 */
    getList() {
      this.loading = true;
      var queryParams = JSON.parse(JSON.stringify(this.queryParams));
      if(queryParams.role){ queryParams.role = this.queryParams.role.join(";");}
      if(queryParams.region){queryParams.region = this.queryParams.region.join(";");}
      listHotQuestion(queryParams).then(response => {
        this.hotQuestionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    // 查询表单重置
    reset() {
      this.form = {
        id: null,
        question: null,
        answer: null,
        region: [],
        role: [],
        isAccuracy: null,
        createBy: null,
        status: null,
        startTime: null,//开始时间
        endTime: null,//结束时间
      };
      this.resetForm("form");
    },
    /** 分析操作*/
    handleAnalyze(){
      this.hotQuestionList = []; // 清空表格
      const queryAnalyzeParams = JSON.parse(JSON.stringify(this.queryAnalyzeParams));
      if(queryAnalyzeParams.role){ queryAnalyzeParams.role = this.queryAnalyzeParams.role.join(";");}
      if(queryAnalyzeParams.region){queryAnalyzeParams.region = this.queryAnalyzeParams.region.join(";");}
      this.$message.info('开始分析，请稍候...');
      analyzeHotQuestion(queryAnalyzeParams).then(response => {
        this.hotQuestionList = response.data.rows;
        this.total = response.data.total;
        this.loading = false;
        console.log(response);

      }).catch(error => {
        this.$message.error(response.data.msg);
      });
    },
    /** 分析表单重叠*/
    resetAnalyze() {
      this.queryAnalyzeParams={
        role: [],//角色身份
        region:[],//国家
        startTime: null,//开始时间
        endTime: null,//结束时间
        isAccuracy: null,//准确性
        questionNum:null,//条数
      }
      this.selectTime = null;
    },

    handleChange(value) {

    },
    /** 搜索按钮操作 */
    handleQuery() {
      var queryParams = JSON.parse(JSON.stringify(this.queryParams));
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        id:null,
        question: null,//提问
        answer: null,//回答
        startTime:null,
        endTime:null,
        region: [],
        role: [],
        createBy: null,//创建者
        status: null,//状态
      },
        this.uploadTime=null;
      this.handleQuery();
    },

    selectable(row, index) {
      return row.status !== 'training';
    },

    //开始时间-结束时间
    handleSelectUploadTime(val) {
      this.queryAnalyzeParams.startTime = val[0];
      this.queryAnalyzeParams.endTime = val[1];
    },
    handleUploadTime(val) {
      this.queryParams.startTime = val[0];
      this.queryParams.endTime = val[1];
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加热点问题";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const ids = row.id || this.ids
      getHotQuestion(ids).then(response => {
        this.form = response.data;
        if (!this.form.region) {
          this.form.region = [];
        } else {
          this.form.region = this.form.region.split(';').map(item => item.trim());
        }
        if (!this.form.role) {
          this.form.role = [];
        } else {
          this.form.role = this.form.role.split(';').map(item => item.trim());
        }

        this.open = true;
        this.title = "修改热点问题";
      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.form.status = "untrained";
            this.form.region = this.form.region.join(';');
            this.form.role = this.form.role.join(';');
            updateHotQuestion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.status = "untrained";
            this.form.region = this.form.region.join(';');
            this.form.role = this.form.role.join(';');
            addHotQuestion(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    //训练入库
    upload() {
      if (this.multipleSelectionData.length < 1) {
        this.$message.warning('请选择入库的数据');
        return;
      }
      let list = this.multipleSelectionData.map(item => {

        return {
          id: item.id,
          question:item.question,
          answer:item.answer,
          recordId: item.recordId,
          regionList: item.region.split(',').join(';'),
          roleList: item.role.split(',').join(';')
        };
      });
      let data ={
        list
      }

      this.multipleSelectionData = [];
      setTimeout(() => {
        this.getList();
      }, 3 * 1000)
      trainQuestion(data).then(() => {
        this.$modal.msgSuccess("上传入库成功");
      }).catch(() => {
        this.$message.error(response.data.msg);
      });
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.multipleSelection = selection.map(item => item.id);
      this.multipleSelectionData = selection;
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
      this.selectedRows = selection;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.multipleSelection.map(item => item.id);
      console.log(ids);
      this.$confirm('此操作将永久删除该文件，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancleButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.isDeleting = true;
        this.multipleSelection = [];
        this.$refs.fileTable.clearSelection();
        delHotQuestion(ids).then(() => {
          this.getList();
          this.$message.success("删除成功");
          this.multipleSelection = [];
        })
      }).catch(() => {
        row.isDeleting = false;
      });
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('/gpt/hotQuestion/export', {
        ...this.queryParams
      }, `hotQuestion_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style lang="scss" scoped>
.search-form {
  ::v-deep .el-form-item__label {
    min-width: 80px;
  }
}

.gray-color {
  .search-form-card {
    border-color: #828282;

    .search-form-header {
      .icon {
        border-color: #E0E0E0;
      }
    }

    .search-form-data {
      border-color: #E0E0E0;
    }
  }
}

.green-color {
  .search-form-card {
    border-color: #3FAD6E;

    .search-form-header {
      .icon {
        border-color: #94C9AA;
      }
    }

    .search-form-data {
      border-color: #94C9AA;
    }
  }
}

.orange-color {
  .search-form-card {
    border-color: #E7AC78;

    .search-form-header {
      .icon {
        border-color: #FEDDBF;
      }
    }

    .search-form-data {
      border-color: #FEDDBF;
    }
  }
}

.blue-color {
  .search-form-card {
    border-color: #2C9DDD;

    .search-form-header {
      .icon {
        border-color: #8FBED8;
      }
    }

    .search-form-data {
      border-color: #8FBED8;
    }
  }
}

.search-form-card {
  margin: 0 4px;
  padding: 6px 8px;
  border-radius: 10px;
  border: 1px solid #999093;
  display: flex;
  flex-direction: column;

  .search-form-header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .title {
      color: #333333;
      font-size: 16px;
    }

    .icon {
      font-size: 14px;
      border-radius: 20px;
      padding: 6px;
      border: 1px solid #999093;
      width: 30px;
      height: 30px;
      text-align: center;
    }
  }

  .search-form-data {
    border-radius: 10px;
    border: 1px solid #888a8e;
    display: flex;
    align-items: center;
    margin-top: 8px;
    padding-right: 8px;

    .search-form-data-select {
      margin-right: 4px;


      ::v-deep .el-input--medium .el-input__inner {
        height: 40px;
        border: none;
        width: 80px;
        padding: 0;
        text-align: center;
        color: #909399;
        border-radius: 10px;
      }

      ::v-deep .el-input__suffix {
        display: none;
      }

    }


    ::v-deep .el-input--medium .el-input__inner {
      border-radius: 0px;
      margin: 2px 0px;
      border-top: none;
      border-left: none;
      border-right: none;
    }
  }

}

::v-deep .el-table th.el-table__cell > .cell {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
<template>
  <div style="margin-top: 10px;" id="searchDiv">
    <el-card style="border: none;box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.1);border-radius: 8px;margin: 30px;">
      <el-row :gutter="10">
        <el-form
          :model="queryAnalyzeParams"
          ref="queryAnalyzeParams"
          size="small"
          :inline="true"
          v-show="showAnalyze"
          style="position: center;"
          :rules="formRules"
        >
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="国家区域" required>
              <el-select
                v-model="queryAnalyzeParams.region"
                multiple
                placeholder="选择国家区域"
                :rules="formRules.region"
                clearable
                collapse-tags
              >
                <el-option
                  v-for="(item,index) in areaOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="用户身份" required>
              <el-select
                v-model="queryAnalyzeParams.role"
                multiple
                placeholder="选择用户身份"
                :rules="formRules.role"
                clearable
                collapse-tags
              >
                <el-option
                  v-for="(item,index) in userRoleOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>

          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="准确性评价" >
              <el-select
                v-model="queryAnalyzeParams.isAccuracy"
                placeholder="请选择准确性评价"
                clearable
              >
                <el-option
                  label="准确"
                  value="yes"/>
                <el-option
                  label="不准确"
                  value="no"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="时间范围" required>
              <el-date-picker
                v-model="queryAnalyzeParams.selectTime"
                class="inputDatePicker"
                style="width: 240px"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="handleSelectUploadTime"
                :rules="formRules.selectTime"
                clearable
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="分析条数" required>
              <el-input-number v-model="queryAnalyzeParams.questionNum"
                               :value="null"
                               controls-position="right"
                               @change="handleChange"
                               :rules="formRules.questionNum"
                               :min="1"
                               :max="100"
                               placeholder="请输入1-100"
                               style="width: 100%">
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item>
              <el-button type="primary" icon="el-icon-connection" size="mini" @click="handleAnalyze">分析</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetAnalyze">重叠</el-button>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
    </el-card>

    <el-card style="border: none;box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.1);border-radius: 8px;margin: 30px;">
      <el-row :gutter="10">
        <el-form
          size="small"
          :inline="true"
          :model="queryParams"
          label-width="100px"
          v-show="showSearch"
          style="width:100%;height:fit-content;"
        >
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="id" prop="id">
              <el-input
                v-model="queryParams.id"
                placeholder="请输入id"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="提问" prop="question">
              <el-input
                v-model="queryParams.question"
                placeholder="请输入提问"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="回答" prop="answer">
              <el-input
                v-model="queryParams.answer"
                placeholder="请输入回答"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="创建者" prop="createBy">
              <el-input
                v-model="queryParams.createBy"
                placeholder="请输入创建者"
                clearable
                @keyup.enter.native="handleQuery"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="用户身份">
              <el-select
                v-model="queryParams.role"
                multiple
                placeholder="选择用户身份"

                clearable
              >
                <el-option
                  v-for="(item,index) in userRoleOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="状态" prop="status">
              <el-select
                v-model="queryParams.status"
                placeholder="请选择解决状态"
                clearable
                @keyup.enter.native="handleQuery"
              >
                <el-option
                  v-for="(item,index) in statusOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="国家/区域">
              <el-select
                v-model="queryParams.region"
                multiple
                placeholder="选择国家/区域"
                clearable
              >
                <el-option
                  v-for="(item,index) in areaOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>


          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="创建时间">
              <el-date-picker
                v-model="uploadTime"
                class="inputDatePicker"
                style="width: 240px"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @change="handleUploadTime"
                clearable
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
            <el-form-item label="语言">
              <el-select
                v-model="queryParams.language"
                placeholder="选择语言"
                clearable
              >
                <el-option
                  v-for="(item,index) in languageOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>

            </el-form-item>
          </el-col>
          <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">

          </el-col>
        </el-form>
      </el-row>
      <el-row :gutter="10" class="mb8">
        <el-col :span="17">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['gpt:hotQuestion:add']"
          >新增</el-button>
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['gpt:hotQuestion:edit']"
          >修改</el-button>
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['gpt:hotQuestion:remove']"
          >删除</el-button>
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['gpt:hotQuestion:export']"
          >导出</el-button>
        </el-col>

        <el-col :span="5">
          <el-button type="primary" icon="el-icon-search" size="medium" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="medium" @click="resetQuery">重置</el-button>
          <el-button icon="el-icon-upload" size="medium" @click="upload">入库</el-button>

        </el-col>
        <el-col :span="2">
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar> </el-col>
      </el-row>

      <el-table v-loading="loading" :data="hotQuestionList"
                ref="fileTable"
                height="550"
                @selection-change="handleSelectionChange"
                style="border-top:solid #E4E7ED;">
        <el-table-column type="selection" width="55" align="center" :selectable="selectable"/>
        <el-table-column label="id" align="center" prop="id" show-overflow-tooltip />
        <el-table-column label="recordId" align="center" prop="recordId" show-overflow-tooltip />
        <el-table-column label="提问" align="center" prop="question" show-overflow-tooltip/>
        <el-table-column label="回答" align="center" prop="answer"  show-overflow-tooltip/>
        <el-table-column label="语言" align="center" prop="language"  show-overflow-tooltip/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180"  value-format="yyyy-MM-dd HH:mm:ss">
          <template slot-scope="scope">
            <span>{{ (scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建者"  align="center" prop="createBy"/>
        <el-table-column label="国家" align="center" prop="region"  show-overflow-tooltip/>
        <el-table-column label="角色" align="center" prop="role"  show-overflow-tooltip/>
        <el-table-column
          prop="status"
          label="状态"
          align="center"
        >
          <template slot-scope="scope" align="enter">
            <div
              style="position: absolute;left:0;right:0;top:0;bottom:0;margin: auto;width: fit-content;height: fit-content;padding: 6px 8px;">
              <el-tag
                v-if="scope.row.status"
                style="font-size:14px;"
                :type="scope.row.status === 'success' ? 'success'
                      : scope.row.status === 'untrained' ? 'primary'
                      : scope.row.status === 'training' ? 'warning': 'info'">
                {{ statusOptions.find(item => item.value === scope.row.status).label }}
                <i class="el-icon-loading" style="font-size:16px;" v-if="scope.row.trainStatus === 'training'"/>
              </el-tag>
              <span v-else> - </span>
              <div style="position: absolute;right:0;top:0;">
                <el-tooltip v-if="scope.row.trainStatus === 'error'" effect="light"
                            :content="scope.row.trainFlow || '-'" placement="top">
                  <i class="el-icon-warning" style="font-size: 18px;color: #f3c27a"/>
                </el-tooltip>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['gpt:hotQuestion:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              class="redDot"
              icon="el-icon-delete"
              style="font-size: 14px;font-weight: bold"
              @click="handleDelete(scope.row)"
              :disabled="scope.row.isDeleting"
              v-hasPermi="['gpt:hotQuestion:remove']"
            >
              删除
            </el-button>

          </template>
        </el-table-column>
      </el-table>
      <div
        align="right"
        style="margin-top: 9px;"
      >
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </div>

      <!-- 添加或修改热点问题对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" label-width="100px">
          <el-form-item label="提问" prop="question">
            <el-input v-model="form.question" placeholder="请输入提问" />
          </el-form-item>
          <el-form-item label="回答" prop="answer">
            <el-input type="textarea" v-model="form.answer" placeholder="请输入回答" :rows="5" style="border:solid #bfbfbf;"/>
          </el-form-item>
          <el-form-item label="国家/区域" prop="region">
            <el-select
              v-model="form.region"
              filterable
              multiple
              placeholder="选择国家/区域"
              clearable
            >
              <el-option
                v-for="(item,index) in areaOptions"
                :key="index"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="用户身份" prop="role">
            <el-select
              v-model="form.role"
              filterable
              multiple
              placeholder="选择用户身份"
              clearable
            >
              <el-option
                v-for="(item,index) in userRoleOptions"
                :key="index"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>
<script>
import {
  listHotQuestion,
  getHotQuestion,
  delHotQuestion,
  addHotQuestion,
  updateHotQuestion,
  analyzeHotQuestion,
  trainQuestion
} from '@/api/data/hotQuestion'
import axios from 'axios'
import { getToken } from '@/utils/auth'
import { getRegion } from '@/api/data/region'
import { delFile, Trained } from '@/api/data/uploadFile'

export default {
  name: "HotQuestion",
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
      showAnalyze:true,
      // 总条数
      total: 0,
      // 热点问题表格数据
      hotQuestionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      baseurl:process.env. VUE_APP_HOT_URL,
      // 分析查询参数
      queryAnalyzeParams: {
        region: [],
        role: [],
        startTime: null,//开始时间
        endTime: null,//结束时间
        isAccuracy: null,//准确性
        questionNum:10,//条数
      },

      // 搜索查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id:null,
        question: null,//提问
        answer: null,//回答
        startTime: null,//开始时间
        endTime: null,//结束时间
        region: [],
        role: [],
        createBy: null,//创建者
        status: null,
        language:'zh-CN',
        recordId:null,
      },
      selectTime:[],//分析时间
      uploadTime: [],//创建时间
      multipleSelection:[],//存储选中数据id
      multipleSelectionData:[],//存储选中数据
      //国家
      areaOptions: [
        {
          label: 'China',
          value: 'China'
        },
        {
          label: 'Germany',
          value: 'Germany'
        },
        {
          label: 'Italy',
          value: 'Italy'
        },
        {
          label: 'Japan',
          value: 'Japan'
        },
        {
          label: 'Netherlands',
          value: 'Netherlands'
        },
        {
          label: 'Spain',
          value: 'Spain'
        },
        {
          label: 'United Kingdom',
          value: 'United Kingdom'
        },

      ],
      //用户
      userRoleOptions: [
        {
          label: '服务伙伴',
          value: 'servicepartner'
        },
        {
          label: '公司管理员',
          value: 'systemmanager'
        },
        {
          label: '国内售后',
          value: 'dasw'
        },
        {
          label: '安装商',
          value: 'installer'
        },
        {
          label: '南通生产',
          value: 'sncreator'
        },
        {
          label: '国外售后',
          value: 'servicer'
        },
        {
          label: '终端用户',
          value:'customer'
        },
        {
          label: '分销商',
          value: 'reseller'
        },
        {
          label: '分享用户',
          value: 'sharer'
        },
        {
          label: '供应商',
          value: 'supplier'
        },
        {
          label: '投资者',
          value: 'investor'
        },
        {
          label: '订单角色',
          value: 'oflduser'
        },
      ],
      //状态
      statusOptions: [
        {
          label: '未入库',
          value: 'untrained'
        },
        {
          label: '入库中',
          value: 'training'
        },
        {
          label: '已入库',
          value: 'success'
        },
      ],

      languageOptions: [
        {
          label: '简体中文',
          value: 'zh-CN'
        },
        {
          label: '英语',
          value: 'en-US'
        },
        {
          label: '西班牙语',
          value: 'es-ES'
        },
        {
          label: '德语',
          value: 'de-DE'
        },
        {
          label: '意大利语',
          value: 'it-IT'
        },
        {
          label: '日语',
          value: 'ja-JP'
        },
        {
          label: '荷兰语',
          value: 'nl-NL'
        },
        {
          label: '西班牙',
          value: 'sp-SP'
        },

      ],
      formRules: {
        role: [
          { required: true, message: '请选择角色类型', trigger: 'change' },
        ],
        region: [
          { required: true, message: '请选择区域', trigger: 'change' },
        ],
        selectTime: [
          { type: 'array', required: true, message: '请选择时间', trigger: 'change' }
        ],
        questionNum: [
          { required: true, message: '请输入分析条数', trigger: 'change' },
          { type: 'number', message: '请输入数字', trigger: 'change' },
          { min: 1, max: 100, message: '请输入1-100之间的数字', trigger: 'change' }
        ]
      },
      // 表单参数
      form: {
        region: [],
        role: [],
        isAccuracy:null,
        question:null,
        answer:null,
        id:null,
        language: null,
        recordId: null,
      },

    };
  },
  created() {
    this.getList();
  },
  methods: {

    /** 查询热点问题列表 */
    getList() {
      this.loading = true;
      var queryParams = JSON.parse(JSON.stringify(this.queryParams));
      if(queryParams.role){ queryParams.role = this.queryParams.role.join(";");}
      if(queryParams.region){queryParams.region = this.queryParams.region.join(";");}
      listHotQuestion(queryParams).then(response => {
        this.hotQuestionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    // 查询表单重置
    reset() {
      this.form = {
        id: null,
        question: null,
        answer: null,
        region: [],
        role: [],
        isAccuracy: null,
        createBy: null,
        status: null,
        startTime: null,//开始时间
        endTime: null,//结束时间
      };
      this.resetForm("form");
    },
    /** 分析操作*/
    handleAnalyze(){
      this.hotQuestionList = []; // 清空表格
      const queryAnalyzeParams = JSON.parse(JSON.stringify(this.queryAnalyzeParams));
      if(queryAnalyzeParams.role){ queryAnalyzeParams.role = this.queryAnalyzeParams.role.join(";");}
      if(queryAnalyzeParams.region){queryAnalyzeParams.region = this.queryAnalyzeParams.region.join(";");}
      this.$message.info('开始分析，请稍候...');
      analyzeHotQuestion(queryAnalyzeParams).then(response => {
        this.hotQuestionList = response.data.rows;
        this.total = response.data.total;
        this.loading = false;

      }).catch(error => {
        this.$message.error(response.data.msg);
      });
    },

    /** 分析表单重叠*/
    resetAnalyze() {
      this.queryAnalyzeParams={
        role: [],
        region:[],
        startTime: null,
        endTime: null,
        isAccuracy: null,
        questionNum:null,
      }
      this.selectTime = null;
      this.$refs.queryAnalyzeParams.resetFields(); // 重置表单验证状态
    },

    handleChange(value) {

    },
    /** 搜索按钮操作 */
    handleQuery() {
      var queryParams = JSON.parse(JSON.stringify(this.queryParams));
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        id:null,
        question: null,
        answer: null,
        startTime:null,
        endTime:null,
        region: [],
        role: [],
        createBy: null,
        status: null,
      },
        this.uploadTime=null;
      this.handleQuery();
    },

    selectable(row, index) {
      return row.status !== 'training';
    },

    //开始时间-结束时间
    handleSelectUploadTime(val) {
      this.queryAnalyzeParams.startTime = val[0];
      this.queryAnalyzeParams.endTime = val[1];
    },
    handleUploadTime(val) {
      this.queryParams.startTime = val[0];
      this.queryParams.endTime = val[1];
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加热点问题";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const ids = row.id || this.ids
      getHotQuestion(ids).then(response => {
        this.form = response.data;
        if (!this.form.region) {
          this.form.region = [];
        } else {
          this.form.region = this.form.region.split(';').map(item => item.trim());
        }
        if (!this.form.role) {
          this.form.role = [];
        } else {
          this.form.role = this.form.role.split(';').map(item => item.trim());
        }

        this.open = true;
        this.title = "修改热点问题";
      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.form.status = "untrained";
            this.form.region = this.form.region.join(';');
            this.form.role = this.form.role.join(';');
            updateHotQuestion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.status = "untrained";
            this.form.region = this.form.region.join(';');
            this.form.role = this.form.role.join(';');
            addHotQuestion(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    //训练入库
    upload() {
      if (this.multipleSelectionData.length < 1) {
        this.$message.warning('请选择入库的数据');
        return;
      }
      let list = this.multipleSelectionData.map(item => {

        return {
          id: item.id,
          question:item.question,
          answer:item.answer,
          recordId: item.recordId,
          regionList: item.region.split(',').join(';'),
          roleList: item.role.split(',').join(';')
        };
      });
      let data ={
        list
      }

      this.multipleSelectionData = [];
      setTimeout(() => {
        this.getList();
      }, 3 * 1000)
      trainQuestion(data).then(() => {
        this.$modal.msgSuccess("上传入库成功");
      }).catch(() => {
        this.$message.error(response.data.msg);
      });
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.multipleSelection = selection.map(item => item.id);
      this.multipleSelectionData = selection;
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
      this.selectedRows = selection;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.multipleSelection.map(item => item.id);
      console.log(ids);
      this.$confirm('此操作将永久删除该文件，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancleButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.isDeleting = true;
        this.multipleSelection = [];
        this.$refs.fileTable.clearSelection();
        delHotQuestion(ids).then(() => {
          this.getList();
          this.$message.success("删除成功");
          this.multipleSelection = [];
        })
      }).catch(() => {
        row.isDeleting = false;
      });
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('/gpt/hotQuestion/export', {
        ...this.queryParams
      }, `hotQuestion_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style lang="scss" scoped>
.search-form {
  ::v-deep .el-form-item__label {
    min-width: 80px;
  }
}

.gray-color {
  .search-form-card {
    border-color: #828282;

    .search-form-header {
      .icon {
        border-color: #E0E0E0;
      }
    }

    .search-form-data {
      border-color: #E0E0E0;
    }
  }
}

.green-color {
  .search-form-card {
    border-color: #3FAD6E;

    .search-form-header {
      .icon {
        border-color: #94C9AA;
      }
    }

    .search-form-data {
      border-color: #94C9AA;
    }
  }
}

.orange-color {
  .search-form-card {
    border-color: #E7AC78;

    .search-form-header {
      .icon {
        border-color: #FEDDBF;
      }
    }

    .search-form-data {
      border-color: #FEDDBF;
    }
  }
}

.blue-color {
  .search-form-card {
    border-color: #2C9DDD;

    .search-form-header {
      .icon {
        border-color: #8FBED8;
      }
    }

    .search-form-data {
      border-color: #8FBED8;
    }
  }
}

.search-form-card {
  margin: 0 4px;
  padding: 6px 8px;
  border-radius: 10px;
  border: 1px solid #999093;
  display: flex;
  flex-direction: column;

  .search-form-header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .title {
      color: #333333;
      font-size: 16px;
    }

    .icon {
      font-size: 14px;
      border-radius: 20px;
      padding: 6px;
      border: 1px solid #999093;
      width: 30px;
      height: 30px;
      text-align: center;
    }
  }

  .search-form-data {
    border-radius: 10px;
    border: 1px solid #888a8e;
    display: flex;
    align-items: center;
    margin-top: 8px;
    padding-right: 8px;

    .search-form-data-select {
      margin-right: 4px;


      ::v-deep .el-input--medium .el-input__inner {
        height: 40px;
        border: none;
        width: 80px;
        padding: 0;
        text-align: center;
        color: #909399;
        border-radius: 10px;
      }

      ::v-deep .el-input__suffix {
        display: none;
      }

    }


    ::v-deep .el-input--medium .el-input__inner {
      border-radius: 0px;
      margin: 2px 0px;
      border-top: none;
      border-left: none;
      border-right: none;
    }
  }

}

::v-deep .el-table th.el-table__cell > .cell {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
