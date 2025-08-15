<script>
export default {
  created() {
    const {params, query} = this.$route
    console.log(query)
    if (query.code && query.businessId ) {
      if(!Array.isArray(query.businessId)){
        query.businessId = [query.businessId];
      }
      this.$store.dispatch("LoginWeChat", query).then(() => {
        this.$router.push({path: "/DataChat"}).catch(() => {});
      }).catch(() => {
        this.$router.replace({path: "/WeChatError", query:{type:'error'}})
      });
    } else {
      this.$router.replace({path: "/WeChatError", query:{type:'query'}})
    }
  },
  render: function (h) {
    return h() // avoid warning message
  }
}
</script>
