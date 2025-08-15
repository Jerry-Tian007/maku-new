<template>
  <el-card class="mt20" v-loading.fullscreen.lock="fullscreenLoading">
    <div class="flex-layout align-center" style="margin-bottom: 10px;justify-content: flex-end;">

      <div class="flex-layout align-center gap8">
        <el-input
          v-model="searchValue"
          :placeholder="$t('dataBase.searchValuePlaceholder')"
          style="width: 200px;"
          size="small"
          clearable
          prefix-icon="el-icon-search"
        />
        <el-popconfirm
          :title="$t('dataBase.deleteSelectedContent')"
          @confirm="deletedTableIds"
        >
          <el-button class="red" disabled
                     plain
                     slot="reference"
                     icon="el-icon-delete"
                     :disabled="multiple"
          >{{ $t('deleted') }}
          </el-button>
        </el-popconfirm>
      </div>
    </div>
    <el-table
      v-loading="loading"
      class="color-table"
      :data="dataList"
      :row-style="{height: '50px'}"
      @selection-change="handleSelectionChange">
      <template slot="empty">
        <Empty/>
      </template>
      <el-table-column
        type="selection"
        width="50"
        align="center"/>
      <el-table-column
        type="index"
        :label="$t('chatBI.index')"
        width="60"
        align="center"/>
      <el-table-column
        :label="$t('name')"
        align="center"
        prop="fileName"
        show-overflow-tooltip/>
      <el-table-column
        :label="$t('description')"
        align="center"
        prop="remark"
        show-overflow-tooltip/>
      <el-table-column
        :label="$t('kbManager.createBy')"
        align="center"
        prop="createBy"
        show-overflow-tooltip/>
      <el-table-column
        :label="$t('historyList.time')"
        align="center"
        prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <!--      <el-table-column
              label="操作"
              align="center"
              width="160"
              class-name="small-padding fixed-width"
              style=""
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                >删除
                </el-button>
              </template>
            </el-table-column>-->
    </el-table>
    <div style="margin-top: 10px;" align="right">
      <pagination
        :total="total"
        :page.sync="pageParams.pageNum"
        :limit.sync="pageParams.pageSize"
        @pagination="getList"
        popper-class="color-pagination"
        background
        layout="total, sizes, prev, pager, next"
      />
    </div>

  </el-card>
</template>
<script>
import {pageList, deletedTableIds} from "@/api/bi/chat/excel";
import {Message} from 'element-ui';

export default {
  name: "ExcelData",
  data() {
    return {
      loading: false,
      fullscreenLoading: false,
      dataList: [],
      total: 0,
      pageParams: {
        pageNum: 1,
        pageSize: 10
      },

      // 不为多选
      multiple: true,

      searchValue: '',
      tableDeleteIds: [],
    };
  },
  mounted() {
    this.getList();
  },
  watch: {
    searchValue(newValue) {
      this.loading = true;
      pageList(this.pageParams, {fileName: newValue}).then(res => {
        if (res.code === 200) {
          this.loading = false;
          this.dataList = res.rows;
          this.total = res.total;
        }
      }).catch(err => {
        this.loading = false;
        Message.error(this.$t('dataBase.queryListError') + err);
      });
    }
  },
  methods: {
    getList() {
      this.loading = true;
      pageList(this.pageParams, {fileName: this.searchValue}).then(res => {
        if (res.code === 200) {
          this.loading = false;
          this.dataList = res.rows;
          this.total = res.total;
        }
      }).catch(err => {
        this.loading = false;
        Message.error(this.$t('dataBase.queryListError') + err);
      });
    },
    handleSelectionChange(e) {
      if (e.length === 0) {
        this.tableDeleteIds = [];
        this.multiple = true;
      } else {
        this.tableDeleteIds = [];
        for (const t of e) {
          this.tableDeleteIds.push(t.id);
        }
        this.multiple = false;
      }
    },
    handleDelete(row) {
    },
    deletedTableIds() {
      if (this.tableDeleteIds.length !== 0) {
        deletedTableIds({ids: this.tableDeleteIds}).then(res => {
          if (res.code === 200) {
            Message.success(res.msg);
            this.getList();
          }
        }).catch(err => {
          Message.error(this.$t('dataBase.deletedError') + err);
        });
      }
    }
  }
};
</script>
