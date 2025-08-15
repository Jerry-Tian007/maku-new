<template>
  <el-card style="width: 100%;height:100%;position: relative;border-radius: 10px;">
    <div slot="header" style="display: flex;align-items: center;justify-content: space-between;height: 14px">
      <div style="min-width: 110px;width:fit-content;display: flex;align-items: center;">
        <img width="18" height="18" style="border-radius: 10px;pointer-events: none" :src="modelImgUrl"/>
        <el-dropdown @command="handleCommand" trigger="hover" style="margin-left: 10px;padding-top: 1px;">
        <span class="el-dropdown-link" style="cursor: pointer;">
          {{ modelName }}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              v-for="item in modelList"
              :key="item.value"
              :command="item.value">
              <div style="display: flex;align-items: center;">
                <!--
                <svg
                  width="14"
                  height="14"
                  viewBox="0 0 41 41"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M37.5324 16.8707C37.9808 15.5241 38.1363 14.0974 37.9886 12.6859C37.8409 11.2744 37.3934 9.91076 36.676 8.68622C35.6126 6.83404 33.9882 5.3676 32.0373 4.4985C30.0864 3.62941 27.9098 3.40259 25.8215 3.85078C24.8796 2.7893 23.7219 1.94125 22.4257 1.36341C21.1295 0.785575 19.7249 0.491269 18.3058 0.500197C16.1708 0.495044 14.0893 1.16803 12.3614 2.42214C10.6335 3.67624 9.34853 5.44666 8.6917 7.47815C7.30085 7.76286 5.98686 8.3414 4.8377 9.17505C3.68854 10.0087 2.73073 11.0782 2.02839 12.312C0.956464 14.1591 0.498905 16.2988 0.721698 18.4228C0.944492 20.5467 1.83612 22.5449 3.268 24.1293C2.81966 25.4759 2.66413 26.9026 2.81182 28.3141C2.95951 29.7256 3.40701 31.0892 4.12437 32.3138C5.18791 34.1659 6.8123 35.6322 8.76321 36.5013C10.7141 37.3704 12.8907 37.5973 14.9789 37.1492C15.9208 38.2107 17.0786 39.0587 18.3747 39.6366C19.6709 40.2144 21.0755 40.5087 22.4946 40.4998C24.6307 40.5054 26.7133 39.8321 28.4418 38.5772C30.1704 37.3223 31.4556 35.5506 32.1119 33.5179C33.5027 33.2332 34.8167 32.6547 35.9659 31.821C37.115 30.9874 38.0728 29.9178 38.7752 28.684C39.8458 26.8371 40.3023 24.6979 40.0789 22.5748C39.8556 20.4517 38.9639 18.4544 37.5324 16.8707ZM22.4978 37.8849C20.7443 37.8874 19.0459 37.2733 17.6994 36.1501C17.7601 36.117 17.8666 36.0586 17.936 36.0161L25.9004 31.4156C26.1003 31.3019 26.2663 31.137 26.3813 30.9378C26.4964 30.7386 26.5563 30.5124 26.5549 30.2825V19.0542L29.9213 20.998C29.9389 21.0068 29.9541 21.0198 29.9656 21.0359C29.977 21.052 29.9842 21.0707 29.9867 21.0902V30.3889C29.9842 32.375 29.1946 34.2791 27.7909 35.6841C26.3872 37.0892 24.4838 37.8806 22.4978 37.8849ZM6.39227 31.0064C5.51397 29.4888 5.19742 27.7107 5.49804 25.9832C5.55718 26.0187 5.66048 26.0818 5.73461 26.1244L13.699 30.7248C13.8975 30.8408 14.1233 30.902 14.3532 30.902C14.583 30.902 14.8088 30.8408 15.0073 30.7248L24.731 25.1103V28.9979C24.7321 29.0177 24.7283 29.0376 24.7199 29.0556C24.7115 29.0736 24.6988 29.0893 24.6829 29.1012L16.6317 33.7497C14.9096 34.7416 12.8643 35.0097 10.9447 34.4954C9.02506 33.9811 7.38785 32.7263 6.39227 31.0064ZM4.29707 13.6194C5.17156 12.0998 6.55279 10.9364 8.19885 10.3327C8.19885 10.4013 8.19491 10.5228 8.19491 10.6071V19.808C8.19351 20.0378 8.25334 20.2638 8.36823 20.4629C8.48312 20.6619 8.64893 20.8267 8.84863 20.9404L18.5723 26.5542L15.206 28.4979C15.1894 28.5089 15.1703 28.5155 15.1505 28.5173C15.1307 28.5191 15.1107 28.516 15.0924 28.5082L7.04046 23.8557C5.32135 22.8601 4.06716 21.2235 3.55289 19.3046C3.03862 17.3858 3.30624 15.3413 4.29707 13.6194ZM31.955 20.0556L22.2312 14.4411L25.5976 12.4981C25.6142 12.4872 25.6333 12.4805 25.6531 12.4787C25.6729 12.4769 25.6928 12.4801 25.7111 12.4879L33.7631 17.1364C34.9967 17.849 36.0017 18.8982 36.6606 20.1613C37.3194 21.4244 37.6047 22.849 37.4832 24.2684C37.3617 25.6878 36.8382 27.0432 35.9743 28.1759C35.1103 29.3086 33.9415 30.1717 32.6047 30.6641C32.6047 30.5947 32.6047 30.4733 32.6047 30.3889V21.188C32.6066 20.9586 32.5474 20.7328 32.4332 20.5338C32.319 20.3348 32.154 20.1698 31.955 20.0556ZM35.3055 15.0128C35.2464 14.9765 35.1431 14.9142 35.069 14.8717L27.1045 10.2712C26.906 10.1554 26.6803 10.0943 26.4504 10.0943C26.2206 10.0943 25.9948 10.1554 25.7963 10.2712L16.0726 15.8858V11.9982C16.0715 11.9783 16.0753 11.9585 16.0837 11.9405C16.0921 11.9225 16.1048 11.9068 16.1207 11.8949L24.1719 7.25025C25.4053 6.53903 26.8158 6.19376 28.2383 6.25482C29.6608 6.31589 31.0364 6.78077 32.2044 7.59508C33.3723 8.40939 34.2842 9.53945 34.8334 10.8531C35.3826 12.1667 35.5464 13.6095 35.3055 15.0128ZM14.2424 21.9419L10.8752 19.9981C10.8576 19.9893 10.8423 19.9763 10.8309 19.9602C10.8195 19.9441 10.8122 19.9254 10.8098 19.9058V10.6071C10.8107 9.18295 11.2173 7.78848 11.9819 6.58696C12.7466 5.38544 13.8377 4.42659 15.1275 3.82264C16.4173 3.21869 17.8524 2.99464 19.2649 3.1767C20.6775 3.35876 22.0089 3.93941 23.1034 4.85067C23.0427 4.88379 22.937 4.94215 22.8668 4.98473L14.9024 9.58517C14.7025 9.69878 14.5366 9.86356 14.4215 10.0626C14.3065 10.2616 14.2466 10.4877 14.2479 10.7175L14.2424 21.9419ZM16.071 17.9991L20.4018 15.4978L24.7325 17.9975V22.9985L20.4018 25.4983L16.071 22.9985V17.9991Z"
                    fill="#606266"
                  ></path>
                </svg>-->
                <div style="margin-left: 8px;display: flex;align-items: center">
                  <img width="14" height="14" style="border-radius: 10px;" :src="item.imgUrl"/>
                  <span style="margin-left: 8px;">{{ item.label }}</span></div>
              </div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div>
        <el-button
          v-show="modelNetwork"
          :style="'font-size: 14px;color:'+(isOnline ? '#5f96dc' : '#C0C4CC')"
          type="text"
          @click="isOnline = !isOnline;">
          <div style="display: flex;align-items: center;">
            <i :class="isOnline ? 'el-icon-circle-check' : 'el-icon-circle-close'" style="font-size: 16px;"/>
            <span style="font-size: 14px;margin-left: 4px;margin-bottom: 1px;">{{isOnline ? '已联网' : '未联网'}}</span>
          </div>
        </el-button>
      </div>
      <div style="min-width: 110px;" align="right">
        <el-tooltip effect="light" content="输入框">
          <el-button :style="'font-size: 20px;color:'+(isEditing? '#5f96dc' : '#909399')" icon="el-icon-edit"
                     type="text"
                     @click="handleEdit">
          </el-button>
        </el-tooltip>
        <el-tooltip effect="light" content="清空对话">
          <el-button style="font-size: 20px;color:#909399" icon="el-icon-delete" type="text"
                     @click="handleClearHistory">
          </el-button>
        </el-tooltip>
        <el-tooltip effect="light" content="对话历史">
          <el-button style="font-size: 20px;color:#909399" icon="el-icon-time" type="text" @click="handleHistory">
          </el-button>
        </el-tooltip>
      </div>
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
        :style="'padding:12px;'"
        :align="item.role=='user' ? 'right' : 'left'"
      >
        <el-card
          class="card"
          align="left"
          :style="(item.role=='user' ? 'background-color: #d8e8ff;' : '')"
        >
          <div
            v-if="item.role=='gpt'"
            slot="header"
            class="card-header"
          >
            <div>
              <svg
                style="margin: 4px;"
                width="18"
                height="18"
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
                  <i class="el-icon-copy-document" style="font-weight: 600;font-size: 14px"/>
                </div>
              </el-tooltip>
              <template v-if="model.startsWith('gpt')">
                <div
                  @click="handleGood(true,item)"
                  style="cursor: pointer;margin-right:10px; "
                >
                  <svg
                    v-if="!item.isChecked"
                    width="16"
                    height="16"
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
                    width="16"
                    height="16"
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
                    width="16"
                    height="16"
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
                    width="16"
                    height="16"
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
              </template>
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
    <div :class="'online-button '+ (isOnline ? 'online-button-show' : '')">
      <el-tooltip effect="light" content="已联网">
        <i class="el-icon-upload" style="font-size: 18px;"/>
      </el-tooltip>
    </div>
    <div
      @blur="handleBlur"
      :class="'dialog-input '+(isEditing ? 'dialog-input-show' : '')"
      align="center">
      <div style="flex:1;">
        <el-input
          resize="none"
          type="textarea"
          v-model="sendDataLocal"
          :rows="1"
          @keydown.native="keydownfnLocal"
          placeholder="请输入对话内容..."
        />
      </div>
      <div
        :style="'width:60px;padding-top:2px;'+ (sendDataLocal === ''  || !isEditing ? 'pointer-event:none;' : 'cursor:pointer')"
      >
        <svg
          width="20"
          height="20"
          viewBox="0 0 52 52"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          @click="sendDataLocal === '' ? '' : handleSemdDataLocal()"
        >
          <path
            d="M48.1857 2.01674C47.8845 1.76668 47.5185 1.60736 47.1302 1.55736C46.742 1.50735 46.3475 1.56871 45.9928 1.73429L3.42639 21.7141V25.5802L21.3051 32.7316L32.7732 50.375H36.6401L48.858 4.12345C48.9571 3.74467 48.9465 3.34555 48.8275 2.97257C48.7084 2.59958 48.4859 2.2681 48.1857 2.01674ZM34.2526 46.6876L24.3953 31.522L39.564 14.9082L37.1639 12.7169L21.8765 29.4601L7.13545 23.5637L45.0633 5.76064L34.2526 46.6876Z"
            :fill="sendDataLocal === '' ? '#aaaaaa' : '#2D9CDB'"
          />
        </svg>
      </div>
    </div>

    <el-dialog title="历史记录" class="history-dialog" :visible.sync="showHistory" width="40%" append-to-body
               :show-close="false">
      <div slot="title" style="display: flex;align-items: center;justify-content: space-between">
        <span>历史记录</span>
        <el-radio-group v-model="showType" size="mini">
          <el-radio-button label="list">列表</el-radio-button>
          <el-radio-button label="md">Markdown</el-radio-button>
        </el-radio-group>
      </div>
      <div v-show="showType === 'md' && historyList.length > 0" style="position:relative;">
        <el-tooltip effect="light" content="下载">
          <div class="md-download" @click="handleDownloadMD">
            <i class="el-icon-download" style="font-size: 20px;"/>
          </div>
        </el-tooltip>
      </div>
      <div style="width:100%;max-height: 400px;overflow-y: auto;padding-bottom: 20px;" align="center">
        <el-empty :image-size="70" v-if="historyList.length < 1"/>
        <template v-if="showType === 'list'">
          <div
            v-for="(item,index) in historyList"
            :key="index"
            :style="(item.role === 'user' ? 'padding:12px 24px;background:#F2F6FC' : 'padding:12px 24px;')"
          >
            <div
              style="margin-bottom: 16px;font-size: 14px;color: #909399;display: flex;align-items: center;justify-content: space-between;">
              <span>{{ item.role === 'user' ? '你' : 'AI Bot' }}</span>
              <span>{{ parseTime(item.timestamp) }}</span>
            </div>
            <div style="font-size: 16px;color: #606266;">
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
              />
            </div>
          </div>
        </template>
        <div v-show="showType === 'md' && historyList.length > 0" style="position:relative;">
