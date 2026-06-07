<script setup lang="ts">
import { ref, onMounted } from 'vue'
import StatusBar from '../components/StatusBar.vue'
import { getSuggestions, type AiSuggestionsData } from '../api/ai'
const data = ref<AiSuggestionsData | null>(null)

onMounted(async () => {
  try { const res = await getSuggestions(); data.value = res.data.data } catch (e) {}
})
</script>
<template>
  <div class="page ai">
    <StatusBar time="08:20" />
    <div class="content">
      <div class="header">
        <span class="greeting">{{ data?.greeting || '早上好' }}</span>
        <h2>今日健康建议</h2>
      </div>
      <div class="cards">
        <div class="card" v-for="s in (data?.suggestions || [])" :key="s.title">
          <div class="card-icon">{{ s.iconEmoji }}</div>
          <div class="card-body">
            <span class="card-title">{{ s.title }}</span>
            <p class="card-desc">{{ s.description }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped>
.ai { width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary); display: flex; flex-direction: column; }
.content { flex: 1; overflow-y: auto; padding: 0 24px; display: flex; flex-direction: column; gap: 24px; padding-bottom: 24px; }
.header { display: flex; flex-direction: column; gap: 8px; padding-top: 24px; }
.greeting { font-size: 14px; color: var(--text-secondary); }
.header h2 { font-size: 28px; font-weight: var(--font-light); color: var(--text-primary); }
.cards { display: flex; flex-direction: column; gap: 16px; }
.card {
  display: flex; gap: 14px; background: var(--bg-white);
  border-radius: var(--radius-lg); padding: 18px;
}
.card-icon { width: 40px; height: 40px; background: var(--bg-light-green); border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; font-size: 20px; flex-shrink: 0; }
.card-body { flex: 1; display: flex; flex-direction: column; gap: 6px; }
.card-title { font-size: 15px; font-weight: var(--font-medium); color: var(--text-primary); }
.card-desc { font-size: 13px; color: var(--text-secondary); line-height: 1.5; }
</style>
