import { getJSON, postJSON, putJSON, BASE_URL, deleteRequest } from '@/service/apiService.ts'
import { type Task, TaskCategory, TaskInterval } from '@/models/TaskModel'
// Tasks
export default {
  async getTasks(): Promise<Task[]> {
    return getJSON<Task[]>(`${BASE_URL}/v1/task`)
  },

  async getFilteredTasks(
    category: TaskCategory[] = [],
    interval: TaskInterval[] = [],
  ): Promise<Task[]> {
    let categoryQuery = ''
    if (category.length > 0) {
      category.forEach((item) => (categoryQuery += `category=${item}&`))
      categoryQuery = categoryQuery.slice(0, -1)
    }
    let intervalQuery = ''
    if (interval.length > 0) {
      interval.forEach((item) => (intervalQuery += `interval=${item}&`))
      intervalQuery = intervalQuery.slice(0, -1)
    }

    let tasks = [] as Task[]

    if (categoryQuery == '' || intervalQuery == '') {
      tasks = await getJSON<Task[]>(`${BASE_URL}/v1/task?${categoryQuery}${intervalQuery}`)
    } else if (categoryQuery == '') {
      tasks = await getJSON<Task[]>(`${BASE_URL}/v1/task?${intervalQuery}`)
    } else if (intervalQuery == '') {
      tasks = await getJSON<Task[]>(`${BASE_URL}/v1/task?${categoryQuery}`)
    } else {
      tasks = await getJSON<Task[]>(`${BASE_URL}/v1/task?${categoryQuery}&${intervalQuery}`)
    }
    return tasks
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
