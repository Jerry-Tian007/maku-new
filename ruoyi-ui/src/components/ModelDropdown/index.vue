<template>
  <div class="flex-layout align-center" :title="model">
    <img :src="botImgUrl" style="width: 22px;height: 22px;border-radius: 16px;pointer-events: none"/>
    <el-dropdown @command="handleCommand" trigger="hover"
                 style="margin-left: 10px;height:20px;line-height: 20px;width:100px;overflow: hidden;">
      <div style="width: 100%" class="flex-layout align-center">
        <div class="el-dropdown-link text-over" style="cursor: pointer;width: 100%;">
          {{ model }}
        </div>
        <!--                  <i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
      </div>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item
          v-for="item in modelList"
          :key="item.type"
          :command="item.type">
          <div style="display: flex;align-items: center;">
            <div style="display: flex;align-items: center">
              <img width="14" height="14" style="border-radius: 10px;" :src="item.icon || placeholder"/>
              <span style="margin-left: 8px;">{{ item.type }}</span></div>
          </div>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>
<script>
import placeholder from "@/assets/logo/logo-placeholder.jpg";

export default {
  name: "ModelDropdown",
  props: {
    modelInfo: {
      type: Object,
      default: () => {
        return {
          model: '暂无可用模型',
          scene: null,
          modelTypeId: null,
          botImgUrl: placeholder,
        }
      }
    }
  },
  computed: {
    modelList() {
      return this.$store.state.user.botList.filter(item => item.scene !== 'embedding');
    }
  },
  model:{
    prop: 'modelInfo',
    event: 'change'
  },
  watch: {
    modelInfo: {
      handler(newVal) {
        this.model = newVal.model;
        this.scene = newVal.scene;
        this.modelTypeId = newVal.modelTypeId;
        this.botImgUrl = newVal.botImgUrl;
      },
      deep: true
    },
    modelList: function (newVal, oldVal) {
      if (newVal.findIndex(item => item.type === this.model) === -1) {
        if (newVal.length > 0) {
          this.handleCommand(newVal[0].type);
        } else {
          this.model = '暂无可用模型';
          this.scene = null;
          this.modelTypeId = null;
          this.botImgUrl = placeholder;
        }
      } else {
        this.handleCommand(this.model);
      }
    }
  },
  data() {
    return {
      placeholder,

      modelTypeId: null,
      model: '暂无可用模型',
      botImgUrl: '',
    }
  },
  mounted() {
    if (this.modelList.length > 0) {
      this.handleCommand(this.modelList[0].type);
    }
  },
  methods: {
    handleCommand(command) {
      let model = this.modelList.find(item => item.type === command);
      this.botImgUrl = model.icon || /*require('@/assets/images/gpt.png')*/placeholder;
      this.scene = model.scene;
      this.modelTypeId = model.id;
      this.model = command;

      this.$emit('change', {
        model: this.model,
        scene: this.scene,
        modelTypeId: this.modelTypeId,
        botImgUrl: this.botImgUrl
      });
    },
  }
}
</script>
