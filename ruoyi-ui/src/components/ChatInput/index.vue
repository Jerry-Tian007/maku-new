<template>
  <el-card :body-style="{padding: '0'}">
    <el-input
      type="textarea"
      class="no-border"
      :autosize="{ minRows: 2, maxRows: 4}"
      v-model="sendData"
      resize="none"
      :readonly="isRecording"
      @keydown.native="keydownfn"
      style="padding-top: 4px;"
      v-show="!isError && !disabled"
      :placeholder="$t('privateDomain.sendInputText')"
    />
    <el-input
      type="textarea"
      class="no-border"
      :autosize="{ minRows: 2, maxRows: 4}"
      resize="none"
      :readonly="isRecording"
      @keydown.native="keydownfn"
      style="padding-top: 4px;"
      :disabled="true"
      :placeholder="errorInfo"
      v-show="isError"
    />
    <el-input
      type="textarea"
      class="no-border"
      :autosize="{ minRows: 2, maxRows: 4}"
      v-model="sendData"
      resize="none"
      :readonly="isRecording"
      @keydown.native="keydownfn"
      style="padding-top: 4px;"
      v-show="disabled"
      disabled
      :placeholder="$t('privateDomain.pleaseSelect')"
    />
    <div class="pd-h16 tool-bar-line" style="padding-bottom: 8px;font-size: 18px;">
      <!--左侧按钮组-->
      <div class="tool-bar" :class="{disabled: disabled}">
        <slot name="left-tools"/>
      </div>

      <!--右侧按钮组-->
      <div class="tool-bar" :class="{disabled: disabled || isError || isLoading}">
        <el-popover
          :append-to-body="true"
          popper-class="input-bar-popover"
          placement="top"
          :visible-arrow="false"
          width="fit-content"
          v-model="isRecording"
          trigger="manual">
          <div style="width: 100%;text-align: center">
            <span>{{ $t('privateDomain.recording') }}</span>
          </div>
          <div title="语音" :class="{'tool-bar-item': true, 'is-active': false}" @click="handleClickRecord"
               slot="reference">
            <svg-icon icon-class="mic-close" v-if="isRecording"/>
            <svg-icon icon-class="mic" v-else/>
          </div>
        </el-popover>

        <div style="font-size: 28px;margin-left: 6px;" @click="handleSendData">
          <svg-icon v-if="sendData && !isError && !isLoading" icon-class="color-send" style="cursor:pointer;"/>
          <svg-icon v-else icon-class="color-not-send"/>
        </div>
      </div>
    </div>
  </el-card>
</template>
<script>
import {sttFromMic} from "@/utils/SpeechRecognitionLong";
import * as sdk from "microsoft-cognitiveservices-speech-sdk";

export default {
  name: "ChatInput",
  props:{
    isError:{
      type: Boolean,
      default: false,
    },
    isLoading: {
      type: Boolean,
      default: false,
    },
    disabled:{
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      sendData: '',
      isRecording: false,

      recognizer: null,
      sendDataList: [],

      errorInfo: this.$t('privateDomain.currSessionOver'),
    }
  },
  methods: {

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

    keydownfn(e) {
      if (event.keyCode == 13) {
        if (!event.shiftKey && !event.ctrlKey && !event.altKey && !event.metaKey) {
          event.preventDefault();
          this.handleSendData();
        } else if (!event.shiftKey) {
          this.sendData = this.sendData + '\n';
        }
      }
    },
    handleSendData() {
      this.$emit('sendData', this.sendData);
      this.sendData = '';
    },
  }
}
</script>
