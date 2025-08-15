<template>
  <div class="app-container full-screen">
    <div style="width: 100%;height:100%;overflow-y: auto;" class="flex-layout column gap10" v-show="!showSettings">
      <div class="flex-1" style="display: flex;overflow: hidden;min-height: 400px;">
        <div class="flex-1" style="overflow: hidden;height: 100%;width: 100%;">
          <el-row :gutter="10" :style="{ height: (botNum === 4 ? 'calc(50% - 5px)' : '100%') }">
            <el-col v-for="(bot, index) in bots" :key="index" :span="botNum === 4 ? 12 : 24 / botNum"
              :style="{ height: '100%', marginBottom: (botNum === 4 && index < 2 ? '10px' : '0') }">
              <chat-bot v-on:update:disableInput="sendDataDisableMet" :index="index" :bots="botNum" style="height: 100%;" ref="chatBot"
                v-on:update:modelType="handleModelTokenList" v-on:update:mainWindow="handleRemoveFile"
                v-on:update:referState="handleReferList" />
            </el-col>
          </el-row>
        </div>
        <div :class="{ 'refer_list_div': true, 'hidden': !showReferState }">
          <div style="flex:0.7;display: flex;align-items: center;justify-content: space-between;">
            <span style="font-size: 15px; color: gray;">
              {{ $t('AllInOne.referenceSource') }} · {{ referList.length }}
            </span>
            <svg-icon icon-class="colse_refer" style="cursor: pointer;" @click="handleReferList([])" />
          </div>
          <div style="flex: 10; display: flex;flex-direction: column;gap: 12px;overflow-y: auto;cursor: pointer;">
            <div v-for="(item, index) in referList" class="refer_item_div" @click="handleOpenUrl(item.link)">
              <div style="flex: 1;color: #454545;display: flex;align-items: center;">
                <div class="item_index">
                  {{ index + 1 }}
                </div>
                <span style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;font-weight: bold;">{{
                  item.title }}</span>
              </div>
              <div class="refer_item_content" @click="handleShowReferContent">
                {{ item.snippet }}
              </div>
              <div style="flex: 1;display: flex;justify-content: space-between;align-items: center;cursor: pointer;">
                <span
                  style="color: gray;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;width: 80%;font-size: 13px;">
                  <svg-icon icon-class="refer_url_logo" />
                  {{ item.link }}
                </span>
                <svg-icon icon-class="refer_tiaozhuan" style="font-size: 13px;" />
              </div>
            </div>
          </div>
        </div>
      </div>
      <div>
        <el-card :class="{ 'file-card': true, 'hide': fileList.length < 1 }"
          :body-style="{ display: 'flex', alignItems: 'center', overflowX: 'auto', gap: '8px' }">
          <file-content v-for="(file, index) in fileList" :key="index" :file="file" :url="file.url" :type="file.type"
            :loading="file.status === 'loading'" :status="file.status" @close="handleClose" />
        </el-card>

        <el-upload class="upload-demo" ref="upload" action="https://jsonplaceholder.typicode.com/posts/"
          :on-success="handleFileSuccess" :on-error="handleFileError" :on-change="handleChange"
          :on-exceed="handleExceed" :limit="1" :show-file-list="false" :auto-upload="false" :accept="accept"
          v-show="false">
          <div ref="uploadButton">{{ $t('AllInOne.uploading') }}</div>
        </el-upload>

        <el-card :body-style="{ padding: '0' }">
          <div style="display: flex;align-items: center; ">
            <el-input type="textarea" class="no-border" :autosize="{ minRows: 2, maxRows: 4 }" v-model="sendData"
              :readonly="isRecording" @keydown.native="keydownfn" id="chatInput"
              @input="handleFindTokenNum" :placeholder="sendInputText" :disabled="sendDataDisable"/>
            <span :class="this.currInputTokenNum <= this.inputMaxToken ? 'token_num_span' : 'token_num_span_false'"
              v-show="!findModelTokenLoading">
              {{ this.currInputTokenNum }}/{{ this.inputMaxToken }}
            </span>
            <span v-show="findModelTokenLoading" style="width: 120px;text-align: center;">
              <svg-icon icon-class="loading" style="font-size: 30px;animation: spin 2s linear infinite;" />
            </span>
          </div>
          <div class="pd-h16 tool-bar-line" style="padding-bottom: 8px;font-size: 18px;">
            <!--左侧按钮组-->
            <div class="tool-bar">
              <el-popover :append-to-body="false" popper-class="input-bar-popover" placement="top"
                :visible-arrow="false" width="fit-content" style="min-width: 0;" trigger="click">

                <div>
                  <div class="popover-list-item flex-layout align-center gap12" v-for="item in 4" :key="item"
                    @click="handleChangeLayout(item)">
                    <svg-icon style="font-size: 20px;" :icon-class="'layout-' + item" />
                    <span>
                      {{ item + ' Bot' }}
                    </span>
                  </div>
                </div>

                <div class="tool-bar-item" slot="reference" :title="$t('layout')">
                  <svg-icon aria-hidden="false" :icon-class="'layout-' + bots.length" />
                </div>
              </el-popover>
              <div class="tool-bar-item" @click="handleShowSettings">
                <el-tooltip :content="$t('setting')">
                  <svg-icon icon-class="setting" />
                </el-tooltip>
              </div>
              <el-tooltip :content="$t('AllInOne.streamState')">
                <el-switch v-model="streamState" />
              </el-tooltip>
            </div>

            <!--右侧按钮组-->
            <div class="tool-bar">
              <div class="el-button-radiow" >
                <span class="switchSenCla">{{ $t('privateDomain.switchSending') }}：</span>
                <el-radio-group fill="#6DA7CE" size="mini" v-model="evertShiftStart" @input="setShiiftStart">
                  <el-radio-button label="false">Enter</el-radio-button>
                  <el-radio-button label="true">Shift+Enter</el-radio-button>
                </el-radio-group>
              </div>
              <el-tooltip :content="$t('AllInOne.attachmentNumber') + `${maxFile}` + $t('fileManager.individual')"
                placement="top" v-show="true">
                <div :class="{ 'tool-bar-item': true, 'disabled': fileList.length >= maxFile }"
                  :title="$t('fileManager.attachment')" @click="handleClickUpload">
                  <svg-icon icon-class="attachment" />
                </div>
              </el-tooltip>

              <!-- <el-popover :append-to-body="true" popper-class="input-bar-popover" placement="top" :visible-arrow="false"
                width="fit-content" v-model="isRecording" trigger="manual">
                <div style="width: 100%;text-align: center">
                  <span>{{ $t('privateDomain.recording') }}</span>
                </div>
                <div :title="$t('privateDomain.voice')" :class="{ 'tool-bar-item': true, 'is-active': false }"
                  @click="handleClickRecord" slot="reference">
                  <svg-icon icon-class="mic-close" v-if="isRecording" />
                  <svg-icon icon-class="mic" v-else />
                </div>
              </el-popover> -->

              <div style="font-size: 28px;margin-left: 6px;" @click="handleSendData">
                <svg-icon v-if="sendData && this.modelList.length > 0" icon-class="color-send"
                  style="cursor:pointer;" />
                <svg-icon v-else icon-class="color-not-send" />
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    <div style="position: relative;width: 100%;height: 100%;">
      <chat-config v-if="showSettings" :visibility.sync="showSettings" />
    </div>
  </div>
