<template>
  <div class="back">
    <div
      class="color-background"
      style="position: absolute;width:100%;height:100%;"
    ></div>
    <div class="background">
      <div class="chat-background">
        <div class="content-card">
          <div style="padding-bottom:20px;">
            <el-form
              size="small"
              :inline="true"
              :model="queryParams"
              label-width="100px"
            >
              <el-row>
                <el-col :span="8" :lg="8" :md="8" :sm="12" :xs="24">
                  <el-form-item label="国家">
                    <el-select
                      v-model="queryParams.region"
                      multiple
                      placeholder="选择国家"
                      clearable
                    >
                      <el-option
                        v-for="(item,index) in areaOptions"
                        :key="index"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="8" :sm="12" :xs="24">
                  <el-form-item label="用户">
                    <el-select
                      v-model="queryParams.role"
                      multiple
                      placeholder="选择用户"
                      clearable
                    >
                      <el-option
                        v-for="(item,index) in rolePermissionOptions"
                        :key="index"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="8" :sm="12" :xs="24">
                  <el-form-item label="提问">
                    <el-input
                      v-model="queryParams.question"
                      placeholder="请输入提问内容"
                      clearable
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="8" :sm="12" :xs="24">
                  <el-form-item label="回答">
                    <el-input
                      v-model="queryParams.answer"
                      placeholder="请输入回答内容"
                      clearable
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="8" :sm="12" :xs="24">
                  <el-form-item label="准确性评价">
                    <el-select
                      v-model="queryParams.isAccuracy"
                      placeholder="请选择准确性评价"
                      clearable
                    >
                      <el-option
                        label="点赞"
                        value="yes"/>
                      <el-option
                        label="点踩"
                        value="no"/>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="8" :sm="12" :xs="24">
                  <el-form-item label="状态" v-if="false">
                    <el-select
                      v-model="queryParams.isSolve"
                      placeholder="请选择解决状态"
                      clearable

                    >
                      <el-option
                        v-for="(item,index) in statusOptions"
                        :key="index"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8" :lg="8" :md="8" :sm="12" :xs="24">
                  <el-form-item label="提问人">
                    <el-input
                      v-model="queryParams.createBy"
                      placeholder="请输入提问人"
                      clearable
                    ></el-input>
                  </el-form-item>
                </el-col>
                <!--                <el-col :span="8" :lg="8" :md="8" :sm="12" :xs="24">-->
                <!--                  <el-form-item label="id">-->
                <!--                    <el-input-->
                <!--                      v-model="queryParams.id"-->
                <!--                      placeholder="请输入id"-->
                <!--                    ></el-input>-->
                <!--                  </el-form-item>-->
                <!--                </el-col>-->

                <el-col :span="12" :lg="12" :md="12" :sm="10" :xs="24" style="height:40px;" v-if="false">
                  <el-form-item label="时间">
                    <el-date-picker
                      v-model="createTime"
                      type="datetimerange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      align="left"
                      @change="handleSelectTime"
                    >
                    </el-date-picker>
                  </el-form-item>
                </el-col>

              </el-row>
              <el-form-item label="" style="margin-left: 75%">
                <el-button
                  type="primary"
                  @click="handleQuery"
                  v-hasPermi="['gpt:kbqarecord:query']"
                >查询
                </el-button>
                <el-button @click="handleReset">重置</el-button>
                <el-button
                  type="warning"
                  @click="handleExport"
                >导出
                </el-button>
              </el-form-item>
            </el-form>
          </div>

          <el-table
            style="width: 100%;flex: 1"
            :data="tableData"
            :row-style="{height:'60px',backgroundColor:'#ffffff80'}"
            height="100%">
            <el-table-column
              type="index"
              label="序号"
              width="55"
              align="center"
            />
            <el-table-column
              prop="question"
              label="提问"
              align="center"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="answer"
              label="回答"
              align="center"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="createTime"
              label="时间"
              align="center"
            >
            </el-table-column>
            <el-table-column
              prop="isAccuracy"
              label="准确性评价"
              align="center"
            >
              <template slot-scope="scope">
                <el-tag
                  v-if="scope.row.isAccuracy=='yes'"
                  type="yes"
                >点赞
                </el-tag>
                <el-tag
                  v-else-if="scope.row.isAccuracy=='no'"
                  type="no"
                >点踩
                </el-tag>
                <div v-else>-</div>
              </template>
            </el-table-column>
