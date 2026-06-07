import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'splash', component: () => import('../views/SplashView.vue') },
  { path: '/login', redirect: '/login-password' },
  { path: '/login-password', name: 'login', component: () => import('../views/LoginPasswordView.vue') },
  { path: '/register', name: 'register', component: () => import('../views/RegisterView.vue') },
  { path: '/profile/basic', name: 'profileBasic', component: () => import('../views/ProfileBasicView.vue') },
  { path: '/profile/body', name: 'profileBody', component: () => import('../views/ProfileBodyView.vue') },
  { path: '/profile/goals', name: 'profileGoals', component: () => import('../views/ProfileGoalsView.vue') },
  { path: '/dashboard', name: 'dashboard', component: () => import('../views/DashboardView.vue') },
  { path: '/timeline', name: 'timeline', component: () => import('../views/TimelineView.vue') },
  { path: '/diet', name: 'diet', component: () => import('../views/DietView.vue') },
  { path: '/water', name: 'water', component: () => import('../views/WaterView.vue') },
  { path: '/exercise', name: 'exercise', component: () => import('../views/ExerciseListView.vue') },
  { path: '/exercise/running', name: 'running', component: () => import('../views/ImmersiveRunView.vue') },
  { path: '/analytics', name: 'analytics', component: () => import('../views/HealthAnalyticsView.vue') },
  { path: '/ai', name: 'ai', component: () => import('../views/AiSuggestionsView.vue') },
  { path: '/reminders', name: 'reminders', component: () => import('../views/ReminderSettingsView.vue') },
  { path: '/profile', name: 'profile', component: () => import('../views/ProfileView.vue') },
]

export default createRouter({
  history: createWebHashHistory(),
  routes
})
