<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import StatusBar from '../components/StatusBar.vue'
import TabBar from '../components/TabBar.vue'
import { getUserProfile, type UserProfileData } from '../api/user'
const router = useRouter()
const activeTab = ref('profile')
const user = ref<UserProfileData | null>(null)

onMounted(async () => {
  try { const res = await getUserProfile(); user.value = res.data.data } catch (e) {}
})

function onTab(t: string) {
  if (t === 'dashboard') router.push('/dashboard')
  else if (t === 'timeline') router.push('/timeline')
  else if (t === 'diet') router.push('/diet')
  else if (t === 'exercise') router.push('/exercise')
  else if (t === 'profile') return
}

function handleLogout() {
  localStorage.removeItem('accessToken')
  localStorage.removeItem('refreshToken')
  router.push('/')
}

const menuItems = [
  { name: '健康目标', icon: '🎯', path: '/profile/goals' },
  { name: '身体数据', icon: '📐', path: '/profile/body' },
  { name: '提醒设置', icon: '🔔', path: '/reminders' },
  { name: '隐私与数据', icon: '🔒', path: '' },
]
</script>
<template>
  <div class="page profile">
    <StatusBar time="22:08" />
    <div class="content">
      <div class="profile-card">
        <div class="avatar"><span>{{ user?.avatarInitial || '用' }}</span></div>
        <div class="user-info">
          <span class="user-name">{{ user?.name || '未设置姓名' }}</span>
          <span class="user-email">{{ user?.email || '' }}</span>
        </div>
        <span class="arrow">›</span>
      </div>
      <div class="menu">
        <button class="menu-item" v-for="m in menuItems" :key="m.name" @click="m.path && router.push(m.path)">
          <span class="menu-icon">{{ m.icon }}</span>
          <span class="menu-name">{{ m.name }}</span>
          <span class="menu-arrow">›</span>
        </button>
      </div>
      <button class="logout" @click="handleLogout">退出登录</button>
    </div>
    <TabBar :activeTab="activeTab" @update:activeTab="onTab" />
  </div>
</template>
<style scoped>
.profile { width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary); display: flex; flex-direction: column; }
.content { flex: 1; overflow-y: auto; -webkit-overflow-scrolling: touch; padding: 0 24px; display: flex; flex-direction: column; gap: 28px; padding-bottom: 100px; }
.profile-card {
  display: flex; align-items: center; gap: 16px;
  background: var(--bg-white); border-radius: var(--radius-xl); padding: 24px; margin-top: 24px;
}
.avatar {
  width: 64px; height: 64px; border-radius: 32px; background: var(--bg-light-green);
  display: flex; align-items: center; justify-content: center;
  font-size: 24px; color: var(--accent); font-weight: var(--font-medium);
}
.user-info { flex: 1; display: flex; flex-direction: column; gap: 4px; }
.user-name { font-size: 18px; font-weight: var(--font-medium); color: var(--text-primary); }
.user-email { font-size: 14px; color: var(--text-secondary); }
.arrow { font-size: 24px; color: var(--text-placeholder); }
.menu { background: var(--bg-white); border-radius: var(--radius-lg); padding: 8px; display: flex; flex-direction: column; gap: 2px; }
.menu-item {
  display: flex; align-items: center; gap: 14px; padding: 14px 18px;
  border: none; background: none; border-radius: var(--radius-sm);
  cursor: pointer; font-family: Inter, sans-serif; text-align: left; width: 100%;
}
.menu-item:hover { background: var(--bg-light); }
.menu-icon { font-size: 18px; }
.menu-name { flex: 1; font-size: 15px; font-weight: var(--font-medium); color: var(--text-primary); }
.menu-arrow { font-size: 18px; color: var(--text-placeholder); }
.logout {
  width: 100%; height: 52px; background: var(--bg-white); border: none;
  border-radius: var(--radius-md); color: var(--error); font-size: 15px;
  font-weight: var(--font-medium); font-family: Inter, sans-serif; cursor: pointer;
}
</style>
