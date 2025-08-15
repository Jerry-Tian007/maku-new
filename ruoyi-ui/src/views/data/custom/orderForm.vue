<template>
  <div class="app-main">
    <el-card>
      <el-form
        :model="orderForm"
        :rules="rules"
        ref="orderForm"
        style="width:100%"
      >
        <el-form-item
          label="主题"
          prop="title"
        >
          <el-input
            v-model="orderForm.title"
            placeholder="Summary"
          ></el-input>
        </el-form-item>
        <el-form-item label="内容描述" prop="description">
          <el-input
            v-model="orderForm.description"
            type="textarea"
            placeholder="请详述具体问题，提供问题单据号/报错截图/相关附件等"
            :rows="4"
            resize="none"
          ></el-input>
        </el-form-item>
        <el-form-item label="BI分类" prop="biCategory" v-if="isBI" :rules="[
            { required: true, message: '请选择BI分类', trigger: 'change' }]">
          <el-select
            v-model="orderForm.biCategory"
            placeholder="请选择"
          >
            <el-option
              v-for="(item,index) in biflOptions"
              :key="index"
              :label="item.label"
              :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="紧急度" prop="emergencyDegree">
          <el-select
            v-model="orderForm.emergencyDegree"
            placeholder="请选择"
          >
            <el-option
              v-for="(item,index) in emergencyDegrees"
              :key="index"
              :label="item.label"
              :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="紧急原因" prop="emergencyCause" v-if="orderForm.emergencyDegree==='2'"
                      :rules="[
            { required: true, message: '请输入紧急原因', trigger: 'blur' }
          ]">
          <el-input
            v-model="orderForm.emergencyCause"
            placeholder="请填写紧急原因"
            type="textarea"
            :rows="4"
            resize="none"
          ></el-input>
        </el-form-item>
        <el-form-item label="附件" prop="files" class="last">
        </el-form-item>
        <el-upload
          class="upload-demo"
          ref="upload"
          action=""
          :on-change="handleChange"
          :on-remove="handleRemove"
          :limit="3"
          :on-exceed="handleExceed"
          :file-list="fileList"
          multiple
          :auto-upload="false">
          <el-button plain :disabled="fileList.length>=3" slot="trigger" size="mini" plain type="primary"
                     icon="el-icon-plus">
            选择文件
          </el-button>
          <span class="el-upload__tip" style="margin-left: 16px;font-size:14px;color:#909399;">
           （限上传共3个文件，且单个文件大小不超过10MB）
          </span>
          <!--            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
        </el-upload>
      </el-form>
      <div style="width: 100%;margin-top: 16px;" align="right">
        <el-button type="primary" size="small" @click="submitForm">提交</el-button>
      </div>


      <el-dialog :visible.sync="showResult" width="300px" append-to-body :show-close="false" :close-on-click-modal="false"
                 :close-on-press-escape="false">
        <div slot="header"></div>
        <el-result icon="success" title="工单已提交" subTitle="">
          <template slot="extra">
            <el-button size="small" type="primary" @click="handleJump()">确定</el-button>
          </template>
        </el-result>
      </el-dialog>

    </el-card>
  </div>

</template>

<script>
import axios from "axios";

export default {
  name: "OrderForm",
  props: {
    isBI: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ''
    },
    content: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      orderForm: {},
      showResult: false,
      basicInfo: {},
      rules: {
        head: [
          {required: true, trigger: "blur", message: "请输入主题"}
        ],
        desc: [
          {required: true, trigger: "blur", message: "请输入内容描述"}
        ],
        emergencyDegree: [
          {required: true, trigger: "change", message: "请输入紧急度"}
        ],
      },
      emergencyDegrees: [
        {label: '高', value: '2'},
        {label: '中', value: '1'},
        {label: '低', value: '0'}
      ],
      biflOptions: [
        {label: 'B-可视化(Tableau、Power BI等)', value: '2'},
        {label: 'BI-数据湖', value: '1'},
        {label: 'BI-BW&HANA', value: '0'}
      ],
      fileList: [],
    }
  },
  mounted() {
    this.orderForm = {
      title: this.title,
      description: this.content
    }
  },
  methods: {
    handleRemove(file, fileList) {
      this.fileList = fileList;
    },
    handleChange(file, fileList) {
      let size = 10;
      this.fileList = [];
      fileList.forEach(item => {
        if (item.size > 1024 * 1024 * size) {
          this.$message.error(item.name + ' 添加失败，单个文件大小不能超过 ' + size + 'M',);
          this.$refs.upload.handleRemove(file);
        } else {
          this.fileList.push(item);
        }
      })
      console.log(file, fileList, this.fileList);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    submitForm() {
      this.$refs["orderForm"].validate((valid) => {
        if (valid) {
          let query = JSON.parse(localStorage.getItem('query') || '{}');
          delete query.businessId
          delete query.identityId
          delete query.identityName

          let data = {
            ...this.orderForm,
            ...query,
            username: this.$store.state.user.userId + '',
            attachments: [],
          };
          let num = 0;
          this.fileList.forEach((item, index) => {
            let reader = new FileReader()
            reader.readAsDataURL(item.raw);
            let that = this;
            reader.onload = function () {
              data.attachments.push({url: reader.result.split(',')[1], filename: item.name});
              num+=1;
              if(num===that.fileList.length){
                console.log(data);
                let headers = {
                  "source_sys_id": "AIBOT",
                  "target_sys_id": "OA",
                  "service_name": "CreatJira",
                  "trans_id": "123123",
                  "biz_event": "",
                  "submit_time": '',
                }
                axios.put(/*process.env.VUE_APP_OATEST_URL+*/'https://esbapit.wilmar.cn/v1/oa-api/notification/jira', data,
                  {headers: headers}).then(res => {
                  console.log(res)
                  if (res.data.status) {
                    that.showResult = true;
                  }
                })
              }
            }
          });

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    handleJump(){
      window.location.replace(' https://oauat.wilmar.cn/mobilemode/mobile/view.html?appHomepageId=466#&page_466');
    }
  },
}
</script>
<style lang="scss" scoped>
::v-deep .last.el-form-item--medium.el-form-item {
  margin-bottom: 4px;
}

::v-deep .el-upload-list__item {
  line-height: 2;
}
</style>
