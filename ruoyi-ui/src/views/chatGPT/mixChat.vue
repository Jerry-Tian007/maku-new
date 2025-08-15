<template>
  <div class="back">
    <div
      class="color-background"
      style="position: absolute;width:100%;height:100%;"
    ></div>
    <div class="backgrond">
      <div
        class="button-background"
        :style="isMobile ? 'background:#00000000;width:100%;height:50px;display: flex;align-items: center;justify-content: space-between' : 'height:0px;'"
      >
        <div
          v-if="isMobile"
          class="clear-button-setting"
          @click="sessionVal = true"
          style="margin-left:10px;"
        >
          <i
            class="el-icon-chat-line-square"
            style="font-size: 18px;margin-right:4px;"
          /><span style="font-size: 16px;">会话</span>
        </div>
        <div style="display:flex;align-items: center;">
          <div
            class="clear-button-clear"
            @click="clearDialog()"
            align="center"
            v-if="isMobile"
          >
            <i
              class="el-icon-delete"
              style="font-size: 18px;margin-right:4px;"
            /><span style="font-size: 18px;">清空</span>
          </div>
          <div
            v-if="isMobile"
            class="clear-button-setting"
            @click="drawerVal = true"
          >
            <i
              class="el-icon-setting"
              style="font-size: 18px;margin-right:4px;"
            /><span style="font-size: 18px;">设置</span>
          </div>
        </div>
      </div>
      <div
        class="chat-background"
        style=""
      >
        <div :style="isMobile ? 'flex:0.02;' : 'width:240px;display:flex;flex-direction: column;'" align="center"
             :class="isMobile?'':'session-list'">
          <template v-if="!isMobile">
            <div
              class="session-card"
              style="margin-bottom:20px;"
            >
              <div @click="handleAddSession()" style="width: 100%;" align="center"><i class="el-icon-plus"></i></div>
            </div>
            <div style="overflow-y:auto;" v-infinite-scroll="load">
              <div
                class="session-card"
                v-for="(item,index) in sessionList"
                :key="index"
                :style="item.id === sessionId ?  'border-color:#1c84c6;border-width:2px;' : ''"
              >
              <span
                @click="handleChangeSession(item,index)"
                style="width:100%;
                    white-space:nowrap;
                    overflow:hidden;
                    text-overflow:ellipsis;
                    text-align: left;"
              >{{ getName(item) || '新会话' }}</span>
                <div class="session-delete" @click="handleDeleteSession(item,index)"><i class="el-icon-delete"></i>
                </div>
              </div>
              <div
                v-if="isLoadingSession"
                class="session-card"
              >
                <div style="width: 100%;" align="center"><i class="el-icon-loading"></i></div>
              </div>
            </div>
          </template>
        </div>
        <!--flex布局-聊天-->
        <div class="dialog-container"
             :style="isMobile ? 'padding-top:50px;' : 'padding-top:10px;'">
          <div v-if="historyList.length<1">
            <svg
              class="empty-logo"
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
          </div>
          <div
            class="dialog-content"
            id="chatBox"
            ref="historyContent"
          >
            <!--对话列表-->
            <div
              v-for="(item,index) in historyList"
              :key="index"
              :style="'padding:12px;' + (isMobile ? 'margin-top:30px;' : '')"
              :align="item.role=='user' ? 'right' : 'left'"
            >
              <el-card
                class="card"
                align="left"
                :style="(item.role=='user' ? 'background-color: #ffddc2;' : '') + (isMobile ? 'max-width:90%;' : '')"
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
                  <div
                    style="display: flex;"
                    v-if="item.timestamp!=null"
                  >
                    <el-tooltip effect="light" content="复制" placement="top">
                      <div @click="handleDbClickCard(item)" style="cursor: pointer;margin-right:14px;margin-top:2px">
                        <i class="el-icon-copy-document" style="font-weight: 600;font-size: 16px"/>
                      </div>
                    </el-tooltip>
                    <div
                      @click="handleGood(true,item)"
                      style="cursor: pointer;margin-right:10px; "
                    >
                      <svg
                        v-if="!item.isChecked"
                        width="18"
                        height="18"
                        viewBox="0 0 48 48"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                      >
                        <path
                          d="M35.911 41.544L41.281 22.544C41.3651 22.2465 41.3791 21.9336 41.3219 21.6299C41.2646 21.3261 41.1378 21.0397 40.9513 20.7932C40.7648 20.5467 40.5236 20.3468 40.2468 20.2092C39.97 20.0715 39.6651 19.9999 39.356 20H27.875C27.7104 20 27.548 19.9629 27.3997 19.8914C27.2515 19.8199 27.1213 19.7159 27.0188 19.5871C26.9164 19.4583 26.8443 19.308 26.808 19.1475C26.7716 18.987 26.772 18.8204 26.809 18.66L27.309 16.496C27.767 14.511 27.914 12.466 27.745 10.436L27.653 9.333C27.5544 8.14969 27.0396 7.03962 26.2 6.2C25.432 5.43181 24.3903 5.00016 23.304 5H23.064C22.407 5 21.802 5.356 21.484 5.93L18.825 10.715C17.633 12.861 15.8561 14.6244 13.701 15.8L7.042 19.43C6.7263 19.6023 6.46289 19.8565 6.27951 20.1659C6.09614 20.4752 5.99958 20.8284 6 21.188V41C6 41.5304 6.21072 42.0391 6.58579 42.4142C6.96086 42.7893 7.46957 43 8 43H33.987C34.4225 42.9999 34.846 42.8576 35.1933 42.5948C35.5406 42.332 35.7925 41.9631 35.911 41.544Z"
                          stroke="black"
                          stroke-width="4"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                        />
                      </svg>

                      <svg
                        v-if="item.isChecked && item.isGood"
                        width="18"
                        height="18"
                        viewBox="0 0 48 48"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                      >
                        <mask
                          id="mask0_142_11"
                          style="mask-type:luminance"
                          maskUnits="userSpaceOnUse"
                          x="4"
                          y="3"
                          width="40"
                          height="42"
                        >
                          <path
                            d="M35.911 41.544L41.281 22.544C41.3651 22.2465 41.3791 21.9336 41.3219 21.6299C41.2646 21.3261 41.1378 21.0397 40.9513 20.7932C40.7648 20.5467 40.5236 20.3468 40.2468 20.2092C39.97 20.0715 39.6651 19.9999 39.356 20H27.875C27.7104 20 27.548 19.9629 27.3997 19.8914C27.2515 19.8199 27.1213 19.7159 27.0188 19.5871C26.9164 19.4583 26.8443 19.308 26.808 19.1475C26.7716 18.987 26.772 18.8204 26.809 18.66L27.309 16.496C27.767 14.511 27.914 12.466 27.745 10.436L27.653 9.333C27.5544 8.14969 27.0396 7.03962 26.2 6.2C25.432 5.43181 24.3903 5.00016 23.304 5H23.064C22.407 5 21.802 5.356 21.484 5.93L18.825 10.715C17.633 12.861 15.8561 14.6244 13.701 15.8L7.042 19.43C6.7263 19.6023 6.46289 19.8565 6.27951 20.1659C6.09614 20.4752 5.99958 20.8284 6 21.188V41C6 41.5304 6.21072 42.0391 6.58579 42.4142C6.96086 42.7893 7.46957 43 8 43H33.987C34.4225 42.9999 34.846 42.8576 35.1933 42.5948C35.5406 42.332 35.7925 41.9631 35.911 41.544Z"
                            fill="white"
                            stroke="white"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                        </mask>
                        <g mask="url(#mask0_142_11)">
                          <path
                            d="M0 0H48V48H0V0Z"
                            fill="#2D9CDB"
                          />
                        </g>
                      </svg>
                    </div>
                    <div
                      @click="handleGood(false,item)"
                      style="cursor: pointer"
                    >
                      <svg
                        v-if="!item.isChecked"
                        width="18"
                        height="18"
                        viewBox="0 0 48 48"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                      >
                        <path
                          d="M35.911 6.456L41.281 25.456C41.3651 25.7535 41.3791 26.0664 41.3219 26.3701C41.2646 26.6739 41.1378 26.9603 40.9513 27.2068C40.7648 27.4533 40.5236 27.6532 40.2468 27.7908C39.97 27.9285 39.6651 28.0001 39.356 28H27.875C27.171 28 26.651 28.654 26.809 29.34L27.309 31.504C27.767 33.489 27.914 35.534 27.745 37.564L27.653 38.667C27.5544 39.8503 27.0396 40.9604 26.2 41.8C25.432 42.5682 24.3903 42.9998 23.304 43H23.064C22.7417 42.9998 22.4252 42.9135 22.1475 42.75C21.8697 42.5865 21.6406 42.3517 21.484 42.07L18.825 37.285C17.6328 35.1394 15.8559 33.3763 13.701 32.201L7.042 28.568C6.72689 28.3961 6.46387 28.1425 6.28053 27.8339C6.09719 27.5252 6.00029 27.173 6 26.814V7C6 6.46957 6.21071 5.96086 6.58579 5.58579C6.96086 5.21071 7.46957 5 8 5H33.987C34.4225 5.00013 34.846 5.1424 35.1933 5.40519C35.5406 5.66798 35.7925 6.03693 35.911 6.456Z"
                          stroke="black"
                          stroke-width="4"
                          stroke-linecap="round"
                          stroke-linejoin="round"
                        />
                      </svg>
                      <svg
                        v-if="item.isChecked && !item.isGood"
                        width="18"
                        height="18"
                        viewBox="0 0 48 48"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                      >
                        <mask
                          id="mask0_142_6"
                          style="mask-type:luminance"
                          maskUnits="userSpaceOnUse"
                          x="4"
                          y="3"
                          width="40"
                          height="42"
                        >
                          <path
                            d="M35.911 6.456L41.281 25.456C41.3651 25.7535 41.3791 26.0664 41.3219 26.3701C41.2646 26.6739 41.1378 26.9603 40.9513 27.2068C40.7648 27.4533 40.5236 27.6532 40.2468 27.7908C39.97 27.9285 39.6651 28.0001 39.356 28H27.875C27.171 28 26.651 28.654 26.809 29.34L27.309 31.504C27.767 33.489 27.914 35.534 27.745 37.564L27.653 38.667C27.5544 39.8503 27.0396 40.9604 26.2 41.8C25.432 42.5682 24.3903 42.9998 23.304 43H23.064C22.7417 42.9998 22.4252 42.9135 22.1475 42.75C21.8697 42.5865 21.6406 42.3517 21.484 42.07L18.825 37.285C17.6328 35.1394 15.8559 33.3763 13.701 32.201L7.042 28.568C6.72689 28.3961 6.46387 28.1425 6.28053 27.8339C6.09719 27.5252 6.00029 27.173 6 26.814V7C6 6.46957 6.21071 5.96086 6.58579 5.58579C6.96086 5.21071 7.46957 5 8 5H33.987C34.4225 5.00013 34.846 5.1424 35.1933 5.40519C35.5406 5.66798 35.7925 6.03693 35.911 6.456Z"
                            fill="white"
                            stroke="white"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                        </mask>
                        <g mask="url(#mask0_142_6)">
                          <path
                            d="M0 0H48V48H0V0Z"
                            fill="#2D9CDB"
                          />
                        </g>
                      </svg>

                    </div>
                  </div>
                </div>

                <!--文本内容-->
                <div
                  class="card-text"
                  :style="'min-width:'+(item.role=='gpt'?'120px;' : '20px;')"
                >
                  <!-- content-->
                  <mavon-editor
                    style="
                      border: none;
                      box-shadow: none;
                      min-height: 0px;
                      background: #ffffff00;
                      background-color: #ffffff00;
                      flex: 10;
                      font-size: 16px;
                      font-weight: 500;
                      line-height: 24px;
                      padding: 0px;
                      min-width: 20px;
                      z-index: 1000;
                    "
                    align="start"
                    previewBackground="#ffffff00"
                    v-model="item.text"
                    :subfield="false"
                    defaultOpen="preview"
                    :toolbarsFlag="false"
                    v-if="item.timestamp!=null && item.role=='gpt'"
                  />
                  <div
                    v-else
                    style="white-space: pre-line"
                  >
                    {{ item.isError ? item.errorText : item.text || waitingText }}
                  </div>
                </div>
              </el-card>
            </div>
          </div>

          <!--输入框-->
          <div
            class="input-bar"
            align="center"
          >
            <div>
              <el-select
                class="type-select"
                v-model="sendType"
                placeholder="请选择"
              >

                <el-option
                  v-for="item in sendTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />

              </el-select>
            </div>
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
              v-if="sendType=='text'"
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
            <div
              v-else
              :style="'width:60px;padding-top:2px;'+ (sendData=='' || isSend ? '' : 'cursor:pointer')"
            >
              <i
                v-if="!isSend"
                class="el-icon-picture-outline"
                @click="sendData=='' ? '' : handleSendData()"
                :style="'color:'+(sendData=='' ? '#aaaaaa' : '#2D9CDB')"
              ></i>
            </div>
          </div>
        </div>
        <div
          :class="isShowSetting? 'setting-container' : 'setting-container hide'"
          :style="isMobile ? 'flex:0.02;' : 'height:100%;display: flex;align-items: center;'"
        >
          <div
            v-if="!isMobile"
            style="display: flex;flex-direction: column;align-items: center;width: 100%;height:90%;margin-bottom: 50px;"
          >

            <div
              class="clear-button-clear-right"
              @click="clearDialog()"
              :style="'width:80%;'"
              v-if="!isMobile"
              align="center"
            >
              <i
                class="el-icon-delete"
                style="font-size: 18px;margin-right:4px;"
              />
              <span v-if="isShowSetting" style="font-size: 18px;">清空对话</span>
              <div v-if="isShowSetting" style="font-size:12px;margin-top:4px;color:#a0a0a0;">请及时清空对话，对话历史过多会影响使用效果</div>
            </div>
            <div
              class="clear-button-tutorial"
              @click="tutorialVal = !tutorialVal;"
              :style="'width:80%;'"
              v-if="!isMobile"
              align="center"
            >
              <i
                class="el-icon-help"
                style="font-size: 18px;margin-right:4px;"
              />
              <span v-if="isShowSetting" style="font-size: 18px;">参数教程</span>
              <div v-if="isShowSetting" style="font-size:12px;margin-top:4px;color:#a0a0a0;">查看参数的具体介绍，通过参数样例了解使用方法
              </div>
            </div>
            <div class="clear-button-hide" style="width:80%;display: flex;align-items: center;"
                 @click="isShowSetting = !isShowSetting">
              <i :class="isShowSetting ? 'el-icon-caret-right':'el-icon-caret-left'"></i>
              <div style="width: 100%;text-align: center">
                <span v-if="isShowSetting" style="font-size: 18px;">隐藏 参数设置</span>
              </div>
            </div>
            <el-card class="control-card" :style="'overflow-y: auto;' + (isShowSetting? '' : 'padding-left:10px')">
              <!--              <el-tabs
                              v-model="activeName"
                              class="tabs"
                            >
                              <el-tab-pane
                                label="Prompt设置"
                                name="prompt"
                              >
                                <el-form
                                  style="width:98%;min-width:200px;padding-left:10px;"
                                  label-width="180px"
                                  label-position="top"
                                >
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.systemMsg"
                                        placement="top"
                                      >
                                        <span>角色设置<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-select
                                      v-model="roleId"
                                      style="margin-bottom:8px;width:100%;"
                                      @change="handleSelectRole(roleId)"
                                    >
                                      <el-option
                                        label="默认"
                                        :value="0"
                                      />
                                      <el-option
                                        v-for="item in roleList"
                                        :key="item.id"
                                        :label="item.title"
                                        :value="item.id"/>
                                    </el-select>
                                    <el-input
                                      resize="none"
                                      v-model="systemMsg"
                                      type="textarea"
                                      :autosize="{ minRows: 4, maxRows: 8}"
                                      @change="handleChangeSetting"
                                      placeholder="请输入角色设定..."

                                    ></el-input>
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.context"
                                        placement="top"
                                      >
                                        <span>问题背景<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-input
                                      resize="none"
                                      v-model="context"
                                      type="textarea"

                                      :autosize="{ minRows: 4, maxRows: 8}"
                                      @change="handleChangeSetting"
                                      placeholder="请输入问题背景..."
                                    ></el-input>
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.textNum"
                                        placement="top"
                                      >
                                        <span>回答字数限定<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-switch
                                      v-model="isTextNum"
                                      active-text="开启"
                                      inactive-text="关闭"
                                      @change="handleChangeSetting"
                                      active-color="#13ce6680"
                                      inactive-color="#dddddd80"
                                    />
                                    <el-input-number
                                      v-model="textNum"
                                      @change="handleChangeSetting"
                                      :max="2000"
                                      :min="1"
                                      :disabled="isTextNum"
                                    ></el-input-number>
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.isSteps"
                                        placement="top"
                                      >
                                        <span>分步返回<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-switch
                                      v-model="isSteps"
                                      active-text="开启"
                                      inactive-text="关闭"
                                      @change="handleChangeSetting"
                                      active-color="#13ce6680"
                                      inactive-color="#dddddd80"
                                    />
                                  </el-form-item>
                                </el-form>
                                <el-form
                                  style="width:98%;min-width:200px;padding-left:10px;"
                                  label-width="180px"
                                  label-position="top"
                                >
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.model"
                                        placement="top"
                                      >
                                        <span>模型选择<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-select
                                      v-model="model"
                                      @change="handleChangeSetting"
                                    >
                                      <el-option
                                        label="gpt-35-turbo"
                                        value="gpt-35-turbo"
                                        v-has-permi="['ny:gpt35']"
                                      />
                                      <el-option
                                        label="gpt-4"
                                        value="gpt-4"
                                        v-has-permi="['ny:gpt40']"
                                      />
                                    </el-select>
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.max_tokens"
                                        placement="top"
                                      >
                                        <span>最大Token数量<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-input-number
                                      v-model="max_tokens"
                                      :max="max_token_max"
                                      :min="1"
                                      @change="handleChangeSetting"
                                    ></el-input-number>
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.temperature"
                                        placement="top"
                                      >
                                        <span>发散性(0~2)<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-slider
                                      v-model="temperature"
                                      :step="0.1"
                                      :min="0"
                                      :max="2"
                                      show-input
                                      @change="handleChangeSetting"
                                    />
                                  </el-form-item>
                                </el-form>
                              </el-tab-pane>
                              <el-tab-pane
                                label="参数样例"
                                name="setting"
                                style="position: relative"
                              >
                                <el-form
                                  style="width:98%;min-width:200px;padding-left:10px;"
                                  label-width="180px"
                                  label-position="top"
                                >
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.systemMsg"
                                        placement="top"
                                      >
                                        <span>角色设置<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <div style="color: #606266;text-indent: 2em">在角色设置中，您可以选择任意一种角色，让GPT扮演该角色与您进行有内容偏向性的对话。例子如下：</div>
                                    <el-select
                                      v-model="roleId"
                                      style="margin-bottom:8px;width:100%;"
                                      @change="handleSelectRole(roleId)"
                                    >
                                      <el-option
                                        label="默认"
                                        :value="0"
                                      />
                                      <el-option
                                        v-for="item in roleList"
                                        :key="item.id"
                                        :label="item.title"
                                        :value="item.id"/>
                                    </el-select>
                                    <el-input
                                      resize="none"
                                      v-model="systemMsg"
                                      type="textarea"
                                      :autosize="{ minRows: 4, maxRows: 8}"
                                      @change="handleChangeSetting"
                                      placeholder="请输入角色设定..."

                                    ></el-input>
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.context"
                                        placement="top"
                                      >
                                        <span>问题背景<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-input
                                      resize="none"
                                      v-model="context"
                                      type="textarea"

                                      :autosize="{ minRows: 4, maxRows: 8}"
                                      @change="handleChangeSetting"
                                      placeholder="请输入问题背景..."
                                    ></el-input>
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.textNum"
                                        placement="top"
                                      >
                                        <span>回答字数限定<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-switch
                                      v-model="isTextNum"
                                      active-text="开启"
                                      inactive-text="关闭"
                                      @change="handleChangeSetting"
                                      active-color="#13ce6680"
                                      inactive-color="#dddddd80"
                                    />
                                    <el-input-number
                                      v-model="textNum"
                                      @change="handleChangeSetting"
                                      :max="2000"
                                      :min="1"
                                      :disabled="isTextNum"
                                    ></el-input-number>
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.isSteps"
                                        placement="top"
                                      >
                                        <span>分步返回<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-switch
                                      v-model="isSteps"
                                      active-text="开启"
                                      inactive-text="关闭"
                                      @change="handleChangeSetting"
                                      active-color="#13ce6680"
                                      inactive-color="#dddddd80"
                                    />
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.model"
                                        placement="top"
                                      >
                                        <span>模型选择<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-select
                                      v-model="model"
                                      @change="handleChangeSetting"
                                    >
                                      <el-option
                                        label="gpt-35-turbo"
                                        value="gpt-35-turbo"
                                        v-has-permi="['ny:gpt35']"
                                      />
                                      <el-option
                                        label="gpt-4"
                                        value="gpt-4"
                                        v-has-permi="['ny:gpt40']"
                                      />
                                    </el-select>
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.max_tokens"
                                        placement="top"
                                      >
                                        <span>最大Token数量<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-input-number
                                      v-model="max_tokens"
                                      :max="max_token_max"
                                      :min="1"
                                      @change="handleChangeSetting"
                                    ></el-input-number>
                                  </el-form-item>
                                  <el-form-item>
                                    <template slot="label">
                                      <el-tooltip
                                        :content="description.temperature"
                                        placement="top"
                                      >
                                        <span>发散性(0~2)<i
                                          class="el-icon-info"
                                          style="margin-left:4px;"
                                        ></i></span>
                                      </el-tooltip>
                                    </template>
                                    <el-slider
                                      v-model="temperature"
                                      :step="0.1"
                                      :min="0"
                                      :max="2"
                                      show-input
                                      @change="handleChangeSetting"
                                    />
                                  </el-form-item>
                                </el-form>
                              </el-tab-pane>
                            </el-tabs>-->

              <el-form
                style="width:98%;min-width:200px;padding-left:10px;"
                label-width="180px"
                label-position="top"
              >
                <el-form-item v-show="model.startsWith('gpt')">
                  <template slot="label">
                    <el-tooltip
                      :content="description.systemMsg"
                      placement="top"
                    >
                          <span>角色设置<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
                    </el-tooltip>
                  </template>
                  <el-select
                    v-model="roleId"
                    style="margin-bottom:8px;width:100%;"
                    @change="handleSelectRole(roleId)"
                  >
                    <el-option
                      label="默认"
                      :value="0"
                    />
                    <el-option
                      v-for="item in roleList"
                      :key="item.id"
                      :label="item.title"
                      :value="item.id"/>
                  </el-select>
                  <el-input
                    resize="none"
                    v-model="systemMsg"
                    type="textarea"
                    :autosize="{ minRows: 4, maxRows: 8}"
                    @change="handleChangeSetting"
                    placeholder="请输入角色设定..."

                  ></el-input>
                </el-form-item>
                <el-form-item v-show="model.startsWith('gpt')">
                  <template slot="label">
                    <el-tooltip
                      :content="description.textNum"
                      placement="top"
                    >
                          <span>回答字数限定<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
                    </el-tooltip>
                  </template>
                  <el-switch
                    v-model="isTextNum"
                    active-text="开启"
                    inactive-text="关闭"
                    @change="handleChangeSetting"
                    active-color="#13ce6680"
                    inactive-color="#dddddd80"
                  />
                  <el-input-number
                    v-model="textNum"
                    @change="handleChangeSetting"
                    :max="2000"
                    :min="1"
                    :disabled="isTextNum"
                  ></el-input-number>
                </el-form-item>
                <el-form-item v-show="model.startsWith('gpt')">
                  <template slot="label">
                    <el-tooltip
                      :content="description.isSteps"
                      placement="top"
                    >
                          <span>分步返回<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
                    </el-tooltip>
                  </template>
                  <el-switch
                    v-model="isSteps"
                    active-text="开启"
                    inactive-text="关闭"
                    @change="handleChangeSetting"
                    active-color="#13ce6680"
                    inactive-color="#dddddd80"
                  />
                </el-form-item>
              </el-form>
              <el-form
                style="width:98%;min-width:200px;padding-left:10px;"
                label-width="180px"
                label-position="top"
              >
                <el-form-item>
                  <template slot="label">
                    <el-tooltip
                      :content="description.model"
                      placement="top"
                    >
                          <span>模型选择<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
                    </el-tooltip>
                  </template>
                  <el-select
                    v-model="model"
                    @change="handleChangeSetting"
                  >
                    <el-option
                      label="gpt-35-turbo"
                      value="gpt-35-turbo"
                      v-has-permi="['ny:gpt35']"
                    />
                    <el-option
                      label="gpt-4"
                      value="gpt-4"
                      v-has-permi="['ny:gpt40']"
                    />
                    <el-option
                      label="文心一言 - ernie"
                      value="ernie-bot"
                      v-has-permi="['ny:ernie']"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item v-show="model.startsWith('gpt')">
                  <template slot="label">
                    <el-tooltip
                      :content="description.max_tokens"
                      placement="top"
                    >
                          <span>最大Token数量<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
                    </el-tooltip>
                  </template>
                  <el-input-number
                    v-model="max_tokens"
                    :max="max_token_max"
                    :min="1"
                    @change="handleChangeSetting"
                  ></el-input-number>
                </el-form-item>
                <el-form-item>
                  <template slot="label">
                    <el-tooltip
                      :content="description.temperature"
                      placement="top"
                    >
                          <span>发散性(0~2)<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
                    </el-tooltip>
                  </template>
                  <el-slider
                    v-model="temperature"
                    :step="0.05"
                    :min="0"
                    :max="model.startsWith('e')? 1 : 2"
                    show-input
                    @change="handleChangeSetting"
                  />
                </el-form-item>
              </el-form>
            </el-card>
          </div>
        </div>
      </div>
    </div>
    <el-drawer
      :visible.sync="sessionVal"
      size="80%"
      class="drawer-setting"
      :withHeader="false"
      style="min-width:400px;background-color: #FFFFFFC0;"
      v-if="isMobile"
      direction="ltr"
    >
      <div style="width:100%;height:100%;display: flex;flex-direction: column;align-items: center;overflow: hidden">
        <div
          class="session-card"
          style="margin-bottom:20px;width: 100%;border-color:#999093;"
        >
          <div @click="handleAddSession();sessionVal = false;" style="width: 100%;" align="center"><i
            class="el-icon-plus"></i></div>
        </div>
        <div style="overflow-y:auto;width: 100%;padding:0;" v-infinite-scroll="load">
          <div
            class="session-card"
            v-for="(item,index) in sessionList"
            :key="index"
            :style="item.id === sessionId ?  'border-color:#1c84c6;border-width:2px;' : 'border-color:#999093;'"
          >
              <span
                @click="handleChangeSession(item,index);sessionVal=false;"
                style="width:100%;
                    white-space:nowrap;
                    overflow:hidden;
                    text-overflow:ellipsis;
                    text-align: left;"
              >{{ getName(item) || '新会话' }}</span>
            <div class="session-delete" @click="handleDeleteSession(item,index)"><i class="el-icon-delete"></i>
            </div>
          </div>
          <div
            v-if="isLoadingSession"
            class="session-card"
          >
            <div style="width: 100%;" align="center"><i class="el-icon-loading"></i></div>
          </div>
        </div>
      </div>
    </el-drawer>
    <el-drawer
      :visible.sync="drawerVal"
      size="80%"
      class="drawer-setting"
      :withHeader="false"
      @close="closeDrawer"
      style="min-width:400px;background-color: #FFFFFFC0;"
    >
      <div
        class="clear-button-tutorial"
        @click="tutorialVal = !tutorialVal;"
        :style="'width:100%;'"
        v-if="isMobile"
        align="center"
      >
        <i
          class="el-icon-help"
          style="font-size: 18px;margin-right:4px;"
        />
        <span v-if="isShowSetting" style="font-size: 18px;">参数教程</span>
        <div v-if="isShowSetting" style="font-size:12px;margin-top:4px;color:#a0a0a0;">查看参数的具体介绍，通过参数样例了解使用方法
        </div>
      </div>
      <el-form
        style="width:98%;min-width:200px;padding-left:10px;"
        label-width="180px"
        label-position="top"
      >
        <el-form-item>
          <template slot="label">
            <el-tooltip
              :content="description.systemMsg"
              placement="top"
            >
                          <span>角色设置<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
            </el-tooltip>
          </template>
          <el-select
            v-model="roleId"
            style="margin-bottom:8px;width:100%;"
            @change="handleSelectRole(roleId)"
          >
            <el-option
              label="默认"
              :value="0"
            />
            <el-option
              v-for="item in roleList"
              :key="item.id"
              :label="item.title"
              :value="item.id"/>
          </el-select>
          <el-input
            resize="none"
            v-model="systemMsg"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 8}"
            @change="handleChangeSetting"
            placeholder="请输入角色设定..."

          ></el-input>
        </el-form-item>
        <el-form-item>
          <template slot="label">
            <el-tooltip
              :content="description.textNum"
              placement="top"
            >
                          <span>回答字数限定<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
            </el-tooltip>
          </template>
          <el-switch
            v-model="isTextNum"
            active-text="开启"
            inactive-text="关闭"
            @change="handleChangeSetting"
            active-color="#13ce6680"
            inactive-color="#dddddd80"
          />
          <el-input-number
            v-model="textNum"
            @change="handleChangeSetting"
            :max="2000"
            :min="1"
            :disabled="isTextNum"
          ></el-input-number>
        </el-form-item>
        <el-form-item>
          <template slot="label">
            <el-tooltip
              :content="description.isSteps"
              placement="top"
            >
                          <span>分步返回<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
            </el-tooltip>
          </template>
          <el-switch
            v-model="isSteps"
            active-text="开启"
            inactive-text="关闭"
            @change="handleChangeSetting"
            active-color="#13ce6680"
            inactive-color="#dddddd80"
          />
        </el-form-item>
      </el-form>
      <el-form
        style="width:98%;min-width:200px;padding-left:10px;"
        label-width="180px"
        label-position="top"
      >
        <el-form-item>
          <template slot="label">
            <el-tooltip
              :content="description.model"
              placement="top"
            >
                          <span>模型选择<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
            </el-tooltip>
          </template>
          <el-select
            v-model="model"
            @change="handleChangeSetting"
          >
            <el-option
              label="gpt-35-turbo"
              value="gpt-35-turbo"
              v-has-permi="['ny:gpt35']"
            />
            <el-option
              label="gpt-4"
              value="gpt-4"
              v-has-permi="['ny:gpt40']"
            />
            <el-option
              label="文心一言 - ernie"
              value="ernie-bot"
              v-has-permi="['ny:ernie']"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <template slot="label">
            <el-tooltip
              :content="description.max_tokens"
              placement="top"
            >
                          <span>最大Token数量<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
            </el-tooltip>
          </template>
          <el-input-number
            v-model="max_tokens"
            :max="max_token_max"
            :min="1"
            @change="handleChangeSetting"
          ></el-input-number>
        </el-form-item>
        <el-form-item>
          <template slot="label">
            <el-tooltip
              :content="description.temperature"
              placement="top"
            >
                          <span>发散性(0~2)<i
                            class="el-icon-info"
                            style="margin-left:4px;"
                          ></i></span>
            </el-tooltip>
          </template>
          <el-slider
            v-model="temperature"
            :step="0.05"
            :min="0"
            :max="model.startsWith('e')? 1 : 2"
            show-input
            @change="handleChangeSetting"
          />
        </el-form-item>
      </el-form>
    </el-drawer>

    <el-dialog
      title="参数解释与样例"
      :visible.sync="tutorialVal"
      :width="isMobile? '90%' : '60%'"
      class="tutorial-dialog"
      center
    >
      <template slot="title">
        <span style="font-size: 18px;">参数解释与样例</span>
      </template>
      <el-collapse v-model="activeTutorial" accordion>
        <el-collapse-item v-for="(item, index) in tutorialList" :name="(index+1)+''">
          <template slot="title">
            <el-row style="width:80%;height:100%;">
              <el-col :span="4" style="min-width: 160px;">
                <span style="font-size: 18px;margin-right:4px;">{{ (index + 1) + '. ' + item.title }}</span>
              </el-col>
              <el-col :span="6">
                <div>
                  <svg
                    v-show="item.models.some(str => str.startsWith('gpt'))"
                    style="margin-left: 4px;color: #606266"
                    width="16"
                    height="16"
                    viewBox="0 0 41 41"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      d="M37.5324 16.8707C37.9808 15.5241 38.1363 14.0974 37.9886 12.6859C37.8409 11.2744 37.3934 9.91076 36.676 8.68622C35.6126 6.83404 33.9882 5.3676 32.0373 4.4985C30.0864 3.62941 27.9098 3.40259 25.8215 3.85078C24.8796 2.7893 23.7219 1.94125 22.4257 1.36341C21.1295 0.785575 19.7249 0.491269 18.3058 0.500197C16.1708 0.495044 14.0893 1.16803 12.3614 2.42214C10.6335 3.67624 9.34853 5.44666 8.6917 7.47815C7.30085 7.76286 5.98686 8.3414 4.8377 9.17505C3.68854 10.0087 2.73073 11.0782 2.02839 12.312C0.956464 14.1591 0.498905 16.2988 0.721698 18.4228C0.944492 20.5467 1.83612 22.5449 3.268 24.1293C2.81966 25.4759 2.66413 26.9026 2.81182 28.3141C2.95951 29.7256 3.40701 31.0892 4.12437 32.3138C5.18791 34.1659 6.8123 35.6322 8.76321 36.5013C10.7141 37.3704 12.8907 37.5973 14.9789 37.1492C15.9208 38.2107 17.0786 39.0587 18.3747 39.6366C19.6709 40.2144 21.0755 40.5087 22.4946 40.4998C24.6307 40.5054 26.7133 39.8321 28.4418 38.5772C30.1704 37.3223 31.4556 35.5506 32.1119 33.5179C33.5027 33.2332 34.8167 32.6547 35.9659 31.821C37.115 30.9874 38.0728 29.9178 38.7752 28.684C39.8458 26.8371 40.3023 24.6979 40.0789 22.5748C39.8556 20.4517 38.9639 18.4544 37.5324 16.8707ZM22.4978 37.8849C20.7443 37.8874 19.0459 37.2733 17.6994 36.1501C17.7601 36.117 17.8666 36.0586 17.936 36.0161L25.9004 31.4156C26.1003 31.3019 26.2663 31.137 26.3813 30.9378C26.4964 30.7386 26.5563 30.5124 26.5549 30.2825V19.0542L29.9213 20.998C29.9389 21.0068 29.9541 21.0198 29.9656 21.0359C29.977 21.052 29.9842 21.0707 29.9867 21.0902V30.3889C29.9842 32.375 29.1946 34.2791 27.7909 35.6841C26.3872 37.0892 24.4838 37.8806 22.4978 37.8849ZM6.39227 31.0064C5.51397 29.4888 5.19742 27.7107 5.49804 25.9832C5.55718 26.0187 5.66048 26.0818 5.73461 26.1244L13.699 30.7248C13.8975 30.8408 14.1233 30.902 14.3532 30.902C14.583 30.902 14.8088 30.8408 15.0073 30.7248L24.731 25.1103V28.9979C24.7321 29.0177 24.7283 29.0376 24.7199 29.0556C24.7115 29.0736 24.6988 29.0893 24.6829 29.1012L16.6317 33.7497C14.9096 34.7416 12.8643 35.0097 10.9447 34.4954C9.02506 33.9811 7.38785 32.7263 6.39227 31.0064ZM4.29707 13.6194C5.17156 12.0998 6.55279 10.9364 8.19885 10.3327C8.19885 10.4013 8.19491 10.5228 8.19491 10.6071V19.808C8.19351 20.0378 8.25334 20.2638 8.36823 20.4629C8.48312 20.6619 8.64893 20.8267 8.84863 20.9404L18.5723 26.5542L15.206 28.4979C15.1894 28.5089 15.1703 28.5155 15.1505 28.5173C15.1307 28.5191 15.1107 28.516 15.0924 28.5082L7.04046 23.8557C5.32135 22.8601 4.06716 21.2235 3.55289 19.3046C3.03862 17.3858 3.30624 15.3413 4.29707 13.6194ZM31.955 20.0556L22.2312 14.4411L25.5976 12.4981C25.6142 12.4872 25.6333 12.4805 25.6531 12.4787C25.6729 12.4769 25.6928 12.4801 25.7111 12.4879L33.7631 17.1364C34.9967 17.849 36.0017 18.8982 36.6606 20.1613C37.3194 21.4244 37.6047 22.849 37.4832 24.2684C37.3617 25.6878 36.8382 27.0432 35.9743 28.1759C35.1103 29.3086 33.9415 30.1717 32.6047 30.6641C32.6047 30.5947 32.6047 30.4733 32.6047 30.3889V21.188C32.6066 20.9586 32.5474 20.7328 32.4332 20.5338C32.319 20.3348 32.154 20.1698 31.955 20.0556ZM35.3055 15.0128C35.2464 14.9765 35.1431 14.9142 35.069 14.8717L27.1045 10.2712C26.906 10.1554 26.6803 10.0943 26.4504 10.0943C26.2206 10.0943 25.9948 10.1554 25.7963 10.2712L16.0726 15.8858V11.9982C16.0715 11.9783 16.0753 11.9585 16.0837 11.9405C16.0921 11.9225 16.1048 11.9068 16.1207 11.8949L24.1719 7.25025C25.4053 6.53903 26.8158 6.19376 28.2383 6.25482C29.6608 6.31589 31.0364 6.78077 32.2044 7.59508C33.3723 8.40939 34.2842 9.53945 34.8334 10.8531C35.3826 12.1667 35.5464 13.6095 35.3055 15.0128ZM14.2424 21.9419L10.8752 19.9981C10.8576 19.9893 10.8423 19.9763 10.8309 19.9602C10.8195 19.9441 10.8122 19.9254 10.8098 19.9058V10.6071C10.8107 9.18295 11.2173 7.78848 11.9819 6.58696C12.7466 5.38544 13.8377 4.42659 15.1275 3.82264C16.4173 3.21869 17.8524 2.99464 19.2649 3.1767C20.6775 3.35876 22.0089 3.93941 23.1034 4.85067C23.0427 4.88379 22.937 4.94215 22.8668 4.98473L14.9024 9.58517C14.7025 9.69878 14.5366 9.86356 14.4215 10.0626C14.3065 10.2616 14.2466 10.4877 14.2479 10.7175L14.2424 21.9419ZM16.071 17.9991L20.4018 15.4978L24.7325 17.9975V22.9985L20.4018 25.4983L16.071 22.9985V17.9991Z"
                      fill="#606266"
                    ></path>
                  </svg>
                  <svg
                    v-show="item.models.some(str => str.startsWith('ernie'))"
                    style="margin-left: 4px;" width="20" height="20" viewBox="0 0 24 24" fill="none"
                    xmlns="http://www.w3.org/2000/svg">
                    <path
                      d="M7.56301 19.28C8.4009 19.316 9.2399 19.243 10.059 19.063C11.0376 18.85 12.0455 18.8057 13.039 18.932C13.4752 18.9812 13.9064 19.0671 14.328 19.189C15.405 19.464 16.938 19.412 17.333 18.779C17.623 18.311 17.586 17.992 17.307 17.58C17.247 17.49 17.181 17.41 17.119 17.345L17.095 17.32C16.8434 17.1188 16.5957 16.9127 16.352 16.702C15.7419 16.1766 15.1569 15.6226 14.599 15.042C14.0259 14.4521 13.4987 13.8192 13.022 13.149L12.986 13.096C12.244 11.957 11.428 12.029 10.984 12.778C10.7043 13.2484 10.3848 13.6941 10.029 14.11C9.61901 14.592 9.19901 15 8.72301 15.407C8.60101 15.512 8.22101 15.827 8.31201 15.751C8.30801 15.754 8.29501 15.766 8.36201 15.68C8.26401 15.8 7.41201 16.557 7.16201 16.842C6.64701 17.425 6.44001 17.922 6.51701 18.322C6.59001 18.698 6.73701 18.909 6.96701 19.067C7.11304 19.1645 7.27574 19.2343 7.44701 19.273C7.48001 19.276 7.51901 19.278 7.56301 19.28ZM14.662 12.004C16.037 13.974 18.393 15.797 18.393 15.797C18.393 15.797 20.457 17.545 19.031 19.835C17.605 22.125 13.778 21.113 13.778 21.113C13.778 21.113 12.258 20.623 10.492 21.015C8.72701 21.41 7.20601 21.26 7.20601 21.26C7.20601 21.26 5.00001 21.015 4.55301 18.701C4.10701 16.387 6.61301 14.661 6.81101 14.417C7.00701 14.17 8.32301 13.344 9.26301 11.759C10.203 10.173 12.847 9.219 14.662 12.004ZM20.2 10.584C20.2 11.041 20.391 12.977 18.648 13.016C16.905 13.054 16.832 11.838 16.832 10.966C16.832 10.053 17.019 8.761 18.422 8.761C19.822 8.761 20.2 10.13 20.2 10.585V10.584ZM14.771 7.807C13.591 7.655 13.324 6.585 13.438 5.514C13.533 4.639 14.58 3.295 15.419 3.488C16.256 3.678 17.019 4.788 16.865 5.742C16.713 6.699 15.953 7.96 14.771 7.807ZM9.75501 7.44C8.89401 7.44 8.19501 6.447 8.19501 5.22C8.19501 3.993 8.89301 3 9.75501 3C10.618 3 11.315 3.993 11.315 5.22C11.315 6.447 10.618 7.44 9.75501 7.44ZM5.96201 12.006C4.26701 12.371 3.63501 10.409 3.82201 9.491C3.82201 9.491 4.02201 7.504 5.39801 7.381C6.49101 7.286 7.29601 8.482 7.37801 9.166C7.43001 9.61 7.66101 11.641 5.96201 12.006Z"
                      fill="#606266"/>
                  </svg>
                </div>
              </el-col>
            </el-row>

          </template>
          <div style="color: #606266;text-indent: 2em;font-size:16px;">
            {{ item.text }}
          </div>

          <div class="tutorial-flex" :style="isMobile ? 'flex-direction: column;' : ''">
            <div class="tutorial-left" align="center" :style="isMobile ? 'width:100%;' : ''">
              <div class="tutorial-demo-title">设置前</div>
              <div class="tutorial-demo-describe">设置内容：{{ item.before.setting }}</div>
              <!-- 用户 -->
              <div
                style="padding:12px;"
                align="right"
              >
                <el-card
                  class="card"
                  align="left"
                  :style="(isMobile ? 'max-width:96%;' : '') + 'background-color: #ffddc2;'"
                >
                  <div
                    class="card-text"
                    style="min-width:20px;"
                  >
                    <div
                      style="white-space: pre-line"
                    >
                      {{ item.before.question }}
                    </div>
                  </div>
                </el-card>
              </div>
              <!-- GPT -->
              <div
                style="padding:12px"
                align="left"
              >
                <el-card
                  class="card"
                  align="left"
                  :style="(isMobile ? 'max-width:96%;' : '')"
                >
                  <div
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
                  </div>

                  <!--文本内容-->
                  <div
                    class="card-text"
                    style="min-width:120px"
                  >
                    <!-- content-->
                    <mavon-editor
                      style="
                      border: none;
                      box-shadow: none;
                      min-height: 0px;
                      background: #ffffff00;
                      background-color: #ffffff00;
                      flex: 10;
                      font-size: 16px;
                      font-weight: 500;
                      line-height: 24px;
                      padding: 0px;
                      min-width: 20px;
                      z-index: 1000;
                    "
                      align="start"
                      previewBackground="#ffffff00"
                      v-model="item.before.answer"
                      :subfield="false"
                      defaultOpen="preview"
                      :toolbarsFlag="false"
                    />
                  </div>

                </el-card>
              </div>
            </div>
            <div class="tutorial-right" align="center" :style="isMobile ? 'width:100%;' : ''">
              <div class="tutorial-demo-title">设置后</div>
              <div class="tutorial-demo-describe" style="color: #2D9CDB;border-color: #2D9CDB;">
                设置内容：{{ item.after.setting }}
              </div>
              <!-- 用户 -->
              <div
                style="padding:12px;"
                align="right"
              >
                <el-card
                  class="card"
                  align="left"
                  :style="(isMobile ? 'max-width:96%;' : '') + 'background-color: #ffddc2;'"
                >
                  <div
                    class="card-text"
                    style="min-width:20px;"
                  >
                    <div
                      style="white-space: pre-line"
                    >
                      {{ item.after.question }}
                    </div>
                  </div>
                </el-card>
              </div>
              <!-- GPT -->
              <div
                style="padding:12px"
                align="left"
              >
                <el-card
                  class="card"
                  align="left"
                  :style="(isMobile ? 'max-width:96%;' : '')"
                >
                  <div
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
                  </div>

                  <!--文本内容-->
                  <div
                    class="card-text"
                    style="min-width:120px"
                  >
                    <!-- content-->
                    <mavon-editor
                      style="
                      border: none;
                      box-shadow: none;
                      min-height: 0px;
                      background: #ffffff00;
                      background-color: #ffffff00;
                      flex: 10;
                      font-size: 16px;
                      font-weight: 500;
                      line-height: 24px;
                      padding: 0px;
                      min-width: 20px;
                      z-index: 1000;
                    "
                      align="start"
                      previewBackground="#ffffff00"
                      v-model="item.after.answer"
                      :subfield="false"
                      defaultOpen="preview"
                      :toolbarsFlag="false"
                    />
                  </div>

                </el-card>
              </div>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-dialog>
  </div>
