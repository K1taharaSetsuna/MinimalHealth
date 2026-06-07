import client from './client'

export interface ExerciseListData {
  weeklyStats: {
    count: number
    totalDistanceKm: number
    totalCalories: number
  }
  records: Array<{
    id: number
    exerciseType: string
    durationMin: number | null
    distanceKm: number | null
    calories: number | null
    avgPace: string | null
    routeDesc: string | null
    recordDate: string
    status: string
  }>
}

export function getExercises(weekStart?: string) {
  return client.get<{ data: ExerciseListData }>('/exercises', { params: { weekStart } })
}

export function startExercise(exerciseType: string) {
  return client.post<{ data: { sessionId: number; startedAt: string } }>('/exercises/start', { exerciseType })
}

export function finishExercise(id: number, data: {
  durationMin?: number; distanceKm?: number; calories?: number
  avgPace?: string; avgHr?: number; routeDesc?: string
}) {
  return client.put<{ data: any }>(`/exercises/${id}/finish`, data)
}

export function getRunningStatus() {
  return client.get<{ data: any }>('/exercises/running/status')
}

export function deleteExercise(id: number) {
  return client.delete<{ data: any }>(`/exercises/${id}`)
}
