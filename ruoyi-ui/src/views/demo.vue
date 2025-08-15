<template>
  <div class="back">
    <el-input v-model="iframe"/>
    <iframe :src="'http://localhost:9801/summary?parentid='+iframe" width="100%" height="100%" />
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Demo",
  data() {
    return {
      iframe: '123',
    }

  },
  mounted() {
  },
  methods: {
  },
}
</script>
<style lang="scss" scoped>
.btn_upload {
  position: absolute;
  left: 50%;
  top: 3%;
}

.card_1 {
  position: absolute;
  bottom: 8%;
  align-self: center;
  min-width: 400px;
  max-width: 800px;
  color: #364b6a;
  padding: 20px;
  font-size: 18px;
  box-shadow: 20px 20px 50px rgba(0, 0, 0, 0.5);
  border-radius: 15px;
  background: rgba(255, 255, 255, 0.1);
  justify-content: center;
  overflow: hidden;
  align-items: center;
  border-top: 1px solid rgba(255, 255, 255, 0.5);
  border-left: 1px solid rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(5px);
  word-break: break-all;
  word-wrap: break-word;
  /*但在有些场景中，还需要加上下面这行代码*/
  white-space: normal;
}

.back {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: url("../assets/images/demo.png");
  -moz-background-size: 100% 100%;
  background-size: 100% 100%;
}

.background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding-top: 50px;
}

.title-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  background-color: rgba(255, 255, 255, 0.5);
}

.button-background {
  position: absolute;
  top: 0px;
  right: 0;
  width: 50%;
  height: 100%;
  display: flex;
  background-color: rgba(255, 255, 255, 0.5);
  z-index: 900;
}

.chat-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  background-color: #ffffff40;
  z-index: 800;
}

.dialog-content {
  flex: 1;
  width: 100%;
  position: relative;
  overflow: auto;
}

.title {
  font-size: 20px;
  font-weight: 600;
  color: #000;
  margin: 10px;
  margin-bottom: 30px;
}

.box-card {
  overflow: auto; //10.20日添加，但是无效
}

#el-container {
  margin-top: 10px;

  ::v-deep .el-tabs__item.is-disabled {
    color: #000 !important;
  }
}

.content-card {
  width: 96%;
  min-height: 40px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
}

.card {
  margin-top: 3%;
  width: max-content;
  max-width: 28%;
  min-width: 28%;
  min-height: 300px;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
  background-color: #ffffffc0;
  border: 0px solid #202020;
  display: inline-block;
}

.card-text {
  width: 100%;
  font-size: 16px;
  padding-top: 6px;
  max-height: 400px;
  overflow-y: auto;
}

.file-tag-container {
  margin-top: 10px;
}

.question-tag-container {
}

.file-container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.dialog-container {
  position: relative;
  width: 100%;
  height: 100%;
  padding: 10px;
  overflow-y: auto;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 36px;
}

.handler {
  position: absolute;
  z-index: 1999;
  bottom: 0;
  top: 0;
  right: 0;
  margin: auto;
  width: 24px;
  height: 80px;
  border: 1.5px solid #dedede;
  border-right: 0px;
  border-radius: 4px;
  background-color: #ffffff40;
  display: flex;
  align-items: center;
}

.handler-inside {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.fade-enter-active,
.fade-leave-active {
  transition: width 0.5s;
}

.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */
{
  width: 100%;
}

//右侧列表出现与消失的伸缩动画
.slide-enter-active,
.slide-leave-active {
  transition: all 0.5s;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

::v-deep .el-card__header {
  padding: 16px 14px;
}

.icons {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  font-size: 18px;
  margin-bottom: 4px;
  cursor: pointer;
}

.input-bar {
  display: flex;
  align-items: center;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  padding: 8px 0px;
  min-height: 40px;
  margin: 0 30% 0 30%;
  transition: 0.5s;
}

.input-bar:focus-within {
  margin: 0 20% 0 20%;
  background: #fff;
  transition: 0.5s;
}

.clear-button-clear {
  margin-right: 12px;
  color: #ff6e74;
  cursor: pointer;
  transition: box-shadow 0.3s, color 0.3s;
  background-color: #FFFFFF80;
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #FFF;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04), 0 0 2px rgba(0, 0, 0, 0.02);
}

.clear-button-clear:hover {
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  color: #ff1c27;
}

.clear-button-clear-right {
  margin-bottom: 10px;
  color: #ff6e74;
  cursor: pointer;
  transition: box-shadow 0.3s, color 0.3s;
  background-color: #FFFFFF40;
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #FFF;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
}

.clear-button-clear-right:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04), 0 0 2px rgba(0, 0, 0, 0.02);
  color: #ff1c27;
}