<!--            <el-table-column-->
<!--              prop="isSolve"-->
<!--              label="解决状态"-->
<!--              align="center"-->
<!--            >-->
<!--              <template slot-scope="scope" align="enter">-->
<!--                <div-->
<!--                  style="position: absolute;left:0;right:0;top:0;bottom:0;margin: auto;width: fit-content;height: fit-content;padding: 6px 8px;">-->
<!--                  <el-tag-->
<!--                    v-if="scope.row.isSolve"-->
<!--                    style="font-size:14px;"-->
<!--                    :type="scope.row.isSolve === 'solved' ? 'solved'-->
<!--                  : scope.row.isSolve === 'unsolved' ? 'primary'-->
<!--                  : scope.row.isSolve === 'solved' ? 'chatting'-->
<!--                  : scope.row.isSolve === 'unsolved' ? 'chatting' : 'info'">-->
<!--                    {{ statusOptions.find(item => item.value === scope.row.isSolve).label }}-->
<!--                    <i class="el-icon-loading" style="font-size:16px;" v-if="scope.row.isSolve === 'chatting'"/>-->
<!--                  </el-tag>-->
<!--                  <span v-else> - </span>-->
<!--                  <div style="position: absolute;right:0;top:0;">-->
<!--                    <el-tooltip v-if="scope.row.isSolve === 'unsolved'" effect="light"-->
<!--                                :content="scope.row.trainFlow || '-'" placement="top">-->
<!--                      <i class="el-icon-warning" style="font-size: 18px;color: #f3c27a"/>-->
<!--                    </el-tooltip>-->
<!--                  </div>-->
<!--                </div>-->
<!--              </template>-->
<!--            </el-table-column>-->
            <el-table-column
              prop="createBy"
              label="提问人"
              align="center"
            >
            </el-table-column>

            <el-table-column
              prop="region"
              label="国家"
              align="center"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column
              prop="role"
              label="角色"
              align="center"
              show-overflow-tooltip
            >
            </el-table-column>

            <el-table-column
              label="操作"
              width="120"
              align="center"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="primary"
                  plain
                  @click="handleView(scope.row)"

                >查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div
            style="display: flex;flex-direction: column;align-items: flex-end;margin-top: 20px;"
          >
            <el-pagination
              background
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="queryParams.pageNum"
              :page-sizes="[10, 20, 30, 40]"
              :page-size="queryParams.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
            >
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      title="对话详情"
      :visible.sync="dialogVisible"
      width="60%"
      class="detail"
      style="background-color: rgba(255,255,255,0.36)"
    >
      <div
        style="margin: 0 25px 50px 25px;"
        id="chatBox"
        ref="historyContent"
      >
        <!--对话列表-->
        <div
          v-for="(item,index) in singleList"
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
            </div>

            <!--文本内容-->
            <div
              class="card-text"
              :style="'min-width:'+(item.role=='gpt'?'100px;' : '20px;')"
            >
              <!-- content-->
              <mavon-editor
                style="
              border: none;
              box-shadow: none;
              min-height: 0px;
              background: #ffffff00;
              background-color: #ffffff00;
              flex: 5;
              font-size: 16px;
              font-weight: 400;
              line-height: 24px;
              padding: 0px;
              min-width: 20px;
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

            <template v-if="item.role==='gpt' && item.text">
              <el-divider content-position="left">
              </el-divider>
              <div style="margin-bottom: 2px;font-size:14px;color: #999093;">
                以上内容供参考
              </div>
            </template>
            </div>
          </el-card>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listDept} from "@/api/system/dept";
import user from '@/store/modules/user'
import { exportHistory, listKBHistory } from '@/api/data/history'



