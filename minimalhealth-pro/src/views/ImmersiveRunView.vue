<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { startExercise, finishExercise } from '../api/exercise'
import client from '../api/client'

const router = useRouter()

// ===== 核心状态 =====
const isRunning = ref(true)
const elapsedSeconds = ref(0)
const distanceKm = ref(0)
const heartRate = ref(0)
const pace = ref('--')
const routeName = ref('户外跑步')
const calories = ref(0)
const showMap = ref(true)

// ===== 计时器 =====
let timerInterval: number
let heartRateInterval: number
let saveInterval: number
let currentExerciseId = 0

const elapsedTime = computed(() => {
  const m = Math.floor(elapsedSeconds.value / 60).toString().padStart(2, '0')
  const s = (elapsedSeconds.value % 60).toString().padStart(2, '0')
  return `${m}:${s}`
})

const calculatePace = () => {
  if (distanceKm.value <= 0) return '--'
  const totalMin = elapsedSeconds.value / 60
  const pm = Math.floor(totalMin / distanceKm.value)
  const ps = Math.round((totalMin / distanceKm.value - pm) * 60)
  return `${pm}'${ps.toString().padStart(2, '0')}"`
}

const currentTime = computed(() => {
  const d = new Date()
  return d.getHours().toString().padStart(2, '0') + ':' + d.getMinutes().toString().padStart(2, '0')
})

// ===== 计时控制 =====
const startTimer = () => {
  isRunning.value = true
  timerInterval = window.setInterval(() => {
    elapsedSeconds.value++
    // 模拟距离增长（~5min/km 配速）
    distanceKm.value = +(elapsedSeconds.value / 300).toFixed(2)
    pace.value = calculatePace()
    calories.value = Math.floor(elapsedSeconds.value * 0.15)
  }, 1000)
}

const togglePause = () => {
  if (isRunning.value) {
    clearInterval(timerInterval)
    isRunning.value = false
  } else {
    startTimer()
  }
}

// ===== 心率模拟 =====
const startHeartRate = () => {
  heartRate.value = 120 + Math.floor(Math.random() * 30)
  heartRateInterval = window.setInterval(() => {
    heartRate.value = 135 + Math.floor(Math.random() * 20)
  }, 3000)
}

// ===== 自动保存 =====
const autoSave = () => {
  saveInterval = window.setInterval(async () => {
    try {
      await client.post('/exercises/checkpoint', {
        exerciseId: currentExerciseId,
        elapsedSeconds: elapsedSeconds.value,
        distanceKm: distanceKm.value,
        heartRate: heartRate.value,
        calories: calories.value
      })
    } catch (e) {}
  }, 10000)
}

// ===== 结束运动 =====
const stopExercise = async () => {
  clearInterval(timerInterval)
  clearInterval(heartRateInterval)
  clearInterval(saveInterval)

  try {
    await client.post('/exercises/finish', {
      exerciseId: currentExerciseId,
      totalSeconds: elapsedSeconds.value,
      distanceKm: distanceKm.value,
      calories: calories.value,
      avgHeartRate: heartRate.value,
    })
  } catch (e: any) {
    alert(e.response?.data?.message || '保存失败')
  }

  router.push('/exercise')
}

// ===== 生命周期 =====
onMounted(async () => {
  try {
    const res = await startExercise('户外跑步')
    currentExerciseId = res.data.data.sessionId
  } catch (e) {
    currentExerciseId = 0
  }
  startTimer()
  startHeartRate()
  autoSave()
})

onUnmounted(() => {
  clearInterval(timerInterval)
  clearInterval(heartRateInterval)
  clearInterval(saveInterval)
})
</script>

