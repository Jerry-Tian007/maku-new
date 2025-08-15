<template>
  <div class="back" align="left">
    <div
      class="color-background"
      style="position: absolute;width:100%;height:100%;"
    ></div>
    <div class="backgrond">
      <div
        class="button-background"
        style="height:30px;background:#00000000;display: flex;flex-direction: column;align-items: flex-end;padding-top:12px;"
      >
        <div style="display: flex;height:30px;align-items: center">
          <div
            class="clear-button"
            @click="switchLanguage()"
          >
            <i
              class="el-icon-connection"
              style="font-size: 18px;margin-right:4px;"
            />
            <span style="font-size: 16px;">搜索引擎: {{ language === 'Chinese' ? '中文' : '英文' }}</span>
          </div>
          <div
            class="clear-button-clear"
            @click="clearDialog()"
          >
            <i
              class="el-icon-delete"
              style="font-size: 18px;margin-right:4px;"
            />
            <span style="font-size: 16px;">清除对话</span>
          </div>
          <!--          <div
                         class="clear-button"
                         @click="drawerVal = true"
                     >
                       <i
                           class="el-icon-setting"
                           style="font-size: 16px;margin-right:4px;"
                       />
                       <span style="font-size: 20px;">参数设置</span>
                     </div>-->
        </div>
      </div>
      <div
        class="chat-background"
        style=""
      >
        <div :style="isMobile ? '' : 'flex:0.25;'" v-show="!isShow || isMobile"></div>
        <!--flex布局-聊天-->
        <div class="dialog-container">
          <div v-show="historyList.length<1">
            <svg
              :class="uploading ? 'loading' : '' + ' empty-logo'"
              width="128"
              height="128"
              viewBox="0 0 41 41"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M37.5324 16.8707C37.9808 15.5241 38.1363 14.0974 37.9886 12.6859C37.8409 11.2744 37.3934 9.91076 36.676 8.68622C35.6126 6.83404 33.9882 5.3676 32.0373 4.4985C30.0864 3.62941 27.9098 3.40259 25.8215 3.85078C24.8796 2.7893 23.7219 1.94125 22.4257 1.36341C21.1295 0.785575 19.7249 0.491269 18.3058 0.500197C16.1708 0.495044 14.0893 1.16803 12.3614 2.42214C10.6335 3.67624 9.34853 5.44666 8.6917 7.47815C7.30085 7.76286 5.98686 8.3414 4.8377 9.17505C3.68854 10.0087 2.73073 11.0782 2.02839 12.312C0.956464 14.1591 0.498905 16.2988 0.721698 18.4228C0.944492 20.5467 1.83612 22.5449 3.268 24.1293C2.81966 25.4759 2.66413 26.9026 2.81182 28.3141C2.95951 29.7256 3.40701 31.0892 4.12437 32.3138C5.18791 34.1659 6.8123 35.6322 8.76321 36.5013C10.7141 37.3704 12.8907 37.5973 14.9789 37.1492C15.9208 38.2107 17.0786 39.0587 18.3747 39.6366C19.6709 40.2144 21.0755 40.5087 22.4946 40.4998C24.6307 40.5054 26.7133 39.8321 28.4418 38.5772C30.1704 37.3223 31.4556 35.5506 32.1119 33.5179C33.5027 33.2332 34.8167 32.6547 35.9659 31.821C37.115 30.9874 38.0728 29.9178 38.7752 28.684C39.8458 26.8371 40.3023 24.6979 40.0789 22.5748C39.8556 20.4517 38.9639 18.4544 37.5324 16.8707ZM22.4978 37.8849C20.7443 37.8874 19.0459 37.2733 17.6994 36.1501C17.7601 36.117 17.8666 36.0586 17.936 36.0161L25.9004 31.4156C26.1003 31.3019 26.2663 31.137 26.3813 30.9378C26.4964 30.7386 26.5563 30.5124 26.5549 30.2825V19.0542L29.9213 20.998C29.9389 21.0068 29.9541 21.0198 29.9656 21.0359C29.977 21.052 29.9842 21.0707 29.9867 21.0902V30.3889C29.9842 32.375 29.1946 34.2791 27.7909 35.6841C26.3872 37.0892 24.4838 37.8806 22.4978 37.8849ZM6.39227 31.0064C5.51397 29.4888 5.19742 27.7107 5.49804 25.9832C5.55718 26.0187 5.66048 26.0818 5.73461 26.1244L13.699 30.7248C13.8975 30.8408 14.1233 30.902 14.3532 30.902C14.583 30.902 14.8088 30.8408 15.0073 30.7248L24.731 25.1103V28.9979C24.7321 29.0177 24.7283 29.0376 24.7199 29.0556C24.7115 29.0736 24.6988 29.0893 24.6829 29.1012L16.6317 33.7497C14.9096 34.7416 12.8643 35.0097 10.9447 34.4954C9.02506 33.9811 7.38785 32.7263 6.39227 31.0064ZM4.29707 13.6194C5.17156 12.0998 6.55279 10.9364 8.19885 10.3327C8.19885 10.4013 8.19491 10.5228 8.19491 10.6071V19.808C8.19351 20.0378 8.25334 20.2638 8.36823 20.4629C8.48312 20.6619 8.64893 20.8267 8.84863 20.9404L18.5723 26.5542L15.206 28.4979C15.1894 28.5089 15.1703 28.5155 15.1505 28.5173C15.1307 28.5191 15.1107 28.516 15.0924 28.5082L7.04046 23.8557C5.32135 22.8601 4.06716 21.2235 3.55289 19.3046C3.03862 17.3858 3.30624 15.3413 4.29707 13.6194ZM31.955 20.0556L22.2312 14.4411L25.5976 12.4981C25.6142 12.4872 25.6333 12.4805 25.6531 12.4787C25.6729 12.4769 25.6928 12.4801 25.7111 12.4879L33.7631 17.1364C34.9967 17.849 36.0017 18.8982 36.6606 20.1613C37.3194 21.4244 37.6047 22.849 37.4832 24.2684C37.3617 25.6878 36.8382 27.0432 35.9743 28.1759C35.1103 29.3086 33.9415 30.1717 32.6047 30.6641C32.6047 30.5947 32.6047 30.4733 32.6047 30.3889V21.188C32.6066 20.9586 32.5474 20.7328 32.4332 20.5338C32.319 20.3348 32.154 20.1698 31.955 20.0556ZM35.3055 15.0128C35.2464 14.9765 35.1431 14.9142 35.069 14.8717L27.1045 10.2712C26.906 10.1554 26.6803 10.0943 26.4504 10.0943C26.2206 10.0943 25.9948 10.1554 25.7963 10.2712L16.0726 15.8858V11.9982C16.0715 11.9783 16.0753 11.9585 16.0837 11.9405C16.0921 11.9225 16.1048 11.9068 16.1207 11.8949L24.1719 7.25025C25.4053 6.53903 26.8158 6.19376 28.2383 6.25482C29.6608 6.31589 31.0364 6.78077 32.2044 7.59508C33.3723 8.40939 34.2842 9.53945 34.8334 10.8531C35.3826 12.1667 35.5464 13.6095 35.3055 15.0128ZM14.2424 21.9419L10.8752 19.9981C10.8576 19.9893 10.8423 19.9763 10.8309 19.9602C10.8195 19.9441 10.8122 19.9254 10.8098 19.9058V10.6071C10.8107 9.18295 11.2173 7.78848 11.9819 6.58696C12.7466 5.38544 13.8377 4.42659 15.1275 3.82264C16.4173 3.21869 17.8524 2.99464 19.2649 3.1767C20.6775 3.35876 22.0089 3.93941 23.1034 4.85067C23.0427 4.88379 22.937 4.94215 22.8668 4.98473L14.9024 9.58517C14.7025 9.69878 14.5366 9.86356 14.4215 10.0626C14.3065 10.2616 14.2466 10.4877 14.2479 10.7175L14.2424 21.9419ZM16.071 17.9991L20.4018 15.4978L24.7325 17.9975V22.9985L20.4018 25.4983L16.071 22.9985V17.9991Z"
                fill="#FFFFFFC0"
              ></path>
            </svg>
            <div v-show="uploading" class="loading-text">
              <div style="margin-bottom: 8px;">{{ uploadText }}</div>
              <el-progress style="width:40%;text-shadow: #FFF 0 0 2px;" :color="customColors" :percentage="uploadState"
                           :format="formatText"></el-progress>
            </div>
          </div>
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
                :style="'background-color: #FFFFFF80;'+ (activeIndex==index ? 'border: 2px solid #2D9CDB;' : '')+ (item.role=='gpt' ? 'min-width:300px;' : '')"
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
                  <!--<div class="question-tag-container">
                    <span style="font-weight:600;font-size:14px;">后续问题：</span>
                    <el-tag
                        v-for="(follow,index) in item.follow"
                        :key="index"
                        @click="setFollowQuestion(follow)"
                        size="small"
                        style="margin: 0px 5px;margin-top:6px;cursor: pointer;"
                        type="info"
                    >
                  <span
                      style="font-style: italic;color: #606266;">{{ follow }}</span>
                    </el-tag>
                  </div>-->
                </div>
              </el-card>
              <div>
                <el-card
                  v-for="(follow, index) in item.follow"
                  :key="index"
                  class="follow-card"
                >
                  <div style="display: flex;align-items: center;" @click="handleClickFollow(follow)" :title="follow">
                    <div style="white-space: nowrap;
                    overflow:hidden;
                    text-overflow:ellipsis;">{{ follow }}</div>
                    <div style="margin-left: 8px;">
                      <i class="el-icon-right" style="font-size: 16px;"/>
                    </div>
                  </div>
                </el-card>
              </div>
            </div>
          </div>

          <!--按钮条 迁移至此，后期继续-->
          <div class="button-bar" v-if="false">
            <div
              v-for="(item,index) in buttonBar"
              :key="index"
              class="button-box"
              @click="item.click()"
            >
              <i :class="item.icon"/>
              <span style="margin-left: 4px;">{{ item.label }}</span>
            </div>
          </div>
          <!--输入栏-->
          <div
            class="input-bar"
            align="center"
          >
            <!--输入框-->
            <div style="flex:1;">
              <el-input
                resize="none"
                v-model="sendData"
                type="textarea"
                class="input-area"
                :autosize="{ minRows: 1, maxRows: 4}"
                @keydown.native="keydownfn"
                placeholder="请输入对话内容..."
                v-clear-emoij
              >
              </el-input>
            </div>
            <!--发送按钮-->
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
                @click="sendData=='' ? '' : handleSendData()"
              >
                <path
                  d="M48.1857 2.01674C47.8845 1.76668 47.5185 1.60736 47.1302 1.55736C46.742 1.50735 46.3475 1.56871 45.9928 1.73429L3.42639 21.7141V25.5802L21.3051 32.7316L32.7732 50.375H36.6401L48.858 4.12345C48.9571 3.74467 48.9465 3.34555 48.8275 2.97257C48.7084 2.59958 48.4859 2.2681 48.1857 2.01674ZM34.2526 46.6876L24.3953 31.522L39.564 14.9082L37.1639 12.7169L21.8765 29.4601L7.13545 23.5637L45.0633 5.76064L34.2526 46.6876Z"
                  :fill="sendData=='' ? '#aaaaaa' : '#2D9CDB'"
                />
              </svg>
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
        style="margin-top:30px;"
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
          >0
            <div
              slot="header"
              style="font-size:18px;font-weight: 600;"
            >{{ item.originalFileName }}
            </div>
            <div class="card-text">
              {{ item }}
            </div>
          </el-card>
        </div>

        <!--pdf浏览-->
        <iframe
          v-show="activeName=='citation'"
          :src="iframeUrl"
          frameborder="0"
          width="100%"
          height="100%"
          style="flex: 1;"
        ></iframe>
      </div>
    </el-drawer>
    <!-- 内容展示抽屉 -->

  </div>
