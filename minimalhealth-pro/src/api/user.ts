import client from './client'

export interface UserProfileData {
  id: number
  name: string | null
  email: string
  phone: string
  avatarInitial: string
  gender: string | null
  birthDate: string | null
  heightCm: number | null
  weightKg: number | null
  activityLevel: string | null
  profileComplete: boolean
}

export function getUserProfile() {
  return client.get<{ data: UserProfileData }>('/user/profile')
}

export function updateUserProfile(name: string, email?: string) {
  return client.put<{ data: any }>('/user/profile', { name, email })
}

export function updateBodyData(heightCm?: number, weightKg?: number) {
  return client.put<{ data: any }>('/user/body', { heightCm, weightKg })
}
