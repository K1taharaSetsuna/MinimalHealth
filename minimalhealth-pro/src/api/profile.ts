import client from './client'

export function updateProfileBasic(name: string, gender: string, birthDate: string) {
  return client.put<{ data: any }>('/profile/basic', { name, gender, birthDate })
}

export function updateProfileBody(heightCm: number, weightKg: number, activityLevel: string) {
  return client.put<{ data: any }>('/profile/body', { heightCm, weightKg, activityLevel })
}

export function updateProfileGoals(dailySteps?: number, dailySleepH?: number, dailyWaterMl?: number) {
  return client.put<{ data: any }>('/profile/goals', { dailySteps, dailySleepH, dailyWaterMl })
}
