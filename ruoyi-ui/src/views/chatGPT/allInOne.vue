<template>
  <div class="back">
<!--    <div
      class="color-background"
      style="position: absolute;width:100%;height:100%;"
    ></div>-->
    <div class="backgrond">
      <div
        class="chat-background"
        style=""
      >

        <div style="width:100%;height:100%;padding:10px;">
          <el-row style="height: 100%;" class="rows">
            <el-col
              class="columns"
              v-for="(item, index) in bots"
              :span="getWidthSpan(bots.length, index)"
              :style="(bots.length>3?'height: 50%;':'height: 100%;')"
            >
              <bot ref="bot" :systemPrompt="getSystem()" :index="index" :length="bots.length" :tone="systemTone.tone"
                   :sysStyle="systemTone.style"/>
            </el-col>
          </el-row>
        </div>
        <div style="width:100%;height:fit-content;" align="center">

          <div style="width: 98%;display:flex;">
            <!--输入框-->
            <div
              class="input-bar"
              align="center"
              style="width:460px;max-height:60px;"
            >
              <el-popover
                :append-to-body="false"
                popper-class="input-bar-popover"
                placement="top"
                :visible-arrow="false"
                width="fit-content"
                style="min-width: 0;"
                trigger="click">
                <div>

                  <el-tooltip effect="light" content="1栏" placement="right">
                    <div :class="'input-button-list-item hover' + (bots.length === 1 ? ' isActive' : '')"
                         @dblclick="handleSetDefaultNum(1)"
                         @click="handleChangeLayout(1)">
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path
                          d="M5 4C5 3.73478 5.10536 3.48043 5.29289 3.29289C5.48043 3.10536 5.73478 3 6 3H18C18.2652 3 18.5196 3.10536 18.7071 3.29289C18.8946 3.48043 19 3.73478 19 4V20C19 20.2652 18.8946 20.5196 18.7071 20.7071C18.5196 20.8946 18.2652 21 18 21H6C5.73478 21 5.48043 20.8946 5.29289 20.7071C5.10536 20.5196 5 20.2652 5 20V4Z"
                          stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </div>
                  </el-tooltip>
                  <el-tooltip effect="light" content="2栏" placement="right">
                    <div :class="'input-button-list-item hover' + (bots.length === 2 ? ' isActive' : '')"
                         @dblclick="handleSetDefaultNum(2)"
                         @click="handleChangeLayout(2)">
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path
                          d="M12 3V21M3 4C3 3.73478 3.10536 3.48043 3.29289 3.29289C3.48043 3.10536 3.73478 3 4 3H20C20.2652 3 20.5196 3.10536 20.7071 3.29289C20.8946 3.48043 21 3.73478 21 4V20C21 20.2652 20.8946 20.5196 20.7071 20.7071C20.5196 20.8946 20.2652 21 20 21H4C3.73478 21 3.48043 20.8946 3.29289 20.7071C3.10536 20.5196 3 20.2652 3 20V4Z"
                          stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </div>
                  </el-tooltip>
                  <el-tooltip effect="light" content="3栏" placement="right">
                    <div :class="'input-button-list-item hover' + (bots.length === 3 ? ' isActive' : '')"
                         @dblclick="handleSetDefaultNum(3)"
                         @click="handleChangeLayout(3)">
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path
                          d="M9 3V21M15 3V21M3 4C3 3.73478 3.10536 3.48043 3.29289 3.29289C3.48043 3.10536 3.73478 3 4 3H20C20.2652 3 20.5196 3.10536 20.7071 3.29289C20.8946 3.48043 21 3.73478 21 4V20C21 20.2652 20.8946 20.5196 20.7071 20.7071C20.5196 20.8946 20.2652 21 20 21H4C3.73478 21 3.48043 20.8946 3.29289 20.7071C3.10536 20.5196 3 20.2652 3 20V4Z"
                          stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </div>
                  </el-tooltip>
                  <el-tooltip effect="light" content="4栏" placement="right">
                    <div :class="'input-button-list-item hover' + (bots.length === 4 ? ' isActive' : '')"
                         @dblclick="handleSetDefaultNum(4)"
                         @click="handleChangeLayout(4)">
                      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path
                          d="M3 12H21M12 3V21M3 5.25C3 4.65326 3.23705 4.08097 3.65901 3.65901C4.08097 3.23705 4.65326 3 5.25 3H18.75C19.3467 3 19.919 3.23705 20.341 3.65901C20.7629 4.08097 21 4.65326 21 5.25V18.75C21 19.3467 20.7629 19.919 20.341 20.341C19.919 20.7629 19.3467 21 18.75 21H5.25C4.65326 21 4.08097 20.7629 3.65901 20.341C3.23705 19.919 3 19.3467 3 18.75V5.25Z"
                          stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </div>
                  </el-tooltip>
                </div>
                <el-tooltip effect="light" content="切换" placement="top" slot="reference">
                  <div :class="'input-button-item hover'">
                    <svg v-show="bots.length === 1" width="18" height="18" viewBox="0 0 24 24" fill="none"
                         xmlns="http://www.w3.org/2000/svg">
                      <path
                        d="M5 4C5 3.73478 5.10536 3.48043 5.29289 3.29289C5.48043 3.10536 5.73478 3 6 3H18C18.2652 3 18.5196 3.10536 18.7071 3.29289C18.8946 3.48043 19 3.73478 19 4V20C19 20.2652 18.8946 20.5196 18.7071 20.7071C18.5196 20.8946 18.2652 21 18 21H6C5.73478 21 5.48043 20.8946 5.29289 20.7071C5.10536 20.5196 5 20.2652 5 20V4Z"
                        stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    <svg v-show="bots.length === 2" width="18" height="18" viewBox="0 0 24 24" fill="none"
                         xmlns="http://www.w3.org/2000/svg">
                      <path
                        d="M12 3V21M3 4C3 3.73478 3.10536 3.48043 3.29289 3.29289C3.48043 3.10536 3.73478 3 4 3H20C20.2652 3 20.5196 3.10536 20.7071 3.29289C20.8946 3.48043 21 3.73478 21 4V20C21 20.2652 20.8946 20.5196 20.7071 20.7071C20.5196 20.8946 20.2652 21 20 21H4C3.73478 21 3.48043 20.8946 3.29289 20.7071C3.10536 20.5196 3 20.2652 3 20V4Z"
                        stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    <svg v-show="bots.length === 3" width="18" height="18" viewBox="0 0 24 24" fill="none"
                         xmlns="http://www.w3.org/2000/svg">
                      <path
                        d="M9 3V21M15 3V21M3 4C3 3.73478 3.10536 3.48043 3.29289 3.29289C3.48043 3.10536 3.73478 3 4 3H20C20.2652 3 20.5196 3.10536 20.7071 3.29289C20.8946 3.48043 21 3.73478 21 4V20C21 20.2652 20.8946 20.5196 20.7071 20.7071C20.5196 20.8946 20.2652 21 20 21H4C3.73478 21 3.48043 20.8946 3.29289 20.7071C3.10536 20.5196 3 20.2652 3 20V4Z"
                        stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    <svg v-show="bots.length === 4" width="18" height="18" viewBox="0 0 24 24" fill="none"
                         xmlns="http://www.w3.org/2000/svg">
                      <path
                        d="M3 12H21M12 3V21M3 5.25C3 4.65326 3.23705 4.08097 3.65901 3.65901C4.08097 3.23705 4.65326 3 5.25 3H18.75C19.3467 3 19.919 3.23705 20.341 3.65901C20.7629 4.08097 21 4.65326 21 5.25V18.75C21 19.3467 20.7629 19.919 20.341 20.341C19.919 20.7629 19.3467 21 18.75 21H5.25C4.65326 21 4.08097 20.7629 3.65901 20.341C3.23705 19.919 3 19.3467 3 18.75V5.25Z"
                        stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </div>
                </el-tooltip>

              </el-popover>
              <!--
              <el-tooltip effect="light" content="1栏">
                <div :class="'input-button-item hover' + (bots.length === 1 ? ' isActive' : '')"
                     @dblclick="handleSetDefaultNum(1)"
                     @click="handleChangeLayout(1)">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path
                      d="M5 4C5 3.73478 5.10536 3.48043 5.29289 3.29289C5.48043 3.10536 5.73478 3 6 3H18C18.2652 3 18.5196 3.10536 18.7071 3.29289C18.8946 3.48043 19 3.73478 19 4V20C19 20.2652 18.8946 20.5196 18.7071 20.7071C18.5196 20.8946 18.2652 21 18 21H6C5.73478 21 5.48043 20.8946 5.29289 20.7071C5.10536 20.5196 5 20.2652 5 20V4Z"
                      stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
              </el-tooltip>
              <el-tooltip effect="light" content="2栏">
                <div :class="'input-button-item hover' + (bots.length === 2 ? ' isActive' : '')"
                     @dblclick="handleSetDefaultNum(2)"
                     @click="handleChangeLayout(2)">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path
                      d="M12 3V21M3 4C3 3.73478 3.10536 3.48043 3.29289 3.29289C3.48043 3.10536 3.73478 3 4 3H20C20.2652 3 20.5196 3.10536 20.7071 3.29289C20.8946 3.48043 21 3.73478 21 4V20C21 20.2652 20.8946 20.5196 20.7071 20.7071C20.5196 20.8946 20.2652 21 20 21H4C3.73478 21 3.48043 20.8946 3.29289 20.7071C3.10536 20.5196 3 20.2652 3 20V4Z"
                      stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
              </el-tooltip>
              <el-tooltip effect="light" content="3栏">
                <div :class="'input-button-item hover' + (bots.length === 3 ? ' isActive' : '')"
                     @dblclick="handleSetDefaultNum(3)"
                     @click="handleChangeLayout(3)">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path
                      d="M9 3V21M15 3V21M3 4C3 3.73478 3.10536 3.48043 3.29289 3.29289C3.48043 3.10536 3.73478 3 4 3H20C20.2652 3 20.5196 3.10536 20.7071 3.29289C20.8946 3.48043 21 3.73478 21 4V20C21 20.2652 20.8946 20.5196 20.7071 20.7071C20.5196 20.8946 20.2652 21 20 21H4C3.73478 21 3.48043 20.8946 3.29289 20.7071C3.10536 20.5196 3 20.2652 3 20V4Z"
                      stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
              </el-tooltip>
              <el-tooltip effect="light" content="4栏">
                <div :class="'input-button-item hover' + (bots.length === 4 ? ' isActive' : '')"
                     @dblclick="handleSetDefaultNum(4)"
                     @click="handleChangeLayout(4)">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path
                      d="M3 12H21M12 3V21M3 5.25C3 4.65326 3.23705 4.08097 3.65901 3.65901C4.08097 3.23705 4.65326 3 5.25 3H18.75C19.3467 3 19.919 3.23705 20.341 3.65901C20.7629 4.08097 21 4.65326 21 5.25V18.75C21 19.3467 20.7629 19.919 20.341 20.341C19.919 20.7629 19.3467 21 18.75 21H5.25C4.65326 21 4.08097 20.7629 3.65901 20.341C3.23705 19.919 3 19.3467 3 18.75V5.25Z"
                      stroke="#606266" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
              </el-tooltip>-->
              <el-tooltip effect="light" :content="promptId === 0 ? '提示词库' : '已选择：'+promptTitle ">
                <div :class="'input-button-item hover' + (promptId !== 0 ? ' isActive' : '')"
                     @click="showLibrary = true;" style="margin-left: 30px;">
                  <i class="el-icon-reading" style="color:#606266"/>
                </div>
              </el-tooltip>

              <el-tooltip effect="light" content="提示词优化">
                <div class="input-button-item hover" @click="handleShowPerfect">
                  <i class="el-icon-edit-outline" style="color:#606266"/>
                </div>
              </el-tooltip>
              <div style="margin-left: 24px;" class="tone-style-bar">
                <el-select
                  v-model="systemTone.tone"
                  placeholder="语气选择..."
                  clearable
                  style="width:120px;"
                  size="small"
                >
