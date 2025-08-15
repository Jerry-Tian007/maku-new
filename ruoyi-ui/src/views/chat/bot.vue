<template>
  <el-card style="position:relative"
    :body-style="{ height: 'calc(100% - 52px)', paddingBottom: '0', overflowY: 'auto' }">
    <template slot="header">
      <div class="flex-layout align-center start-end"
        style="display: flex; justify-content: space-between;gap:6px; overflow-x: auto;max-height: 80px;">
        <div style="flex: 10;display: flex; flex-wrap: wrap; justify-content: space-between">
          <div style="flex: 1;height:30px;" class="flex-layout align-center">
            <img :src="botImgUrl" style="width: 22px;height: 22px;border-radius: 16px;pointer-events: none" />
            <el-dropdown @command="handleCommand" trigger="hover"
              style="margin-left: 10px;height:20px;line-height: 20px;width:230px;">
              <span class="el-dropdown-link text-over" style="cursor: pointer;">
                {{ model }}<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-for="item in modelList" :key="item.type" :command="item.type"
                  :disabled="item.disable">
                  <div style="display: flex;align-items: center;">
                    <div style="display: flex;align-items: center">
                      <img width="14" height="14" style="border-radius: 10px;" :src="item.icon || placeholder" />
                      <span style="margin-left: 8px;">{{ item.type }}</span>
                    </div>
                  </div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div
            style="flex: 1;height:30px;line-height: 30px;text-align: center;display: flex; align-items: center; gap: 12px;justify-content: flex-start;min-width: 300px;">
            <!-- <el-switch v-model="isNetwork" :active-text="$t('AllInOne.networkedSearch')" :disabled="!hasNetwork"
            :class="{ disabled: !hasNetwork }"></el-switch> -->
            <el-switch v-model="newWordState" :disabled="!hasNetwork" @change="handleSwitch"
              :class="{ disabled: !hasNetwork }"></el-switch>
            <el-dropdown @command="handleNetWork" trigger="hover" :disabled="!newWordState">
              <span class="el-dropdown-link text-over" style="cursor: pointer;">
                {{ newWordState ? isNetworkLabel : $t('AllInOne.networkedSearch') }}<i
                  class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-for="item in netWorkList" :key="item.dictCode" :command="item.dictValue">
                  <div style="display: flex;align-items: center;">
                    <div style="display: flex;align-items: center">
                      <span>{{ item.dictLabel }}</span>
                    </div>
                  </div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
        <div style="flex: 1;height:30px;font-size: 22px;justify-content: flex-end;gap: 6px;"
          class="flex-layout align-center">
          <div :title="$t('AllInOne.askQuestion')" @click="showInput = !showInput">
            <svg-icon :icon-class="showInput ? 'color-edit-gray' : 'color-edit'" style="cursor: pointer;" />
          </div>

          <div :title="$t('AllInOne.clear')" @click="handleClearDialog">
            <svg-icon icon-class="color-delete" style="cursor: pointer;" />
          </div>

          <div :title="$t('AllInOne.history')" @click="handleShowHistory">
            <svg-icon icon-class="color-clock" style="cursor: pointer;" />
          </div>
        </div>
      </div>
    </template>
    <div ref="historyContent" style="height: 100%;overflow-y:auto;padding: 0 10px;position: relative;">
      <div v-for="(item, index) in historyList" :key="index" class="flex-layout mb10"
        :style="{ justifyContent: item.role === 'user' ? 'flex-end' : 'flex-start', gap: '6px' }">
        <div style="width:100%;" v-if="item.role === 'info'">
          <div class="flex-layout align-center center-center gap6">
            <el-divider class="flex-1" />
            <span class="fs-12 text-gray">
              {{ item.content }}
            </span>
            <el-divider class="flex-1" />
          </div>
        </div>
        <template v-else>
          <div v-if="item.role === 'bot'">
            <img :src="botImgUrl" class="avatar" />
          </div>
          <div :class="item.role" :body-style="item.type === 'image' ? { padding: '0' } : {}"
               :style="item.type === 'image' ? { borderRadius: '11px' } : {}"
               style="margin-top: 23px;max-width: 75%;position: relative;overflow: initial;">
            <el-card :class="item.role" :body-style="item.type === 'image' ? { padding: '0' } : {}"
                     :style="item.type === 'image' ? { borderRadius: '11px' } : {}"
                     style="position: relative;overflow: initial;">
              <div style="position: absolute;top: -23px;left: 0;min-width: 300px;" v-if="item.role === 'bot'">
                {{ item.modelType }}
              </div>
              <template v-if="item.loading">
                <span>{{ $t('privateDomain.thinkingAbout') }}</span><i class="el-icon-loading"
                                                                       style="margin-left: 4px;" />
              </template>
              <template v-else-if="item.type === 'image'">
                <img :src="item.content" @click="handleShowImage(item.content)"
                     style="max-width: 200px;max-height: 200px;display: block;cursor: pointer;" />
              </template>
              <template v-else>
                <div v-if="item.reasoning" style="margin-bottom: 15px;">
                  <div style="line-height: 30px;display: flex;align-items: center;cursor: pointer;"
                       @click="handleReasoning(item)">
                  <span style="font-size: 13px;color: gray;">
                    {{ $t('AllInOne.deeplyPondered') }}
                    <span v-show="item.reasoningTime">（{{ $t('AllInOne.deeplyTime') }} {{ item.reasoningTime
                      }} {{ $t('AllInOne.second') }}）</span>
                  </span>
                    <svg-icon v-show="!item.showReasoningState" icon-class="reasonging_bottom"
                              style="font-size: 18px;margin-left: 6px;" />
                    <svg-icon v-show="item.showReasoningState" icon-class="reasonging_top"
                              style="font-size: 18px;margin-left: 6px;" />
                  </div>
                  <markdown-it-vue v-show="item.showReasoningState" :content="item.reasoning" :options="options"
                                   align="left" />
                </div>
                <!-- <markdown-it-vue class="md-body" id="md-body" :content="item.content"
                  :options="options" align="left" /> -->
                <markdown-it-vue v-if="item.role === 'user'" class="md-body" id="md-body" :content="item.content"
                                 :options="options" align="left" />
                <MarkdownASTProcessor v-if="item.role === 'bot'" :content="item.content" />
                <div style="margin-top: 12px;" v-if="item.linkAndTitle && item.linkAndTitle.length > 0">
                <span style="font-size: 13px;color: gray;cursor: pointer;" @click="handleShowRefer(item.linkAndTitle)">
                  {{ $t('AllInOne.referenceSource') }} ·
                  {{ item.linkAndTitle.length }}
                  >
                </span>
                </div>
              </template>
            </el-card>
            <div class="completionTokensCla" v-if="item.shouToken ">
              {{ $t('AllInOne.completionTokens')+ ': '+ (item.completionTokens || 0) + ' Token'}}
            </div>
          </div>
          <div v-if="item.role === 'user'">
            <img :src="userAvatar" class="avatar" />
          </div>
        </template>
      </div>
      <div :class="{ 'blank': true, 'expand': showInput }" />
    </div>
    <el-image-viewer v-if="imgViewerVisible" :on-close="closeViewer" :url-list="imgList" />
    <div :class="{ 'flex-layout': true, 'align-center': true, 'single-input': true, 'hidden': !showInput }">
      <el-input v-model="sendData" :placeholder="$t('privateDomain.sendInputText')" style="width: 100%;"
        @keydown.native="keydownfn" type="textarea" class="no-border" :rows="1" resize="none" />
      <div v-if="sendData && !isLoading" style="margin:0 8px;font-size: 28px;" @click="handleSendData()">
        <svg-icon icon-class="color-send" style="cursor:pointer;" />
      </div>
      <div v-else style="margin:0 8px;font-size: 28px;">
        <svg-icon icon-class="color-not-send" />
      </div>
    </div>
    <div class="history_list_div" id="history_list_div" v-show="index === 0 && bots === 1">
      <div :class="{ 'flex-layout': true, 'align-center': true, 'history_list': true, 'hidden': !showHistoryState }">
        <div class="history_operation_div">
          <div style="width: 100%;display: flex; align-items: center;">
            <el-button type="primary" :icon="sessionId ? 'el-icon-plus' : 'el-icon-check'"
              style="width: 93%;border-radius: 12px;" @click="handleConversation">
              {{ sessionId ?
                $t('privateDomain.addConversation') : $t('privateDomain.newQaConversation') }}
            </el-button>
            <div v-show="showHistoryState" class="fold-button" @click="handleShowHistoryList(false)">
              <svg-icon icon-class="fold" style="font-size: 20px;cursor: pointer;" />
            </div>
          </div>
          <div style="display: flex;align-items: center;border-bottom: 1px solid #E0E0E0;width: 100%;margin-top: 12px;">
            <svg-icon icon-class="search" style="font-size: 20px;color: #a1a1a1;" />
            <el-input class="no-border" v-model="historySearchData" :placeholder="$t('search')"
              @input="handleSearchSession" />
          </div>
        </div>
        <div v-show="!sessionListLoading" class="content_list_div" v-infinite-scroll="sessionLoading">
          <div v-for="(date, index) in sessionTimeList" style="border-bottom: 1px solid #E0E0E0;">
            <div class="session-date text-gray" style="font-size: 14px;">{{ date.type }}</div>
            <div :class="{ 'flex-layout align-center': true, 'editing': isEditSessions }"
              style="overflow: hidden;width: 100%;" v-for="(item, index) in date.sessions" :key="index">
              <div :class="{
                'session-item flex-1 flex-layout align-center start-end': true,
                'active': item.sessionId === sessionId,
                'is-chosen': isEditSessions && editSessions.includes(item.sessionId)
              }" :title="item.sessionName" @click="handleClickSession(item)" style="gap: 12px;">
                <div class="text-over" style="color: #333333;">
                  {{ item.sessionName }}
                </div>
                <div :class="{ 'tool-item-button': true }" class="session-item-deleted"
                  @click.stop="handleDeleteSessionById(item.sessionId)">
                  <i class="el-icon-delete" />
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-show="sessionListLoading" class="content_list_div"
          style="display: flex; align-items: center;justify-content: center;">
          <svg-icon icon-class="loading" style="font-size: 30px;animation: spin 2s linear infinite;" />
        </div>
      </div>
      <div class="history_button_div" v-show="index === 0 && bots === 1">
        <div v-show="!showHistoryState" :class="{ 'fold-button': true, 'unfold': true, 'hide': showHistoryState }"
          @click="handleShowHistoryList(true)">
          <svg-icon icon-class="unfold" style="font-size: 20px;cursor: pointer;" />
        </div>
      </div>
    </div>

    <el-dialog :visible.sync="showHistory" width="60%" append-to-body>
      <div style="width: 100%;height: 80vh;">
        <div style="width: 100%;text-align: center;">
          <span class="title">{{ $t('AllInOne.historyRecords') }}</span>
        </div>

        <el-tabs v-model="historyType" style="width: 100%;" class="is-center mt10 pd-h20">
          <el-tab-pane :label="$t('AllInOne.list')" name="list">
            <div style="width: 100%;height: calc(80vh - 120px);overflow-y: auto;">
              <div style="width: 100%;gap: 20px;" class="flex-layout column pd-h20 pd-v6">
                <div v-for="(item, index) in historyList" class="flex-layout pd-h20 gap10">
                  <img v-if="item.role === 'user'" :src="userAvatar" class="avatar small" />
                  <img v-else-if="item.role === 'bot'" :src="botImgUrl" class="avatar small" />
                  <template v-if="item.role === 'info'">
                    <div style="width:100%;" class="flex-layout center-center gap6 text-gray">
                      <el-divider class="flex-1" />
                      <span>{{ item.content }}</span>
                      <el-divider class="flex-1" />
                    </div>
                  </template>
                  <div v-else class="flex-layout column" style="margin-top: 6px;">
                    <div class="hint-text fs-12">{{ item.timestamp }}</div>
                    <div :class="{ 'pd-v2': true, 'fs-16': true, 'main-color': item.role === 'user' }">
                      <template v-if="item.type === 'image'">
                        <img :src="item.content" style="max-width: 200px;max-height: 200px;" />
                      </template>
                      <template v-else>
                        {{ item.content }}
                      </template>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="Markdown" name="md" style="padding: 0 20px;">
            <div style="position:absolute;z-index:1;top: 10px;right:30px;cursor:pointer;" @click="handleDownloadMD">
              <svg-icon icon-class="md-download" style="font-size: 32px;" />
            </div>
            <el-card class="blue" style="width: 100%;height: calc(80vh - 120px);"
              :body-style="{ overflowY: 'auto', padding: '12px', height: '100%', position: 'relative' }">
              <div style="width: 100%;" class="md-show pd-h10 primary-text">
                {{ historyMd }}
              </div>
            </el-card>
          </el-tab-pane>
        </el-tabs>

      </div>

    </el-dialog>
  </el-card>