</template>

<script>
import axios from "axios";
import {getToken} from '@/utils/auth'
import {addHistory} from "../../api/data/history";

export default {
  name: 'DataChat',
  data() {
    return {
      isMobile: false,

      activeIndex: -1,
      filesLimit: 5,
      uptimecount: 0,
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 上传的地址
        url: process.env.VUE_APP_GPT_URL + `/datasetupload/`,
      },

      // language: 'Chinese' || 'English',
      language: 'English' || 'Chinese',

      baseUrl: process.env.VUE_APP_GPT_URL,
      //baseUrl: 'http://192.168.50.103:5000',
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

      uploading: false,
      uploadProcess: '0',

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
      historyList: [/*
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
        }*/
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

      fileList: [],
      maxLength: 0,
      uploadfilelist: [],
      fileListlength: 0,
      uploadText: '正在上传...',
      uploadState: 0,

      singleQA: {
        question: '',
        answer: '',
        response: null,
      },

      customColors: [
        {color: '#f56c6c', percentage: 20},
        {color: '#e6a23c', percentage: 40},
        {color: '#5cb87a', percentage: 60},
        {color: '#1989fa', percentage: 80},
        {color: '#6f7ad3', percentage: 100}
      ],

      processText: 'placeholder',
      isWaiting: false,
      token: '',

      // 按钮条迁移，后期继续
      buttonBar: [
        {
          label: '语言',
          icon: 'el-icon-edit-outline',
          click: () => {
            this.switchLanguage();
          }
        },
        {
          label: '清除对话',
          icon: 'el-icon-delete',
          click: () => {
            this.clearDialog();
          }
        }
      ]
    };
  },
  mounted() {
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
    clearTable() {
      this.$confirm('此操作将清空存储库, 是否继续?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.isWaiting = true;
        axios.post(this.baseUrl + '/redisclear', '', {headers: {'Authorization': 'Bearer ' + getToken()}})
          .then(response => {
            if (response.data.status == 'success') {

              this.$alert('清除成功', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  let t = setTimeout(() => {
                    this.isWaiting = false;
                    clearTimeout(t);
                  }, 3 * 1000);
                }
              });
            } else {
              console.log(response.data.msg)
              let t = setTimeout(() => {
                this.isWaiting = false;
                clearTimeout(t);
              }, 10 * 1000);
            }
          }).catch(error => {
          this.$message.error(error);
          let t = setTimeout(() => {
            this.isWaiting = false;
            clearTimeout(t);
          }, 10 * 1000);
        });
      }).catch(() => {
      });
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
    //点击预设follow-up问题
    setFollowQuestion(question) {
      this.sendData = question;
    },
    //打开上传对话框
    handleImport() {
      this.upload.open = true;
    },
    submitFileForm() {
      this.$refs.uploadFileButton.submit();
    },

    handleChange(file, fileList) {
      this.uploadfilelist = [];
      for (let x of fileList) {
        this.uploadfilelist.push(x.raw)
      }
      //   this.fileList = fileList;
      this.fileListlength = fileList.length;
      console.log(this.fileListlength)
      //防抖
      let length = fileList.length
      this.maxLength = Math.max(length, this.maxLength)
      setTimeout(() => {
        console.log(this.maxLength, length)
        if (length === this.maxLength) {
          //上传文件，触发
          this.$refs.uploadFileButton.submit();
        }
      }, 100)
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
        .then(response => { //请求成功
          /*
            answer: string;
            thoughts: string | null;
            data_points: string[];
            error?: string;
          */
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
    uploadFile(param) {
      //判断文件名是否只有A-Z,a-z,0-9,_, ,-
      let reg = /^[\u0020A-Za-z0-9_-]+$/;
      if (!reg.test(param.file.name.split('.')[0])) {
        this.$message.error('文件名只能包含字母、数字、下划线、中划线');
        this.$refs.uploadFileButton.clearFiles();
        this.maxLength = 0;
        return;
      }

      if (param.file.size > 1024 * 1024 * 40) {
        this.$message.error('文件大小不能超过40M');
        this.$refs.uploadFile.clearFiles();
        this.maxLength = 0;
        return;
      }

      this.uptimecount = this.uptimecount + 1;
      if (this.uptimecount == this.fileListlength) {
        const formData = new FormData()
        //    formData.append('file', param.file)
        //   formData.append('name', param.file.name);
        for (let i = 0; i < this.fileListlength; i++) {
          formData.append('file', this.uploadfilelist[i])
          formData.append('name', this.uploadfilelist[i].name);
        }
        this.uploading = true;
        this.uploadState = 0;
        let that = this;
        fetch(this.baseUrl + '/datasetupload', {method: 'post', body: formData})
          .then(response => {
            const reader = response.body.getReader();
            const decoder = new TextDecoder('utf-8');
            const stream = new ReadableStream({
              start(controller) {
                function push() {
                  reader.read().then(({done, value}) => {
                    if (done) {
                      controller.close();
                      return;
                    }
                    controller.enqueue(decoder.decode(value));
                    push();
                  });
                }

                push();
              }
            });
            const reader2 = stream.getReader();

            function read() {
              reader2.read().then(({done, value}) => {
                if (done) {
                  return;
                }
                if (value.startsWith('Proxy error')) {
                  console.log('error clear')
                  that.fileList = [];
                  that.uploadfilelist = [];
                  that.$refs.uploadFileButton.clearFiles();
                  that.uptimecount = 0;
                  that.fileListlength = 0;
                  that.maxLength = 0;
                  that.uploading = false;
                  return;
                }
                let valueString = value.split(';').reverse()[1];
                let num = 0;
                switch (valueString) {
                  case 'one':
                    num = 1;
                    break;
                  case 'two':
                    num = 2;
                    break;
                  case 'three':
                    num = 3;
                    break;
                  case 'four':
                    num = 4;
                    break;
                  case 'five':
                    num = 5;
                    break;
                }
                console.log(value, num)
                that.uploadState = Number(num) * 100 / 5;
                if (valueString == 'five') {
                  that.$alert('上传成功', '提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                      console.log('five clear')
                      that.fileList = [];
                      that.uploadfilelist = [];
                      that.$refs.uploadFileButton.clearFiles();
                      that.uptimecount = 0;
                      that.fileListlength = 0;
                      that.maxLength = 0;
                      that.uploading = false;
                    }
                  });
                }
                read();
              });
            }

            read();
          })
          .catch(error => {
            console.error('请求出错', error);
            this.fileList = [];
            that.uploadfilelist = [];
            this.$refs.uploadFileButton.clearFiles();
            that.uptimecount = 0;
            that.fileListlength = 0;
            that.maxLength = 0;
            this.uploading = false;
          });
      }
    },
    handleFileSuccess() {
      this.$message({
        message: '上传成功',
        type: 'success'
      });
      this.isSend = false;
    },
    handleFileFail() {
      this.$message.error('上传失败');
      this.isSend = false;
    },
    retrySend(item, index) {
      this.sendData = this.historyList[index - 1].text;
      this.historyList.splice(index - 1, 2);
      this.isSend = false;
      this.handleSendData();
    },

    formatText(percentage) {
      switch (percentage) {
        case 0:
          this.uploadText = '正在进行预处理';
          return '等待中';
        case 20:
          this.uploadText = '正在上传至blob';
          return `${percentage}%`;
        case 40:
          this.uploadText = '正在更新索引';
          return `${percentage}%`;
        case 60:
          this.uploadText = '正在更新技能集';
          return `${percentage}%`;
        case 80:
          this.uploadText = '正在更新索引器';
          return `${percentage}%`;
        case 100:
          this.uploadText = '处理完成';
          return `${percentage}%`;
        default:
          return `${percentage}%`;
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 ${this.filesLimit} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      this.fileList = [];
      this.maxLength = 0;
    },
    handleClickFollow(text){
      this.sendData = text;
      if (text && text !== ''){
        this.handleSendData();
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
  background-color: #ffffff40;
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
  background-color: #ffffff40;
  border: 1px solid #ffffff;
  transition: box-shadow 0.15s ease-in-out, background-color 0.15s ease-in-out;
  margin: 8px 0 8px 0;

  ::v-deep .el-card__body{
    padding: 6px 12px 6px 16px;
  }
}

.follow-card:hover{
  cursor: pointer;
  background-color: #ffffffc0;
  box-shadow: 0 3px 6px #00000024, 0 0 3px #0000001f;
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
