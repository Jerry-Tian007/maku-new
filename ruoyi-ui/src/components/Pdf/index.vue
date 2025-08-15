<template>
    <iframe :src="src" width="100%" height="100%" style="width: 100%;flex:1;border:none;"/>
</template>

<script>
export default {
  name: 'PdfComponent',
  props: {
    url: {
      type: String,
      default: ''
    },
    page: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      src: ''
    }
  },
  watch: {
    url: function (newData, oldData) {
      this.getFile();
    }
  },
  mounted() {
    this.getFile();
  },
  methods: {
    getFile() {
      let url = this.url.replace('https://nygpt.blob.core.windows.net', process.env.VUE_APP_FILE_URL);
      let fileUrl =  '/build/pdf/web/viewer.html?file=' + encodeURIComponent(url);
      if(this.page){
        fileUrl += '#page=' + this.page;
      }
      this.src = fileUrl;
    },
  }
}
</script>