.clear-button-tutorial {
  margin-bottom: 10px;
  color: #ffb76e;
  cursor: pointer;
  transition: box-shadow 0.3s, color 0.3s;
  background-color: #FFFFFF40;
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #FFF;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
}

.clear-button-tutorial:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04), 0 0 2px rgba(0, 0, 0, 0.02);
  color: #ff821c;
}

.clear-button-hide {
  margin-bottom: 10px;
  color: rgba(126, 146, 207, 0.68);
  cursor: pointer;
  transition: box-shadow 0.3s, color 0.3s;
  background-color: #FFFFFF40;
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #FFF;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
}

.clear-button-hide:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04), 0 0 2px rgba(0, 0, 0, 0.02);
  color: rgba(17, 39, 152, 0.64);
}

.tutorial-dialog ::v-deep .el-dialog {
  border-radius: 6px;
}

.tutorial-flex {
  display: flex;
  width: 100%;
  min-height: 100px;
}

.tutorial-left {
  width: 50%;
  flex: 1;
  padding: 6px 20px;
  background-color: #f8f8ef;
}

.tutorial-right {
  width: 50%;
  flex: 1;
  padding: 6px 12px;
  background-color: #fff4e2;
}

.tutorial-demo-title {
  width: 100%;
  margin-top: 12px;
  text-align: center;
  font-size: 18px;
  font-weight: 500;
  color: #606266;
}

.tutorial-demo-describe {
  width: fit-content;
  text-align: center;
  font-size: 16px;
  font-weight: 400;
  color: #999093;
  border-bottom: 1px solid #999093;
}

.clear-button-setting {
  margin-right: 12px;
  color: #909399;
  cursor: pointer;
  transition: box-shadow 0.3s, color 0.3s;
  background-color: #FFFFFF80;
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #FFF;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04), 0 0 2px rgba(0, 0, 0, 0.02);
}

.clear-button-setting:hover {
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  color: #606266;
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
  z-index: 800;
  transition: transform 3s ease-in-out, text-shadow 3s;
}

.empty-logo:hover {
  transform: rotate(360deg);
  transition: transform 3s ease-in-out;
  text-shadow: 0 2px 4px #00000080;
}

.input-bar ::v-deep .el-textarea__inner {
  border: 0;
  border-radius: 10px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
  font-size: 18px;
  line-height: 1.5em;
  background-color: rgba(255, 255, 255, 0);
}

.drawer-setting ::v-deep .el-textarea__inner {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
  font-size: 16px;
}

.control-card {
  width: 80%;
  height: 100%;
  background-color: #ffffff40;
  border-radius: 10px;
  border: 1px solid #ffffff;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  position: relative;
}

.tabs {
  position: absolute;
  width: calc(100% - 20px);
  height: calc(100% - 16px);

  left: 4px;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;

  flex: auto;
  display: flex;
  flex-direction: column;
}

::v-deep .el-tabs__content {
  flex-grow: 1;
  overflow-y: scroll;
}

::v-deep .el-textarea__inner {
  border: 1px solid #ffffff;
  border-radius: 8px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
  font-size: 14px;
}

.drawer-tabs ::v-deep .el-textarea__inner {
  border: 1px solid #ffffff;
  border-radius: 8px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
  font-size: 14px;
  background-color: #ffffff;
}

::v-deep .v-note-wrapper .v-note-panel .v-note-show .v-show-content,
.v-note-wrapper .v-note-panel .v-note-show .v-show-content-html {
  padding: 0px;
  width: 100%;
  min-width: 20px;
}

::v-deep .markdown-body p {
  margin-bottom: 0px;
  width: 100%;
}

