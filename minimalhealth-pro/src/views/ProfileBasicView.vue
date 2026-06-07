<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import StatusBar from '../components/StatusBar.vue'
import { updateProfileBasic } from '../api/profile'
import { getUserProfile } from '../api/user'
const router = useRouter()
const name = ref('')
const gender = ref<'male' | 'female'>('male')
const birthDate = ref('')
let isWizard = true

onMounted(async () => {
  try {
    const res = await getUserProfile()
    const u = res.data.data
    if (u.name) name.value = u.name
    if (u.gender) gender.value = u.gender as 'male' | 'female'
    if (u.birthDate) birthDate.value = u.birthDate
    isWizard = !u.profileComplete
  } catch (e) {}
})

async function handleNext() {
  if (!name.value || !birthDate.value) { alert('请填写完整信息'); return }
  try {
    await updateProfileBasic(name.value, gender.value, birthDate.value)
    router.push(isWizard ? '/profile/body' : '/profile')
  } catch (e: any) { alert(e.response?.data?.message || '保存失败') }
}
</script>
<template>
  <div class="page profile-basic">
    <StatusBar time="9:41" />
    <div class="content">
      <div class="progress"><div class="fill" style="width:33%"></div></div>
      <div class="header">
        <span class="step">1 / 3</span>
        <h2>基本信息</h2>
        <p>让我们了解你的基础情况，以便提供更精准的建议</p>
      </div>
      <div class="fields">
        <div class="field">
          <label>姓名</label>
          <div class="input"><input type="text" placeholder="请输入姓名" v-model="name" /></div>
        </div>
        <div class="field">
          <label>性别</label>
          <div class="gender-options">
            <button :class="{ active: gender==='male' }" @click="gender='male'">男</button>
            <button :class="{ active: gender==='female' }" @click="gender='female'">女</button>
          </div>
        </div>
        <div class="field">
          <label>出生日期</label>
          <div class="input"><input type="date" v-model="birthDate" /></div>
        </div>
      </div>
      <button class="btn" @click="handleNext">下一步</button>
    </div>
  </div>
</template>
<style scoped>
.profile-basic { width: 100%; height: 100vh; height: 100dvh; background: var(--bg-primary); display: flex; flex-direction: column; }
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
.input {
  height: 52px; background: var(--bg-white); border-radius: var(--radius-md);
  display: flex; align-items: center; padding: 0 16px;
}
.input input { border: none; outline: none; font-size: 15px; color: var(--text-primary); font-family: Inter, sans-serif; width: 100%; }
.input input::placeholder { color: var(--text-placeholder); }
.gender-options { display: flex; gap: 12px; }
.gender-options button {
  flex: 1; height: 52px; background: var(--bg-white); border: none;
  border-radius: var(--radius-md); font-size: 15px; font-family: Inter, sans-serif;
  color: var(--text-secondary); cursor: pointer;
}
.gender-options button.active { background: var(--accent); color: #fff; }
.btn {
  width: 100%; height: 56px; background: var(--accent); border: none;
  border-radius: var(--radius-pill); color: #fff; font-size: 17px;
  font-weight: var(--font-medium); font-family: Inter, sans-serif; cursor: pointer;
  margin-bottom: 24px;
}
</style>
