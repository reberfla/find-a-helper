export type ActionName = 'open' | 'accept' | 'reject' | 'delete'

export interface CardAction {
  name: ActionName
  icon: string
  color?: string
  visible: boolean
}

export interface CardLine {
  label?: string
  value: string
}

export interface CardChip {
  text: string
  color?: string
}

export interface CardAdapter<T> {
  getId(item: T): number
  getImage?(item: T): string | undefined
  getTitle(item: T): string
  getSubtitle?(item: T): string | undefined
  getLines?(item: T): CardLine[]
  getChips?(item: T): CardChip[]
  getActions(item: T): CardAction[]
}

export interface ViewFactory<T> {
  kind: 'offer' | 'task'
  getTitle(context?: any): string
  getEmptyText(): string
  getAdapter(context?: any): CardAdapter<T>
  loadItems(context?: any): Promise<T[]>
}
