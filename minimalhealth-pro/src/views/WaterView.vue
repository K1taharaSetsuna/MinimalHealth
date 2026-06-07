<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import StatusBar from '../components/StatusBar.vue'
import TabBar from '../components/TabBar.vue'
import { getWater, addWater, deleteWater } from '../api/water'
const router = useRouter()
const activeTab = ref('diet')
const water = ref({ currentMl: 0, goalMl: 2500, percentage: 0, logs: [] as any[] })

onMounted(async () => {
  try {
    const res = await getWater()
    water.value = res.data.data
  } catch (e) {}
})

async function handleDelete(id: number) {
  if (!confirm('确定删除这条记录？')) return
  try {
    await deleteWater(id)
    // Refresh
    const res = await getWater()
    water.value = res.data.data
  } catch (e: any) { alert('删除失败') }
}

async function handleAddWater(ml: number) {
  try {
    const res = await addWater(ml)
    water.value = res.data.data
  } catch (e: any) {
    alert(e.response?.data?.message || '操作失败')
  }
}

function onTab(t: string) {
  if (t === 'dashboard') router.push('/dashboard')
  else if (t === 'timeline') router.push('/timeline')
  else if (t === 'diet') return
  else if (t === 'exercise') router.push('/exercise')
  else if (t === 'profile') router.push('/profile')
}
</script>
<template>
  <div class="page water">
    <StatusBar time="11:00" />
    <div class="content">
      <div class="back-row"><button class="back" @click="router.push('/diet')">←</button></div>
      <h2>饮水追踪</h2>
      <div class="circle-wrap">
        <svg width="200" height="200" viewBox="0 0 200 200">
          <circle cx="100" cy="100" r="90" stroke="#E8EBED" stroke-width="12" fill="none"/>
          <circle cx="100" cy="100" r="90" stroke="#8FA89B" stroke-width="12" fill="none"
            :stroke-dasharray="565"
            :stroke-dashoffset="565 - 565 * water.percentage / 100"
            stroke-linecap="round" transform="rotate(-90 100 100)"/>
        </svg>
        <div class="circle-text">
          <span class="big">{{ (water.currentMl / 1000).toFixed(1) }}<span class="unit">L</span></span>
          <span class="sub">目标 {{ (water.goalMl / 1000).toFixed(1) }}L · 已达成 {{ water.percentage }}%</span>
        </div>
      </div>
      <div class="add-row">
        <button class="add-btn" @click="handleAddWater(200)">+200ml</button>
        <button class="add-btn accent" @click="handleAddWater(300)">+300ml</button>
        <button class="add-btn" @click="handleAddWater(500)">+500ml</button>
      </div>
      <div class="log">
        <span class="log-title">今日记录</span>
        <div class="log-item" v-for="l in water.logs" :key="l.id" @click="handleDelete(l.id)">
          <span class="l-amount">{{ l.amountMl }}ml</span>
          <span class="l-time">{{ l.recordedAt }}</span>
        </div>
      </div>
    </div>
    <TabBar :activeTab="activeTab" @update:activeTab="onTab" />
  </div>
</template>
<style scoped>
.water { width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary); display: flex; flex-direction: column; }
.content { flex: 1; overflow-y: auto; -webkit-overflow-scrolling: touch; padding: 0 24px; display: flex; flex-direction: column; align-items: center; gap: 24px; padding-bottom: 100px; }
.back-row { width: 100%; padding-top: 24px; }
.back { width: 36px; height: 36px; background: var(--bg-white); border: none; border-radius: 18px; font-size: 18px; color: var(--text-primary); cursor: pointer; }
.content h2 { font-size: 22px; font-weight: var(--font-light); color: var(--text-primary); }
.circle-wrap { position: relative; width: 200px; height: 200px; display: flex; align-items: center; justify-content: center; }
.circle-text { position: absolute; display: flex; flex-direction: column; align-items: center; gap: 6px; }
.big { font-size: 48px; font-weight: var(--font-light); color: var(--accent); }
.unit { font-size: 24px; }
.sub { font-size: 13px; color: var(--text-secondary); }
.add-row { display: flex; gap: 12px; }
.add-btn {
  padding: 14px 20px; background: var(--bg-white); border: none;
  border-radius: var(--radius-pill); font-size: 15px; font-family: Inter, sans-serif;
  color: var(--text-primary); cursor: pointer;
}
.add-btn.accent { background: var(--accent); color: #fff; }
.log { width: 100%; display: flex; flex-direction: column; gap: 12px; }
.log-title { font-size: 16px; font-weight: var(--font-medium); color: var(--text-primary); }
.log-item { display: flex; align-items: center; justify-content: space-between; padding: 14px 18px; background: var(--bg-white); border-radius: var(--radius-md); }
.l-amount { font-size: 15px; font-weight: var(--font-medium); color: var(--accent); }
.l-time { font-size: 13px; color: var(--text-secondary); }
</style>
