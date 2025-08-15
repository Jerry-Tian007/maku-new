<template>
  <div class="app-container">

    <div class="configHeader" style="font-size:14px;color:#55648A;">
      返回对话
    </div>
    <el-row :gutter="10" style="margin-top: 20px;">
      <el-col :span="12">
        <div class="configContainerHeader" style="font-size:21px;color:rgba(0,0,0,0.7);font-weight:500;">
          参数设置
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="14">
        <el-card class="plainCard">
          <div class="containerConfig" style="margin:10px 30px;height:auto;">
            <!--            <div style="display: block">-->
            <div class="headerText" style="float:left;">
              角色设置
            </div>
            <div style="float:right;">
              <svg-icon
                slot="prefix"
                icon-class="user"
                class="el-input__icon input-icon"
                style=""
              />
              <el-dropdown trigger="click" style="font-size:14px;">
                    <span class="el-dropdown-link">
                      GPT3.5<i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item icon="">GPT4.0</el-dropdown-item>
                  <el-dropdown-item icon="">通义千问</el-dropdown-item>
                  <el-dropdown-item icon="">文心一言</el-dropdown-item>
                  <el-dropdown-item icon="">智谱清言</el-dropdown-item>
                  <el-dropdown-item icon="">KIMI</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <!--            </div>-->
            <el-input placeholder="请输入内容" style="margin:10px 0;"></el-input>
            <div class="headerText">提示词描述</div>
            <el-row style="margin-top: 10px;">
              <el-col :span="24">
                <el-input
                  type="textarea"
                  placeholder="请输入内容"
                  style="margin-bottom: 20px;">
                </el-input>
              </el-col>
              <el-col :span="24" style="text-align: right;">
                <el-button type="primary" class="green" size="small" style="">保存</el-button>
                <el-button type="primary" size="small">优化</el-button>
              </el-col>
            </el-row>
            <!--          </el-card>-->
            <!--          <el-card class="plain blue left-card">-->
            <el-row :gutter="50" style="margin-top: 10px;">
              <el-col :span="12">
                <el-row :gutter="10">
                  <el-col :span="12">
                    <div class="headerText">回答字数限定</div>
                  </el-col>
                  <el-col :span="12">
                    <el-switch v-model="switched">开关</el-switch>
                  </el-col>
                </el-row>
                <el-input-number v-model="num" @change="handleChange" :min="1" :max="1000" label="描述文字"
                                 style="margin-top: 10px;"></el-input-number>
                <div class="headerText" style="margin-top: 10px;">语气/风格</div>
                <el-select v-model="select" clearable popper-class="color-select" style="width:75%;margin-top: 10px;">
                  <el-option
                    v-for="item in selectOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
                <el-select v-model="select" clearable popper-class="color-select" style="width:75%;margin-top: 10px;">
                  <el-option
                    v-for="item in selectOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>

              </el-col>
              <el-col :span="12">
                <div class="headerText">最大Token数量</div>
                <el-input-number v-model="num" @change="handleChange" :min="1" :max="1000" label="描述文字"
                                 style="margin-top: 13px;"></el-input-number>
                <div class="headerText" style="margin-top: 15px;">发散性（0~2）</div>
                <el-slider v-model="slider1" style="width:75%;margin-top:5px; "></el-slider>
                <el-input-number v-model="num" @change="handleChange" :min="0" :max="2" label="描述文字"
                                 style="margin-top: 10px;"></el-input-number>
              </el-col>
            </el-row>
            <!--          </el-card>-->
            <!--          <el-card class="plain blue left-card">-->
            <div class="headerText" style="margin-top: 20px;">同步至其他模型</div>
            <el-row :gutter="20" style="margin-top: 15px;">
              <el-col :span="8">
                <svg-icon
                  slot="prefix"
                  icon-class="user"
                  class="el-input__icon input-icon"
                  style=""
                />
                <el-checkbox v-model="checked">GPT3.5</el-checkbox>
              </el-col>
              <el-col :span="8">
                <el-checkbox v-model="checked">GPT4.0</el-checkbox>
              </el-col>
              <el-col :span="8">
                <el-checkbox v-model="checked">文心一言</el-checkbox>
              </el-col>
              <el-col :span="8" style="margin-top: 15px;">
                <el-checkbox v-model="checked">讯飞星火</el-checkbox>
              </el-col>
            </el-row>


            <div style="clear:both;"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card class="plainCard">
          <div style="margin:0 35px;">
            <div class="headerText" style="text-align: center;">
              <span>提示词库</span>
            </div>
            <el-tabs v-model="test" class="is-center">
              <el-tab-pane label="我的" name="first">我的</el-tab-pane>
              <el-tab-pane label="社区" name="second">
                <el-input class="dark" placeholder="搜索" prefix-icon="el-icon-search"></el-input>
                <el-table :data="wordList" style="margin-top: 20px;">
                  <el-table-column
                    v-for="word in wordList">
                    <template slot-scope="scope">
                      <span>{{ word.text }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="操作"
                    align="center"
                    width="160"
                    class-name="small-padding fixed-width"
                    style=""
                  >
                    <template slot-scope="scope" v-if="scope.row.userId !== 1">
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-edit"
                        @click=""
                        v-hasPermi="[]"
                      >保存
                      </el-button>
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click=""
                        v-hasPermi="[]"
                      >使用
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>


<script>
export default {
  name: "IndexProd",
  data() {
    return {
      switched: true,
      checked: false,
      checkedTrue: true,
      test: 'first',
      slider1: 20,
      select: '正常',
      num: 1,
      // loading: true,
      wordList: [
        {text:'充当文案专员'},
        {text:'充当职业顾问'},
        {text:'Linux终端'},
        {text:'井字棋'},
        {text:'媒体运营'},
        {text:'算法工程师'},
        {text:'前端工程师'},
      ],
      selectOptions: [
        {label: '正常', value: '正常'},
        {label: '停用', value: '停用'}
      ],
      leftCards: [
        {title: '角色设置', description: '这是角色设置的内容'},
        {title: '限制', description: '这是限制的内容'},
        {title: '其他类型', description: '这是其他类型的内容'},
      ],
    };
  },
  created() {

  },
  methods: {
    handleChange(value) {
      console.log(value);
    },
  }
}
</script>

<style scoped lang="scss">
.plainCard {
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.52) 0%, #ECF2FD 100%);
  box-shadow: 0px 0px 7px 0px rgba(0, 0, 0, 0.11);
  border-radius: 4px;
}

.configHeader {

}

.configContainerHeader {
  margin-bottom: 10px;
}

.card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.left-card {
  width: 100%;
  margin-bottom: 10px;
}

.right-card {
  width: 100%;
}

//.clearfix::after {
//  content: "";
//  display: block;
//  clear: both;
//}

::v-deep .el-card__header {
  border-bottom: none;
}

.el-card__body {
  padding: 20px;
}

.headerText {
  font-family: PingFangSC, PingFang SC;
  font-weight: 600;
  font-size: 13px;
  color: #2C2C2C;
  line-height: 17px;
  text-align: left;
  font-style: normal;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
}

::v-deep .el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: #755FCB; //选中时内部背景为白色
}

::v-deep .dark .el-input__inner, .dark .el-textarea__inner {
  background: #FFFFFF;
  border-radius: 4px;
  height: 40px;
}

.demonstration {
  display: block;
  color: #8492a6;
  font-size: 14px;
  margin-bottom: 20px;
}
</style>

