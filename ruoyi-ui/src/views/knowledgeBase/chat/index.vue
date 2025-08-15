<template>
  <div class="app-container full-screen flex-layout gap10 column" v-loading.fullscreen="fullScreenLoading">
    <div style="width: 100%; height: 100%;" class="flex-layout column gap10" v-show="!showPromptSettings">
      <div style="width: 100%;overflow: hidden;height: 100%;" :class="{ 'flex-layout': true }">

        <el-card :class="{ 'session-card': true, 'hide': !showSession }"
          :body-style="{ padding: '20px 16px', height: '100%', display: 'flex', flexDirection: 'column', gap: '10px', overflow: 'hidden' }">
          <div class="fold-button" @click="showSession = false;">
            <svg-icon icon-class="fold" style="font-size: 20px;" />
          </div>
          <div class="flex-layout " style="justify-content: flex-start;">
            <el-button type="primary" :icon="sessionId ? 'el-icon-plus' : 'el-icon-check'"
              style="width: 93%;border-radius: 12px;" @click="handleNewSession">
              {{ sessionId ?
                $t('privateDomain.addConversation') : $t('privateDomain.newQaConversation') }}
            </el-button>
          </div>
          <div style="padding-top: 8px;width: 100%;" class="flex-layout align-center gap4">
            <svg-icon icon-class="search" style="font-size: 20px;color: #a1a1a1;" />
            <el-input class="no-border" v-model="historySearchData" :placeholder="$t('search')"
              @input="handleSearchSession" />
          </div>
          <div class="flex-1" style="overflow-y: auto;padding-right: 2px;" v-loading="sessionLoading">
            <Empty v-if="sessionList.length < 1" :description="$t('privateDomain.noneSession')" />
            <template v-else>
              <div v-for="(date, index) in sessionTimeList" style="border-bottom: 1px solid #E0E0E0;">
                <div class="session-date text-gray">{{ date.type }}</div>
                <div :class="{ 'flex-layout align-center': true, 'editing': isEditSessions }"
                  style="overflow: hidden;width: 100%;" v-for="(item, index) in date.sessions" :key="index">
                  <div :class="{
                    'session-item flex-1 flex-layout align-center start-end': true,
                    'active': item.sessionMark === sessionMark,
                    'is-chosen': isEditSessions && editSessions.includes(item.id)
                  }" :title="item.sessionName" @click="handleClickSession(item)">
                    <div class="text-over">
                      {{ item.sessionName }}
                    </div>
                    <div :class="{ 'tool-item-button': true }" @click.stop="handleDeleteSession(item.id)">
                      <i class="el-icon-delete" />
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </div>
        </el-card>
        <el-card class="flex-1" style="position:relative;overflow-y: auto;" v-loading="historyLoading"
          :body-style="{ padding: '0 0', height: '100%' }">
          <div :class="{ 'fold-button': true, 'unfold': true, 'hide': showSession }" @click="showSession = true;">
            <svg-icon icon-class="unfold" style="font-size: 20px;" />
          </div>
          <template v-if="!historyLoading">
            <div v-if="historyList.length < 1" style="padding: 20px 50px;">
              <div class="flex-layout align-center" style="gap: 6px;">
                <svg-icon icon-class="kb-title" style="font-size: 20px;" />
                <span class="kb-sub-title">{{ $t('privateDomain.answerQuestions') }}</span>
              </div>
              <div style="margin-top: 10px;" class="flex-layout align-center start-end">
                <span class="kb-title text-over">{{ $t('privateDomain.answerVariousProfessional') }}</span>
                <el-button @click="getRandomHotQuestion" type="primary" icon="el-icon-refresh" style="margin-top: 2px;">
                  {{ $t('privateDomain.ChangeBatch') }}
                </el-button>
              </div>

              <div v-loading="hotLoading" style="margin-top: 40px;width:100%;"
                class="grid-layout col-g10 row-g20 center-center row3 col2">
                <div v-for="(item, index) in hotQuestionList" :title="item" @click="handleClickFollowUp(item)"
                  class="flex-layout align-center start-end hot-question-item">
                  <div class="flex-1 text-over" style="color: #2C2C2C;">
                    {{ item }}
                  </div>
                  <div style="color: #154F96;">
                    <i class="el-icon-arrow-right" />
                  </div>
                </div>
              </div>
            </div>
            <div v-else ref="historyContent" style="width: 100%;height: 100%;overflow-y:auto;padding: 0 50px;"
              @scroll="handleScroll">
              <div>
                <div :class="{ 'blank': true }" />
                <div v-for="(item, index) in historyList" :key="index" class="flex-layout mb10"
                  :style="{ justifyContent: item.role === 'user' ? 'flex-end' : 'flex-start', gap: '6px' }">
                  <div v-if="item.role === 'bot'">
                    <img :src="botImgUrl" class="avatar medium avatar-bg" />
                  </div>
                  <div v-else class="avatar placeholder"></div>
                  <div style="overflow:hidden;" :class="{ 'history-content': showContent }">
                    <div>
                      <el-card :class="item.role" style="position: relative;overflow: initial;"
                               :style="item.role === 'user' ? 'cursor:pointer;' : ''"
                               :title.native="item.role === 'user' ? $t('privateDomain.doubleClickCopy') : ''"
                               @dblclick.native="handleCopyContent(item)">
                        <div style="position: absolute;top: -23px;left: 0;min-width: 300px;" v-if="item.role === 'bot'">
                          {{ item.modelType }}
                        </div>
                        <template v-if="item.loading">
                          <span>{{ $t('privateDomain.thinkingAbout') }}</span><i class="el-icon-loading"
                                                                                 style="margin-left: 4px;" />
                        </template>
                        <span v-else-if="item.isError">
                        {{ item.errorContent }}
                      </span>
                        <template v-else>
                          <div v-if="item.reasoningContent" style="margin-bottom: 15px;">
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
                            <markdown-it-vue v-show="item.showReasoningState" :content="item.reasoningContent"
                                             :options="options" align="left" />
                          </div>
                          <markdown-it-vue class="md-body" :content="item.content" :options="options" align="left" />
                        </template>

                        <div class="content-area" v-if="item.citation && item.citation.length > 0">
                          <div class="flex-layout align-center gap8">
                            <span class="content-title">{{ $t('privateDomain.quote') }}</span>
                            <el-divider class="flex-1 m16" />
                          </div>
                          <div class="content-link" style="margin: 4px 0 4px 0;" :key="index"
                               :title="citation.originalFileName" v-for="(citation, index) in item.citation"
                               @click="handleShowContent(citation)">
                            <svg-icon v-if="citation.suffix === 'pdf'" icon-class="pdf" class="text-red fs-14 shrink" />
                            <svg-icon v-else-if="citation.suffix === 'xlsx'" icon-class="excel"
                                      class="text-green fs-14 shrink" />
                            <span class="text-over">{{ citation.originalFileName }}</span>
                          </div>
                        </div>
                        <div class="tool-bar" v-if="item.role === 'bot' && !item.loading && !item.isError"
                             style="padding-top: 4px;font-size: 16px;gap:2px;">
                          <div class="tool-bar-item" @click="handleCopy(item)">
                            <svg-icon icon-class="copy" />
                          </div>
                          <div class="tool-bar-item" @click="handleAccuracy(item, 'yes')">
                            <svg-icon icon-class="good-chosen" v-if="item.accuracy === 'yes'" />
                            <svg-icon icon-class="good" v-else />
                          </div>
                          <div class="tool-bar-item" @click="handleAccuracy(item, 'no')">
                            <svg-icon icon-class="bad-chosen" v-if="item.accuracy === 'no'" />
                            <svg-icon icon-class="bad" v-else />
                          </div>
                        </div>
                      </el-card>
                      <div class="completionTokensCla" v-if="item.shouToken ">
                        {{ $t('AllInOne.completionTokens')+ ': '+ (item.completionTokens || 0 ) + ' Token'}}
                      </div>
                    </div>

                    <template v-if="index === historyList.length - 1 && item.followUp && item.followUp.length > 0">
                      <div class="follow-title" style="margin-top: 10px;">{{
                        $t('privateDomain.GuessWhatYouWantAsk')
                      }}
                      </div>
                      <el-card v-for="(follow, fIndex) in item.followUp" :key="fIndex"
                        @click.native="handleClickFollowUp(follow)" class="bot follow" :body-style="{
                          whiteSpace: 'nowrap',
                          overflow: 'hidden',
                          textOverflow: 'ellipsis'
                        }" style="margin-top: 6px;">
                        <span :title="follow">
                          {{ follow }}
                        </span>
                      </el-card>
                    </template>
                  </div>
                  <div v-if="item.role === 'user'">
                    <img :src="userAvatar" class="avatar border" />
                  </div>
                  <div v-else class="avatar placeholder"></div>
                </div>
                <div v-show="isError">
                  <div class="flex-layout align-center center-center gap6">
                    <el-divider class="flex-1" />
                    <span class="fs-12 text-gray">
                      {{ errorInfo }}
                    </span>
                    <el-divider class="flex-1" />
                  </div>
                </div>
                <div :class="{ 'blank': true }" />
              </div>
            </div>

            <div :title="$t('privateDomain.ReturnTheBottom')" @click="handleScrollToBottom"
              :class="{ 'back-to-bottom': true, 'sub-color': true, 'is-hidden': !isFar || isBackHidden }">
              <i class="el-icon-arrow-down" /> <span style="margin-left: 2px;">{{
                $t('privateDomain.ReturnTheBottom')
              }}</span>
            </div>
          </template>
        </el-card>
        <el-card style="position:relative;"
          :class="{ 'hide': !showContent, 'flex-1': showContent, 'content-card': true }"
          :body-style="{ padding: '10px', height: '100%', display: 'flex', flexDirection: 'column' }">
          <div class="flex-layout align-center start-end" style="padding-bottom: 6px;">
            <div style="width: 80px;flex-shrink: 0;">
              <div class="fs-20 content-fold" style="width: fit-content;"
                @click="showContent = showContentDetail = false;">
                <svg-icon icon-class="unfold" />
              </div>
            </div>
            <div class="content-card-title pd-h8 text-over" :title="contentName" style="text-align:center;">{{
              contentName
            }}
            </div>
            <div style="width: 80px;font-size: 12px;text-align: right;opacity: 0.5;flex-shrink: 0;">
              <div v-if="pdfs.includes(suffix)">
                {{ $t('privateDomain.quotePage') }}：{{ contentPage }}
              </div>
            </div>
          </div>
          <template v-if="showContentDetail">
            <excel-component ref="excelComponent" v-if="excels.includes(suffix)" :url="contentInfo"
              :is-xls="suffix === 'xls'" style="width: 100%;flex:1;border:none;" />
            <pdf-component ref="pdfComponent" v-else-if="pdfs.includes(suffix)" :page="contentPage"
              :url="contentInfo" />
          </template>
        </el-card>
      </div>
      <div>
        <el-card :body-style="{ padding: '0' }">
          <div style="display: flex;align-items: center; ">
            <el-input type="textarea" class="no-border" :autosize="{ minRows: 2, maxRows: 4 }" v-model="sendData"
              resize="none" :readonly="isRecording" @keydown.native="keydownfn" style="padding-top: 4px;"
              v-show="!isError" :placeholder="sendInputText" @input="handleFindTokenNum" />
            <el-input type="textarea" class="no-border" :autosize="{ minRows: 2, maxRows: 4 }" resize="none"
              :readonly="isRecording" @keydown.native="keydownfn" style="padding-top: 4px;" :disabled="true"
              :placeholder="errorInfo" v-show="isError" />
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
              <div class="flex-layout align-center" :title="model">
                <img :src="botImgUrl" style="width: 22px;height: 22px;border-radius: 16px;pointer-events: none" />
                <el-dropdown @command="handleCommand" trigger="hover"
                  style="margin-left: 10px;height:20px;line-height: 20px;overflow: hidden;">
                  <div style="width: 100%" class="flex-layout align-center">
                    <div class="el-dropdown-link text-over" style="cursor: pointer;width: 100%;">
                      {{ model }}
                    </div>
                    <!--                  <i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
                  </div>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="item in modelList" :key="item.type" :command="item.type">
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
              <el-button class="darkblue" size="mini" @click="handleChooseKBs">
                <div class="flex-layout align-center gap6">
                  <svg-icon icon-class="kb-choose" class="fs-16" />
                  <span>
                    {{ $t('privateDomain.buttonDatabase') }}
                    <template v-if="usingKBs.length === 0">{{ $t('privateDomain.buttonDatabaseAll') }}</template>
                    <template v-else>({{ usingKBs.length }}/{{ kbAllList.length }})</template>
                  </span>
                </div>
              </el-button>
              <div class="tool-bar-item" :title="$t('setting')" @click="handleShowSettings"
                v-if="queryMethodValue === 'rag'">
                <svg-icon icon-class="setting" />
              </div>
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
              <div class="flex-layout align-center" :title="graphInitValue" v-if="queryMethodValue === 'kag'">
                <el-dropdown @command="handleGraphCommand" trigger="hover"
                  style="margin-left: 10px;height:20px;line-height: 20px;overflow: hidden;">
                  <div style="width: 100%;gap: 6px;" class="flex-layout align-center">
                    <i class="el-icon-arrow-up el-icon--right" />
                    <div class="el-dropdown-link text-over" style="cursor: pointer;width: 100%;">
                      {{ graphInitValue }}
                    </div>
                  </div>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-for="item in graphList" :key="item.type" :command="item.spaceId">
                      <div style="display: flex;align-items: center;">
                        <div style="display: flex;align-items: center">
                          <span style="margin-left: 8px;">{{ item.cnName }}</span>
                        </div>
                      </div>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
              <el-dropdown @command="handleQueryMethod" trigger="hover" v-if="queryMethodList.length > 0"
                style="height:20px;line-height: 20px;overflow: hidden;">
                <div style="width: 100%;gap: 6px;" class="flex-layout align-center">
                  <i class="el-icon-arrow-up el-icon--right" />
                  <div class="el-dropdown-link text-over" style="cursor: pointer;width: 100%;">
                    {{ queryMethodLabel }}
                  </div>
                </div>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item v-for="item in queryMethodList" :key="item.dictCode" :command="item">
                    <span>{{ item.dictLabel }}</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
