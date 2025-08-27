import { putJSON, getJSON } from '@/service/apiService.ts'
import type { AssignmentModel, AssignmentUpdateModel } from '@/models/AssignmentModel.ts'
import { AssignmentStatus } from '@/models/AssignmentModel'
import { TaskCategory, TaskInterval, TaskStatus, Weekday } from '@/models/TaskModel'


export default {
  async updateAssignment(
    assignmentId: number,
    update: AssignmentUpdateModel,
  ): Promise<AssignmentModel> {
    return await putJSON<AssignmentModel>(`/assignments/${assignmentId}`, update)
  },

  async createAssignment(){
    return true;
  },

  async revertOffer(){
    return true
  },

  async getAssignmentFromUser(): Promise<AssignmentModel[]> {
    return MOCK_ASSIGNMENTS
    //return await getJSON<AssignmentModel[]>(`/assignment/my`)
  },
}


export const MOCK_ASSIGNMENTS = [
  {
    id: 1,
    taskCreatorUser: { id: 101, name: 'Anna Müller', email: 'anna.mueller@example.com' },
    offerCreatorUser: { id: 202, name: 'Max Schmidt', email: 'max.schmidt@example.com' },
    task: {
      id: 301,
      title: 'Datenbank aktualisieren',
      description: 'Kundendatenbank bis Ende Woche aktualisieren.',
      category: TaskCategory.TECHHELP,
      zipCode: '8000',
      coordinates: '47.3769,8.5417',
      status: TaskStatus.ASSIGNED,
      active: true,
      taskInterval: TaskInterval.ONE_TIME,
      weekdays: [],
      createdAt: Math.floor(Date.now() / 1000),
      deadline: Math.floor(Date.now() / 1000) + 86400 * 7,
    },
    offer: {
      id: 401,
      title: 'DB-Update-Service',
      text: 'Ich erledige das bis Freitag.',
      active: true,
      validUntil: new Date().toISOString().slice(0, 10),
      createdAt: Math.floor(Date.now() / 1000),
      taskId: 301,
      userId: 202,
      status: 'ACCEPTED',
    },
    createdAt: Date.now(),
    status: AssignmentStatus.IN_PROGRESS,
    active: true,
  },
  {
    id: 2,
    taskCreatorUser: { id: 111, name: 'Peter Frei', email: 'peter.frei@example.com' },
    offerCreatorUser: { id: 222, name: 'Lena Vogt', email: 'lena.vogt@example.com' },
    task: {
      id: 302,
      title: 'Garten jäten',
      description: 'Unkraut im Vorgarten entfernen.',
      category: TaskCategory.GARDENING,
      zipCode: '4600',
      coordinates: '47.34,7.9',
      status: TaskStatus.ASSIGNED,
      active: true,
      taskInterval: TaskInterval.RECURRING,
      weekdays: [Weekday.SATURDAY],
      createdAt: Math.floor(Date.now() / 1000),
      deadline: null,
    },
    offer: {
      id: 402,
      title: 'Gartenhilfe',
      text: 'Samstagvormittag passt mir.',
      active: true,
      validUntil: new Date().toISOString().slice(0, 10),
      createdAt: Math.floor(Date.now() / 1000),
      taskId: 302,
      userId: 222,
      status: 'SUBMITTED',
    },
    createdAt: Date.now(),
    status: AssignmentStatus.OPEN,
    active: true,
  },
]
