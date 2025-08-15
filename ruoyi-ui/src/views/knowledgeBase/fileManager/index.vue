<template>
  <div class="app-container">
    <el-card>
      <div class="flex-layout align-center">
        <div>
          <el-row :gutter="20">
            <el-form class="gray-label" :model="queryParams" ref="queryForm" size="small" label-width="68px">
              <el-col style="height: 50px;" :sm="12" :xs="24">
                <el-form-item :label="$t('fileManager.fileName')" label-width="auto" style="display: flex"
                              prop="fileName">
                  <el-input style="width: 300px" v-model="queryParams.fileName"
                            :placeholder="$t('fileManager.fileNamePlaceholder')" clearable
                            @keyup.enter.native="getList"/>
                </el-form-item>
              </el-col>
              <el-col style="height: 50px" :sm="12" :xs="24">
                <el-form-item :label="$t('fileManager.fileSuffix')" label-width="auto" prop="suffix"
                              style="display: flex">
                  <el-select style="width: 300px" v-model="queryParams.suffix"
                             :placeholder="$t('fileManager.fileSuffixPlaceholder')" clearable>
                    <el-option v-for="item in fileTypes" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col style="height: 50px" :sm="12" :xs="24">
                <el-form-item :label="$t('fileManager.uploadTime')" label-width="auto" style="display: flex"
                              prop="createTime" class="no-bottom">
                  <el-date-picker style="width: 100%;" v-model="createTime" type="datetimerange"
                                  :range-separator="$t('or')" :start-placeholder="$t('startTime')"
                                  :end-placeholder="$t('endTime')"
                                  value-format="yyyy-MM-dd HH:mm:ss" :default-time="['00:00:00', '23:59:59']" clearable
                                  @change="(val) => handleChangeTime(['startTime', 'endTime'], val)"/>
                </el-form-item>
              </el-col>
              <el-col style="height: 50px;" :sm="12" :xs="24">
                <el-form-item :label="$t('fileManager.trainTime')" label-width="auto" style="display: flex"
                              prop="trainTime" class="no-bottom">
                  <el-date-picker style="width: 100%;" v-model="trainTime" type="datetimerange"
                                  :range-separator="$t('or')" :start-placeholder="$t('startTime')"
                                  :end-placeholder="$t('endTime')"
                                  :default-time="['00:00:00', '23:59:59']" value-format="yyyy-MM-dd HH:mm:ss" clearable
                                  @change="(val) => handleChangeTime(['trainStartTime', 'trainEndTime'], val)"/>
                </el-form-item>
              </el-col>
              <el-col style="height: 50px" :sm="12" :xs="24">
                <el-form-item :label="$t('fileManager.trainingState')" label-width="auto" style="display: flex"
                              prop="trainStatus" class="no-bottom">
                  <el-select style="width: 300px" v-model="queryParams.trainStatus"
                             :placeholder="$t('fileManager.trainingMsg')" clearable>
                    <el-option v-for="item in trainStatusOptions" :key="item.value" :label="item.label"
                               :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col style="height: 50px;" :sm="12" :xs="24">
                <el-form-item :label="$t('fileManager.kbSelect')" label-width="auto" style="display: flex"
                              prop="kbSelect" class="no-bottom">
                  <el-select style="width: 300px" v-model="queryParams.kbName"
                             :placeholder="$t('fileManager.kbSelectMsg')" clearable>
                    <el-option v-for="item in kbAllList" :key="item.id" :label="item.kbName" :value="item.kbName">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <!-- 新增level过滤条件 -->
              <el-col style="height: 50px" :sm="12" :xs="24">
                <el-form-item :label="$t('fileManager.bookLevel')" label-width="auto" style="display: flex"
                              prop="level" class="no-bottom">
                  <el-select style="width: 300px"
                             v-model="queryParams.bookLevel"
                             :placeholder="$t('fileManager.levelPlaceholder')"
                             :default-first-option="true"
                             filterable
                             clearable>
                    <el-option :value="0" :label="$t('fileManager.emptyOption')" class="empty-option" />
                    <el-option v-for="level in 10" :key="level" :label="level" :value="level" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col style="height: 50px;">
                <el-button size="small" type="primary" class="blur-green" icon="el-icon-refresh" @click="resetQuery">
                  {{ $t('rest') }}
                </el-button>
                <el-button size="small" type="primary" icon="el-icon-search" @click="handleQuery">{{
                    $t('query')
                  }}
                </el-button>
                <el-button size="small" class="blue" icon="el-icon-refresh-right" @click="getList">
                  {{ $t('refresh') }}
                </el-button>
                <el-button size="small" class="green" icon="el-icon-download" @click="handleExport">
                  {{ $t('export') }}
                </el-button>
                <el-button size="small" class="purple" icon="el-icon-finished"
                           :disabled="!fileSelection.length"
                           @click="handleSwitchStatus('enable')">
                  {{ $t('systemManager.enable') }}
                </el-button>
                <el-button size="small" class="red" icon="el-icon-switch-button"
                           :disabled="!fileSelection.length"
                           @click="handleSwitchStatus('unable')">
                  {{ $t('systemManager.disable') }}
                </el-button>
              </el-col>
              <el-col style="height: 50px;">
                <el-button size="small" class="blue" icon="el-icon-download"
                           :disabled="!fileSelection.length"
                           @click="handleBatchDownload" v-loading="batchDownloadLoading">
                  {{ $t('fileManager.batchDownload') }}
                </el-button>
                <el-button size="small" class="blue" icon="el-icon-download"
                           :disabled="!fileSelection.length"
                           @click="handleBatchDownloadMd()" v-loading="batchDownloadMdLoading">
                  {{ $t('fileManager.batchDownloadMd') }}
                </el-button>
                <el-button size="small" class="blue" icon="el-icon-download"
                           :disabled="!fileSelection.length"
                           @click="handleBatchDownloadYaml()" v-loading="batchDownloadYamlLoading">
                  {{ $t('fileManager.batchDownloadYaml') }}
                </el-button>
                <el-button size="small" class="blue" icon="el-icon-download"
                           :disabled="!fileSelection.length"
                           @click="handleBatchDownloadImage()" v-loading="batchDownloadImageLoading">
                  {{ $t('fileManager.batchDownloadImage') }}
                </el-button>
                <el-button size="small" class="blue" icon="el-icon-download"
                           :disabled="!fileSelection.length"
                           @click="handleFullBatchDownload()" v-loading="fullBatchDownloadLoading">
                  {{ $t('fileManager.fullBatchDownload') }}
                </el-button>
              </el-col>
            </el-form>
          </el-row>
        </div>
        <div class="flex-layout align-center gap10">

          <div class="big-button blue back-blue" @click="handleUpload()">
            <span>{{ $t('fileManager.fileUpload') }}</span>
          </div>

          <div class="big-button green back-green" @click="handleTrain" v-loading.fullscreen.lock="fullscreenLoading">
            <span>{{ $t('fileManager.startTrain') }}</span>
          </div>

          <div class="big-button green update-green" @click="handleBatchEdit"
               v-loading.fullscreen.lock="updateFileTrainLoading">
            <span>{{ $t('fileManager.startUpdate') }}</span>
          </div>
        </div>
      </div>
    </el-card>

    <el-card class="mt20">
      <el-table :data="fileManagerList" style="width: 100%" :row-style="{ height: '50px' }" class="color-table"
                :row-key="getRowKey" v-loading="loading" ref="fileTable" @selection-change="handleSelectionChange">
        <template slot="empty">
          <Empty/>
        </template>
        <el-table-column type="selection" :selectable="selectable" width="55" :reserve-selection="true">
        </el-table-column>
        <el-table-column prop="originalFileName" :label="$t('fileManager.fileName')" min-width="200">
          <template slot-scope="scope">
