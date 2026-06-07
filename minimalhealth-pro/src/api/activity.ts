import client from './client'

export interface ActivityItem {
  id: number
  name: string
  detail: string
  time: string
  valueLabel: string
  activityType: string
}

export function getActivities(date?: string, page = 0, size = 20) {
  return client.get<{ data: { content: ActivityItem[] } }>('/timeline', { params: { date, page, size } })
}

export function addActivity(data: {
  type: string
  name: string
  detail?: string
  valueLabel?: string
  valueAmount?: number
  valueUnit?: string
}) {
  return client.post<{ data: ActivityItem }>('/timeline', data)
}

export function deleteActivity(id: number) {
  return client.delete<{ data: any }>(`/timeline/${id}`)
}
