export const BASE_URL = 'http://localhost:8080'

function getToken(): string | null {
  const user = sessionStorage.getItem('user')
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

export default {}
