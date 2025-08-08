import type { ViewFactory, CardAdapter, CardAction } from './view-factory.types'
import type { OfferModel as Offer } from '@/models/OfferModel'
import apiService from '@/service/apiService'
import { translate as t } from '@/service/translationService'

const IMG = 'https://images.pexels.com/photos/5428830/pexels-photo-5428830.jpeg'

const chipColor = (s: Offer['status']) =>
  s === 'ACCEPTED' ? 'green' : s === 'REJECTED' ? 'red' : 'grey'

type OfferCtx = { mine?: boolean; taskId?: number }

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
      getImage: o => o.task?.imageUrl || IMG,
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
        if (isMine) {
          acts.push({
            name: 'delete',
            icon: 'mdi-delete',
            color: 'error',
            visible: o.status === 'SUBMITTED'
          })
        } else {
          acts.push({ name: 'open', icon: 'mdi-eye', color: 'primary', visible: true })
          acts.push({ name: 'accept', icon: 'mdi-check', color: 'green', visible: true })
          acts.push({ name: 'reject', icon: 'mdi-close', color: 'red', visible: true })
        }
        return acts
      }
    }
  }
}
