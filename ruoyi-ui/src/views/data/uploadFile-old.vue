<template>
  <div class="back" align="left">
    <div
      class="color-background"
      style="position: absolute;width:100%;height:100%;"
    />
    <div class="background">
      <div style="display: flex;flex-direction: column;align-items: center;">
        <div class="left-controller" id="searchDiv" style="min-height:140px;height: 100%;">
          <div class="card-button" style="background-color: #87A5CF" @click="handleUpload()">
            <span class="card-button-text">文件上传</span>
            <svg style="position: absolute;bottom:2px;right:2px;" width="82" height="66" viewBox="0 0 82 66" fill="none"
                 xmlns="http://www.w3.org/2000/svg">
              <path
                d="M7.54666 5.42714C7.93103 4.64081 8.77666 4.18964 9.63509 4.30565L41.0001 8.25018L72.3651 4.30565C73.2235 4.20253 74.0692 4.6537 74.4535 5.42714L79.7963 16.1779C80.9495 18.4853 79.7195 21.2826 77.2595 21.9916L56.3238 28.0115C54.5429 28.5271 52.6338 27.7666 51.6857 26.1682L41.0001 8.25018L30.3145 26.1682C29.3663 27.7666 27.4573 28.5271 25.6763 28.0115L4.75353 21.9916C2.28072 21.2826 1.06353 18.4853 2.21666 16.1779L7.54666 5.42714ZM41.141 16.5002L48.1751 28.2822C50.0842 31.4791 53.8895 33.0002 57.4642 31.9689L73.8001 27.2767V48.8041C73.8001 51.64 71.8782 54.115 69.1363 54.8111L42.986 61.3853C41.6792 61.7205 40.3082 61.7205 39.0142 61.3853L12.8638 54.8111C10.122 54.1021 8.20009 51.6271 8.20009 48.7912V27.2639L24.5488 31.9689C28.1107 33.0002 31.9288 31.4791 33.8379 28.2822L40.8592 16.5002H41.141Z"
                fill="white"/>
            </svg>
          </div>
          <div style="flex:5;padding:0 20px;height:fit-content;">
            <el-form style="width:100%;height:fit-content;" label-position="left">
              <el-row style="width:100%;height:fit-content;">
                <el-col :span="8" :lg="8" :md="12" :sm="12" :xs="24">
                  <el-form-item label="文档名称" prop="originalFileName">
                    <el-input
                      v-model="queryParams.originalFileName"
                      placeholder="请输入文档名称"
                      style="width: 70%;"
                      @keydown.enter.nat.native="handleQuery"
                      @clear="handleQuery"
                      clearable/>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="12" :sm="12" :xs="24">
                  <el-form-item label="文件格式" prop="suffix">
                    <el-select
                      v-model="queryParams.suffix"
                      style="width: 70%;"
                      placeholder="文件格式"
                      @change="handleQuery"
                      @clear="handleQuery"
                      clearable
                    >
                      <el-option
                        v-for="(item,index) in offerTypes"
                        :key="index"
                        :label="item.label"
                        :value="item.value">
                      </el-option>

                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="12" :sm="12" :xs="24">
                  <el-form-item label="国家" prop="areaPermission">
                    <el-select
                      v-model="queryParams.areaPermission"
                      multiple
                      style="width: 70%;"
                      placeholder="选择国家"
                      @change="handleQuery"
                      @clear="handleQuery"
                      clearable
                    >
                      <el-option
                        v-for="(item,index) in areasOptions"
                        :key="index"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>

                <el-col :span="8" :lg="8" :md="12" :sm="12" :xs="24">
                  <el-form-item label="上传时间" prop="uploadTime">
                    <el-date-picker
                      v-model="uploadTime"
                      type="datetimerange"
                      class="inputDatePicker"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      range-separator="至"
                      start-placeholder="开始时间"
                      end-placeholder="结束时间"
                      @change="handleSelectUploadTime"
                      align="right">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="12" :sm="12" :xs="24">
                  <el-form-item label="训练时间" prop="trainTime">
                    <el-date-picker
                      v-model="trainTime"
                      type="datetimerange"
                      class="inputDatePicker"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      range-separator="至"
                      start-placeholder="开始时间"
                      end-placeholder="结束时间"
                      @change="handleSelectTrainTime"
                      align="right">
                    </el-date-picker>
                  </el-form-item>
                </el-col>

                <el-col :span="8" :lg="8" :md="12" :sm="12" :xs="24">
                  <el-form-item label="用户" prop="rolePermission">
                    <el-select
                      v-model="queryParams.rolePermission"
                      multiple
                      style="width: 70%;"
                      placeholder="选择用户"
                      @change="handleQuery"
                      @clear="handleQuery"
                      clearable
                    >
                      <el-option
                        v-for="(item,index) in rolePermissionOptions"
                        :key="index"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="12" :sm="12" :xs="24">
                  <el-button
                    class='button-reset'
                    icon="el-icon-refresh"
                    @click="resetQuery"
                    size="medium"
                    round
                  >重置
                  </el-button>
                  <el-button
                    class="button-primary"
                    icon="el-icon-search"
                    size="medium"
                    @click="handleQuery"
                    round
                  >查询
                  </el-button>
                  <el-button
                    class='button-warning'
                    icon="el-icon-download"
                    @click="handleExport"
                    size="medium"
                    v-hasPermi="['rpdp:contractinfo:export']"
                    round
                  >导出
                  </el-button>

                </el-col>
              </el-row>
            </el-form>
          </div>
          <div class="card-button" style="background-color: #F9B889;" @click="handleStartTraning">
            <span class="card-button-text">开始训练</span>
            <svg style="position: absolute;bottom:-2px;right: 0;" width="86" height="86" viewBox="0 0 86 86" fill="none"
                 xmlns="http://www.w3.org/2000/svg">
              <path
                d="M18.4542 67.546C16.0056 64.6793 14.1088 61.4687 12.7638 57.914C11.4189 54.3593 10.7476 50.5825 10.75 46.5835C10.75 37.6252 13.8854 30.0106 20.1563 23.7397C26.4271 17.4689 34.0417 14.3335 43 14.3335H43.7167L37.9833 8.60016L43 3.5835L57.3333 17.9168L43 32.2502L37.8938 27.1439L43.5375 21.5002H43C36.0722 21.5002 30.1597 23.9488 25.2625 28.846C20.3653 33.7432 17.9167 39.6557 17.9167 46.5835C17.9167 49.6293 18.4088 52.496 19.393 55.1835C20.3772 57.871 21.7664 60.2897 23.5604 62.4397L18.4542 67.546ZM39.4167 66.2918C39.4167 64.9182 38.9532 63.5601 38.0263 62.2176C37.0995 60.875 36.0698 59.4715 34.9375 58.0072C33.8028 56.5428 32.772 55.0199 31.8451 53.4384C30.9182 51.857 30.456 50.1692 30.4583 48.3752C30.4583 44.9113 31.6826 41.955 34.1313 39.5064C36.5799 37.0578 39.5361 35.8335 43 35.8335C46.4639 35.8335 49.4201 37.0578 51.8688 39.5064C54.3174 41.955 55.5417 44.9113 55.5417 48.3752C55.5417 50.1668 55.0782 51.8546 54.1513 53.4384C53.2245 55.0222 52.1948 56.544 51.0625 58.0036C49.9278 59.468 48.897 60.8714 47.9701 62.214C47.0432 63.5565 46.581 64.9158 46.5833 66.2918H39.4167ZM39.4167 75.2502V69.8752H46.5833V75.2502H39.4167ZM67.5458 67.546L62.4396 62.4397C64.2313 60.2897 65.6204 57.871 66.607 55.1835C67.5936 52.496 68.0857 49.6293 68.0833 46.5835C68.0833 42.6418 67.2628 38.9832 65.6216 35.6077C63.9804 32.2322 61.7253 29.411 58.8563 27.1439L63.9625 22.0377C67.4264 25.0238 70.1736 28.6226 72.2042 32.8342C74.2347 37.0459 75.25 41.6289 75.25 46.5835C75.25 50.5849 74.5775 54.3629 73.2326 57.9176C71.8876 61.4722 69.9921 64.6817 67.5458 67.546Z"
                fill="white"/>
            </svg>
          </div>
        </div>
      </div>
      <div class="right-table" style="flex:4; position: relative">
        <template v-if="!training">
          <el-table
            :data="upfileList"
            v-loading="loading"
            height="100%"
            @selection-change="handleSelectionChange"
            :row-style="{ height: '60px' }"
            :row-key="getRowKey"
            ref="fileTable"
            style="flex: 1"
          >
            <template slot="empty">
              <el-empty
                description="暂无数据"
                :image-size="120"
              ></el-empty>
            </template>
            <el-table-column
            type="selection"
            width="50"
            align="center"
            reserve-selection
            :selectable="selectable"
          >
          </el-table-column><!--
            <el-table-column
              type="index"
              width="60"
              label="序号"
              align="center"
            ></el-table-column>-->
            <el-table-column
              prop="originalFileName"
              label="文件名称"
              align="left"
              min-width="40%"
            ></el-table-column>
            <el-table-column
              prop="trainStatus"
              label="文件状态"
              align="center"
              min-width="20%"
            >
              <template slot-scope="scope" align="enter">
                <div
                  style="position: absolute;left:0;right:0;top:0;bottom:0;margin: auto;width: fit-content;height: fit-content;padding: 6px 8px;">
                  <el-tag
                    v-if="scope.row.trainStatus"
                    style="font-size:14px;"
                    :type="scope.row.trainStatus === 'success' ? 'success'
                  : scope.row.trainStatus === 'untrained' ? 'primary'
                  : scope.row.trainStatus === 'training' ? 'warning'
                  : scope.row.trainStatus === 'error' ? 'danger' : 'info'">
                    {{ statusOptions.find(item => item.value === scope.row.trainStatus).label }}
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
            <el-table-column
              :prop="item.prop"
              :label="item.label"
              align="center"
              :min-width="item.width"
              v-for="(item,index) in columnOptions"
              :key="index"
              v-if="item.show"
            ></el-table-column>
            <el-table-column
              label="操作"
              align="center"
              width="160px"
            >
              <template slot="header" slot-scope="scope">
                <el-tooltip
                  content="表格列设置"
                  placement="top"
                >
                  <el-button @click="columnSettingVal = true;" size="small" plain style="float: right;font-size: 14px;"
                             icon="el-icon-setting">表格列显示设置
                  </el-button>
                </el-tooltip>
              </template>
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  style="font-size: 14px;font-weight: 600"
                  @click="showEditDialog(scope.row)"
                >
                  编辑
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-download"
                  style="font-size: 14px;font-weight: 600"
                  @click="handleDownload(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  class="redDot"
                  icon="el-icon-delete"
                  style="font-size: 14px;font-weight: bold"
                  @click="handleDelete(scope.row)"
                  :disabled="scope.row.isDeleting"
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
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="queryParams.pageNum"
              :page-sizes="[10, 20, 30, 50]"
              :page-size="queryParams.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              background
            >
            </el-pagination>
            <el-dialog
              :visible.sync="editDialogVisible"
              width="40%"
              :close-on-click-modal="false">
              <div style="">
                <el-form :model="editForm"  ref="editForm" label-width="100px">
                  <el-form-item label="修改权限:"/>
                  <el-form-item label="国家">
                    <el-select
                      v-model="editForm.areaPermission"
                      multiple
                      style="width: 100%;"
                      placeholder="选择国家"
                      clearable>
                      <el-option
                        v-for="(item,index) in areasOptions"
                        :key="index"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="用户权限">
                    <el-select
                      v-model="editForm.rolePermission"
                      multiple
                      filterable
                      style="width: 100%;"
                      placeholder="选择用户"
                      clearable
                    >
                      <el-option
                        v-for="(item,index) in rolePermissionOptions"
                        :key="index"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-form>

                <span slot="footer" class="dialog-footer">
                  <el-button @click="editDialogVisible = false">取 消</el-button>
                  <el-button type="primary" @click="saveEditForm()">确 定</el-button>
              </span>
              </div>
            </el-dialog>
          </div>
        </template>
        <template v-else>
          <div style="position:absolute;left: 0;top: 0;bottom: 0;right: 0;margin: auto">
            <svg
              :class="training ? 'loading' : '' + ' empty-logo'"
              width="128"
              height="128"
              viewBox="0 0 41 41"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M37.5324 16.8707C37.9808 15.5241 38.1363 14.0974 37.9886 12.6859C37.8409 11.2744 37.3934 9.91076 36.676 8.68622C35.6126 6.83404 33.9882 5.3676 32.0373 4.4985C30.0864 3.62941 27.9098 3.40259 25.8215 3.85078C24.8796 2.7893 23.7219 1.94125 22.4257 1.36341C21.1295 0.785575 19.7249 0.491269 18.3058 0.500197C16.1708 0.495044 14.0893 1.16803 12.3614 2.42214C10.6335 3.67624 9.34853 5.44666 8.6917 7.47815C7.30085 7.76286 5.98686 8.3414 4.8377 9.17505C3.68854 10.0087 2.73073 11.0782 2.02839 12.312C0.956464 14.1591 0.498905 16.2988 0.721698 18.4228C0.944492 20.5467 1.83612 22.5449 3.268 24.1293C2.81966 25.4759 2.66413 26.9026 2.81182 28.3141C2.95951 29.7256 3.40701 31.0892 4.12437 32.3138C5.18791 34.1659 6.8123 35.6322 8.76321 36.5013C10.7141 37.3704 12.8907 37.5973 14.9789 37.1492C15.9208 38.2107 17.0786 39.0587 18.3747 39.6366C19.6709 40.2144 21.0755 40.5087 22.4946 40.4998C24.6307 40.5054 26.7133 39.8321 28.4418 38.5772C30.1704 37.3223 31.4556 35.5506 32.1119 33.5179C33.5027 33.2332 34.8167 32.6547 35.9659 31.821C37.115 30.9874 38.0728 29.9178 38.7752 28.684C39.8458 26.8371 40.3023 24.6979 40.0789 22.5748C39.8556 20.4517 38.9639 18.4544 37.5324 16.8707ZM22.4978 37.8849C20.7443 37.8874 19.0459 37.2733 17.6994 36.1501C17.7601 36.117 17.8666 36.0586 17.936 36.0161L25.9004 31.4156C26.1003 31.3019 26.2663 31.137 26.3813 30.9378C26.4964 30.7386 26.5563 30.5124 26.5549 30.2825V19.0542L29.9213 20.998C29.9389 21.0068 29.9541 21.0198 29.9656 21.0359C29.977 21.052 29.9842 21.0707 29.9867 21.0902V30.3889C29.9842 32.375 29.1946 34.2791 27.7909 35.6841C26.3872 37.0892 24.4838 37.8806 22.4978 37.8849ZM6.39227 31.0064C5.51397 29.4888 5.19742 27.7107 5.49804 25.9832C5.55718 26.0187 5.66048 26.0818 5.73461 26.1244L13.699 30.7248C13.8975 30.8408 14.1233 30.902 14.3532 30.902C14.583 30.902 14.8088 30.8408 15.0073 30.7248L24.731 25.1103V28.9979C24.7321 29.0177 24.7283 29.0376 24.7199 29.0556C24.7115 29.0736 24.6988 29.0893 24.6829 29.1012L16.6317 33.7497C14.9096 34.7416 12.8643 35.0097 10.9447 34.4954C9.02506 33.9811 7.38785 32.7263 6.39227 31.0064ZM4.29707 13.6194C5.17156 12.0998 6.55279 10.9364 8.19885 10.3327C8.19885 10.4013 8.19491 10.5228 8.19491 10.6071V19.808C8.19351 20.0378 8.25334 20.2638 8.36823 20.4629C8.48312 20.6619 8.64893 20.8267 8.84863 20.9404L18.5723 26.5542L15.206 28.4979C15.1894 28.5089 15.1703 28.5155 15.1505 28.5173C15.1307 28.5191 15.1107 28.516 15.0924 28.5082L7.04046 23.8557C5.32135 22.8601 4.06716 21.2235 3.55289 19.3046C3.03862 17.3858 3.30624 15.3413 4.29707 13.6194ZM31.955 20.0556L22.2312 14.4411L25.5976 12.4981C25.6142 12.4872 25.6333 12.4805 25.6531 12.4787C25.6729 12.4769 25.6928 12.4801 25.7111 12.4879L33.7631 17.1364C34.9967 17.849 36.0017 18.8982 36.6606 20.1613C37.3194 21.4244 37.6047 22.849 37.4832 24.2684C37.3617 25.6878 36.8382 27.0432 35.9743 28.1759C35.1103 29.3086 33.9415 30.1717 32.6047 30.6641C32.6047 30.5947 32.6047 30.4733 32.6047 30.3889V21.188C32.6066 20.9586 32.5474 20.7328 32.4332 20.5338C32.319 20.3348 32.154 20.1698 31.955 20.0556ZM35.3055 15.0128C35.2464 14.9765 35.1431 14.9142 35.069 14.8717L27.1045 10.2712C26.906 10.1554 26.6803 10.0943 26.4504 10.0943C26.2206 10.0943 25.9948 10.1554 25.7963 10.2712L16.0726 15.8858V11.9982C16.0715 11.9783 16.0753 11.9585 16.0837 11.9405C16.0921 11.9225 16.1048 11.9068 16.1207 11.8949L24.1719 7.25025C25.4053 6.53903 26.8158 6.19376 28.2383 6.25482C29.6608 6.31589 31.0364 6.78077 32.2044 7.59508C33.3723 8.40939 34.2842 9.53945 34.8334 10.8531C35.3826 12.1667 35.5464 13.6095 35.3055 15.0128ZM14.2424 21.9419L10.8752 19.9981C10.8576 19.9893 10.8423 19.9763 10.8309 19.9602C10.8195 19.9441 10.8122 19.9254 10.8098 19.9058V10.6071C10.8107 9.18295 11.2173 7.78848 11.9819 6.58696C12.7466 5.38544 13.8377 4.42659 15.1275 3.82264C16.4173 3.21869 17.8524 2.99464 19.2649 3.1767C20.6775 3.35876 22.0089 3.93941 23.1034 4.85067C23.0427 4.88379 22.937 4.94215 22.8668 4.98473L14.9024 9.58517C14.7025 9.69878 14.5366 9.86356 14.4215 10.0626C14.3065 10.2616 14.2466 10.4877 14.2479 10.7175L14.2424 21.9419ZM16.071 17.9991L20.4018 15.4978L24.7325 17.9975V22.9985L20.4018 25.4983L16.071 22.9985V17.9991Z"
                fill="#FFFFFFC0"
              ></path>
            </svg>
            <div v-if="training" class="loading-text">
              <div style="margin-bottom: 8px;">{{ uploadText }}</div>
              <el-progress style="width:40%;text-shadow: #FFF 0 0 2px;" :color="customColors" :percentage="uploadState"
                           :format="formatText"></el-progress>
            </div>
          </div>
        </template>
      </div>
    </div>
    <el-dialog
      title="表格列显示设置"
      :visible.sync="columnSettingVal"
      width="40%"
    >
      <div style="height:120px;">
        <el-row>
          <el-col :span="6" style="height:50px;">
            <el-checkbox v-model="defaultShow" label="文件名称" border disabled></el-checkbox>
          </el-col>
          <el-col :span="6" style="height:50px;">
            <el-checkbox v-model="defaultShow" label="训练状态" border disabled></el-checkbox>
          </el-col>
          <el-col :span="6" v-for="(item,index) in columnOptions" :key="index" style="height:50px;">
            <el-checkbox v-model="item.show" :label="item.label" border @change="handleChangeColumn"></el-checkbox>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <el-dialog
      title="文件上传"
      :visible.sync="uploadVal"
      width="75%"
      :close-on-click-modal="false"
    >
      <div style="width:100%;height:60vh;background-color: beige">
      </div>
      <div v-show="!isUploading" style="position: absolute;right: 20px;top:16px;z-index:200;font-size: 20px;cursor: pointer"
           @click="uploadVal=false;"><i class="el-icon-close"></i></div>
      <div class="dialog-background">
        <div style="width:280px;position:relative;background-color: #87A5CF">
          <svg style="position: absolute;bottom:0;left:0;right: 0;margin:auto;margin-left:-32px;" width="370"
               height="370" viewBox="0 0 370 370" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path
              d="M105.665 20.3916L33.3484 51.3646L110.164 230.724L139.929 217.976L119.979 79.2657L119.054 72.8275L175.831 64.6615L166.784 43.5383C154.729 46.2649 145.92 46.0307 132.529 58.7589L128.198 62.8729L123.731 58.9099C110.638 47.2954 108.68 32.4853 105.665 20.3916ZM119.331 20.7471C121.675 30.0008 123.331 38.2896 128.565 45.0862C137.828 37.7064 146.232 34.805 153.822 32.994C143.355 27.4483 132.394 24.1653 119.331 20.7471ZM211.648 72.6527L133.78 83.8509L161.556 276.981L188.279 273.138L205.036 132.945L205.807 126.488L268.018 133.924L264.695 110.828C252.344 110.343 243.897 107.836 227.666 116.664L222.42 119.518L219.13 114.533C209.49 99.9257 211.43 85.1149 211.648 72.6527ZM224.756 76.5312C224.625 86.0774 224.079 94.513 227.376 102.433C238.234 97.7014 247.103 97.0741 254.904 97.2895C246.228 89.2232 236.49 83.2143 224.756 76.5312ZM217.179 140.948L194.023 334.686L318.876 349.608L336.652 200.89C324.846 197.224 317.336 192.614 299.373 196.941L293.568 198.34L291.681 192.674C286.149 176.07 291.857 162.266 295.292 150.285L217.179 140.948ZM306.951 157.424C304.354 166.611 301.643 174.617 302.778 183.12C314.49 181.36 323.219 183.05 330.698 185.277C324.405 175.24 316.555 166.915 306.951 157.424Z"
              fill="white"/>
          </svg>
        </div>
        <div style="flex:1;background-color: white">
          <div style="font-size: 24px;font-weight: bold;margin-left: 50px;margin-top: 20px;">文件上传</div>
          <div class="upload-config-box">
            <div style="width:120px;font-size: 18px;font-weight: 600;" align="center">
              文件要求
            </div>
            <div style="flex:1;padding: 0 20px;">
              <el-row>
                <el-col
                  v-for="(item,index) in (handleCheckUnlimited()?configListUnlimited:configList)"
                  :span="index % 2 === 0 && configList.length === (index+1) ? 24 : 12"
                  :key="index"
                  style="height:30px;display: flex;align-items: center"
                >
                  <span> {{ (index + 1) + '. ' + item }} </span>
                </el-col>
              </el-row>
            </div>
          </div>

          <div style="padding: 0 40px;display: flex; align-items: center;justify-content: space-between;">
            <div style="display: flex; align-items: center;">
              <el-select
                v-model="queryParams.areaPermission"
                placeholder="请选择上传国家"
                multiple
                filterable
                size="small"
                style="margin: 10px"
                @change="areaSelectChange">
                <el-option
                  v-for="(item,index) in areasOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>

              <el-upload
                ref="uploadFileButton"
                style="width:fit-content"
                action
                accept=".pdf, .xls, .xlsx, .doc, .docx"
                :http-request="uploadFile"
                multiple
                :auto-upload="false"
                :show-file-list="false"
                :style="isUploading?'pointer-events: none':''"
                :disabled="isUploading"
                :limit="handleCheckUnlimited() ? 100 : 5"
                :on-change="handleChange"
                :on-exceed="handleExceed"
                :file-list="fileList">
                <el-button
                  slot="trigger"
                  style="background-color: #2D9CDB;border-color: #2D9CDB;"
                  size="small"
                  icon="el-icon-plus"
                  class="upload-button-animation"
                  :disabled="isUploading"
                  type="primary">
                  选取文件
                </el-button>
              </el-upload>
              <el-button
                style="margin-left: 10px;background-color: #3281ED;border-color: #3281ED;"
                size="small"
                type="success"
                :icon="isUploading ? 'el-icon-loading' : 'el-icon-upload'"
                :disabled="isUploading || uploadList.length < 1"
                class="upload-button-animation"
                :style="isUploading || uploadList.length < 1 ? 'cursor: not-allowed;background-color: #A0C0EB;border-color: #A0C0EB;':''"
                @click="submitUpload">
                上传文件
              </el-button>
              <el-select
                v-model="queryParams.rolePermission"
                placeholder="请选择用户"
                multiple
                size="small"
                style="margin: 10px"
              >
                <el-option
                  v-for="(item,index) in rolePermissionOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </div>
            <el-checkbox v-model="isOverwrite" label="重名覆盖" style="margin-left:10px;"></el-checkbox>
          </div>
          <div style="width:100%;margin-top:20px;padding:0 40px;">
            <div v-if="uploadList.length<1" style="width:100%;text-align: center;color: #888888">暂无文件</div>
            <div style="width: 100%;height:40px;border-bottom:1px solid #eeeeee;display:flex;align-items:center;"
                 v-for="(item,index) in uploadList"
                 :key="index"
            >
              <div style="width:20px;font-size: 16px;"><i class="el-icon-document"></i></div>
              <div style="flex:1;">{{ item.name }}</div>
              <div v-if="!isUploading" style="width:20px;" class="upload-file-close"
                   @click="handleDeleteUpload(item,index)"><i class="el-icon-circle-close"></i></div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import {
  delFile,
  fileUpload,
  listFile,
  updateFile,
  editFile,
  addFile,
  exportFile,
  Trained
} from '@/api/data/uploadFile'
import {getToken} from '@/utils/auth';
import {deptTreeSelect} from "@/api/system/user";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {listDept} from "@/api/system/dept";
import {checkPermi} from "../../utils/permission";
import {checkIsTrained} from "@/api/data/uploadFile";

