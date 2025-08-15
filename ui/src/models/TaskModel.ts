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

export const categoryMap = new Map<TaskCategory, string>([
  [TaskCategory.SHOPPING, 'Einkaufen'],
  [TaskCategory.TRANSPORT, 'Transport'],
  [TaskCategory.CLEANING, 'Reinigung'],
  [TaskCategory.PETCARE, 'Haustierpflege'],
  [TaskCategory.GARDENING, 'Gartenarbeit'],
  [TaskCategory.TUTORING, 'Nachhilfe'],
  [TaskCategory.TECHHELP, 'Technikhilfe'],
  [TaskCategory.CHILDCARE, 'Kinderbetreuung'],
  [TaskCategory.LANGUAGETANDEM, 'Sprachtandem'],
  [TaskCategory.HOMEWORK, 'Hausaufgaben'],
  [TaskCategory.REPAIRS, 'Reparaturen'],
  [TaskCategory.OTHERS, 'Sonstiges'],
])

export const categories = Object.values(TaskCategory).map((category) => ({
  title: categoryMap.get(category),
  value: category,
}))

export enum TaskStatus {
  OPEN = 'OPEN',
  ASSIGNED = 'ASSIGNED',
  COMPLETED = 'COMPLETED',
}

export const statusMap = new Map<TaskStatus, string>([
  [TaskStatus.OPEN, 'Offen'],
  [TaskStatus.ASSIGNED, 'Zugewiesen'],
  [TaskStatus.COMPLETED, 'Abgeschlossen'],
])

export const status = Object.values(TaskStatus).map((status) => ({
  title: statusMap.get(status),
  value: status,
}))

export enum TaskInterval {
  CONTINUOUS = 'CONTINUOUS',
  RECURRING = 'RECURRING',
  ONE_TIME = 'ONE_TIME',
}

export const intervalMap = new Map<TaskInterval, string>([
  [TaskInterval.CONTINUOUS, 'Kontinuierlich'],
  [TaskInterval.RECURRING, 'Wiederkehrend'],
  [TaskInterval.ONE_TIME, 'Einmalig'],
])

export const interval = Object.values(TaskInterval).map((interval) => ({
  title: intervalMap.get(interval),
  value: interval,
}))

export enum Weekday {
  MONDAY = 'MONDAY',
  TUESDAY = 'TUESDAY',
  WEDNESDAY = 'WEDNESDAY',
  THURSDAY = 'THURSDAY',
  FRIDAY = 'FRIDAY',
  SATURDAY = 'SATURDAY',
  SUNDAY = 'SUNDAY',
}

export const weekdayMap = new Map<Weekday, string>([
  [Weekday.MONDAY, 'Mo'],
  [Weekday.TUESDAY, 'Di'],
  [Weekday.WEDNESDAY, 'Mi'],
  [Weekday.THURSDAY, 'Do'],
  [Weekday.FRIDAY, 'Fr'],
  [Weekday.SATURDAY, 'Sa'],
  [Weekday.SUNDAY, 'So'],
])

export function getIconOfCategory(category: TaskCategory): string {
  switch (category) {
    case TaskCategory.SHOPPING:
      return 'mdi-cart'
    case TaskCategory.TRANSPORT:
      return 'mdi-train-car'
    case TaskCategory.CLEANING:
      return 'mdi-vacuum'
    case TaskCategory.PETCARE:
      return 'mdi-paw'
    case TaskCategory.GARDENING:
      return 'mdi-grass'
    case TaskCategory.TUTORING:
      return 'mdi-school'
    case TaskCategory.TECHHELP:
      return 'mdi-laptop'
    case TaskCategory.CHILDCARE:
      return 'mdi-baby-carriage'
    case TaskCategory.LANGUAGETANDEM:
      return 'mdi-translate'
    case TaskCategory.HOMEWORK:
      return 'mdi-book'
    case TaskCategory.REPAIRS:
      return 'mdi-hammer-screwdriver'
    default:
      return 'mdi-folder-question'
  }
}

export function getColorOfCategory(category: TaskCategory): string {
  switch (category) {
    case TaskCategory.SHOPPING:
      return '#FF9800'
    case TaskCategory.TRANSPORT:
      return '#2196F3'
    case TaskCategory.CLEANING:
      return '#4CAF50'
    case TaskCategory.PETCARE:
      return '#FFEB3B'
    case TaskCategory.GARDENING:
      return '#8BC34A'
    case TaskCategory.TUTORING:
      return '#9C27B0'
    case TaskCategory.TECHHELP:
      return '#3F51B5'
    case TaskCategory.CHILDCARE:
      return '#E91E63'
    case TaskCategory.LANGUAGETANDEM:
      return '#00BCD4'
    case TaskCategory.HOMEWORK:
      return '#FF5722'
    case TaskCategory.REPAIRS:
      return '#607D8B'
    default:
      return '#9E9E9E'
  }
}
