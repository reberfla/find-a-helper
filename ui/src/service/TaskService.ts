import { getJSON, postJSON, putJSON, BASE_URL, deleteRequest } from '@/service/apiService.ts'
import type { Task } from '@/models/TaskModel'
// Tasks
export default {
  async getTasks(): Promise<Task[]> {
    return getJSON<Task[]>(`${BASE_URL}/v1/task`)
  },

  async getMyTasks(): Promise<Task[]> {
    return getJSON<Task[]>(`${BASE_URL}/v1/task/my`)
  },

  async createTask(data: Partial<Task>): Promise<Task> {
    return postJSON(`${BASE_URL}/v1/task`, data)
  },

  async updateTask(data: Partial<Task>, id: number): Promise<Task> {
    return putJSON(`${BASE_URL}/v1/task/${id}`, data)
  },

  async deleteTask(id: number): Promise<{ message: string }> {
    return deleteRequest(`${BASE_URL}/v1/task/${id}`)
  },
}
