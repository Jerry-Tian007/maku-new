<template>
  <el-card :class="{'session-card': true, 'hide': !showSession}"
           :body-style="{padding: '20px 16px', height: '100%', display: 'flex', flexDirection: 'column', gap: '10px', overflow: 'hidden'}">
    <div class="fold-button" @click="$emit('state', false)">
      <svg-icon icon-class="fold" style="font-size: 20px;"/>
    </div>
    <div class="flex-layout center-center">
      <div class="flex-layout align-center gap4 text-over">
        <svg-icon icon-class="history" style="font-size: 24px;margin-top: 2px;"/>
        <span style="margin-bottom: 2px;">{{ $t('privateDomain.qaHistory') }}</span>
      </div>
    </div>
    <div style="padding-top: 8px;width: 100%;" class="flex-layout align-center gap4">
      <div class="flex-1">
        <template v-if="isEditSessions">
          <el-button
            @click="handleDeleteSessions"
            class="red"
            style="width: 100%;"
            icon="el-icon-delete">
            {{ $t('privateDomain.deletedConversation') }}
          </el-button>
        </template>
        <template v-else>
          <el-button
            v-if="isNewSession"
            style="width: 100%;"
            class="green"
            icon="el-icon-finished">
            {{ $t('privateDomain.newQaConversation') }}
          </el-button>
          <el-button
            v-else
            @click="handleNewSession"
            style="width: 100%;"
            class="blue"
            icon="el-icon-plus">
            {{ $t('privateDomain.addConversation') }}
          </el-button>
        </template>
      </div>
      <el-button
        v-if="needEdit"
        @click="handleEditSessions"
        class="purple thin"
        :title.native="isEditSessions?$t('complete'):$t('privateDomain.editSession')"
        :icon="isEditSessions? 'el-icon-check' : 'el-icon-set-up'">
      </el-button>
    </div>
    <div class="flex-1" style="overflow-y: auto;padding-right: 2px;" v-loading="sessionLoading">
      <Empty v-if="sessionList.length < 1" :description="$t('privateDomain.noneSession')"/>
      <template v-else>
        <!--            <el-checkbox-group v-model="editSessions" style="overflow: hidden;">-->
        <div
          v-for="(date,index) in sessionTimeList"
        >
          <div class="session-date text-gray">{{ date.type }}</div>
          <div
            :class="{'flex-layout align-center':true,'editing':isEditSessions}"
            style="overflow: hidden;width: 100%;"
            v-for="(item, index) in date.sessions"
            :key="index"
          ><!--
                <div :class="{'session-checkbox': true, 'show': isEditSessions}">
                  <el-checkbox/>
                </div>-->
            <div
              :class="{
                  'session-item flex-1 flex-layout align-center start-end': true,
                   'active': item.sessionMark === sessionMark,
                   'is-chosen': isEditSessions && editSessions.includes(item.id)
                }"
              :title="item.sessionName"
              @click="handleClickSession(item)"
            >
              <div class="text-over">
                {{ item.sessionName }}
              </div>
              <div :class="{'tool-item-button':true}" @click.stop="handleDeleteSession(item.id, item.sessionMark)">
                <i class="el-icon-delete"/>
              </div>
            </div>
          </div>
        </div>
        <!--            </el-checkbox-group>-->
      </template>
    </div>
  </el-card>
</template>
<script>
import {removeUserSession} from "@/api/knowledgeBase/sessionList";
import {parseTime} from "@/utils/ruoyi";

