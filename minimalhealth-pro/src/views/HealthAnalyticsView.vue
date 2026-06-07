<script setup lang="ts">
import { ref, onMounted } from 'vue'
import StatusBar from '../components/StatusBar.vue'
import { getAnalytics, type AnalyticsData } from '../api/analytics'
const data = ref<AnalyticsData | null>(null)

onMounted(async () => {
  try { const res = await getAnalytics(); data.value = res.data.data } catch (e) {}
})
</script>
<template>
  <div class="page analytics">
    <StatusBar time="20:00" />
    <div class="content">
      <div class="header">
        <h2>健康分析</h2>
        <button class="period">本周 ▾</button>
      </div>
      <div class="score-card">
        <div class="sc-left"><span class="sc-label">综合评分</span><span class="sc-val">{{ data?.overallScore ?? 85 }}</span></div>
        <div class="sc-trend">
          <span class="trend-up" v-if="(data?.scoreChange ?? 0) > 0">↑ +{{ data?.scoreChange }}</span>
          <span class="trend-down" v-else-if="(data?.scoreChange ?? 0) < 0">↓ {{ data?.scoreChange }}</span>
          <span class="trend-label">vs 上周</span>
        </div>
      </div>
      <div class="metrics-grid">
        <div class="m-item" v-for="m in (data?.metrics || [])" :key="m.label">
          <span class="m-value">{{ m.value }}</span>
          <span class="m-label">{{ m.label }}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped>
.analytics { width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary); display: flex; flex-direction: column; }
.content { flex: 1; overflow-y: auto; padding: 0 24px; display: flex; flex-direction: column; gap: 28px; padding-bottom: 24px; }
.header { display: flex; align-items: center; justify-content: space-between; padding-top: 24px; }
.header h2 { font-size: 28px; font-weight: var(--font-light); color: var(--text-primary); }
.period { font-size: 14px; color: var(--text-secondary); background: none; border: none; font-family: Inter, sans-serif; cursor: pointer; }
.score-card {
  display: flex; align-items: center; justify-content: space-between;
  background: var(--bg-white); border-radius: var(--radius-xl); padding: 24px;
}
.sc-left { display: flex; flex-direction: column; gap: 4px; }
.sc-label { font-size: 14px; color: var(--text-secondary); }
.sc-val { font-size: 48px; font-weight: var(--font-light); color: var(--accent); }
.sc-trend { display: flex; flex-direction: column; align-items: flex-end; gap: 4px; }
.trend-up { font-size: 20px; font-weight: var(--font-medium); color: var(--accent); }
.trend-down { font-size: 20px; font-weight: var(--font-medium); color: var(--error); }
.trend-label { font-size: 13px; color: var(--text-secondary); }
.metrics-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.m-item {
  background: var(--bg-white); border-radius: var(--radius-md);
  padding: 20px; display: flex; flex-direction: column; gap: 8px;
}
.m-value { font-size: 24px; font-weight: var(--font-light); color: var(--text-primary); }
.m-label { font-size: 13px; color: var(--text-secondary); }
</style>
