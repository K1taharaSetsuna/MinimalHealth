<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { smsLogin, sendSms } from '../api/auth'
const router = useRouter()
const phone = ref('')
const code = ref('')
const sending = ref(false)
const countdown = ref(0)

async function handleSendSms() {
  if (!phone.value || sending.value) return
  sending.value = true
  try {
    await sendSms(phone.value, 'login')
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) { clearInterval(timer); sending.value = false }
    }, 1000)
  } catch (e: any) {
    alert(e.response?.data?.message || '发送失败')
    sending.value = false
  }
}

async function handleLogin() {
  if (!phone.value || !code.value) return
  try {
    const res = await smsLogin(phone.value, code.value)
    const data = res.data.data
    localStorage.setItem('accessToken', data.accessToken)
    localStorage.setItem('refreshToken', data.refreshToken)
    if (data.user.profileComplete) {
      router.push('/dashboard')
    } else {
      router.push('/profile/basic')
    }
  } catch (e: any) {
    alert(e.response?.data?.message || '登录失败')
  }
}
</script>
<template>
  <div class="page login-sms">
    <div class="content">
      <div class="logo"></div>
      <div class="tab-bar-custom">
        <span class="tab active">验证码登录</span>
        <span class="tab" @click="router.push('/login-password')">账号密码登录</span>
      </div>
      <div class="text-area">
        <h2>登录 MinimalHealth</h2>
        <p>登录你的健康账户</p>
      </div>
      <div class="input-box">
        <span class="prefix">+86</span>
        <input type="tel" placeholder="手机号码" v-model="phone" />
      </div>
      <button class="btn" @click="handleSendSms" :disabled="sending">{{ countdown ? countdown + 's' : '发送验证码' }}</button>
      <div class="input-box">
        <input type="text" placeholder="请输入验证码" v-model="code" maxlength="6" />
      </div>
      <button class="btn" @click="handleLogin">登录</button>
      <p class="link" @click="router.push('/register')">还没有账号？注册</p>
    </div>
  </div>
</template>
<style scoped>
.login-sms {
  width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary);
  display: flex; align-items: center; justify-content: center;
}
.content { width: 100%; display: flex; flex-direction: column; align-items: center; gap: 24px; }
.logo { width: 64px; height: 64px; background: var(--accent); border-radius: var(--radius-lg); }
.tab-bar-custom {
  width: 100%; height: 44px; display: flex; align-items: center;
  background: var(--bg-light); border-radius: 24px; padding: 4px;
}
.tab-bar-custom .tab {
  flex: 1; height: 36px; display: flex; align-items: center; justify-content: center;
  font-size: 14px; color: var(--text-secondary); border-radius: 20px; cursor: pointer;
}
.tab-bar-custom .tab.active { background: var(--accent); color: #fff; font-weight: var(--font-medium); }
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
