import type { ViewFactory, CardAdapter, CardAction } from './view-factory.types'
import type { OfferModel as Offer } from '@/models/OfferModel'
import apiService from '@/service/apiService'
import { translate as t } from '@/service/translationService'
import {getTaskImage} from "@/service/imageService.ts";
import type {SubmissionFormConfig} from "@/core/factory/SubmissionFormConfig.ts";

const chipColor = (s: Offer['status']) =>
  s === 'ACCEPTED' ? 'green' : s === 'REJECTED' ? 'red' : 'grey'

type OfferCtx = { mine?: boolean; taskId?: number; task?: { id: number; title: string; description: string; category: string; imageUrl?: string } }

export class OfferFactory implements ViewFactory<Offer> {
  kind: 'offer' = 'offer'

  getTitle(ctx?: OfferCtx) {
    return ctx?.mine ? t('MY_OFFERS') : t('OFFERS_FOR_TASK')
  }

  getEmptyText() {
    return t('NO_OFFERS_FOUND')
  }

  async loadItems(ctx?: OfferCtx): Promise<Offer[]> {
    if (ctx?.mine) {
      return await apiService.getMyOffers() as any
    }
    if (ctx?.taskId) {
      return await apiService.getOffersForTask(ctx.taskId) as any
    }
    return []
  }

  getAdapter(ctx?: OfferCtx): CardAdapter<Offer> {
    const isMine = !!ctx?.mine
    return {
      getId: o => o.id,
      getImage: o => o.task?.imageUrl ?? getTaskImage(o.task.category),
      getTitle: o => o.task?.title ?? o.title,
      getSubtitle: o => o.task?.description ?? '',
      getLines: o => [
        { value: o.title },
        { value: o.text }
      ],
      getChips: o =>
        isMine ? [{ text: t(o.status), color: chipColor(o.status) }] : [],
      getActions: o => {
        const acts: CardAction[] = []
        acts.push({ name: 'open', icon: 'info', color: 'primary', visible: true })
        if (isMine) {
          acts.push({name: 'delete', icon: 'delete', color: 'error', visible: o.status != 'ACCEPTED'})
        } else {
          acts.push({ name: 'accept', icon: 'check', color: 'green', visible: true })
          acts.push({ name: 'reject', icon: 'close', color: 'red', visible: true })
        }
        return acts
      }
    }
  }

  getFormConfig(ctx?: OfferCtx): SubmissionFormConfig<Offer> {
    console.log(ctx)
    return {
      title:` ${t('NEW_OFFER')} '${ctx?.task.title}'`,
      getInitialData:  () => {
        return {
          id: 0,
          title: '',
          text: '',
          status: 'SUBMITTED',
          userId: null,
          task: ctx?.task ?? { id: 0, title: '', description: '', category: '', imageUrl: '' },
          createdAt: Date.now()
        } as Offer
      },
      fields: [
        { key: 'title', label: t('OFFER_TITLE'), type: 'text', required: true },
        { key: 'text',  label: t('OFFER_TEXT'),  type: 'textarea', required: true },
        { key: 'status', label: t('STATUS'), type: 'text', readonly: true }
      ],
      save: async (offer) => {
          await apiService.submitOffer(offer)
      }
    }
  }

}