</template>
<script>
import GptApi from "@/views/chat/bots/gpt.vue";
import userAvatar from "@/assets/images/default_profile.png";
import { chatQuestion } from "@/api/chat";
import placeholder from "@/assets/logo/logo-placeholder.jpg";
import { findModelParam, findHistoryList, findHistoryItem, deletedHistoryById } from '@/api/chat';
import dayjs from 'dayjs';
import { listData } from '@/api/system/dict/data';
import { parseTime } from "@/utils/ruoyi";
import { nanoid } from 'nanoid';
import { getSessionMark } from "@/utils";
import marked from 'marked';
import { getToken } from '@/utils/auth';
import MarkdownASTProcessor from './components/MarkdownASTProcessor.vue'
import MarkdownIt from 'markdown-it';
import multimdTable from 'markdown-it-multimd-table';
import { parse as csvParse } from 'papaparse'

export default {
  name: 'ChatBot',
  components: {
    MarkdownASTProcessor,
    GptApi,
    'el-image-viewer': () => import('element-ui/packages/image/src/image-viewer')
  },
  props: {
    index: {
      type: Number,
      default: 0
    },
    bots: {
      type: Number,
      default: 0
    }
  },
  computed: {
    modelList() {
      return this.$store.state.user.botList.filter(item => item.scene !== 'embedding');
    }
  },
  created() {
    this.renderAllBlocks();
  },
  data() {
    return {
      imgViewerVisible: false,
      imgList: [],
      options: {
        markdownIt: {
          linkify: false,
          html: true,
          typographer: true
        },
      },
      placeholder,
      userAvatar,
      model: this.$t('systemManager.noAvailableModels'),
      historyMd: '',
      historyType: 'list',
      showHistory: false,
      showInput: false,
      // modelList: [],
      botImgUrl: placeholder,
      modelName: '',
      sendData: '',
      isNetwork: '',
      isNetworkLabel: this.$store.getters.windowModel[this.index]['isNetworkLabel'],
      hasNetwork: false,
      newWordState: this.$store.getters.windowModel[this.index]['newWordState'],
      historyList: [],
      messages: [],
      showHistoryState: this.$store.getters.windowModel[this.index]['showHistoryState'],
      chatParams: {
        "jsonFormat": false,
        "stream": true,
        "temperature": 0.0,
        "maxTokens": 1,
        "maxWords": 120,
        "needMaxWords": true,
        "tools": "",
        "topK": 1,
        "topP": 0.0,
        "repetitionPenalty": 0.0,
        "needOnlineSearch": this.$store.getters.windowModel[this.index]['needOnlineSearch']
      },
      scene: null,
      modelTypeId: null,
      prompt: '你必须用中文回答用户的问题',

      singleQA: {
        question: '',
        answer: '',
        tokenUsage: 0,
      },
      netWorkList: [],
      isLoading: false,
      chatTable: this.$store.getters.windowModel[this.index]['chatTable'],
      chatTableIndex: this.$store.getters.windowModel[this.index]['chatTableIndex'],
      sessionTimeList: this.$store.getters.windowModel[this.index]['sessionTimeList'],
      sessionOldList: [],
      isEditSessions: false,
      editSessions: [],
      sessionId: this.$store.getters.windowModel[this.index]['sessionId'],
      reasoningShowState: true,
      resTableList: [],
      historySearchData: '',
      sessionPageNum: this.$store.getters.windowModel[this.index]['sessionPageNum'],
      sessionListLoading: false,
      sessionTotal: 0,
      sessionPageSize: 15,
      socket: null,
      scrollTimeout: 0,
      startTime: null,
      md: null,
      sessions: [],
      sockets: new Map(),
    }
  },
  watch: {
    modelList: function (newVal, oldVal) {
      if (newVal.findIndex(item => item.type === this.model) === -1) {
        if (newVal.length > 0) {
          this.handleCommand(newVal[0].type);
        } else {
          this.model = '暂无可用模型';
          this.scene = null;
          this.modelTypeId = null;
          this.botImgUrl = placeholder;
        }
      } else {
        this.handleCommand(this.model);
      }
    }
  },
  mounted() {
    // this.modelList = this.$store.state.user.botList;
    if (this.$store.getters.windowModel[this.index]['botType']) {
      this.handleCommand(this.$store.getters.windowModel[this.index]['botType']);
    } else {
      if (this.modelList.length > 0) {
        this.handleCommand(this.modelList[0].type);
      }
    }
    /* if (this.modelList.length > 0) {
      if (this.index < this.modelList.length) {
        this.handleCommand(this.modelList[this.index].type);
      } else {
        this.handleCommand(this.modelList[this.modelList.length - 1].type);
      }
    } */
    document.addEventListener("click", this.handleDocumentClick);
    this.getNetWordList();
    //this.handleHistoryList();
  },
  beforeDestroy() {
    document.removeEventListener("click", this.handleDocumentClick);
    this.sockets?.forEach(v => {
      v?.close();
    });
  },
  methods: {
    renderAllBlocks() {
      this.md = new MarkdownIt().use(multimdTable, {
        multiline: true,
        rowspan: true,
        headerless: true
      });

      const defaultRender = this.md.renderer.rules.fence || function (tokens, idx, options, env, slf) {
        return slf.renderToken(tokens, idx, options);
      };

      // 自定义 fence 渲染器
      this.md.renderer.rules.fence = (tokens, idx, options, env, slf) => {
        const token = tokens[idx];
        const lang = token.info.trim().toLowerCase();

        if (['markdown', 'csv', 'psv'].includes(lang)) {
          try {
            const data = this.parseCodeContent(token.content, lang);
            if (data.length === 0) {
              return defaultRender(tokens, idx, options, env, slf);
            }
            return this.customProcessor(data, lang, token.content);
          } catch (e) {
            console.error(`渲染失败（${lang}）`, e);
            return defaultRender(tokens, idx, options, env, slf);
          }
        };
        return defaultRender(tokens, idx, options, env, slf);
      };
    },
    parseCodeContent(content, type) {
      switch (type) {
        case 'markdown':
          const fakeContent = `\n${content}\n`
          const tokens = this.md.parse(fakeContent, {})
          return this.extractTables(tokens);
        case 'csv':
          return csvParse(content.trim(), { skipEmptyLines: true }).data
        case 'psv':
          return content.trim().split('\n').map(line =>
            line.split(/(?<!\\)\|/g).map(cell =>
              cell.trim().replace(/\\\|/g, '|')
            )
          )
        default:
          return []
      }
    },
    extractTables(tokens) {
      let rows = []
      let currentRow = []
      let inRow = false

      for (const token of tokens) {
        if (token.type === 'tr_open') {
          currentRow = []
          inRow = true
        } else if (token.type === 'tr_close') {
          inRow = false
          rows.push(currentRow)
        } else if (token.type === 'inline' && inRow) {
          currentRow.push(token.content)
        }
      }
      return rows
    },
    handleStreamState(state) {
      this.chatParams.stream = state;
    },
    //获取联网类型列表
    getNetWordList() {
      listData({ pageNum: 1, pageSize: 10, dictType: 'net_work_utils' }).then(res => {
        if (res.code === 200) {
          this.netWorkList = res.rows;
          /* this.chatParams.needOnlineSearch = res.rows[0].dictValue;
          this.isNetworkLabel = res.rows[0].dictLabel; */
        }
      }).catch(err => {
        this.$message.error(this.$t('AllInOne.findModelError'));
      })
    },
    //监听点击事件，监听问答中导出csv按钮
    handleDocumentClick(event) {
      const historyListDiv = document.getElementById('history_list_div');
      if (historyListDiv && !historyListDiv.contains(event.target)) {
        this.handleShowHistoryList(false);
      }
      if (event.target.className === 'export_csv') {
        const index = event.target.id.split('_')[2];
        const id = event.target.id.split('_')[3];
        if (parseInt(index) === this.index) {
          this.handleExportCsv(id);
        }
      }
    },
    handleConversation() {
      if (this.sessionId) {
        this.sessionId = '';
        this.handleClearDialog();
      }
    },
    handleHistoryList() {
      if (this.index === 0 && this.bots === 1) {

        this.sessionTimeList = this.formatterList(list);
      }
    },
    //历史会话时间处理
    formatterList(list) {
      //根据时间分为 今天 近一周 近一个月 近一年 和更早，分类加到sessions中。praseTime没有周的格式化，所以没有周
      let sessionList = [];
      let type = this.$t('privateDomain.earlier');
      let now = new Date();
      list.forEach(item => {
        if (parseTime(item.updateTime, '{y}-{m}-{d}') === parseTime(now, '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.today');
        } else if (parseTime(item.updateTime, '{y}-{m}-{d}') >= parseTime(new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000), '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.pastWeek');
        } else if (parseTime(item.updateTime, '{y}-{m}-{d}') >= parseTime(new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000), '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.pastMonth');
        } else if (parseTime(item.updateTime, '{y}-{m}-{d}') >= parseTime(new Date(now.getTime() - 365 * 24 * 60 * 60 * 1000), '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.pastYear');
        } else {
          type = this.$t('privateDomain.earlier');
        }

        let data = {
          updateTime: item.updateTime,
          sessionName: item.sessionTitle,
          sessionId: item.sessionId || 0,
          id: item.sessionId
        }

        let index = sessionList.findIndex(session => session.type === type);

        if (index === -1) {
          sessionList.push({
            type: type,
            sessions: [data]
          });
        } else {
          sessionList[index].sessions.push(data);
        }
      })
      return sessionList;
    },
    //点击历史会话某条数据
    handleClickSession(item) {
      if (this.isEditSessions) {
        if (this.editSessions.includes(item.id)) {
          this.editSessions = this.editSessions.filter(session => session !== item.id);
        } else {
          this.editSessions.push(item.id);
        }
      } else {
        this.handleUseSession(item.sessionId);
      }
    },
    //打开某个历史会话记录
    handleUseSession(sessionId) {
      if (this.sessions.includes(sessionId)) {
        this.$emit('update:disableInput', true);
      } else {
        this.$emit('update:disableInput', false);
      }
      this.handleClearDialog();
      this.sessionId = sessionId;
      const windowModel = this.$store.getters.windowModel;
      windowModel[this.index]['sessionId'] = this.sessionId;
      this.$store.dispatch('setWindowModel', windowModel);

      findHistoryItem({ sessionId }).then(res => {
        if (res.code === 200) {
          const historyObj = this.$store.getters.chatHistoryList;
          const list = res.data;
          const resHistory = [];
          if (list.length > 0) {
            list.forEach(item => {
              resHistory.push({
                role: 'user',
                content: item.question,
                type: 'text'
              });
              let reasoning = '';
              if (item.reasoningContent) {
                reasoning = '<div class="chat_reasoning">\n' + item.reasoningContent + '\n</div>'
              }
              let linkAndTitle = [];
              if (linkAndTitle) {
                linkAndTitle = JSON.parse(item.linkAndTitle);
              }
              resHistory.push({
                role: 'bot',
                content: this.md.render(item.answer || this.$t('AllInOne.modelFailed')),
                reasoning,
                showReasoningState: true,
                id: nanoid(),
                loading: false,
                type: 'text',
                modelType: item.model,
                linkAndTitle,
                completionTokens: item.completionTokens || 0,
                shouToken: item.answer ? true : false
              });
            });
          }
          this.historyList = resHistory;
          historyObj[this.index] = resHistory;
          this.$store.dispatch("setHistoryList", historyObj);
        }
      }).catch(err => {
        console.log(err)
        this.$message.error(this.$t('AllInOne.searchSessionError'));
      });
    },
    customProcessor(data, format, content) {
      if (data.length === 0) {
        return data;
      }
      // 为不同格式添加不同处理
      switch (format) {
        case 'markdown':
          this.chatTableIndex = this.chatTableIndex + 1;
          this.chatTable.push({
            id: this.chatTableIndex,
            content: content
          });
          this.saveStoreData();
          return `\n<div class="table-div">\n <div class="table_operation_div">\n <button class="export_csv" id="export_csv_${this.index}_${this.chatTableIndex}">${this.$t('AllInOne.exportCsv')}</button> \n </div> \n\n${this.arrayToHtmlTable(data)}\n\n</div>\n`;
        case 'csv':
          this.chatTableIndex = this.chatTableIndex + 1;
          const resCsvContent = this.convertToMarkdown(content, ',');
          this.chatTable.push({
            id: this.chatTableIndex,
            content: resCsvContent
          });
          this.saveStoreData();
          return `\n<div class="table-div">\n <div class="table_operation_div">\n <button class="export_csv" id="export_csv_${this.index}_${this.chatTableIndex}">${this.$t('AllInOne.exportCsv')}</button> \n </div> \n\n${this.arrayToHtmlTable(data)}\n\n</div>\n`;
        case 'psv':
          this.chatTableIndex = this.chatTableIndex + 1;
          const resPsvContent = this.convertToMarkdown(content, '|');
          this.chatTable.push({
            id: this.chatTableIndex,
            content: resPsvContent
          });
          this.saveStoreData();
          return `\n<div class="table-div">\n <div class="table_operation_div">\n <button class="export_csv" id="export_csv_${this.index}_${this.chatTableIndex}">${this.$t('AllInOne.exportCsv')}</button> \n </div> \n\n${this.arrayToHtmlTable(data)}\n\n</div>\n`;
        default:
          return data;
      }
    },
    saveStoreData() {
      const windowModel = this.$store.getters.windowModel;
      windowModel[this.index]['chatTable'] = this.chatTable;
      windowModel[this.index]['chatTableIndex'] = this.chatTableIndex;
      this.$store.dispatch("setWindowModel", windowModel);
    },
    arrayToHtmlTable(data) {
      const [header, ...rows] = data

      const thead = `<thead><tr>${header.map(h => `<th>${h}</th>`).join('')}</tr></thead>`
      const tbody = `<tbody>${rows.map(row =>
        `<tr>${row.map(cell => `<td>${cell}</td>`).join('')}</tr>`
      ).join('')}</tbody>`

      return `<table border="1">${thead}${tbody}</table>`
    },
    //删除某条历史会话
    handleDeleteSession(id) {
      this.$confirm(this.$t('privateDomain.okDeleted'), this.$t('warding'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.fullScreenLoading = true;
        //删除会话
        /*  removeUserSession(id).then(() => {
           this.handleNewSession();
           this.getSessionList();
           this.fullScreenLoading = false;
         }).catch(err => {
           this.fullScreenLoading = false;
           console.log(err)
           this.$message.error(err);
         }); */
      }).catch(() => {
      });
    },
    //展开或收起推理过程
    handleReasoning(chatContent) {
      const historyObj = this.$store.getters.chatHistoryList;
      const history = historyObj[this.index];
      const state = !chatContent.showReasoningState;
      history.forEach(item => {
        if (item.id) {
          if (item.id === chatContent.id) {
            item.showReasoningState = state;
          }
        }
      });
      this.historyList.forEach((item, index) => {
        if (item.id) {
          if (item.id === chatContent.id) {
            const newItem = { ...item, showReasoningState: state };
            this.$set(this.historyList, index, newItem);
          }
        }
      });
      historyObj[this.index] = history;
      this.$store.dispatch("setHistoryList", historyObj);
    },
    //选择联网类型
    handleNetWork(command) {
      let type = this.netWorkList.find(item => item.dictValue === command);
      this.chatParams.needOnlineSearch = command;
      this.isNetworkLabel = type.dictLabel;
      const windowModel = this.$store.getters.windowModel;
      windowModel[this.index]['isNetworkLabel'] = type.dictLabel;
      windowModel[this.index]['needOnlineSearch'] = command;
      this.$store.dispatch("setWindowModel", windowModel);
    },
    //开启\关闭联网业务
    handleSwitch(state) {
      const windowModel = this.$store.getters.windowModel;
      if (!state) {
        this.chatParams.needOnlineSearch = '';
        windowModel[this.index]['isNetworkLabel'] = '';
        windowModel[this.index]['needOnlineSearch'] = '';
      } else {
        this.chatParams.needOnlineSearch = this.netWorkList[1].dictValue;
        this.isNetworkLabel = this.netWorkList[1].dictLabel;
        windowModel[this.index]['isNetworkLabel'] = this.netWorkList[1].dictLabel;
        windowModel[this.index]['needOnlineSearch'] = this.netWorkList[1].dictValue;
      }
      windowModel[this.index]['newWordState'] = state;
      this.$store.dispatch("setWindowModel", windowModel);
    },
    //选择模型时的业务处理
    handleCommand(command) {
      if (!command) {
        command = this.modelList[this.index].type;
      }
      let model = this.modelList.find(item => item.type === command);
      this.botImgUrl = model.icon || placeholder;
      this.modelTypeId = model.id;
      this.isNetwork = false;
      this.hasNetwork = true;
      this.scene = model.scene;
      this.model = command;
      const windowModel = this.$store.getters.windowModel;
      windowModel[this.index]['botType'] = command;
      this.$store.dispatch("setWindowModel", windowModel);
      this.handleFindModelParam(command);
      this.$emit('update:modelType', command);
    },
    //查询model对应的提示词信息
    handleFindModelParam(command) {
      findModelParam({ modelName: command }).then(res => {
        if (res.code === 200) {
          this.prompt = JSON.parse(res.data.promptSetting).prompt;
          const configSetting = JSON.parse(res.data.configureSetting);
          if (configSetting.length > 0) {
            configSetting.forEach(item => {
              if (this.chatParams.hasOwnProperty(item.name)) {
                this.chatParams[item.name] = item.value;
              }
            });
          }
        }
      }).catch(err => {
        this.$message.error(this.$t('AllInOne.findModelError'));
        this.sessionListLoading = false;
      });
    },
    //回车发送问题
    keydownfn(event) {
      if (event.keyCode == 13) {
        if (!event.shiftKey && !event.ctrlKey && !event.altKey && !event.metaKey) {
          event.preventDefault();
          this.handleSendData();
        } else if (!event.shiftKey) {
          this.sendData = this.sendData + '\n';
        }
      }
    },
    //问题发送预处理
    handleSendData(text, fileList = []) {
      this.$emit('update:disableInput', true);
      if (text) this.sendData = text;
      if (!this.sendData || !this.scene || this.isLoading) return;
      /* if (this.scene !== 'vision' && fileList.length > 0) {
        this.historyList.push({
          role: 'info',
          content: this.$t('AllInOne.modelCurrently'),
          type: 'info'
        });
        return;
      } */
      this.historyList.push({
        role: 'user',
        content: this.sendData,
        type: 'text'
      });

      let question = this.singleQA.question = this.sendData;

      this.messages.push({
        role: 'user',
        content: this.singleQA.question,
        type: 'text'
      });

      /* let images = fileList.filter(item => item.isImage);
      if (this.scene === 'vision' && images.length > 0) {
        this.historyList.push({
          role: 'user',
          content: images[0].url,
          type: 'image'
        });
        this.messages.push({
          role: 'user',
          content: images[0].url.split(',')[1],
          img_type: images[0].imageType,
          type: 'image'
        });
      } */

      setTimeout(() => {
        this.historyList.push({
          role: 'bot',
          content: '',
          loading: true,
          type: 'text',
          modelType: this.model,
          timestamp: new Date().toLocaleString()
        });
        this.isLoading = true;
        this.$nextTick(() => {
          this.handleScrollToBottom();
        })
        this.sendRequest(question);
      }, 500);

    },
    //问题发送
    sendRequest(question) {
      // 发送请求
      let data = {
        "modelType": this.model,
        "modelTypeId": this.modelTypeId,
        "scene": this.scene,
        "chatParams": this.chatParams,
        "history": this.messages,
        "fileMark": this.$store.getters.windowModel[this.index].fileMark,
        "prompt": this.prompt,
        "question": question,
        //"needOnlineSearch": this.isNetwork  弃用 转移至chatParams
      };
      if (this.sessionId === '') {
        this.sessionId = getSessionMark();
        data.sessionId = this.sessionId;
        const windowModel = this.$store.getters.windowModel;
        windowModel[this.index]['sessionId'] = this.sessionId;
        this.$store.dispatch('setWindowModel', windowModel);
      } else {
        data.sessionId = this.sessionId;
      }
      if (this.chatParams.stream) {
        if (this.socket) {
          this.startTime = performance.now();
          this.socket.send(JSON.stringify(data));
        } else {
          this.initWebSocket(this.sessionId, JSON.stringify(data))
        }
      } else {
        let index = this.historyList.length - 1;
        const startTime = performance.now();
        chatQuestion(data).then(res => {
          if (res.code === 200) {

            if (res.data.code === 200) {
              this.singleQA.answer = res.data.data;
              this.singleQA.tokenUsage = res.data.tokenUsage;
              this.singleQA.completionTokens = data.completionTokens;
              //this.historyList[index].content = this.renderMarkdownWithTableExport(this.singleQA.answer);
              this.historyList[index].content = this.md.render(this.singleQA.answer || this.$t('AllInOne.modelFailed'));
              if (res.data.reasoningContent) {
                this.historyList[index].reasoning = '<div class="chat_reasoning">\n' + res.data.reasoningContent + '\n</div>';
              }
              this.historyList[index].linkAndTitle = res.data.linkAndTitle;
              this.historyList[index].showReasoningState = true;
              this.historyList[index].id = nanoid();
              this.historyList[index].loading = false;
              this.historyList[index].modelType = this.model;
              this.messages.push({
                role: 'assistant',
                content: this.singleQA.answer,
                type: 'text',
                shouToken: true,
              });
              const endTime = performance.now();
              this.historyList[index].reasoningTime = ((endTime - startTime) / 1000).toFixed(0);
              let history = this.$store.getters.chatHistoryList;
              history[this.index] = this.historyList;
              this.$store.dispatch("setHistoryList", history);


              let message = this.$store.getters.chatMessage;
              message[this.index] = this.messages;
              this.$store.dispatch("setMessage", message);
              this.$nextTick(() => {
                this.handleScrollToBottom();
              })
            } else {
              this.historyList[index].shouToken = false;
              this.historyList[index].loading = false;
              this.historyList[index].content = this.$t('AllInOne.modelFailed');
            }

          } else {
            this.historyList[index].shouToken = false;
            this.historyList[index].content = this.$t('AllInOne.requestError');
            this.historyList[index].loading = false;
          }
          this.isLoading = false;
        }).catch(err => {
          this.historyList[index].shouToken = false;
          this.historyList[index].content = this.$t('AllInOne.requestError');
          this.historyList[index].loading = false;
          this.isLoading = false;
        });
      }
      this.sendData = '';
    },
    //创建WebSocket连接并发送问题
    initWebSocket(sessionId, msg) {
      let currUrl = `${process.env.VUE_APP_CHAT_WEB_SOCKET_URL}/websocket/allInOne/chat/`;
      // 创建WebSocket连接
      this.socket = new WebSocket(currUrl + sessionId, [getToken()]);
      // 监听WebSocket连接成功事件
      let endTime = null;
      this.socket.onopen = () => {
        this.startTime = performance.now();
        this.socket.send(msg);
        if (!this.sessions.includes(sessionId)) {
          this.sessions.push(sessionId);
          this.sockets.set(sessionId,this.socket);
        }
      };
      this.socket.onmessage = (event) => {
        let index = this.historyList.length - 1;
        const resObj = JSON.parse(event.data);
        if (resObj.event === 'add' || resObj.event === 'finish') {
          const data = resObj.data;
          const resSessionId = resObj.sessionId;

          if (resSessionId === this.sessionId) {
            this.singleQA.answer = data.data;
            this.singleQA.tokenUsage = data.tokenUsage;
            //this.historyL监听WebSocket消息事件ist[index].content = this.renderMarkdownWithTableExport(this.singleQA.answer);
            this.$set(this.historyList[index], 'content', this.md.render(data.data || ''));
            this.$set(this.historyList[index], 'reasoning', '<div class="chat_reasoning">\n' + data.reasoningContent + '\n</div>');
            if (data.linkAndTitle && data.linkAndTitle.length > 0) {
              this.$set(this.historyList[index], 'linkAndTitle', data.linkAndTitle);
            }
            this.historyList[index].showReasoningState = true;
            this.historyList[index].id = nanoid();
            this.historyList[index].loading = false;
            this.historyList[index].modelType = this.model;


            let history = this.$store.getters.chatHistoryList;
            history[this.index] = this.historyList;
            this.$store.dispatch("setHistoryList", history);
          }

          if (resObj.event === 'finish') {
            if (this.sessions.includes(resSessionId)) {
              this.sessions = this.sessions.filter( v => v !== resSessionId)
              this.sockets.get(resSessionId)?.close();
              this.sockets.delete(resSessionId);
            }
            this.$set(this.historyList[index], 'completionTokens', resObj.data.completionTokens||0);
            this.$set(this.historyList[index], 'shouToken', true);
            let message = this.$store.getters.chatMessage;
            this.messages.push({
              role: 'assistant',
              content: this.singleQA.answer,
              type: 'text',
              completionTokens: data.completionTokens,
              shouToken: true,
            });

            message[this.index] = this.messages;
            this.$store.dispatch("setMessage", message);
            this.$emit('update:disableInput', false);
          }
          if (data.data !== '' && !this.historyList[index].reasoningTime) {
            endTime = performance.now();
            this.historyList[index].reasoningTime = ((endTime - this.startTime) / 1000).toFixed(0);
            this.startTime = null;
          }
        } else if (resObj.event === 'error') {
          this.historyList[index].content = this.$t('AllInOne.requestError');
          this.historyList[index].shouToken = false;
          this.historyList[index].loading = false;
          this.$emit('update:disableInput', false);
          endTime = performance.now();
          this.historyList[index].reasoningTime = ((endTime - this.startTime) / 1000).toFixed(0);
          this.startTime = null;
        } else if (resObj.event === 'webError') {
          this.historyList[index].content = this.$t('AllInOne.requestError');
          this.historyList[index].loading = false;
          this.historyList[index].loading = false;
          this.$emit('update:disableInput', false);
          endTime = performance.now();
          this.historyList[index].reasoningTime = ((endTime - this.startTime) / 1000).toFixed(0);
          this.startTime = null;
        }
        this.$nextTick(() => {
          this.handleScrollToBottom();
        })
        this.isLoading = false;
      };

      // 监听WebSocket关闭事件
      this.socket.onclose = () => {
        /* let index = this.historyList.length - 1;
        this.historyList[index].content = this.$t('AllInOne.requestError');
        this.historyList[index].loading = false; */
        this.socket = null;
      };

      // 监听WebSocket错误事件
      this.socket.onerror = () => {
        /* let index = this.historyList.length - 1;
        this.historyList[index].content = this.$t('AllInOne.requestError');
        this.historyList[index].loading = false; */
        this.socket = null;
      };
    },
    containsMarkdownTable(markdown) {
      /* const tableRegex = /(?:\n|^)(\|(?:[^\n|]+\|?)+)\n\|(?:[-:\s|]+)\|\n((?:\|(?:[^\n]+\|?)+\n?)*)/g;
      const tables = markdown.match(tableRegex);
      return tables || []; */
      const tableRegex = /(?:^|\n)(\|(?:[^\n|]+\|)+)\n\|(?:[-: ]+\|)+\n((?:\|(?:[^\n|]+\|)+\n?)*)/g;
      const matches = [...markdown.matchAll(tableRegex)];
      // 将完整表格字符串还原（含表头、分隔线、数据行）
      const tables = matches.map(match => match[0]);

      return tables || [];
    },
    //表格内容转csv数据
    parseMarkdownToCSV(mdTable) {
      // 分割行并过滤无效行（如空行、分隔行）
      const rows = mdTable.trim().split('\n').filter(line => {
        return line.trim().startsWith('|') && !line.includes('---');
      });

      // 转换为二维数组
      return rows.map(line => {
        // 分割单元格并去除首尾空格及竖线
        return line.split('|')
          .slice(1, -1) // 移除首尾空元素
          .map(cell => {
            const text = cell.trim();
            // 处理含逗号的内容（用双引号包裹）
            return text.includes(',') ? `"${text}"` : text;
          });
      });
    },
    extractMarkdownTablesUsingMarked(markdown) {
      const tokens = marked.lexer(markdown);
      const tables = [];

      // 当前偏移量，用于精确从原文中定位
      let currentIndex = 0;

      for (const token of tokens) {
        if (token.type === 'table') {
          // 找出 token 前后内容，定位表格段落
          const remainingText = markdown.slice(currentIndex);

          // 尝试匹配表格（Markdown原始写法）从当前位置开始
          const match = remainingText.match(/(?:^|\n)(\|[^\n]+\|\n\|[-:| ]+\|\n(?:\|[^\n]+\|\n?)*)/);

          if (match) {
            tables.push(match[1] || match[0]);
            // 更新 currentIndex 避免重复匹配
            currentIndex += match.index + match[0].length;
          }
        }
      }

      return tables;
    },
    //处理返回的答案中表格和代码块相关的内容添加导出按钮
    renderMarkdownWithTableExport(markdown) {

      let list = this.containsMarkdownTable(markdown);

      const transformMarkdown = this.handlePsvData(markdown);

      let newMarkdown = transformMarkdown;

      if (this.resTableList.length > 0) {
        this.resTableList.forEach(item => {
          list.forEach(listItem => {
            const similarity = this.getStringSimilarity(item, listItem).toFixed(0);
            if (similarity < 90) {
              this.chatTable.push({
                id: this.chatTableIndex,
                content: listItem
              });
              const divWrappedTable = this.addDivToTable(listItem, this.chatTableIndex);
              this.chatTableIndex = this.chatTableIndex + 1;
              newMarkdown = newMarkdown.replace(listItem, divWrappedTable);
            }
          })
        });
      } else {
        if (list.length > 0) {
          let divWrappedTableList = [];
          list.forEach(item => {
            this.chatTable.push({
              id: this.chatTableIndex,
              content: item
            });
            const divWrappedTable = this.addDivToTable(item, this.chatTableIndex);
            divWrappedTableList.push(divWrappedTable);
            this.chatTableIndex = this.chatTableIndex + 1;
          });
          let index = 0;
          list.forEach(item => {
            // 用 <div> 包裹的表格替换原来的表格
            newMarkdown = newMarkdown.replace(item, () => divWrappedTableList[index]);
            index++;
          });
        }
      }

      const windowModel = this.$store.getters.windowModel;
      windowModel[this.index]['chatTable'] = this.chatTable;
      windowModel[this.index]['chatTableIndex'] = this.chatTableIndex;
      this.$store.dispatch("setWindowModel", windowModel);
      return newMarkdown;
    },
    //处理回答字符串中的代码块中的表格相关数据
    handlePsvData(markdown) {
      return markdown.replace(/```(||psv|csv|text|plaintext|markdown|json)\n([\s\S]*?)```/g, (match, type, content) => {
        if (type === 'json') {
          return '\n```json\n' + content + '\n```\n';
        }
        const delimiter = type === "psv" ? "|" : type === "csv" ? "," : this.handlePsvOrCsv(content);
        return this.convertToMarkdown(content, delimiter);
      })
    },
    //识别字符串是否是psv与csv格式的内容
    handlePsvOrCsv(text) {
      const indexList = [",", "|"];
      let returnStr = '';
      indexList.forEach(item => {
        if (text.indexOf(item) != -1) {
          returnStr = item;
        }
      });
      return returnStr;
    },
    //符合条件的表格包装导出按钮
    convertToMarkdown(content, delimiter) {
      /* if (this.isMarkdownTable(content)) {
        this.chatTable.push({
          id: this.chatTableIndex,
          content: content
        });
        this.resTableList.push(content);
        const newContent = `\n<div class="table-div">\n <div class="table_operation_div">\n <button class="export_csv" id="export_csv_${this.index}_${this.chatTableIndex}">${this.$t('AllInOne.exportCsv')}</button> \n </div> \n\n${content}\n\n</div>\n`
        this.chatTableIndex = this.chatTableIndex + 1;
        return newContent;
      } */
      // 移除首尾空行，避免 split 出现空行
      const rows = content.trim().split("\n")
        .map(row => row.split(delimiter).map(cell => cell.trim()));

      if (rows.length < 2) return content; // 需要至少有表头和一行数据

      // 计算最大列数，防止数据列数不均匀
      const maxCols = Math.max(...rows.map(row => row.length));

      // 填充空缺列（防止行数不齐）
      const normalizedRows = rows.map(row => {
        while (row.length < maxCols) row.push(""); // 补充空字符串
        return row;
      });

      // 处理 Markdown 特殊字符（防止 `|` 影响解析）
      function escapeMarkdown(text) {
        return text.replace(/\|/g, "\\|");
      }

      // 生成 Markdown 表格
      const header = `| ${normalizedRows[0].map(escapeMarkdown).join(" | ")} |`;
      const separator = `| ${new Array(maxCols).fill("---").join(" | ")} |`;
      const body = normalizedRows.slice(1)
        .map(row => `| ${row.map(escapeMarkdown).join(" | ")} |`)
        .join("\n");


      const tableContent = `${header}\n${separator}\n${body}`
      /* this.chatTable.push({
        id: this.chatTableIndex,
        content: tableContent
      });

      const newTableContent = `\n<div class="table-div">\n <div class="table_operation_div">\n <button class="export_csv" id="export_csv_${this.index}_${this.chatTableIndex}">${this.$t('AllInOne.exportCsv')}</button> \n </div> \n\n${tableContent}\n\n</div>\n`;

      this.chatTableIndex = this.chatTableIndex + 1; */

      return tableContent;
    },
    //判断markdown字符串中是否包含表格数据
    isMarkdownTable(str) {
      // 将字符串按行分割
      const lines = str.trim().split('\n');

      // 至少需要两行（表头和分隔符）
      if (lines.length < 2) {
        return false;
      }

      // 检查第二行是否是分隔符行（包含 - 和 |）
      const separatorRegex = /^\s*\|[\s-]*\|[\s-]*\|\s*$/;
      if (!separatorRegex.test(lines[1])) {
        return false;
      }

      return true;
    },
    //添加表格的导出按钮
    addDivToTable(tableString, id) {
      // 在表格前后添加 <div> 标签
      return '\n<div class="table-div">\n <div class="table_operation_div">\n <button class="export_csv" id="export_csv_' + this.index + '_' + id + '">' + this.$t('AllInOne.exportCsv') + '</button> \n </div> \n\n' + tableString + '\n\n</div>\n';
    },
    //比对两段字符串的匹配度
    getStringSimilarity(str1, str2) {
      // 处理空字符串情况
      if (str1.length === 0) return str2.length === 0 ? 100 : 0;
      if (str2.length === 0) return 0;

      // 创建编辑距离矩阵
      const matrix = [];
      const maxLen = Math.max(str1.length, str2.length);

      // 初始化矩阵第一行和第一列
      for (let i = 0; i <= str1.length; i++) {
        matrix[i] = [i];
      }
      for (let j = 0; j <= str2.length; j++) {
        matrix[0][j] = j;
      }

      // 填充矩阵
      for (let i = 1; i <= str1.length; i++) {
        for (let j = 1; j <= str2.length; j++) {
          const cost = str1[i - 1] === str2[j - 1] ? 0 : 1;
          matrix[i][j] = Math.min(
            matrix[i - 1][j] + 1,    // 删除操作
            matrix[i][j - 1] + 1,    // 插入操作
            matrix[i - 1][j - 1] + cost // 替换操作
          );
        }
      }

      // 计算相似度百分比
      const distance = matrix[str1.length][str2.length];
      return (1 - distance / maxLen) * 100;
    },
    //导出csv
    handleExportCsv(id) {
      this.chatTable.forEach(item => {
        if (item.id === parseInt(id)) {
          const csvData = this.parseMarkdownToCSV(item.content);
          const csvContent = csvData.map(row => row.join(',')).join('\n');

          // 创建 Blob 并下载
          const blob = new Blob([`\uFEFF${csvContent}`], { type: 'text/csv;charset=utf-8' });
          const url = URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          const time = dayjs().format('YYYYMMDDHHmmss')
          a.download = time + 'sales_report.csv';
          a.click();
          URL.revokeObjectURL(url);
        }
      })

    },
    //打开问答历史
    handleShowHistory() {
      this.historyMd = this.history2Markdown(this.historyList);
      this.historyType = 'list';
      this.showHistory = true;
    },
    //判断当前窗口是否允许打开历史记录
    handleShowHistoryList(state) {
      this.sessionListLoading = true;
      if (state === undefined) {
        const subData = {
          sessionTitle: this.historySearchData,
          userId: this.$store.getters.userId,
          topK: this.chatParams.topK
        }
        const params = {
          pageSize: this.sessionPageSize,
          pageNum: this.sessionPageNum
        }
        findHistoryList(subData, params).then(res => {
          if (res.code === 200) {
            this.sessionOldList = res.rows;
            this.sessionTimeList = this.formatterList(res.rows);
            this.sessionTotal = res.total;
            const windowModel = this.$store.getters.windowModel;
            windowModel[this.index]['sessionTimeList'] = this.formatterList(res.rows);
            this.$store.dispatch('setWindowModel', windowModel);
          }
          this.sessionListLoading = false;
        }).catch(err => {
          this.$message.error(this.$t('AllInOne.searchSessionError'));
          this.sessionListLoading = false;
        });
        const windowModel = this.$store.getters.windowModel;
        windowModel[this.index]['showHistoryState'] = false;
        this.$store.dispatch('setWindowModel', windowModel);
        return;
      }
      if (this.index === 0 && this.bots === 1) {
        if (state) {
          const subData = {
            sessionTitle: this.historySearchData,
            userId: this.$store.getters.userId,
            topK: this.chatParams.topK
          }
          const params = {
            pageSize: this.sessionPageSize,
            pageNum: this.sessionPageNum
          }
          findHistoryList(subData, params).then(res => {
            if (res.code === 200) {
              this.sessionOldList = res.rows;
              this.sessionTimeList = this.formatterList(res.rows);
              this.sessionTotal = res.total;
              const windowModel = this.$store.getters.windowModel;
              windowModel[this.index]['sessionTimeList'] = this.formatterList(res.rows);
              this.$store.dispatch('setWindowModel', windowModel);
            }
            this.sessionListLoading = false;
          }).catch(err => {
            this.$message.error(this.$t('AllInOne.searchSessionError'));
            this.sessionListLoading = false;
          });
          this.showHistoryState = state;
          const windowModel = this.$store.getters.windowModel;
          windowModel[this.index]['showHistoryState'] = state;
          this.$store.dispatch('setWindowModel', windowModel);
          return;
        }
        this.showHistoryState = state;
        this.sessionTimeList = [];
        this.sessionPageNum = 1;
        this.sessionTotal = 0;
        const windowModel = this.$store.getters.windowModel;
        windowModel[this.index]['showHistoryState'] = state;
        windowModel[this.index]['sessionTimeList'] = [];
        windowModel[this.index]['sessionPageNum'] = 1;
        this.$store.dispatch('setWindowModel', windowModel);
        this.sessionListLoading = false;
      }
    },
    //历史记录列表无限滚动查询
    async sessionLoading() {
      let currSessionLength = 0;
      this.sessionTimeList.forEach(item => {
        currSessionLength += item.sessions.length;
      });
      if (this.sessionTotal <= currSessionLength) {
        return;
      }
      try {
        const subData = {
          sessionTitle: this.historySearchData,
          userId: this.$store.getters.userId,
          topK: this.chatParams.topK
        }
        const params = {
          pageSize: this.sessionPageSize,
          pageNum: this.sessionPageNum + 1
        }
        const data = await findHistoryList(subData, params);
        if (data.rows.length === 0) {
          return;
        } else {
          // 追加数据到列表
          if (data.code === 200) {
            this.sessionTotal = data.total;
            this.sessionOldList.push(...data.rows);
            this.sessionTimeList = this.formatterList(this.sessionOldList);
            this.sessionPageNum = params.pageNum;
            const windowModel = this.$store.getters.windowModel;
            windowModel[this.index]['sessionPageNum'] = params.pageNum;
            windowModel[this.index]['sessionTimeList'] = this.formatterList(this.sessionOldList);
            this.$store.dispatch('setWindowModel', windowModel);
          }
        }
      } catch (error) {
        console.error(error);
      }
    },
    //模糊查询历史记录
    handleSearchSession() {
      this.sessionListLoading = true;
      const subData = {
        sessionTitle: this.historySearchData,
        userId: this.$store.getters.userId,
        topK: this.chatParams.topK
      }
      const params = {
        pageSize: this.sessionPageSize,
        pageNum: 1
      }
      findHistoryList(subData, params).then(res => {
        if (res.code === 200) {
          this.sessionOldList = res.rows;
          this.sessionTimeList = this.formatterList(res.rows);
          this.sessionTotal = res.total;
          this.sessionPageNum = 1;
          const windowModel = this.$store.getters.windowModel;
          windowModel[this.index]['sessionPageNum'] = 1;
          windowModel[this.index]['sessionTimeList'] = this.formatterList(res.rows);
          this.$store.dispatch('setWindowModel', windowModel);
        }
        this.sessionListLoading = false;
      }).catch(err => {
        this.$message.error(this.$t('AllInOne.searchSessionError'));
        this.sessionListLoading = false;
      });
    },
    //删除历史记录
    handleDeleteSessionById(id) {
      this.$confirm(this.$t('privateDomain.okDeleted'), this.$t('warding'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        deletedHistoryById(id).then(res => {
          if (res.code === 200) {
            this.handleShowHistoryList();
          }
        }).catch(err => {
          this.$message.error(this.$t('AllInOne.deleteSessionError'));
        });
      }).catch(() => { })

    },
    //打开\关闭右侧参考内容
    handleShowRefer(obj) {
      this.$emit('update:referState', obj);
    },
    //清空弹窗参数
    handleClearDialog() {
      this.historyList = [];
      this.messages = [];
      let obj = this.$store.getters.chatHistoryList;
      obj[this.index] = [];
      this.$store.dispatch("setHistoryList", obj);
      this.$store.dispatch("setMessage", obj);
      this.isLoading = false;
      this.chatTable = [];
      this.chatTableIndex = 1;
      this.sessionId = '';
      // if (this.socket) {
      //   this.socket.close();
      // }
      this.socket = null;
      const windowModel = this.$store.getters.windowModel;
      windowModel[this.index]['chatTable'] = [];
      windowModel[this.index]['chatTableIndex'] = 1;
      windowModel[this.index]['fileMark'] = '';
      windowModel[this.index]['sessionId'] = '';
      this.$store.dispatch("setWindowModel", windowModel);
      this.$emit('update:mainWindow');
    },
    history2Markdown(history) {
      let result = '';
      history.forEach(item => {
        if (item.role === 'user') {
          if (item.type === 'image') {
            result += '## User\n' + '![image](' + item.content + ')\n\n';
          } else {
            result += '## User\n' + item.content + '\n\n';
          }
        } else if (item.role === 'info') {
          result += '## Info\n' + item.content + '\n\n';
        } else {
          result += '## Bot\n' + item.content + '\n\n';
        }
      });
      // result = '```markdown\n' + result + '```';
      return result;
    },
    handleDownloadMD() {
      //Blob为js的一个对象，表示一个不可变的, 原始数据的类似文件对象，这是创建文件中不可缺少的！
      let result = '';
      this.historyList.forEach(item => {
        if (item.role === 'user') {
          result += '## User\n' + item.content + '\n\n';
        } else {
          result += '## Bot\n' + item.content + '\n\n';
        }
      });

      const urlObject = window.URL || window.webkitURL || window;
      const export_blob = new Blob([result]);
      const save_link = document.createElement("a")
      save_link.href = urlObject.createObjectURL(export_blob);
      save_link.download = 'History_' + this.model + '_' +
        new Date().toLocaleString()
          .replaceAll('/', '')
          .replaceAll(':', '')
          .replaceAll(' ', '')
        + '.md';
      save_link.click();
    },
    handleScrollToBottom() {
      this.$nextTick(() => {
        const scrollHeight = this.$refs.historyContent.scrollHeight;
        this.$refs.historyContent.scrollTo({
          top: scrollHeight,
          behavior: 'smooth'
        });
      })
    },
    handleShowImage(url) {
      this.imgList = [url];
      this.imgViewerVisible = true;
    },
    closeViewer() {
      this.imgViewerVisible = false;
    }
  }
}
</script>
<style scoped lang="scss">
.disabled {
  opacity: 0.35;
  transition: all 0.3s;
}

