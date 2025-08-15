<template>
  <div class="back" align="left">
    <div class="backgrond">
      <div
        class="chat-background"
        style=""
      >
        <div :style="isMobile ? '' : 'flex:0.25;'" v-show="!isShow || isMobile"></div>
        <!--flex布局-聊天-->
        <div class="dialog-container">
          <div
            style="flex:1;width:100%;position:relative;overflow: auto; padding-top:20px;"
            id="chatBox"
            ref="historyContent"
          >
            <!--对话列表-->
            <div
              v-for="(item,index) in historyList"
              :key="index"
              style="padding:12px;"
              :align="item.role=='user' ? 'right' : 'left'"
            >
              <el-card
                class="card"
                align="left"
                :style="'background-color: #FFFFFF80;'+ (activeIndex==index ? 'border: 2px solid #2D9CDB;' : '')+ (item.role=='gpt' ? 'min-width:200px;' : '')"
              >
                <div
                  v-if="item.role=='gpt'"
                  slot="header"
                  class="card-header"
                >
                  <div>
                    <svg
                      width="24"
                      height="24"
                      viewBox="0 0 75 75"
                      fill="none"
                      xmlns="http://www.w3.org/2000/svg"
                    >
                      <g clip-path="url(#clip0_54_2)">
                        <path
                          d="M35.8922 29.2828C36.4078 27.7359 38.5922 27.7359 39.1078 29.2828L42.1313 38.3625C42.7968 40.3584 43.918 42.1719 45.4062 43.6593C46.8943 45.1466 48.7084 46.2668 50.7047 46.9313L59.7797 49.9547C61.3266 50.4703 61.3266 52.6547 59.7797 53.1703L50.7 56.1938C48.7041 56.8592 46.8906 57.9805 45.4033 59.4686C43.9159 60.9568 42.7957 62.7709 42.1313 64.7672L39.1078 73.8422C38.9967 74.1809 38.7814 74.4759 38.4926 74.685C38.2039 74.8941 37.8565 75.0067 37.5 75.0067C37.1435 75.0067 36.7961 74.8941 36.5074 74.685C36.2187 74.4759 36.0034 74.1809 35.8922 73.8422L32.8688 64.7625C32.2038 62.7671 31.0832 60.9539 29.5959 59.4666C28.1086 57.9793 26.2955 56.8588 24.3 56.1938L15.2203 53.1703C14.8816 53.0591 14.5867 52.8438 14.3775 52.5551C14.1684 52.2664 14.0558 51.919 14.0558 51.5625C14.0558 51.206 14.1684 50.8586 14.3775 50.5699C14.5867 50.2812 14.8816 50.0659 15.2203 49.9547L24.3 46.9313C26.2955 46.2662 28.1086 45.1457 29.5959 43.6584C31.0832 42.1711 32.2038 40.3579 32.8688 38.3625L35.8922 29.2828ZM17.7844 5.38125C17.8516 5.17826 17.9811 5.00161 18.1544 4.8764C18.3278 4.7512 18.5362 4.68381 18.75 4.68381C18.9639 4.68381 19.1723 4.7512 19.3456 4.8764C19.519 5.00161 19.6484 5.17826 19.7157 5.38125L21.5297 10.8281C22.3407 13.2563 24.2438 15.1594 26.6719 15.9703L32.1188 17.7844C32.3218 17.8516 32.4984 17.9811 32.6236 18.1544C32.7488 18.3278 32.8162 18.5362 32.8162 18.75C32.8162 18.9638 32.7488 19.1722 32.6236 19.3456C32.4984 19.5189 32.3218 19.6484 32.1188 19.7156L26.6719 21.5297C25.4744 21.9287 24.3862 22.6011 23.4937 23.4936C22.6011 24.3862 21.9287 25.4743 21.5297 26.6719L19.7157 32.1188C19.6484 32.3217 19.519 32.4984 19.3456 32.6236C19.1723 32.7488 18.9639 32.8162 18.75 32.8162C18.5362 32.8162 18.3278 32.7488 18.1544 32.6236C17.9811 32.4984 17.8516 32.3217 17.7844 32.1188L15.9703 26.6719C15.5714 25.4743 14.8989 24.3862 14.0064 23.4936C13.1138 22.6011 12.0257 21.9287 10.8282 21.5297L5.38128 19.7156C5.17828 19.6484 5.00163 19.5189 4.87643 19.3456C4.75123 19.1722 4.68384 18.9638 4.68384 18.75C4.68384 18.5362 4.75123 18.3278 4.87643 18.1544C5.00163 17.9811 5.17828 17.8516 5.38128 17.7844L10.8282 15.9703C12.0257 15.5713 13.1138 14.8989 14.0064 14.0064C14.8989 13.1138 15.5714 12.0257 15.9703 10.8281L17.7844 5.38125ZM50.9203 0.464063C50.9666 0.330592 51.0533 0.214856 51.1684 0.132945C51.2835 0.0510342 51.4213 0.00701904 51.5625 0.00701904C51.7038 0.00701904 51.8415 0.0510342 51.9566 0.132945C52.0717 0.214856 52.1584 0.330592 52.2047 0.464063L53.4141 4.09219C53.9532 5.71406 55.2235 6.98438 56.8453 7.52344L60.4735 8.73281C60.6069 8.77909 60.7227 8.8658 60.8046 8.98089C60.8865 9.09598 60.9305 9.23374 60.9305 9.375C60.9305 9.51626 60.8865 9.65402 60.8046 9.76911C60.7227 9.8842 60.6069 9.97091 60.4735 10.0172L56.8453 11.2266C56.0461 11.4925 55.3198 11.941 54.7242 12.5366C54.1285 13.1323 53.68 13.8585 53.4141 14.6578L52.2047 18.2859C52.1584 18.4194 52.0717 18.5351 51.9566 18.6171C51.8415 18.699 51.7038 18.743 51.5625 18.743C51.4213 18.743 51.2835 18.699 51.1684 18.6171C51.0533 18.5351 50.9666 18.4194 50.9203 18.2859L49.711 14.6578C49.4451 13.8585 48.9965 13.1323 48.4009 12.5366C47.8053 11.941 47.079 11.4925 46.2797 11.2266L42.6563 10.0172C42.5228 9.97091 42.4071 9.8842 42.3252 9.76911C42.2432 9.65402 42.1992 9.51626 42.1992 9.375C42.1992 9.23374 42.2432 9.09598 42.3252 8.98089C42.4071 8.8658 42.5228 8.77909 42.6563 8.73281L46.2844 7.52344C47.9063 6.98438 49.1766 5.71406 49.7157 4.09219L50.9203 0.46875V0.464063Z"
                          fill="#2D9CDB"
                        />
                      </g>
                      <defs>
                        <clipPath id="clip0_54_2">
                          <rect
                            width="75"
                            height="75"
                            fill="white"
                          />
                        </clipPath>
                      </defs>
                    </svg>
                  </div>
                  <div style="display: flex;">
                    <div
                      class="icons"
                      v-if="item.text !== '' && !item.isError"
                    >
                      <i
                        class="el-icon-news"
                        @click="changeIframeShowStatus(item,index)"
                      />
                    </div>
                  </div>
                </div>

                <!--文本内容-->
                <div class="card-text" v-if="!item.text">
                  {{ item.isError ? item.errorText : waitingText }}
                  <div style="margin-top: 16px;">
                    <el-button v-if="item.isError" style="margin-left: 6px;font-size:16px;" type="primary" size="small"
                               @click="retrySend(item,index)">
                      重试
                    </el-button>
                  </div>
                </div>

                <div
                  v-else-if="item.role=='user'"
                  style="white-space: pre-wrap;">
                  {{ item.text }}
                </div>
                <div
                  v-else
                  style="white-space: pre-wrap;line-height: 1.5em;">
                  <span v-for="(data,index2) in item.answers" :key="index2">
                    <span>{{ data.text }}</span>
                    <span
                      v-for="file in data.pdf"
                      :key="file"
                      @click="openFile(item,item.citation.find(item => item.name === file).originalFileUrl,item.citation.find(item => item.name === file).page,file,index)"
                      class="index-span">
                      {{ item.citation.findIndex(item => item.name === file) + 1 }}
                    </span>
                  </span>
                </div>

                <!--文本内容
                <div class="card-text">
                  {{ item.isError ? item.errorText : '' || item.text || waitingText }}
                </div>-->
                <!--仅gpt回答时显示文件和后续问题-->
                <div
                  v-if="item.role=='gpt' && item.text != '' && !item.isError && !item.hasNoFiles"
                  style="padding-top:8px;"
                >
                  <div class="file-tag-container">
                    <span style="font-weight:600;font-size:14px;">文件：</span>
                    <span
                      v-for="(file,index2) in item.citation"
                      :key="index2">
                    <el-tag
                      @click="openFile(item,file.originalFileUrl,file.page,file.originalFileName,index)"
                      size="small"
                      style="margin: 0px 5px;margin-bottom:6px;cursor: pointer;font-size:14px;"
                      v-if="file.url != ''"
                    >
                      {{ index2 + 1 + '. ' + file.originalFileName }}
                    </el-tag>
                    </span>
                  </div>
                </div>
              </el-card>
              <div
                v-if="index > minShowButtonLength && index+1 === historyList.length && item.role === 'gpt' && item.text !== '' && !item.isError"
                class="follow-bar"
              >
                <!--                <div style="font-weight: 400;margin-right: 24px;color:#606266;">
                                  请问您认为问题是否已经解决了？
                                </div>-->
                <el-card
                  class="follow-card"
                  style="margin-right: 12px;color:#67C23A"
                >
                  <div @click="handleCompleted(item)">
                    <i class="el-icon-circle-check"/>
                    <span style="margin-left: 4px;">已解决</span>
                  </div>
                </el-card>
                <el-card
                  class="follow-card"
                  style="color:#E6A23C"
                  v-loading.fullscreen.lock="fullscreenLoading"
                >
                  <div @click="handleJump(item)">
                    <i class="el-icon-circle-close"/>
                    <span style="margin-left: 4px;">转人工</span>
                  </div>
                </el-card>
              </div>
            </div>
          </div>
        </div>

        <div :style="(isMobile ? '' : 'flex:0.25;') + 'display: flex;align-items: center'" v-show="!isShow || isMobile">
        </div>

        <!--flex布局-文件支持-->
        <div
          class="file-container"
          v-show="isShow && !isMobile"
          style="margin-top:40px;"
        >
          <div style="position: absolute;right: 20px;top: 50px;cursor: pointer;z-index: 20" @click="isShow = !isShow">
            <i class="el-icon-close" style="font-size: 20px;color: #606266"/>
          </div>
          <!--切换栏-->
          <el-tabs
            v-model="activeName"
            style="padding: 0px 10px;"
          >
            <el-tab-pane
              name="content"
              label="相关段落"
            ></el-tab-pane>
            <el-tab-pane
              name="citation"
              label="引用文献"
              :disabled="isNotPdf"
            ></el-tab-pane>
          </el-tabs>

          <!--思维过程-->
          <div
            v-show="activeName=='process'"
            style="flex: 1;overflow-y: auto;"
            align="center"
          >
            <el-card
              class="process-card"
              align="left"
            >
              <div class="card-text">
                {{ processText }}
              </div>
            </el-card>
          </div>
          <!--支持内容-->
          <div
            v-show="activeName=='content'"
            style="flex: 1;overflow-y: auto;"
            align="center"
          >
            <el-card
              class="content-card"
              align="left"
              v-for="(item, index) in contentList"
              :key="index"
            >
              <div
                slot="header"
                style="font-size:18px;font-weight: 600;"
              >{{ item.originalFileName }}
              </div>
              <div class="card-text">
                {{ item.content }}
              </div>
            </el-card>
          </div>

          <!--pdf浏览-->
          <iframe
            v-if="activeName=='citation' && !isMobile"
            :src="iframeUrl"
            frameborder="0"
            width="100%"
            height="100%"
            style="flex: 1;"
          ></iframe>
        </div>

      </div>
    </div>
    <el-drawer
      title="设置"
      :visible.sync="drawerVal"
      align="left"
      size="32%">
      <el-form
        :model="overrides"
        label-width="120px"
        style="margin-top: 20px;width:80%;"
      >
        <el-form-item label="检索文档数量">
          <template slot="label">
            <el-tooltip :content="description.top" placement="top">
              <span>检索文档数量</span>
            </el-tooltip>
          </template>
          <el-input-number
            v-model="overrides.top"
            :min="1"
            :max="4"
            :step="1"
            controls-position="right"
          ></el-input-number>
        </el-form-item>
        <el-form-item>
          <template slot="label">
            <el-tooltip :content="description.prompt_template" placement="top">
              <span>提示词模板</span>
            </el-tooltip>
          </template>
          <el-input
            :resize="false"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 8}"
            v-model="overrides.prompt_template"
            placeholder="请输入提示词模板"
            v-clear-emoij
          ></el-input>
        </el-form-item>
        <el-form-item>
          <template slot="label">
            <el-tooltip :content="description.exclude_category" placement="top">
              <span>排除类别</span>
            </el-tooltip>
          </template>
          <el-input
            :resize="false"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 8}"
            v-model="overrides.exclude_category"
            placeholder="请输入排除类别"
            v-clear-emoij
          ></el-input>
        </el-form-item>
        <el-form-item>
          <template slot="label">
            <el-tooltip :content="description.semantic_ranker" placement="top">
              <span>语义排序器</span>
            </el-tooltip>
          </template>
          <el-switch v-model="overrides.semantic_ranker" active-text="开启"></el-switch>
        </el-form-item>
        <el-form-item>
          <template slot="label">
            <el-tooltip :content="description.semantic_captions" placement="top">
              <span>概要查询</span>
            </el-tooltip>
          </template>
          <el-switch v-model="overrides.semantic_captions" active-text="开启"></el-switch>
        </el-form-item>
      </el-form>
    </el-drawer>
    <el-drawer
      :visible.sync="isShow"
      v-if="isMobile"
      size="80%"
      class="drawer-setting"
      :withHeader="false"
      :modal="false"
      style="min-width:400px;"
    >
      <div
        class="file-container"
        style="margin-top:30px;position: relative;"
      >
        <!--切换栏-->
        <el-tabs
          v-model="activeName"
          style="padding: 0px 10px;"
        >
          <el-tab-pane
            name="content"
            label="相关段落"
          ></el-tab-pane>
          <el-tab-pane
            name="citation"
            label="引用文献"
            :disabled="isNotPdf"
          ></el-tab-pane>
        </el-tabs>

        <!--思维过程-->
        <div
          v-show="activeName=='process'"
          style="flex: 1;overflow-y: auto;"
          align="center"
        >
          <el-card
            class="process-card"
            align="left"
          >
            <div class="card-text">
              {{ processText }}
            </div>
          </el-card>
        </div>
        <!--支持内容-->
        <div
          v-show="activeName=='content'"
          style="flex: 1;overflow-y: auto;"
          align="center"
        >
          <el-card
            class="content-card"
            align="left"
            v-for="(item, index) in contentList"
            :key="index"
          >
            <div
              slot="header"
              style="font-size:18px;font-weight: 600;"
            >{{ item.originalFileName }}
            </div>
            <div class="card-text">
              {{ item.content }}
            </div>
          </el-card>
        </div>

        <!--pdf浏览-->
        <iframe
          v-show="activeName=='citation'"
          :src="iframeUrl"
          frameborder="0"
          width="100%"
          style="flex: 1;height:54vh;"
        ></iframe>
      </div>
    </el-drawer>
    <!-- 内容展示抽屉 -->
    <el-dialog :visible.sync="showResult" width="300px" append-to-body :show-close="false" :close-on-click-modal="false" :close-on-press-escape="false">
      <div slot="header"></div>
      <el-result icon="success" title="问题已解决" subTitle="">
        <template slot="extra">
          <el-button size="small" type="primary" @click="handleEnd(true)">确定</el-button>
        </template>
      </el-result>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import {getToken} from '@/utils/auth'
