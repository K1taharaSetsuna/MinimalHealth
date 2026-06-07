<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import StatusBar from '../components/StatusBar.vue'
import TabBar from '../components/TabBar.vue'
import { getDiet, addMeal, deleteMeal, type DietData } from '../api/diet'
const router = useRouter()
const activeTab = ref('diet')
const diet = ref<DietData | null>(null)
const showAdd = ref(false)
const newMeal = ref({ mealType: 'breakfast', foods: '', calories: 0 })

onMounted(async () => { await loadDiet() })

async function loadDiet() {
  try { const res = await getDiet(); diet.value = res.data.data } catch (e) {}
}

async function handleDeleteMeal(id: number) {
  if (!confirm('确定删除？')) return
  try { await deleteMeal(id); await loadDiet() } catch (e) {}
}

async function handleAddMeal() {
  if (!newMeal.value.foods || newMeal.value.calories <= 0) { alert('请填写食物和热量'); return }
  try {
    await addMeal(newMeal.value.mealType, newMeal.value.foods, newMeal.value.calories)
    newMeal.value = { mealType: 'breakfast', foods: '', calories: 0 }
    showAdd.value = false
    await loadDiet()
  } catch (e: any) { alert('添加失败') }
}

function onTab(t: string) {
  if (t === 'dashboard') router.push('/dashboard')
  else if (t === 'timeline') router.push('/timeline')
  else if (t === 'diet') return
  else if (t === 'exercise') router.push('/exercise')
  else if (t === 'profile') router.push('/profile')
}

const mealTypes = [
  { value: 'breakfast', label: '早餐' },
  { value: 'lunch', label: '午餐' },
  { value: 'dinner', label: '晚餐' },
  { value: 'snack', label: '加餐' },
]
</script>
<template>
  <div class="page diet">
    <StatusBar time="12:15" />
    <div class="content">
      <div class="header">
        <h2>饮食追踪</h2>
        <div class="cal-card">
          <span class="cal-label">今日摄入</span>
          <span class="cal-number">{{ (diet?.totalCalories ?? 0).toLocaleString() }}<span class="cal-total"> / {{ (diet?.calorieGoal ?? 2100).toLocaleString() }} kcal</span></span>
        </div>
      </div>
      <div class="meals">
        <div class="meal-card" v-for="m in (diet?.meals || [])" :key="m.mealType" :class="{ empty: !m.recorded }" @click="m.recorded && handleDeleteMeal(m.id)">
          <template v-if="m.recorded">
            <div class="meal-header"><span class="meal-name">{{ m.mealTypeLabel }}</span><span class="meal-cal">{{ m.calories }} kcal</span></div>
            <p class="meal-food">{{ m.foods }}</p>
          </template>
          <template v-else><span class="meal-name">{{ m.mealTypeLabel }}</span><span class="meal-hint">尚未记录</span></template>
        </div>
      </div>

      <!-- 添加餐食 -->
      <button class="add-btn" @click="showAdd=!showAdd">{{ showAdd ? '取消' : '+ 添加餐食' }}</button>
      <div class="add-form" v-if="showAdd">
        <div class="meal-type-row">
          <button v-for="mt in mealTypes" :key="mt.value" :class="{ active: newMeal.mealType === mt.value }" @click="newMeal.mealType = mt.value">{{ mt.label }}</button>
        </div>
        <input class="food-input" v-model="newMeal.foods" placeholder="食物名称，如：鸡胸肉沙拉 · 糙米饭" />
        <div class="cal-row">
          <input class="cal-input" type="number" v-model.number="newMeal.calories" placeholder="热量(kcal)" />
          <button class="submit-btn" @click="handleAddMeal">确认添加</button>
        </div>
      </div>
    </div>
    <TabBar :activeTab="activeTab" @update:activeTab="onTab" />
  </div>
</template>
<style scoped>
.diet { width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary); display: flex; flex-direction: column; }
.content { flex: 1; overflow-y: auto; -webkit-overflow-scrolling: touch; padding: 0 24px; display: flex; flex-direction: column; gap: 16px; padding-bottom: 100px; }
.header { display: flex; flex-direction: column; gap: 20px; padding-top: 24px; }
.header h2 { font-size: 30px; font-weight: var(--font-light); color: var(--text-primary); }
.cal-card { display: flex; flex-direction: column; gap: 4px; }
.cal-label { font-size: 14px; color: var(--text-secondary); }
.cal-number { font-size: 48px; font-weight: var(--font-light); color: var(--text-primary); }
.cal-total { font-size: 20px; color: var(--text-secondary); }
.meals { display: flex; flex-direction: column; gap: 12px; }
.meal-card { background: var(--bg-white); border-radius: var(--radius-lg); padding: 18px; display: flex; flex-direction: column; gap: 8px; }
.meal-header { display: flex; align-items: center; justify-content: space-between; }
.meal-name { font-size: 16px; font-weight: var(--font-medium); color: var(--text-primary); }
.meal-cal { font-size: 14px; font-weight: var(--font-medium); color: var(--accent); }
.meal-food { font-size: 14px; color: var(--text-secondary); line-height: 1.5; }
.meal-card.empty { flex-direction: row; justify-content: space-between; align-items: center; }
.meal-hint { font-size: 14px; color: var(--text-placeholder); }
.add-btn { width: 100%; padding: 14px; background: var(--bg-white); border: 2px dashed var(--border); border-radius: var(--radius-md); font-size: 15px; font-family: Inter, sans-serif; color: var(--accent); cursor: pointer; }
.add-form { background: var(--bg-white); border-radius: var(--radius-lg); padding: 18px; display: flex; flex-direction: column; gap: 12px; }
.meal-type-row { display: flex; gap: 8px; }
.meal-type-row button { flex: 1; padding: 10px; border: none; border-radius: var(--radius-sm); background: var(--bg-light); font-size: 13px; font-family: Inter, sans-serif; cursor: pointer; color: var(--text-secondary); }
.meal-type-row button.active { background: var(--accent); color: #fff; }
.food-input { width: 100%; padding: 12px; border: 1px solid var(--border); border-radius: var(--radius-sm); font-size: 14px; font-family: Inter, sans-serif; }
.cal-row { display: flex; gap: 12px; }
.cal-input { flex: 1; padding: 12px; border: 1px solid var(--border); border-radius: var(--radius-sm); font-size: 14px; font-family: Inter, sans-serif; }
.submit-btn { padding: 12px 24px; background: var(--accent); border: none; border-radius: var(--radius-sm); color: #fff; font-size: 14px; font-family: Inter, sans-serif; cursor: pointer; }
</style>