<!--                  @change="function(val){if(val === 'clear')systemTone.tone = '';}"
                >-->
                  <el-option
                    v-for="item in dict.type.tone_select"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                    class="select-item"
                  /><!--
                  <el-option
                    label="清空"
                    value="clear"
                    style="color: #E65D6E;text-align: center;"
                  ><i class="el-icon-delete"/></el-option>-->
                </el-select>
                <el-select
                  v-model="systemTone.style"
                  placeholder="风格选择..."
                  clearable
                  style="width:120px;margin: 0 16px 0 16px;"
                  size="small"
                  >
<!--                  @change="function(val){if(val === 'clear')systemTone.style = '';}"
                >-->
                  <el-option
                    v-for="item in dict.type.style_select"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                    class="select-item"
                  /><!--
                  <el-option
                    label="清空"
                    value="clear"
                    style="color: #E65D6E;text-align: center;"
                  ><i class="el-icon-delete"/></el-option>-->
                </el-select>
              </div>
            </div>
            <div
              style="margin-left: 12px;flex:1;"
              class="input-bar"
              align="center">
              <div style="flex:1;">
                <el-input
                  resize="none"
                  v-model="sendData"
                  type="textarea"

                  :autosize="{ minRows: 1, maxRows: 4}"
                  @keydown.native="keydownfn"
                  :placeholder="isMobile? '':'请输入对话内容...'"
                >
                </el-input>
              </div>
              <div
                :style="'width:60px;padding-top:2px;'+ (sendData=='' || isSend ? '' : 'cursor:pointer')"
              >
                <svg
                  v-if="!isSend"
                  width="20"
                  height="20"
                  viewBox="0 0 52 52"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                  @click="sendData=='' ? '' : handleSubmitData()"
                >
                  <path
                    d="M48.1857 2.01674C47.8845 1.76668 47.5185 1.60736 47.1302 1.55736C46.742 1.50735 46.3475 1.56871 45.9928 1.73429L3.42639 21.7141V25.5802L21.3051 32.7316L32.7732 50.375H36.6401L48.858 4.12345C48.9571 3.74467 48.9465 3.34555 48.8275 2.97257C48.7084 2.59958 48.4859 2.2681 48.1857 2.01674ZM34.2526 46.6876L24.3953 31.522L39.564 14.9082L37.1639 12.7169L21.8765 29.4601L7.13545 23.5637L45.0633 5.76064L34.2526 46.6876Z"
                    :fill="sendData=='' ? '#aaaaaa' : '#2D9CDB'"
                  />
                </svg>
              </div>
            </div>
            <div
              v-if="false"
              style="width: 50px;margin-left: 10px;"
              class="input-bar mic-button"
              align="center">
              <div style="width: 100%;" align="center">
                <i class="el-icon-microphone"/>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>

    <el-dialog
      title="提示词库"
      class="library-dialog"
      :visible.sync="showLibrary"
      width="50%"
      align="center"
      append-to-body
      destroy-on-close
    >
      <div style="width:100%" align="center">
        <library @promptClick="handlePromptClick" :promptId="promptId"/>
      </div>
    </el-dialog>
    <el-dialog
      title="提示词优化"
      class="perfect-dialog"
      :visible.sync="showPerfectPrompt"
      width="720px"
      align="center"
      append-to-body
      destroy-on-close
    >
      <div style="width:100%" align="center">
        <prompt-perfect @promptPerfect="handlePerfectPrompt" :text="sendData" ref="perfect"/>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Bot from "./components/bot";