import {addHistory} from "@/api/data/history";
import {addChatgptsummary} from "../../../api/chatGPT/chatgptsummary";

export default {
  name: 'OnlyRead',
  props:{
    historyListString: {
      type: String,
      default: '[]'
    }
  },
  data() {
    return {
      minShowButtonLength: 2,
      showResult: false,
      isMobile: false,

      activeIndex: -1,
      // language: 'Chinese' || 'English',
      language: 'English' || 'Chinese',

      baseUrl: process.env.VUE_APP_GPT_URL,
      //baseUrl: 'http://192.168.50.103:5000',
      fullscreenLoading: false,
      isNotPdf: true,
      drawerVal: false,
      isError: false,
      isSend: false,
      sendData: ' ',
      activeName: 'content',
      iframeUrl: '',
      pageNum: 1,
      isShow: false,
      waitingText: '请稍候，小助手正在为您查询',

      total_tokens: 0,
      systemMsg: '你是一个喜欢使用中文的助手',

      message: [],
      max_token_max: 4096,
      max_tokens: 4096,
      temperature: 1,
      top_p: 1,
      frequency_penalty: 0,
      presence_penalty: 0,
      model: 'gpt-3.5-turbo',
      models: ['gpt-3.5-turbo'],
      stop: ["Human:", "AI:"],
      isSteps: false,
      response: '',
      lastText: '',

      context: '',
      textNum: 150,

      historyListOld: [],
      historyList: [
        {
          "role": "user",
          "text": "什么是温室气体",
        },
        {
          "role": "gpt",
          "text": "温室气体是指大气中那些吸收和重新放出红外辐射的自然的和人为的气态成分，其中包括二氧化碳、甲烷、氧化亚氮、氢氟碳化物、全氟化碳和六氟化硫等六种气体。[262-242-1689910824060_8.pdf]",
          "citation": [{
            "name": "262-242-1689910824060_8.pdf",
            "originalFileName": "Public building operation enterprises_8.pdf",
            "originalFileUrl": "https://nygpt.blob.core.windows.net/zqlblobsource/gptFile/262/242/262-242-1689910824060.pdf?sv=2020-10-02&se=2033-07-21T03%3A40%3A24Z&sr=b&sp=r&sig=a%2BEzp3QNdq1zOiIhDPygxPo6vq%2Ff%2B8FQbBG8ltp0Uyk%3D",
            "url": "/gptData/contentfile?path=262-242-1689910824060_8.pdf",
            "page": "8",
            "content": ["2气体排放核算与报告方法》（2012）《建筑物温室气体排放的量化和报告规范及指南（试行）》（深圳市住房和建设局，2013）《Tokyo Cap-and-Trade Program for Large Facilities》(Bureauof Environment，Tokyo Metropolitan Government，2012)三、术语和定义下列术语和定义适用于本指南。（一）温室气体指大气中那些吸收和重新放出红外辐射的自然的和人为的气态成分。《京都议定书》中规定了六种温室气体，分别为二氧化碳（CO2）、甲烷（CH4）、氧化亚氮（N2O）、氢氟碳化物（HFCs）、全氟化碳（PFCs）和六氟化硫（SF6）。（二）核算主体具有温室气体排放行为并应定期核算和报告的法人企业或视同法人的独立核算单位。（三）公共建筑公共建筑包括办公建筑(写字楼、政府部门办公楼等)，商业建筑(商场、金融建筑等)，旅游建筑(旅馆酒店、娱乐场所等)，科教文卫建筑(包括文化、教育、科研、医疗、卫生、体育建筑等)，通信建筑(邮电、通讯、广播用房)以及交通运输用房(机场、车站建筑等)等建筑。（四）化石燃料燃烧排放"]
          }, {
            "name": "262-242-1689910824060_20.pdf",
            "url": "",
            "originalFileName": "Public building operation enterprises_20.pdf",
            "content": ["14hEF ——运营单位（企业）外购的蒸汽和热水的排放因子，单位为吨/吉焦（tCO2/GJ）。由国家统一规定确定，现可采用0.11tCO2/GJ，见附录表3。购入热力的活动水平数据根据热力供应商和公共建筑运营单位（企业）存档的热力流入和流出记录获得，同时相关的计量器具应符合《GB17167用能单位能源计量器具配备和管理通则》要求。六、质量保证和文件存档运营单位（企业）应建立温室气体年度核算和报告的质量保证体系，主要包括以下方面的工作：1）建立运营单位（企业）温室气体排放核算和报告的规章制度，包括负责机构和人员、工作流程和内容、工作周期等；指定专职人员负责温室气体排放核算和报告工作。2）建立温室气体排放源一览表，分别确定合适的核算方法，形成文件并存档；3）建立健全的温室气体排放和能源消耗的台账记录。4）建立健全的企业温室气体排放参数的监测计划。具备测量条件的运营单位（企业），对温室气体排放量影响较大的参数，如化石燃料的低位发热量，应定期监测。原则上每批燃料进企业，都应监测低位发热量。5）建立企业温室气体排放报告内部审核制度。"]
          }, {
            "name": "262-242-1689910824060_24.pdf",
            "url": "",
            "originalFileName": "Public building operation enterprises_24.pdf",
            "content": ["18根据国家发展和改革委员会发布的《中国公共建筑运营单位（企业）温室气体排放核算方法与报告指南（试行）》，本企业核算了年度温室气体排放量，并填写了相关数据表格。现将有关情况报告如下：一、企业基本情况二、温室气体排放三、活动水平数据及来源说明四、排放因子数据及来源说明本报告真实、可靠，如报告中的信息与实际情况不符，本企业将承担相应的法律责任。法人(签字):年月日附表 1报告主体二氧化碳排放量报告附表 2报告主体活动水平数据附表 3报告主体排放因子和计算系数"]
          }, {
            "name": "262-242-1689910824060_4.pdf",
            "url": "",
            "originalFileName": "Public building operation enterprises_4.pdf",
            "content": ["法科学、客观地获取相关活动水平数据。如企业以实测的方式获取核算所需的相关参数，应严格按照本指南提供的标准方法，进行检测并提供检测报告。鉴于企业温室气体核算和报告是一项全新的复杂工作，本指南在实际运用中可能存在不足之处，希望相关使用单位能及时予以反馈，以便今后做出进一步的修改。本指南由国家发展和改革委员会提出并负责解释和修订。"]
          }],
          "follow": [
            "\u516d\u795e\u54c1\u724c\u7684\u5386\u53f2\u6709\u591a\u4e45\u4e86\uff1f",
            " \u516d\u795e\u54c1\u724c\u7684\u4e3b\u8981\u4ea7\u54c1\u6709\u54ea\u4e9b\uff1f",
            " \u516d\u795e\u54c1\u724c\u5728\u56fd\u5185\u5916\u5e02\u573a\u4e0a\u7684\u5730\u4f4d\u5982\u4f55\uff1f"
          ],
          "answers": [{
            "text": "温室气体是指大气中那些吸收和重新放出红外辐射的自然的和人为的气态成分，其中包括二氧化碳、甲烷、氧化亚氮、氢氟碳化物、全氟化碳和六氟化硫等六种气体。",
            "pdf": ["262-242-1689910824060_8.pdf"]
          }],
          "hasNoFiles": false
        }
      ],

      contentList: [],
      waitingAnTimer: null,

      scrollPosition: 0,

      overrides: {
        prompt_template: '',
        exclude_category: '',
        top: 4,
        semantic_ranker: true,
        semantic_captions: true,
        suggest_followup_questions: false
      },
      description: {
        prompt_template: '',
        exclude_category: '',
        top: '',
        semantic_ranker: '',
        semantic_captions: '',
      },
      messages: [],

      single: {
        user: null,
        bot: null,
      },

      singleQA: {
        question: '',
        answer: '',
        response: null,
      },

      processText: 'placeholder',
      isWaiting: false,
      token: '',
      summary: '',
      userIdentity: {},
    };
  },
  mounted() {
    this.historyList = JSON.parse(this.historyListString || '[]');
    this.userIdentity = JSON.parse(localStorage.getItem("query"));
    this.sendData = '';
    this.token = getToken();

    if (document.body.getBoundingClientRect().width - 1 < 992) {
      this.isMobile = true;
    }

    this.delayAn(); //加载动画
    const historyContent = this.$refs.historyContent;
    historyContent.addEventListener('scroll', () => {
      this.scrollPosition = historyContent.scrollTop;
    }); //滚动条监听
  },
  methods: {
    switchLanguage() {
      this.language = this.language === 'Chinese' ? 'English' : 'Chinese';
    },
    clearDialog() {
      this.isError = false;
      this.isSend = false;
      this.isShow = false;
      this.sendData = '';
      this.messages = [];
      this.historyList = [];
      this.total_tokens = 0;
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
          this.waitingText = '请稍候，小助手正在为您查询';
        }
      }, 300);
    },

    //打开侧边栏-查看文件
    openFile(data, url, page, name, index) {
      if (this.isShow && this.activeIndex === index && this.activeName === 'citation' && this.iframeUrl === url) {
        this.isShow = false;
        this.$nextTick(() => {
          this.$refs.historyContent.scrollTop = this.scrollPosition;
        });
        this.activeIndex = -1;
        this.iframeUrl = '';
        return;
      }
      this.activeIndex = index;

      this.pageNum = page;
      let formatUrl = url.replace('https://nygpt.blob.core.windows.net', process.env.VUE_APP_FILE_URL);
      formatUrl = formatUrl.replace('https://nyllm.blob.core.windows.net', process.env.VUE_APP_LLM_BLOB);
      if (name.split('.').reverse()[0] === 'pdf') {
        this.iframeUrl = '/build/pdf/web/viewer.html?file=' + encodeURIComponent(formatUrl) + '#page=' + this.pageNum;
      } else {
        this.iframeUrl = url + '&deptId=' + this.$store.state.user.deptId + '&userId=' + this.$store.state.user.userId + '&token=Bear ' + this.token;
      }
      console.log(this.iframeUrl)
      this.contentList = [];

      //data.contents.forEach(item => {
      //  this.contentList.push({
      //    name: item.name,
      //    content: item.content,
      //  });
      //});

      data.citation.forEach(item => {
        for (let i = 0; i < item.content.length; i++) {
          this.contentList.push({
            name: item.name,
            originalFileName: item.originalFileName,
            content: item.content[i],
          });
        }
      });
      this.activeName = 'citation';
      this.isShow = true;
      this.isNotPdf = false;
      this.$nextTick(() => {
        this.$refs.historyContent.scrollTop = this.scrollPosition;
      });

    },
    //过程
    showProcess(data, index) {
      if (this.isShow && this.activeIndex === index && this.activeName === 'process') {
        this.isShow = false;
        this.$nextTick(() => {
          this.$refs.historyContent.scrollTop = this.scrollPosition;
        });
        this.activeIndex = -1;
        return;
      }
      this.activeIndex = index;
      this.contentList = [];
      data.citation.forEach(item => {
        for (let i = 0; i < item.content.length; i++) {
          this.contentList.push({
            name: item.name,
            originalFileName: item.originalFileName,
            content: item.content[i],
          });
        }
      });
      this.activeName = 'process';
      this.isShow = true;
      this.isNotPdf = true;
      this.$nextTick(() => {
        this.$refs.historyContent.scrollTop = this.scrollPosition;
      });
    },
    //切换侧边栏
    changeIframeShowStatus(data, index) {
      if (this.isShow && this.activeIndex === index && this.activeName === 'content') {
        this.isShow = false;
        this.$nextTick(() => {
          this.$refs.historyContent.scrollTop = this.scrollPosition;
        });
        this.activeIndex = -1;
        return;
      }
      this.activeIndex = index;
      this.contentList = [];
      data.citation.forEach(item => {
        for (let i = 0; i < item.content.length; i++) {
          this.contentList.push({
            name: item.name,
            originalFileName: item.originalFileName,
            content: item.content[i],
          });
        }
      });
      this.activeName = 'content';
      this.isShow = true;
      this.isNotPdf = true;
      this.$nextTick(() => {
        this.$refs.historyContent.scrollTop = this.scrollPosition;
      });
    },
    //点击确定
    handleSendData() {
      if (this.isSend) {
        return;
      }
      if (this.sendData === '') {
        return;
      }
      this.isSend = true;
      this.historyList.push({
        text: this.sendData,
        role: 'user',
      });

      // 创建单个对话
      this.single = {
        user: this.sendData,
        bot: undefined
      }

      this.singleQA.question = this.sendData + '';

      let t = setTimeout(() => {
        this.historyList.push({
          role: 'gpt',
          text: '',
          citation: [],
          follow: [],
        });

        this.sendRequest();
        clearTimeout(t);
      }, 200);
      this.sendData = '';
    },
    //发送请求，处理返回数据
    sendRequest() {
      let his = JSON.parse(JSON.stringify(this.messages));
      his.push({user: this.single.user, bot: undefined});

      let data = {
        history: his,
        approach: 'rrr',
        overrides: this.overrides,
        language: this.language,// en or cn

        identityId: this.userIdentity.identityId,
        businessId: this.userIdentity.businessId,

        deptId: this.$store.state.user.deptId,
        userId: this.$store.state.user.userId,
        trainType: 'Embedding',
      };

      let index = this.historyList.length - 1;

      this.$nextTick(() => {
        this.$refs.historyContent.scrollTop = this.$refs.historyContent.scrollHeight;
      });

      axios.post(this.baseUrl + '/chat', data, {
        headers: {'Authorization': 'Bearer ' + getToken()},
        timeout: 3 * 60 * 1000
      }) //post请求，传递参数text
        .then(response => {
          if (response.data.error) {
            console.log(response.data.error);
            this.historyList[index].role = 'gpt';
            this.historyList[index].isError = true;
            this.historyList[index].errorText = response.data.error;
            this.singleQA.answer = response.data.error + '';
            this.singleQA.response = JSON.stringify({});
            this.handleAddHistory();
            this.isSend = false;
            return;
          }
          this.historyList[index].role = 'gpt';

          const text = response.data.answer;
          this.singleQA.answer = unescape(text.replace(/\\u/g, '%u'));
          this.singleQA.response = JSON.stringify(response.data);
          const regex = /\[([^\]]+.(pdf|xlsx))\]/g;
          const matches = text.match(regex);
          if (matches != null) {
            const result = [];
            let start = 0;
            matches.forEach(item => {
              const index = text.indexOf(item, start);
              const before = text.substring(start, index);
              const content = item.substr(1, item.length - 2);
              const pdf = content.split(",");
              result.push({text: before, pdf: [content]});
              start = index + item.length;
            })
            if (start < text.length) {
              const last = text.substr(start);
              result.push({text: last, pdf: []});
            }
            this.historyList[index].answers = result;

          } else {
            this.historyList[index].answers = [{
              text: text,
              pdf: [],
            }];
          }

          this.historyList[index].text = response.data.answer;
          let originalFileNames = response.data.originalFileNames;
          let blobUrl = response.data.blobUrl;

          let tmp = response.data.data_points;
          let citation = [];
          tmp.forEach((item, index) => {
            let list = item.split(': ');
            if (matches != null && matches.indexOf('[' + list[0] + ']') != -1) {
              let data = {
                name: list[0],
                originalFileName: originalFileNames[index],
                originalFileUrl: blobUrl[index],
                url: this.baseUrl + '/contentfile?path=' + list[0],
                page: list[0].split('.')[0].split('_')[1],
                content: [list.slice(1).join(": ")],
              };
              citation.push(data);
            } else {
              let data = {
                name: list[0],
                url: '',
                originalFileName: originalFileNames[index],
                content: [list.slice(1).join(": ")],
              };
              citation.push(data);
            }
          });
          let n = 0;
          citation.forEach(item => {
            if (item.url === '') {
              n += 1;
            }
          });
          if (n === citation.length) {
            this.historyList[index].hasNoFiles = true;
          } else {
            this.historyList[index].hasNoFiles = false;
          }
          this.historyList[index].citation = citation;
          this.historyList[index].follow = response.data.hiddenQuesion || [];

          his[his.length - 1].bot = response.data.answer;
          this.messages = JSON.parse(JSON.stringify(his));
          this.handleAddHistory();
          this.isSend = false;
        })
        .catch(error => {
          console.log(error.response);
          this.historyList[index].role = 'gpt';
          this.historyList[index].isError = true;
          this.historyList[index].errorText = error.response.data.error || error;
          this.isSend = false;
          this.singleQA.answer = (error.response.data.error || error) + '';
          this.singleQA.response = JSON.stringify({});
          this.handleAddHistory();
        });
    },
    handleAddHistory() {
      addHistory({...this.singleQA, type: 'Embedding'})
        .then(res => {
          if (res.code !== 200) {
            console.log(res.msg);
            this.$message.error(res.msg);
          }
        }).catch(err => {
        console.log(err);
        this.$message.error(err);
      })
    },
    keydownfn(event) {
      if (event.keyCode == 13) {
        if (!event.shiftKey && !event.ctrlKey && !event.altKey && !event.metaKey) {
          event.preventDefault();
          this.handleSendData();
        } else if (!event.shiftKey) {
          this.sendData = this.sendData + '\n'
        }
      }
    },
    retrySend(item, index) {
      this.sendData = this.historyList[index - 1].text;
      this.historyList.splice(index - 1, 2);
      this.isSend = false;
      this.handleSendData();
    },
    handleClickFollow(text) {
      this.sendData = text;
      if (text && text !== '') {
        this.handleSendData();
      }
    },
    handleCompleted(item) {
      this.showResult = true;
    },
    handleJump(item) {
      this.$message.info('点击：转人工');
      this.fullscreenLoading = true;

      let his = JSON.parse(JSON.stringify(this.messages));
      his.push({user: this.single.user, bot: undefined});
      let data = {
        history: his,

        deptId: this.$store.state.user.deptId,
        userId: this.$store.state.user.userId,
      };
      axios.post(this.baseUrl + '/chat_summary', data, {
        headers: {'Authorization': 'Bearer ' + getToken()},
        timeout: 3 * 60 * 1000
      }).then(response => {
        let res = response.data;
        if(res.code===200){
          this.summary = res.data.question;
          console.log(this.summary);
          this.jumpToChat();
        } else {
          console.log(res.msg);
          this.$message.error(res.msg);
          this.fullscreenLoading = false;
        }
      }).catch(err => {
        console.log(err);
        this.$message.error(err);
        this.fullscreenLoading = false;
      });
      setTimeout(() => {
        this.fullscreenLoading = false;
      }, 2000);
    },
    jumpToChat(){
      let userIdentity = JSON.parse(localStorage.getItem('query'));
      let data = {
        identityId: userIdentity.identityId,
        businessName: userIdentity.businessId.join(','),
        summary: this.summary,
        history: JSON.stringify(this.historyList)
      };

      addChatgptsummary(data).then(res => {
        if(res.code===200){
          this.fullscreenLoading = false;
          window.location.replace('https://www.sobot.com/chat/pc/v2/index.html?sysnum=399729eec9eb4635947b92bb5bb1cec9&type=2&partnerid='+userIdentity.identityId);
        }
      })
    },
    handleEnd(isEnd){
      if(isEnd){
        this.showResult = false;

      }
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
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  background-color: rgba(255, 255, 255, 0.5);
  z-index: 1000;
}

