import client from './client'

export interface WaterData {
  currentMl: number
  goalMl: number
  percentage: number
  logs: Array<{
    id: number
    amountMl: number
    recordedAt: string
  }>
}

export function getWater(date?: string) {
  return client.get<{ data: WaterData }>('/water', { params: { date } })
}

export function addWater(amountMl: number) {
  return client.post<{ data: WaterData }>('/water', { amountMl })
}

export function deleteWater(id: number) {
  return client.delete<{ data: any }>(`/water/${id}`)
}

export function getWaterGoal(date?: string) {
  return client.get<{ data: { date: string; targetMl: number } }>('/water/goal', { params: { date } })
}
