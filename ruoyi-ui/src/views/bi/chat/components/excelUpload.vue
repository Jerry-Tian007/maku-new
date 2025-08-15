<template>
  <div class="upload-area flex-layout" style="overflow:hidden;" v-loading="loading">
    <div class="pd-v20 pd-h16 flex-layout align-center shrink" align="center"
         style="overflow: hidden auto;background-color: #fafbfd">
      <div>
        <el-upload
          class="auto-width"
          drag
          :accept="accept.join(',')"
          :before-upload="beforeUpload"
          :show-file-list="false"
          :on-success="handleSuccess"
          :on-error="handleError"
          :disabled="uploadState === 'loading'"
          :action="baseUrl+'/chat-bi-file/upload'"
          :headers="{Authorization: 'Bearer ' + getToken()}"
        >
          <template v-if="uploadState==='wait'">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">{{ $t('chatBI.dragTheFile') }}<em>{{ $t('AllInOne.uploading') }}</em></div>
          </template>
          <template v-else-if="uploadState==='loading'">
            <i class="el-icon-loading icon-inside"></i>
            <div class="el-upload__text">{{ $t('chatBI.uploading') }}</div>
          </template>
          <template v-else-if="uploadState==='done'">
            <i class="el-icon-circle-check icon-inside"></i>
            <div class="el-upload__text">
              <span>{{ fileInfo.name }}</span>
              <div class="secondary-text fs-12" style="margin-top: 4px;">
                <span>
                  {{ fileInfo.suffix.toLocaleUpperCase() + ', ' + getSize(fileInfo.size) }}
                </span>
              </div>
            </div>
          </template>

          <div class="el-upload__tip" slot="tip">{{ $t('chatBI.uploadText') }}</div>
        </el-upload>
      </div>
    </div>

    <div class="shrink" style="opacity: 0.4;">
      <el-divider direction="vertical" class="m12 height-full"></el-divider>
    </div>

    <div class="flex-1 pd-h20 pd-v10 flex-layout column gap16 over-hidden" style="height: 100%;position:relative;">
      <div class="disable-overlay" v-show="uploadState !== 'done'"/>

      <div class="flex-1 over-auto pd-h10">
        <el-form
          :model="form"
          label-width="72px"
          style="width: 100%;"
          class="pd-v20 pd-h10">

          <el-form-item :label="$t('chatBI.tableName')">
            <template slot="label">
              <el-tooltip class="item" effect="dark" :content="tableNameHint" placement="top">
                <span>
                  <span>{{ $t('chatBI.tableName') }}</span>
                  <i class="el-icon-info regular-text" style="margin-left: 4px;"></i>
                </span>
              </el-tooltip>
            </template>
            <el-input v-model="form.tableName"></el-input>
          </el-form-item>

          <el-form-item :label="$t('chatBI.tableDescription')">
            <template slot="label">
              <el-tooltip class="item" effect="dark" :content="tableDescHint" placement="top">
                <span>
                  <span>{{ $t('chatBI.tableDescription') }}</span>
                  <i class="el-icon-info regular-text" style="margin-left: 4px;"></i>
                </span>
              </el-tooltip>
            </template>
            <el-input v-model="form.tableDescribe"></el-input>
          </el-form-item>
        </el-form>

        <el-table
          style="width: 100%;"
          :data="columnList">
          <Empty slot="empty" :description="$t('chatBI.noneData')"></Empty>
          <el-table-column
            type="index"
            :label="$t('chatBI.index')"
            align="center"
            width="60">
          </el-table-column>
          <el-table-column
            prop="fieldName"
            :label="$t('chatBI.rowName')"
            show-overflow-tooltip
            min-width="20">
            <template slot="header">
              <el-tooltip class="item" effect="dark" :content="nameHint" placement="top">
                <span>
                  <span>{{ $t('chatBI.rowName') }}</span>
                  <i class="el-icon-info regular-text" style="margin-left: 4px;"></i>
                </span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
            prop="fieldDescribe"
            :label="$t('chatBI.rowDescription')"
            min-width="80">
            <template slot="header">
              <el-tooltip class="item" effect="dark" :content="descHint" placement="top">
                <span>
                  <span>{{ $t('chatBI.rowDescription') }}</span>
                  <i class="el-icon-info regular-text" style="margin-left: 4px;"></i>
                </span>
              </el-tooltip>
            </template>
            <template slot-scope="scope">
              <el-input v-model="scope.row.fieldDescribe" prefix-icon="el-icon-edit"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div align="right">
        <el-button @click="handleSubmit" size="medium" type="primary">{{ $t('save') }}</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import {getToken} from "@/utils/auth";
