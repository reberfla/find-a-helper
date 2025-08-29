import { getJSON, postJSON, putJSON, BASE_URL, deleteRequest } from '@/service/apiService.ts'
import { type Task, TaskCategory, TaskInterval } from '@/models/TaskModel'
// Tasks
export default {
  async getTasks(category: string | null = null): Promise<Task[]> {
    const res = await getJSON<
      [
        {
          task: Task
          offerUserIds: number[]
        },
      ]
    >(`${BASE_URL}/v1/task${category ? `?category=${category}` : ''}`)
    return res.map((entry: { task: Task; offerUserIds: number[] }) => {
      return {
        ...entry.task,
        offerUserIds: entry.offerUserIds,
      }
    })
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

    let tasks = [] as { task: Task; offerUserIds: number[] }[]
    if (categoryQuery == '' || intervalQuery == '') {
      tasks = await getJSON(`${BASE_URL}/v1/task?${categoryQuery}${intervalQuery}`)
    } else if (categoryQuery == '') {
      tasks = await getJSON(`${BASE_URL}/v1/task?${intervalQuery}`)
    } else if (intervalQuery == '') {
      tasks = await getJSON(`${BASE_URL}/v1/task?${categoryQuery}`)
    } else {
      tasks = await getJSON(`${BASE_URL}/v1/task?${categoryQuery}&${intervalQuery}`)
    }
    return tasks.map((entry: { task: Task; offerUserIds: number[] }) => {
      return {
        ...entry.task,
        offerUserIds: entry.offerUserIds,
      }
    })
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
