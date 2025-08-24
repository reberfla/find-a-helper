import type { UserModel } from '@/models/UserModel.ts'
import type { Task } from '@/models/TaskModel.ts'
import type { OfferModel } from '@/models/OfferModel.ts'

export interface AssignmentModel {
  id: number
  taskCreatorUser: Partial<UserModel>
  offerCreatorUser: Partial<UserModel>
  task: Task
  offer: OfferModel
  createdAt: number
  status: AssignmentStatus
  active: boolean
}

export type AssignmentUpdateModel = Partial<Pick<AssignmentModel, 'status' | 'active'>>

export enum AssignmentStatus {
  OPEN = 'OPEN',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
}
