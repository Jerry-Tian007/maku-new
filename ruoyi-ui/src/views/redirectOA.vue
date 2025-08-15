<script>
export default {
  created() {
    const { params, query } = this.$route
    console.log(query)
    if(query.identityId && query.businessId && query.identityName){
      query.identityName = query.identityName.replaceAll("'","");
      this.$store.dispatch("LoginOA", query).then(() => {
        localStorage.setItem("query", JSON.stringify(query));
        this.$router.push({ path: "/DataChat" }).catch(() => { });
      }).catch(() => {
        this.loading = false;
        if (this.captchaEnabled) {
          this.getCode();
        }
      });
    } else{
      this.$router.replace({ path: "/404" })
    }
  },
  render: function(h) {
    return h() // avoid warning message
  }
}
</script>
