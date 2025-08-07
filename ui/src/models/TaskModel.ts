export interface Task {
  id: number
  name?: string
  email?: string
  zipCode: string
  coordinates?: string
  title: string
  description: string
  category: TaskCategory
  status: TaskStatus
  active: boolean
  deadline?: number
  taskInterval: TaskInterval
  weekdays: Weekday[]
  createdAt: number
}

export enum TaskCategory {
  SHOPPING = 'SHOPPING',
  TRANSPORT = 'TRANSPORT',
  CLEANING = 'CLEANING',
  PETCARE = 'PETCARE',
  GARDENING = 'GARDENING',
  TUTORING = 'TUTORING',
  TECHHELP = 'TECHHELP',
  CHILDCARE = 'CHILDCARE',
  LANGUAGETANDEM = 'LANGUAGETANDEM',
  HOMEWORK = 'HOMEWORK',
  REPAIRS = 'REPAIRS',
  OTHERS = 'OTHERS',
}

export enum TaskStatus {
  OPEN = 'OPNE',
  ASSIGNED = 'ASSIGNED',
  COMPLETED = 'COMPLETED',
}

export enum TaskInterval {
  CONTINUOUS = 'CONTINUOUS',
  RECURRING = 'RECURRING',
  ONE_TIME = 'ONE_TIME',
}

export enum Weekday {
  MONDAY = 'MONDAY',
  TUESDAY = 'TUESDAY',
  WEDNESDAY = 'WEDNESDAY',
  THURSDAY = 'THURSDAY',
  FRIDAY = 'FRIDAY',
  SATURDAY = 'SATURDAY',
  SUNDAY = 'SUNDAY',
}
