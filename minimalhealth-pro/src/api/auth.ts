import client from './client'

export interface LoginResponse {
  accessToken: string
  refreshToken: string
  user: {
    id: number
    name: string | null
    phone: string
    profileComplete: boolean
  }
}

export function passwordLogin(account: string, password: string) {
  return client.post<{ data: LoginResponse }>('/auth/login/password', { account, password })
}

export function register(phone: string, password: string) {
  return client.post<{ data: LoginResponse }>('/auth/register', { phone, password })
}

export function refreshToken(refreshToken: string) {
  return client.post<{ data: LoginResponse }>('/auth/refresh', { refreshToken })
}
