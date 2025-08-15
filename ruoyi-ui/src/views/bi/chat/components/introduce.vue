<template>
  <div style="padding: 20px 50px;height: 100%;" class="flex-layout column">
    <div class="flex-layout align-center" style="gap: 6px;">
      <svg-icon icon-class="kb-title" style="font-size: 20px;"/>
      <span class="introduce-sub-title">{{ $t('privateDomain.answerQuestions') }}</span>
    </div>
    <div style="margin-top: 10px;" class="flex-layout align-center start-end">
      <span class="introduce-title text-over">{{ $t('privateDomain.answerVariousProfessional') }}</span>

      <div>
        <el-button @click="showType='database'" class="blue" icon="el-icon-s-data">{{ $t('chatBI.selectDatasource') }}</el-button>
        <el-button @click="showType='excel'" class="purple" icon="el-icon-upload">{{ $t('chatBI.uploadFile') }}</el-button>
      </div>
    </div>

    <el-card class="border flex-1" style="margin-top: 20px;border-radius: 8px;"
             :body-style="{
      height: '100%',
      display: 'flex',
      flexDirection: 'column',
    }">
      <div class="main-title pd-v16">
        <span v-if="showType==='database'">{{ $t('chatBI.dataSourceSelect') }}</span>
        <span v-else-if="showType==='excel'">{{ $t('chatBI.uploadFile') }}</span>
      </div>
      <excel-upload style="height: 100%;" v-show="showType === 'excel'" @submit="handleSubmit"/>
      <database-choose :data-source="dataSourceList" v-model="datalist" style="height: 100%;" v-show="showType === 'database'" @submit="handleSubmit"/>
    </el-card>
  </div>
</template>
<script>
import ExcelUpload from "@/views/bi/chat/components/excelUpload.vue";
import DatabaseChoose from "@/views/bi/chat/components/databaseChoose.vue";
import {selectDataSource} from "@/api/bi/chat/select";

export default {
  name: "BIIntroduce",
  components: {DatabaseChoose, ExcelUpload},
  props: {
    dataSourceList: {
      type: Array,
      default: () => [],
    }
  },
  data() {
    return {
      datalist: [],
      showType: 'database',
    }
  },
  methods: {
    handleSubmit(type, data) {
      this.$emit('submit', type, data);
    },
  }
}
</script>
<style scoped lang="scss">
.introduce-sub-title {
  font-weight: 600;
  font-size: 16px;
  color: #2C2C2C;
  line-height: 22px;
}

.introduce-title {
  font-weight: 400;
  font-size: 28px;
  color: #2C2C2C;
  line-height: 40px;
}

</style>