<!--             <el-button v-if="scope.row.jsonCosUrl" @click="handleShowJson(scope.row.jsonCosUrl)" type="text">-->
<!--              {{scope.row.originalFileName}}-->
<!--            </el-button>-->
<!--            <span v-else>-->
<!--              {{scope.row.originalFileName}}-->
<!--            </span>-->
            <span>
              {{scope.row.originalFileName}}
            </span>

          </template>
        </el-table-column>
        <el-table-column prop="suffix" :label="$t('fileManager.fileSuffix')" min-width="100">
        </el-table-column>
        <el-table-column prop="trainStatus" :label="$t('fileManager.trainState')" min-width="150">
          <template slot-scope="scope">
            <el-tag :type="trainStatusOptions.find(item => item.value === scope.row.trainStatus).type"
                    :class="{ 'purple': scope.row.trainStatus === 'deleting' }">
              {{ trainStatusOptions.find(item => item.value === scope.row.trainStatus).label }}
            </el-tag>
            <template v-if="scope.row.trainStatus === 'waiting'">
              <span @click="handleCancelTrain(scope.row)" class="cancel-button" :title="$t('fileManager.cancelTrain')">
                <i class="el-icon-error"/>
              </span>
            </template>
            <template v-else-if="errorStatus.includes(scope.row.trainStatus) && scope.row.trainErrorReason">
              <el-tooltip placement="top" effect="light" :content="scope.row.trainErrorReason">
                <i class="el-icon-info text-yellow" style="margin-left: 4px;cursor: pointer;"/>
              </el-tooltip>
            </template>
          </template>
        </el-table-column>
        <el-table-column prop="kbName" :label="$t('fileManager.belongKb')" min-width="150">
          <template slot-scope="scope">
            <el-popover placement="right" width="150" trigger="hover"
                        v-if="scope.row.kbName && scope.row.kbName.split(',').length > 1">
              <div class="flex-layout column" style="max-height: 200px;overflow-y: auto;">
                <k-b-radio v-for="(item, index) in scope.row.kbName ? scope.row.kbName.split(',') : []" :key="index"
                           :value="item" style="padding: 10px;"/>
              </div>

              <div style="width: fit-content;" slot="reference">
                <el-button type="text">
                  <span class="main-text">{{
                      $t('fileManager.common') + `${scope.row.kbName ? scope.row.kbName.split(',').length : 0}` +
                      $t('fileManager.individual')
                    }}</span>
                  <i class="el-icon-view" style="margin-left: 4px;"/>
                </el-button>
              </div>
            </el-popover>
            <span v-else>
              {{ scope.row.kbName }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="$t('fileManager.status')" min-width="130">
          <template slot-scope="scope">
            <span v-if="scope.row.status === 'enable'">
              {{ $t('systemManager.enable') }}
            </span>
            <span v-else-if="scope.row.status === 'unable'" style="color: #F56C6C;">
              {{ $t('systemManager.disable') }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" :label="$t('fileManager.tableFileSize')" min-width="130">
          <template slot-scope="scope" v-if="scope.row.fileSize">
            {{ scope.row.fileSize + ' ' + 'MB' }}
          </template>
        </el-table-column>
        <!-- 新增level列 -->
        <el-table-column prop="bookLevel" :label="$t('fileManager.bookLevel')" min-width="100" align="center">
          <template slot-scope="scope">
            <el-select
              ref="levelSelect"
              v-model="scope.row.bookLevel"
              :placeholder="$t('fileManager.levelPlaceholder')"
              size="small"
              :clearable="false"
              filterable
              :popper-class="'elegant-select-dropdown'"

              @change="handleLevelChange(scope.row, $event)"
              @visible-change="(visible) => !visible && $refs.levelSelect && $refs.levelSelect.blur()">
              <el-option :value="0" :label="$t('fileManager.emptyOption')" class="empty-option" />
              <el-option v-for="level in 10" :key="level" :label="level" :value="level" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="filePage" :label="$t('fileManager.filePage')" min-width="80">
        </el-table-column>
        <el-table-column prop="fileToken" :label="$t('fileManager.fileTokens')" min-width="120">
        </el-table-column>
        <el-table-column prop="trainDuration" :label="$t('fileManager.trainDuration')" min-width="130">
          <template slot-scope="scope" v-if="scope.row.trainDuration">
            {{ scope.row.trainDuration + ' ' + $t('fileManager.unit') }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('fileManager.uploadTime')" min-width="200">
        </el-table-column>
        <el-table-column prop="trainTime" :label="$t('fileManager.trainTime')" min-width="200">
        </el-table-column>
        <el-table-column prop="trainFinishTime" :label="$t('fileManager.trainFinishTime')" min-width="200">
        </el-table-column>
        <el-table-column fixed="right" :label="$t('operation')" align="center" width="160">
          <template slot-scope="scope">
            <div class="grid-layout row1 col3 row-g4">
              <span>
                <el-button :title="$t('edit')" class="purple" size="mini" icon="el-icon-edit"
                           :disabled="cantTrainStatus.includes(scope.row.trainStatus)"
                           @click="handleEdit(scope.row)"></el-button>
              </span>
              <span>
                <el-button :title="$t('download')" class="blue" size="mini" icon="el-icon-download"
                           :disabled="scope.row.trainStatus === 'deleting'"
                           @click="handleShowDownload(scope.row)"></el-button>
              </span>
              <span>
                <el-button :title="$t('deleted')" class="red" size="mini" icon="el-icon-delete"
                           :disabled="cantTrainStatus.includes(scope.row.trainStatus)"
                           @click="handleDelete(scope.row)"></el-button>
              </span>
<!--              <span style="grid-column: 1 / -1;padding: 0 2px;">
                <el-button style="width: 100%;" :title="'查看切片'" class="green" size="mini"
                           @click="handleShowJson(scope.row.jsonCosUrl)"
                           icon="el-icon-connection">{{ '查看切片' }}</el-button>
              </span>-->
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div style="width: 100%;margin-top: 10px;" class="flex-layout align-center start-end">
        <el-switch v-model="needRefresh" :active-text="$t('fileManager.autoRefresh')" style="opacity: 0.8;"/>

        <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                       :current-page="queryParams.pageNum" :page-sizes="[100, 200, 500, 1000]"
                       :page-size="queryParams.pageSize"
                       :total="total" layout="total, sizes, prev, pager, next, jumper"/>
      </div>
    </el-card>
    <div class="dialog-container">
      <el-dialog :visible.sync="uploadVal" :close-on-click-modal="false">
        <div style="width: 100%;" align="center">
          <span class="dialog-title">{{ $t('fileManager.fileUpload') }}</span>
        </div>
        <div class="dialogBody" style="margin-top: 16px;">
          <div class="upload-config-box" style="gap: 12px;padding: 16px 20px;">
            <div style="display: flex;align-items: center;gap: 6px;">
              <svg-icon icon-class="bell" class="fs-20"/>
              <span style="font-size: 14px;font-weight: 600;color: #2C2C2C;">
                {{ $t('fileManager.fileRequire') }}
              </span>
            </div>
            <el-divider direction="vertical" style="height: 2em;color: rgba(0,0,0,0.6);"></el-divider>
            <div style="flex:1;">
              <el-row>
                <el-col>
                  <span>{{ $t('fileManager.fileUploadText') }}</span>
                </el-col>
                <el-col>
                  <span>{{ $t('fileManager.fileUploadText2') }}</span>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="chooseFile" style="margin-top: 15px;display: flex;justify-content:space-between;">
            <div style="font-weight: 560;font-size: 14px;color: #2C2C2C;margin-top: 6px;">
              {{ $t('fileManager.switchFile') }}
            </div>
            <div style="">
              <el-checkbox v-model="isOverwrite" style="margin-right: 15px;">{{
                  $t('fileManager.duplicateCoverage')
                }}
              </el-checkbox>
              <el-button type="primary" icon="el-icon-plus" @click="handleClickUpload"
                         :class="{ 'tool-bar-item': true, 'disabled': fileList.length >= maxFile }"
                         :title="$t('fileManager.attachment')">{{ $t('fileManager.selectFile') }}
              </el-button>
            </div>
          </div>
          <el-card :class="{ 'file-card': true, 'hide': fileList.length < 1 }"
                   :body-style="{ display: 'flex', alignItems: 'center', overflowX: 'auto', gap: '8px' }"
                   style="margin-top:10px;">
            <file-content v-for="(file, index) in fileList" :key="index" :file="file" :url="file.url" :type="file.type"
                          :status="file.status" @close="handleClose"/>
          </el-card>

          <div style="font-weight: 560;font-size: 14px;color: #2C2C2C;margin-top: 20px;">
            {{ $t('fileManager.associationKb') }}
          </div>


          <k-b-select style="margin-top: 10px;height: 36vh;" v-model="kbList"/>

        </div>

        <div slot="footer" class="dialog-footer" align="center">
          <el-button class="secondary" @click="uploadVal = false;" style="width: 120px;">{{ $t('cancel') }}</el-button>
          <el-button type="primary" @click="handleSubmitUpload" style="width: 120px;"
                     :disabled="kbList.length < 1 || fileList.length < 1">
            <i class="el-icon-upload"/><span style="margin-left: 4px;">{{ $t('fileManager.fileUpload') }}</span>
          </el-button>
        </div>
      </el-dialog>

      <el-upload class="upload-demo" ref="upload" :auto-upload="false" action="" :http-request="handleUploadFiles"
                 :on-preview="handlePreview" :on-change="handleChange" :on-remove="handleRemove"
                 :on-exceed="handleExceed"
                 :show-file-list="false" :limit="maxFile" :accept="fileTypeString" multiple>
        <div ref="uploadButton" style="margin-left: 10px;" size="small" type="success">
        </div>
      </el-upload>
    </div>

    <el-dialog :visible.sync="showKB" width="50%" append-to-body>
      <div style="width: 100%;" align="center">
        <span class="dialog-title">{{ $t('fileManager.belongKb') }}</span>
      </div>
      <k-b-select v-model="kbList" style="height: 50vh;margin-top: 20px;"/>

      <div slot="footer" class="dialog-footer" align="center">
        <el-button class="secondary" @click="showKB = false;">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitFileKB">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>


    <el-dialog :visible.sync="showChunk" width="50%" append-to-body>
      <chunk-list :chunks="chunks"/>
    </el-dialog>

    <el-dialog
      :visible.sync="showDownloadDialog"
      :title="$t('warding')"
      :close-on-click-modal="false"
      width="20%"
    >
      <div style="font-size: medium">{{ $t('fileManager.selectDownLoadType') }}</div>
      <span slot="footer" class="dialog-footer">
      <el-button @click="handleDownload('md')">{{ $t('fileManager.mdType') }}</el-button>
      <el-button type="primary" @click="handleDownload('pdf')">{{ $t('fileManager.pdfType') }}</el-button>
      <el-button type="primary" @click="handleDownloadImage()">{{ $t('fileManager.imageType') }}</el-button>
      <el-button type="primary" @click="handleDownloadYaml()">{{ $t('fileManager.yamlType') }}</el-button>

    </span>
    </el-dialog>
  </div>
</template>
<script>
import {
  cancelTraining,
  editFile,
  editLevel,
  getFileInfo,
  listFile,
  removeFile,
  trainFile,
  uploadFile,
  updateFileTrain, batchEditStatus
} from "@/api/knowledgeBase/fileManager";
import FileContent from "@/components/FileConntent/index.vue";
import {addStartDateRange, parseTime} from "@/utils/ruoyi";
import KBRadio from "@/components/KBRadio/index.vue";
import {listKB} from "@/api/knowledgeBase/kbManager";
import KBSelect from "@/components/KBSelect/index.vue";
import axios from "axios";
import {jsonDownload} from "@/utils/request";
import ChunkList from "@/views/knowledgeBase/fileManager/components/chunkList.vue";
import JSZip from 'jszip'
import { saveAs } from 'file-saver'

export default {
  name: "FileManager",
  components: {ChunkList, KBSelect, KBRadio, FileContent},
  data() {
    return {
      // 遮罩层
      loading: true,
      fullscreenLoading: false,
      batchDownloadLoading: false,
      batchDownloadMdLoading: false,
      batchDownloadYamlLoading: false,
      batchDownloadImageLoading: false,
      fullBatchDownloadLoading: false,

      showDownloadDialog: false,
      fileId: undefined,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      uploadVal: false,
      // 总条数
      total: 0,
      // 文件管理表格数据
      fileManagerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        "fileName": null, //文档名称
        "suffix": null, //文档格式
        "startTime": null, //上传时间范围 开始时间点
        "endTime": null, //上传时间范围 结束时间点
        "trainStartTime": null, //训练时间范围 开始
        "trainEndTime": null, //训练时间范围 结束
        "kbIds": null,
        "trainStatus": null,
        "kbSelect": null,
        pageNum: 1,
        pageSize: 200,
      },
      pageParams: {
        pageNum: 1,
        pageSize: 200,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},

      trainStatusOptions: [
        {label: this.$t('fileManager.unTrained'), value: 'untrained', type: 'primary'},
        {label: this.$t('fileManager.training'), value: 'training', type: 'warning'},
        {label: this.$t('fileManager.trained'), value: 'success', type: 'success'},
        {label: this.$t('fileManager.trainingFailed'), value: 'error', type: 'danger'},
        //排队中
        {label: this.$t('fileManager.line'), value: 'waiting', type: 'info'},
        {label: this.$t('fileManager.cancelProgress'), value: 'canceling', type: 'info'},
        {label: this.$t('fileManager.cancelFailed'), value: 'cancelFailed', type: 'danger'},

        {label: this.$t('fileManager.deletingProgress'), value: 'deleting', type: 'info'},
        {label: this.$t('deleteFailed'), value: 'deleteFailed', type: 'danger'},
      ],
      // 文件格式
      fileTypes: [
        {label: "PDF", value: "pdf"},
        {label: "Docx", value: "docx"},
        {label: "Doc", value: "doc"},
        {label: "Xlsx", value: "xlsx"},
        {label: "Xls", value: "xls"},
        {label: "MD", value: "md"},
      ],
      fileTypeString: ".pdf, .docx, .doc, .xlsx, .xls, .md",
      fileUrl: '',
      fileList: [],
      updateFileTrainLoading: false,
      maxFile: 1000 * 1000 * 1000,

      fileSelection: [],

      isOverwrite: false,

      createTime: [],
      trainTime: [],

      kbList: [],
      fileInfo: {},

      showKB: false,

      timer: null,

      needRefresh: true,

      cantTrainStatus: [
        'training',
        'waiting',
        'deleting',
        'canceling'
      ],
      errorStatus: [
        'error',
        'deleteFailed',
        'cancelFailed'
      ],
      kbAllList: [],
      showKBType: '',

      showChunk: false,
      chunks: [],
    };
  },
  created() {
    this.getList();
  },
  watch: {
    '$route.query': {
      handler(val) {
        if (val && val.kbName) {
          this.queryParams.kbName = val.kbName;
        }
      },
      immediate: true
    },
  },
  mounted() {

    this.timer = setInterval(() => {
      if (this.needRefresh) {
        this.getList(true, true); // 自动刷新时保持选择状态
      }
    }, 30 * 1000);
    this.getKBList();
  },
  methods: {
    handleOpenFile(row) {
      switch (row.suffix) {
        case 'pdf':

          break;
        case 'md':

          break;
      }
    },
    getRowKey(row) {
      return row.id;
    },
    selectable(row, index) {
      return !this.cantTrainStatus.includes(row.trainStatus);
    },
    handleCancelTrain(row) {
      this.$confirm(this.$t('fileManager.cancelTrainingFile'), this.$t('warding'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.fullscreenLoading = true;
        cancelTraining(row.id).then(res => {
          if (res.code === 200) {
            this.$message.success(this.$t('fileManager.cancelSuccess'));
            this.getList();
          } else {
            console.log(res.msg);
            this.$message.error(res.msg);
          }
          this.fullscreenLoading = false;
        }).catch(err => {
          console.log(err);
          this.$message.error(this.$t('fileManager.cancelFailed'));
          this.fullscreenLoading = false;
        });
      }).catch(() => {
      });
    },
    handleTrain() {
      if (this.fileSelection.length < 1) {
        this.$message.warning(this.$t('fileManager.selectFileTrained'));
        return;
      }
      this.fullscreenLoading = true;
      trainFile(this.fileSelection.map(item => item.id)).then(res => {
        if (res.code === 200) {
          this.$message.success(this.$t('fileManager.trainingBegins'));
          this.handleClearSelection();
        } else {
          console.log(res.msg);
          this.$message.error(res.msg);
        }
        this.fullscreenLoading = false;
        this.getList();
      }).catch(err => {
        console.log(err);
        this.$message.error(this.$t('fileManager.trainingFailedStart'));
        this.fullscreenLoading = false;
      });
    },
    getKBList() {
      listKB({pageNum: 1, pageSize: 100}, {}).then(response => {
          this.kbAllList = response.rows;
        }
      );
    },
    /** 查询文件管理列表 */
    getList(refresh, keepSelection = false) {
      this.loading = true;
      if (!keepSelection) {
        this.handleClearSelection(); // 仅在非保持选择状态时清理
      }
      let data = {...this.queryParams};
      if (refresh) {
        data = {...this.queryParams, timestamp: new Date().getTime()};
      }
      listFile(this.pageParams, data).then(response => {
        this.fileManagerList = response.rows;

        this.total = response.total;
        this.loading = false;
      });
    },
    handleQuery() {
      // this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.queryParams = {
        "fileName": "", //文档名称
        "suffix": "", //文档格式
        "startTime": "", //上传时间范围 开始时间点
        "endTime": "", //上传时间范围 结束时间点
        "trainStartTime": "", //训练时间范围 开始
        "trainEndTime": "", //训练时间范围 结束
        "kbIds": [],
        "trainStatus": null,
        "kbSelect": null,
        "pageNum": 1,
        "pageSize": 200,
      };
      this.pageParams = {
        pageNum: 1,
        pageSize: 200,
      };
      this.createTime = [];
      this.trainTime = [];
      this.getList();
    },
    handleSizeChange(val) {
      this.pageParams.pageSize = val;
      this.handleClearSelection(); // 分页大小变化时清理选择
      this.getList();
    },
    handleCurrentChange(val) {
      this.pageParams.pageNum = val;
      this.handleClearSelection(); // 页码变化时清理选择
      this.getList();
    },
    handleChangeTime(keys, val) {
      this.queryParams = addStartDateRange(this.queryParams, val, keys);
      console.log(this.queryParams);
    },
    handleSelectionChange(val) {
      this.fileSelection = val;
    },
    handleClearSelection() {
      this.fileSelection = [];
      if (this.$refs.fileTable) {
        this.$refs.fileTable.clearSelection();
      }
    },
    handleUpload() {
      this.fileList = [];
      this.$refs.upload.clearFiles();

      this.kbList = [];
      this.searchChosenKB = this.searchAllKB = null;

      this.isOverwrite = false;
      this.uploadVal = true;

    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 重置按钮
    reset() {
      this.form = {};
    },
    handleClose(file) {
      this.fileList = this.fileList.filter(item => item !== file);
      const {uploadFiles} = this.$refs.upload;
      if (uploadFiles && uploadFiles.length) {
        let index = uploadFiles.findIndex(item => item.name === file.fileName);
        uploadFiles.splice(index, 1);
      }
    },
    handleClickUpload() {
      if (this.fileList.length >= this.maxFile) return;
      // this.$refs.upload.clearFiles();
      this.$refs.uploadButton.click();
    },
    handleFileSuccess(file) {
      this.fileList = this.fileList.map(item => {
        if (item.fileName === file.fileName) {
          item.status = 'success';
        }
        return item;
      });
    },
    handleFileError(file) {
      this.fileList = this.fileList.map(item => {
        if (item.fileName === file.fileName) {
          item.status = 'error';
        }
        return item;
      });
    },
    handleChange(file, fileList) {
      if (file.status === 'ready') {
        console.log(this.fileList.length, fileList.length)
        if (fileList.length > this.maxFile) {
          this.$message.warning(this.$t('fileManager.currentRestrictionSelection') + ` ${this.maxFile} ` + this.$t('fileManager.aFile') + '，' + this.$t('fileManager.chosenThisTime') + ` ${fileList.length}` + this.$t('fileManager.aFile') + '，' + this.$t('fileManager.selectedTogether') + `${fileList.length + this.fileList.length} ` + this.$t('fileManager.aFile'));
          return;
        }
        // if (file.raw.size > 150 * 1024 * 1024) {
        //   this.$message.warning(this.$t('fileManager.fileSize'));
        //   return;
        // }
        this.fileList.push({
          file: file.raw,
          fileName: file.name,
          type: file.name.split('.').pop(),
          status: 'success'
        });
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning(this.$t('fileManager.currentRestrictionSelection') + ` ${this.maxFile} ` + this.$t('fileManager.aFile') + '，' + this.$t('fileManager.chosenThisTime') + ` ${files.length}` + this.$t('fileManager.aFile') + '，' + this.$t('fileManager.selectedTogether') + `${files.length + fileList.length}  ` + this.$t('fileManager.aFile'));
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    // 点击上传文件按钮，上传前的操作
    handleSubmitUpload() {
      this.fullscreenLoading = true;

      let finishCount = 0;
      if (this.isOverwrite) {
        this.fileList = this.fileList.filter((item, index, self) =>
          index === self.findIndex(i => i.fileName === item.fileName)
        );
      }
      let totalCount = this.fileList.length;
      this.fileList.forEach(file => {
        let query = {
          "updateStatus": this.isOverwrite,
          "kbIds": this.kbList.join(','),
        }

        let formData = new FormData();
        formData.append('file', file.file);

        uploadFile(query, formData).then(res => {
          if (res.code === 200) {
            this.$message.success(this.$t('fileManager.uploadSuccess'));
          } else {
            console.log(res.msg);
            this.$message.error(res.msg);
          }
          finishCount++;
          if (finishCount === totalCount) {
            this.uploadVal = false;
            this.fullscreenLoading = false;
            this.getList();
          }
        }).catch(err => {
          console.log(err);
          this.$message.error(this.$t('fileManager.uploadError'));
          finishCount++;
          if (finishCount === totalCount) {
            this.uploadVal = false;
            this.fullscreenLoading = false;
            this.getList();
          }
        });
      });
    },
    //文件上传
    uploadFiles(file) {
    },
    handleUploadFiles(file) {
      console.log(file);
    },
    handleDelete(row) {
      this.$confirm(this.$t('fileManager.wantDelete'), this.$t('warding'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.fullscreenLoading = true;
        removeFile(row.id).then(res => {
          if (res.code === 200) {
            this.$message.success(this.$t('deletedSuccess'));
            this.getList();
          } else {
            console.log(res.msg);
            this.$message.error(res.msg);
          }
          this.fullscreenLoading = false;
        }).catch(err => {
          console.log(err);
          this.$message.error(this.$t('deletedSuccess'));
          this.fullscreenLoading = false;
        });
      }).catch(() => {
      });
    },
    handleBatchEdit() {
      if (this.fileSelection.length < 1) {
        this.$message.warning(this.$t('fileManager.selectFileUpdateTrained'));
        return;
      }
      this.showKBType = 'all';
      this.showKB = true;
    },
    handleEdit(row) {
      this.showKBType = 'one';
      this.fullscreenLoading = true;
      getFileInfo(row.id).then(res => {
        if (res.code === 200) {
          this.fileInfo = res.data;
          this.kbList = this.fileInfo.kbId ? this.fileInfo.kbId.split(',') : [];
          this.showKB = true;
        }
        this.fullscreenLoading = false;
      }).catch(err => {
        console.log(err);
        this.$message.error(err);
        this.fullscreenLoading = false;
      });
    },
    submitFileKB() {
      if (this.kbList.length < 1) {
        this.$message.warning(this.$t('fileManager.selectLeastKb'));
        return;
      }
      if (this.showKBType === 'all') {
        let data = {
          "fileIds": this.fileSelection.map(item => item.id),
          "kbIds": this.kbList
        };
        this.updateFileTrainLoading = true;
        updateFileTrain(data).then(res => {
          if (res.code === 200) {
            this.$message.success(this.$t('fileManager.associationUpdateSuccess'));
            this.showKB = false;
            this.getList();
          } else {
            console.log(res.msg);
            this.$message.error(res.msg);
          }
          this.updateFileTrainLoading = false;
          this.showKB = false;
        }).catch(err => {
          console.log(err);
          this.$message.error(this.$t('fileManager.associationUpdateError'));
          this.updateFileTrainLoading = false;
        });
      } else {
        let data = {
          "fileId": this.fileInfo.id, //文件id
          "kbIds": this.kbList //知识库ids
        };
        this.fullscreenLoading = true;
        editFile(data).then(res => {
          if (res.code === 200) {
            this.$message.success(this.$t('fileManager.associationUpdateSuccess'));
            this.showKB = false;
            this.getList();
          } else {
            console.log(res.msg);
            this.$message.error(res.msg);
          }
          this.fullscreenLoading = false;
          this.showKB = false;
        }).catch(err => {
          console.log(err);
          this.$message.error(this.$t('fileManager.associationUpdateError'));
          this.fullscreenLoading = false;
        });
      }
    },
    handleShowDownload(row) {
      this.showDownloadDialog = true;
      this.fileId = row.id;
    },
    handleDownload(type) {
      this.showDownloadDialog = false;
      if (type === 'pdf') {
        this.fullscreenLoading = true;
        getFileInfo(this.fileId).then(response => {
          if (response.code === 200) {
            let row = response.data;
            let url = row.downloadUrl.replace(process.env.VUE_APP_FILE_MANAGER_DOWNLOAD_URL, process.env.VUE_APP_FILE_URLS);
            axios.get(url, {
              responseType: 'blob'
            }).then(res => {
              this.fullscreenLoading = false;
              const blob = new Blob([res.data]);
              const link = document.createElement('a');
              link.href = window.URL.createObjectURL(blob);
              link.download = row.originalFileName + '.' + row.suffix;
              link.click();
            });
          } else {
            console.log(response.msg);
            this.$message.error(response.msg);
            this.fullscreenLoading = false;
          }
        }).catch(err => {
          console.log(err);
          this.$message.error(this.$t('fileManager.failedObtainDownload'));
          this.fullscreenLoading = false;
        });
      } else {
        this.fullscreenLoading = true;
        getFileInfo(this.fileId).then(response => {
          if (response.code === 200) {
            let row = response.data;
            if (row.downloadUrlMd === null || row.downloadUrlMd === '') {
              this.$message.warning(this.$t('fileManager.mdDownloadError'));
              this.fullscreenLoading = false;
            } else {
              let url = row.downloadUrlMd.replace(process.env.VUE_APP_FILE_MANAGER_DOWNLOAD_URL, process.env.VUE_APP_FILE_URLS);
              axios.get(url, {
                responseType: 'blob'
              }).then(res => {
                this.fullscreenLoading = false;
                const blob = new Blob([res.data]);
                const link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = row.originalFileName + '.md';
                link.click();
              });
            }
          } else {
            console.log(response.msg);
            this.$message.error(response.msg);
            this.fullscreenLoading = false;
          }
        }).catch(err => {
          console.log(err);
          this.$message.error(this.$t('fileManager.failedObtainDownload'));
          this.fullscreenLoading = false;
        });
      }
    },
    handleExport() {
      let time = parseTime(new Date().getTime(), '{y}{m}{d}{h}{i}{s}');
      this.jsonDownload(
        'kb/file/export',
        {
          "fileName": this.queryParams.fileName,
          "suffix": this.queryParams.suffix,
          "startTime": this.queryParams.startTime,
          "endTime": this.queryParams.endTime,
          "trainStartTime": this.queryParams.trainStartTime,
          "trainEndTime": this.queryParams.trainEndTime,
          "kbIds": this.kbList,
          "trainStatus": this.queryParams.trainStatus,
          "kbName": this.queryParams.kbName,
        },
        this.$t('fileManager.exportName') + `_${time}.xlsx`
      )
    },
    handleStartUpdateFileTrain() {
      if (this.fileSelection.length < 1) {
        this.$message.warning(this.$t('fileManager.selectFileUpdateTrained'));
        return;
      }
      this.updateFileTrainLoading = true;
      let ids = this.fileSelection.map(item => item.id);
      updateFileTrain({ids}).then(res => {
        if (res.code === 200) {
          this.$message.success(this.$t('configuration.operationSuccess'));
        }
      }).catch(err => {
        this.$message.error(err);
      })
    },

    handleSwitchStatus(status) {
      /*      if (this.fileSelection.length < 1) {

              return;
            }*/
      let fileIds = this.fileSelection.map(item => item.id);
      let data = {
        fileIds,
        status
      };
      this.fullscreenLoading = true;
      batchEditStatus(data).then(res => {
        if (res.code === 200) {
          this.$message.success(this.$t('fileManager.batchEditSuccess'));
          this.getList();
          //清空已选择
          this.handleClearSelection();
        } else {
          console.log(res.msg);
          this.$message.error(res.msg);
        }
        this.fullscreenLoading = false;
      }).catch(err => {
        console.log(err);
        this.$message.error(this.$t('fileManager.batchEditError'));
        this.fullscreenLoading = false;
      });
    },

    handleShowJson(url) {
      let link = url.replace('https://deepseek-1340465638.cos.ap-nanjing.myqcloud.com', '/cosUrl');
      axios.get(link, {
        responseType: 'blob'
      }).then(res => {
        // 读取文字
        const reader = new FileReader();
        reader.readAsText(res.data, 'utf-8');
        reader.onload = () => {
          const jsonData = JSON.parse(reader.result);
          this.chunks = jsonData || [];
          this.showChunk = true;
        };
      }).catch(err => {
        console.log(err);
        this.$message.error(this.$t('fileManager.getChunkFailed'));
      });
    },
    handleLevelChange(row, value) {
      // 确保选择了有效级别
      if (value === undefined || value === null) {
        this.$message.warning('请选择有效级别');
        return;
      }
      // 准备请求数据
      const data = {
        fileId: row.id,
        bookLevel: value
      };
      // 调用封装好的editLevel函数
      editLevel(data)
        .then(response => {
          if (response.code === 200) {
            this.$message.success(this.$t('fileManager.levelUpdateSuccess'));
            // 更新本地数据以避免刷新页面
            row.bookLevel = value;
            // 关闭下拉框
            this.$refs.levelSelect.blur();
          } else {
            this.$message.error(this.$t('fileManager.levelUpdateError'));
          }
        })
        .catch(error => {
          console.error('更新级别失败:', error);
          this.$message.error(this.$t('fileManager.levelUpdateError'));
        });
    },
    async handleBatchDownload() {
      this.batchDownloadLoading = true;
      // 添加加载提示
      const loading = this.$loading({
        lock: true,
        text: this.$t('fileManager.batchDownloadLoading'),
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      try {
        const zip = new JSZip();
        const promises = [];
        const timestamp = new Date().getTime();
        // 遍历选中的文件
        for (const file of this.fileSelection) {
          const promise = this.downloadSingleFile(file)
            .then(({fileName, blobData}) => {
              zip.file(fileName, blobData, {binary: true})
            })
            .catch(error => {
              console.error(`文件${file.originalFileName}下载失败:`, error);
              this.$message.error(`文件${file.originalFileName}下载失败`);
            })
          promises.push(promise)
        }
        // 等待所有文件下载完成
        await Promise.all(promises)
        // 生成zip文件
        const content = await zip.generateAsync({
          type: 'blob',
          compression: 'DEFLATE',
          compressionOptions: {
            level: 6
          }
        })
        // 保存zip文件
        saveAs(content, this.$t('fileManager.batchDownload') + `_${timestamp}.zip`)
        this.$message.success(this.$t('fileManager.batchDownloadSuccess') + `${this.fileSelection.length}` + this.$t('fileManager.aFile'))
      } catch (error) {
        console.error('批量下载失败:', error);
        this.$message.error(this.$t('fileManager.batchDownloadError'));
      } finally {
        this.batchDownloadLoading = false;
        loading.close();
      }
    },
    async downloadSingleFile(file) {
      // 获取文件信息
      const response = await getFileInfo(file.id);
      if (response.code !== 200) {
        throw new Error('获取文件信息失败');
      }
      const fileData = response.data;
      const url = fileData.downloadUrl.replace(process.env.VUE_APP_FILE_MANAGER_DOWNLOAD_URL, process.env.VUE_APP_FILE_URLS);
      // 下载文件
      const res = await axios.get(url, {
        responseType: 'blob'
      })
      return {
        fileName: `${fileData.originalFileName}.${fileData.suffix}`,
        blobData: res.data
      }
    },
    async handleBatchDownloadMd() {
      // 添加trainStatus判断逻辑
      const hasUntrained = this.fileSelection.some(row => row.trainStatus !== 'success');
      if (hasUntrained) {
        this.$message.warning(this.$t('fileManager.onlyTrainedCanDownload'));
        return;
      }

      this.batchDownloadMdLoading = true;
      const loading = this.$loading({
        lock: true,
        text: this.$t('fileManager.batchDownloadLoading'),
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      try {
        const zip = new JSZip();
        const promises = [];
        const timestamp = new Date().getTime();
        // 遍历选中的文件
        for (const file of this.fileSelection) {
          const promise = this.downloadSingleFileMd(file)
            .then(({ fileName, blobData }) => {
              zip.file(fileName, blobData, { binary: true })
            })
            .catch(error => {
              this.fileSelection.length--;
              console.error(`文件${file.originalFileName}下载失败:`, error);
              this.$message.error(`文件${file.originalFileName}下载失败`);
            })
          promises.push(promise)
        }
        // 等待所有文件下载完成
        await Promise.all(promises)
        // 生成zip文件
        const content = await zip.generateAsync({
          type: 'blob',
          compression: 'DEFLATE',
          compressionOptions: {
            level: 6
          }
        })
        // 保存zip文件
        saveAs(content, this.$t('fileManager.batchDownloadMd')+`_${timestamp}.zip`)
        this.$message.success(this.$t('fileManager.batchDownloadSuccess')+`${this.fileSelection.length}`+this.$t('fileManager.aFile'))
      } catch (error) {
        console.error('批量下载失败:', error);
        this.$message.error(this.$t('fileManager.batchDownloadError'));
      } finally {
        this.batchDownloadMdLoading = false;
        loading.close();
      }
    },

    async handleDownloadImage() {
      this.showDownloadDialog = false;
      this.fullscreenLoading = true;
      try {
        const zip = new JSZip();
        // ✅ 过滤成功的结果，并添加到 ZIP
        let successCount = 0;
        const timestamp = new Date().getTime();
        const promises = [];
        const response = await getFileInfo(this.fileId);
        if (response.code !== 200) {
          this.$message.error(`获取文件 ${file.originalFileName} 信息失败`);
        }
        const fileData = response.data;
        const imageUrls = fileData.downloadUrlImages || [];
        if (!Array.isArray(imageUrls) || imageUrls.length === 0) {
          console.warn(`文件 ${file.originalFileName} 没有关联的图片 URL`);
        }
        // ✅ 收集每个图片的 Promise，返回 { filename, blob }
        for (const imageUrl of imageUrls) {
          const promise = this.downloadFileFromURL(imageUrl, "image_file",fileData.originalFileName)
            .catch(error => {
              console.error(`图片 ${imageUrl} 下载失败:`, error);
              this.$message.error(`图片 ${imageUrl} 下载失败`);
              // 返回 null 表示失败
              return null;
            });
          promises.push(promise);
        }
        // ✅ 等待所有图片下载完成
        const results = await Promise.all(promises);
        results.forEach(result => {
          if (result) {
            const { filename, blobData } = result;
            zip.file(filename, blobData, { binary: true });
            successCount++;
          }
        });

        if (successCount === 0) {
          this.$message.warning(this.$t('fileManager.noFileDownloaded'));
          return;
        }

        // ✅ 此时所有文件已添加，生成 ZIP
        const content = await zip.generateAsync({
          type: 'blob',
          compression: 'STORE' // 或 'DEFLATE'，根据需求
        });

        // 使用 file-saver 保存
        saveAs(content, `${this.$t('fileManager.downloadImage')}_${timestamp}.zip`);

        this.$message.success(
          `${this.$t('fileManager.batchDownloadSuccess')}${successCount}${this.$t('fileManager.aFile')}`
        );
      } catch (error) {
        console.error('批量下载失败:', error);
        this.$message.error(this.$t('fileManager.batchDownloadError'));
      } finally {
        this.fullscreenLoading = false;
        loading.close();
      }
    },

    async handleDownloadYaml() {
      this.showDownloadDialog = false;
      this.fullscreenLoading = true;
      const loading = this.$loading({
        lock: true,
        text: this.$t('fileManager.batchDownloadLoading'),
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      try {
        const zip = new JSZip();
        const timestamp = new Date().getTime();
        const response = await getFileInfo(this.fileId);

        if (response.code !== 200) {
          this.$message.error(`获取文件信息失败`);
          return;
        }

        const fileData = response.data;
        const yamlUrls = fileData.downloadUrlYamls || [];
        if (!Array.isArray(yamlUrls) || yamlUrls.length === 0) {
          this.$message.warning('该文件没有关联的 YAML 配置');
          return;
        }

        const regex = /\/yamls\/([^\/]+)\/([^?#]+)(?:\?|$)/;
        const map = new Map(); // Map<subdir, Array<{ url, filename }>>

        // 1. 分组 YAML URL
        yamlUrls.forEach(url => {
          const match = url.match(regex);
          if (match) {
            const subdir = match[1];
            const originalFilename = match[2];
            if (!map.has(subdir)) {
              map.set(subdir, []);
            }
            map.get(subdir).push({ url, originalFilename });
          } else {
            console.warn('URL 不匹配 yamls 结构，跳过:', url);
          }
        });

        const allPromises = [];
        const successCount = { value: 0 }; // 引用传递

        // 2. 遍历子目录，创建文件夹并收集下载任务
        for (const [subdir, items] of map) {
          const subdirZip = zip.folder(subdir);
          items.forEach(({ url, originalFilename }) => {
            const promise = this.downloadFileFromURL(url, subdir,fileData.originalFileName)
              .then(result => {
                if (result) {
                  subdirZip.file(result.filename, result.blobData, { binary: true });
                  successCount.value++;
                }
              })
              .catch(error => {
                console.error(`YAML 文件下载失败: ${url}`, error);
                // 不中断整体流程
              });

            allPromises.push(promise);
          });
        }

        // 3. 等待所有下载完成
        await Promise.allSettled(allPromises);

        if (successCount.value === 0) {
          this.$message.warning(this.$t('fileManager.noFilesDownloaded'));
          return;
        }

        // 4. 生成 ZIP
        const content = await zip.generateAsync({
          type: 'blob',
          compression: 'DEFLATE',
          compressionOptions: { level: 6 }
        });

        saveAs(content, `${this.$t('fileManager.downloadYaml')}_${timestamp}.zip`);
        this.$message.success(
          `${this.$t('fileManager.batchDownloadSuccess')}${successCount.value}${this.$t('fileManager.aFile')}`
        );
      } catch (error) {
        console.error('下载 YAML 失败:', error);
        this.$message.error(this.$t('fileManager.batchDownloadError'));
      } finally {
        this.fullscreenLoading = false;
        loading.close();
      }
    },
    async handleBatchDownloadYaml() {
      const hasUntrained = this.fileSelection.some(row => row.trainStatus !== 'success');
      if (hasUntrained) {
        this.$message.warning(this.$t('fileManager.onlyTrainedCanDownload'));
        return;
      }

      this.batchDownloadYamlLoading = true;
      const loading = this.$loading({
        lock: true,
        text: this.$t('fileManager.batchDownloadLoading'),
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      try {
        const zip = new JSZip();
        const timestamp = new Date().getTime();
        const allPromises = [];
        let successCount = 0;

        // 1. 遍历每个选中文件
        for (const file of this.fileSelection) {
          const response = await getFileInfo(file.id);

          if (response.code !== 200) {
            this.$message.error(`获取文件 ${file.originalFileName} 信息失败`);
            continue;
          }

          const fileData = response.data;
          const yamlUrls = fileData.downloadUrlYamls || [];
          if (!Array.isArray(yamlUrls) || yamlUrls.length === 0) {
            console.warn(`文件 ${file.originalFileName} 没有关联的 YAML URL`);
            continue;
          }
          const fileIdDirZip = zip.folder(fileData.originalFileName);
          const regex = /\/yamls\/([^\/]+)\/([^?#]+)(?:\?|$)/;
          const map = new Map();
          // 分组
          yamlUrls.forEach(url => {
            const match = url.match(regex);
            if (match) {
              const subdir = match[1];
              const originalFilename = match[2];
              if (!map.has(subdir)) {
                map.set(subdir, []);
              }
              map.get(subdir).push({ url, originalFilename });
            } else {
              console.warn('URL 不匹配，跳过:', url);
            }
          });

          // 为当前文件创建子目录结构
          for (const [subdir, items] of map) {
            const subdirZip = fileIdDirZip.folder(subdir);
            items.forEach(({ url, originalFilename }) => {

              const promise = this.downloadFileFromURL(url, subdir, fileData.originalFileName)
                .then(result => {
                  if (result) {
                    subdirZip.file(result.filename, result.blobData, { binary: true });
                    successCount++;
                  }
                })
                .catch(error => {
                  console.error(`YAML 文件下载失败: ${url}`, error);
                });

              allPromises.push(promise);
            });
          }
        }

        // 2. 等待所有下载完成
        await Promise.allSettled(allPromises);

        if (successCount === 0) {
          this.$message.warning(this.$t('fileManager.noFilesDownloaded'));
          return;
        }

        // 3. 生成 ZIP
        const content = await zip.generateAsync({
          type: 'blob',
          compression: 'DEFLATE',
          compressionOptions: { level: 6 }
        });

        saveAs(content, `${this.$t('fileManager.batchDownloadYaml')}_${timestamp}.zip`);
        this.$message.success(
          `${this.$t('fileManager.batchDownloadSuccess')}${successCount}${this.$t('fileManager.aFile')}`
        );
      } catch (error) {
        console.error('批量下载失败:', error);
        this.$message.error(this.$t('fileManager.batchDownloadError'));
      } finally {
        this.batchDownloadYamlLoading = false;
        loading.close();
      }
    },
    async handleBatchDownloadImage() {
      const hasUntrained = this.fileSelection.some(row => row.trainStatus !== 'success');
      if (hasUntrained) {
        this.$message.warning(this.$t('fileManager.onlyTrainedCanDownload'));
        return;
      }

      this.batchDownloadImageLoading = true;
      const loading = this.$loading({
        lock: true,
        text: this.$t('fileManager.batchDownloadLoading'),
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      try {
        const zip = new JSZip();
        // ✅ 过滤成功的结果，并添加到 ZIP
        let successCount = 0;
        const timestamp = new Date().getTime();

        for (const file of this.fileSelection) {
          const promises = [];
          const response = await getFileInfo(file.id);
          if (response.code !== 200) {
            this.$message.error(`获取文件 ${file.originalFileName} 信息失败`);
            continue;
          }
          const fileData = response.data;
          const imageUrls = fileData.downloadUrlImages || [];
          if (!Array.isArray(imageUrls) || imageUrls.length === 0) {
            console.warn(`文件 ${file.originalFileName} 没有关联的图片 URL`);
            continue;
          }
          const headphones = zip.folder(fileData.originalFileName);
          // ✅ 收集每个图片的 Promise，返回 { filename, blob }
          for (const imageUrl of imageUrls) {
            const promise = this.downloadFileFromURL(imageUrl,"image_file", fileData.originalFileName)
              .catch(error => {
                console.error(`图片 ${imageUrl} 下载失败:`, error);
                this.$message.error(`图片 ${imageUrl} 下载失败`);
                // 返回 null 表示失败
                return null;
              });
            promises.push(promise);
          }
          // ✅ 等待所有图片下载完成
          const results = await Promise.all(promises);
          results.forEach(result => {
            if (result) {
              const { filename, blobData } = result;
              headphones.file(filename, blobData, { binary: true });
              successCount++;
            }
          });
        }
        if (successCount === 0) {
          this.$message.warning(this.$t('fileManager.noFileDownloaded'));
          return;
        }

        // ✅ 此时所有文件已添加，生成 ZIP
        const content = await zip.generateAsync({
          type: 'blob',
          compression: 'STORE' // 或 'DEFLATE'，根据需求
        });

        // 使用 file-saver 保存
        saveAs(content, `${this.$t('fileManager.batchDownloadImage')}_${timestamp}.zip`);

        this.$message.success(
          `${this.$t('fileManager.batchDownloadSuccess')}${successCount}${this.$t('fileManager.aFile')}`
        );
      } catch (error) {
        console.error('批量下载失败:', error);
        this.$message.error(this.$t('fileManager.batchDownloadError'));
      } finally {
        this.batchDownloadImageLoading = false;
        loading.close();
      }
    },
    // 添加完整下载方法
    async handleFullBatchDownload() {
      const hasUntrained = this.fileSelection.some(row => row.trainStatus !== 'success');
      if (hasUntrained) {
        this.$message.warning(this.$t('fileManager.onlyTrainedCanDownload'));
        return;
      }

      this.fullBatchDownloadLoading = true;
      const loading = this.$loading({
        lock: true,
        text: this.$t('fileManager.batchDownloadLoading'),
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      try {
        const zip = new JSZip();
        const timestamp = new Date().getTime();
        const allPromises = [];
        let successCount = 0;

        // 遍历每个选中文件
        for (const file of this.fileSelection) {
          const response = await getFileInfo(file.id);

          if (response.code !== 200) {
            this.$message.error(`获取文件 ${file.originalFileName} 信息失败`);
            continue;
          }

          const fileData = response.data;
          // 创建主文件目录
          const mainDir = zip.folder(fileData.originalFileName);

          // 1. 处理PDF文件
          const pdfDir = mainDir.folder('PDF');
          const pdfPromise = this.downloadSingleFile(file)
            .then(({fileName, blobData}) => {
              pdfDir.file(fileName, blobData, {binary: true});
              successCount++;
            })
            .catch(error => {
              console.error(`PDF文件 ${file.originalFileName} 下载失败:`, error);
            });
          allPromises.push(pdfPromise);

          // 2. 处理MD文件
          const mdDir = mainDir.folder('MD');
          const mdUrl = fileData.downloadUrlMd || '';
          if (mdUrl) {
            const mdPromise = this.downloadFileFromURL(mdUrl, 'md_file', fileData.originalFileName)
              .then(result => {
                if (result) {
                  mdDir.file(result.filename, result.blobData, {binary: true});
                  successCount++;
                }
              })
              .catch(error => {
                console.error(`MD文件 ${file.originalFileName} 下载失败:`, error);
              });
            allPromises.push(mdPromise);
          }

          // 3. 处理YAML文件
          const yamlDir = mainDir.folder('YAML');
          const yamlUrls = fileData.downloadUrlYamls || [];
          if (Array.isArray(yamlUrls) && yamlUrls.length > 0) {
            const regex = /\/yamls\/([^\/]+)\/([^?#]+)(?:\?|$)/;
            const map = new Map();

            // 分组YAML文件
            yamlUrls.forEach(url => {
              const match = url.match(regex);
              if (match) {
                const subdir = match[1];
                const originalFilename = match[2];
                if (!map.has(subdir)) {
                  map.set(subdir, []);
                }
                map.get(subdir).push({ url, originalFilename });
              }
            });

            // 创建YAML子目录结构
            for (const [subdir, items] of map) {
              const subdirZip = yamlDir.folder(subdir);
              items.forEach(({ url, originalFilename }) => {
                const promise = this.downloadFileFromURL(url, subdir, fileData.originalFileName)
                  .then(result => {
                    if (result) {
                      subdirZip.file(result.filename, result.blobData, { binary: true });
                      successCount++;
                    }
                  })
                  .catch(error => {
                    console.error(`YAML文件下载失败: ${url}`, error);
                  });
                allPromises.push(promise);
              });
            }
          }

          // 4. 处理IMAGE文件
          const imageDir = mainDir.folder('IMAGE');
          const imageUrls = fileData.downloadUrlImages || [];
          if (Array.isArray(imageUrls) && imageUrls.length > 0) {
            for (const imageUrl of imageUrls) {
              const promise = this.downloadFileFromURL(imageUrl, "image_file", fileData.originalFileName)
                .catch(error => {
                  console.error(`图片 ${imageUrl} 下载失败:`, error);
                  return null;
                });
              allPromises.push(promise.then(result => {
                if (result) {
                  imageDir.file(result.filename, result.blobData, { binary: true });
                  successCount++;
                }
              }));
            }
          }
        }

        // 等待所有下载完成
        await Promise.allSettled(allPromises);

        if (successCount === 0) {
          this.$message.warning(this.$t('fileManager.noFilesDownloaded'));
          return;
        }

        // 生成ZIP
        const content = await zip.generateAsync({
          type: 'blob',
          compression: 'DEFLATE',
          compressionOptions: { level: 6 }
        });

        saveAs(content, `FullbatchDownload_${timestamp}.zip`);
        this.$message.success(
          `${this.$t('fileManager.batchDownloadSuccess')}${successCount}${this.$t('fileManager.aFile')}`
        );
      } catch (error) {
        console.error('批量下载失败:', error);
        this.$message.error(this.$t('fileManager.batchDownloadError'));
      } finally {
        this.fullBatchDownloadLoading = false;
        loading.close();
      }
    },
    async downloadFileFromURL(fileUrl, fileType, originFileName) {
      let filename;
      try {
        const urlObj = new URL(fileUrl);
        filename = urlObj.pathname.split('/').pop() || filename;
        filename = this.generateFileName(filename, fileType, originFileName)
      } catch (e) {
        console.warn('无法从 URL 提取文件名:', fileUrl);
      }

      const url = fileUrl.replace(
        process.env.VUE_APP_FILE_MANAGER_DOWNLOAD_URL,
        process.env.VUE_APP_FILE_URLS
      );

      const res = await axios.get(url, {
        responseType: 'blob'
      });

      return {
        filename,
        blobData: res.data
      };
    },
    /**
     * 生成标准化文件名（完全基于结构识别，无关键词依赖）
     *
     * @param {string} sourceName - 原始名称（如 1936_1.yaml, 1936_image001.yaml, abc123, 001 等）
     * @param {string} fileType - 类型：'chapter_yaml' | 'document_yaml' | 'image_yaml' | 'image_file'
     * @param {string} originFileName- 前缀，如 DOC-RedMarx-0051
     * @returns {string} 格式化后的文件名
     */
    generateFileName(sourceName, fileType, originFileName) {
      if (!sourceName || !fileType|| !originFileName) {
        console.warn('缺少必要参数:', { sourceName, fileType, originFileName });
      }

      const cleanPrefix = this.getFileNamePrefix(originFileName) // 清理扩展名
      let num = 1;

      // 提取字符串末尾的连续数字
      const extractTrailingNumber = (str) => {
        const match = String(str).trim().match(/(\d+)(?:\.\w+)?$/);
        return match ? parseInt(match[1], 10) : null;
      };

      // 格式化数字为三位：1 → 001, 10 → 010, 123 → 123
      const padNum = (n) => (isNaN(n) ? '001' : n.toString().padStart(3, '0'));

      let result = '';

      switch (fileType) {
        // ✅ chapter_yaml: 匹配 _数字 结尾
        case 'chapter_yaml': {
          const match = sourceName.match(/_(\d+)(?:\.\w+)?$/);
          num = match ? parseInt(match[1], 10) : 1;
          result = `${cleanPrefix}-chapter-${padNum(num)}.yaml`;
          break;
        }

        // ✅ document_yaml: 无下划线结构，或纯 ID
        case 'document_yaml': {
          result = `${originFileName}.yaml`;
          break;
        }

        // ✅ image_yaml: 包含 _ 且末尾有数字（如 _001, _123）
        case 'images_yaml': {
          // 匹配 _ 后跟数字结尾：_001.yaml, _123.yml, _1
          const match = sourceName.match(/image(\d+)(?:\.\w+)?$/);
          num = match ? parseInt(match[1], 10) : 1;
          result = `${cleanPrefix}-image-${padNum(num)}.yaml`;
          break;
        }

        // ✅ image_file: 纯字符串末尾带数字（如 abc123, file3, 001）
        case 'image_file': {
          num = extractTrailingNumber(sourceName) || 1;

          // ✅ 从 sourceName 提取扩展名（带 .）
          const extMatch = String(sourceName).match(/\.\w+$/);
          const fileExt = extMatch ? extMatch[0] : '.jpg'; // 无扩展名时默认 .jpg

          result = `${cleanPrefix}-image-${padNum(num)}${fileExt}`;
          break;
        }

        default:
          console.warn('未知的 fileType:', fileType);
          result = `${cleanPrefix}-unknown.${sourceName.split('.').pop() || 'file'}`;
      }
      return result;
    },

    getFileNamePrefix(fileName) {
      console.warn('origin文件名:', fileName);
      const match = fileName.match(/^([^-]+-[^-]+-[^-]+)/);
      const finalFileName = match ? match[1] : fileName;
      return finalFileName;
    },
    async downloadSingleFileMd(file) {
      // 获取文件信息
      const response = await getFileInfo(file.id);
      if (response.code !== 200) {
        throw new Error('获取文件信息失败');
      }
      const fileData = response.data;
      const url = fileData.downloadUrlMd.replace(process.env.VUE_APP_FILE_MANAGER_DOWNLOAD_URL, process.env.VUE_APP_FILE_URLS);
      // 下载文件
      const res = await axios.get(url, {
        responseType: 'blob'
      })
      return {
        fileName: `${fileData.originalFileName}.md`,
        blobData: res.data
      }
    },
    beforeDestroy() {
      clearInterval(this.timer);
      this.timer = null;
      this.handleClearSelection(); // 组件卸载时清理选择
    }
  }
}
</script>
<style lang="scss" scoped>
.back-blue {
  background-image: url("~@/assets/images/upload.png");
  background-size: 100% 100%;
  background-repeat: no-repeat;
}

.back-green {
  background-image: url("~@/assets/images/train.png");
  background-size: 100% 100%;
  background-repeat: no-repeat;
}

.update-green {
  background-image: url("~@/assets/images/update1.png");
  background-size: 100% 100%;
  background-repeat: no-repeat;
}

.table_row_file_name {
  color: #405EB0;
  cursor: pointer;
}

.dialog-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 760px;
  height: auto;
}

//::v-deep .el-dialog__wrapper {
//  margin-top: 0px;
//}

::v-deep .el-dialog__body {
  padding: 10px 20px 30px 20px;
}

.upload-config-box {
  background-color: #F2F5FA;
  display: flex;
  align-items: center;
  padding: 10px 0;
}

::v-deep .elInput {
  background-color: transparent !important;
  border-radius: 4px;
}

.upload-title {
  font-weight: 600;
  font-size: 21px;
  color: #2C2C2C;
  line-height: 29px;
}

::v-deep .el-dialog .el-card {
  box-shadow: none;
  border-radius: 0;
  border: none;
}

.batch-download-container {
  display: flex;
  flex-wrap: nowrap;  /* 不允许换行 */
  gap: 8px;  /* 按钮之间的间距 */
  overflow-x: auto;  /* 如果按钮太多，允许水平滚动 */
  padding: 8px 0;
}

/* 确保按钮在容器中正确对齐 */
.batch-download-container .el-button {
  flex-shrink: 0;  /* 不允许按钮缩小 */
}

/* 优雅的下拉框样式 */
::v-deep .elegant-select-dropdown {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border: 1px solid #e5e6eb;
  padding: 6px 0;
}

::v-deep .elegant-select-dropdown .el-select-dropdown__item {
  padding: 8px 16px;
  transition: all 0.2s ease;
  border-radius: 4px;
  margin: 0 6px;
}

::v-deep .elegant-select-dropdown .el-select-dropdown__item:hover {
  background-color: #f0f5ff;
  color: #1890ff;
}

::v-deep .elegant-select-dropdown .el-select-dropdown__item.selected {
  background-color: #e6f7ff;
  color: #1890ff;
  font-weight: 500;
}

/* 空选项样式 */
::v-deep .empty-option {
  color: #909399;
  font-style: italic;
}

/* 表格单元格内边距调整 */
::v-deep .el-table__cell {
  padding: 12px 0;
}
/* 优雅的消息提示 */
::v-deep .elegant-message {
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
</style>
