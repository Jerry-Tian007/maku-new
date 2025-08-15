<template>
  <vue-office-excel
    :src="excel"
    style="height:100%;"
    @rendered="renderedHandler"
    @error="errorHandler"
    v-loading="loading"
    :options="{
        xls: isXls
      }"
  />
</template>

<script>
//引入VueOfficeExcel组件
import VueOfficeExcel from '@vue-office/excel'
//引入相关样式
import '@vue-office/excel/lib/index.css'
// import {downloadFile} from "@/api/data/uploadFile";

export default {
  name: "ExcelComponent",
  props: {
    url: {
      type: String,
      default: '',
    },
    isXls: {
      type: Boolean,
      default: true
    }
  },
  components: {
    VueOfficeExcel
  },
  data() {
    return {
      loading: false,
      options:{
        xls: false,
      },
      excel: ''//设置文档地址
    }
  },
  watch: {
    fileId: function (newData, oldData) {
      this.getFile();
    }
  },
  mounted() {
    this.getFile();
  },
  methods: {
    getFile() {

      this.$nextTick(()=>{
        // process.env.VUE_APP_FILE_URL
        this.excel = this.url.replace('https://nygpt.blob.core.windows.net', process.env.VUE_APP_FILE_URL);
      })
      /*if (this.fileId === 0) return;
      this.loading = true;

      downloadFile(this.fileId).then(res => {
        if(res.code===200){
          let url = res.data.blobUrl;
          axios.get(url, {responseType: 'blob'}).then(res => {
            this.loading = false;
            this.excel = res.data;
          }).catch(err => {
            this.$message.error('文件下载失败：' + err);
          });
        }
      })*/
    },
    renderedHandler() {
      this.$forceUpdate();
      console.log("渲染完成")
    },
    errorHandler() {
      console.log("渲染失败")
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .x-spreadsheet-sheet canvas {
  width: 100%;
}

</style>
