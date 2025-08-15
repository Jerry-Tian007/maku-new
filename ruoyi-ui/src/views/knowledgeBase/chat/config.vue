<template>
  <div class="app-container full-screen flex-layout column"
    style="background-color: rgba(255,255, 255, 0.8);border-radius: 6px;box-shadow: 0 0 5px 1px rgba(0, 0, 0, 0.2);">
    <div style="width: fit-content;font-size: 15px;cursor: pointer;" class="sub-color" @click="handleBack()">
      <i class="el-icon-arrow-left" /><span style="margin-left: 4px;">{{ $t('AllInOne.returnConversation') }}</span>
    </div>
    <div class="main-title flex-layout align-center" style="margin-top:20px;padding-left: 4px;width: 85%;margin: auto;">
      <span style="font-weight: bold;">{{ $t('AllInOne.paramSetting') }}</span>
      <img :src="botImgUrl"
        style="width: 22px;height: 22px;border-radius: 16px;pointer-events: none;margin-left: 12px" />
      <el-dropdown @command="handleCommand" trigger="hover" style="margin-left: 6px;height:20px;line-height: 20px;">
        <span class="el-dropdown-link" style="cursor: pointer;">
          {{ bot.type }}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-for="item in botList" :key="item.type" :command="item.type">
            <div style="display: flex;align-items: center;">
              <div style="margin-left: 8px;display: flex;align-items: center">
                <img :src="item.icon || placeholder"
                  style="width: 22px;height: 22px;border-radius: 16px;pointer-events: none" />
                <span style="margin-left: 8px;">{{ item.type }}</span>
              </div>
            </div>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <div class="flex-1 flex-layout gap10 pd-v6" style="overflow-y: auto;width: 85%;margin: auto;">
      <div class="setting_div">
        <div class="role_setting">
          <div class="role_setting_item">
            <span style="font-weight: bold;">
              {{ $t('AllInOne.roleSetting') }}
            </span>
            <el-input class="no-border" v-model="promptValue.name" />
          </div>
          <div class="role_description">
            <span style="font-weight: bold;">
              {{ $t('AllInOne.roleDescription') }}
            </span>
            <div class="text_area_div">
              <el-input class="no-border" type="textarea" :autosize="{ minRows: 9, maxRows: 9 }"
                v-model="promptValue.prompt" />
              <div class="operation_div">
                <el-button type="primary" size="small" style="width: 80px;" :disabled="promptSaveState"
                  @click="handleAddPrompt">{{ $t('save')
                  }}</el-button>
                <!-- <el-button type="primary" size="small" style="width: 80px;">{{ $t('AllInOne.optimize') }}</el-button> -->
              </div>
            </div>
          </div>
        </div>
        <div class="token_setting">
          <div style="width: 100%; display: flex;flex: 1;align-items: center">
            <div class="max_token_div">
              <span>
                Max Token
                <el-tooltip :content="$t('AllInOne.maxTokenText')" placement="top" :open-delay="300"
                  :visible-arrow="false">
                  <svg-icon icon-class="explanatoryNote" style="font-size: 13px;" />
                </el-tooltip>
              </span>
              <el-input-number v-model="chatParams.maxTokens.value" :min="1" :max="maxTokens_max" ref="maxTokens"
                class="counter-wrapper" :disabled="chatParams.maxTokens.operable !== '1'" />
            </div>
            <div class="max_token_div">
              <span>
                Top K
                <el-tooltip :content="$t('AllInOne.topKText')" placement="top" :open-delay="300" :visible-arrow="false">
                  <svg-icon icon-class="explanatoryNote" style="font-size: 13px;" />
                </el-tooltip>
              </span>
              <el-input-number v-model="chatParams.topK.value" :min="1" :max="99" class="counter-wrapper" ref="topK"
                :disabled="chatParams.topK.operable !== '1'" />
            </div>
            <!-- <div class="max_token_div">
              <span>
                Repetition Penalty
                <el-tooltip :content="$t('AllInOne.frequencyPenalty')" placement="top" :open-delay="300"
                  :visible-arrow="false">
                  <svg-icon icon-class="explanatoryNote" style="font-size: 13px;" />
                </el-tooltip>
              </span>
              <el-input-number v-model="chatParams.repetitionPenalty.value" :min="0" :max="10" :precision="1"
                ref="repetitionPenalty" class="counter-wrapper" :step="0.1"
                :disabled="chatParams.repetitionPenalty.operable !== '1'" />
            </div> -->
            <div class="max_token_div">
              <span>
                Top N
                <el-tooltip :content="$t('AllInOne.topNText')" placement="top" :open-delay="300" :visible-arrow="false">
                  <svg-icon icon-class="explanatoryNote" style="font-size: 13px;" />
                </el-tooltip>
              </span>
              <el-input-number v-model="chatParams.topN.value" :min="1" :max="99" class="counter-wrapper" ref="topN"
                :disabled="chatParams.topN.operable !== '1'" />
            </div>
          </div>
          <div style="width: 100%; display: flex;flex: 1;align-items: center">
            <div class="max_token_div">
              <span>
                Top P
                <el-tooltip :content="$t('AllInOne.topPText')" placement="top" :open-delay="300" :visible-arrow="false">
                  <svg-icon icon-class="explanatoryNote" style="font-size: 13px;" />
                </el-tooltip>
              </span>
              <div style="display: flex; align-items: center; gap: 6px;">
                <el-slider v-model="chatParams.topP.value" :min="0" :max="1" :precision="1" style="width: 80%;"
                  :step="0.1" :disabled="chatParams.topP.operable !== '1'" />
                <span style="font-size: 14px;color: gray;">
                  {{ chatParams.topP.value }}
                </span>
              </div>
            </div>
            <div class="max_token_div">
              <span>
                Temperature
                <el-tooltip :content="$t('AllInOne.temperatureText')" placement="top" :open-delay="300"
                  :visible-arrow="false">
                  <svg-icon icon-class="explanatoryNote" style="font-size: 13px;" />
                </el-tooltip>
              </span>
              <div style="display: flex; align-items: center; gap: 6px;">
                <el-slider v-model="chatParams.temperature.value" :min="0" :max="1" :precision="1" style="width: 80%;"
                  :disabled="chatParams.temperature.operable !== '1'" :step="0.1" />
                <span style="font-size: 14px;color: gray;">
                  {{ chatParams.temperature.value }}
                </span>
              </div>
            </div>
          </div>
        </div>
        <!-- <div class="bot_setting">

        </div> -->
      </div>
      <div style="flex:1;min-width: 500px;">
        <div class="prompt_library">
          <span style="font-weight: bold;line-height: 24px;font-size: 18px;">
            {{ $t('AllInOne.promptLibrary') }}
          </span>
          <div class="prompt_library_div">
            <el-tabs v-model="activeName" stretch @tab-click="handleTabClick"
              style="width: 100%; height: 100%;display: flex;flex-direction: column;">
              <el-tab-pane :label="$t('AllInOne.me')" name="me" class="tab_content">
                <div style="flex: 1;display: flex;align-items: center;background-color: white;border-radius: 6px;">
                  <div style="flex: 1.3;color: gray;display: flex; align-items: center;justify-content: center;">
                    <svg-icon icon-class="search" style="font-size: 15px;" />
                    <span>{{ $t('search') }}</span>
                  </div>
                  <el-input class="no-border" style="flex: 6;" v-model="promptMeSearchValue" @input="handleQueryPrompt"
                    clearable />
                </div>
                <div class="tab_list_div" v-if="!findPromptLoading">
                  <div
                    style="flex: 8;display: flex;flex-direction: column;gap: 12px; width: 100%;align-items: center;overflow-y: auto;">
                    <div :class="promptListCurr === prompt.id ? 'list_div_true' : 'list_div'"
                      v-for="prompt of promptList.rows" :key="prompt.id">
                      <span style="width: 50%; overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                        {{ prompt.name }}
                      </span>
                      <div v-if="promptListCurr !== prompt.id"
                        style="width: 40%;display: flex;justify-content: flex-end;">
                        <el-button type="text" v-if="releaseState" @click="handleRelease(prompt)">
                          {{ prompt.releaseStatus === '0' ? $t('AllInOne.release') : $t('AllInOne.releases') }}
                        </el-button>
                        <el-button type="text" style="color: #4E71EB;" @click="handlePromptItem(prompt)">
                          {{ $t('AllInOne.use') }}
                        </el-button>
                        <el-button type="text" style="color: red;" @click="handleDeletedPromptItem(prompt)">
                          {{ $t('deleted') }}
                        </el-button>
                      </div>
                      <div v-else>
                        <el-button type="text" style="color: gray;">{{ $t('AllInOne.inUse') }}</el-button>
                        <el-button type="text" @click="handleCancelSelect()">{{ $t('cancel') }}</el-button>
                      </div>
                    </div>
                  </div>
                  <div style="flex: 1;width: 90%;overflow-x: auto;">
                    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                      :current-page="promptPage.pageNum" :page-sizes="[10, 20, 50, 100]"
                      :page-size="promptPage.pageSize" layout="total, sizes, prev, pager, next, jumper"
                      :total="promptList.total">
                    </el-pagination>
                  </div>
                </div>
                <div class="tab_list_div" v-if="findPromptLoading" style="justify-content: center;">
                  <svg-icon icon-class="loading" style="font-size: 30px;animation: spin 2s linear infinite;" />
                </div>
              </el-tab-pane>
              <el-tab-pane :label="$t('AllInOne.community')" name="community" class="tab_content">
                <div style="flex: 1;display: flex;align-items: center;background-color: white;border-radius: 6px;">
                  <div style="flex: 1.3;color: gray;display: flex; align-items: center;justify-content: center;">
                    <svg-icon icon-class="search" style="font-size: 15px;" />
                    <span>{{ $t('search') }}</span>
                  </div>
                  <el-input class="no-border" style="flex: 6;" v-model="promptSearchValue" @input="handleQueryCommunity"
                    clearable />
                </div>
                <div class="tab_list_div" v-if="!findPromptLoading">
                  <div
                    style="flex: 8;display: flex;flex-direction: column;gap: 12px; width: 100%;align-items: center;overflow-y: auto;">
                    <div :class="promptListCurr === prompt.id ? 'list_div_true' : 'list_div'"
                      v-for="prompt of promptList.rows" :key="prompt.id">
                      <span>
                        {{ prompt.name }}
                      </span>
                      <div v-if="promptListCurr !== prompt.id">
                        <el-button type="text" style="color: orange;" @click="handleCommunitySave(prompt)">
                          {{ $t('save') }}
                        </el-button>
                        <el-button type="text" style="color: #4E71EB;" @click="handleCommunityPromptItem(prompt)">
                          {{ $t('AllInOne.use') }}
                        </el-button>
                      </div>
                      <div v-else>
                        <el-button type="text" style="color: orange;" @click="handleCommunitySave(prompt)">
                          {{ $t('save') }}
                        </el-button>
                        <el-button type="text" style="color: gray;">{{ $t('AllInOne.inUse') }}</el-button>
                        <el-button type="text" @click="handleCancelSelect()">{{ $t('cancel') }}</el-button>
                      </div>
                    </div>
                  </div>
                  <div style="flex: 1;width: 90%;overflow-x: auto;">
                    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                      :current-page="promptPage.pageNum" :page-sizes="[1, 10, 20, 50, 100]"
                      :page-size="promptPage.pageSize" layout="total, sizes, prev, pager, next, jumper"
                      :total="promptList.total">
                    </el-pagination>
                  </div>
                </div>
                <div class="tab_list_div" v-if="findPromptLoading" style="justify-content: center;">
                  <svg-icon icon-class="loading" style="font-size: 30px;animation: spin 2s linear infinite;" />
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </div>
    </div>
    <div class=" flex-layout center-center pd-v10">
      <el-button class="secondary" style="width: 120px;" @click="handleRest">{{ $t('rest') }}</el-button>
      <el-button type="primary" style="width: 120px;" @click="handleSaveModel">{{ $t('app') }}</el-button>
    </div>
  </div>
