<script setup lang="ts">
</script>

<template>
  <router-view v-slot="{ Component }">
    <transition name="slide" mode="out-in">
      <component :is="Component" />
    </transition>
  </router-view>
</template>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
html, body, #app {
  width: 100%; height: 100%;
}
body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  background: #E8EBED;
}

.slide-enter-active, .slide-leave-active { transition: all 0.35s ease; }
.slide-enter-from { opacity: 0; transform: translateX(40px); }
.slide-leave-to { opacity: 0; transform: translateX(-40px); }
</style>
