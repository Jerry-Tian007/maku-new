<template>
  <div class="app-container full-screen flex-layout column center-center" style="gap:40px;padding-bottom: 10%;">
    <div class="flex-layout column align-center" style="gap:20px;">
      <img :src="logoIcon || logoPlaceholder" width="160px" height="160px" style="border-radius: 20px;"/>
      <div class="main-color index-title">
        {{ $t('home.title') }}
      </div>
    </div>
    <div class="grid-layout row2 col2" style="width: 72%;row-gap: 20px;column-gap: 20px;">
      <el-card
        v-for="(item,index) in buttonList"
        :key="index"
        :body-style="{height: '100%',width: '100%'}"
        @click.native="handleClickJump(item)"
        :title="item.title"
        :class="{'index-card': true, 'clickable': true, 'is-disabled': !item.active}"
      >
        <div class="flex-layout gap8" style="width: 100%;height:100%;">
          <svg-icon :icon-class="item.icon" style="font-size: 30px;"/>
<!--          <img :src="allinone" width="80px" height="80px"/>-->
          <div class="flex-1 flex-layout column gap6">
            <div class="flex-layout align-center start-end">
              <div class="index-card-title">
                {{item.title}}
              </div>
              <div>
                <svg-icon v-if="item.active" icon-class="index-arrow-right" style="font-size: 20px;"/>
                <div v-else class="coming-soon sub-color">
                  {{ $t('home.comingSoon') }}
                </div>
              </div>
            </div>
            <div class="index-card-content text-over two-line" :title="item.content">
              {{item.content}}
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import logoPlaceholder from "@/assets/logo/logo-placeholder.jpg";
import logoIcon from "@/assets/logo/logoIcon1.png";
export default {
  name: "Index",
  data() {
    return {
      logoIcon,
      logoPlaceholder,
      buttonList: [ //All in One  知识库  创意设计  智能体
        {
          title: 'All In One',
          icon: 'index-allinone',
          content: '多模型对比，快速解析网址与文件，助力信息洞察与决策。',
          name: 'AllInOne',
          active: true
        },
        {
          title: '知识库',
          icon: 'index-knowledge',
          content: '深度挖掘企业内部知识，赋能创新与高效协作。',
          name: 'KBChat',
          active: true
        },
        {
          title: '创意设计',
          icon: 'index-idea',
          content: '一句话生成精美图片，激发创意灵感，轻松实现视觉呈现。',
          name: 'CreativeDesign',
          active: false
        },
        {
          title: '智能体',
          icon: 'index-agent',
          content: '探索智能体的广泛应用，开启无限创新与智能化未来。',
          name: 'IntelligentAgent',
          active: false
        }
      ]
    };
  },
  created() {

  },
  methods: {
    handleClickJump(item) {
      if(item.active){
        this.$router.push({name: item.name});
      }
    }
  }
}
</script>

<style scoped lang="scss">
body{
  font-family: PingFangSC, PingFang SC;
}
.back {
  //background: radial-gradient( at 50% 0%, rgba(245,214,255,0.49) 0%, rgba(191,209,241,0.46) 47%, #FBFFFF 100%);
}

.icon-red {
  color: #73B2C2;
  opacity: 0.684227257;
}

.index-title{
  font-weight: 600;
  font-size: 36px;
  line-height: 50px;
}

.index-card{
  width: 100%;
  height: 100%;
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.index-card-title{
  font-weight: 600;
  font-size: 21px;
  color: rgba(0,0,0,0.8);
  line-height: 29px;
}
.index-card-content{
  font-weight: 400;
  font-size: 16px;
  color: rgba(0,0,0,0.35);
  line-height: 22px;
  //限制为两行 超出显示省略号
}

.coming-soon{
  font-size: 12px;
  background-color: #d9e7f4;
  padding: 4px 8px;
  border-radius: 4px;
}
</style>
