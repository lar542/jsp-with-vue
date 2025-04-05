<template>
  <div class="container">
    <h2>index.vue</h2>
    <div>{{ message }}</div>
    <button @click="changeMessage">메시지 버튼</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      message: '기본 메시지'
    }
  },
  methods: {
    changeMessage() {
      this.message = '메시지 변경!';
    }
  }
}
</script>

<style>
.container {
  background-color: chartreuse;
}
</style>