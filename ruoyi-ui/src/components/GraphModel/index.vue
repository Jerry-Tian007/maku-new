<template>
    <div class="flex-layout gap10">
        <div class="flex-1 pd-a20 flex-layout column"
            style="background-color: #F5F9FF;overflow: hidden;border-radius: 4px;">
            <div class="flex-layout align-center gap4 bottom-border">
                <div class="model-all">
                    <span>{{ $t('systemManager.toBeUsed') }}</span>
                    <span>
                        ({{ modelAllList.length }})
                    </span>
                </div>
                <el-divider direction="vertical" />
                <el-input v-model="searchAllModel" :placeholder="$t('search')" clearable prefix-icon="el-icon-search"
                    class="flex-1 transparent" />
            </div>

            <div class="flex-1" style="overflow-y:auto;" v-loading="loading">
                <el-checkbox-group class="flex-layout column pd-v10" v-model="modelList" @change="getResult">
                    <el-checkbox
                        v-for="(item, index) in (searchAllModel ? modelAllList.filter(item => item.cnName.includes(searchAllModel)) : modelAllList)"
                        :key="item.spaceId" :label="item" style="padding: 8px 0;overflow: hidden;"
                        class="over flex-layout align-center">
                        <div class="flex-layout align-center gap6">
                            <!-- <img width="16" height="16" style="border-radius: 10px;" :src="item.icon || placeholder" /> -->
                            <div style="margin-right: 20px;padding-bottom: 2px;" class="text-over">
                                {{ item.cnName }}
                            </div>
                        </div>
                    </el-checkbox>
                </el-checkbox-group>
            </div>
        </div>
        <div class="flex-1 pd-a20 flex-layout column"
            style="background-color: #EBF0F9;overflow: hidden;border-radius: 4px;">
            <div class="flex-layout align-center gap4 bottom-border">
                <div class="model-chosen">
                    <span>{{ $t('selected') }}</span>
                    <span>
                        ({{ modelList.length }})
                    </span>
                </div>
                <el-divider direction="vertical" />
                <el-input v-model="searchChosenModel" :placeholder="$t('search')" clearable prefix-icon="el-icon-search"
                    class="flex-1 transparent" />
            </div>

            <div class="flex-1" style="overflow-y:auto;" v-loading="loading">
                <template v-if="modelList.length < 1">
                    <empty style="color: rgba(0,0,0,0.29);" :image-size="120">
                        <template slot="description">
                            <div>{{ $t('noSelectModel') }}</div>
                        </template>
                    </empty>
                </template>
                <el-checkbox-group class="flex-layout column pd-v10" v-model="modelList" @change="getResult">
                    <el-checkbox
                        v-for="(item, index) in (searchChosenModel ? modelList.filter(item => item.cnName.includes(searchChosenModel)) : modelList)"
                        :key="item.spaceId" :label="item" style="padding: 8px 0;overflow: hidden;"
                        class="over flex-layout align-center">
                        <div class="flex-layout align-center gap6">
                            <!-- <img width="16" height="16" style="border-radius: 10px;" :src="item.icon || placeholder" /> -->
                            <div style="margin-right: 20px;padding-bottom: 2px;" class="text-over">
                                {{ item.cnName }}
                            </div>
                        </div>
                    </el-checkbox>
                </el-checkbox-group>
            </div>
        </div>
    </div>
</template>
<script>

import { getModelSelect, getModelTemplateSelect } from "@/api/modelManager/modelManager";
import placeholder from '@/assets/logo/logo-placeholder.jpg';
import { findList } from '@/api/knowledgeBase/knowledgeGraph';


export default {
    name: "GraphModel",
    props: {
        // id数组
        defaultValue: {
            type: Array,
            default: () => []
        }
    },
    model: {
        prop: 'defaultValue',
        event: 'result'
    },
    data() {
        return {
            placeholder,
            loading: false,

            modelList: [],
            modelAllList: [],
            pageParams: {
                pageNum: 1,
                pageSize: 100,
                name: '',
                cnName: ''
            },
            searchAllModel: null,
            searchChosenModel: null,
        }
    },
    watch: {
        defaultValue: {
            handler: function (val) {
                this.modelList = this.modelAllList.filter(item => val.includes(item.spaceId + ''));
            },
            immediate: true
        }
    },
    mounted() {
        this.getModelList()
    },
    methods: {
        getModelList() {
            this.loading = true;
            findList(this.pageParams).then(response => {
                if (response.code === 200) {
                    this.modelAllList = response.rows;
                }   

                if (this.defaultValue.length > 0) {
                    this.modelList = this.modelAllList.filter(item => this.defaultValue.includes(item.spaceId + ''));
                }

                this.loading = false;
            }).catch(() => {
                this.loading = false;
            });
        },
        clear() {
            this.modelList = [];
        },
        default(defaultValue) {
            let value = defaultValue || this.defaultValue || [];
            this.modelList = this.modelAllList.filter(item => value.includes(item.spaceId + ''));
        },
        refreshData() {
            this.getKBList();
        },
        init() {
            this.clear();
            this.refreshData();
        },
        getResult() {
            this.$emit('result', this.modelList.map(item => item.spaceId + ''));
        }
    }
}
</script>
<style scoped lang="scss">
.model-all {
    font-weight: 600;
    font-size: 16px;
    color: rgba(44, 44, 44, 0.84);
    line-height: 22px;
}

.model-chosen {
    font-weight: 600;
    font-size: 16px;
    color: #536FBD;
    line-height: 22px;
}

::v-deep .el-loading-mask {
    background-color: rgba(255, 255, 255, 0.3);
}
</style>