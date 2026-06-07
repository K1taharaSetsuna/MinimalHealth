<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import StatusBar from '../components/StatusBar.vue'
import { getReminders, updateReminders, type ReminderItem } from '../api/reminders'
const router = useRouter()
const toggles = ref<ReminderItem[]>([])

onMounted(async () => {
  try { const res = await getReminders(); toggles.value = res.data.data } catch (e) {}
})

async function handleToggle(idx: number) {
  const item = toggles.value[idx]
  item.on = !item.on
  try {
    await updateReminders({
      waterEnabled: toggles.value[0]?.on,
      sleepEnabled: toggles.value[1]?.on,
      exerciseEnabled: toggles.value[2]?.on,
    })
  } catch (e) { item.on = !item.on }
}
</script>
<template>
  <div class="page reminders">
    <StatusBar time="21:30" />
    <div class="content">
      <div class="header">
        <button class="back" @click="router.push('/profile')">←</button>
        <h2>提醒设置</h2>
      </div>
      <div class="list">
        <div class="item" v-for="(t, i) in toggles" :key="t.name">
          <div class="item-info"><span class="item-name">{{ t.name }}</span><span class="item-desc">{{ t.desc }}</span></div>
          <button class="toggle" :class="{ active: t.on }" @click="handleToggle(i)">
            <span class="toggle-knob"></span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped>
.reminders { width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary); display: flex; flex-direction: column; }
.content { flex: 1; overflow-y: auto; padding: 0 24px; display: flex; flex-direction: column; gap: 28px; padding-bottom: 24px; }
.header { display: flex; align-items: center; gap: 16px; padding-top: 24px; }
.back { width: 36px; height: 36px; background: var(--bg-white); border: none; border-radius: 18px; font-size: 18px; color: var(--text-primary); cursor: pointer; display: flex; align-items: center; justify-content: center; }
.header h2 { font-size: 22px; font-weight: var(--font-light); color: var(--text-primary); }
.list { background: var(--bg-white); border-radius: var(--radius-lg); padding: 8px; display: flex; flex-direction: column; gap: 4px; }
.item {
  display: flex; align-items: center; justify-content: space-between;
  padding: 14px 18px; border-radius: var(--radius-sm);
}
.item-info { display: flex; flex-direction: column; gap: 2px; }
.item-name { font-size: 15px; font-weight: var(--font-medium); color: var(--text-primary); }
.item-desc { font-size: 13px; color: var(--text-secondary); }
.toggle {
  width: 48px; height: 28px; border-radius: 14px; border: none;
  background: #D1D5DB; cursor: pointer; position: relative; transition: background 0.2s;
}
.toggle.active { background: var(--accent); }
.toggle-knob {
  position: absolute; top: 3px; left: 3px; width: 22px; height: 22px;
  border-radius: 11px; background: #fff; transition: left 0.2s;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}
.toggle.active .toggle-knob { left: 23px; }
</style>
