<template>
  <div class="back" align="left">
    <div
      class="color-background"
      style="position: absolute;width:100%;height:100%;"
    />
    <div class="background">
      <div class="chat-background">
        <div class="content-card">
          <div style="position: relative;height: 100%;">
            <el-tabs v-model="activeName" stretch>
              <el-tab-pane label="最新总结" name="latest">
                <div class="summary" v-if="summaryList.length>0">
                  <div class="summary-title">最新总结</div>
                  <div class="summary-text">{{ summaryList[0].summary }}</div>
                  <div class="summary-time">{{ summaryList[0].createTime.replace('-','年').replace('-','月').replace(' ','日 ') }}</div>
                </div>
                <div v-else>
                  <el-empty :image-size="100"/>
                </div>
              </el-tab-pane>
              <el-tab-pane label="问答记录" name="list" lazy>
                <template v-if="summaryList.length>0 && summaryList[0].history">
<!--                  <div v-for="(item,index) in summaryList" :key="index" class="summary">
                    <div class="summary-title">{{item.createTime.replace('-','年').replace('-','月').replace(' ','日 ')}}</div>
                    <div class="summary-text">{{ item.summary }}</div>
                  </div>-->
                  <only-read :historyListString="summaryList[0].history"/>
                </template>
                <div v-else>
                  <el-empty :image-size="100"/>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import {identityChatgptsummary} from "@/api/chatGPT/chatgptsummary";
import OnlyRead from "./onlyRead";

export default {
  name: 'SummaryList',
  components: {OnlyRead},
  data() {
    return {
      iframe: '5562332',
      partnerId: this.$route.query.partnerid || this.$route.query.partnerId || 0,
      activeName: 'latest',
      summaryList: []
    }
  },
  activated: function () {
    if (this.$route.query.partnerid || this.$route.query.partnerId) {
      this.partnerId = this.$route.query.partnerid || this.$route.query.partnerId || 0;
      console.log(this.partnerId);
      this.getList();
    }
  },
  mounted() {
    this.partnerId = this.$route.query.partnerid || this.$route.query.partnerId;
    if(this.partnerId){
      console.log(this.partnerId);
      this.getList();
    }
  },
  methods: {
    getList(){
      identityChatgptsummary(this.partnerId).then(res=>{
        if(res.code===200){
          this.summaryList = res.rows;
        }
      })
    }
  },
  destroyed() {

  }
}

</script>


<style lang="scss" scoped>
.summary{
  padding: 16px;

  .summary-title{
    color: #303133;
    font-size: 18px;
    font-weight: 600;
  }

  .summary-text{
    color: #606266;
    margin-top: 6px;
    text-indent: 2em;
    line-height: 1.6em;
    font-size: 16px;
    font-weight: 500;
  }
  .summary-time{
    color: #606266;
    margin-top: 12px;
    text-align: right;
    font-size: 16px;
    font-weight: 500;
  }
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


::v-deep .el-tabs__content {
  position: absolute;
  width: 96%;
  top: 50px;
  bottom: 8px;
  left: 0;
  right: 0;
  margin: auto;
  overflow-y: auto;
  z-index: 1000;
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


.chat-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #ffffff40;
}

.content-card {
  width: 96%;
  height: 96%;
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: auto;
  padding: 30px;
  background-color: #ffffff40;
  border: 1px solid #ffffffc0;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
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
