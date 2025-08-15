<template>
  <div class="app-container">
    <el-card>
      <el-tabs v-model="activeTab" class="no-margin">
        <el-tab-pane :label="$t('dataBase.dataBaseData')" name="database">
        </el-tab-pane>
        <el-tab-pane :label="$t('dataBase.excelData')" name="excel">
        </el-tab-pane>
      </el-tabs>

    </el-card>

    <data-base-page v-if="activeTab === 'database'"></data-base-page>

    <excel-data v-if="activeTab === 'excel'"></excel-data>
  </div>
</template>
<script>
import DataBasePage from "@/views/bi/dataBaseManager/components/database.vue";
import ExcelData from "@/views/bi/dataBaseManager/components/excel.vue";

export default {
  name: "DataBaseManager",
  components: {ExcelData, DataBasePage},
  data() {
    return {
      activeTab: 'database', // database, excel
    };
  },
  mounted() {

  },
  methods: {

  }
};
</script>
<style scoped lang="scss">

</style>
