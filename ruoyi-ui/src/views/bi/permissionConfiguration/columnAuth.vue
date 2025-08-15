<template>
  <div class="flex-layout gap10">
    <div class="flex-1 pd-a20 flex-layout column"
         style="background-color: #F5F9FF;overflow: hidden;border-radius: 4px;">
      <div class="flex-layout align-center gap4 bottom-border">
        <div class="kb-all">
          <span>{{ $t('privateDomain.toBeUsed') }}</span>
          <span>
                  ({{ kbAllList.length }})
                  </span>
        </div>
        <el-divider direction="vertical"/>
        <el-input
          v-model="searchAllKB"
          :placeholder="$t('search')"
          clearable
          prefix-icon="el-icon-search"
          class="flex-1 transparent"/>
      </div>

      <div class="flex-1" style="overflow-y:auto;" v-loading="loading">
        <el-checkbox-group class="flex-layout column pd-v10" v-model="kbList" @change="getResult">
          <el-checkbox
            v-for="(item, index) in (searchAllKB?kbAllList.filter(item=>item.fieldName.includes(searchAllKB)):kbAllList)"
            :key="item.fieldId"
            :label="item"
            style="overflow: hidden;padding: 8px 0"
            class="over"
          >
            <k-b-radio :value="item.fieldName" style="overflow: hidden;margin-right: 20px;"/>
          </el-checkbox>
        </el-checkbox-group>
      </div>
    </div>
    <div class="flex-1 pd-a20 flex-layout column"
         style="background-color: #EBF0F9;overflow: hidden;border-radius: 4px;">
      <div class="flex-layout align-center gap4 bottom-border">
        <div class="kb-chosen">
          <span>{{ $t('selected') }}</span>
          <span>
                  ({{ kbList.length }})
                  </span>
        </div>
        <el-divider direction="vertical"/>
        <el-input
          v-model="searchChosenKB"
          :placeholder="$t('search')"
          clearable
          prefix-icon="el-icon-search"
          class="flex-1 transparent"/>
      </div>

      <div class="flex-1" style="overflow-y:auto;" v-loading="loading">
        <template v-if="kbList.length<1">
          <empty style="color: rgba(0,0,0,0.29);" :image-size="120">
            <template slot="description">
              <div>{{ $t('noSelectKb') }}</div>
              <div>{{ $t('selectKb') }}</div>
            </template>
          </empty>
        </template>
        <el-checkbox-group class="flex-layout column pd-v10" v-model="kbList" @change="getResult">
          <el-checkbox
            v-for="(item, index) in (searchChosenKB?kbList.filter(item=>item.fieldName.includes(searchChosenKB)):kbList)"
            :key="item.fieldId"
            :label="item"
            style="overflow: hidden;padding: 8px 0"
            class="over"
          >
            <k-b-radio :value="item.fieldName" style="overflow: hidden;margin-right: 20px;"/>
          </el-checkbox>
        </el-checkbox-group>
      </div>
    </div>
  </div>
</template>
<script>
import KBRadio from "@/components/KBRadio/index.vue";
import {
  findAllRole
} from '@/api/bi/permissionConfiguration/api';

export default {
  name: "KBSelect",
  components: {KBRadio},
  props: {
    // id数组
    defaultValue: {
      type: Array,
      default: () => []
    },
    tableId: String
  },
  model: {
    prop: 'defaultValue',
    event: 'result'
  },
  data() {
    return {
      loading: false,

      kbList: [],
      kbAllList: [],

      searchAllKB: null,
      searchChosenKB: null,
    }
  },
  watch: {
    defaultValue: {
      handler: function (val) {
        this.kbList = this.kbAllList.filter(item => val.includes(item.fieldId));
      },
      immediate: true
    },
    tableId(val){
      if (val){
        this.getKBList();
      }
    }
  },
  mounted() {
    this.getKBList();
  },
  methods: {
    getKBList() {
      this.loading = true;
      findAllRole({tableId: this.tableId, fieldName: ''}).then(response => {
        if (response.code === 200) {
          this.kbAllList = response.data;

          if (this.defaultValue.length > 0) {
            this.kbList = this.kbAllList.filter(item => this.defaultValue.includes(item.fieldId));
          }

          this.loading = false;
        }
      }).catch(() => {
        this.loading = false;
      });
    },
    clear() {
      this.kbList = [];
    },
    default(defaultValue) {
      let value = defaultValue || this.defaultValue || [];
      this.kbList = this.kbAllList.filter(item => value.includes(item.fieldId));
    },
    refreshData() {
      this.getKBList();
    },
    init() {
      this.clear();
      this.refreshData();
    },
    getResult() {
      this.$emit('result', this.kbList.map(item => item.fieldId));
    }
  }
}
</script>
<style scoped lang="scss">
.kb-all {
  font-weight: 600;
  font-size: 16px;
  color: rgba(44, 44, 44, 0.84);
  line-height: 22px;
}

.kb-chosen {
  font-weight: 600;
  font-size: 16px;
  color: #536FBD;
  line-height: 22px;
}

::v-deep .el-loading-mask {
  background-color: rgba(255, 255, 255, 0.3);
}
</style>