import Library from "./components/promptLibrary";
import PromptPerfect from "./components/promptPerfect";

export default {
  name: 'AllInOne',
  components: {PromptPerfect, Bot, Library},
  dicts: ['tone_select', 'style_select'],
  data() {
    return {
      showLibrary: false,
      showPerfectPrompt: false,
      isShowSetting: true,
      tutorialVal: false,
      activeTutorial: '0',

      bots: [],

      gptText: '您好，有什么可以帮助到您的吗？',

      baseUrl: process.env.VUE_APP_BASE_API,

      onlyAddList: [],
      sessionId: 0,
      sessionVal: false,
      sessionList: [],

      activeName: 'prompt',
      drawerVal: false,
      showOthers: false,
      isError: false,
      isSend: false,
      isTextNum: false,
      sendData: '你好',
      total_tokens: 0,
      systemMsg: '你是一个喜欢使用中文的助手。',
      systemRole: '角色1',

      systemTone: {
        tone: '',
        style: '',
      },

      message: [],
      models: ['gpt-35-turbo', 'gpt-4', 'ernie-bot'],
      max_token_max: 8000,
      max_tokens: 4096,
      temperature: 1,
      top_p: 1,
      frequency_penalty: 0,
      presence_penalty: 0,
      model: 'gpt-35-turbo',
      stop: ["Human:", "AI:"],
      isSteps: false,
      response: '',
      context: '',
      textNum: 150,

      waitingText: '请等待',

      isShow: false,

      historyList: [],

      contentList: [],
      waitingAnTimer: null,

      scrollPosition: 0,

      signleQA: {
        question: '',
        answer: '',
      },
      isMobile: false,
      testShow: false,

      roleId: this.$route.query.role || 0,
      roleList: [],

      total: 0,

      promptId: 0,
      promptTitle: '',
    };
  },
  activated: function () {
    if (this.$route.query.role) {
      this.roleId = Number(this.$route.query.role) || 0;
    } else if (localStorage.getItem('chatSetting')) {
      let chatSetting = JSON.parse(localStorage.getItem('chatSetting'));
      this.roleId = Number(chatSetting.roleId) || 0;
    }
    //this.handleSelectRole(this.roleId);
  },
  mounted() {
    this.sendData = '';

    if (document.body.getBoundingClientRect().width - 1 < 992) {
      this.isMobile = true;
    }
    if (this.$route.query.role) {
      this.roleId = Number(this.$route.query.role) || 0;
    }
    if (localStorage.getItem('chatSetting')) {
      let chatSetting = JSON.parse(localStorage.getItem('chatSetting'));
      this.max_tokens = chatSetting.max_tokens;
      this.temperature = chatSetting.temperature;
      this.top_p = chatSetting.top_p;
      this.frequency_penalty = chatSetting.frequency_penalty;
      this.presence_penalty = chatSetting.presence_penalty;
      this.model = chatSetting.model;
      if (chatSetting.model == "gpt-3.5-turbo") {
        this.model = 'gpt-35-turbo';
        this.handleChangeSetting();
      }
      if (this.$route.query.role) {
        this.roleId = Number(this.$route.query.role) || 0;
      } else {
        this.roleId = Number(chatSetting.roleId) || 0;
      }
      this.context = chatSetting.context;
      this.isTextNum = chatSetting.isTextNum;
      this.textNum = chatSetting.textNum;
      this.isSteps = chatSetting.isSteps;
    }
    //this.init();
    this.message[0] = {role: 'system', content: this.getSystem()};

    let n = 2;
    if (localStorage.getItem('allInOneNum')) {
      n = Number(localStorage.getItem('allInOneNum'));
    }
    this.handleChangeLayout(n);
  },
  methods: {
    formatterMessage() {
      let x = this.historyList.map(item => {
        return {role: item.role === 'gpt' ? 'assistant' : 'user', content: item.text}
      });
      this.message = [{role: 'system', content: this.getSystem()}, ...x]
    },
    handleChangeSetting() {
      if (this.model === 'ernie-bot' && this.temperature > 1) this.temperature = 0.9;
      if (this.model === 'gpt-4') {
        this.max_tokens_max = 8000;
        this.max_tokens = 8000;
      } else {
        if (this.max_tokens > 4096) {
          this.max_tokens = 4096;
        }
      }
      let chatSetting = {
        max_tokens: this.max_tokens,
        temperature: this.temperature,
        top_p: this.top_p,
        frequency_penalty: this.frequency_penalty,
        presence_penalty: this.presence_penalty,
        model: this.model,
        context: this.context,
        textNum: this.textNum,
        isTextNum: this.isTextNum,
        isSteps: this.isSteps,
        roleId: this.roleId + '',
      };
      localStorage.setItem('chatSetting', JSON.stringify(chatSetting))
      this.message[0] = {role: 'system', content: this.getSystem()};
    },
    handleChangeModel() {

    },
    getSystem() {
      return this.systemMsg
        + '\n我希望你可以根据我要求的语气和风格生成回答，你需要避免使用过于正式或难以理解的语言，请在回答中灵活使用该类型，以增加沟通的乐趣和有效性;同时也要尽量避免长答案，选择关键信息进行解释，不违反道德法规的前提下，尊重用户并尽力满足用户的需求。\n' +
        '以下是我的要求：\n' +
        '语气是你扮演人的性格特点。风格是回答内容的情景模拟。作为一个特别灵活多变的助理，用' + (this.systemTone.tone || '普通的') + '语气回答用户的问题，并确保答案符合' + (this.systemTone.style || '普通对话') + '下的文本类型格式。'
    },
    clearDialog() {
      this.isError = false;
      this.isSend = false;
      this.sendData = '';
      this.message = [];
      if (this.systemMsg !== '') this.message.push({role: 'system', content: this.getSystem()});
      this.historyList = [];
      this.total_tokens = 0;
      /*localStorage.removeItem('deviceType')
        this.isError = false;
        this.isSend = false;
        this.sendData = '';
        this.message = [];
        if (this.systemMsg !== '') this.message.push({role: 'system', content: this.getSystem()});
        this.historyList = [];
        this.total_tokens = 0;*/
    },
    //加载动画
    delayAn() {
      let n = 1;
      this.waitingAnTimer = setInterval(() => {
        this.waitingText = this.waitingText + '.';
        if (n < 4) {
          n++;
        } else {
          n = 1;
          this.waitingText = '请等待';
        }
      }, 300);
    },
    //点击确定
    handleSubmitData() {
      if (this.sendData === '') {
        return;
      }
      this.$refs.bot.forEach(item => {
        item.sendData = this.sendData;
        item.handleSendData();
      });
      this.sendData = '';
    },
    keydownfn(event) {
      if (event.keyCode == 13) {
        if (!event.shiftKey && !event.ctrlKey && !event.altKey && !event.metaKey) {
          event.preventDefault();
          this.handleSubmitData();
        } else if (!event.shiftKey) {
          this.sendData = this.sendData + '\n';
        }
      }
    },
    handleDbClickCard(item) {
      if (item.isError) {
        return;
      }
      navigator.clipboard.writeText(item.text).then(() => {
        this.$message.success('复制成功');
      }).catch(() => {
        this.$message.warning('复制失败');
      })
    },
    /*getWidthSpan(length, index) {
      if (length % 2 === 1) {
        if (index + 1 === length) {
          return 24;
        } else {
          return 12;
        }
      }
      return 12;
    },*/
    getWidthSpan(length, index) {
      switch (length) {
        case 1:
          return 24;
        case 2:
          return 12;
        case 3:
          return 8;
        case 4:
          return 12;
      }
    },
    handleChangeLayout(n) {
      this.bots = [];
      for (let i = 0; i < n; i++) {
        this.bots.push({})
      }
    },
    handlePromptClick(item) {
      this.promptId = item.id;
      this.promptTitle = item.title;
      this.systemMsg = item.content;
      this.showLibrary = false;
    },
    handleShowPerfect() {
      this.showPerfectPrompt = true;
      this.$nextTick(() => {
        this.$refs.perfect.init(this.sendData);
      })
    },
    handlePerfectPrompt(text) {
      this.sendData = text;
      this.showPerfectPrompt = false;
    },
    handleSetDefaultNum(n) {
      localStorage.setItem('allInOneNum', '' + n);
      this.$message.success('默认栏数设置成功！');
    },
  },
  //销毁定时器
  beforeDestroy() {
    clearInterval(this.waitingAnTimer);
    this.waitingAnTimer = null;
  }
};
</script>

