<template>
  <div class="app-container">
    <el-card>
      <div class="main-title">
        按钮
      </div>
      <el-card class="plain pd-a10">
        <div class="sub-title">
          类型
        </div>
        <div class="pd-v4">
          <el-button type="primary">主要</el-button>
          <el-button type="primary" class="green">主绿</el-button>
          <el-button class="secondary">次要</el-button>
          <el-button class="blue">蓝色</el-button>
          <el-button class="green">绿色</el-button>
          <el-button class="red">红色</el-button>
          <el-button class="purple">紫色</el-button>
          <el-button class="blue-light">亮蓝</el-button>
        </div>
        <div class="pd-v4">
          <el-button type="primary" disabled>主要</el-button>
          <el-button type="primary" class="green" disabled>主绿</el-button>
          <el-button class="secondary" disabled>次要</el-button>
          <el-button class="blue" disabled>蓝色</el-button>
          <el-button class="green" disabled>绿色</el-button>
          <el-button class="red" disabled>红色</el-button>
          <el-button class="purple" disabled>紫色</el-button>
          <el-button class="blue-light" disabled>亮蓝</el-button>
        </div>
        <div class="sub-title mt20">
          图标
        </div>
        <div class="pd-v4">
          <el-button type="primary" icon="el-icon-search">搜索</el-button>
          <el-button class="blue" icon="el-icon-plus">新增</el-button>
          <el-button class="green" icon="el-icon-edit">修改</el-button>
        </div>
        <div class="sub-title mt20">
          形状
        </div>
        <div class="pd-v4">
          <el-button type="primary" round>搜索</el-button>
          <el-button class="blue" icon="el-icon-plus" circle></el-button>
        </div>
      </el-card>



      <div class="main-title mt20">
        输入框
      </div>
      <el-card class="plain blue">
        <div class="flex-layout align-center mb10" style="gap: 10px;width:50%;">
          <el-input placeholder="请输入内容"></el-input>
          <el-input placeholder="请输入内容" disabled></el-input>
        </div>
        <el-card class="mb10" :body-style="{display: 'flex', alignItems: 'center', gap: '10px'}" style="width:50%;">
          <el-input class="dark" placeholder="请输入内容" prefix-icon="el-icon-search"></el-input>
          <el-input class="dark border" placeholder="请输入内容" prefix-icon="el-icon-search"></el-input>
        </el-card>
        <el-input style="width: 50%;" type="textarea" placeholder="请输入内容"></el-input>
      </el-card>

      <div class="main-title mt20">
        数字输入框
      </div>
      <el-card class="plain blue">
        <el-input-number size="medium" class="main" v-model="slider1" :min="0" :max="100" :step="1"></el-input-number>
      </el-card>

      <div class="main-title mt20">
        选择框
      </div>
      <el-card class="plain blue">
        <div class="flex-layout align-center mb10" style="gap: 10px;width:50%;">
          <el-select v-model="select" clearable popper-class="color-select">
            <el-option
              v-for="item in selectOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <el-select disabled popper-class="color-select">
            <el-option
              v-for="item in selectOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
      </el-card>

      <div class="main-title mt20">
        卡片
      </div>
      <div class="pd-a6 flex-layout align-center" style="gap:10px;">
        <el-card style="width:120px; height:120px;">常规</el-card>
        <el-card style="width:120px; height:120px;" class="plain">边框</el-card>
        <el-card style="width:120px; height:120px;" class="blue">蓝色</el-card>

      </div>
      <el-card class="plain mt10">
        <div class="flex-layout align-center pd-v4" style="width: 100%;">
          <el-card class="bot" style="max-width: 75%;min-width:300px;">
            你好，这是bot的回答卡片
            <div class="tool-bar mt10">
              <div class="tool-bar-item">
                <i class="el-icon-document-copy"></i>
              </div>
            </div>
          </el-card>
        </div>
        <div class="flex-layout align-center pd-v4" style="width: 100%;justify-content: flex-end;">
          <el-card class="user" style="max-width: 75%;min-width:120px;">
            你好，这是user的提问卡片
          </el-card>
        </div>
      </el-card>


      <div class="main-title mt20">
        滑块
      </div>
      <el-card class="plain">
        <el-slider v-model="slider1"></el-slider>
      </el-card>

      <div class="main-title mt20">
        Tab页
      </div>
      <el-card class="plain">
        <el-tabs v-model="test" class="is-center">
          <el-tab-pane label="用户管理" name="first">用户管理</el-tab-pane>
          <el-tab-pane label="配置管理" name="second">配置管理</el-tab-pane>
          <el-tab-pane label="角色管理" name="third">角色管理</el-tab-pane>
          <el-tab-pane label="定时任务补偿" name="fourth">定时任务补偿</el-tab-pane>
        </el-tabs>
      </el-card>

      <div class="main-title mt20">
        复选框
      </div>
      <el-card class="plain blue">
        <el-checkbox v-model="checked">复选框</el-checkbox>
        <el-checkbox v-model="checkedTrue">复选框</el-checkbox>
      </el-card>


      <div class="main-title mt20">
        切换按钮
      </div>
      <el-card class="plain">
        <el-switch v-model="switched">开关</el-switch>
      </el-card>

      <div class="main-title mt20">
        分页
      </div>
      <el-card class="plain">
        <el-pagination
          popper-class="color-pagination"
          background
          layout="total, sizes, prev, pager, next"
          :total="1000">
        </el-pagination>
      </el-card>


    </el-card>
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
      selectOptions: [
        {label: '正常', value: '正常'},
        {label: '停用', value: '停用'}
      ]
    };
  },
  created() {

  },
  methods: {}
}
</script>

<style scoped lang="scss">
.back {
  //background: radial-gradient( at 50% 0%, rgba(245,214,255,0.49) 0%, rgba(191,209,241,0.46) 47%, #FBFFFF 100%);
}

.icon-red {
  color: #73B2C2;
  opacity: 0.684227257;
}
</style>
