const BASE_URL = 'http://localhost:8080'

function getToken(): string | null {
  return localStorage.getItem('token')
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
    return getJSON(`${BASE_URL}/auftrags?lat=${lat}&lng=${lng}`)
  },

  // Auth
  async authUser(data: any) {
    return postJSON(`${BASE_URL}/v1/auth`, data)
  },

  async authUserByToken(token: string) {
    return getJSON(`${BASE_URL}/v1/auth/${token}`)
  },

  async validateToken() {
    return getJSON(`${BASE_URL}/v1/auth/validate`)
  },

  // User-Profile
  async registerLokalUser(data: any) {
    return postJSON(`${BASE_URL}/v1/user/register`, data)
  },

  async updateUser(data: any) {
    return putJSON(`${BASE_URL}/v1/user/${data.id}`, data)
  },

  async getUser(token: string) {
    return getJSON(`${BASE_URL}/v1/user/${token}`)
  },
}
