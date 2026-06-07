<script setup lang="ts">
defineProps<{ time?: string }>()
</script>
<template>
  <div class="status-bar">
    <span class="status-time">{{ time || '9:41' }}</span>
    <div class="status-icons">
      <svg class="icon-signal" width="18" height="14" viewBox="0 0 18 14"><rect x="0" y="10" width="3" height="4" rx="0.5" fill="#1A1A1A"/><rect x="5" y="7" width="3" height="7" rx="0.5" fill="#1A1A1A"/><rect x="10" y="4" width="3" height="10" rx="0.5" fill="#1A1A1A"/><rect x="15" y="0" width="3" height="14" rx="0.5" fill="#1A1A1A"/></svg>
      <svg class="icon-wifi" width="18" height="14" viewBox="0 0 18 14"><path d="M9 11a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3Z" fill="#1A1A1A"/><path d="M6.26 8.5a4.62 4.62 0 0 1 5.48 0" stroke="#1A1A1A" stroke-width="1.2" fill="none"/><path d="M3.67 5.5a8.27 8.27 0 0 1 10.66 0" stroke="#1A1A1A" stroke-width="1.2" fill="none"/><path d="M1.26 2.5A11.56 11.56 0 0 1 16.74 2.5" stroke="#1A1A1A" stroke-width="1.2" fill="none"/></svg>
      <svg class="icon-battery" width="27" height="14" viewBox="0 0 27 14"><rect x="0" y="0" width="24" height="14" rx="3" stroke="#1A1A1A" stroke-width="1" fill="none"/><rect x="2.5" y="2.5" width="16" height="9" rx="1.5" fill="#1A1A1A"/><rect x="25" y="4" width="2" height="6" rx="1" fill="#1A1A1A"/></svg>
    </div>
  </div>
</template>
<style scoped>
.status-bar {
  height: var(--status-bar-h); padding: 0 24px;
  display: flex; align-items: center; justify-content: space-between;
  flex-shrink: 0;
}
.status-time { font-size: 17px; font-weight: var(--font-semibold); color: var(--text-primary); }
.status-icons { display: flex; align-items: center; gap: 6px; }
</style>
