<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import StatusBar from '../components/StatusBar.vue'
import { updateProfileGoals } from '../api/profile'
import { getUserProfile } from '../api/user'
const router = useRouter()
const goals = ref({ steps: true, sleep: false, water: false })
let isWizard = true

onMounted(async () => {
  try {
    const res = await getUserProfile()
    isWizard = !res.data.data.profileComplete
  } catch (e) {}
})

async function handleFinish() {
  try {
    await updateProfileGoals(
      goals.value.steps ? 8000 : 0,
      goals.value.sleep ? 8.0 : 0,
      goals.value.water ? 2500 : 0
    )
    router.push(isWizard ? '/dashboard' : '/profile')
  } catch (e: any) { alert(e.response?.data?.message || '保存失败') }
}
</script>
<template>
  <div class="page profile-goals">
    <StatusBar time="9:42" />
    <div class="content">
      <div class="progress fill-full"></div>
      <div class="header">
        <span class="step">3 / 3</span>
        <h2>健康目标</h2>
        <p>选择你最关心的健康目标，我们会据此调整每日建议</p>
      </div>
      <div class="goals">
        <div class="goal-card" :class="{ active: goals.steps }" @click="goals.steps = !goals.steps">
          <div class="info"><span class="g-title">每日步数</span><span class="g-value">8,000 步</span></div>
          <div class="check-box" :class="{ checked: goals.steps }"><svg v-if="goals.steps" width="14" height="14" viewBox="0 0 24 24" fill="white"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41L9 16.17z"/></svg></div>
        </div>
        <div class="goal-card" :class="{ active: goals.sleep }" @click="goals.sleep = !goals.sleep">
          <div class="info"><span class="g-title">改善睡眠</span><span class="g-value">目标 8 小时</span></div>
          <div class="check-box" :class="{ checked: goals.sleep }"><svg v-if="goals.sleep" width="14" height="14" viewBox="0 0 24 24" fill="white"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41L9 16.17z"/></svg></div>
        </div>
        <div class="goal-card" :class="{ active: goals.water }" @click="goals.water = !goals.water">
          <div class="info"><span class="g-title">充分补水</span><span class="g-value">每日 2.5L</span></div>
          <div class="check-box" :class="{ checked: goals.water }"><svg v-if="goals.water" width="14" height="14" viewBox="0 0 24 24" fill="white"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41L9 16.17z"/></svg></div>
        </div>
      </div>
      <button class="btn" @click="handleFinish">开始使用</button>
    </div>
  </div>
</template>
<style scoped>
.profile-goals { width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary); display: flex; flex-direction: column; }
.content { flex: 1; overflow-y: auto; -webkit-overflow-scrolling: touch; padding: 0 24px; display: flex; flex-direction: column; gap: 32px; }
.progress { width: 100%; height: 4px; background: var(--accent); border-radius: 2px; margin-top: 24px; }
.header { display: flex; flex-direction: column; gap: 8px; }
.step { font-size: 13px; color: var(--accent); }
.header h2 { font-size: 28px; font-weight: var(--font-light); color: var(--text-primary); }
.header p { font-size: 14px; color: var(--text-secondary); }
.goals { display: flex; flex-direction: column; gap: 12px; }
.goal-card {
  display: flex; align-items: center; justify-content: space-between;
  padding: 14px 18px; border-radius: var(--radius-lg); cursor: pointer;
  background: var(--bg-white);
}
.goal-card.active { background: var(--bg-light-green); }
.info { display: flex; flex-direction: column; gap: 4px; }
.g-title { font-size: 16px; font-weight: var(--font-medium); color: var(--text-primary); }
.g-value { font-size: 13px; color: var(--text-secondary); }
.check-box {
  width: 24px; height: 24px; border-radius: 12px;
  background: var(--border); display: flex; align-items: center; justify-content: center;
}
.check-box.checked { background: var(--accent); }
.btn { width: 100%; height: 56px; background: var(--accent); border: none; border-radius: var(--radius-pill); color: #fff; font-size: 17px; font-weight: var(--font-medium); font-family: Inter, sans-serif; cursor: pointer; margin-bottom: 24px; }
</style>
