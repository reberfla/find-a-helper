export interface SubmissionFormConfig<T> {
  title: string
  getInitialData: (props: any) => T
  fields: {
    key: keyof T | string
    label: string
    type: 'text' | 'textarea' | 'select' | 'date' | 'image'
    readonly?: boolean
    options?: any[],
    required?:boolean
  }[]
  save: (data: T) => Promise<void> | void
}