export default {
  name: 'HistoryList',
  data() {
    return {
      dialogVisible: false,
      isAdmin: false,
      deptOptions: [],
      singleList: [],
      tableData: [],
      total: 0,

      queryParams: {
        pageSize: 10,
        pageNum: 1,
        id: null,
        deptId: null,
        createBy: null,
        startTime: null,
        endTime: null,
        question: null,
        answer: null,
        role:[],
        region:[],

      },
      createTime: null,
      activeIndex: -1,
      activeName: '',
      iframeUrl: '',
      contentList: [],
      isNotPdf: true,
      isShow: false,

      //国家
      areaOptions: [
        {
          label: 'China',
          value: 'China'
        },
        {
          label: 'Germany',
          value: 'Germany'
        },
        {
          label: 'Italy',
          value: 'Italy'
        },
        {
          label: 'Japan',
          value: 'Japan'
        },
        {
          label: 'Netherlands',
          value: 'Netherlands'
        },
        {
          label: 'Spain',
          value: 'Spain'
        },
        {
          label: 'United Kingdom',
          value: 'United Kingdom'
        },

      ],
      //用户
      rolePermissionOptions: [
        {
          label: '服务伙伴',
          value: 'servicepartner'
        },
        {
          label: '公司管理员',
          value: 'systemmanager'
        },
        {
          label: '国内售后',
          value: 'dasw'
        },
        {
          label: '安装商',
          value: 'installer'
        },
        {
          label: '南通生产',
          value: 'sncreator'
        },
        {
          label: '国外售后',
          value: 'servicer'
        },
        {
          label: '终端用户',
          value:'customer'
        },
        {
          label: '分销商',
          value: 'reseller'
        },
        {
          label: '分享用户',
          value: 'sharer'
        },
        {
          label: '供应商',
          value: 'supplier'
        },
        {
          label: '投资者',
          value: 'investor'
        },
        {
          label: '订单角色',
          value: 'oflduser'
        },
      ],
      //状态
      // statusOptions: [
      //   {
      //     label: '已解决',
      //     value: 'solved'
      //   },
      //   {
      //     label: '未解决',
      //     value: 'unsolved'
      //   },
      //   {
      //     label: '转人工',
      //     value: 'chatting'
      //   },
      // ],
    };
  },
  mounted() {

    this.getData();
  },
  methods: {
    getRowKey(row) {
      return row.id;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    selectable(row, index) {
      return row.isSolve !== 'training';
    },

    getData() {
      this.loading = true;
      var queryParams = JSON.parse(JSON.stringify(this.queryParams));
      if(this.queryParams.role){ queryParams.role = this.queryParams.role.join(";");}
      if(this.queryParams.region){queryParams.region = this.queryParams.region.join(";");}
      listKBHistory(queryParams).then(response => {
        if (response.code === 200) {
          this.tableData = response.rows.slice().reverse();
          this.total = response.total;
          this.loading = false;
        } else {
          this.$message.error(response.msg);
        }
      }).catch(error => {
        console.log(error);
      });
    },
    handleSelectTime(val) {
      this.queryParams.startTime = val[0];
      this.queryParams.endTime = val[1];
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val;
      this.getData();
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val;
      this.getData();
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      // var queryParams = JSON.parse(JSON.stringify(this.queryParams));
      this.getData();
    },
    handleExport(){
      let dateTime = new Date().getTime();
      const data={
        role : this.queryParams.role.join(";"),
        region : this.queryParams.region.join(";"),
      }
      // if(this.queryParams.role){ this.queryParams.role = this.queryParams.role.join(";");}
      // if(this.queryParams.region){this.queryParams.region = this.queryParams.region.join(";");}
      this.download(
        "gpt/kbqarecord/export",
        {
          ...this.queryParams,
          ...data,
        },
        `QARecord-${dateTime}.xlsx`
      );
    },
    handleReset() {
      this.queryParams = {
        pageSize: 10,
        pageNum: 1,
        deptId: null,
        id: null,
        createBy: null,
        startTime: null,
        endTime: null,
        question: null,
        answer: null,
        role:[],
        region:[],
        isAccuracy:null,
        ramark: this.$route.query.type
      };
      this.createTime = null;
      this.getData();
    },
    handleView(row) {
      this.singleList = [];
      this.singleList.push({
        role: 'user',
        text: row.question,
        timestamp: new Date(row.createTime),
      });
      this.singleList.push({
        role: 'gpt',
        text: row.answer,
        timestamp: new Date(row.createTime),
      });
      this.dialogVisible = true;
    },
  },
  //销毁定时器
  beforeDestroy() {
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

.title {
  font-size: 24px;
  font-weight: 600;
  color: #0b7191;
}

.chat-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #ffffff40;
}

.content-card {
  width: 90%;
  height: 90%;
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: auto;
  padding: 30px;
  background-color: #ffffff40;
  border: 1px solid #ffffffc0;
  box-shadow: 0 2px 4px #00000024, 0 0 2px #0000001f;
  border-radius: 10px;
}

#el-container {
  margin-top: 10px;

  ::v-deep .el-tabs__item.is-disabled {
    color: #000 !important;
  }
}

.iframe-content-card {
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


.file-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;
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

::v-deep .el-textarea__inner {
  border: 0px;
  border-radius: 10px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
  "Microsoft YaHei", 微软雅黑, Arial, sans-serif;
  font-size: 16px;
}

::v-deep .v-note-wrapper .v-note-panel .v-note-show .v-show-content,
.v-note-wrapper .v-note-panel .v-note-show .v-show-content-html {
  padding: 0px;
}

::v-deep .markdown-body p {
  margin-bottom: 0px;
}

/* 定义滚动条高宽及背景
 高宽分别对应横竖滚动条的尺寸 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

/* 定义滚动条轨道
内阴影+圆角 */
::-webkit-scrollbar-track {
  background-color: transparent;
}

/* 定义滑块
内阴影+圆角 */
::-webkit-scrollbar-thumb {
  border-radius: 8px;
  background-color: rgba(221, 221, 221, 0.5);
}

.content-card ::v-deep .el-table,
.content-card ::v-deep .el-table__expanded-cell {
  background-color: #ffffff20;
}

/* 表格内背景颜色 */
.content-card ::v-deep .el-table th,
.content-card ::v-deep .el-table tr,
.content-card ::v-deep .el-table td {
  background-color: #ffffff20 !important;
}

::v-deep .el-table__body-wrapper::-webkit-scrollbar {
}

// 滚动条的滑块
::v-deep .el-table__body-wrapper::-webkit-scrollbar-thumb {
  background-color: #d0d0d040;
  border-radius: 3px;
}

::v-deep .detail .el-dialog {
  background-color: #ffffffc0;
  border-radius: 20px;
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
