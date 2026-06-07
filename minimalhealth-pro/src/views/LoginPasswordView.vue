<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { passwordLogin } from '../api/auth'
const router = useRouter()
const account = ref('')
const password = ref('')

async function handleLogin() {
  if (!account.value || !password.value) return
  try {
    const res = await passwordLogin(account.value, password.value)
    const data = res.data.data
    localStorage.setItem('accessToken', data.accessToken)
    localStorage.setItem('refreshToken', data.refreshToken)
    if (data.user.profileComplete) {
      router.push('/dashboard')
    } else {
      router.push('/profile/basic')
    }
  } catch (e: any) {
    const msg = e.response?.data?.message || "登录失败"
    alert(msg)
  }
}
</script>
<template>
  <div class="page login-pwd">
    <div class="content">
      <div class="logo"></div>
      <div class="text-area">
        <h2>登录 MinimalHealth</h2>
        <p>输入你的账号和密码</p>
      </div>
      <div class="input-box"><input type="text" placeholder="手机号" v-model="account" /></div>
      <div class="input-box"><input type="password" placeholder="密码" v-model="password" /></div>
      <button class="btn" @click="handleLogin">登录</button>
      <p class="link" @click="router.push('/register')">还没有账号？注册</p>
    </div>
  </div>
</template>
<style scoped>
.login-pwd {
  width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary);
  display: flex; align-items: center; justify-content: center;
}
.content { width: 100%; display: flex; flex-direction: column; align-items: center; gap: 24px; padding: 0 36px; }
.logo { width: 64px; height: 64px; background: var(--accent); border-radius: var(--radius-lg); }
.text-area { text-align: center; display: flex; flex-direction: column; gap: 8px; }
.text-area h2 { font-size: 22px; font-weight: var(--font-light); color: var(--text-primary); }
.text-area p { font-size: 14px; color: var(--text-secondary); }
.input-box {
  width: 100%; height: 56px; background: var(--bg-white); border-radius: var(--radius-pill);
  display: flex; align-items: center; padding: 0 20px;
}
.input-box input { border: none; outline: none; font-size: 16px; color: var(--text-primary); font-family: Inter, sans-serif; width: 100%; }
.input-box input::placeholder { color: var(--text-placeholder); }
.btn {
  width: 100%; height: 56px; background: var(--accent); border: none;
  border-radius: var(--radius-pill); color: #fff; font-size: 17px;
  font-weight: var(--font-medium); font-family: Inter, sans-serif; cursor: pointer;
}
.link { font-size: 14px; color: var(--accent); cursor: pointer; }
</style>
