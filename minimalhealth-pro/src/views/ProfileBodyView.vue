<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import StatusBar from '../components/StatusBar.vue'
import { updateProfileBody } from '../api/profile'
import { getUserProfile } from '../api/user'
const router = useRouter()
const heightCm = ref('')
const weightKg = ref('')
const activity = ref<'sedentary' | 'moderate'>('moderate')
let isWizard = true

onMounted(async () => {
  try {
    const res = await getUserProfile()
    const u = res.data.data
    if (u.heightCm) heightCm.value = String(u.heightCm)
    if (u.weightKg) weightKg.value = String(u.weightKg)
    if (u.activityLevel) activity.value = u.activityLevel as 'sedentary' | 'moderate'
    isWizard = !u.profileComplete
  } catch (e) {}
})

async function handleNext() {
  if (!heightCm.value || !weightKg.value) { alert('请填写完整信息'); return }
  try {
    await updateProfileBody(parseFloat(heightCm.value), parseFloat(weightKg.value), activity.value)
    router.push(isWizard ? '/profile/goals' : '/profile')
  } catch (e: any) { alert(e.response?.data?.message || '保存失败') }
}
</script>
<template>
  <div class="page profile-body">
    <StatusBar time="9:42" />
    <div class="content">
      <div class="progress"><div class="fill" style="width:67%"></div></div>
      <div class="header">
        <span class="step">2 / 3</span>
        <h2>身体数据</h2>
        <p>这些数据将用于计算你的健康指数和运动建议</p>
      </div>
      <div class="fields">
        <div class="field"><label>身高 (cm)</label><div class="input"><input type="number" placeholder="请输入身高" v-model="heightCm" /></div></div>
        <div class="field"><label>体重 (kg)</label><div class="input"><input type="number" placeholder="请输入体重" v-model="weightKg" /></div></div>
        <div class="field">
          <label>活动水平</label>
          <div class="options">
            <button :class="{ active: activity==='sedentary' }" @click="activity='sedentary'">
              <span class="o-title">久坐少动</span><span class="o-desc">几乎不运动</span>
              <svg v-if="activity==='sedentary'" class="check" width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41L9 16.17z"/></svg>
            </button>
            <button :class="{ active: activity==='moderate' }" @click="activity='moderate'">
              <span class="o-title">适度运动</span><span class="o-desc">每周 2-4 次</span>
              <svg v-if="activity==='moderate'" class="check" width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41L9 16.17z"/></svg>
            </button>
          </div>
        </div>
      </div>
      <button class="btn" @click="handleNext">下一步</button>
    </div>
  </div>
</template>
<style scoped>
.profile-body { width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary); display: flex; flex-direction: column; }
.content { flex: 1; overflow-y: auto; -webkit-overflow-scrolling: touch; padding: 0 24px; display: flex; flex-direction: column; gap: 32px; }
.progress { width: 100%; height: 4px; background: var(--border); border-radius: 2px; margin-top: 24px; }
.fill { height: 100%; background: var(--accent); border-radius: 2px; transition: width 0.3s; }
.header { display: flex; flex-direction: column; gap: 8px; }
.step { font-size: 13px; color: var(--accent); }
.header h2 { font-size: 28px; font-weight: var(--font-light); color: var(--text-primary); }
.header p { font-size: 14px; color: var(--text-secondary); }
.fields { display: flex; flex-direction: column; gap: 20px; }
.field { display: flex; flex-direction: column; gap: 8px; }
.field label { font-size: 13px; font-weight: var(--font-medium); color: var(--text-primary); }
.input { height: 52px; background: var(--bg-white); border-radius: var(--radius-md); display: flex; align-items: center; padding: 0 16px; }
.input input { border: none; outline: none; font-size: 15px; color: var(--text-primary); font-family: Inter, sans-serif; width: 100%; }
.input input::placeholder { color: var(--text-placeholder); }
.options { display: flex; flex-direction: column; gap: 10px; }
.options button {
  display: flex; align-items: center; gap: 12px; padding: 14px 16px;
  background: var(--bg-white); border: none; border-radius: var(--radius-md);
  cursor: pointer; text-align: left; font-family: Inter, sans-serif;
}
.options button.active { background: var(--bg-light-green); }
.o-title { font-size: 15px; font-weight: var(--font-medium); color: var(--text-primary); }
.o-desc { font-size: 13px; color: var(--text-secondary); }
.check { margin-left: auto; color: var(--accent); }
.btn { width: 100%; height: 56px; background: var(--accent); border: none; border-radius: var(--radius-pill); color: #fff; font-size: 17px; font-weight: var(--font-medium); font-family: Inter, sans-serif; cursor: pointer; margin-bottom: 24px; }
</style>
