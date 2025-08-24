import { getJSON, postJSON, putJSON, BASE_URL, deleteRequest } from '@/service/apiService.ts'
import type {Offer, OfferDto} from "@/models/OfferModel.ts";

export default {
  async getOffers(category: string | null = null): Promise<Offer[]> {
    return getJSON<Offer[]>(`${BASE_URL}/v1/offer${category ? `?category=${category}` : ''}`)
  },

  async getMyOffers(): Promise<Offer[]> {
    return getJSON<Offer[]>(`${BASE_URL}/v1/offer/my`)
  },

  async getOfferById(id: number): Promise<Offer> {
    return getJSON<Offer>(`${BASE_URL}/v1/offer/${id}`)
  },

  async getOffersForTask(taskId: number): Promise<Offer[]> {
    return getJSON<Offer[]>(`${BASE_URL}/v1/offer/task/${taskId}`)
  },

  async createOffer(data: OfferDto): Promise<Offer> {
    console.log(data)
    return postJSON(`${BASE_URL}/v1/offer`, data)
  },

  async deleteOffer(offerId: number): Promise<{ message: string }> {
    return deleteRequest(`${BASE_URL}/v1/offer/${offerId}`)
  },
}
