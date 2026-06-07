import client from './client'

export interface DashboardData {
  greeting: string
  healthScore: number
  stats: {
    steps: number
    restingHr: number
    sleepHours: number
  }
  activities: Array<{
    id: number
    name: string
    detail: string
    time: string
    valueLabel: string
    activityType: string
  }>
}

export function getDashboard(date?: string) {
  return client.get<{ data: DashboardData }>('/dashboard', { params: { date } })
}
