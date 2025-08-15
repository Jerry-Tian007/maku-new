<template>

</template>
<script>
import {getToken} from "@/utils/auth";

export default {
  name: 'WebSocKetComponent',
  data() {
    return {
      socket: null
    }
  },
  methods: {
    init(path, data) {
      let that = this;

      this.socket = new WebSocket(path, [getToken()]);

      this.socket.onopen = function () {
        console.log('WebSocket open');
        that.socket.send(JSON.stringify(data));
      };
      this.socket.onmessage = function (msg) {
        console.log('WebSocket message');
        that.$emit('message', msg);
      };
      this.socket.onclose = function () {
        console.log('WebSocket close');
        that.$emit('close');
      };
      this.socket.onerror = function () {
        console.log('WebSocket error');
        that.$emit('error', '网络连接出现异常，请稍后重试');
      };
    },
    close(){
      this.socket.close();
    },
  }
}
</script>
