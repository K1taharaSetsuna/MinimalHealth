<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import StatusBar from '../components/StatusBar.vue'
import TabBar from '../components/TabBar.vue'
import { getActivities, deleteActivity, type ActivityItem } from '../api/activity'
const router = useRouter()
const activeTab = ref('timeline')
const entries = ref<ActivityItem[]>([])

onMounted(async () => { await loadEntries() })

async function loadEntries() {
  try {
    const res = await getActivities()
    entries.value = res.data.data.content || []
  } catch (e) {}
}

async function handleDelete(id: number) {
  if (!confirm('确定删除？')) return
  try { await deleteActivity(id); await loadEntries() } catch (e) {}
}

function onTab(t: string) {
  if (t === 'dashboard') router.push('/dashboard')
  else if (t === 'timeline') return
  else if (t === 'diet') router.push('/diet')
  else if (t === 'exercise') router.push('/exercise')
  else if (t === 'profile') router.push('/profile')
}
</script>
<template>
  <div class="page timeline">
    <StatusBar time="14:30" />
    <div class="content">
      <div class="header">
        <span class="date">6月7日 · 周六</span>
        <h2>时间轴</h2>
      </div>
      <div class="tl">
        <div class="entry" v-for="e in entries" :key="e.id" @click="handleDelete(e.id)">
          <div class="dot-line">
            <div class="dot" style="background: #8FA89B"></div>
          </div>
          <div class="card">
            <div class="card-header"><span class="c-name">{{ e.name }}</span><span class="c-time">{{ e.time }}</span></div>
            <p class="c-detail">{{ e.detail }}</p>
            <span class="c-val">{{ e.valueLabel }}</span>
          </div>
        </div>
        <div class="entry-empty" v-if="entries.length === 0">
          <span class="empty-text">暂无活动记录</span>
        </div>
      </div>
    </div>
    <TabBar :activeTab="activeTab" @update:activeTab="onTab" />
  </div>
</template>
<style scoped>
.timeline { width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary); display: flex; flex-direction: column; }
.content { flex: 1; overflow-y: auto; -webkit-overflow-scrolling: touch; padding: 0 24px; display: flex; flex-direction: column; gap: 24px; padding-bottom: 100px; }
.header { display: flex; flex-direction: column; gap: 4px; padding-top: 24px; }
.date { font-size: 14px; color: var(--text-secondary); }
.header h2 { font-size: 30px; font-weight: var(--font-light); color: var(--text-primary); }
.tl { display: flex; flex-direction: column; }
.entry { display: flex; gap: 16px; padding-bottom: 100px; }
.dot-line { display: flex; flex-direction: column; align-items: center; width: 12px; flex-shrink: 0; }
.dot { width: 12px; height: 12px; border-radius: 6px; margin-top: 4px; }
.card {
  flex: 1; background: var(--bg-white); border-radius: var(--radius-md);
  padding: 16px 18px; display: flex; flex-direction: column; gap: 8px; position: relative;
}
.card-header { display: flex; align-items: center; justify-content: space-between; }
.c-name { font-size: 15px; font-weight: var(--font-medium); color: var(--text-primary); }
.c-time { font-size: 13px; color: var(--text-secondary); }
.c-detail { font-size: 13px; color: var(--text-secondary); }
.c-val { font-size: 14px; font-weight: var(--font-medium); color: var(--accent); }
.entry-empty { text-align: center; padding: 48px 0; }
.empty-text { font-size: 14px; color: var(--text-placeholder); }
</style>