::v-deep .type-select .el-input--medium .el-input__inner {
  height: 40px;
  border: none;
  width: 80px;
  padding: 0;
  text-align: center;
  color: #909399;
  font-size: 16px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
}

::v-deep .type-select-popper {
  text-align: center;
  font-size: 16px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
}

::v-deep .el-select .el-input .el-select__caret {
  display: none;
}

/* 定义滚动条高宽及背景
 高宽分别对应横竖滚动条的尺寸 */
::-webkit-scrollbar {
  width: 6px;
  height: 8px;
}

/* 定义滚动条轨道
内阴影+圆角 */
::-webkit-scrollbar-track {
  background-color: rgba(255, 255, 255, 0);
}

/* 定义滑块
内阴影+圆角 */
::-webkit-scrollbar-thumb {
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.5);
}


.drawer-setting::v-deep .el-drawer {
  width: 40%;
  height: 60%;
  margin-top: 20vh;
  margin-right: 4%;
  padding: 12px;
  box-shadow: 0 4px 8px #00000024, 0 2px 4px #0000001f;
  background-color: rgba(255, 255, 255, 0.88);
  border: 1px solid #FFF;
  border-radius: 6px;
}

.session-card {
  font-size: 16px;
  line-height: 30px;
  padding: 10px 12px;
  margin: 8px 6px;
  background-color: #ffffff60;
  border: 1px solid #FFF;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #606266;
}

.session-card:hover {
  cursor: pointer;
}

.session-delete {
  color: #888888;
}

.session-delete:hover {
  color: #000000;
  cursor: pointer;
}

.session-list {
  margin: 20px;
  background-color: #ffffff20;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border: 1px solid #FFF;
  border-radius: 10px;
}

.setting-container {
  width: 360px;
  transition: all 0.3s ease-in-out;
}

.setting-container.hide {
  width: 48px;
  flex: 0;
}

body {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
</style>
<style scoped>


:root {
  font-size: 15px;
}

/*.color-background {*/
/*  font-family: "Quicksand", sans-serif;*/
/*  margin: 0;*/
/*  min-height: 100vh;*/
/*  background-color: #cdd5ae;*/
/*  background-image: radial-gradient(*/
/*    closest-side,*/
/*    rgb(184, 223, 220),*/
/*    rgba(243, 11, 164, 0)*/
/*  ),*/
/*  radial-gradient(closest-side, #c8d8bd, #86fde800),*/
/*  radial-gradient(closest-side, #acc1b8, #acb6e500);*/
/*  background-size: 80vmax 80vmax, 70vmax 70vmax, 80vmax 80vmax;*/
/*  background-position: -20vmax -20vmax, 40vmax -10vmax, 0vmax 0vmax;*/
/*  background-repeat: no-repeat;*/
/*  animation: 10s movement linear infinite;*/

/*}*/

/*@keyframes movement {*/
/*  0%,*/
/*  100% {*/
/*    background-position: -50vmax -50vmax, 60vmax -30vmax, 10vmax 10vmax;*/
/*  }*/
/*  25% {*/
/*    background-position: -40vmax -40vmax, 50vmax -40vmax, 0vmax -20vmax;*/
/*  }*/
/*  50% {*/
/*    background-position: -40vmax -60vmax, 40vmax -30vmax, 10vmax 0vmax;*/
/*  }*/
/*  75% {*/
/*    background-position: -40vmax -30vmax, 50vmax -30vmax, 20vmax 0vmax;*/
/*  }*/
/*}*/

.hover_ball {
  position: absolute;
  bottom: 15%;
  align-self: center;
  width: auto;
  height: 130px;
  font-size: 20px;
  color: rgba(24, 24, 24, 0.6);
  border-radius: 50%;
  cursor: pointer;
  background-color: rgba(215, 208, 224, 0.15);
  font-family: "Montserrat", sans-serif;
  letter-spacing: 2px;
  font-weight: lighter;
  transition: 0.6s box-shadow;
  border: 1px solid #05335e;
}

.hover_ball:hover {
  box-shadow: 0 5px 35px 0 rgba(0, 0, 0, 0.1);
}
</style>
