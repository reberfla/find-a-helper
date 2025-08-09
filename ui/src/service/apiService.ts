import {useAuth} from "@/service/userAuthService.ts";
import type { Task } from '@/models/TaskModel.ts'
const BASE_URL = 'http://localhost:8080'

function getToken(): string | null {
  const user = localStorage.getItem('user')
  return user ? JSON.parse(user).token : null
}

function buildHeaders(): HeadersInit {
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
  }
  const token = useAuth().getCurrentUser()?.token ?? null
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }
  return headers
}

async function getJSON<T>(url: string): Promise<T> {
  const response = await fetch(url, {
    method: 'GET',
    headers: buildHeaders(),
  })
  if (!response.ok) {
    throw response
  }
  return await response.json()
}

async function postJSON<T>(url: string, data: any): Promise<T> {
  const response = await fetch(url, {
    method: 'POST',
    headers: buildHeaders(),
    body: JSON.stringify(data),
  })
  if (!response.ok) {
    throw response
  }
  return await response.json()
}

async function putJSON<T>(url: string, data: any): Promise<T> {
  const response = await fetch(url, {
    method: 'PUT',
    headers: buildHeaders(),
    body: JSON.stringify(data),
  })
  if (!response.ok) {
    throw response
  }
  return await response.json()
}

export default {
  // Public API
  async getAuftrags(lat: string, lng: string) {
    return getJSON(`${BASE_URL}/auftrags?lat=${lat}&lng=${lng}`);
  },

  // Auth
  async authUser(data: any) {
    return postJSON(`${BASE_URL}/v1/auth`, data);
  },

  async authUserByToken(token: string) {
    return getJSON(`${BASE_URL}/v1/auth/${token}`)
  },

  async validateToken() {
    return getJSON(`${BASE_URL}/v1/auth/validate`);
  },

  // User-Profile
  async registerLokalUser(data: any) {
    return postJSON(`${BASE_URL}/v1/user/register`, data);
  },

  async updateUser(data: any) {
    return putJSON(`${BASE_URL}/v1/user/${data.id}`, data);
  },

  async getUser(token: string) {
    return getJSON(`${BASE_URL}/v1/user/${token}`)
  },

//Offers
  async getMyOffers() {
    return getJSON(`${BASE_URL}/v1/offer/my`)
  },

  async getOfferById(id: number) {
    return getJSON(`${BASE_URL}/v1/offer/${id}`)
  },

  async getOffersForTask(taskId: number) {
    return getJSON(`${BASE_URL}/v1/offer/task/${taskId}`)
  },

  async submitOffer(data: any) {
    return postJSON(`${BASE_URL}/v1/offer`, data)
  },

  async acceptOffer(offerId: number) {
    return putJSON(`${BASE_URL}/v1/offer/accept/${offerId}`, null)
  },

  async rejectOffer(offerId: number) {
    return putJSON(`${BASE_URL}/v1/offer/reject/${offerId}`, null)
  },

  async deleteOffer(offerId: number) {
    return getJSON(`${BASE_URL}/v1/offer/${offerId}`)
  },


  // Tasks
  async getTasks() {
    return getJSON<Task[]>(`${BASE_URL}/v1/task`)
  },

  //todo => implement backend
  async getMyTasks() {
    return getJSON<Task[]>(`${BASE_URL}/v1/task`)
  },
}