<!--              <el-popover :append-to-body="true" popper-class="input-bar-popover" placement="top" :visible-arrow="false"-->
<!--                width="fit-content" v-model="isRecording" trigger="manual">-->
<!--                <div style="width: 100%;text-align: center">-->
<!--                  <span>{{ $t('privateDomain.recording') }}</span>-->
<!--                </div>-->
<!--                <div :title="$t('privateDomain.voice')" :class="{ 'tool-bar-item': true, 'is-active': false }"-->
<!--                  @click="handleClickRecord" slot="reference">-->
<!--                  <svg-icon icon-class="mic-close" v-if="isRecording" />-->
<!--                  <svg-icon icon-class="mic" v-else />-->
<!--                </div>-->
<!--              </el-popover>-->

              <div style="font-size: 28px;margin-left: 6px;" @click="handleSendData">
                <svg-icon v-if="sendData && !isError && !isLoading" icon-class="color-send" style="cursor:pointer;" />
                <svg-icon v-else icon-class="color-not-send" />
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    <div style="position: relative;width: 100%;height: 100%;" v-if="showPromptSettings">
      <chat-config v-if="showPromptSettings" :visibility.sync="showPromptSettings" :current-model="model" :current-usekb="usingKBs.length === 0 ? kbAllList.map(item => item.kbId) : usingKBs" />
    </div>
    <el-dialog :visible.sync="showSettings" :close-on-click-modal="false" width="60%" append-to-body>
      <div style="width: 100%;" align="center">
        <span class="dialog-title">
          {{ $t('privateDomain.dialogTitle') }}
        </span>
      </div>
      <div style="margin-top: 10px;height: 60vh;" class="flex-layout gap10 pd-h10">
        <div class="flex-1 pd-a20 flex-layout column"
          style="background-color: #F5F9FF;overflow: hidden;border-radius: 4px;">
          <div class="flex-layout align-center start-end">
            <span class="kb-state-title">{{ $t('privateDomain.toBeUsed') }}</span>
            <span class="kb-state-count">
              {{ kbList.length }}/{{ kbAllList.length }}
            </span>
          </div>
          <div class="flex-layout align-center gap4 bottom-border" style="margin-top: 4px;">
            <el-button type="text" @click="handleSelectAll">{{ $t('privateDomain.selectAll') }}</el-button>
            <el-divider direction="vertical" />
            <el-input v-model="searchAllKB" :placeholder="$t('search')" clearable prefix-icon="el-icon-search"
              class="flex-1 transparent" />
          </div>

          <div class="flex-1" style="overflow-y:auto;">
            <el-checkbox-group class="flex-layout column pd-v10" v-model="kbList">
              <el-checkbox
                v-for="(item, index) in (searchAllKB ? kbAllList.filter(item => item.kbName.includes(searchAllKB)) : kbAllList)"
                :key="item.id" :label="item" style="overflow: hidden;padding: 8px 0" class="over">
                <k-b-radio :value="item.kbName" style="overflow: hidden;margin-right: 20px;" />
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </div>
        <div class="flex-1 pd-a20 flex-layout column"
          style="background-color: #EBF0F9;overflow: hidden;border-radius: 4px;">
          <div class="flex-layout align-center start-end">
            <span class="kb-state-title">{{ $t('privateDomain.inUse') }}</span>
            <span class="kb-state-count">
              {{ kbList.length }}/{{ kbAllList.length }}
            </span>
          </div>
          <div class="flex-layout align-center gap4 bottom-border" style="margin-top: 4px;">
            <el-button type="text" @click="kbList = [];">{{ $t('privateDomain.cancelAll') }}</el-button>
            <el-divider direction="vertical" />
            <el-input v-model="searchChosenKB" :placeholder="$t('search')" clearable prefix-icon="el-icon-search"
              class="flex-1 transparent" />
          </div>

          <div class="flex-1" style="overflow-y:auto;">
            <el-checkbox-group class="flex-layout column pd-v10" v-model="kbList">
              <el-checkbox
                v-for="(item, index) in (searchChosenKB ? kbList.filter(item => item.kbName.includes(searchChosenKB)) : kbList)"
                :key="item.id" :label="item" style="overflow: hidden;padding: 8px 0" class="over">
                <k-b-radio :value="item.kbName" style="overflow: hidden;margin-right: 20px;" />
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer" align="center">
        <el-button class="secondary" @click="showSettings = false;">{{ $t('cancel') }}</el-button>
        <el-button type="primary" @click="submitKBs">{{ $t('ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import logoIcon from '@/assets/logo/logoIcon1.png'
import hotQuestion from "@/views/data/hotQuestion/index.vue";
import { sttFromMic } from "@/utils/SpeechRecognitionLong";
import * as sdk from "microsoft-cognitiveservices-speech-sdk";
import userAvatar from "@/assets/images/default_profile.png";
import { getSessionMark } from "@/utils";
import KBRadio from "@/components/KBRadio/index.vue";
import { listKB } from "@/api/knowledgeBase/kbManager";
import { accuracyOrNot, listMyKBHistory } from "@/api/knowledgeBase/historyList";
import { getKBSelect } from "@/api/knowledgeBase/kbAuthManager";
import { randomQuestionList } from "@/api/knowledgeBase/hotQuestions";
import { getHistoryBySessionId, listUserSession, removeUserSession } from "@/api/knowledgeBase/sessionList";
import placeholder from "@/assets/logo/logo-placeholder.jpg";
import ExcelComponent from "@/components/Excel/index.vue";
import PdfComponent from "@/components/Pdf/index.vue";
import { chatKB } from "@/api/knowledgeBase";
import { parseTime } from "@/utils/ruoyi";
import { mapState } from "vuex";
import ChatConfig from "./config.vue";
import { listData } from '@/api/system/dict/data';
import { findTokenNum } from '@/api/chat/index';
import axios from "axios";
import { currUserAuthGraph } from '@/api/knowledgeBase/knowledgeGraph';
import { findModelParam, findHistoryList } from '@/api/kbChat';


export default {
  name: "KBChat",
  components: { PdfComponent, ExcelComponent, KBRadio, ChatConfig },
  computed: {
    modelList() {
      return this.$store.state.user.botList.filter(item => item.scene !== 'embedding');
    }
  },
  data() {
    return {
      options: {
        markdownIt: {
          linkify: true,
          html: true,
        },
      },
      logoIcon,
      placeholder,
      userAvatar,

      fullScreenLoading: false,

      showSettings: false,
      botImgUrl: placeholder,
      showSession: true,
      showContent: false,
      showContentDetail: false,
      historyLoading: false,
      sessionLoading: false,
      hotLoading: false,
      hotQuestionList: [
        '',
        '',
        '',
        '',
        '',
        '',
      ],
      sendData: '',
      isRecording: false,
      // modelList: [],

      historyList: [],
      messages: [],

      sessionList: [/*
        {
          "id": "1811656571831193600",
          "sessionMark": "test",
          "sessionName": "Pocket的作用",
          "createTime": "2024-07-12T06:59:20.000+00:00",
          "createBy": "admin"
        },*/
      ],
      sessionTimeList: [],

      sessionMark: 'test',
      usingKBs: [],

      kbList: [],
      kbAllList: [],

      searchAllKB: null,
      searchChosenKB: null,

      suffix: '',
      contentInfo: '',
      contentPage: null,
      contentName: '',

      excels: ['xlsx', 'xls', 'csv'],
      pdfs: ['pdf'],
      prompt: '',
      modelTypeId: null,
      model: this.$t('privateDomain.NoAvailableModels'),
      scene: null,
      ragParams: {
        topK: 4,
        search: 'vector',
        queryMethod: 'rag',
        needFollow: true,
        grapgId: [],
        topN: 8,
        maxTokens: 1,
        topP: 0.0,
        temperature: 0.0
      },

      errorInfo: this.$t('privateDomain.currSessionOver'),
      isError: false,
      isLoading: false,
      isNewSession: true,
      isFar: false,
      isBackHidden: false,

      needEdit: false,
      isEditSessions: false,
      editSessions: [],
      showPromptSettings: false,
      queryMethodList: [],
      queryMethodLabel: '',
      queryMethodValue: '',
      sessionId: '',
      modelTokenDictParam: {
        dictType: 'model_token_num',
        pageNum: 1,
        pageSize: 10
      },
      findModelTokenLoading: false,
      inputMaxToken: 131072,
      currInputTokenNum: 0,
      timeoutId: null,
      historySearchData: '',
      pythonUrl: process.env.VUE_APP_PYTHON_URL,
      inputTokenState: true,
      pageParams: {
        pageNum: 1,
        pageSize: 100,
        name: '',
        cnName: ''
      },
      graphList: [],
      graphInitValue: this.$t('knowledgeGraph.graphSelect'),
      evertShiftStart: localStorage.getItem("EVERT_SHIFT_START") || false,
      sendInputText: this.$t('privateDomain.sendInputText'),
      chatActiveText: this.$t('privateDomain.chatActiveText'),
      sessions: [],
    }
  },
  watch: {
    showPromptSettings: function (newV, oldV) {
      if (!newV) {
        this.handleCommand(this.model);
      }
    },
    modelList: function (newVal, oldVal) {
      if (newVal.findIndex(item => item.type === this.model) === -1) {
        if (newVal.length > 0) {
          this.handleCommand(newVal[0].type);
        } else {
          this.model = this.$t('privateDomain.NoAvailableModels');
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
    this.historyList = [/*
      {
        role: 'user',
        content: '你好',
        type: 'text',
      },
      {
        role: 'bot',
        content: '你好，我是小助手，有什么可以帮助你的吗？',
        type: 'text',
        timestamp: new Date().toLocaleString(),
        isError: true,
        errorContent: '这是一个报错信息',
      }*/
    ]
    // this.isError = true;

    this.sessionMark = getSessionMark();

    // this.modelList = this.$store.state.user.botList;

    if (this.modelList.length > 0) {
      this.handleCommand(this.modelList[0].type);
    }
    this.getKBList();
    this.getRandomHotQuestion();
    this.getSessionList();
    this.handleQueryMethodType();
    if (this.$store.state.permission.needEdit === '1') {
      this.needEdit = true;
    }
  },
  methods: {
    handleGraphCommand(command) {
      this.graphList.forEach(item => {
        if (item.spaceId === command) {
          this.ragParams.grapgId = [];
          this.graphInitValue = item.cnName;
          this.ragParams.grapgId.push(command);
        }
      })
    },
    handleQueryMethodType() {
      listData({ pageNum: 1, pageSize: 10, dictType: 'query_method' }).then(res => {
        if (res.code === 200) {
          this.queryMethodLabel = res.rows[0].dictLabel;
          this.queryMethodValue = res.rows[0].dictValue;
          this.queryMethodList = res.rows;
        }
      }).catch(err => {
        this.$message.error(this.$t('privateDomain.queryMethodError'));
      })
    },
    async handleQueryMethod(dict) {
      if (dict.dictValue === 'kag') {
        currUserAuthGraph().then(response => {
          this.graphList = response.data;
          if (response.data.length > 0) {
            this.queryMethodList.forEach(item => {
              if (item.dictCode === dict.dictCode) {
                this.queryMethodLabel = item.dictLabel;
                this.queryMethodValue = item.dictValue;
                this.ragParams.queryMethod = item.dictValue;
              }
            })
          } else {
            this.$message.warning(this.$t('privateDomain.graphNullWarning'));
          }
        }
        ).catch(err => {
          console.log(err)
        });
      } else {
        this.queryMethodList.forEach(item => {
          if (item.dictCode === dict.dictCode) {
            this.queryMethodLabel = item.dictLabel;
            this.queryMethodValue = item.dictValue;
            this.ragParams.queryMethod = item.dictValue;
          }
        })
      }
    },
    handleShowSettings() {
      this.showPromptSettings = true;
    },
    handleClearDialog(newSession) {
      this.showContent = false;
      this.isError = false;
      this.isLoading = false;
      this.historyList = [];
      this.messages = [];
      if (newSession) this.sessionMark = getSessionMark();
    },
    getRandomHotQuestion() {
      this.hotLoading = true;
      randomQuestionList().then(response => {
        this.hotQuestionList = response.data;
        this.hotLoading = false;
      }).catch(() => {
        this.hotLoading = false;
      });
    },
    handleCommand(command) {
      let model = this.modelList.find(item => item.type === command);
      this.botImgUrl = model.icon || /*require('@/assets/images/gpt.png')*/placeholder;
      this.scene = model.scene;
      this.modelTypeId = model.id;
      this.model = command;
      this.handleModelToken(command);
      this.handleFindModelParam(command);
    },
    //查询model对应的提示词信息
    handleFindModelParam(command) {
      findModelParam({ modelName: command }).then(res => {
        if (res.code === 200) {
          this.prompt = JSON.parse(res.data.promptSetting).prompt;
          const configSetting = JSON.parse(res.data.configureSetting);
          if (configSetting.length > 0) {
            configSetting.forEach(item => {
              if (this.ragParams.hasOwnProperty(item.name)) {
                this.ragParams[item.name] = item.value;
              }
            });
          }
        }
      }).catch(err => {
        this.$message.error(this.$t('AllInOne.findModelError'));
        this.sessionListLoading = false;
      });
    },
    handleModelToken(command) {
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
    getKBList() {
      getKBSelect().then(response => {
        this.kbAllList = response.data;
      });
    },
    getSessionList() {
      this.sessionLoading = true;
      listUserSession().then(response => {
        this.sessionList = response.data;

        this.sessionTimeList = this.formatterList(response.data);

        this.sessionLoading = false;
      }).catch(() => {
        this.sessionLoading = false;
      });
    },
    //模糊查询历史记录
    handleSearchSession() {
      this.sessionListLoading = true;
      const subData = {
        sessionTitle: this.historySearchData,
        userId: this.$store.getters.userId,
        topK: this.ragParams.topK
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
    formatterList(list) {
      //根据时间分为 今天 近一周 近一个月 近一年 和更早，分类加到sessions中。praseTime没有周的格式化，所以没有周
      let sessionList = [];
      let type = this.$t('privateDomain.earlier');
      let now = new Date();
      list.forEach(item => {
        if (parseTime(item.createTime, '{y}-{m}-{d}') === parseTime(now, '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.today');
        } else if (parseTime(item.createTime, '{y}-{m}-{d}') >= parseTime(new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000), '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.pastWeek');
        } else if (parseTime(item.createTime, '{y}-{m}-{d}') >= parseTime(new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000), '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.pastMonth');
        } else if (parseTime(item.createTime, '{y}-{m}-{d}') >= parseTime(new Date(now.getTime() - 365 * 24 * 60 * 60 * 1000), '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.pastYear');
        } else {
          type = this.$t('privateDomain.earlier');
        }

        let data = {
          createTime: item.createTime,
          sessionName: item.sessionName,
          sessionMark: item.sessionMark || 0,
          id: item.id
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
    keydownfn(e) {
      if (this.evertShiftStart === 'true') {
        if (event.shiftKey && event.keyCode == 13) {
          event.preventDefault();
          this.handleSendData();
        }
      } else {
        if (event.keyCode == 13) {
          if (!event.shiftKey && !event.ctrlKey && !event.altKey && !event.metaKey) {
            event.preventDefault();
            this.handleSendData();
          } else if (!event.shiftKey) {
            this.sendData = this.sendData + '\n';
          }
        }
      }
    },
    setShiiftStart (v) {
      localStorage.setItem("EVERT_SHIFT_START", v);
      this.evertShiftStart = v;
      this.sendInputText = v === 'true'?  this.$t('privateDomain.sendInputText2'): this.$t('privateDomain.sendInputText');
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

      accuracyOrNot({
        id: item.id,
        accuracy: result
      }).then(() => {
        this.$message.success(this.$t('privateDomain.thankYourFeedback'));
      }).catch(err => {
        item.accuracy = old;
        this.$forceUpdate();

        console.log(err);
        this.$message.error(err);
      });

      // this.$message.success('感谢您的反馈');
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
    handleClickFollowUp(text) {
      this.sendData = text;
      this.handleSendData();
    },
    handleRetry() {
      this.sendData = this.historyList[this.historyList.length - 1].content;
      this.handleSendData();
    },
    handleSendData() {
      if (this.ragParams.grapgId.length === 0 && this.queryMethodValue === 'kag') {
        this.$message.warning(this.$t('knowledgeGraph.graphSelectMsg'));
        return;
      }
      if (this.currInputTokenNum > this.inputMaxToken) {
        this.$message.warning(this.$t('AllInOne.tokenMaxNum'));
        return;
      }
      if (this.isError || this.isLoading || !this.scene) return;
      if (this.sendData) {
        //把sendData中末尾的换行符去掉，保留非末尾的换行符
        this.sendData = this.sendData.replace(/\n+$/, '');

        //发送消息
        this.currInputTokenNum = 0;
        let text = this.sendData;

        this.historyList.push({
          role: 'user',
          content: text,
          type: 'text',
          loading: false,
        });

        this.messages.push({
          role: 'user',
          content: text,
          type: 'text'
        });

        setTimeout(() => {
          this.historyList.push({
            role: 'bot',
            content: '',
            loading: true,
            type: 'text',
            timestamp: new Date().toLocaleString()
          });

          this.isLoading = true;

          this.$nextTick(() => {
            this.handleScrollToBottom();
          })

          this.sendRequest(text);
        }, 500);

        this.sendData = '';
      }
    },
    sendRequest(text) {
      this.isError = true;
      this.errorInfo = this.$t('privateDomain.thinkingAbout');
      if (!this.sessions.includes(this.sessionMark)) {
        this.sessions.push(this.sessionMark);
      }
      let kbList = this.usingKBs;
      if (kbList.length === 0) {
        kbList = this.kbAllList.map(item => item.kbId);
      }
      let data = {
        "modelType": this.model,
        "modelTypeId": this.modelTypeId,
        "scene": this.scene,
        "ragParams": { ...this.ragParams, kbId: kbList },
        "history": this.messages,
        "prompt": this.prompt,
        "sessionId": this.sessionMark, //会话ID
        "question": text, //问题
        "kbManagerIds": kbList,
        "chatTitle": text, //会话标题
      };
      let index = this.historyList.length - 1;
      let startTime = performance.now();
      this.inputTokenState = false;
      chatKB(data).then(response => {
        // 40001 GPT异常
        if (response.code === 200) { // java code

          let res = response.data;
          const sessionId = this.sessionMark;
          if (res.code === 200) {// python code
            if (this.sessions.includes(sessionId)) {
              this.sessions = this.sessions.filter( v => v !== sessionId)
            }
            this.historyList[index].id = res.id;
            this.historyList[index].loading = false;
            this.historyList[index].content = res.data.answer;
            this.historyList[index].reasoningContent = '<div class="chat_reasoning">\n' + res.data.reasoningContent + '\n</div>';
            this.historyList[index].shouToken = true;
            this.historyList[index].completionTokens = res.data.completionTokens;

            this.historyList[index].showReasoningState = true;
            this.historyList[index].modelType = this.model;
            let endTime = performance.now();
            this.historyList[index].reasoningTime = ((endTime - startTime) / 1000).toFixed(0);
            this.historyList[index].citation = res.data.originalFileNames.map((item, index) => {
              return {
                originalFileName: item,
                url: res.data.blobUrl[index],
                suffix: item.split('.').pop(),
                page: item.split('_').pop().split('.').shift()
              }
            });
            this.historyList[index].followUp = res.data.followQuestions;
            this.messages.push({
              role: 'assistant',
              content: res.data.answer,
              type: 'text'
            });
            this.isError = false;
            this.errorInfo = this.$t('privateDomain.currSessionOver')
          } else {
            this.historyList[index].shouToken = false;
            this.historyList[index].loading = false;
            this.historyList[index].isError = true;
            this.historyList[index].errorContent = res.msg || this.$t('privateDomain.modelAbnormal');
            this.isError = true;
          }

        } else {
          this.historyList[index].shouToken = false;
          this.historyList[index].loading = false;
          this.historyList[index].isError = true;
          this.historyList[index].errorContent = response.msg || this.$t('privateDomain.networkError');
          this.isError = true;
        }

        if (!this.isFar) {
          this.isBackHidden = true;
          this.$nextTick(() => {
            this.handleScrollToBottom();
          })
        }

        if (this.isNewSession) {
          this.getSessionList();
        }
        this.isNewSession = false;
        this.isLoading = false;
      }).catch(err => {
        this.historyList[index].loading = false;
        this.historyList[index].isError = true;
        this.historyList[index].errorContent = this.$t('privateDomain.networkError');
        this.isError = true;
        this.isLoading = false;

        console.log(err);
        this.$message.error(err);

        if (!this.isFar) {
          this.isBackHidden = true;
          this.$nextTick(() => {
            this.handleScrollToBottom();
          })
        }

        if (this.isNewSession) {
          this.getSessionList();
        }
        this.isNewSession = false;
      });

      /*      setTimeout(() => {
              let res = {
                "code": 200,
                "data": {
                  "answer": "百万医疗险的投保年龄为出生满30天至60周岁(含)。",
                  "blobUrl": [
                    "https://501351981.github.io/vue-office/examples/dist/static/test-files/test.xlsx",
                    "http://127.0.0.1:5000",
                    "http://127.0.0.1:5000"
                  ],
                  "followQuestions": [
                    "百万医疗险的保险期限是多久？",
                    "百万医疗险有哪些主要保障内容？",
                    "百万医疗险的等待期是多少天？"
                  ],
                  "originalFileNames": [
                    "xxxx_test_1.xlsx",
                    "xxxx_12.pdf",
                    "xxxx_12.pdf"
                  ],
                  "questionVector": [],
                },
                "msg": "回答生成成功",
                "modelId": "232321321321",
                "tokenUsage": "211"
              }
              this.historyList[index].loading = false;
              this.historyList[index].content = res.data.answer;
              this.historyList[index].citation = res.data.originalFileNames.map((item, index) => {
                return {
                  originalFileName: item,
                  url: res.data.blobUrl[index],
                  suffix: item.split('.').pop(),
                  page: item.split('_').pop().split('.').shift()
                }
              });
              this.historyList[index].followUp = res.data.followQuestions;
              this.messages.push({
                role: 'assistant',
                content: res.data.answer,
                type: 'text'
              });
            }, 2000);
            if(this.isNewSession){
              this.getSessionList();
            }
            this.isNewSession = false;*/

    },
    //展开或收起推理过程
    handleReasoning(chatContent) {
      const state = !chatContent.showReasoningState;
      this.historyList.forEach((item, index) => {
        if (item.id) {
          if (item.id === chatContent.id) {
            const newItem = { ...item, showReasoningState: state };
            this.$set(this.historyList, index, newItem);
          }
        }
      });
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
    handleLocatePage(page) {
    },
    handleShowContent(file) {
      const httpUrl = process.env.VUE_APP_FILE_DOWNLOAD_URL;
      const oldUrl = process.env.VUE_APP_INIT_FILE_URL;
      if (!file.url) {
        this.$message.warning(this.$t('privateDomain.fileShowError'));
        return;
      }
      let url = file.url.replace(oldUrl, httpUrl);
      /* if (file.url) {
        const split = file.url.split('/kb');
        url = httpUrl + '/kb' + split[1];
      } */
      this.contentName = file.originalFileName;
      this.suffix = file.suffix;
      // this.contentInfo = '/build/pdf/web/viewer.html?file=' + encodeURIComponent(url);
      this.contentInfo = url;
      this.contentPage = file.page;
      this.showContent = true;

      setTimeout(() => {
        this.showContentDetail = true;


      }, 500);
    },

    handleDeleteSession(id) {
      this.$confirm(this.$t('privateDomain.okDeleted'), this.$t('warding'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.fullScreenLoading = true;
        //删除会话
        removeUserSession(id).then(() => {
          this.handleNewSession();
          this.getSessionList();
          this.fullScreenLoading = false;
        }).catch(err => {
          this.fullScreenLoading = false;
          console.log(err)
          this.$message.error(err);
        });
      }).catch(() => {
      });
    },
    handleNewSession() {
      this.handleClearDialog(true);
      this.isNewSession = true;
      this.sessionId = '';
    },
    handleClickSession(item) {
      if (item) {
        this.sessionId = item.id;
      }
      if (this.isEditSessions) {
        if (this.editSessions.includes(item.id)) {
          this.editSessions = this.editSessions.filter(session => session !== item.id);
        } else {
          this.editSessions.push(item.id);
        }
      } else {
        this.handleUseSession(item.sessionMark);
      }
    },
    handleUseSession(sessionMark) {
      this.handleClearDialog();
      this.isBackHidden = true;
      this.isNewSession = false;
      this.historyLoading = true;
      this.sessionMark = sessionMark;
      getHistoryBySessionId(sessionMark).then(res => {
        this.historyLoading = false;
        this.formatHistoryList(res.data);
      }).catch(err => {
        console.log(err);
        this.$message.error(err);
        this.historyLoading = false;
      })
    },
    formatHistoryList(history) {
      let maxLenthId = history[history.length-1]?.id || '';
      let isLeng = true;
      history.forEach(row => {
        let resId = row?.id;
        let res = JSON.parse(row.response || "{\"code\": 500, \"data\": {\"answer\": \"\", \"blobUrl\": [],\"hiddenQuesion\": [],\"originalFileNames\":[]},\"msg\": \"\",\"modelId\": \"\",\"tokenUsage\": \"\"}");
        this.historyList.push({
          role: 'user',
          content: row.question,
          type: 'text',
          loading: false,
        });
        if (res.code !== 200) {
          let contentMsg = this.$t('privateDomain.modelAbnormal')
          let isLoadingCode = false;
          if ( resId && maxLenthId)
          if (this.sessions.includes(row.sessionId) && maxLenthId === resId) {
            contentMsg = this.$t('privateDomain.thinkingAbout');
            isLoadingCode = true;
            this.errorInfo = contentMsg;
            this.$set(this,'isError', true);
            isLeng = false;
          }
          this.historyList.push({
            role: 'bot',
            content: res.answer || contentMsg,
            type: 'text',
            errorContent: res.answer || contentMsg,
            completionTokens: 0,
            shouToken: false,
            loading: isLoadingCode
          });
          return;
        }

        let data = {
          id: row.id,
          role: 'bot',
          type: 'text',
          accuracy: row.isAccuracy
        };

        data.completionTokens = res.data.completionTokens || 0;
        data.shouToken = res.data.answer ? true : false;

        data.content = res.data.answer;
        data.reasoningContent = '<div class="chat_reasoning">\n' + res.data.reasoningContent + '\n</div>';
        data.showReasoningState = true;
        data.modelType = row.modelType;
        data.citation = res.data.originalFileNames.map((item, index) => {
          return {
            originalFileName: item,
            url: res.data.blobUrl[index],
            suffix: item.split('.').pop(),
            page: item.split('_').pop().split('.').shift()
          }
        });
        data.followUp = res.data.followQuestions;

        this.historyList.push(data);

        this.messages.push({
          role: 'user',
          content: row.question,
          type: 'text'
        });
        this.messages.push({
          role: 'assistant',
          content: row.answer,
          type: 'text'
        });

      });
      if (isLeng && this.historyList.length > 0) {
        this.isError = this.historyList[this.historyList.length - 1].isError || false;
      }

      this.$nextTick(() => {
        this.handleScrollToBottom();
      })
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
    handleSelectAll() {
      this.kbList = [...this.kbAllList];
    },
    handleChooseKBs() {
      this.kbList = this.kbAllList.filter(item => this.usingKBs.includes(item.kbId));
      this.showSettings = true;
    },
    submitKBs() {
      this.usingKBs = this.kbList.map(item => item.kbId);
      this.showSettings = false;
    },
    handleScroll() {
      if (this.$refs.historyContent.scrollHeight === (this.$refs.historyContent.scrollTop + this.$refs.historyContent.clientHeight)) {
        this.isBackHidden = false;
      }
      this.isFar = this.$refs.historyContent.scrollTop + this.$refs.historyContent.clientHeight < this.$refs.historyContent.scrollHeight - 10;
    },

    handleEditSessions() {
      this.isEditSessions = !this.isEditSessions;
      if (!this.isEditSessions) {
        this.editSessions = [];
      }
    },
    handleDeleteSessions() {
      this.$confirm(this.$t('privateDomain.okDeleted'), this.$t('warding'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        console.log(this.editSessions);
        //删除会话
      }).catch(() => {
      });
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
                  this.currInputTokenNum = res.data.data;
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
                this.currInputTokenNum = res.data;
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
    }
  }
}
</script>
<style scoped lang="scss">
.kb-sub-title {
  font-weight: 600;
  font-size: 16px;
  color: #2C2C2C;
  line-height: 22px;
}

.kb-title {
  font-weight: 400;
  font-size: 28px;
  color: #2C2C2C;
  line-height: 40px;
}

.hot-question-item {
  width: 100%;
  height: 100%;
  padding: 20px 20px;
  overflow: hidden;
  background-color: #EBF0F9;
  cursor: pointer;
}

.fold-button {
  width: fit-content;
  border-radius: 4px 0px 0px 4px;
  background: #E9EAFB;
  padding: 4px;
  cursor: pointer;

  position: absolute;
  right: 0;
  top: 25px;
  transition: all 0.3s;

  &.unfold {
    left: 0;
    transition: all 0.3s;
    border-radius: 0px 4px 4px 0px;
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

.session-card {
  height: 100%;
  width: 300px;
  position: relative;
  transition: all 0.3s;
  margin-right: 10px;
  border: none;

  &.hide {
    width: 0;
    overflow: hidden;
    border: none;
    margin-right: 0;
    transition: all 0.3s;
  }
}

.session-item {
  padding: 10px 10px 10px 20px;
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

.editing {

  .session-item {

    .tool-item-button {
      opacity: 0;
      pointer-events: none;
      transition: all 0.2s;
    }

    &:hover {
      background: #fffDFD;
    }
  }
}

.kb-state-title {
  font-weight: 600;
  font-size: 16px;
  color: rgba(44, 44, 44, 0.84);
  line-height: 22px;
}

.kb-state-count {
  font-weight: 400;
  font-size: 12px;
  color: rgba(44, 44, 44, 0.7);
  line-height: 17px;
}

.mg-l10 {
  margin-left: 10px;
}

.content-fold {
  transition: all 0.3s;
  cursor: pointer;

  &:hover {
    text-shadow: 0 0 2px rgba(0, 0, 0, 0.3);
    transition: all 0.3s;
  }
}

.content-card {
  margin-left: 10px;

  &.hide {
    width: 0;
    margin-left: 0;
    overflow: hidden;
    transition: all 0.3s;
  }
}

.half-width {
  max-width: 50%;
  transition: all 0.3s;
}

.history-content {
  max-width: 75%;
  overflow: hidden;
  transition: all 0.3s;
}

.session-checkbox {
  padding-right: 6px;
  width: 20px;
  overflow: hidden;
  transition: all 0.2s;

  &.show {
    padding-right: 0;
    width: 0;
    transition: all 0.2s;
  }
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

.switchSenCla{
  font-size: 12px;
  color: #606266;
  font-weight: 600;
}

.completionTokensCla {
  text-align: right;
  margin: 15px;
  font-size: 15px;
  font-weight: bold;
}

</style>