.is-active {
  color: #909399;
  fill: #909399;
}

.single-input {
  position: absolute;
  bottom: 10px;
  left: 0;
  right: 0;
  margin: auto;
  width: 96%;
  overflow: hidden;
  padding: 4px;
  height: 47px;

  background: #FFFFFF;
  box-shadow: 0px 2px 4px 0px rgba(177, 177, 177, 0.31);
  border-radius: 4px;
  border: 1px solid #E9E9E9;
  transition: all 0.3s;

  &.hidden {
    height: 0;
    padding: 0;
    border-width: 0;
    border-color: transparent;
    box-shadow: 0px 2px 4px 0px rgba(177, 177, 177, 0);
    transition: all 0.3s;
  }
}

.history_list_div {
  position: absolute;
  left: 5px;
  top: 55px;
  padding: 4px;
  height: 90%;
  display: flex;
}

.history_list {
  left: 0;
  top: 55px;
  width: 300px;
  overflow: hidden;
  padding: 12px;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;

  background: #FFFFFF;
  box-shadow: 0px 2px 4px 0px rgba(177, 177, 177, 0.31);
  border-radius: 4px;
  border: 1px solid #E9E9E9;
  transition: all 0.3s;

  &.hidden {
    width: 0;
    padding: 0;
    border-width: 0;
    border-color: transparent;
    box-shadow: 0px 2px 4px 0px rgba(177, 177, 177, 0);
    transition: all 0.3s;
  }
}

