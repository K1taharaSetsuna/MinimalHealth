<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/auth'
const router = useRouter()
const phone = ref('')
const password = ref('')
const confirmPassword = ref('')


async function handleRegister() {
  if (!phone.value || !password.value) return
  if (password.value.length < 8) { alert('密码至少8位'); return }
  if (password.value !== confirmPassword.value) { alert('两次密码不一致'); return }
  try {
    console.log('Register: calling API...', phone.value)
    const res = await register(phone.value, password.value)
    console.log('Register: success', res.status)
    const data = res.data.data
    localStorage.setItem('accessToken', data.accessToken)
    localStorage.setItem('refreshToken', data.refreshToken)
    router.push('/profile/basic')
  } catch (e: any) {
    console.error('Register FAILED:', e.message, e.code, e.response?.status, JSON.stringify(e))
    const msg = e.response?.data?.message || e.message || JSON.stringify(e)
    alert('注册失败: ' + msg)
  }
}
</script>
<template>
  <div class="page register">
    <div class="content">
      <div class="logo"></div>
      <div class="text-area">
        <h2>注册 MinimalHealth</h2>
        <p>创建你的健康账户</p>
      </div>
      <div class="input-box"><span class="prefix">+86</span><input type="tel" placeholder="手机号码" v-model="phone" /></div>
      <div class="input-box"><input type="password" placeholder="设置密码（至少8位）" v-model="password" /></div>
      <div class="input-box"><input type="password" placeholder="确认密码" v-model="confirmPassword" /></div>
      <button class="btn" @click="handleRegister">注册</button>
      <p class="link" @click="router.push('/login-password')">已有账号？登录</p>
    </div>
  </div>
</template>
<style scoped>
.register {
  width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary);
  display: flex; align-items: center; justify-content: center;
}
.content { width: 100%; display: flex; flex-direction: column; align-items: center; gap: 20px; padding: 0 36px; }
.logo { width: 64px; height: 64px; background: var(--accent); border-radius: var(--radius-lg); }
.text-area { text-align: center; display: flex; flex-direction: column; gap: 8px; }
.text-area h2 { font-size: 22px; font-weight: var(--font-light); color: var(--text-primary); }
.text-area p { font-size: 14px; color: var(--text-secondary); }
.input-box {
  width: 100%; height: 56px; background: var(--bg-white); border-radius: var(--radius-pill);
  display: flex; align-items: center; gap: 12px; padding: 0 20px;
}
.prefix { font-size: 16px; font-weight: var(--font-medium); color: var(--text-primary); }
.input-box input { border: none; outline: none; font-size: 16px; color: var(--text-primary); font-family: Inter, sans-serif; width: 100%; }
.input-box input::placeholder { color: var(--text-placeholder); }
.btn {
  width: 100%; height: 56px; background: var(--accent); border: none;
  border-radius: var(--radius-pill); color: #fff; font-size: 17px;
  font-weight: var(--font-medium); font-family: Inter, sans-serif; cursor: pointer;
}
.link { font-size: 14px; color: var(--accent); cursor: pointer; }
</style>
