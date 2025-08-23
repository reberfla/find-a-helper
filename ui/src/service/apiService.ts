import { useAuth } from '@/service/userAuthService.ts'
import type { AuthRequest, AuthResponse, UserModel } from '@/models/UserModel.ts'

export const BASE_URL = 'http://localhost:8080'

function getToken(): string | null {
  const user = localStorage.getItem('user')
  return user ? JSON.parse(user).token : null
}

function buildHeaders(): HeadersInit {
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
  }
  const token = getToken()
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }
  return headers
}

export async function getJSON<T>(url: string): Promise<T> {
  const response = await fetch(url, {
    method: 'GET',
    headers: buildHeaders(),
  })
  if (!response.ok) {
    throw response
  }
  return await response.json()
}

export async function postJSON<T>(url: string, data: any): Promise<T> {
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

export async function putJSON<T>(url: string, data: any): Promise<T> {
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

export async function deleteRequest(url: string): Promise<{ message: string }> {
  const response = await fetch(url, {
    method: 'DELETE',
    headers: buildHeaders(),
  })
  if (!response.ok) {
    throw response
  }
  return await response.json()
}

export default {
  // Auth
  async authUser(data: any): Promise<AuthResponse> {
    return postJSON(`${BASE_URL}/v1/auth`, data)
  },

  async authUserByToken(token: string) {
    return getJSON(`${BASE_URL}/v1/auth/${token}`)
  },

  async validateToken() {
    return getJSON(`${BASE_URL}/v1/auth/validate`)
  },

  // User-Profile
  async registerUser(data: Partial<UserModel>): Promise<AuthResponse> {
    console.log(data)
    return postJSON(`${BASE_URL}/v1/user/register`, data)
  },

  async updateUser(data: any) {
    const token = useAuth().getCurrentUser()?.token ?? data.token
    return putJSON(`${BASE_URL}/v1/user/${token}`, data)
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
    return deleteRequest(`${BASE_URL}/v1/offer/${offerId}`)
  },
}