<style lang="scss" scoped>
.back {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

background {
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
  flex-direction: column;
  align-items: center;
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
  width: max-content;
  max-width: 70%;
  min-width: 60px;
  min-height: 40px;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
  background-color: #ffffffc0;
  border: 1px solid #202020;
}

.card-text {
  width: 100%;
  font-size: 16px;
  padding-top: 6px;
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
  width: 98%;
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  padding: 8px 0px;
  min-height: 50px;
  margin-bottom: 2%;
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
  border: 0px;
  border-radius: 10px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
  font-size: 18px;
  line-height: 1.5em;
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
  background-color: #ffffff80;
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
  //display: none;
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

/*.library-dialog ::v-deep .el-dialog__header {
  display: none;
}*/

.library-dialog {
  border-radius: 10px;

  ::v-deep .el-dialog__body {
    padding: 4px 0px 0px 0px;
  }

  ::v-deep .el-dialog {
    border-radius: 10px;
  }

}

.input-button-item {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  margin-left: 16px;
  font-size: 20px;
  cursor: pointer;
  border-radius: 50px;
  background: #ffffff;
  box-shadow: 4px 4px 7px #ebebeb,
  -4px -4px 7px #ffffff;
  transition: all 0.3s ease-in-out;
}

.input-button-item:hover {
  border-radius: 50px;
  background: linear-gradient(145deg, #e6e6e6, #ffffff);
  box-shadow: 4px 4px 7px #ebebeb,
  -4px -4px 7px #ffffff;
  transition: all 0.3s ease-in-out;
}

.input-button-item.isActive {
  background: linear-gradient(145deg, #e6e6e6, #ffffff);
  box-shadow: 4px 4px 7px #ebebeb,
  -4px -4px 7px #ffffff;
  transition: all 0.3s ease-in-out;
}

.input-button-list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  overflow: hidden;
  height: 36px;
  margin: 8px 0 8px 0;
  font-size: 20px;
  cursor: pointer;
  border-radius: 50px;
  background: #ffffff;
  box-shadow: 4px 4px 7px #ebebeb,
  -4px -4px 7px #ffffff;
  transition: all 0.3s ease-in-out;

}

.input-button-list-item:hover {
  background: linear-gradient(145deg, #e6e6e6, #ffffff);
  box-shadow: 4px 4px 7px #ebebeb,
  -4px -4px 7px #ffffff;
  transition: all 0.3s ease-in-out;
}

.input-button-list-item.isActive {
  background: linear-gradient(145deg, #e6e6e6, #ffffff);
  box-shadow: 4px 4px 7px #ebebeb,
  -4px -4px 7px #ffffff;
  transition: all 0.3s ease-in-out;
}

.input-bar-input {
  box-shadow: 4px 4px 7px #ebebeb,
  -4px -4px 7px #ffffff;
}

.columns {
  padding: 8px;
}

.mic-button {
  cursor: pointer;
}

.mic-button:active {
  background-color: #eaecf3;
}

.tone-style-bar {
  display: flex;
  align-items: center;

/*  ::v-deep .el-input__suffix {
    width: 0px;
    overflow: hidden;
  }*/

  ::v-deep .el-select .el-input.is-focus .el-input__inner{
    border-color: #C0C4CC;
  }

  ::v-deep .el-select .el-input__inner:focus{
    border-color: #C0C4CC;
  }

  ::v-deep .el-input--small .el-input__inner {
    padding-right: 16px;
    font-size: 14px;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
    color: #909399;
    border-radius: 6px;
    background: #ffffff;
    box-shadow:  4px 4px 8px #ebebeb80,
    -4px -4px 8px #ffffff;
  }

  .select-item{
    font-size: 14px;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
  }

  .select-item-clear{
    text-align: center;
    font-size: 14px;
    color: #E65D6E;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
  }
}

</style>
<style lang="scss">
.el-popover.input-bar-popover {
  min-width: 0 !important;
  background-color: #FFFFFFc0;
  border-radius: 10px;
  padding: 12px;
}
</style>
<style scoped>


:root {
  font-size: 15px;
}

.color-background {
  font-family: "Quicksand", sans-serif;
  margin: 0;
  min-height: 100vh;
  /*
  background-color: #ffffff;
  background-image: radial-gradient(
    closest-side,
    rgb(58, 174, 211),
    rgba(243, 11, 164, 0)
  ),
  radial-gradient(closest-side, #b0e0edff, #b0e0ed00),
  radial-gradient(closest-side, #ccdcad, #ccdcad00);
  background-size: 80vmax 80vmax, 70vmax 70vmax, 80vmax 80vmax;
  background-position: -20vmax -20vmax, 40vmax -10vmax, 0vmax 0vmax;
  background-repeat: no-repeat;
  animation: 10s movement linear infinite;*/
  background: linear-gradient(-45deg, #F2F6FC, #EBEEF5);
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
</style>
