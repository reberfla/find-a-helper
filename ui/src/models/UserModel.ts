export class UserModel {
  constructor(
    public email: string,
    public name: string,
    public password?: string,
    public id?: number,
    public googleToken?: string,
    public authProvider?: AuthProvider,
    public zipCode?: number,
    public birthdate?: string,
    public image?: File,
    public imageUrl?: string,
    public imgBase64?: string,
  ) {}

  static toFormDataFromPartial(data: Partial<UserModel>): FormData {
    const formData = new FormData()
    for (const [key, value] of Object.entries(data)) {
      if (value !== undefined && value !== null) {
        formData.append(key, value instanceof File ? value : String(value))
      }
    }
    return formData
  }

  toAuthenticationDto(): {
    email: string
    password?: string | null
    googleToken?: string
    authProvider: string
    name?: string
    zipCode?: number
    birthdate?: string
  } {
    return {
      email: this.email,
      password: this.password ?? null,
      googleToken: this.googleToken ?? '',
      authProvider: this.authProvider ?? 'LOCAL',
      name: this.name ?? 'User',
      zipCode: this.zipCode ?? -1,
      birthdate: this.birthdate ?? '1970-01-01',
    }
  }
}

export type AuthProvider = 'LOCAL' | 'GOOGLE'

export interface AuthResponse {
  id: number
  jwt: string
  email: string
  name: string
  imgUrl: string | null
  imgBlob: string | null
}

export interface AuthRequest {
  email: string
  googleToken?: string
  password?: string
  authProvider: AuthProvider
}
