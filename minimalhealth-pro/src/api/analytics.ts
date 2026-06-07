import client from './client'

export interface AnalyticsData {
  overallScore: number
  scoreChange: number
  period: string
  metrics: Array<{
    label: string
    value: string
  }>
}

export function getAnalytics(period = 'week') {
  return client.get<{ data: AnalyticsData }>('/analytics', { params: { period } })
}
