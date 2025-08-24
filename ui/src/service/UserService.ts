import type { AuthRequest, AuthResponse, UserModel } from '@/models/UserModel.ts'
import { BASE_URL, getJSON, postJSON, putJSON } from '@/service/apiService.ts'

export default {
  async authUser(data: AuthRequest): Promise<AuthResponse> {
    return postJSON(`${BASE_URL}/v1/auth`, data)
  },

  // User-Profile
  async registerUser(data: Partial<UserModel>): Promise<AuthResponse> {
    console.log(data)
    return postJSON(`${BASE_URL}/v1/user/register`, data)
  },

  async updateUser(data: Partial<UserModel>): Promise<UserModel> {
    return putJSON(`${BASE_URL}/v1/user`, data)
  },

  async getUser(): Promise<UserModel> {
    return await getJSON(`${BASE_URL}/v1/user/profile`)
  },
}
