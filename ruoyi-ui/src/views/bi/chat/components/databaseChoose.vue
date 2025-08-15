<template>
  <div class="flex-layout column gap16 over-hidden">
    <div class="flex-1 flex-layout gap20 over-hidden" style="width: 100%;">
      <div class="flex-1 selected left">
        <div class="header bottom-border">
          <div class="flex-layout align-center start-end">
            <div class="select-title">
              {{ $t('chatBI.allDatasource') }}
            </div>
            <div class="select-count">
              {{ checkedList.length }}/{{ allList.length }}
            </div>
          </div>
          <div class="flex-layout align-center">
            <el-button type="text" @click="handleSelectAll" :disabled="disabled">{{ $t('privateDomain.selectAll') }}</el-button>
            <el-divider direction="vertical"/>
            <el-input
              v-model="searchAll"
              :placeholder="$t('search')"
              clearable
              prefix-icon="el-icon-search"
              class="flex-1 transparent"/>
          </div>


        </div>

        <div class="flex-1 gap10 pd-v10" style="overflow-y:auto;">
          <el-checkbox-group v-model="checkedList">
            <div
              :class="{'select-disabled': disabled}"
              v-for="(item, index) in (searchAll?allList.filter(item=>item.name.includes(searchAll)):allList)"
              :key="item.name"
              class="flex-layout column">
              <div class="flex-layout align-center gap16 pd-v6">
                <el-checkbox :title="item.name+'\n'+$t('description')+'：'+item.databaseDesc" :label="item">
                  <div class="flex-layout align-center gap8">
                    <div class="circle-icon">{{ item.name ? item.name.charAt(0) : '-' }}</div>
                    <div class="item-name primary-text fs-16">{{ item.name }}</div>
                    <div class="item-desc secondary-text">{{ item.databaseDesc }}</div>
                  </div>
                </el-checkbox>
              </div>

            </div>
          </el-checkbox-group>
        </div>
      </div>
      <div class="flex-1 selected right">
        <div class="header bottom-border">
          <div class="flex-layout align-center start-end">
            <div class="select-title">
              {{ $t('selected') }}
            </div>
            <div class="select-count">
              {{ checkedList.length }}/{{ allList.length }}
            </div>
          </div>
          <div class="flex-layout align-center">
            <el-button type="text" @click="handleSelectNone" :disabled="disabled">{{ $t('privateDomain.cancelAll') }}</el-button>
            <el-divider direction="vertical"/>
            <el-input
              v-model="searchSelected"
              :placeholder="$t('search')"
              clearable
              prefix-icon="el-icon-search"
              class="flex-1 transparent"/>
          </div>
        </div>

        <div class="flex-1 pd-v10" style="overflow-y:auto;">
          <el-checkbox-group v-model="checkedList">
            <div
              :class="{'select-disabled': disabled}"
              v-for="(item, index) in (searchSelected?checkedList.filter(item=>item.name.includes(searchSelected)):checkedList)"
              :key="item.name"
              class="flex-layout column">
              <div class="flex-layout align-center gap16 pd-v6">
                <el-checkbox :title="item.name+'\n'+$t('description')+'：'+item.databaseDesc" :label="item">
                  <div class="flex-layout align-center gap8">
                    <div class="circle-icon">{{ item.name ? item.name.charAt(0) : '-' }}</div>
                    <div class="item-name primary-text fs-16">{{ item.name }}</div>
                    <div class="item-desc secondary-text">{{ item.databaseDesc }}</div>
                  </div>
                </el-checkbox>
              </div>

            </div>
          </el-checkbox-group>
        </div>

      </div>
    </div>
    <div align="right" v-if="!disabled">
      <el-button
        :disabled="checkedList.length === 0"
        @click="handleSubmit"
        size="medium"
        type="primary">{{ $t('ok') }}
      </el-button>
    </div>
  </div>
</template>
<script>
export default {
  name: 'DatabaseChoose',
  props: {
    value: {
      type: Array,
      default: []
    },
    dataSource: {
      type: Array,
      default: []
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      searchAll: '',
      searchSelected: '',

      allList: [],
      checkedList: [],


    }
  },
  watch: {
    value: {
      handler(val) {
        this.checkedList = this.dataSource.filter(item => val.includes(item.id));
      },
      immediate: true
    },
    dataSource: {
      handler(val) {
        this.allList = val;
      },
      immediate: true
    }
  },
  model: {
    prop: 'value',
    event: 'change'
  },
  methods: {
    handleSelectAll() {
      console.log('全选')
      this.checkedList = [...this.allList];
    },
    handleSelectNone() {
      console.log('全部取消')
      this.checkedList = [];
    },
    handleSubmit() {
      this.$emit('change', this.checkedList);
      let data = {
        list: this.checkedList.map(item => item.id),
        count: {
          total: this.allList.length,
          selected: this.checkedList.length
        }
      }
      this.$emit('submit', 'database', data);
    }
  }
}
</script>
<style scoped lang="scss">
.selected {
  overflow: hidden;
  border-radius: 10px;
  height: 100%;
  padding: 10px 20px;

  display: flex;
  flex-direction: column;

  .header {

    .select-title {
      font-weight: 600;
      font-size: 16px;
      color: rgba(44, 44, 44, 0.84);
      line-height: 22px;
    }

    .select-count {
      font-weight: 400;
      font-size: 12px;
      color: rgba(44, 44, 44, 0.7);
      line-height: 17px;
    }
  }

  &.left {
    background-color: #F5F9FF;
  }

  &.right {
    background-color: #EBF0F9;
  }


}

.item-name {
  font-weight: 400;
  font-size: 14px;
}

.item-desc {
  font-weight: 400;
  font-size: 12px;
}

.table-prefix {
  width: 6px;
  height: 20px;
  display: flex;
  flex-direction: column;
  margin-left: 40px;


  .top {
    flex: 1;
    width: 100%;
    border-left: 1px solid #C0C4CC;
    border-bottom: 1px solid #C0C4CC;
  }

  .bottom {
    flex: 1;
    width: 100%;
    border-left: 1px solid #C0C4CC;
  }

  &.end {
    .top {
      border-radius: 4px;
    }

    .bottom {
      opacity: 0;
    }
  }
}


.circle-icon {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: #4A5F9C;
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 13px;
  padding-bottom: 1px;
}

.select-disabled {
  pointer-events: none;
  opacity: 0.6;
}
</style>
