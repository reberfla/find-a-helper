import { putJSON, getJSON } from '@/service/apiService.ts'
import type { AssignmentModel, AssignmentUpdateModel } from '@/models/AssignmentModel.ts'

export default {
  async updateAssignment(
    assignmentId: number,
    update: AssignmentUpdateModel,
  ): Promise<AssignmentModel> {
    return await putJSON<AssignmentModel>(`/assignments/${assignmentId}`, update)
  },

  async getAssignmentFromUser(): Promise<AssignmentModel[]> {
    return await getJSON<AssignmentModel[]>(`/assignment/my`)
  },
}
