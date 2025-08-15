<template>
  <div
    style="width: 100%; height: 100%; position:absolute;"
    :class="{'background': background, 'padding': padding}"
    class="flex-layout"
    v-loading="loading">
    <iframe
      v-if="src"
      :src="src"
      class="flex-1"
      style="width: 100%; height: 100%;"
      :class="{'border': border}"
      frameborder="0"
      allow="microphone">
    </iframe>
  </div>
</template>
<script>
export default {
  name: "SinglePage",
  // dicts: ['iframe_url'],
  data() {
    return {
      src: '',
      label: '',
      loading: true,
      background: false,
      padding: false,
      border: false,
    }
  },
  mounted() {
    if (this.$route.query.name) {
      this.label = this.$route.query.name;
    }
    this.background = this.$route.query.bg;
    this.padding = this.$route.query.pd;
    this.border = this.$route.query.bd;

    this.getDicts("iframe_url").then(response => {
      let obj = response.data.find(item => item.dictLabel === this.label);
      if (obj) {
        this.src = obj.dictValue;
      }
    }).finally(() => {
      this.loading = false;
    });
  },
};
</script>
<style scoped lang="scss">
.background {
  background-color: #FFFFFF40;
}

.padding {
  padding: 20px;
}

.border {
  border-radius: 10px;
}
</style>
