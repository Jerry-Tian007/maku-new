<template>
        <div style="margin-top: 10px;" id="searchDiv">
          <el-card style="border: none;box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.1);border-radius: 8px;margin: 30px;">
            <div slot="header" class="card-header" >
              <span style="font-weight: bold;">热点问题分析：</span>
            </div>
            <el-row :gutter="10">
              <el-col :span="18">
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
                  <el-form-item label="国家区域" prop="region">
                    <el-select
                      v-model="queryAnalyzeParams.region"
                      multiple
                      placeholder="选择国家区域"
                      required
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
<!--                    <treeselect style="width:75%" v-model="queryAnalyzeParams.region" :options="areaOptions" :show-count="true"-->
<!--                                multiple-->
<!--                                placeholder="请选择国家区域"/>-->
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
                  <el-form-item label="用户身份" prop="role">
                    <el-select
                      v-model="queryAnalyzeParams.role"
                      multiple
                      placeholder="选择用户身份"
                      required
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
                  <el-form-item label="准确性评价" prop="isAccuracy">
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
                  <el-form-item label="时间范围" prop="selectTime">
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
                      required
                      clearable
                    ></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
                  <el-form-item label="分析条数" prop="questionNum">
                    <el-input-number v-model="queryAnalyzeParams.questionNum"
                                     controls-position="right"
                                     @change="handleChange"
                                     :min="1"
                                     :max="7"
                                     placeholder="请输入1-7"
                                     style="width: 100%">
                    </el-input-number>
                  </el-form-item>
                </el-col>

              </el-form>

                <el-button type="primary" icon="el-icon-connection" size="mini" @click="handleAnalyze">分析</el-button>
                <el-button icon="el-icon-refresh-left" size="mini" @click="resetAnalyze">重设</el-button>
                <el-button icon="el-icon-refresh-right" size="mini" @click="handAnalyzeStatus">查询状态</el-button>
              </el-col>
              <el-col :span="6">
                <el-table :data="analyzeList" v-loading="loading"  >
                  <el-table-column label="更新时间" align="center" prop="trainTime" width="180px" show-overflow-tooltip>
                    <template slot-scope="scope">
                      <span>{{ (scope.row.trainTime) }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="状态" align="center" prop="status" show-overflow-tooltip>
                    <template slot-scope="scope" align="enter">
                      <div
                        style="position: absolute;left:0;right:0;top:0;bottom:0;margin: auto;width: fit-content;height: fit-content;padding: 6px 8px;">
                        <el-tag
                          v-if="scope.row.status"
                          style="font-size:14px;"
                          :type="scope.row.status === 'success' ? 'success'
                          : scope.row.status === 'analysising' ? 'warning'
                          : scope.row.status === 'failed' ? 'danger' : 'info'">
                          {{ staOptions.find(item => item.value === scope.row.status).label }}
                          <i class="el-icon-loading" style="font-size:16px;" v-if="scope.row.status === 'analysising'"/>
                        </el-tag>
                        <span v-else> - </span>
                        <div style="position: absolute;right:0;top:0;">
                          <el-tooltip v-if="scope.row.status === 'failed'" effect="light"
                                      :content="scope.row.trainFlow || '-'" placement="top">
                            <i class="el-icon-warning" style="font-size: 18px;color: #f3c27a"/>
                          </el-tooltip>
                        </div>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column label="备注" align="center" prop="trainFlow"  show-overflow-tooltip/>
                  <el-table-column label="提问" align="center" prop="question" show-overflow-tooltip v-if="false"/>
                  <el-table-column label="语言" align="center" prop="language"  show-overflow-tooltip v-if="false"/>
                </el-table>
              </el-col>
            </el-row>

          </el-card>

          <el-card style="border: none;box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.1);border-radius: 8px;margin: 30px;">
            <div slot="header" class="card-header">
              <span style="font-weight: bold;">热点问题查询：</span>
            </div>
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
                      @clear="handleQuery"
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
                      @clear="handleQuery"
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
                      @clear="handleQuery"
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
                      @clear="handleQuery"
                    ></el-input>
                  </el-form-item>
                </el-col>

                <el-col :span="8" :lg="8" :md="8" :sm="8" :xs="24">
                  <el-form-item label="用户身份" prop="role">
                    <el-select
                      v-model="queryParams.role"
                      multiple
                      placeholder="选择用户身份"
                      clearable
                      @clear="handleQuery"
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
                      @clear="handleQuery"
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
                  <el-form-item label="国家/区域" prop="region">
                    <el-select
                      v-model="queryParams.region"
                      multiple
                      placeholder="选择国家/区域"
                      clearable
                      @clear="handleQuery"
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
                      @clear="handleQuery"
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
                      @clear="handleQuery"
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
                  v-if="true"
                >新增</el-button>
                <el-button
                  type="success"
                  plain
                  icon="el-icon-edit"
                  size="mini"
                  :disabled="multiple"
                  @click="handleUpdateMul"
                  v-hasPermi="['gpt:hotQuestion:edit']"
                  v-if="true"
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
                <el-button icon="el-icon-upload" size="medium" @click="upload" >入库</el-button>

              </el-col>
              <el-col :span="2">
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar> </el-col>
            </el-row>

            <el-table v-loading="loading" :data="hotQuestionList"
                      ref="fileTable"
                      height="550"
                      @selection-change="handleSelectionChange"
                      :row-key="getRowKey"
                      style="border-top:solid #E4E7ED;">
              <el-table-column type="selection" width="55" align="center" :selectable="selectable"/>
              <el-table-column label="id" align="center" prop="id" show-overflow-tooltip />
              <el-table-column label="recordId" align="center" prop="recordId" show-overflow-tooltip />
              <el-table-column label="提问" align="center" prop="question" show-overflow-tooltip/>
              <el-table-column label="回答" align="center" prop="answer"  show-overflow-tooltip/>
              <el-table-column label="语言" align="center" prop="language"  show-overflow-tooltip/>
              <el-table-column label="创建时间" align="center" prop="createTime" width="180px"  value-format="yyyy-MM-dd HH:mm:ss">
                <template slot-scope="scope">
                  <span>{{ (scope.row.createTime) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="入库时间" align="center" prop="trainTime" width="180"  value-format="yyyy-MM-dd HH:mm:ss">
                <template slot-scope="scope">
                  <span>{{ (scope.row.trainTime) }}</span>
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
            <el-dialog :title="title"
                       :visible.sync="open"

                       width="500px" append-to-body>
              <el-form ref="form" :model="form"  label-width="100px">
                <el-form-item label="提问" prop="question" v-if="handleAdd">
                  <el-input v-model="form.question" placeholder="请输入提问" required/>
                </el-form-item>
                <el-form-item label="回答" prop="answer" v-if="handleAdd">
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
<!--                <el-form-item label="语言" prop="language" v-else-if="">-->
<!--                  <el-select-->
<!--                    v-model="form.language"-->
<!--                    placeholder="选择语言"-->
<!--                    clearable-->
<!--                    required-->
<!--                  >-->
<!--                    <el-option-->
<!--                      v-for="(item,index) in languageOptions"-->
<!--                      :key="index"-->
<!--                      :label="item.label"-->
<!--                      :value="item.value">-->
<!--                    </el-option>-->
<!--                  </el-select>-->
<!--                </el-form-item>-->
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
              </div>
            </el-dialog>
            <el-dialog :title="title"
                       :visible.sync="openedit"
                       width="500px" append-to-body>
              <el-form ref="form" :model="form"  label-width="100px">
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
                <!--                <el-form-item label="语言" prop="language" v-else-if="">-->
                <!--                  <el-select-->
                <!--                    v-model="form.language"-->
                <!--                    placeholder="选择语言"-->
                <!--                    clearable-->
                <!--                    required-->
                <!--                  >-->
                <!--                    <el-option-->
                <!--                      v-for="(item,index) in languageOptions"-->
                <!--                      :key="index"-->
                <!--                      :label="item.label"-->
                <!--                      :value="item.value">-->
                <!--                    </el-option>-->
                <!--                  </el-select>-->
                <!--                </el-form-item>-->
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitEditForm">确 定</el-button>
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
  trainQuestion, listMonitor
} from '@/api/data/hotQuestion'
import axios from 'axios'
import { getToken } from '@/utils/auth'
import { getRegion, listRegion } from '@/api/data/region';
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { delFile, Trained } from '@/api/data/uploadFile'
import { deptTreeSelect } from '@/api/system/role'

export default {
  name: "HotQuestion",
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
      showAnalyze:true,
      // 总条数
      total: 0,
      // 热点问题表格数据
      hotQuestionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openedit:false,

      selectedRows:[],

      baseurl:process.env. VUE_APP_HOT_URL,

      analyzeList: [],

      // 分析查询参数
      queryAnalyzeParams: {
        region: [],
        role: [],
        startTime: null,//开始时间
        endTime: null,//结束时间
        isAccuracy: null,//准确性
        questionNum:1,//条数
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
        trainTime:null,
      },
      selectTime:[],//分析时间
      uploadTime: [],//创建时间
      multipleSelection:[],//存储选中数据id
      multipleSelectionData:[],//存储选中数据
      //国家
      areaOptions: [],
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
      staOptions: [
        {
          label: '分析中',
          value: 'analysising'
        },
        {
          label: '分析成功',
          value: 'success'
        },
        {
          label: '分析失败',
          value: 'failed'
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
          { required: true, message: '请选择时间', trigger: 'change' }
        ],
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
      waitingTraining: null,

    };
  },
  created() {
    this.getList();
    this.getRegionOption();
    this.getReList();
  },
  mounted(){
    this.getDelay();
  },
  methods: {
    getDelay() {
      this.waitingTraining = setInterval(() => {
        this.getList();
        this.getReList();
      }, 60 * 1000)
    },
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
    //获取国家
    getRegionOption(){
      const queryParams={
        pageNum:this.queryParams.pageNum,
        pageSize:this.queryParams.pageSize
      }
      listRegion(queryParams).then(response=>{
        this.areaOptions = response.rows.map(item => ({
          label: item.region,
          value: item.region
        }));
      })
    },

    //分析状态
    getReList(){
      // this.loading=true;
      listMonitor().then(response=>{
        this.analyzeList = [response.data];
      })
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.openedit = false;

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
        language:null,
      };
      // this.resetForm("form");
      this.handleQuery();
    },
    handAnalyzeStatus(){
      this.getReList();
    },
    /** 分析操作*/
    handleAnalyze() {
      this.$refs["queryAnalyzeParams"].validate(valid =>{
        if (valid) {
          const queryAnalyzeParams = JSON.parse(JSON.stringify(this.queryAnalyzeParams));
          if(queryAnalyzeParams.role){ queryAnalyzeParams.role = this.queryAnalyzeParams.role.join(";");}
          if(queryAnalyzeParams.region){queryAnalyzeParams.region = this.queryAnalyzeParams.region.join(";");}
          this.$message.info('开始分析，请稍候...');
          analyzeHotQuestion(queryAnalyzeParams).then(response => {
            this.hotQuestionList = response.rows;
            this.total = response.total;
            this.loading = false;
            this.$message.success(response.msg);
            this.getReList();
          }).catch(error => {
            // this.$message.error(response.data.msg);
          });
        } else {
          this.$message.error('请填写必要的数据');
        }
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
    handleUpdateMul(row) {
      this.openedit = true;
      if (this.multipleSelectionData.length === 1) {
        const selectedRow = this.multipleSelectionData[0]; // 获取多选中的第一个行数据
        this.form = {
          id: selectedRow.id,
          recordId: selectedRow.recordId,
          region: selectedRow.region.split(';'), // 将字符串转换为数组
          role: selectedRow.role.split(';') // 将字符串转换为数组
        };
      } else {
        const selectedIds = this.multipleSelectionData.map(item => item.id);
        const recordIds = this.multipleSelectionData.map(item => item.recordId);
        this.form = {
          id: selectedIds,
          recordId: recordIds,
          region: [], // 清空国家/区域字段的值
          role: [] // 清空用户身份字段的值
        };
      }

      // if (this.selectedRows.length === 1) {
      //   //
      //   const selectedRow = this.selectedRows[0];
      //   const region = selectedRow.region.split(';');
      //   const role = selectedRow.role.split(';');
      //   // 设置默认值
      //   this.form = {
      //     region,
      //     role
      //   };
      //
      // } else {
      //   // 多条设置默认值为空
      //   this.form = {
      //     region: [],
      //     role: [],
      //   };
      // }
      console.log(this.form)
      console.log(this.multipleSelectionData)
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
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
        this.openedit = true;
        this.title = "修改热点问题";
      });
    },

    submitEditForm(){
      this.form.region = this.form.region.join(';');
      this.form.role = this.form.role.join(';');

      updateHotQuestion(this.form).then(response => {
        this.$modal.msgSuccess("修改成功");
        this.openedit = false;
        this.getList();
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.form.region = this.form.region.join(';');
      this.form.role = this.form.role.join(';');
      addHotQuestion(this.form).then(response => {
        this.$modal.msgSuccess("新增成功");
        this.open = false;
        this.getList();
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
    getRowKey(row) {
      return row.id;
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
      const data={
        role : this.queryParams.role.join(";"),
        region : this.queryParams.region.join(";"),
      }
      this.download('/gpt/hotQuestion/export', {
        ...this.queryParams,
        ...data,
      }, `hotQuestion_${new Date().getTime()}.xlsx`, () => {
        // this.reset();
      });
    }
  },
  destroyed() {
    clearInterval(this.waitingTraining);
    this.waitingTraining = null;
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
