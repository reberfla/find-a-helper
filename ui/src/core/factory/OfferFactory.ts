import type { ViewFactory, CardAdapter, CardAction } from './view-factory.types'
import type { OfferModel as Offer } from '@/models/OfferModel'
import apiService from '@/service/apiService'
import { translate as t } from '@/service/translationService'
import { getTaskImage } from '@/service/imageService.ts'
import type { SubmissionFormConfig } from '@/core/factory/SubmissionFormConfig.ts'
import { useAuth } from '@/service/userAuthService.ts'

const chipColor = (s: Offer['status']) =>
  s === 'ACCEPTED' ? 'green' : s === 'REJECTED' ? 'red' : 'grey'

type OfferCtx = {
  mine?: boolean
  taskId?: number
  task?: { id: number; title: string; description: string; category: string; imageUrl?: string }
}

export class OfferFactory implements ViewFactory<Offer> {
  kind = 'offer' as const

  getTitle(ctx?: OfferCtx) {
    return ctx?.mine ? t('MY_OFFERS') : t('OFFERS_FOR_TASK')
  }

  getEmptyText() {
    return t('NO_OFFERS_FOUND')
  }

  async loadItems(ctx?: OfferCtx): Promise<Offer[]> {
    if (ctx?.mine) {
      return (await apiService.getMyOffers()) as any
    }
    if (ctx?.taskId) {
      return (await apiService.getOffersForTask(ctx.taskId)) as any
    }
    return []
  }

  getAdapter(ctx?: OfferCtx): CardAdapter<Offer> {
    const isMine = !!ctx?.mine
    return {
      getId: (o) => o.id,
      getImage: (o) => o.task?.imageUrl ?? getTaskImage(o.task.category),
      getTitle: (o) => o.task?.title ?? o.title,
      getSubtitle: (o) => o.task?.description ?? '',
      getLines: (o) => [{ value: o.title }, { value: o.text }],
      getChips: (o) => (isMine ? [{ text: t(o.status), color: chipColor(o.status) }] : []),
      getActions: (o) => {
        const acts: CardAction[] = []
        acts.push({ name: 'open', icon: 'info', color: 'primary', visible: true })
        if (isMine) {
          acts.push({
            name: 'delete',
            icon: 'delete',
            color: 'error',
            visible: o.status != 'ACCEPTED',
          })
        } else {
          acts.push({ name: 'accept', icon: 'check', color: 'green', visible: true })
          acts.push({ name: 'reject', icon: 'close', color: 'red', visible: true })
        }
        return acts
      },
    }
  }

  getFormConfig(ctx?: Partial<Offer>): SubmissionFormConfig<Offer> {
    const taskTitle = ctx?.task?.title ?? ''
    const isEdit = !!ctx?.id && ctx.id !== 0
    return {
      title: isEdit ? `${t('OFFER_FOR')} '${taskTitle}'` : `${t('NEW_OFFER')} '${taskTitle}'`,

      getInitialData: () => {
        const auth = useAuth()
        const userId = auth.getCurrentUserId() ?? 0
        const taskId = ctx?.task?.id ?? (ctx as any)?.taskId ?? 0

        const defaultValidUntil = this.toDateOnly(new Date(Date.now() + 14 * 24 * 3600 * 1000))

        return {
          title: '',
          text: '',
          status: 'SUBMITTED',
          active: true,
          userId,
          taskId,
          task: ctx?.task ?? { id: taskId, title: '', description: '', category: '', imageUrl: '' },
          validUntil: ctx?.validUntil ? this.toDateOnly(ctx.validUntil as any) : defaultValidUntil,
          createdAt: Date.now(),
          ...ctx,
        } as Offer
      },
      fields: [
        { key: 'title', label: t('OFFER_TITLE'), type: 'text', required: true },
        { key: 'text', label: t('OFFER_TEXT'), type: 'textarea', required: true },
        { key: 'validUntil', label: 'Gültig bis', type: 'date' },
        { key: 'status', label: t('STATUS'), type: 'text', readonly: true },
      ],
      save: async (form) => {
        const dto = {
          id: form.id && form.id !== 0 ? form.id : undefined,
          userId: form.userId,
          taskId: form.taskId,
          status: form.status ?? 'SUBMITTED',
          active: !!form.active,
          text: form.text,
          title: form.title || null,
          validUntil: form.validUntil || null, // 'yyyy-MM-dd'
        }
        if (!dto.id) await apiService.submitOffer(dto)
      },
    }
  }

  toDateOnly = (d: unknown): string | null => {
    if (!d) return null
    const dt = d instanceof Date ? d : new Date(d as any)
    const yyyy = dt.getFullYear()
    const mm = String(dt.getMonth() + 1).padStart(2, '0')
    const dd = String(dt.getDate()).padStart(2, '0')
    return `${yyyy}-${mm}-${dd}`
  }
}