<!--          <mavon-editor
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
            v-model="historyMd"
            :subfield="false"
            defaultOpen="preview"
            :toolbarsFlag="false"
          />-->
          <div class="md-show">
            {{historyMd}}
          </div>
        </div>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import {chatQuestion, wxyyQuestion} from "@/api/chatGPT/chat";
import {addHistory, updateHistory} from "@/api/chatGPT/history";
import {xfxhQuestion} from "../../../api/chatGPT/chat";
import {toneStyleDetail} from "../../../api/chatGPT/promptlibrary";

export default {
  name: "Bot",
  props: {
    systemPrompt: {
      type: String,
      default: '你是一个喜欢使用中文的助手。',
    },
    tone:  {
      type: String,
      default: '普通的',
    },
    sysStyle:  {
      type: String,
      default: '普通对话',
    },
    index: {
      type: Number,
      default: 0,
    },
    length: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      showHistory: false,
      isEditing: false,
      isOnline: false,
      showType: 'list',
      historyMd: '',

      modelImgUrl: '',
      modelName: 'GPT-3.5',
      model: 'gpt-35-turbo',
      modelNetwork: true,
      modelList: [
      ],
      max_token_max: 8000,
      max_tokens: 4096,
      temperature: 1,
      top_p: 1,
      frequency_penalty: 0,
      presence_penalty: 0,
      total_tokens: 0,
      stop: ["Human:", "AI:"],

      historyList: [],

      waitingText: '请等待',
      signleQA: {
        question: '',
        answer: '',
      },
      message: [],
      //systemPrompt: '你是一个喜欢使用中文的助手',
      sendData: '',
      sendDataLocal: '',

    };
  },
  watch: {
    systemPrompt: function(oldVal, newVal){
      this.handleGetToneStyleDetail();
    }
  },
  mounted() {
    this.initModel();
    if (this.index > this.modelList.length - 1) {
      this.model = this.modelList[this.modelList.length - 1].value;
    } else {
      this.model = this.modelList[this.index].value;
    }
    let obj = this.modelList.find(item => item.value === this.model);
    this.modelName = obj.label;
    this.modelImgUrl = obj.imgUrl;
    this.modelNetwork = obj.network;
    this.handleGetToneStyleDetail();
    this.delayAn();
  },
  methods: {
    initModel(){
      if(this.$auth.hasPermi('ny:gpt35')){
        this.modelList.push(
          {
            label: 'GPT-3.5',
            value: 'gpt-35-turbo',
            imgUrl: require('@/assets/images/gpt.png'),
            network: true,
          });
      }
      if(this.$auth.hasPermi('ny:gpt40')){
        this.modelList.push(

          {
            label: 'GPT-4',
            value: 'gpt-4',
            imgUrl: require('@/assets/images/gpt.png'),
            network: true,
          });
      }
      if(this.$auth.hasPermi('ny:wxyy')){
        this.modelList.push(
          {
            label: '文心一言',
            value: 'ernie-bot',
            imgUrl: require('@/assets/images/wxyy.jpg'),
            network: false,
          });
      }
      if(this.$auth.hasPermi('ny:xfxh')){
        this.modelList.push(
          {
            label: '讯飞星火',
            value: '讯飞星火',
            imgUrl: require('@/assets/images/xfxh.jpg'),
            network: false,
          });
      }
    },
    handleGetToneStyleDetail(){
      toneStyleDetail({ model: this.model, tone: this.tone, style: this.sysStyle }).then(res => {
        if(res.code === 200){
          this.temperature = Number(res.data.temperature || '0.7');
        } else{
          this.$message.error(res.msg);
          console.log(res.msg);
        }
      }).catch(err => {
        console.log(err);
        this.$message.error(err);
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
          this.waitingText = '请等待';
        }
      }, 300);
    },
    handleCommand(command) {
      this.model = command;
      let obj = this.modelList.find(item => item.value === this.model);
      this.modelName = obj.label;
      this.modelImgUrl = obj.imgUrl;
      this.modelNetwork = obj.network;
      this.handleGetToneStyleDetail();
      if(!this.modelNetwork){
        this.isOnline = false;
      }
      this.message = [];
      this.historyList = [];
      this.signleQA = {
        question: '',
        answer: '',
      };
    },

    handleHistory() {
      this.historyMd = this.history2Markdown(this.historyList);
      this.showType = 'list';
      this.showHistory = true;
    },
    handleClearHistory() {
      this.$confirm('确认情况当前对话历史？',
        '',
        {confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'}
      ).then(() => {
        this.message = [];
        this.historyList = [];
        this.signleQA = {
          question: '',
          answer: '',
        };
      }).catch(() => {
      });
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

    getSystem() {
      return this.systemPrompt;
    },

    //点击确定
    handleSendData() {
      this.signleQA = {
        question: '',
        answer: '',
      };
      this.historyList.push({
        text: this.sendData,
        role: 'user',
        timestamp: new Date().getTime(),
      });
      if (this.message.length < 1) {
        if (this.systemMsg != '') this.message.push({role: 'system', content: this.getSystem()});
      } else{
        this.message[0].content = this.getSystem();
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
        });
        this.switchRequestType();
        clearTimeout(t);
      }, 200);
      this.signleQA.question = this.sendData;
      this.sendData = '';
    },
    switchRequestType() {
      switch (this.model) {
        case 'gpt-35-turbo':
          this.sendRequest();
          break;
        case 'gpt-4':
          this.sendRequest();
          break;
        case 'ernie-bot':
          this.sendDataWxyy();
          break
        case '讯飞星火':
          this.sendDataXunFei();
          break
      }
    },
    //发送请求，处理返回数据
    sendRequest() {
      let data = {
        question: this.message,
        temperature: this.temperature,
        top_p: this.top_p,
        model: this.model,
        frequency_penalty: this.frequency_penalty,
        presence_penalty: this.presence_penalty,
        network: this.isOnline,

        max_tokens: this.max_tokens,
      }
      let index = this.historyList.length - 1;
      let url = 'gpt35';
      if (this.model === 'gpt-4') {
        url = 'gpt40';
      }

      chatQuestion(url, data) //post请求，传递参数text
        .then(response => { //请求成功
          console.log('then')
          if (response.code === 200) {
            this.historyList[index].text = response.choices[0].message.content;
            this.historyList[index].timestamp = new Date().getTime();
            this.historyList[index].role = 'gpt';
            this.historyList[index].question = this.signleQA.question;
            if(this.isOnline){
              this.message = response.question.filter(item => item.role !== 'function' && !item.name);
            } else {
              this.message = response.question;
            }
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
            let words = '\n问题词汇 暂不清晰';
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
            let words = '\n问题词汇 暂不清晰';
            if (response.detail[0].contexts) {
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
    sendDataXunFei() {
      let question = JSON.parse(JSON.stringify(this.message));
      // if (question[0].role === 'system') question.splice(0, 1);
      console.log(question)
      let data = {
        "parameter": {
          "chat": {
            "domain": "general",
            "random_threshold": 0.5,
            "max_tokens": 2048,
            "auditing": "default"
          }
        },
        "payload": {
          "message": {
            "text": question,
          }
        }
      }
      let index = this.historyList.length - 1;
      // axios.post(process.env.VUE_APP_DOMESTIC_API + '/get_answer', data)//post请求，传递参数text
      xfxhQuestion(data)
        .then(response => { //请求成功
          this.historyList[index].text = response.message.text.reverse()[0].content;
          this.historyList[index].timestamp = new Date().getTime();
          this.historyList[index].role = 'gpt';
          this.historyList[index].question = this.signleQA.question;
          this.message = response.message.text;
          this.total_tokens = response.usage.text.total_tokens;
          this.signleQA.answer = response.message.text.reverse()[0].content;
          this.handleAddHistory();
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
        tokenUsage: this.total_tokens,
        network: this.isOnline,
      }
      addHistory(data).then(res => {
        this.historyList[this.historyList.length - 1].id = res.data;
      }).catch(err => {
        console.log(err);
      });
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
        this.$forceUpdate();
      }).catch(err => {
        console.log(err);
      });
    },
    handleEdit() {
      this.isEditing = !this.isEditing;
    },
    handleSemdDataLocal() {
      this.sendData = this.sendDataLocal;
      this.sendDataLocal = '';
      this.handleSendData();
    },
    keydownfnLocal(event) {
      if (event.keyCode == 13) {
        if (!event.shiftKey && !event.ctrlKey && !event.altKey && !event.metaKey) {
          event.preventDefault();
          this.handleSemdDataLocal();
        } else if (!event.shiftKey) {
          this.sendDataLocal = this.sendDataLocal + '\n'
        }
      }
    },
    handleBlur() {
      console.log('blur')
    },
    history2Markdown(history) {
      let result = '';
      history.forEach(item => {
        if (item.role === 'user') {
          result += '## User\n' + item.text + '\n\n';
        } else {
          result += '## Bot\n' + item.text + '\n\n';
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
          result += '## User\n' + item.text + '\n\n';
        } else {
          result += '## Bot\n' + item.text + '\n\n';
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
  }
};
</script>

<style scoped lang="scss">
.md-show{
  background-color: #fafafa;
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
  text-align: left;
  white-space: pre-wrap;
  margin: 16px;
  padding: 4px;
}

.dialog-content {
  flex: 1;
  width: calc(100% - 40px);
  position: absolute;
  height: calc(100% - 75px);
  overflow: scroll;
}

.dialog-input {
  width: 100%;
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  border: 1px solid #FFFFFF;
  border-radius: 0 0 10px 10px;
  box-shadow: 0 -1px 1px #00000024, 0 0 2px #0000001f;
  overflow: hidden;
  height: 0;
  opacity: 0;
  background: #fff;
  transition: all 0.3s ease-in-out;
}

.dialog-input-show {
  height: 40px;
  opacity: 1;
  z-index: 2000;
  border: 1px solid #FFFFFF;
  transition: border 0.3s;
}

.dialog-input-show:focus-within{
  border: 1px solid #74bce120;
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
  border: 1px solid #909399;

  ::v-deep .el-card__header {
    padding: 4px 6px;
    min-height: 10px;
  }
}

.card-text {
  width: 100%;
  font-size: 16px;
  padding-top: 6px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
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
  background-color: #EBEEF580;
}

.history-dialog {
  ::v-deep .el-dialog {
    border-radius: 10px;
  }

  ::v-deep .el-dialog__body {
    padding: 10px 0px 0px 0px;
  }
}

.md-download {
  position: absolute;
  right: 24px;
  top: 20px;
  z-index: 3000;
  color: #606266;
  cursor: pointer;
  transition: all 0.3s;
}

.md-download:hover {
  color: #1c84c6;
  text-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
}

::v-deep .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  background-color: #88beef;
  border-color: #88beef;
  box-shadow: -1px 0 0 0 #88beef;
}

::v-deep .el-radio-button:last-child .el-radio-button__inner {
  border-radius: 0 12px 12px 0;
}

::v-deep .el-radio-button:first-child .el-radio-button__inner {
  border-radius: 12px 0 0 12px;
}

.online-button {
  position: absolute;
  left: 14px;
  top: 64px;
  display: flex;
  align-items: center;
  color: #4e9dcf;
  opacity: 0;
  transition: opacity 0.2s ease-in-out;
}

.online-button-show {
  opacity: 1;
}

</style>