.chat-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
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

.process-card {
  width: 96%;
  min-height: 40px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
  background-color: #ffffff80;
  border: 1px solid #ffffff;
  white-space: pre-wrap;
}

.card {
  width: max-content;
  max-width: 70%;
  min-width: 60px;
  min-height: 40px;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
  background-color: #ffffffc0;
  border: 1px solid #ffffff;
  transition: box-shadow 0.3s ease-in-out, background-color 0.3s ease-in-out;
}

.card:hover {
  background-color: #ffffffff;
  box-shadow: 0 4px 8px #00000035, 0 0 4px #00000030;
}

.card-text {
  width: 100%;
  font-size: 16px;
  padding-top: 6px;
  white-space: pre-wrap;
}

.file-tag-container {
  margin-top: 10px;
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

.button-bar {
  width: 98%;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  margin: 10px;
}

.button-box {
  cursor: pointer;
  display: flex;
  align-items: center;
  margin: 0 6px;
  padding: 10px 16px;
  border-radius: 10px;
  font-size: 14px;
  color: #606266;
  background-color: #d0d3f380;
  border: 1px solid #d0d3f3;
}

.input-bar {
  width: 98%;
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  padding: 4px 0px;
  min-height: 60px;
  margin-bottom: 10px;
}

.clear-button-clear {
  margin-right: 12px;
  color: #ff6e74;
  cursor: pointer;
  transition: text-shadow 0.3s, color 0.3s;
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

.clear-button {
  margin-right: 12px;
  color: #909399;
  cursor: pointer;
  transition: text-shadow 0.3s, color 0.3s;
  background-color: #FFFFFF80;
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #FFF;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04), 0 0 2px rgba(0, 0, 0, 0.02);
}

.clear-button:hover {
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  color: #606266;
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
  z-index: 2000;
  transition: transform 3s ease-in-out, text-shadow 3s;
  opacity: 0.1;
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
  font-size: 18px;
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
  height: calc(100% - 12px);
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;

  flex: auto;
  display: flex;
  flex-direction: column;
}

.index-span {
  height: fit-content;
  font-size: 12px;
  border: 1px solid rgba(198, 209, 244, 0.91);
  padding: 4px 1px;
  margin: 2px 2px;
  border-radius: 4px;
  background-color: rgb(213, 216, 244);
  color: rgb(10, 103, 159);
  cursor: pointer;
}

::v-deep .el-tabs__content {
  flex-grow: 1;
  overflow-y: scroll;
}

.tabs ::v-deep .el-textarea__inner {
  border: 1px solid #ffffff;
  border-radius: 8px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
  font-size: 14px;
  background-color: #ffffff80;
}

::v-deep .v-note-wrapper .v-note-panel .v-note-show .v-show-content,
.v-note-wrapper .v-note-panel .v-note-show .v-show-content-html {
  padding: 0px;
}

::v-deep .markdown-body p {
  margin-bottom: 0px;
}

::v-deep .type-select .el-input--medium .el-input__inner {
  height: 40px;
  border: none;
  width: 80px;
  padding: 0;
  text-align: center;
  color: #888888;
}

::v-deep .el-select .el-input .el-select__caret {
  display: none;
}

.logo-center {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}

.drawer-setting::v-deep .el-drawer {
  width: 40%;
  height: 70%;
  margin-top: 15vh;
  margin-right: 4%;
  background-color: #ffffffc0;
  border: 1px solid #FFF;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
  padding: 12px;
}

.follow-card {
  font-size: 14px;
  color: #606266;
  width: max-content;
  max-width: 70%;
  min-width: 60px;
  min-height: 10px;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 24px;
  background-color: #ffffff80;
  border: 1px solid #ffffff;
  transition: box-shadow 0.15s ease-in-out, background-color 0.15s ease-in-out;
  margin: 8px 0 8px 0;

  ::v-deep .el-card__body {
    padding: 8px 16px 8px 16px;
  }
}

.follow-card:hover {
  cursor: pointer;
  background-color: #ffffff40;
  box-shadow: 0 3px 6px #00000024, 0 0 3px #0000001f;
}

.follow-bar {
  display: flex;
  align-items: center;
  justify-content: right;
  margin-top: 4px;
  max-width: 70%;
  font-size: 18px;
  font-weight: 600;
  font-family: "微软雅黑 Light", "Microsoft YaHei", "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", Arial, sans-serif;
}
::v-deep .el-tag--small{
  height:fit-content;
  white-space: pre-wrap;
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
</style>