export default {
  name: 'SessionList',
  props: {
    sessionList: {
      type: Array,
      default: () => []
    },
    sessionMark: {
      type: String,
      default: ''
    },
    showSession: {
      type: Boolean,
      default: true
    },
    sessionLoading: {
      type: Boolean,
      default: false
    },
    isNewSession: {
      type: Boolean,
      default: false
    },
  },
  model: {
    prop: 'showSession',
    event: 'state'
  },
  data(){
    return {/*
      sessionLoading: false,
      sessionList: [],
      sessionTimeList: [],*/
      // sessionMark: '',
      isEditSessions: false,
      editSessions: [],
      needEdit: false,
    }
  },
  watch: {
    sessionList: {
      handler(val) {
        this.sessionTimeList = this.formatterList(val);
      },
      immediate: true
    }
  },
  mounted(){
// this.getSessionList()
  },
  methods:{
    handleDeleteSessions(){

    },
    formatterList(list) {
      //根据时间分为 今天 近一周 近一个月 近一年 和更早，分类加到sessions中。praseTime没有周的格式化，所以没有周
      let sessionList = [];
      let type = this.$t('privateDomain.earlier');
      let now = new Date();
      list.forEach(item => {
        if (parseTime(item.createTime, '{y}-{m}-{d}') === parseTime(now, '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.today');
        } else if (parseTime(item.createTime, '{y}-{m}-{d}') >= parseTime(new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000), '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.pastWeek');
        } else if (parseTime(item.createTime, '{y}-{m}-{d}') >= parseTime(new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000), '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.pastMonth');
        } else if (parseTime(item.createTime, '{y}-{m}-{d}') >= parseTime(new Date(now.getTime() - 365 * 24 * 60 * 60 * 1000), '{y}-{m}-{d}')) {
          type = this.$t('privateDomain.pastYear');
        } else {
          type = this.$t('privateDomain.earlier');
        }

        let data = {
          createTime: item.createTime,
          sessionName: item.sessionName || item.sessionTitle,
          sessionMark: item.sessionMark || item.sessionId || 0,
          id: item.id,
          session: item,
        }

        let index = sessionList.findIndex(session => session.type === type);

        if (index === -1) {
          sessionList.push({
            type: type,
            sessions: [data]
          });
        } else {
          sessionList[index].sessions.push(data);
        }
      })
      return sessionList;
    },
    handleDeleteSession(id, sessionMark) {
      this.$confirm(this.$t('privateDomain.okDeleted'), this.$t('warding'), {
        confirmButtonText: this.$t('ok'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        this.$emit('delete', id, sessionMark);
      }).catch(() => {
      });
    },
    handleNewSession() {
      // this.isNewSession = true;
      this.$emit('new');
    },
    handleClickSession(item) {
      if (this.isEditSessions) {
        if (this.editSessions.includes(item.id)) {
          this.editSessions = this.editSessions.filter(session => session !== item.id);
        } else {
          this.editSessions.push(item.id);
        }
      } else {
        this.handleUseSession(item.sessionMark, item.session);
      }
    },
    handleUseSession(sessionMark, session) {
      this.$emit('use', sessionMark, session);
    },
  }
}
</script>
<style scoped lang="scss">

.fold-button {
  width: fit-content;
  border-radius: 4px 0px 0px 4px;
  background: #E9EAFB;
  padding: 4px;
  cursor: pointer;

  position: absolute;
  right: 0;
  top: 20px;
  transition: all 0.3s;

  &.unfold {
    left: 0;
    transition: all 0.3s;

  }

  &.hide {
    opacity: 0;
    pointer-events: none;
    transition: all 0.3s;
  }

  &:hover {
    box-shadow: 0 0 2px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s;
  }
}


.session-card {
  width: 240px;
  position: relative;
  transition: all 0.3s;
  margin-right: 10px;
  border: none;

  &.hide {
    width: 0;
    overflow: hidden;
    border: none;
    margin-right: 0;
    transition: all 0.3s;
  }
}

.session-item {
  padding: 10px 10px 10px 20px;
  background-color: #ffffff80;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.5);
  line-height: 20px;
  transition: all 0.3s;
  border: 1px solid transparent;
  overflow: hidden;

  &:hover {
    background-color: #EBF0F9;
    border-radius: 4px;
    transition: all 0.3s;
  }

  .tool-item-button:hover {
    background-color: #dee4ed;
  }

  &.active {
    background: rgba(134, 167, 229, 0.27);

    color: #254F91;

    .tool-item-button {
      color: #254F91;

      &:hover {
        background: rgba(125, 158, 220, 0.3);
      }
    }
  }

  &.is-chosen {
    background: #fffDFD;
    border: 1px solid #e1bfbf;

    &.active {
      color: #ca4747;
    }

    &:hover {
      background: #fffDFD;
    }

  }


}

</style>