import {saveFile} from "@/api/bi/chat/excel";

export default {
  name: 'ExcelUpload',
  data() {
    return {
      loading: false,
      nameHint: this.$t('chatBI.nameHint'),
      descHint: this.$t('chatBI.descHint'),
      tableNameHint: this.$t('chatBI.tableNameHint'),
      tableDescHint: this.$t('chatBI.tableDescHint'),

      form: {},

      columnList: [],

      uploadState: 'wait',

      fileInfo: {
        name: this.$t('test'),
        suffix: 'xlsx',
        size: 1024
      },
      file: null,

      baseUrl: process.env.VUE_APP_BASE_API,

      accept: ['.xlsx', '.xls']
    }
  },
  methods: {
    getToken,
    getSize(size) {
      return size > 1024 ? (size / 1024).toFixed(2) + 'MB' : size + 'KB';
    },
    beforeUpload(file) {
      this.uploadState = 'loading';
      let name = file.name;
      let suffix = name.split('.').pop();

      if (!this.accept.includes('.' + suffix)) {
        this.$message.warning(this.$t('chatBI.uploadError'));
        return false;
      }

      this.fileInfo = {
        name: name,
        suffix: suffix,
        size: file.size
      }
      return true;
    },
    handleSuccess(response, file, fileList) {
      if (response.code !== 200) {
        this.$message.error(response.msg);
        this.uploadState = 'wait';
        return;
      } else {
        let data = response.data[0];
        this.form = {
          tableName: data.tableName,
          tableDescribe: data.tableDescribe
        };
        this.columnList = data.fields;
        this.file = file;
        this.uploadState = 'done';
      }
    },
    handleError(err, file, fileList) {
      this.uploadState = 'wait';
      this.$message.error(this.$t('fileManager.uploadError'));
    },
    handleSubmit() {
      let list = [
        {
          tableName: this.form.tableName,
          tableDescribe: this.form.tableDescribe,
          fields: this.columnList
        }
      ]

      let formData = new FormData();
      // 二进制文件
      formData.append('file', this.file.raw);
      formData.append('list', JSON.stringify(list));

      this.loading = true;

      saveFile(formData).then(res => {
        if (res.code === 200) {
          this.$message.success(this.$t('chatBI.saveSuccess'));
          let fileInfo = {
            "id": res.data.id,
            "fileName": res.data.fileName,
            "fileSuffix": res.data.fileSuffix,
            "fileUrl": res.data.fileUrl,
            "dataSourceId": res.data.dataSourceId,
            "dataSourceTableId": res.data.dataSourceTableId,
          }
          let data = {
            fileInfo: fileInfo,
            tableInfo: list
          }
          this.$emit('submit', 'excel', data);
        } else {
          this.$message.error(this.$t('chatBI.saveError'));
        }
      }).catch(err => {
        this.$message.error(this.$t('chatBI.saveError'));
      }).finally(() => {
        this.loading = false;
      })

      /*      let data = {
              fileInfo: this.fileInfo,
              tableInfo: {}
            }
            this.$emit('submit', 'excel', data);*/
    }
  }
}
</script>
<style lang="scss" scoped>
.upload-area {
}
</style>