export default {
  name: 'ChatGPT',
  components: {Treeselect},
  data() {
    return {
      upfileList: [],

      // baseUrl: process.env.VUE_APP_UP_URL,
      // baseUrl: 'http://10.10.101.233:5000',
      isError: false,
      isSend: false,
      loading: false,

      editDialogVisible: false,
      editForm: [{ // 表单数据
        areaPermission: [],
        rolePermission: []
      }],
      editId: null, // 编辑文件的ID

      fileList: [], //上传文件列表
      maxLength: 0,
      uploadfilelist: [],
      fileListlength: 0,
      uptimecount: 0,
      uploadVal: false,
      checkForm: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        originalFileName: null,
        id: null,
        deptId: null,
        userId: null,
        suffix: null,
        createTime: null,
        blobFileName: null,
        blobUrl: null,
        startTime: null,
        endTime: null,
        trainStartTime: null,
        trainEndTime: null,
        areaPermission:[],//国家
        rolePermission:[],//用户
      },
      filesLimit: 5,
      total: 0,
      upload: {
        url: process.env.VUE_APP_BASE_API + '/blob/upload/upload',
      },

      iframeInfo: {},
      types: ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx'],
      multipleSelection: [],

      uploadNum: 0,
      training: false,
      uploadText: '正在上传...',
      uploadState: 20,

      // uploadDeptId: this.$store.state.user.deptId,
      // uploadAreaPermission: this.$store.state.user.areaPermission,
      // uploadRolePermission:this.$store.state.user.rolePermission,
      customColors: [
        {color: '#f56c6c', percentage: 20},
        {color: '#e6a23c', percentage: 40},
        {color: '#5cb87a', percentage: 60},
        {color: '#1989fa', percentage: 80},
        {color: '#6f7ad3', percentage: 100}
      ],

      offerTypes: [
        {
          label: 'PDF',
          value: 'pdf',
        },
        {
          label: 'Excel文件（xlsx）',
          value: 'xlsx',
        },
        {
          label: 'Excel文件（xls）',
          value: 'xls',
        },
      ],

      uploadTime: [],
      trainTime: [],

      deptOptions: [],
      uploadList: [],
      configListUnlimited: ['单个文件大小不超过50MB', '文件名不得包含特殊字符', '当前仅支持 pdf xlsx xls 文件，doc/docx 文件上传后会自动转为pdf'],
      configList: ['单个文件大小不超过 5MB，文件个数不超过5个', '文件名不得包含特殊字符', '当前仅支持 pdf xlsx xls 文件，doc/docx 文件上传后会自动转为pdf'],
      isUploading: false,
      columnSettingVal: false,
      defaultShow: true,
      columnOptions: [
        {
          label: '上传部门',
          prop: 'deptName',
          width: '20%',
          show: true,
        }, {
          label: '上传人',
          prop: 'userName',
          width: '20%',
          show: true,
        }, {
          label: '文件类型',
          prop: 'suffix',
          width: '10%',
          show: true,
        },
        {
          label: '上传时间',
          prop: 'createTime',
          width: '20%',
          show: true,
        },
        {
          label: '训练时间',
          prop: 'trainTime',
          width: '20%',
          show: true,
        },
        //增加表格列
        {
          label:'国家',
          prop:'areaPermission',
          width:'20%',
          show:true,
        },
        {
          label:'用户',
          prop:'rolePermission',
          width:'20%',
          show:true,
        },
      ],

      statusOptions: [
        {
          label: '未训练',
          value: 'untrained'
        },
        {
          label: '训练中',
          value: 'training'
        },
        {
          label: '已训练',
          value: 'success'
        },
        {
          label: '训练失败',
          value: 'error'
        },
        {
          label: '删除中',
          value: 'deleting'
        }
      ],

      //国家
      areasOptions: [
        {
          label: 'China',
          value: 'China'
        },
        {
          label: 'United Kingdom',
          value: 'United Kingdom'
        },
        {
          label: 'United States',
          value: 'United States'
        },
        // {
        //   label: 'Indian',
        //   value: 'Indian'
        // },

      ],
      // //用户
      // rolePermissionOptions: [
      //   {
      //     label: '游客',
      //     value: '游客'
      //   },
      //   {
      //     label: '安装商',
      //     value: '安装商'
      //   },
      //   {
      //     label: '售后服务',
      //     value: '售后服务'
      //   },
      //   {
      //     label: '内部研发',
      //     value: '内部研发'
      //   },
      //   {
      //     label: '大客户',
      //     value: '大客户'
      //   },
      //   {
      //     label: '终端用户',
      //     value: '终端用户'
      //   },
      //   {
      //     label: '分销商',
      //     value:'分销商'
      //   }
      // ],
      rolePermissionOptions: [
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
      waitingTraining: null,
      isOverwrite: false,
    }
  },
  mounted() {
    if (localStorage.getItem('uploadFileColumn')) {
      this.columnOptions = JSON.parse(localStorage.getItem('uploadFileColumn'));
      if (this.columnOptions.length < 6) {
        this.columnOptions = [
          {
            label: '上传部门',
            prop: 'deptName',
            width: '20%',
            show: true,
          }, {
            label: '上传人',
            prop: 'userName',
            width: '20%',
            show: true,
          }, {
            label: '文件类型',
            prop: 'suffix',
            width: '10%',
            show: true,
          },
          {
            label: '上传时间',
            prop: 'createTime',
            width: '20%',
            show: true,
          },
          {
            label: '训练时间',
            prop: 'trainTime',
            width: '20%',
            show: true,
          },
          //增加表格列
          {
            label:'国家',
            prop:'areaPermission',
            width:'20%',
            show:true,
          },
          {
            label:'用户',
            prop:'rolePermission',
            width:'20%',
            show:true,
          },
        ];
      }
    }
    // this.getTreeselect();
    this.getDelay();
    let that = this;
    window.addEventListener('resize', function () {
      let myDiv = document.getElementById('searchDiv');
      myDiv.style.height = 'min-content';
      that.$nextTick(() => {
        myDiv.style.height = 'fit-content';
      })
    });
    //this.getDeptOption();
  },
  methods: {

    getDelay() {
      this.waitingTraining = setInterval(() => {
        this.getList();
      }, 60 * 1000)
    },
    selectable(row, index) {
      return row.trainStatus !== 'training';
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    handleStartTraning() {

      checkIsTrained().then(response => {
        if (response.data) {

          if (this.multipleSelection.length < 1) {
            this.$message.warning('请选择需要训练的文件');
            return;
          }

          let list = this.multipleSelection.map(item => {
            let modifiedRegionList = item.areaPermission.split(',').join(';');
            return {
              id: item.id,
              name: item.blobFileName,
              suffix: item.suffix,
              blobUrl: item.blobUrl,
              regionList: modifiedRegionList,
              roleList: item.rolePermission
            };
          });

          //this.training = true;
          //this.uploadState = 0;
          let data = {
            list: list,
            // deptId: this.$store.state.user.deptId,
            // userId: this.$store.state.user.userId,
            // trainType: 'Embedding',
          }
          console.log(data);
          this.multipleSelection = [];
          this.$refs.fileTable.clearSelection();
          this.$message.success('开始训练');
          setTimeout(() => {
            this.getList();
          }, 3 * 1000);

          Trained(data).then(() => {
            if (response.data.state === 'training') {
              // 请求成功时的处理逻辑
            } else {
              // this.$message.error(response.data.msg);
            }
        }).catch(() => {
            // this.$message.error(response.data.msg);
        });

        }
        else {
          this.$message.warning('已有文件正在训练，请耐心等待...')
        }
      })
    },
    formatText(percentage) {
      switch (percentage) {
        case 0:
          this.uploadText = '正在进行预处理';
          return '等待中';
        case 20:
          this.uploadText = '正在上传至blob';
          return `${percentage}%`;
        case 40:
          this.uploadText = '正在更新索引';
          return `${percentage}%`;
        case 60:
          this.uploadText = '正在更新技能集';
          return `${percentage}%`;
        case 80:
          this.uploadText = '正在更新索引器';
          return `${percentage}%`;
        case 100:
          this.uploadText = '处理完成';
          return `${percentage}%`;
        default:
          return `${percentage}%`;
      }
    },
    getRowKey(row) {
      return row.id;
    },
    //查询表单
    getList() {
      this.loading = true;
      var queryParams = JSON.parse(JSON.stringify(this.queryParams));
      if(queryParams.rolePermission){ queryParams.rolePermission = this.queryParams.rolePermission.join(";");}

      if(queryParams.areaPermission){queryParams.areaPermission = this.queryParams.areaPermission.join(";");}
      listFile(queryParams).then((res) => {
        this.upfileList = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    //搜索按钮
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    //重置按钮
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        originalFileName: null,
        id: null,
        deptId: null,
        userId: null,
        suffix: null,
        createTime: null,
        blobFileName: null,
        blobUrl: null,
        startTime: null,
        endTime: null,
        trainStartTime: null,
        trainEndTime: null,
        areaPermission:null,
        rolePermission:[],
      }
      this.uploadTime = null;
      this.trainTime = null;
      this.handleQuery();
    },
    //返回主界面
    goto() {
      this.$router.push('/');
    },
    handleDelete(row) {
      const ids = row.id
      this.$confirm('此操作将永久删除该文件，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancleButtonText: '取消',
        type: 'warning'
      }).then(() => {

        setTimeout(() => {
          this.getList();
        },1 * 1000);

        // updateFile({id: ids, trainStatus: 'deleting', trainDomesticStatus: 'deleting'}).then(res => {
        //   this.getList();
        // })

        row.isDeleting = true;
        this.multipleSelection = [];
        this.$refs.fileTable.clearSelection();
        delFile(ids).then(() => {
          this.getList();
          this.$message.success("删除成功");
        })
      }).catch(() => {
        row.isDeleting = false;
      });
    },
    showEditDialog(row) {
      this.editDialogVisible = true;
      this.editId = row.id;
      this.editForm = {
        areaPermission: row.areaPermission.split(';'),
        rolePermission: row.rolePermission.split(';')
      };
    },
    saveEditForm() {
      // 获取表单数据
      const formData = {
        id:this.editId,
        areaPermission: this.editForm.areaPermission.join(';'),
        rolePermission: this.editForm.rolePermission.join(';'),
      };
      editFile(formData).then(response => {
        if (response.code === 200) {
          this.$message.success('保存成功');
          this.editDialogVisible = false;
          this.getList();
        } else {
          this.$message.error('保存失败');
        }
      }).catch(error => {
        this.$message.error('保存失败：' + error.message);
      });
    },
    handleUpload() {
      this.uploadList = [];
      this.isOverwrite = false;
      this.uploadVal = true;
    },
    handleDeleteUpload(item, index) {
      this.uploadList.splice(index, 1);
      this.$refs.uploadFileButton.handleRemove(item);
    },
    submitUpload() {
      this.$refs.uploadFileButton.submit();
    },
    areaSelectChange(value){
      if (value.length === 0) {
        this.queryParams.areaPermission = this.areasOptions.map(item => item.value);
      }
    },
    uploadFile(param) {
      const formData = new FormData()
      formData.append('file', param.file);

      //formData.append('deptId', this.uploadDeptId);
      formData.append('areaPermission', this.queryParams.areaPermission);
      formData.append('rolePermission',this.queryParams.rolePermission);
      formData.append('isUpdate', this.isOverwrite ? 'yes' : 'no');

      let originalFileName = param.file.name;

      this.isUploading = true;

      fileUpload(formData).then(res => {
        if (res.code === 200) {
          this.$message.success(originalFileName + ' 上传成功');
        } else {
          this.$message.error(originalFileName + ': ' + res.msg)
        }
        this.uploadNum += 1;
        console.log(this.uploadNum, this.uploadList)
        if (this.uploadNum === this.uploadList.length) {
          this.uploadComplete();
        }
      }).catch(err => {
        this.$message.error(originalFileName + ' 上传失败')
        this.uploadNum += 1;
        if (this.uploadNum === this.uploadList.length) {
          this.uploadComplete();
        }
      })
    },
    uploadComplete() {
      this.fileListlength = 0;
      this.uploadNum = 0;
      this.isUploading = false;
      this.uploadVal = false;
      this.$refs.uploadFileButton.clearFiles();
      this.getList();
    },
    handleChange(file, fileList) {
      this.fileListlength = fileList.length;
      this.uploadList = [];
      let size = this.handleCheckUnlimited() ? 50 : 5;

      fileList.forEach(item => {
        if (item.size > 1024 * 1024 * size) {
          this.$message.error(item.name + ' 添加失败，单个文件大小不能超过 '+size+'M',);
          this.$refs.uploadFileButton.handleRemove(file);
        } else {
          this.uploadList.push(item);
        }
      })
      console.log(this.uploadList, fileList, file)
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 ${this.filesLimit} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    handleDownload(row) {
      this.download('blob/upload/download?id='+row.id, {
        id: row.id,
      }, row.originalFileName+'.'+row.suffix);/*

      const url = row.blobUrl; // 文件的URL地址
      const link = document.createElement('a');
      link.href = url;
      link.download = row.originalFileName; // 设置下载文件的名称
      document.body.appendChild(link);
      link.click();*/
    },
    //导出
    handleExport() {
      this.download('gpt/userblob/export', {
        ...this.queryParams.startTime,
        ...this.queryParams.endTime,
        ...this.queryParams.trainEndTime,
        ...this.queryParams.trainStartTime,
      }, `contractinfo_${new Date().getTime()}.xlsx`)
    },
    //后端未传入数据
    handleSizeChange(val) {
      this.queryParams.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val;
      this.getList();
    },
    handleSelectUploadTime(val) {
      if (val) {
        this.queryParams.startTime = val[0];
        this.queryParams.endTime = val[1];
        this.handleQuery();
      } else {
        this.queryParams.startTime = null;
        this.queryParams.endTime = null;
        this.handleQuery();
      }
    },
    handleSelectTrainTime(val) {
      if (val) {
        this.queryParams.trainStartTime = val[0];
        this.queryParams.trainEndTime = val[1];
        this.handleQuery();
      } else {
        this.queryParams.trainStartTime = null;
        this.queryParams.trainEndTime = null;
        this.handleQuery();
      }
    },
    handleChangeColumn(val) {
      localStorage.setItem('uploadFileColumn', JSON.stringify(this.columnOptions));
    },
    handleCheckUnlimited(){
      if (this.$auth.hasPermi("gpt:upload:limited")){
        this.filesLimit = 5;
        return false;
      } else {
        this.filesLimit = 100;
        return true;
      }
    }
  },
  created() {
    this.getList();
  },
  editDialogClosed(done){
    this.$refs.editFormRef.resetFields()
  },
  destroyed() {
    clearInterval(this.waitingTraining);
    this.waitingTraining = null;
  }
}

</script>
<style lang="scss" scoped>
.upload-button-animation{
  transition: all 0.3s ;
}
.upload-button-animation:hover{
  transform: translateY(-2px);
  box-shadow: 0 2px 6px #00000036;
}

.button-primary {
  background-color: #6093ff;
  color: #fff;
  border: none;
  width: 90px;

  &:hover {
    background-color: #92b4fd;
  }
}

.button-reset {
  width: 90px;
}

.loadtable {
  background-color: #ffffff21;
  border-radius: 10px;
  margin-right: 50px;
  margin-top: 20px;
}

.uploadaside {
  background-color: #ffffff21;
  border-radius: 10px;
  margin-left: 50px;
  margin-right: 50px;
  margin-top: 20px;
}

.redDot {
  color: #d33737;
}

.left-controller {
  margin: 20px;
  padding: 10px;
  background-color: #ffffff80;
  border: 1px solid #ffffff;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
  display: flex;
  align-items: center;
}

.right-table {
  margin: 20px;
  margin-top: 0;
  padding: 10px;
  background-color: #ffffff80;
  border: 1px solid #ffffff;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  width: calc(100% - 40px);
}

.card-button {
  height: 120px;
  width: 140px;
  border-radius: 6px;
  border: 1px solid rgba(140, 130, 134, 0.75);
  box-shadow: 0 2px 6px #00000024;
  position: relative;
  cursor: pointer;
  transition: box-shadow 0.3s, transform 0.3s;
}

.card-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 4px #00000040;
}

.card-button-text {
  position: absolute;
  top: 10px;
  left: 10px;
  color: white;
  font-size: 18px;
  font-weight: 600;
}

.upload-dialog {
  display: flex;
}

.dialog-background {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  display: flex;
}

.upload-config-box {
  width: calc(100% - 80px);
  margin: 30px 40px;
  background-color: #F2F5FA;
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.upload-file-close {
  color: #999093;
  font-size: 16px;
  transition: color 0.3s;
}

.upload-file-close:hover {
  color: #484848;
  cursor: pointer;
}

.Card {
  background-color: #ffffff63;
  border-radius: 10px;
}

.right-table ::v-deep .el-table,
.right-table ::v-deep .el-table__expanded-cell {
  background-color: #ffffff20;
}

.right-table ::v-deep .el-table__fixed-right {
  background-color: #ffffffd8;
}

/* 表格内背景颜色 */
.right-table ::v-deep .el-table th,
.right-table ::v-deep .el-table tr,
.right-table ::v-deep .el-table td {
  background-color: #ffffff20 !important;
}

::v-deep .el-table__body-wrapper::-webkit-scrollbar {
}

// 滚动条的滑块
::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
  background-color: #d0d0d040;
  border-radius: 3px;
}

::v-deep {
  .el-upload {
    width: 100%;
  }

  .el-upload-dragger {
    width: 100%;
    height: 160px;
    background-color: #ffffff70;
  }

  .el-table {
    background-color: #ffffff50;
  }

  .el-table__expanded-cell {
    background-color: transparent !important;
  }

  .el-table th,
  .el-table tr,
  .el-table td {
    background-color: transparent !important;
  }

  .inputStyle .el-input__inner {
  }

  .inputDatePicker {
    width: 70%;
  }

  .inputDatePicker .el-date-editor .el-range-input {
  }

  /*
    .el-pagination.is-background .el-pager li:not(.disabled).active {
      background-color: #78dbd7;
    }*/

}

.back {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.clear-button {
  margin-right: 12px;
  color: #909399;
  cursor: pointer;
  transition: text-shadow 0.3s, color 0.3s;
}

.clear-button:hover {
  text-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  color: #606266;
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
  radial-gradient(closest-side, #bdf6be, #b0e0ed00),
  radial-gradient(closest-side, #e8d6bb, #ccdcad00);
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


.loading-text {
  font-size: 28px;
  font-weight: 600;
  height: 60px;
  color: #FFF;
  text-shadow: #20202080 0 0 4px;
  position: absolute;
  left: 0;
  top: calc(50% + 140px);
  right: 0;
  margin: auto;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.empty-logo {
  width: 240px;
  height: 240px;
  position: absolute;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  margin: auto;
  z-index: 1000;
  transition: transform 3s ease-in-out, text-shadow 3s;
}

.empty-logo:hover {
  transform: translateY(-1em) scale(1.25) rotate(-30deg);
  transition: transform 1.5s ease-in-out;
  text-shadow: 0 2px 4px #00000080;
}

.loading {
  width: 240px;
  height: 240px;
  position: absolute;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  margin: auto;
  z-index: 1000;
  text-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  animation: 4s loading ease-in-out infinite;
}

@keyframes loading {
  0% {
    transform: rotate(0deg);
  }
  30% {
    transform: rotate(180deg +10deg);
  }
  40% {
    transform: rotate(180deg - 8deg);
  }
  50% {
    transform: rotate(180deg);
  }
  80% {
    transform: rotate(360deg+10deg);
  }
  90% {
    transform: rotate(360deg - 8deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

::v-deep .el-dialog {
  display: flex;
  flex-direction: column;
  margin: 0 !important;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  max-height: calc(100% - 30px);
  max-width: calc(100% - 30px);
}

</style>