</template>

<script>
import {addHistory, updateHistory} from "@/api/chatGPT/history";
import {chatQuestion, wxyyQuestion} from "@/api/chatGPT/chat";
import {listSession, addSession, updateSession, delSession} from "@/api/chatGPT/chat";
import {getPromptAllList} from "@/api/chatGPT/prompt";
import item from "../../layout/components/Sidebar/Item";

export default {
  name: 'ChatGPT',
  data() {
    return {
      isShowSetting: true,
      tutorialVal: false,
      activeTutorial: '0',

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
      systemMsg: '你是一个喜欢使用中文的助手',
      systemRole: '角色1',

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

      description: {
        max_tokens: '要在聊天完成中生成的最大令牌(Token)数。',
        temperature: '较高的值（如 0.8）将使输出更加随机，而较低的值（如 0.2）将使其更加集中和确定。',
        top_p: '使用温度采样的替代方法称为核心采样，其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的代币。',
        frequency_penalty: '正值会根据新标记到目前为止在文本中的现有频率来惩罚新标记，从而降低模型逐字重复同一行的可能性。',
        presence_penalty: '正值会根据新标记到目前为止是否出现在文本中来惩罚它们，从而增加模型讨论新主题的可能性。',
        model: '选择所使用的模型',
        systemMsg: '对系统提示进行编辑，奠定对话的风格与主基调',
        context: '在这里输入上下文，以便模型在生成响应时考虑它',
        textNum: '对生成的文本进行数量限制',
        isSteps: '是否开启步骤模式，将回答以分条步骤的形式展示',
      },

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

      sendType: 'text',
      sendTypes: [
        {
          value: 'text',
          label: '文本'
        },
        //{
        //  value: 'image',
        //  label: '图片'
        //},
      ],
      isMobile: false,
      testShow: false,

      roleId: this.$route.query.role || 0,
      roleList: [],

      total: 0,
      isLoadingSession: true,
      sessionParams: {
        pageSize: 20,
        pageNum: 1,
      },

      tutorialList: [
        {
          title: '角色设置',
          models: ['gpt-35-turbo', 'gpt-4'],
          text: '在角色设置中，您可以根据需求，选择任意一种已设定的角色，让GPT扮演该指定角色，与您进行相应内容偏向的对话，针对性地解决您的问题。',
          before: {
            setting: '无',
            question: '如何学习编程',
            answer: '学习编程的方法因人而异，但以下是一些通用的建议：\n' +
              '1. 选择一门编程语言并学习其基本语法；\n' +
              '2. 寻找好的编程教材或课程，并跟随它们的步骤逐渐学习；\n' +
              '3. 练习编写代码，尝试编写简单的项目；\n' +
              '4. 参加编程社区或论坛，交流经验和代码；\n' +
              '5. 学习使用版本控制工具，如Git，以管理自己的代码；\n' +
              '6. 不断学习新的编程技术和工具，并持续练习、编写代码。\n' +
              '此外，还有一些在线编程平台和社区，如Codecademy、Leetcode和Github等，可以提供练习和交流的机会。\n'
          },
          after: {
            setting: '充当励志演讲者',
            question: '如何学习编程',
            answer: '欢迎大家，今天我们来谈一下如何学习编程。\n' +
              '编程是一项强大的技能，可以让你构建出各种有用的工具、应用程序和网站。但是，对于许多人来说，从零开始学习编程可能感到有些吓人。但是不要担心，下面是几个能够帮助你掌握编程技能并克服学习编程过程中困难的提示。\n' +
              '首先，你需要一个长期的目标。设定一个可以量化的目标，如制作一个简单的网站或开发一个向东代码库，这能够帮助你思考您所需学习的内容和时间。坚定的目标会帮助你在学习过程中保持动力。\n' +
              '接下来，选择一个编程语言并找到相关的教育资源。例如，尝试Python，C++或Java等语言，它们都有各自的优点和适用范围。你可以在网络上找到一些很好的编程教育资源，包括视频教程，应用程序和在线代码任务。\n' +
              '最后，不要害怕犯错。学习编程是一个持续不断的过程，不要在学习过程中伤害自己。调试代码通常需要很长时间才能做到精通，因此，不要失去信心并相信自己的能力。\n' +
              '总之，学习编程需要耐心，恒心和勇气。尝试吸收信息并将其应用于你手头的项目，把握大局同时保持细节的关注，相信自己并别忘了享受你所学习的过程，你终会成为一个优秀的程序员。\n'
          },
        },
        {
          title: '回答字数限定',
          models: ['gpt-35-turbo', 'gpt-4'],
          text: '在回答字数限定中，您可以设置GPT单个回答的字数上限，GPT会在对话时尽量将回答字数压缩在所设字数上限内。',
          before: {
            setting: '无 (此次回答共318字)',
            question: '对迪士尼乐园进行描述并介绍相应方案',
            answer: '迪士尼乐园是世界闻名的主题公园，拥有多个分布在不同地区的主题公园，如美国的迪士尼乐园、迪士尼加勒比海盗乐园、欧洲的迪士尼乐园等。每个乐园都有不同的主题和特色，如动画电影、卡通人物、经典童话故事等。\n' +
              '为了让游客在乐园中有更好的体验和游玩，迪士尼乐园提供了多种方案。首先，游客可以提前购买门票，避免排队购票浪费时间，并且预订酒店、餐饮和特别活动。其次，乐园也提供了多种游玩方式，如快速通行证（Fastpass）和单车道游玩等，可以节省排队时间。此外，乐园还设有许多娱乐设施和表演节目，如过山车、翻转舞台、花车巡游等，让游客能够全方位地享受乐园的精彩。\n' +
              '总之，迪士尼乐园是一个可以让人沉浸在童话世界的地方，提供了多种方案和体验，让游客能够获得愉悦和满足的感受。'
          },
          after: {
            setting: '250',
            question: '对迪士尼乐园进行描述并介绍相应方案',
            answer: '迪士尼乐园是一个拥有众多主题区域、各种刺激娱乐项目以及迪士尼经典角色的主题公园。每个主题区域都有其独特的风格和文化特色，例如童话城堡、海底世界和星球大战等。游客可以乘坐过山车、旋转木马等项目，观看绝妙的音乐舞蹈表演，甚至可以和迪士尼角色进行互动。为了更好地享受迪士尼乐园的游乐项目和文化体验，游客可以提前规划好行程，购买入园门票、快速通行证等，以避免排队等待过长时间，同时可提供提前预定餐饮服务和住宿计划，以充分利用时间和资源。此外，迪士尼乐园还提供诸如 VIP 服务、特殊主题套餐等定制服务，以满足不同游客的需求。'
          },
        },
        {
          title: '分步返回',
          models: ['gpt-35-turbo', 'gpt-4'],
          text: '在分布返回中，您可以设定回答的格式、是否开启步骤模式。开启后，GPT的回答将以分条步骤的形式展示，以使回答逻辑更清晰。',
          before: {
            setting: '无',
            question: '如何学习编程',
            answer: '学习编程的步骤可以大致分为以下几步：\n' +
              '选择一门编程语言：从目前流行的编程语言中选择适合自己的学习语言，如 Python、Java、C++ 等。\n' +
              '学习基础知识：不管选择哪种编程语言，都需要掌握基础的编程概念，例如变量、数据类型、运算符、流程控制等。\n' +
              '练习编程：通过编写小程序、练手项目、参与开源项目等方式，来巩固和提高自己的编程能力。\n' +
              '查阅资料：当遇到难题或者要学习一些高级内容时，可以通过查阅书籍、教程、网上资料等方式获取相关知识。\n' +
              '参与交流：加入编程社区，和其他开发者交流经验，获取更多的学习资源和支持。\n' +
              '总之，学习编程需要坚持不懈，不断实践和探索，在实际中才能不断地提高自己的编程能力。\n'
          },
          after: {
            setting: '开启步骤模式',
            question: '如何学习编程',
            answer: '学习编程的步骤可以按以下的几个步和点来进行分解：\n' +
              '步1：选择编程语言和学习方式\n' +
              '点1：选择适合自己的编程语言，例如 Python、Java、C++等常见的编程语言。\n' +
              '点2：选择适合自己的学习方式，例如自学、参加线下课程、参加在线课程等。\n' +
              '步2：学习编程基础知识\n' +
              '点1：学习计算机基础知识，例如计算机组成原理、操作系统等。\n' +
              '点2：学习编程语言基础语法和编程规范，例如变量、运算符、条件语句、循环语句等。\n' +
              '点3：学习编程范式和设计模式，例如面向过程编程、面向对象编程、函数式编程、MVC模式等。\n' +
              '步3：锻炼编程实战能力\n' +
              '点1：完成编程练习，例如LeetCode、LintCode等在线编程练习平台上的题目。\n' +
              '点2：参与编程实战项目，例如开发一个小应用程序、参与开源项目等。\n' +
              '步4：不断学习和提升\n' +
              '点1：关注编程知识和技术趋势，例如阅读相关书籍、博客、论坛等。\n' +
              '点2：学习和使用相关的编程工具和框架，例如 Git、Django、React等。\n' +
              '点3：参加技术社区和开发者活动，例如参加技术论坛、技术交流群、技术会议等。\n' +
              '以上是学习编程的一些步骤和方法，希望能够帮助到你。\n'
          },
        },
        {
          title: '模型选择',
          models: ['gpt-35-turbo', 'gpt-4', 'ernie-bot'],
          text: '在模型选择中，您可以选择需要使用的语言模型。目前支持GPT-3.5、GPT-4与文言一心（ernie-bot）。需注意：使用的模型不同，Token单价以及最终产生的费用也会不同。',
          before: {
            setting: '无 (默认使用GPT-3.5)',
            question: '如何学习编程',
            answer: '学习编程可以分为以下几个步骤：\n' +
              '1. 了解编程的基础知识：需要了解计算机、编程语言、算法等基本概念；\n' +
              '2. 选择一门编程语言：目前较为流行的编程语言包括 Python、Java、C++、JavaScript 等；\n' +
              '3. 学习编程语言的基础语法和基本数据类型：例如变量、运算符、循环语句、条件语句等；\n' +
              '4. 通过练习来强化学习：可以通过刷题、写项目、看书等方式来不断提高自己的编程技能；\n' +
              '5. 学习其他相关技能：例如如何使用开发工具、如何调试程序、如何管理代码等。\n' +
              '需要注意的是，编程是一门需要不断练习的技能，只有通过不断的实践和总结才能够提高自己的编程水平。\n'
          },
          after: {
            setting: 'GPT-4',
            question: '如何学习编程',
            answer: '学习编程可以分为几个步骤：\n' +
              '1. 确定学习目标：首先，你需要确定你想学习编程的目的。是为了找工作、开发自己的项目、兴趣爱好还是其他原因？这将帮助你选择最适合你的编程语言和学习资源。\n' +
              '2. 选择编程语言：根据你的学习目标和兴趣，选择合适的编程语言。如果你是编程初学者，可以从Python、JavaScript或Ruby等编程语言开始学习，因为它们入门较为简单，同时也非常实用。\n' +
              '3. 学习基本概念：开始学习编程时，你需要掌握一些基本概念，例如变量、数据类型、算术运算、条件语句、循环语句以及函数等。这些基本概念在不同的编程语言中均通用。\n' +
              '4. 利用在线资源：现在有很多在线资源供你免费学习编程，例如菜鸟教程（www.runoob.com）、W3School（www.w3school.com.cn）等。可以根据自己的需求和学习进度挑选合适的课程。\n' +
              '5. 实践编程：编程学习的关键在于实践。只有通过编写代码、调试错误，才能真正掌握编程技巧。你可以尝试编写一些简单的练习项目，如计算器、日历或网页等。\n' +
              '6. 参加编程社区：加入编程社区，如GitHub、Stack Overflow等，可以与其他编程爱好者交流，获取帮助和灵感。还可以通过阅读和分析别人的代码，提高自己的编程水平。\n' +
              '7. 学习进阶知识：在掌握基本概念后，可以深入学习一些编程的高级知识，比如面向对象编程、设计模式、数据结构和算法等。这将有助于你成为更专业的程序员。\n' +
              '8. 坚持练习：学习编程需要时间和毅力，不要怕遇到困难，要学会解决问题和独立思考。坚持练习和实践，你的编程能力会不断提高。\n' +
              '9. 考虑参加编程课程或订阅付费资源：如果觉得自学效果不佳，可以考虑参加线上或线下的编程课程，有专业老师指导。也可以订阅一些付费教学资源，如LeetCode、极客时间等。\n' +
              '10. 参与开源项目：通过参与开源项目，你可以学习到更多的编程技巧，提高编程水平。同时，这对你将来找工作或者与其他程序员合作也是很有帮助的。\n' +
              '最后，希望您加油，祝您学习编程顺利！\n'
          },
        },
        {
          title: '最大Token数量',
          models: ['gpt-35-turbo', 'gpt-4'],
          text: '在最大token数量中，您可以设定在聊天中生成的最大令牌数（GPT3.5上限4096，GPT4上限8000）。token是指将输入文本分解成的单词或子序列。因此token越多，可使回答更明确，但所需费用也会更高。最大Token数量将直接影响到对话轮数、对话长度，请注意选择合适的数量。',
          before: {
            setting: '1000',
            question: '请针对公司在职人员，对提高工作效率以及增进组织协调能力提供指导建议，限定范围互联网行业',
            answer: '对于在互联网行业工作的人员，提高工作效率以及增进组织协调能力是非常重要的事情，以下是几个建议：\n' +
              '\n' +
              '1. 利用工具提高效率：互联网行业使用大量的电脑，可以使用一些工具来提高工作效率。例如，使用通讯软件如Slack和微信，可以快速地与同事交流，避免错过重要信息。使用项目管理工具如Trello和Asana，可以更好地协调团队成员，提高工作效率。\n' +
              '\n' +
              '2. 学习时间管理技巧：时间管理技巧对于提高工作效率非常重要。可以学习一些时间管理技巧，如制定日程表、合理安排工作时间、设置优先级等。这将帮助你更好地控制时间，更高效地完成工作。\n' +
              '\n' +
              '3. 建立团队协作文化：在互联网行业，许多工作都需要与团队成员合作完成。因此，建立团队协作文化非常重要。可以积极提供帮助并寻求反馈，增强团队合作能力。\n' +
              '\n' +
              '4. 学习并使用团队协作工具：互联网行业中，有许多团队协作工具可以使用，如 GitHub、Google Docs和Confluence等。使用这些工具能够更好地协调团队成员，提高工作效率。\n' +
              '\n' +
              '5. 持续学习新知识：随着互联网行业的快速发展，持续学习新知识非常重要。对于职业发展来说，掌握最新的技术和知识是必要的。通过看技术书籍，参加技术\n'
          },
          after: {
            setting: '4000',
            question: '请针对公司在职人员，对提高工作效率以及增进组织协调能力提供指导建议，限定范围互联网行业',
            answer: '针对互联网行业公司在职人员提高工作效率以及增进组织协调能力的建议：\n' +
              '1. 学习时间管理：\n' +
              '时间管理是提高工作效率的关键之一，可以通过制定具体的工作计划以及设定优先级，最大程度地利用工作时间。例如，利用日程表、任务列表、提醒工具等来规划和管理自己的时间。\n' +
              '2. 掌握团队协作技巧：\n' +
              '团队合作是互联网行业发展不可或缺的一部分。学会如何与团队成员合作，如何分配任务和资源，如何解决冲突并建立团队文化，可以使整个团队更加紧密并提高工作效率。\n' +
              '3. 积极学习技能：\n' +
              '互联网行业也是一个高速发展的行业，所以员工需要不断更新知识和技能，掌握新技能。学习和发展新技能可以让员工更加适应行业的变化，提高自己的工作效率并为公司的成功发展做出贡献。\n' +
              '4. 精简流程：\n' +
              '互联网行业的传统模式被颠覆，为了扩大业务并减少浪费的人力和资源，员工可以审查一遍现有的流程，精简多余的步骤和环节，从而提高质量和效率。\n' +
              '5. 掌握有效沟通技巧：\n' +
              '良好的沟通技巧是提高团队合作和组织协调能力的关键。沟通技巧包括倾听、讲话、写作和演示。学习如何有效地传递信息、建立信任和处理冲突可以帮助促进团队合作，加强组织协调和沟通效率。\n' +
              '6. 利用技术工具：\n' +
              '在互联网行业，几乎所有的工具和软件都是为了提高效率而生。例如使用专业的项目管理工具、协作工具、虚拟会议软件等可以帮助员工更好地完成任务和项目。合理使用这些技术工具可以提高工作效率和组织协调能力。\n' +
              '7. 建立学习型组织文化：\n' +
              '建立学习型组织文化，激励和鼓励员工不断学习和改进，互相提供支持和反馈，让整个企业不断进步并提高工作效率和组织协调能力。\n' +
              '总之，以上建议可以帮助互联网行业的在职人员提高工作效率和增进组织协调能力，从而为公司的成功发展做出贡献。\n'
          },
        },
        {
          title: '发散性',
          models: ['gpt-35-turbo', 'gpt-4', 'ernie-bot'],
          text: '在发散性中，您可以设定范围为0~2（GPT） / 0~1（文心一言）的对话发散性，GPT会在对话时根据设定值控制回复对问题的关联性。须知：靠近2为较高的值，可使回答更随机发散；靠近0为较低的值，可使问题更集中贴近问题。',
          before: {
            setting: '0.2（GPT）',
            question: '如何学习编程',
            answer: '学习编程可以遵循以下步骤：\n' +
              '1. 选择编程语言：选择一种适合自己的编程语言，例如Python、Java、C++等。\n' +
              '2. 学习基础知识：学习编程的基础知识，例如变量、数据类型、运算符、控制结构、函数等。\n' +
              '3. 练习编程：通过编写简单的程序来练习编程，例如编写一个计算器、猜数字游戏等。\n' +
              '4. 学习算法和数据结构：学习算法和数据结构可以帮助你更好地理解编程，并提高编程能力。\n' +
              '5. 参加编程社区：加入编程社区可以与其他编程爱好者交流经验，获取更多的学习资源和支持。\n' +
              '6. 实践项目：通过实践项目来提高编程能力，例如开发一个网站、一个应用程序等。\n' +
              '7. 持续学习：编程是一个不断学习的过程，需要不断地学习新的知识和技能，保持学习的热情和动力。\n'
          },
          after: {
            setting: '1.2（GPT）',
            question: '如何学习编程',
            answer: '学习编程的方法有很多种，以下是一些常见的方法：\n' +
              '1.选择合适的编程语言：编程语言有很多种，例如经典的C/C++，易学易貌成的Python和网站开发必备的JavaScript等。选择一种自己感兴趣且开发领域需求大的编程语言学习。\n' +
              '2.找到合适的学习重点：学习编程需要系统性的相关知识，建议先学习一些基本的编程原理和算法等。\n' +
              '3.了解相关文献资料：买书、看博客、找学习最佳实践文章等都是非常好的学习方法，深入阅读编程方面的相关书籍可以更快地掌握编程技能。\n' +
              '4.获取编程经验：光坐在台式机前看书不如在实际开发中丰富经验。\n' +
              '5.询问和反馈：去相关的开发者社交网站提问，检查代码并寻求设置更为高级和优化化策略的指导。\n' +
              '6.参加编程社区： 可以向各种技术博客、问答问询网站、开放社区等去留言沟通，分享经验。\n'
          },
        },
      ],
    };
  },
  activated: function () {
    if (this.$route.query.role) {
      this.roleId = Number(this.$route.query.role) || 0;
    } else if (localStorage.getItem('chatSetting')) {
      let chatSetting = JSON.parse(localStorage.getItem('chatSetting'));
      this.roleId = Number(chatSetting.roleId) || 0;
    }
    this.handleSelectRole(this.roleId);
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
    this.delayAn(); //加载动画
    const historyContent = this.$refs.historyContent;
    historyContent.addEventListener('scroll', () => {
      this.scrollPosition = historyContent.scrollTop;
    }); //滚动条监听
    this.init();
    this.message[0] = {role: 'system', content: this.getSystem()};
  },
  methods: {
    init() {
      listSession(this.sessionParams).then(res => {
        if (res.code === 200) {
          this.total = res.total;
          this.sessionList = res.rows;
          if (this.sessionList.length === 0) {
            this.sessionList.push({id: 0, history: '[]'})
          } else {
            this.sessionId = this.sessionList[0].id;
            this.roleId = Number(this.sessionList[0].promptId || '0');
            let list = this.sessionList[0].history;
            this.onlyAddList = JSON.parse(list);
            this.historyList = JSON.parse(list);
            this.formatterMessage();
          }
        } else {
          this.$message.error(res.msg);
        }
      }).catch(err => {
        this.$message.error(err)
      })
      getPromptAllList().then(res => {
        this.roleList = res.data;
        this.handleSelectRole(this.roleId);
      })
    },
    handleSelectRole(id) {
      this.roleId = id;
      const i = id - 1;
      if (i < 0) {
        this.systemMsg = '你是一个喜欢使用中文的助手';
        return;
      }
      ;
      this.systemMsg = this.roleList[i].rule;
      this.handleChangeSetting();
      this.message[0] = {role: 'system', content: this.getSystem()};
    },
    formatterMessage() {
      let x = this.historyList.map(item => {
        return {role: item.role === 'gpt' ? 'assistant' : 'user', content: item.text}
      });
      this.message = [{role: 'system', content: this.getSystem()}, ...x]
    },
    load() {
      console.log('load')
      if (this.sessionList.length === this.total || this.total === 0) {
        this.isLoadingSession = false;
        return;
      }
      if (this.sessionList.length === this.total + 1 && this.sessionList.findIndex(item => item.id === 0) != -1) {
        this.isLoadingSession = false;
        return;
      }
      this.sessionParams.pageNum += 1;
      this.isLoadingSession = true;
      listSession(this.sessionParams).then(res => {
        if (res.code === 200) {
          this.total = res.total;
          this.sessionList = [...this.sessionList, ...res.rows];
        }
      })
    },
    getSessionList(needChange, index) {
      listSession({pageSize: this.sessionParams.pageSize * this.sessionParams.pageNum, pageNum: 1}).then(res => {
        if (res.code === 200) {
          this.total = res.total;
          this.sessionList = res.rows;
          if (this.sessionList.length === 0) {
            this.sessionList.push({id: 0, history: '[]'})
          } else {
          }
          if (needChange) {
            this.sessionId = this.sessionList[index].id;
            this.handleSelectRole(Number(this.sessionList[0].promptId || '0'));
            let list = this.sessionList[0].history;
            this.onlyAddList = JSON.parse(list);
            this.historyList = JSON.parse(list);
          }
        } else {
          this.$message.error(res.msg);
        }
      }).catch(err => {
        this.$message.error(err)
      })
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
        systemMsg: this.systemMsg,
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
      return this.systemMsg + (this.content != '' ? ' 问题背景： ' + this.context : '')
        + (this.isTextNum ? ' 请在' + this.textNum + '字以内简略叙述完毕 ' : '') + (this.isSteps ? '请按步骤，分步、分点告诉我' : '');
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
    handleSendData() {
      if (this.isSend) {
        return;
      }
      if (this.sendData == '') {
        return;
      }
      this.signleQA = {
        question: '',
        answer: '',
      };
      this.isSend = true;
      this.historyList.push({
        text: this.sendData,
        role: 'user',
        timestamp: new Date().getTime(),
      });/*
      if (this.sendType == 'image') {
        let data = this.sendData;
        this.signleQA.question = this.sendData;
        let t = setTimeout(() => {
          this.historyList.push({
            role: 'gpt',
            text: '',
            timestamp: null,
            id: '',
          });
          this.handleImage(data);
          this.$nextTick(() => {
            this.$refs.historyContent.scrollTop = this.$refs.historyContent.scrollHeight;
          })
          clearTimeout(t);
        }, 200);
        this.sendData = '';
        return;
      }*/
      if (this.message.length < 1) {
        if (this.systemMsg != '') this.message.push({role: 'system', content: this.getSystem()});
      }
      this.message.push({role: 'user', content: this.sendData});
      let t = setTimeout(() => {
        this.historyList.push({
          role: 'gpt',
          text: '',
          timestamp: null,
          id: '',
        });
        this.$nextTick(() => {
          this.$refs.historyContent.scrollTop = this.$refs.historyContent.scrollHeight;
        })
        this.sendRequest();
        clearTimeout(t);
      }, 200);
      this.signleQA.question = this.sendData;
      this.sendData = '';
    },
    //发送请求，处理返回数据
    sendRequest() {
      let data = {
        //messages: this.message,
        question: this.message,
        temperature: this.temperature,
        top_p: this.top_p,
        model: this.model,
        frequency_penalty: this.frequency_penalty,
        presence_penalty: this.presence_penalty,

        max_tokens: this.max_tokens,
      }
      let index = this.historyList.length - 1;
      let url = 'gpt35';
      if (this.model === 'gpt-4') {
        url = 'gpt40';
      } else if (this.model === 'ernie-bot') {
        this.sendDataWxyy();
        return;
      }
      chatQuestion(url, data) //post请求，传递参数text
        .then(response => { //请求成功
          console.log('then')
          if (response.code === 200) {
            this.historyList[index].text = response.choices[0].message.content;
            this.historyList[index].timestamp = new Date().getTime();
            this.historyList[index].role = 'gpt';
            this.historyList[index].question = this.signleQA.question;
            this.message = response.question;
            this.total_tokens = response.usage.total_tokens;
            this.signleQA.answer = response.choices[0].message.content;
            this.handleAddHistory();
          } else if (response.code === 421) {
            console.log(421)
            this.historyList[index].text = response.msg;
            this.historyList[index].timestamp = new Date().getTime();
            this.historyList[index].role = 'gpt';
            this.historyList[index].isError = true;
            this.historyList[index].errorText = response.msg;
            this.historyList[index].question = this.signleQA.question;
            this.signleQA.answer = response.msg;
            this.total_tokens = 0;
            this.message.pop();
            this.handleAddHistory();
          } else if (response.code === 422) {
            console.log(422)
            let words = '\n问题词汇：' + response.detail[0].contexts.map(item => {
              return item.context
            }).join(';');
            this.historyList[index].text = response.msg + words;
            this.historyList[index].timestamp = new Date().getTime();
            this.historyList[index].role = 'gpt';
            this.historyList[index].isError = true;
            this.historyList[index].errorText = response.msg + words;
            this.historyList[index].question = this.signleQA.question;
            this.signleQA.answer = response.msg + words;
            this.total_tokens = 0;
            this.message.pop();
            this.handleAddHistory();
          }
        })
        .catch(error => {
          console.log('err');
          this.historyList[index].role = 'gpt';
          this.historyList[index].isError = true;
          this.historyList[index].text = 'Error: ' + error;
          this.historyList[index].timestamp = new Date().getTime();
          this.historyList[index].errorText = 'Error: ' + error;
          this.historyList[index].question = this.signleQA.question;
          this.signleQA.answer = error + '';
          this.total_tokens = 0;
          this.handleAddHistory();
        });
    },
    sendDataWxyy() {
      let question = JSON.parse(JSON.stringify(this.message));
      if (question[0].role === 'system') question.splice(0, 1);
      let data = {
        //messages: this.message,
        messages: question,
        temperature: this.temperature,
        top_p: this.top_p,
        model: this.model,
        frequency_penalty: this.frequency_penalty,
        presence_penalty: this.presence_penalty,

        max_tokens: this.max_tokens,
      }
      let index = this.historyList.length - 1;
      wxyyQuestion(data)//post请求，传递参数text
        .then(response => { //请求成功
          if (response.code === 200) {
            this.historyList[index].text = response.data.result;
            this.historyList[index].timestamp = new Date().getTime();
            this.historyList[index].role = 'gpt';
            this.historyList[index].question = this.signleQA.question;
            this.message = response.data.messages;
            this.total_tokens = response.data.usage.total_tokens;
            this.signleQA.answer = response.data.result;
            this.handleAddHistory();
          } else if (response.code === 421) {
            this.historyList[index].text = response.msg;
            this.historyList[index].timestamp = new Date().getTime();
            this.historyList[index].role = 'gpt';
            this.historyList[index].isError = true;
            this.historyList[index].errorText = response.msg;
            this.historyList[index].question = this.signleQA.question;
            this.signleQA.answer = response.msg;
            this.total_tokens = 0;
            this.message.pop();
            this.handleAddHistory();
          } else if (response.code === 422) {
            let words = '问题词汇 暂不清晰';
            if (response.detail[0].contexts){
               words = '\n问题词汇：' + response.detail[0].contexts.map(item => {
                return item.context
              }).join(';');
            }
            this.historyList[index].text = response.msg + words;
            this.historyList[index].timestamp = new Date().getTime();
            this.historyList[index].role = 'gpt';
            this.historyList[index].isError = true;
            this.historyList[index].errorText = response.msg + words;
            this.historyList[index].question = this.signleQA.question;
            this.signleQA.answer = response.msg + words;
            this.total_tokens = 0;
            this.message.pop();
            this.handleAddHistory();
          }
        })
        .catch(error => {
          this.historyList[index].role = 'gpt';
          this.historyList[index].isError = true;
          this.historyList[index].text = error + '';
          this.historyList[index].timestamp = new Date().getTime();
          this.historyList[index].errorText = 'Error: ' + error;
          this.historyList[index].question = this.signleQA.question;
          this.signleQA.answer = error + '';
          this.total_tokens = 0;
          this.handleAddHistory();
        });
    },
    handleAddHistory() {
      let data = {
        question: this.signleQA.question,
        answer: this.signleQA.answer,
        model: this.model,
        tokenUsage: this.total_tokens
      }
      addHistory(data).then(res => {
        console.log(res)
        this.historyList[this.historyList.length - 1].id = res.data;
        this.handleSaveSession();
        this.isSend = false;
      }).catch(err => {
        console.log(err);
        this.isSend = false;
      });
    },
    closeDrawer() {
      if (this.message.length < 1) {
        if (this.systemMsg != '') {
          this.message.push({role: 'system', content: this.systemMsg});
        } else {
          this.systemMsg = '你是一个喜欢使用中文的助手';
          this.message.push({role: 'system', content: '你是一个喜欢使用中文的助手'});
        }
      } else {
        this.message[0] = {role: 'system', content: this.systemMsg};
      }
    },
    handleGood(isGood, item) {
      if (item.isChecked === true) return;
      let isAccuracy = isGood ? 'yes' : 'no';
      let data = {
        question: item.question,
        answer: item.text,
        id: item.id,
        isAccuracy: isAccuracy,
      };
      updateHistory(data, isGood).then(res => {
        item.isGood = isGood;
        this.historyList.find(obj => obj.id === item.id).isGood = isGood;
        this.historyList.find(obj => obj.id === item.id).isChecked = true;
        this.onlyAddList.find(obj => obj.id === item.id).isGood = isGood;
        this.onlyAddList.find(obj => obj.id === item.id).isChecked = true;
        this.$forceUpdate();
        let session = this.sessionList.find(item => item.id === this.sessionId);
        session.history = JSON.stringify(this.onlyAddList);
      }).catch(err => {
        console.log(err);
      });
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
    handleSaveSession() {
      let list = JSON.parse(JSON.stringify(this.historyList)).reverse();
      let isNew = false;
      if (this.onlyAddList.length < 1) {
        isNew = true;
      }
      this.onlyAddList.push(list[1]);
      this.onlyAddList.push(list[0]);

      let session = this.sessionList.find(item => item.id === this.sessionId);
      session.history = JSON.stringify(this.onlyAddList);
      if (isNew) this.handleCreateSession(session.history);
    },
    handleCreateSession(list) {
      let data = {
        history: list,
      };
      addSession(data).then(res => {
        if (res.code === 200) {
          this.sessionId = res.data.id;

          this.getSessionList();
        } else {
          this.$message.error(res.msg);
        }
      }).catch(err => {
        this.$message.error(err);
      })
    },
    handleAddSession() {
      if (this.sessionId === 0) {
        this.$message.warning('您已在新会话中');
        return;
      }
      let session = this.sessionList.find(item => item.id === this.sessionId);
      session.history = JSON.stringify(this.onlyAddList);
      if (this.sessionList.find(x => x.id === this.sessionId)) {
        this.handleUpdateSession(this.sessionId, JSON.stringify(this.onlyAddList));
      }
      this.clearDialog();
      this.onlyAddList = [];
      this.sessionList.splice(0, 0, {id: 0, history: '[]'});
      this.sessionId = 0;
    },
    handleChangeSession(item, index) {
      if (this.sessionId === item.id) return;
      if (this.sessionId === 0) {
        if (this.onlyAddList.length < 1) {
          let index = this.sessionList.findIndex(item => item.id === 0);
          this.sessionList.splice(index, 1);
        }
      } else {
        if (this.sessionList.find(x => x.id === this.sessionId)) {
          this.handleUpdateSession(this.sessionId, JSON.stringify(this.onlyAddList));
        }
        let obj = this.sessionList.find(x => x.id === this.sessionId);
        obj.promptId = this.roleId + '';
        this.handleSelectRole(Number(item.promptId || '0'));
      }
      this.clearDialog();
      let list = item.history;
      this.onlyAddList = JSON.parse(list);
      this.historyList = JSON.parse(list);
      this.formatterMessage();
      this.sessionId = item.id;
    },
    handleDeleteSession(item, index) {
      if (item.id === 0) {
        if (this.sessionList.length < 2) {
          this.$message.warning('当前为唯一对话，不可删除');
        } else {
          this.sessionList.splice(index, 1);
          this.sessionId = this.sessionList[index].id;
          let list = this.sessionList[index].history;
          this.onlyAddList = JSON.parse(list);
          this.historyList = JSON.parse(list);
        }
        return;
      }
      this.$confirm('您确定要删除此对话吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSession(item.id).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.getSessionList(true, index);
          } else {
            this.$message.error(res.msg);
          }
        }).catch(err => {
          this.$message.error(err);
        })
      }).catch(err => {
      })
    },
    handleUpdateSession(id, listString) {
      if (id === 0) return;
      updateSession({id: id, history: listString, promptId: this.roleId + ''}).then(res => {
        if (res.code === 200) {
        } else {
          this.$message.error(res.msg);
        }
      }).catch(err => {
        this.$message.error(err);
      })
    },
    getName(item) {
      let history = JSON.parse(item.history);
      if (history.length < 1) {
        return '';
      } else {
        return history[0].text;
      }
    },
    async saveData() {
      await this.handleUpdateSession(this.sessionId, JSON.stringify(this.onlyAddList));
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
    }
  },
  //销毁定时器
  beforeDestroy() {
    clearInterval(this.waitingAnTimer);
    this.waitingAnTimer = null;
    this.saveData()
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
  min-height: 40px;
  margin-bottom: 3%;
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
