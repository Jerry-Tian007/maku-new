<template>
  <div class="app-container full-screen flex-layout gap10">
    <session-list
      :session-list="sessionList"
      :session-time-list="sessionTimeList"
      v-model="showSession"
      :session-mark="sessionMark"
      :session-loading="sessionLoading"
      :is-new-session="isNewSession"
      @new="handleNewSession"
      @delete="handleDeleteSession"
      @use="handleUseSession"
    />
    <div class="flex-1 flex-layout column gap12" style="overflow:hidden;">


      <el-card
        class="flex-1"
        style="position:relative;overflow-y: auto;"
        v-loading="historyLoading"
        :body-style="{padding: '0 0', height: '100%'}">
        <div :class="{'fold-button':true, 'unfold':true, 'hide': showSession}" @click="showSession = true;">
          <svg-icon icon-class="unfold" style="font-size: 20px;"/>
        </div>
        <template v-if="!historyLoading">
          <template v-if="historyList.length<1 && currentSourceType===''">
            <BIIntroduce @submit="handleSubmitDataSource" :data-source-list="dataSourceList"/>
          </template>
          <div
            v-else
            ref="historyContent"
            style="width: 100%;height: 100%;overflow-y:auto;padding: 0 50px;"
            @scroll="handleScroll">

            <div>
              <div :class="{'blank': true}"/>
              <div
                class="flex-layout mb10"
                :style="{justifyContent: 'flex-start', gap: '6px'}"
              >
                <div>
                  <img :src="logoIcon" class="avatar medium avatar-bg"/>
                </div>
                <div style="overflow:hidden;max-width: 75%;">
                  <div
                    class="flex-layout gap10"
                    style="margin-top: 18px;"
                  >
                    <el-card
                      class="bot"
                      style="max-width: 500px;"
                    >
                      <div style="overflow-x: auto;">
                        <!--                        欢迎语-->
                        <markdown-it-vue class="md-body" :content="$t('chatBI.chatBiAi')"
                                         :options="options" align="left"/>
                      </div>


                    </el-card>
                  </div>
                </div>
                <div class="avatar placeholder"></div>
              </div>
              <div
                v-for="(item, index) in historyList"
                :key="index"
                class="flex-layout mb10"
                :style="{justifyContent: item.role === 'user' ? 'flex-end' : 'flex-start', gap: '6px'}"
              >
                <div v-if="item.role === 'bot'">
                  <img :src="logoIcon" class="avatar medium avatar-bg"/>
                </div>
                <div v-else class="avatar placeholder"></div>
                <div style="overflow:hidden;max-width: 75%;">
                  <!--                <div :class="{'history-content': true}">-->
                  <el-card
                    v-if="item.role === 'user'"
                    class="user"
                    style="margin-top: 18px;cursor: pointer;"
                    :title="$t('privateDomain.doubleClickCopy')"
                    @dblclick.native="handleCopyContent(item)">
                    <markdown-it-vue class="md-body" :content="item.content" :options="options" align="left"/>
                  </el-card>


                  <div
                    class="flex-layout gap10"
                    style="margin-top: 18px;"
                    v-else
                  >
                    <el-card
                      :class="item.role"
                      style="max-width: 500px;"
                    >
                      <template v-if="item.loading">
                        <span>{{ $t('privateDomain.thinkingAbout') }}</span><i class="el-icon-loading" style="margin-left: 4px;"/>
                      </template>
                      <span v-else-if="item.isError">
                  {{ item.errorContent }}
                </span>
                      <div v-else style="overflow-x: auto;">
                        <!--                      <markdown-it-vue class="md-body" :content="item.content" :options="options" align="left"/>-->
                        <b-i-bot-content :content="item.content" :chart-data="item.chartData"
                                         :chart-loading="!item.chartData.type && !item.finished"
                                         :content-loading="item.content === '' && !item.finished"
                                         :show-work-flow="item.showWorkFlow"/>
                      </div>

                      <!--工具栏-->
                      <div
                        class="flex-layout align-center start-end"
                        style="padding-top: 4px;font-size: 16px;"
                        v-if="item.role === 'bot' && !item.loading && !item.isError"
                      >
                        <div
                          v-if="item.finished"
                          style="gap:2px;"
                          class="tool-bar">
                          <div class="tool-bar-item" @click="handleCopy(item)" :title="$t('chatBI.summarizeCopy')">
                            <svg-icon icon-class="copy"/>
                          </div>
                          <div class="tool-bar-item" @click="handleAccuracy(item,'yes')" :title="$t('chatBI.favorableComment')">
                            <svg-icon icon-class="good-chosen" v-if="item.accuracy === 'yes'"/>
                            <svg-icon icon-class="good" v-else/>
                          </div>
                          <div class="tool-bar-item" @click="handleAccuracy(item,'no')" :title="$t('chatBI.bad')">
                            <svg-icon icon-class="bad-chosen" v-if="item.accuracy === 'no'"/>
                            <svg-icon icon-class="bad" v-else/>
                          </div>
                        </div>
                        <div style="width: 10px;" v-else></div>

                        <div
                          style="gap:2px;"
                          class="tool-bar">

                          <div class="tool-bar-item" @click="handleSwitchSQLStatus(item)"
                               :title="item.showSQL ? $t('chatBI.hideSql') : $t('chatBI.displaySql')">
                            <svg-icon icon-class="sql"/>
                          </div>

                          <div class="tool-bar-item" @click="handleSwitchShowStatus(item)"
                               :title="item.showWorkFlow ? $t('chatBI.hideWorkFlow') : $t('chatBI.displayWorkFlow')">
                            <i class="el-icon-d-arrow-right" v-show="!item.showWorkFlow"/>
                            <i class="el-icon-d-arrow-left" v-show="item.showWorkFlow"/>
                          </div>
                        </div>
                      </div>

                      <div class="sql-area" v-loading="!item.sql && item.showSQL && !item.finished" :class="{hide: !item.showSQL}">
                        <markdown-it-vue class="md-body" :content="