</template>
<script>
import GptApi from "@/views/chat/bots/gpt.vue";
import ChatBot from "@/views/chat/bot.vue";
import { sttFromMic } from "@/utils/SpeechRecognitionLong";
import * as sdk from "microsoft-cognitiveservices-speech-sdk";
import FileContent from "@/components/FileConntent/index.vue";
import ChatConfig from "@/views/chat/components/config.vue";
import { getModelTemplateSelect } from "@/api/modelManager/modelManager";
import { uploadFile, findTokenNum } from '@/api/chat/index';
import axios from "axios";
import { listData } from '@/api/system/dict/data';

export default {
  name: 'AllInOne',
  components: { ChatConfig, FileContent, ChatBot, GptApi },
  data() {
    return {
      showSettings: false,
      sendData: this.$store.getters.mainWindow.sendData,
      botNum: this.$store.getters.botNum,
      bots: [],
      isRecording: false,
      sendDataList: [],
      fileList: this.$store.getters.mainWindow.fileList,
      maxFile: 1,
      accept: '.xlsx, .docx, .md, .doc, .xls, .txt, .pdf',
      fileTypeLimit: this.$t('AllInOne.picture'),

      notification: null,
      modelList: [],

      extraTips: this.$t('AllInOne.pictureItem'),
      maxWidth: 1092,
      maxHeight: 1092,
      pythonUrl: process.env.VUE_APP_PYTHON_URL,
      inputMaxToken: 131072,
      currInputTokenNum: 0,
      currInputTokens: this.$store.getters.mainWindow.currInputTokens,
      currFileTokenNum: this.$store.getters.mainWindow.currFileTokenNum,
      modelTokenDictParam: {
        dictType: 'model_token_num',
        pageNum: 1,
        pageSize: 10
      },
      findModelTokenLoading: false,
      timeoutId: null,
      modelTypeList: this.$store.state.user.botList.filter(item => item.scene !== 'embedding'),
      inputTokenState: true,
      showReferState: false,
      referList: [],
      streamState: true,
      textareaHeight: 100,   // 初始高度
      isDragging: false,     // 是否正在拖动
      startY: 0,            // 拖动起始Y坐标
      evertShiftStart: localStorage.getItem("EVERT_SHIFT_START") || false,
      sendInputText: this.$t('privateDomain.sendInputText'),
      sendDataDisable: false,
    }
  },
  watch: {
    showSettings: function (newV, oldV) {
      if (!newV) {
        this.$refs.chatBot.forEach(bot => {
          bot.handleCommand();
        });
      }
    },
    fileList: function (newVal, oldVal) {
      if (newVal.length === 0 && newVal.length != oldVal.length) {
        this.handleRemoveFile();
      }
    },
    streamState: function (newVal, oldVal) {
      this.$refs.chatBot.forEach(bot => {
        bot.handleStreamState(newVal);
      });
    }
  },
  mounted() {
    this.bots = new Array(this.$store.getters.botNum).fill(true);
    getModelTemplateSelect().then(res => {
      if (res.code === 200) {
        this.modelList = res.data;
      }
    }).catch(error => {
      this.$message.error(this.$t('AllInOne.saveModelError'));
    });
    this.currInputTokenNum = this.$store.getters.mainWindow.currInputTokens + this.$store.getters.mainWindow.currFileTokenNum;
    this.sendData = this.$store.getters.mainWindow.sendData;
    document.getElementById('chatInput').addEventListener('paste', async (e) => {
      // 从剪贴板获取文件列表（支持截图、本地文件）
      const files = e.clipboardData.files;
      if (files.length === 1) {
        files.forEach(item => {
          const data = {
            name: item.name,
            raw: item,
            size: item.size,
            status: 'ready',
          }
          this.fileList = [];
          this.handleChange(data, [data]);
        })
        return;
      } else {
        if (files.length === 0) {
          return;
        }
        this.$message.warning(this.$t('AllInOne.attachmentNumber') + `${this.maxFile}` + this.$t('fileManager.individual'))
        return;
      }
      // 处理浏览器 Clipboard API（如复制的图片数据）
      /* if (navigator.clipboard) {
        try {
          const clipboardItems = await navigator.clipboard.read();
          for (const item of clipboardItems) {
            for (const type of item.types) {
              if (type.startsWith('image/')) {
                const blob = await item.getType(type);
                const file = new File([blob], 'pasted-image.png', { type });
                const data = {
                  name: file.name,
                  raw: file,
                  size: file.size,
                  status: 'ready',
                }
                this.handleChange(data, [data]);
              }
            }
          }
        } catch (error) {
          console.error(error);
        }
      } */
    });
  },
  methods: {
    startIncrease() {
      this.isIncreasing = true;
      // 持续增加高度
      this.intervalId = setInterval(() => {
        this.textareaHeight += 5; // 每50ms增加5px
      }, 50);
    },
    stopIncrease() {
      this.isIncreasing = false;
      clearInterval(this.intervalId); // 停止增加
    },
    handleShowSettings() {
      this.showSettings = true;
    },
    handleChangeLayout(num) {
      this.botNum = num;
      this.$store.dispatch("setBotNum", num)
      this.bots = new Array(num).fill(true);
    },
    handleClickRecord() {
      if (this.isRecording) {
        this.notification.close();
        this.handleStop();
        this.sendData = this.sendDataList.join('');
        this.isRecording = false;
      } else {
        this.notification = this.$notify.info({
          title: this.$t('privateDomain.recordingProgress'),
          duration: 0,
          showClose: false,
        });
        this.handleStart();
      }
    },
    handleStart() {
      this.sendDataList = [''];
      let audioContext = new AudioContext();
      audioContext.resume().then(() => {

        let recognizer = this.recognizer = sttFromMic();
        recognizer.recognizing = (s, e) => {
          // console.log(`RECOGNIZING: Text=${e.result.text}`);
          this.sendDataList[this.sendDataList.length - 1] = e.result.text;
          this.sendData = this.sendDataList.join('');
          this.$forceUpdate();
        };

        recognizer.recognized = (s, e) => {
          if (e.result.reason == sdk.ResultReason.RecognizedSpeech) {
            // console.log(`RECOGNIZED: Text=${e.result.text}`);
            this.sendDataList[this.sendDataList.length - 1] = e.result.text;
            this.sendDataList.push('');
            this.sendData = this.sendDataList.join('');
          } else if (e.result.reason == sdk.ResultReason.NoMatch) {
            console.log("NOMATCH: Speech could not be recognized.");
          }
        };

        recognizer.canceled = (s, e) => {
          console.log(`CANCELED: Reason=${e.reason}`);

          if (e.reason == sdk.CancellationReason.Error) {
            console.log(`"CANCELED: ErrorCode=${e.errorCode}`);
            console.log(`"CANCELED: ErrorDetails=${e.errorDetails}`);
            console.log("CANCELED: Did you set the speech resource key and region values?");
          }

          recognizer.stopContinuousRecognitionAsync();
          this.isRecording = false;
        };

        recognizer.sessionStopped = (s, e) => {
          console.log("\n    Session stopped event.");
          recognizer.stopContinuousRecognitionAsync();
          this.sendData = this.sendDataList.join('');
          this.isRecording = false;
        };

        recognizer.startContinuousRecognitionAsync();
        this.isRecording = true;
      });
    },
    handleStop() {
      this.recognizer.stopContinuousRecognitionAsync();
      this.isRecording = false;
    },
    handleClose(file) {
      this.fileList = this.fileList.filter(item => item !== file);
    },
    handleClickUpload() {
      if (this.fileList.length >= this.maxFile) return;
      this.$refs.upload.clearFiles();
      this.$refs.uploadButton.click();
    },
    handleChange(file, fileList) {
      if (file.status === 'ready') {
        let data = {
          isImage: false,
          fileName: file.name,
          type: file.name.split('.').pop(),
          status: 'loading'
        };
        this.fileList.push(data);
        if (this.accept.includes(file.name.split('.').pop().toLowerCase())) {
          uploadFile(file.raw).then(res => {
            if (res.code === 200) {
              const resData = JSON.parse(res.msg);
              const windowModel = this.$store.getters.windowModel;
              for (let i = 0; i < this.botNum; i++) {
                windowModel[i].fileMark = resData.fileMark;
              }
              this.$store.dispatch('setWindowModel', windowModel);


              this.currFileTokenNum = resData.fileTokens;

              this.currInputTokenNum = resData.fileTokens + this.currInputTokens;
              const mainWindow = this.$store.getters.mainWindow;
              data.isImage = true;
              // file.raw转为base64
              let reader = new FileReader();
              reader.readAsDataURL(file.raw);
              reader.onload = () => {
                this.fileList.forEach(item => {
                  if (item.fileName === data.fileName) {
                    item.status = 'success'
                  }
                })
              }
              mainWindow.fileList = this.fileList;
              mainWindow.currFileTokenNum = resData.fileTokens;
              this.$store.dispatch('setMainWindow', mainWindow);
            }
          }).catch(err => {
            this.$message.error(this.$t('fileManager.uploadError'));
            data.isImage = true;
            // file.raw转为base64
            let reader = new FileReader();
            reader.readAsDataURL(file.raw);
            reader.onload = () => {
              this.fileList.forEach(item => {
                if (item.fileName === data.fileName) {
                  item.status = 'error'
                }
              })
            }
          });
        } else {
          let reader = new FileReader();
          reader.readAsDataURL(file.raw);
          reader.onload = () => {
            this.fileList.forEach(item => {
              if (item.fileName === data.fileName) {
                item.status = 'error'
              }
            })
          }
        }
      }
    },
    handleRemoveFile() {
      this.fileList = [];
      this.currInputTokenNum = 0;
      const mainWindow = this.$store.getters.mainWindow;
      this.currFileTokenNum = 0;
      this.currInputTokens = 0;
      // this.sendData = '';
      mainWindow.currInputTokens = 0;
      mainWindow.currFileTokenNum = 0;
      // mainWindow.sendData = '';
      mainWindow.fileList = [];
      this.$store.dispatch('setMainWindow', mainWindow);
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
    handleExceed(files, fileList) {
      this.$message.warning(this.$t('fileManager.currentRestrictionSelection') + ` 1 ` + this.$t('fileManager.aFile') + `，` + this.$t('fileManager.chosenThisTime') + ` ${files.length} ` + this.$t('fileManager.aFile') + `，` + this.$t('fileManager.selectedTogether') + ` ${files.length + fileList.length} ` + this.$t('fileManager.aFile'));
    },
    keydownfn(event) {
      if (this.evertShiftStart === 'true') {
        if (event.shiftKey && event.keyCode == 13) {
          event.preventDefault();
          this.handleSendData();
        }
      } else {
        if (event.keyCode == 13) {
          if (!event.shiftKey && !event.ctrlKey && !event.altKey && !event.metaKey) {
            event.preventDefault();
            this.handleSendData(this.sendData, this.fileList);
          } else if (!event.shiftKey) {
            this.sendData = this.sendData + '\n';
          }
        }
      }
    },
    handleSendData() {
      if (this.currInputTokenNum > this.inputMaxToken) {
        this.$message.warning(this.$t('AllInOne.tokenMaxNum'));
        return;
      }
      this.currInputTokenNum = 0;
      const mainWindow = this.$store.getters.mainWindow;
      mainWindow.currInputTokens = 0;
      mainWindow.currFileTokenNum = 0;
      this.$store.dispatch('setMainWindow', mainWindow);
      if (this.sendData && this.modelList.length > 0) {
        this.inputTokenState = false;
        this.$refs.chatBot.forEach(bot => {
          bot.handleSendData(this.sendData, this.fileList);
        });

        // this.fileList = [];
        // this.$refs.upload.clearFiles();
        this.sendData = '';
      }
    },
    handleModelTokenList(command) {
      this.findModelTokenLoading = true;
      if (command) {
        listData(this.modelTokenDictParam).then(res => {
          if (res.code === 200) {
            res.rows.forEach(item => {
              if (item.dictLabel === command) {
                this.inputMaxToken = parseInt(item.dictValue);
              }
            });
            this.findModelTokenLoading = false;
          }
        }).catch(err => {
          this.$message.error(this.$t('AllInOne.tokenFindError'));
          this.findModelTokenLoading = false;
        });
      } else {
        this.inputMaxToken = 131072;
        this.findModelTokenLoading = false;
      }
    },
    handleReferList(list) {
      if (this.referList.length > 0 && this.showReferState) {
        if (list.length === 0) {
          this.referList = [];
          this.showReferState = false;
        }
        if (this.referList[0].title === list[0].title) {
          this.showReferState = !this.showReferState
          return;
        }
        this.referList = list;
        return;
      }
      this.referList = list;
      this.showReferState = !this.showReferState
    },
    handleShowReferContent() {
      const btn = document.querySelector('.refer_item_content');
      btn.addEventListener('click', () => {
        btn.classList.toggle('active'); // 切换点击状态
      });
    },
    handleOpenUrl(url) {
      window.open(url);
    },
    handleFindTokenNum() {
      if (this.timeoutId) {
        clearTimeout(this.timeoutId);
      }
      const httpState = process.env.VUE_APP_PYTHON_URL_STATE;
      if (httpState === 'true') {
        this.timeoutId = setTimeout(() => {
          this.findModelTokenLoading = true;
          axios.post(this.pythonUrl + '/get_token_num',
            { inputText: this.sendData },
            {
              headers: { 'Content-Type': 'application/json' }
            })
            .then(res => {
              if (res.data.code === 200) {
                if (this.inputTokenState) {
                  this.currInputTokenNum = res.data.data + this.currFileTokenNum;
                  const mainWindow = this.$store.getters.mainWindow;
                  mainWindow.currInputTokens = res.data.data;
                  this.$store.dispatch('setMainWindow', mainWindow);
                }
                this.findModelTokenLoading = false;
                if (!this.inputTokenState) {
                  setTimeout(() => {
                    this.inputTokenState = true;
                  }, 500);
                }
              }
            })
            .catch(err => {
              this.$message.error(this.$t('AllInOne.tokenFindError'));
              this.findModelTokenLoading = false;
            });
        }, 300);
      } else {
        this.timeoutId = setTimeout(() => {
          const subData = {
            inputText: this.sendData
          }
          this.findModelTokenLoading = true;
          findTokenNum(subData).then(res => {
            if (res.code === 200) {
              if (this.inputTokenState) {
                this.currInputTokenNum = res.data + this.currFileTokenNum;
                const mainWindow = this.$store.getters.mainWindow;
                mainWindow.currInputTokens = res.data;
                this.$store.dispatch('setMainWindow', mainWindow);
              }
              this.findModelTokenLoading = false;
              if (!this.inputTokenState) {
                setTimeout(() => {
                  this.inputTokenState = true;
                }, 500);
              }
            }
          }).catch(err => {
            this.$message.error(this.$t('AllInOne.tokenFindError'));
            this.findModelTokenLoading = false;
          });
        }, 300);
      }
    },
    sendDataDisableMet(v){
      this.sendDataDisable = v;
    },
    setShiiftStart (v) {
      localStorage.setItem("EVERT_SHIFT_START", v);
      this.evertShiftStart = v;
      this.sendInputText = v === 'true' ?  this.$t('privateDomain.sendInputText2'): this.$t('privateDomain.sendInputText');
    },
  },
  beforeDestroy() {
    if (this.recognizer) {
      this.recognizer.close();
    }
    if (this.notification) {
      this.notification.close();
    }
    if (this.audioContext) {
      this.audioContext.close();
    }
    if (this.isRecording) {
      this.handleStop();
    }
  }
}
</script>
<style lang="scss" scoped>
.popover-list-item {
  padding: 4px 8px;
  cursor: pointer;

  &:hover {
    background-color: #f5f7fa;
  }
}

.file-card {
  height: 133px;
  background: rgba(233, 241, 246, 0.91);
  transition: all 0.25s;
}

.file-card.hide {
  height: 0;
  transition: all 0.25s;
}

.token_num_span {
  width: 120px;
  text-align: center;
  margin: 0 12px;
  color: rgba($color: #6DA7CE, $alpha: 1);
}

.token_num_span_false {
  width: 120px;
  text-align: center;
  margin: 0 12px;
  color: rgb(240, 83, 83);
}


.refer_list_div {
  width: 300px;
  background-color: white;
  margin-left: 6px;
  border-radius: 6px;
  padding: 12px 12px;
  display: flex;
  flex-direction: column;

  &.hidden {
    width: 0;
    padding: 0;
    border-width: 0;
    border-color: transparent;
    box-shadow: 0px 2px 4px 0px rgba(177, 177, 177, 0);
    transition: all 0.3s;
  }

}


.refer_item_div {
  display: flex;
  flex-direction: column;
  border: 1px solid rgb(213, 213, 213);
  border-radius: 12px;
  width: 100%;
  height: 180px;
  justify-content: space-between;
  padding: 12px 12px;
  gap: 10px;
}

.refer_item_div:hover {
  border: 2px solid #6a8be7;
}

.item_index {
  background-color: #5172cd;
  border-radius: 100px;
  min-width: 20px;
  width: 20px;
  height: 20px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 6px;
  color: white;
  padding: 0;
}

.refer_item_content {
  flex: 3;
  color: gray;
  overflow: hidden;
  word-wrap: break-word;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
}

/* 底部拖动手柄样式 */
.drag-handle {
  top: 0;
  left: 0;
  width: 12px;
  height: 12px;
  cursor: pointer;
  background: rgba(64, 158, 255, 0.3);
  border: 1px solid black;
}

.switchSenCla{
  font-size: 12px;
  color: #606266;
  font-weight: 600;
}

</style>
