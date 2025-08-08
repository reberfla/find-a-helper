// TaskFactory.ts
import type { ViewFactory, CardAdapter } from './view-factory.types'
import type { Task } from '@/models/TaskModel'
import apiService from '@/service/apiService'
import { translate as t } from '@/service/translationService'
import { TaskInterval, Weekday } from '@/models/TaskModel'

type Ctx = { mine?: boolean }

export class TaskFactory implements ViewFactory<Task> {
  kind: 'task' = 'task'

  getTitle(ctx?: Ctx) { return ctx?.mine ? t('MY_TASKS') : t('TASKS') }
  getEmptyText() { return t('NO_TASKS_FOUND') }

  async loadItems(ctx?: Ctx): Promise<Task[]> {
    if (ctx?.mine && typeof apiService.getMyTasks === 'function') {
      return apiService.getMyTasks()
    }
    return apiService.getTasks()
  }

  getAdapter(_: Ctx = {}): CardAdapter<Task> {
    return {
      getId: x => x.id,
      // getImage: x => x.imageUrl,
      getTitle: x => x.title,
      getSubtitle: x => x.zipCode,
      getLines: x => {
        const lines = [
          { value: x.description },
          { label: t('CATEGORY'), value: t(x.category) },
          { label: t('INTERVAL'), value: t(x.taskInterval) }
        ]
        if (x.taskInterval !== TaskInterval.ONE_TIME) {
          const days = (x.weekdays || []).map((d: Weekday) => t(d.toString())).join(', ')
          lines.push({ label: t('WEEKDAYS'), value: days })
        }
        lines.push({ label: t('DEADLINE'), value: x.deadline ? new Date(x.deadline).toLocaleDateString() : t('NO_DEADLINE') })
        return lines
      },
      getChips: () => [],
      getActions: () => [{ name: 'open', icon: 'add', color: 'primary', visible: true }]
    }
  }
}
