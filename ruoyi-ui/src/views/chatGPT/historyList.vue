<template>
  <history-list-mobile v-if="isMobile"></history-list-mobile>
  <history-list-p-c v-else></history-list-p-c>
</template>
<script>
import HistoryListPC from "./historyListPC";
import HistoryListMobile from "./historyListMobile";
export default {
  components: {HistoryListPC,HistoryListMobile},
  data() {
    return {
      isMobile: false,
    }
  },
  mounted() {
    if (document.body.getBoundingClientRect().width - 1 < 992) {
      this.isMobile = true;
    }
  }
}
</script>