<template>
  <div class="page immersive-run">
    <!-- 顶部状态 -->
    <div class="top-bar">
      <span class="time">{{ currentTime }}</span>
      <span class="route-badge">{{ routeName }} · 进行中</span>
    </div>

    <!-- 地图区域 -->
    <div class="map-area" @click="showMap = !showMap">
      <div class="route-preview">
        <svg viewBox="0 0 300 200" class="route-svg">
          <path d="M30,160 Q80,80 120,120 T200,60 T280,90"
            stroke="#8FA89B" stroke-width="4" fill="none" stroke-linecap="round"
            :stroke-dashoffset="showMap ? 0 : 400"
            stroke-dasharray="400" style="transition: stroke-dashoffset 2s" />
          <circle cx="30" cy="160" r="7" fill="#8FA89B" />
          <circle cx="280" cy="90" r="7" fill="#E57373">
            <animate attributeName="r" values="5;8;5" dur="2s" repeatCount="indefinite" />
          </circle>
        </svg>
      </div>
    </div>

    <!-- 核心数据 -->
    <div class="info-area">
      <span class="elapsed-label">已运动</span>
      <span class="timer">{{ elapsedTime }}</span>

      <div class="metrics">
        <div class="metric">
          <span class="m-val">{{ distanceKm.toFixed(1) }}</span>
          <span class="m-unit">公里</span>
        </div>
        <div class="metric">
          <span class="m-val">{{ pace }}</span>
          <span class="m-unit">配速</span>
        </div>
        <div class="metric">
          <span class="m-val" :class="{ high: heartRate > 160 }">{{ heartRate }}</span>
          <span class="m-unit">心率</span>
        </div>
      </div>

      <!-- 控制 -->
      <div class="controls">
        <button class="ctrl-btn pause-btn" @click="togglePause">
          <svg v-if="isRunning" width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
            <rect x="6" y="4" width="4" height="16" rx="1"/><rect x="14" y="4" width="4" height="16" rx="1"/>
          </svg>
          <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
            <polygon points="6,3 20,12 6,21"/>
          </svg>
        </button>
        <button class="ctrl-btn stop-btn" @click="stopExercise">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="white">
            <rect x="6" y="6" width="12" height="12" rx="2"/>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.immersive-run {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background: #0D1117;
  display: flex; flex-direction: column;
  color: #fff;
}

.top-bar {
  padding: 16px 24px;
  display: flex; align-items: center; justify-content: space-between;
  flex-shrink: 0;
}
.time { font-size: 16px; color: rgba(255,255,255,0.6); }
.route-badge {
  padding: 6px 14px;
  background: rgba(143,168,155,0.2);
  border-radius: 20px;
  font-size: 12px; color: #8FA89B; font-weight: 500;
}

.map-area {
  flex: 1;
  background: linear-gradient(180deg, rgba(143,168,155,0.15), rgba(143,168,155,0.02));
  display: flex; align-items: center; justify-content: center;
}
.route-svg { width: 90%; height: 90%; opacity: 0.7; }

.info-area {
  display: flex; flex-direction: column;
  align-items: center; justify-content: center;
  gap: 12px;
  padding-bottom: 32px;
  flex-shrink: 0;
}
.elapsed-label { font-size: 14px; color: rgba(255,255,255,0.4); }
.timer {
  font-size: 72px; font-weight: 200;
  letter-spacing: 6px;
  font-variant-numeric: tabular-nums;
}

.metrics { display: flex; gap: 36px; margin-top: 8px; }
.metric { display: flex; flex-direction: column; align-items: center; gap: 4px; }
.m-val { font-size: 28px; font-weight: 400; }
.m-val.high { color: #E57373; }
.m-unit { font-size: 12px; color: rgba(255,255,255,0.4); }

.controls { display: flex; gap: 28px; margin-top: 20px; align-items: center; }
.ctrl-btn { border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; border-radius: 50%; }
.pause-btn {
  width: 68px; height: 68px;
  background: rgba(255,255,255,0.15);
  border: 2px solid rgba(255,255,255,0.3);
  color: #fff;
}
.stop-btn {
  width: 80px; height: 80px;
  background: #E57373;
  box-shadow: 0 0 20px rgba(229,115,115,0.4);
}
</style>