'```sql\n'+item.sql+'\n```'
" :options="options" align="left"/>


                        <div class="tool-bar-item copy-btn" @click="handleCopySql(item)" :title="$t('chatBI.copySql')">
                          <svg-icon icon-class="copy"/>
                        </div>
                      </div>

                    </el-card>
                    <div class="flex-layout workflow" :class="{hide: !item.showWorkFlow}"
                         style="overflow:auto;position:relative;">
                      <work-flow :work-list="item.workList"
                                 style="position:absolute;z-index:1;height: 100%;width:100%;"/>
                    </div>
                  </div>

                  <!--猜你下午-->
                  <template
                    v-if="index === historyList.length - 1 && item.followUp && item.followUp.length > 0">
                    <div class="follow-title" style="margin-top: 10px;">{{ $t('chatBI.guessWhatYou') }}</div>
                    <el-card
                      v-for="(follow, fIndex) in item.followUp"
                      :key="fIndex"
                      @click.native="handleClickFollowUp(follow)"
                      class="bot follow"
                      :body-style="{
                    whiteSpace: 'nowrap',
                    overflow: 'hidden',
                    textOverflow: 'ellipsis'
                  }"
                      style="margin-top: 6px;width: fit-content;min-width: 120px;">
                    <span :title="follow">
                      {{ follow }}
                    </span>
                    </el-card>
                  </template>
                </div>
                <div v-if="item.role === 'user'">
                  <img :src="userAvatar" class="avatar border"/>
                </div>
                <div v-else class="avatar placeholder"></div>
              </div>
              <div v-show="isError">
                <div class="flex-layout align-center center-center gap6">
                  <el-divider class="flex-1"/>
                  <span class="fs-12 text-gray">
                    {{ errorInfo }}
                  </span>
                  <el-divider class="flex-1"/>
                </div>
              </div>
              <div :class="{'blank': true}"/>
            </div>
          </div>

          <div
            :title="$t('privateDomain.ReturnTheBottom')"
            @click="handleScrollToBottom"
            :class="{'back-to-bottom':true, 'sub-color':true, 'is-hidden': !isFar || isBackHidden}">
            <i class="el-icon-arrow-down"/> <span style="margin-left: 2px;">{{ $t('chatBI.returnBottom') }}</span>
          </div>
        </template>
      </el-card>

      <el-card v-if="false" :body-style="{padding: '0'}">
        <el-input
          type="textarea"
          class="no-border"
          :autosize="{ minRows: 2, maxRows: 4}"
          v-model="sendData"
          resize="none"
          :readonly="isRecording"
          @keydown.native="keydownfn"
          style="padding-top: 4px;"
          v-show="!isError"
          :placeholder="$t('privateDomain.sendInputText')"
        />
        <div class="pd-h16 tool-bar-line" style="padding-bottom: 8px;font-size: 18px;">
          <!--左侧按钮组-->
          <div class="tool-bar">
          </div>

          <!--右侧按钮组-->
          <div class="tool-bar">
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
              <div :title="$t('privateDomain.voice')" :class="{'tool-bar-item': true, 'is-active': false}" @click="handleClickRecord"
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

      <chat-input
        :disabled="currentSourceType===''"
        @sendData="handleSendData"
        :is-error="isError"
        :is-loading="isLoading">
        <template slot="left-tools">
          <el-button
            :title="$t('chatBI.clickViewCurrently')"
            v-if="currentSourceType==='database'"
            class="blue"
            size="small"
            icon="el-icon-coin"
            @click="showDialog = true">
            <span>{{ $t('chatBI.dataSource') }} {{ databaseCount.selected }}/{{ databaseCount.total }}</span>
          </el-button>

          <el-button
            v-if="currentSourceType==='excel'"
            :title="$t('chatBI.useFiles')+'：'+(fileInfo.fileName || '-') + '.' + (fileInfo.fileSuffix || '-')"
            class="purple"
            size="small">
            <svg-icon icon-class="excel"/>
            <span style="margin-left: 6px;">
              {{ (fileInfo.fileName || '-') + '.' + (fileInfo.fileSuffix || '-') }}
            </span>
          </el-button>

          <model-dropdown v-model="modelInfo" style="margin-left: 10px;"/>

        </template>
      </chat-input>

      <el-dialog
        :title="$t('chatBI.selectedDataSource')"
        :visible.sync="showDialog"
        width="60%"
        center>
        <database-choose v-model="databaseList" :data-source="dataSourceList" style="height: 60vh;" disabled/>
      </el-dialog>

      <web-soc-ket-component
        style="display: none;"
        ref="ws"
        @message="handleWebSocketMessage"
        @error="handleWebSocketError"
      />
    </div>
  </div>
