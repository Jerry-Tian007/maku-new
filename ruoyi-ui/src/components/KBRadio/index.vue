<template>
  <div class="flex-layout align-center" style="gap: 6px;" :title="radioValue">
    <div class="circle-icon">
      <span>{{radioValue.charAt(0)}}</span>
    </div>
    <span class="span-label">
      {{radioValue}}
    </span>
  </div>
</template>
<script>
export default {
  name: 'KBRadio',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      radioValue: this.value
    };
  },
  watch: {
    value(val) {
      this.radioValue = val;
    }
  },
  methods: {
  }
}
</script>
<style scoped lang="scss">
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
}
.span-label{
  font-weight: 400;
  font-size: 14px;
  color: rgba(44,44,44,0.7);
  line-height: 20px;
  flex: 1;

  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
