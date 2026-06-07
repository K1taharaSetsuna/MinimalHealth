<script setup lang="ts">
defineProps<{ activeTab?: string }>()
const emit = defineEmits<{ 'update:activeTab': [tab: string] }>()

const tabs = [
  { key: 'dashboard', label: '看板', icon: 'grid' },
  { key: 'timeline', label: '日志', icon: 'clock' },
  { key: 'diet', label: '饮食', icon: 'utensils' },
  { key: 'exercise', label: '运动', icon: 'run' },
  { key: 'profile', label: '我的', icon: 'user' },
]

function iconPath(key: string): string {
  const m: Record<string, string> = {
    grid: 'M3 3h7v7H3V3Zm11 0h7v7h-7V3ZM3 14h7v7H3v-7Zm11 0h7v7h-7v-7Z',
    clock: 'M12 2a10 10 0 1 0 10 10A10 10 0 0 0 12 2Zm0 18a8 8 0 1 1 8-8 8 8 0 0 1-8 8Zm1-13h-2v6l5.25 3.15.75-1.23-4-2.37V7Z',
    utensils: 'M8 1v7a3 3 0 0 1-6 0V1m6 0v7m7-3v11a2 2 0 0 1-2 2h-3a2 2 0 0 1-2-2V5m7 0h1a3 3 0 0 1 3 3v1a3 3 0 0 1-3 3h-1m0-7v7m-5-2a4 4 0 1 0 8 0',
    run: 'M13.5 5.5a2 2 0 1 0 0-4 2 2 0 0 0 0 4ZM7 23l3-7 2 2v7h2v-8l-2.33-2.33L13 13l5 3.5V20h2v-5l-4.67-3.13L16 8a3 3 0 0 0-5.66-1L6 14v5h2v-5l2-2-1 11Z',
    user: 'M12 12a5 5 0 1 0 0-10 5 5 0 0 0 0 10Zm0 2c-3.33 0-10 1.67-10 5v3h20v-3c0-3.33-6.67-5-10-5Z',
  }
  return m[key] || m.grid
}
</script>
<template>
  <div class="tab-bar">
    <div class="pill">
      <button v-for="t in tabs" :key="t.key"
        class="tab" :class="{ active: activeTab === t.key }"
        @click="emit('update:activeTab', t.key)">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path :d="iconPath(t.icon)"/></svg>
        <span class="label">{{ t.label }}</span>
      </button>
    </div>
  </div>
</template>
<style scoped>
.tab-bar {
  height: var(--tab-bar-h); padding: 12px 21px 21px;
  display: flex; align-items: flex-start; justify-content: center;
  flex-shrink: 0;
}
.pill {
  width: 100%; height: var(--tab-pill-h);
  display: flex; align-items: center;
  background: var(--bg-white); border: 1px solid var(--border);
  border-radius: var(--radius-full); padding: 4px;
}
.tab {
  flex: 1; height: 54px;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  gap: 4px; border: none; background: none; cursor: pointer;
  border-radius: 26px; color: var(--text-secondary);
  font-family: Inter, sans-serif; transition: all 0.2s;
}
.tab.active { background: var(--accent); color: #fff; }
.tab .label { font-size: 10px; font-weight: var(--font-medium); }
.tab.active .label { font-weight: var(--font-semibold); }
</style>
