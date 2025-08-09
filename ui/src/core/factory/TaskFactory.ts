// TaskFactory.ts
import type {CardAction, CardAdapter, ViewFactory} from './view-factory.types'
import {type Task, TaskCategory, TaskInterval, TaskStatus, Weekday} from '@/models/TaskModel'
import apiService from '@/service/apiService'
import {translate as t} from '@/service/translationService'
import {getTaskImage} from "@/service/imageService.ts";
import type {SubmissionFormConfig} from "@/core/factory/SubmissionFormConfig.ts";

type Ctx = { mine?: boolean }

export class TaskFactory implements ViewFactory<Task> {
  kind: 'task' = 'task'

  getTitle(ctx?: Ctx) { return ctx?.mine ? t('MY_TASKS') : t('TASKS') }
  getEmptyText() { return t('NO_TASKS_FOUND') }

  async loadItems(ctx?: Ctx): Promise<Task[]> {
    if (ctx?.mine) {
      return apiService.getMyTasks()
    }
    return apiService.getTasks()
  }

  getAdapter(ctx: Ctx = {}): CardAdapter<Task> {
    const isMine = !!ctx?.mine
    return {
      getId: x => x.id,
      getImage: x => /*x?.imageUrl ??*/ getTaskImage(x.category),
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
      getActions: (x) => {
        const acts: CardAction[] = []
        acts.push({ name: 'open', icon: 'info', color: 'primary', visible: true })
        if (isMine) {
          acts.push({name: 'delete', icon: 'delete', color: 'error', visible: isMine})
        } else {
          acts.push({ name: 'addOffer', icon: 'add_circle', color: 'green', visible: true })
        }
        return acts
      },
    }
  }

  getFormConfig(ctx?: Partial<Task>): SubmissionFormConfig<Task> {
    console.log(ctx)
    return {
      title: ctx?.title || t('NEW_TASK'),
      getInitialData: () => ({
        id: 0,
        name: '',
        email: '',
        zipCode: '',
        coordinates: '',
        title: '',
        description: '',
        category: TaskCategory.OTHERS,
        status: TaskStatus.OPEN,
        active: true,
        deadline: undefined,
        taskInterval: TaskInterval.ONE_TIME,
        weekdays: [],
        createdAt: Date.now(),
        ...ctx
      }),
      fields: [
        { key: 'title', label: t('TASK_TITLE'), type: 'text', required: true },
        { key: 'description', label: t('TASK_DESCRIPTION'), type: 'textarea', required: true },
        { key: 'zipCode', label: t('ZIP_CODE'), type: 'text', required: true },
        { key: 'category', label: t('CATEGORY'), type: 'select', options: Object.keys(TaskCategory).map(k => ({ value: k, text: t(k) })) },
        { key: 'taskInterval', label: t('INTERVAL'), type: 'select', options: Object.keys(TaskInterval).map(k => ({ value: k, text: t(k) })) },
        { key: 'weekdays', label: t('WEEKDAYS'), type: 'multiselect', options: Object.keys(Weekday).map(k => ({ value: k, text: t(k) })) },
        { key: 'deadline', label: t('DEADLINE'), type: 'date' }
      ],
      save: async (task) => {
        if (task.id) {
          //todo implement updateTask method
         // await apiService.updateTask(task)
        } else {
        //todo implenebt createTask method
         // await apiService.createTask(task)
        }
      }
    }
  }

}
