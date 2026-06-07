import client from './client'

export interface ReminderItem {
  name: string
  desc: string
  on: boolean
}

export function getReminders() {
  return client.get<{ data: ReminderItem[] }>('/reminders')
}

export function updateReminders(data: {
  waterEnabled?: boolean
  sleepEnabled?: boolean
  exerciseEnabled?: boolean
}) {
  return client.put<{ data: ReminderItem[] }>('/reminders', data)
}