.history_button_div {
  height: 100%;
  display: flex;
  align-items: center;
}


.history_operation_div {
  flex: 1;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.content_list_div {
  flex: 10;
  width: 100%;
  overflow-y: auto;
}

.content_list_div::-webkit-scrollbar-thumb {
  background: transparent;
  transition: opacity 0.3s;
}

.content_list_div:hover::-webkit-scrollbar-thumb {
  background: #e8e8e8;
  opacity: 1;
  transition: 500ms;
}


.session-item {
  padding: 6px 10px 6px 20px;
  background-color: #ffffff80;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.5);
  line-height: 20px;
  transition: all 0.3s;
  border: 1px solid transparent;
  overflow: hidden;

  &:hover {
    background-color: #EBF0F9;
    border-radius: 4px;
    transition: all 0.3s;
  }

  .tool-item-button:hover {
    background-color: #dee4ed;
  }

  &.active {
    background: rgba(134, 167, 229, 0.27);

    color: #254F91;

    .tool-item-button {
      color: #254F91;

      &:hover {
        background: rgba(125, 158, 220, 0.3);
      }
    }
  }

  &.is-chosen {
    background: #fffDFD;
    border: 1px solid #e1bfbf;

    &.active {
      color: #ca4747;
    }

    &:hover {
      background: #fffDFD;
    }

  }
}

.blank {
  width: 100%;
  height: 20px;
  transition: all 0.3s;

  &.expand {
    height: 67px;
    transition: all 0.3s;
  }
}

.title {
  font-weight: 600;
  font-size: 21px;
  color: #2C2C2C;
  line-height: 29px;
}

.md-show {
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
  text-align: left;
  white-space: pre-wrap;
  //margin: 16px;
  //padding: 4px;
}

.table-container {
  position: relative;
}

.export-csv-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 8px 15px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
}

.export-csv-btn:hover {
  background-color: #45a049;
}


.fold-button {
  width: fit-content;
  border-radius: 4px 0px 0px 4px;
  background: #E9EAFB;
  padding: 4px;
  cursor: pointer;

  position: absolute;
  right: 4px;
  top: 23px;
  transition: all 0.3s;

  &.unfold {
    left: -5px;
    transition: all 0.3s;
    border-radius: 0px 4px 4px 0px;
  }

  &.hide {
    opacity: 0;
    pointer-events: none;
    transition: all 0.5s;
  }

  &:hover {
    box-shadow: 0 0 2px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s;
  }
}

.completionTokensCla {
  text-align: right;
  margin: 15px;
  font-size: 15px;
  font-weight: bold;
}

/*::v-deep .v-note-wrapper .v-note-panel .v-note-show .v-show-content,
.v-note-wrapper .v-note-panel .v-note-show .v-show-content-html {
  padding: 0px;
  width: 100%;
}

::v-deep .markdown-body p {
  margin-bottom: 0px;
  width: 100%;
}*/
</style>