</template>
<script>
import SessionList from "@/components/SessionList/index.vue";
import {listUserSession} from "@/api/knowledgeBase/sessionList";
import {getSessionMark} from "@/utils";
import ChatInput from "@/components/ChatInput/index.vue";
import {accuracyOrNot} from "@/api/knowledgeBase/historyList";
import logoIcon from "@/assets/logo/logoIcon1.png";
import placeholder from "@/assets/logo/logo-placeholder.jpg";
import userAvatar from "@/assets/images/default_profile.png";
import BIIntroduce from "./components/introduce.vue";
import BIBotContent from "@/views/bi/chat/components/botContent.vue";
import WorkFlow from "@/views/bi/chat/components/workFlow.vue";
import ModelDropdown from "@/components/ModelDropdown/index.vue";
import DatabaseChoose from "@/views/bi/chat/components/databaseChoose.vue";
import WebSocKetComponent from "@/components/WebSocKetComponent/index.vue";
import {biSessionHistory, biSessionList, deleteBISession, selectDataSource} from "@/api/bi/chat/select";
import {detailFile} from "@/api/bi/chat/excel";
import {biAccuracy} from "@/api/bi/chat/history";

export default {
  name: "BIChat",
  components: {
    WebSocKetComponent,
    DatabaseChoose, ModelDropdown, WorkFlow, BIBotContent, ChatInput, SessionList, BIIntroduce
  },
  computed: {
    modelList() {
      return this.$store.state.user.botList.filter(item => item.scene !== 'embedding');
    }
  },
  data() {
    return {
      logoIcon,
      placeholder,
      userAvatar,
      sessionList: [],
      sessionTimeList: [],
      showSession: true,
      sessionMark: null,
      sessionLoading: false,
      isNewSession: true,

      sendData: '',
      isRecording: false,
      isLoading: false,
      isError: false,

      recognizer: null,
      sendDataList: [],

      historyLoading: false,


      options: {
        markdownIt: {
          linkify: true,
          html: true,
        },
      },
      historyList: [],
      messages: [],
      errorInfo: this.$t('privateDomain.currSessionOver'),
      isFar: false,
      isBackHidden: false,

      modelInfo: {},

      showDialog: false,
      databaseList: [],
      databaseCount: {
        total: '-',
        selected: '-'
      },
      fileInfo: {},
      currentSourceType: '',

      wsPath: (process.env.VUE_APP_WS_TYPE || 'ws') + '://' + window.location.host + '/websocket/chatbi/',

      dataSourceList: []
    }
  },
  mounted() {
    this.sessionMark = getSessionMark();
    this.getSessionList();
    this.getDataSourceList();
  },
  methods: {
    getDataSourceList() {
      selectDataSource({}).then(res => {
        this.dataSourceList = res.data;
      }).catch(err => {
        console.log(err);
      });
    },
    getSessionList() {
      this.sessionLoading = true;
      biSessionList({}).then(response => {
        this.sessionList = response.data;
      }).catch(() => {
      }).finally(() => {
        this.sessionLoading = false;
      });
    },
    handleClearDialog(needNew) {
      if(needNew)this.sessionMark = getSessionMark();
      this.historyList = [];
      this.messages = [];
      this.isError = false;
      this.isLoading = false;

      this.currentSourceType = '';
      this.databaseList = [];
      this.databaseCount = {
        total: '-',
        selected: '-'
      };
      this.fileInfo = {};
    },
    handleNewSession() {
      this.handleClearDialog(true);
      this.isNewSession = true;
    },
    handleDeleteSession(id, sessionMark) {
      let isCurrent = this.sessionMark === sessionMark;
      if(isCurrent) this.historyLoading = true;
      this.sessionLoading = true;
      deleteBISession(id).then(() => {
        if(isCurrent) this.handleNewSession();
        this.getSessionList();
      }).catch(() => {
        this.sessionLoading = false;
      }).finally(() => {
        if(isCurrent) this.historyLoading = false;
      });
    },
    handleUseSession(sessionMark, session) {
      this.sessionMark = sessionMark;
      this.isNewSession = false;
      this.handleClearDialog(false);

      this.historyLoading = true;

      this.currentSourceType = '';

      biSessionHistory(sessionMark).then(res => {
        if(res.code===200){
          res.data.forEach(item =>{
            this.formatBotHistory(item);
          });

          if(session.dataSourceIds){
            this.databaseList = session.dataSourceIds.split(',');
            this.databaseCount = {
              total: this.dataSourceList.length,
              selected: this.databaseList.length
            };
            this.currentSourceType = 'database';
          } else if(session.excelId){
            detailFile(session.excelId).then(res => {
              this.fileInfo = res.data;
            }).catch(() => {
            });
            this.currentSourceType = 'excel';
          }

          this.historyLoading = false;

          this.$nextTick(() => {
            this.handleScrollToBottom();
          });
        }
      }).catch(() => {
        this.historyLoading = false;
      });
    },
    formatBotHistory(item){
      let data = {
        role: 'user',
        type: 'text',
        content: item.question,
      };
      this.messages.push(data);
      this.historyList.push({
        role: 'user',
        content: item.question,
        loading: false,
      });

      this.messages.push({
        role: 'assistant',
        type: 'text',
        content: item.answer,
      });

      let res = JSON.parse(item.response || '{}');
      if(res.code===200 && res.data){
        this.historyList.push({
          role: 'bot',
          content: item.answer,
          loading: false,
          chartData: res.data.chartData,
          sql: res.data.querySql,
          workList: res.node,
          timestamp: item.createTime,
          id: item.id,
          finished: true,
          accuracy: item.isAccuracy
        });
        this.$forceUpdate();
      }
    },

    handleSendData(content) {
      if (content !== null && content !== '') {
        this.sendData = content;
      }

      if (this.isError || this.isLoading || !this.modelInfo.scene) return;
      if (this.sendData === null || this.sendData === '') {
        return;
      }

      let text = this.sendData.replace(/\n+$/, '');

      this.messages.push({
        role: 'user',
        type: 'text',
        content: text,
      });
      this.historyList.push({
        role: 'user',
        content: text,
        loading: false,
      });

      let data = {
        "userId": this.$store.state.user.userId,
        "scene": this.modelInfo.scene,
        "modelType": this.modelInfo.model,
        "history": this.messages,
        "sessionId": this.sessionMark,
        "dataSourceIds": [],
        "excelId": null
      }
      if(this.currentSourceType === 'database'){
        data.dataSourceIds = this.databaseList || [];
      } else if (this.currentSourceType === 'excel') {
        data.excelId = this.fileInfo.id;
      } else {

      }

      setTimeout(() => {
        this.historyList.push({
          role: 'bot',
          content: '',
          loading: true,
          chartData: {},
          sql: '',
          timestamp: new Date().toLocaleString()
        })

        this.sendRequest(data);

        this.isLoading = true;

        this.$nextTick(() => {
          this.handleScrollToBottom();
        })

      }, 500);

      this.sendData = '';

      /*      this.historyList = [
              {
                role: 'user',
                content: '你好',
              },
              {
                role: 'bot',
                content: '您好，我是智能助手，有什么可以帮助您的吗？',
                loading: false,
                isError: false,
                errorContent: '抱歉，我没有理解您的问题，请重新描述您的问题',
                accuracy: '',
                followUp: ['如何查询订单', '如何退款', '如何联系客服'],
                workList: [

                  {
                    name: '工作项',
                    status: 'success',
                  },
                  {
                    name: '工作项',
                    status: 'error',
                  },
                  {
                    name: '工作项',
                    status: 'loading',
                  },
                  {
                    name: '工作项',
                    status: 'waiting',
                  },
                  {
                    name: '工作项',
                    status: 'waiting',
                  },
                  {
                    name: '工作项',
                    status: 'waiting',
                  },
                ],
                sql: `SELECT wc.S_INFO_NAME as S_INFO_NAME,
                     wc.S_INFO_CODE as S_INFO_CODE,
                     ae.CONTRACT_LIABILITIES as CONTRACT_LIABILITIES,
                     ae.REPORT_PERIOD as REPORT_PERIOD,
                     ae.STATEMENT_TYPE as STATEMENT_TYPE
              FROM SYS_WIND.ASHAREBALANCESHEET ae
              LEFT JOIN SYS_WIND.WINDCUSTOMCODE wc
              ON wc.S_INFO_COMPCODE = ae.S_INFO_COMPCODE`
              },
            ];*/
    },


    handleCopy(item) {
      if (item.isError) {
        return;
      }
      let textToCopy = item.content;
      this.copyContent(textToCopy);
    },
    handleCopyContent(item) {
      if (item.role === 'user') {
        let textToCopy = item.content;
        this.copyContent(textToCopy);
      }
    },
    handleCopySql(item) {
      if (item.isError) {
        return;
      }
      if (!item.sql) {
        this.$message.warning(this.$t('chatBI.currentlyNoSql'));
        return;
      }
      let textToCopy = item.sql || this.$t('chatBI.currentlyNoSql');
      this.copyContent(textToCopy);
    },
    handleAccuracy(item, accuracy) {
      let result = accuracy;
      let old = item.accuracy;
      if (item.accuracy === accuracy) {
        result = '';
        item.accuracy = '';
      } else {
        item.accuracy = accuracy;
      }
      this.$forceUpdate();

      biAccuracy({
        id: item.id,
        isAccuracy: result
      }).then(() => {
        this.$message.success(this.$t('chatBI.thankYouFeedback'));
      }).catch(err => {
        item.accuracy = old;
        this.$forceUpdate();

        console.log(err);
        this.$message.error(err);
      });
    },
    handleSwitchShowStatus(item) {
      item.showWorkFlow = !item.showWorkFlow;
      this.$forceUpdate();
    },
    handleSwitchSQLStatus(item) {
      item.showSQL = !item.showSQL;
      this.$forceUpdate();
      setTimeout(() => {
        this.handleScroll();
      }, 200);
      /*      this.$nextTick(()=>{
              this.handleScroll();
            })*/
    },
    handleClickFollowUp(follow) {
      this.sendData = follow;
      this.handleSendData();
    },
    handleRetry() {
      this.sendData = this.historyList[this.historyList.length - 1].content;
      this.handleSendData();
    },
    handleScrollToBottom() {
      this.$nextTick(() => {
        const scrollHeight = this.$refs.historyContent.scrollHeight;
        this.$refs.historyContent.scrollTo({
          top: scrollHeight,
          behavior: 'smooth'
        });

        // this.scrollPosition = scrollHeight;
      })

    },
    handleScroll() {
      if (this.$refs.historyContent.scrollHeight === (this.$refs.historyContent.scrollTop + this.$refs.historyContent.clientHeight)) {
        this.isBackHidden = false;
      }
      this.isFar = this.$refs.historyContent.scrollTop + this.$refs.historyContent.clientHeight < this.$refs.historyContent.scrollHeight - 10;
    },

    handleSubmitDataSource(type, data) {
      console.log(type, data);
      this.currentSourceType = type;
      if (type === 'database') {
        this.databaseList = data.list;
        this.databaseCount = data.count;
      } else if (type === 'excel') {
        this.fileInfo = data.fileInfo;
      }
      this.isNewSession = false;
    },

    sendRequest(data) {
      this.isLoading = true;
      this.$refs.ws.init(this.wsPath + this.sessionMark, data);
    },
    handleWebSocketMessage(msg) {
      console.log(msg);
      let data = JSON.parse(msg.data);

      let last = this.historyList[this.historyList.length - 1];

      switch (data.event) {
        case 'id':
          last.id = data.data;
          break;
        case 'error':
          this.$message.error(data.data);


          last.loading = false;
          last.isError = true;
          last.errorContent = data.data || this.$t('privateDomain.modelAbnormal');
          last.showWorkFlow = false;
          last.finished = true;
          this.isError = true;

          this.getSessionList();

          this.handleRequestEnd();

          break;
        case 'add':
          this.formatBotAnswer(data);
          break;
        default:
          this.$message.info(data.msg);
      }
    },
    formatBotAnswer(res) {
      let last = this.historyList[this.historyList.length - 1];

      if (res.data === '<start>') {
        last.showWorkFlow = true;

        this.$nextTick(() => {
          this.handleScrollToBottom();
        });

        return;
      }
      if(res.data === '<end>'){
        last.showWorkFlow = false;
        last.finished = true;

        this.messages.push({
          role: 'assistant',
          type: 'text',
          content: last.content,
        });

        this.handleRequestEnd();

        this.$forceUpdate();
        return;
      }
      let isFirst = last.loading;

      last.loading = false;
      last.isError = false;
      last.errorContent = '';
      last.workList = res.data.node;
      last.sql = res.data.data.querySql;
      last.content = res.data.data.answer;
      last.chartData = Array.isArray(res.data.data.chartData) ? {} : res.data.data.chartData;

      if(isFirst){
        this.$nextTick(() => {
          this.handleScrollToBottom();
        });
      }
    },
    handleWebSocketError(err) {
      this.$message.error(err);
      this.handleRequestEnd();
    },
    checkBottom(){
      if (!this.isFar) {
        this.isBackHidden = true;
        this.$nextTick(() => {
          this.handleScrollToBottom();
        })
      }
    },
    handleRequestEnd(){
      this.checkBottom();

      if (this.isNewSession) {
        this.getSessionList();
      }
      this.isNewSession = false;
      this.isLoading = false;
    }
  }
}
</script>
<style scoped lang="scss">
.fold-button {
  width: fit-content;
  border-radius: 4px 0px 0px 4px;
  background: #E9EAFB;
  padding: 4px;
  cursor: pointer;

  position: absolute;
  right: 0;
  top: 20px;
  transition: all 0.3s;

  &.unfold {
    left: 0;
    transition: all 0.3s;

  }

  &.hide {
    opacity: 0;
    pointer-events: none;
    transition: all 0.3s;
  }

  &:hover {
    box-shadow: 0 0 2px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s;
  }
}


.workflow {
  width: 200px;
  transition: opacity 0.3s, width 0.4s;

  &.hide {
    margin-left: 0;
    opacity: 0;
    pointer-events: none;
    width: 0;
    transition: opacity 0.3s, width 0.4s;
  }
}

.sql-area {
  width: 100%;
  height: fit-content;
  transition: all 0.3s;
  padding: 4px 0;

  position: relative;

  &.hide {
    height: 0;
    opacity: 0;
    padding: 0;
    transition: all 0.3s;

    .copy-btn {
      opacity: 0;
      pointer-events: none;
    }
  }

  .copy-btn {
    position: absolute;
    right: 2px;
    bottom: 2px;
    cursor: pointer;
    //transition: all 0.3s;
    padding: 2px;
    border-radius: 4px;
  }
}

::v-deep .bot{
  .el-loading-mask{
    background-color: rgba(210, 220, 234, 0.8);
    border-radius: 6px;

    .el-loading-spinner .path{
      stroke: #76aae1;
    }
  }
}
</style>