</template>
<script>
import placeholder from "@/assets/logo/logo-placeholder.jpg";
import { publicPromptList, personalPromptList, findModelParam, saveModelParam, addPrompt, updatePrompt, deletedPrompt, communitySave, releasePrompt } from '@/api/kbChat';

export default {
  name: 'ChatConfig',
  props: {
    visibility: {
      type: Boolean,
      default: false
    },
    botType: {
      type: String,
      default: ''
    },
    currentModel: {
      type: String,
      default: ''
    },
    currentUsekb: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      activeName: 'me',
      promptMeSearchValue: '',
      promptSearchValue: '',
      promptValue: {
        name: '',
        prompt: ''
      },
      sessionPageNum: 1,
      sessionPageSize: 10,
      promptListCurr: '',
      chatParams: {
        temperature: {
          value: 0.0,
          operable: '1'
        },
        maxTokens: {
          value: 1,
          operable: '1'
        },
        topK: {
          value: 99,
          operable: '1'
        },
        topN: {
          value: 99,
          operable: '1'
        },
        topP: {
          value: 0.0,
          operable: '1'
        },
        repetitionPenalty: {
          value: 0.0,
          operable: '1'
        }
      },
      chatParamsList: [{
        label: 'Max Tokens',
        name: 'maxTokens',
        type: 'input',
        value: 1,
        operable: '1'
      }, {
        label: 'Top K',
        name: 'topK',
        type: 'input',
        value: 99,
        operable: '1'
      }, {
        label: 'Top N',
        name: 'topN',
        type: 'input',
        value: 99,
        operable: '1'
      }, {
        label: 'Top P',
        name: 'topP',
        type: 'slider',
        value: 0.0,
        operable: '1'
      }, {
        label: 'Temperature',
        name: 'temperature',
        type: 'slider',
        value: 0.0,
        operable: '1'
      }, {
        label: 'Repetition Penalty',
        name: 'repetitionPenalty',
        type: 'input',
        value: 0.0,
        operable: '1'
      }],
      prompt: '',
      maxTokens_max: 100,
      botImgUrl: placeholder,
      maxTokenDict: {
        "default": {
          max: 4096,
          min: 1
        },
        "gpt-4o": {
          max: 50,
          min: 1
        },
        "glm-4": {
          max: 20,
          min: 1
        },
        "claude3": {
          max: 40,
          min: 1
        },
        "claude3.5": {
          max: 30,
          min: 1
        },
        "deepseek-R1": {
          max: 8192,
          min: 1
        },
        "deepseek-R1-volcengine": {
          max: 16384,
          min: 1
        },
        "deepseek-R1-siliconflow": {
          max: 16384,
          min: 1
        },
        "deepseek-R1-localdeployed": {
          max: 16384,
          min: 1
        }
      },
      hint: {
        maxWords: '回答字数限定',
        maxTokens: '最大Token数量'
      },

      styleOptions: [
        {
          value: '风格1',
          label: '风格1'
        },
        {
          value: '风格2',
          label: '风格2'
        }
      ],
      toneOptions: [
        {
          value: '语气1',
          label: '语气1'
        },
        {
          value: '语气2',
          label: '语气2'
        }
      ],

      bot: {},
      botList: [],
      otherBotList: [],
      botScene: ['vision', 'text'],
      promptList: {
        total: 0,
        rows: []
      },
      findPromptLoading: true,
      promptPage: {
        pageSize: 10,
        pageNum: 1
      },
      currTabName: 'me',
      releaseState: false,
      releaseAuth: ['admin', 'manager'],
      promptSaveState: false
    }
  },
  mounted() {
    this.botList = this.$store.state.user.botList.filter(item => this.botScene.includes(item.scene));
    // const windowModel = this.$store.getters.windowModel;
    // if (windowModel[0]['botType']) {
    if (this.currentModel) {
      this.botList.forEach(item => {
        // if (item.type === windowModel[0]['botType']) {
        if (item.type === this.currentModel) {
          this.bot = item;
          this.botImgUrl = item.icon;
          if (this.maxTokenDict.hasOwnProperty(item.type)) {
            this.maxTokens_max = this.maxTokenDict[item.type].max;
          } else {
            this.maxTokens_max = this.maxTokenDict['default'].max;
          }
        }
      })
    } else {
      this.bot = this.botList[2];
      this.botImgUrl = this.botList[2].icon;
    }
    this.handleFindModelParam(this.bot.type);
    this.handleQueryPrompt();

    const roleAuth = this.$store.getters.roles;
    roleAuth.forEach(item => {
      this.releaseState = this.releaseAuth.includes(item)
    });
  },
  methods: {
    handleQueryPrompt() {
      this.findPromptLoading = true;
      personalPromptList({ name: this.promptMeSearchValue, kbManagerIds: this.currentUsekb }, this.promptPage).then(res => {
        if (res.code === 200) {
          this.promptList.rows = res.rows;
          this.promptList.total = res.total;
          this.findPromptLoading = false;
          this.sessionPageNum = 1;
          res.rows.forEach(item => {
            if (item.id === this.promptValue.id) {
              this.promptValue.releaseStatus = item.releaseStatus;
            }
          })
        }
      }).catch(err => {
        this.$message.error(this.$t('AllInOne.findListError'));
      });
    },
    handleQueryCommunity() {
      this.findPromptLoading = true;
      publicPromptList({ name: this.promptSearchValue }, this.promptPage).then(res => {
        if (res.code === 200) {
          this.promptList.rows = res.rows;
          this.promptList.total = res.total;
          this.findPromptLoading = false;
        }
      }).catch(err => {
        this.$message.error(this.$t('AllInOne.findListError'));
      });
    },
    handleBack() {
      this.$emit('update:visibility', false);
    },
    handleCommand(command) {
      this.handleFindModelParam(command);
      this.bot = this.botList.find(item => item.type === command);
      this.otherBotList = [];
      this.botImgUrl = this.bot.icon || placeholder;
    },
    handleRest() {
      this.chatParams = {
        temperature: {
          value: 0.3,
          operable: '1'
        },
        maxTokens: {
          value: 4096,
          operable: '1'
        },
        topK: {
          value: 10,
          operable: '1'
        },
        topN: {
          value: 99,
          operable: '1'
        },
        topP: {
          value: 0.9,
          operable: '1'
        },
        repetitionPenalty: {
          value: 1.0,
          operable: '1'
        }
      };
      this.promptValue = {
        name: '',
        prompt: ''
      };
      this.promptListCurr = '';
    },
    handleTabClick(tab) {
      this.promptList = {
        rows: [],
        total: 0
      }
      this.currTabName = tab.name;
      switch (tab.name) {
        case 'me':
          this.handleQueryPrompt();
          break;
        case 'community':
          this.handleQueryCommunity();
          break;
      }
    },
    handleCancelSelect() {
      this.promptValue = {};
      this.promptListCurr = '';
    },
    handlePromptItem(prompt) {
      this.promptValue = JSON.parse(JSON.stringify(prompt));
      this.promptListCurr = prompt.id;
    },
    handleCommunityPromptItem(prompt) {
      this.promptValue = JSON.parse(JSON.stringify(prompt));
      this.promptValue.id = this.promptValue.personalPromptId;
      if (this.$store.state.user.admin) {
        this.promptListCurr = prompt.id;
      } else {
        this.promptListCurr = '';
      }
    },
    /* //历史记录列表无限滚动查询
    async sessionLoading() {
      console.log(this.promptList.total)
      if(this.promptList.total <= 10){
        return;
      }
      console.log(this.promptList)
      this.findPromptLoading = true;
      if (this.activeName === 'me') {
        try {
          this.promptPage = {
            pageNum: this.promptPage.pageNum + 1
          }
          const data = await publicPromptList({ name: this.promptSearchValue }, this.promptPage);
          if (data.rows.length === 0) {
            return;
          } else {
            // 追加数据到列表
            if (data.code === 200) {
              this.promptList.rows = res.rows;
              this.promptList.total = res.total;
              this.sessionPageNum = this.promptPage.pageNum;
              this.findPromptLoading = false;
            }
          }
        } catch (error) {
          console.error(error);
          this.findPromptLoading = false;
        }
      } else {
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
              this.promptTotal = data.total;
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
      }

    }, */
    handleFindModelParam(command) {
      findModelParam({ modelName: command }).then(res => {
        if (res.code === 200) {
          const data = res.data;
          this.promptListCurr = data.promptId;
          this.promptValue = JSON.parse(data.promptSetting);
          this.promptValue.id = data.promptId;
          this.botList.forEach(item => {
            if (item.type === data.model) {
              this.bot = item;
              this.botImgUrl = item.icon;
            }
          });
          const configSetting = JSON.parse(data.configureSetting);
          configSetting.forEach(item => {
            if (this.chatParams.hasOwnProperty(item.name)) {
              this.chatParams[item.name].value = item.value;
              this.chatParams[item.name].operable = item.operable;
              if (this.$refs.hasOwnProperty(item.name)) {
                if (item.operable === '0') {
                  this.$refs[item.name].$el.style.setProperty('--increase-color', 'rgb(117, 117, 117)');
                  this.$refs[item.name].$el.style.setProperty('--decrease-color', 'rgb(117, 117, 117)');
                } else {
                  this.$refs[item.name].$el.style.setProperty('--increase-color', 'linear-gradient(270deg, #6DA7CE 0%, #4E71EB 100%)');
                  this.$refs[item.name].$el.style.setProperty('--decrease-color', 'linear-gradient(270deg, #6DA7CE 0%, #4E71EB 100%)');
                }
              }
            }
          });
        }
      }).catch(err => {
        this.$message.error(this.$t('AllInOne.findModelError'));
      });
    },
    handleSaveModel() {

      this.handleAddPrompt('app');

      let chatParamList = [];
      this.chatParamsList.forEach(list => {
        if (this.chatParams.hasOwnProperty(list.name)) {
          chatParamList.push({
            label: list.label,
            name: list.name,
            type: list.type,
            value: this.chatParams[list.name].value,
            operable: '1'
          });
        }
      })
      const subData = {
        userId: JSON.stringify(this.$store.getters.userId),
        promptId: this.promptListCurr,
        promptSetting: JSON.stringify({
          name: this.promptValue.name,
          prompt: this.promptValue.prompt
        }),
        configureSetting: JSON.stringify(chatParamList),
        model: this.bot.type,
        modelApplicationList: []
      }

      saveModelParam(subData).then(res => {
        if (res.code === 200) {
          this.$modal.msgSuccess(this.$t('AllInOne.saveModelSuccess'));
        }
      }).catch(err => {
        this.$message.error(this.$t('AllInOne.findModelError'));
      })
    },
    handleSizeChange(val) {
      this.promptPage.pageSize = val;
      let tabName = this.currTabName;
      switch (tabName) {
        case 'me':
          this.handleQueryPrompt();
          break;
        case 'community':
          this.handleQueryCommunity();
          break;
      }
    },
    handleCurrentChange(val) {
      this.promptPage.pageNum = val;
      let tabName = this.currTabName;
      switch (tabName) {
        case 'me':
          this.handleQueryPrompt();
          break;
        case 'community':
          this.handleQueryCommunity();
          break;
      }
    },
    handleCommunitySave(val) {
      communitySave({ id: val.id }).then(res => {
        if (res.code === 200) {
          this.$modal.msgSuccess(this.$t('chatBI.saveSuccess'));
        }
      }).catch(err => {
        this.$message.error(this.$t('chatBI.saveError'));
      });
    },
    handleDeletedPromptItem(val) {
      if (val.releaseStatus === '1') {
        this.$message.warning(this.$t('AllInOne.deletedRelease'))
        return;
      }
      this.$modal.confirm(this.$t('kbManager.areYouDelete')).then(function () {
        return deletedPrompt(val.id);
      }).then(() => {
        this.handleQueryPrompt();
        this.$modal.msgSuccess(this.$t('deletedSuccess'));
      })
    },
    handleAddPrompt(index) {
      if (this.promptValue.name || this.promptValue.prompt) {
        if (this.promptListCurr) {
          // if (this.promptValue.releaseStatus === '0') {
          if (!this.$store.state.user.admin) {
            const subData = {
              id: this.promptValue.id,
              name: this.promptValue.name,
              prompt: this.promptValue.prompt,
              isUpdateRelease: '0'
            }
            this.promptSaveState = true;
            updatePrompt(subData).then(res => {
              if (res.code === 200) {
                switch (this.currTabName) {
                  case 'me':
                    this.handleQueryPrompt();
                    break;
                }
                this.promptList.rows.forEach(item => {
                  if (item.name === this.promptValue.name) {
                    this.promptListCurr = item.id;
                  }
                })
                this.$modal.msgSuccess(this.$t('updateSuccess'));
                this.promptSaveState = false;
              }
            }).catch(err => {
              this.$message.error(this.$t('updateError'));
              this.promptSaveState = false;
            })
          } else {
            this.$confirm(this.$t('AllInOne.synchronousConfirm'), this.$t('systemPrompt'), {
              distinguishCancelAndClose: true,
              confirmButtonText: this.$t('synchronous'),
              cancelButtonText: this.$t('AllInOne.noSynchronous')
            }).then(() => {
              const subData = {
                id: this.promptValue.id,
                name: this.promptValue.name,
                prompt: this.promptValue.prompt,
                isUpdateRelease: '1'
              }
              this.promptSaveState = true;
              updatePrompt(subData).then(res => {
                if (res.code === 200) {
                  switch (this.currTabName) {
                    case 'me':
                      this.handleQueryPrompt();
                      break;
                  }
                  this.promptList.rows.forEach(item => {
                    if (item.name === this.promptValue.name) {
                      this.promptListCurr = item.id;
                    }
                  })
                  this.$modal.msgSuccess(this.$t('updateSuccess'));
                  this.promptSaveState = false;
                }
              }).catch(err => {
                this.$message.error(this.$t('updateError'));
                this.promptSaveState = false;
              })
            }).catch(action => {
              if (action === 'cancel') {
                const subData = {
                  id: this.promptValue.id,
                  name: this.promptValue.name,
                  prompt: this.promptValue.prompt,
                  isUpdateRelease: '0'
                }
                this.promptSaveState = true;
                updatePrompt(subData).then(res => {
                  if (res.code === 200) {
                    switch (this.currTabName) {
                      case 'me':
                        this.handleQueryPrompt();
                        break;
                    }
                    this.promptList.rows.forEach(item => {
                      if (item.name === this.promptValue.name) {
                        this.promptListCurr = item.id;
                      }
                    })
                    this.$modal.msgSuccess(this.$t('updateSuccess'));
                    this.promptSaveState = false;
                  }
                }).catch(err => {
                  this.$message.error(this.$t('updateError'));
                  this.promptSaveState = false;
                })
              }
            })
          }
        } else {
          this.promptSaveState = true;
          addPrompt(this.promptValue).then(res => {
            if (res.code === 200) {
              switch (this.currTabName) {
                case 'me':
                  this.handleQueryPrompt();
                  break;
              }
              this.promptList.rows.forEach(item => {
                if (item.name === this.promptValue.name) {
                  this.promptListCurr = item.id;
                  this.promptValue = JSON.parse(JSON.stringify(item));
                }
              })
              this.$modal.msgSuccess(this.$t('addSuccess'));
              this.promptSaveState = false;
            }
          }).catch(err => {
            this.$message.error(this.$t('addError'));
            this.promptSaveState = false;
          })
        }
      } else {
        if (!index) {
          this.$message.warning(this.$t('AllInOne.addPromptWarding'));
        }
        this.promptSaveState = false;
      }
    },
    handleRelease(prompt) {
      this.$confirm(prompt.releaseStatus === '1' ? this.$t('AllInOne.releaseCancel') : this.$t('AllInOne.releaseConfirm'), this.$t('systemPrompt'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel')
      }).then(() => {
        const subData = {
          id: prompt.id,
          releaseStatus: prompt.releaseStatus === '0' ? '1' : '0'
        }
        releasePrompt(subData).then(res => {
          if (res.code === 200) {
            this.$message.success(this.$t('AllInOne.releaseSuccess'))
            this.handleQueryPrompt();
          }
        }).catch(err => {
          this.$message.error(this.$t('AllInOne.releaseError'));
        });
      })
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-form--label-top .el-form-item__label {
  padding-bottom: 0;
}

::v-deep .extra-label {
  label.el-form-item__label {
    width: 100%;
  }
}

.setting_div {
  flex: 1.5;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.role_setting {
  flex: 2;
  background-color: rgb(236, 242, 253);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
}


.role_setting_item {
  flex: 1;
  width: 90%;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.role_description {
  flex: 4;
  width: 90%;
  display: flex;
  flex-direction: column;
  gap: 6px;
}


.text_area_div {
  background-color: white;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border-radius: 6px;
}

.operation_div {
  display: flex;
  justify-content: flex-end;
  padding-right: 12px;
  padding-bottom: 12px;
}


.token_setting {
  flex: 1;
  background-color: rgb(236, 242, 253);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  padding: 12px 36px;
  gap: 12px;
}

.bot_setting {
  flex: 1;
}

.prompt_library {
  background-color: rgb(236, 242, 253);
  border-radius: 12px;
  height: 100%;
  padding: 12px 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.max_token_div {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.no-border ::v-deep .el-textarea__inner {
  resize: none !important;
}

::v-deep .el-input-number__increase {
  background: var(--increase-color);
  color: rgb(137, 129, 129);
}

::v-deep .el-input-number__decrease {
  background: var(--decrease-color);
  color: white;
}


/* 定义动态颜色变量 */
.counter-wrapper {
  --increase-color: linear-gradient(270deg, #6DA7CE 0%, #4E71EB 100%);
  /* 默认增加按钮颜色 */
  --decrease-color: linear-gradient(270deg, #6DA7CE 0%, #4E71EB 100%);
  /* 默认减少按钮颜色 */
}


.prompt_library_div {
  height: 90%;
  display: flex;
  align-items: center;
  width: 95%;
  border-radius: 6px;
}

::v-deep .el-tabs__content {
  height: 100%;
}

.tab_content {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tab_list_div {
  flex: 10;
  background-color: white;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 0;
  overflow-y: auto;
  gap: 12px;
}

.list_div {
  width: 90%;
  height: 50px;
  border: 1px solid rgb(156, 156, 156);
  border-radius: 6px;
  display: flex;
  align-items: center;
  padding: 0 12px;
  justify-content: space-between;
  cursor: pointer;
}

.list_div_true {
  width: 90%;
  height: 50px;
  border: 1px solid rgb(156, 156, 156);
  border-radius: 6px;
  display: flex;
  align-items: center;
  padding: 0 12px;
  justify-content: space-between;
  border: 1px solid #4E71EB;
  background-color: rgba($color: #6DA7CE, $alpha: 0.1);
  cursor: pointer;
  color: #4E71EB;
}

.list_div:hover {
  border: 1px solid #4E71EB;
  background-color: rgba($color: #6DA7CE, $alpha: 0.1);
  cursor: pointer;
  color: #4E71EB;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
}
</style>
