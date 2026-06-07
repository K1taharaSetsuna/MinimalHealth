<script setup lang="ts">
import { useRouter } from 'vue-router'
const router = useRouter()
</script>
<template>
  <div class="page splash">
    <div class="content">
      <div class="logo-wrap">
        <img src="/assets/svg/2_250.svg" alt="" class="app-icon" />
      </div>
      <div class="text">
        <h1 class="name">MinimalHealth Pro</h1>
        <p class="tagline">你的个人健康助手</p>
      </div>
    </div>
    <button class="btn" @click="router.push('/login-password')">开始</button>
  </div>
</template>
<style scoped>
.splash {
  width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary);
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  position: relative;
}
.content { display: flex; flex-direction: column; align-items: center; gap: 48px; }
.logo-wrap {
  width: 80px; height: 80px; background: var(--accent); border-radius: 22px;
  display: flex; align-items: center; justify-content: center;
}
.app-icon { width: 40px; height: 40px; filter: brightness(0) invert(1); }
.text { display: flex; flex-direction: column; align-items: center; gap: 8px; }
.name { font-size: 24px; font-weight: var(--font-light); color: var(--text-primary); }
.tagline { font-size: 15px; color: var(--text-secondary); }
.btn {
  position: absolute; bottom: 64px; left: 24px; right: 24px; width: auto; height: 56px;
  background: var(--accent); border: none; border-radius: var(--radius-pill);
  color: #fff; font-size: 17px; font-weight: var(--font-medium);
  font-family: Inter, sans-serif; cursor: pointer;
}
</style>
