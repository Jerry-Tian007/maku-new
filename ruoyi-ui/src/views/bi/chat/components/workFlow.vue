<template>
  <el-card class="plain" style="border-radius: 6px;" :body-style="{
    padding: '8px',
    height: '100%',
    overflow: 'auto',
  }">
    <div :style="{
    display: 'flex',
    flexDirection: 'column',
    gap: '4px',
    }">
      <work-item
        v-for="(item,index) in workList"
        :key="index"
        :work="item"
      />
    </div>
  </el-card>
</template>
<script>
import WorkItem from "@/views/bi/chat/components/workItem.vue";

export default {
  name: "WorkFlow",
  components: {WorkItem},
  props:{
    workList:{
      type: Array,
      default: ()=>{
        return [];
      }
    },
  },
  data() {
    return {
    }
  }
}
</script>
