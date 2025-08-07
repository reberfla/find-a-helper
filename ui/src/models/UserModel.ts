export class UserModel {
  constructor(
    public email: string,
    public name: string,
    public password?: string,
    public id?: number,
    public token?: string,
    public authenticationProvider?: string,
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
    token?: string
    authenticationProvider: string
    name?: string
    zipCode?: number
    birthdate?: string
  } {
    return {
      email: this.email,
      password: this.password ?? null,
      token: this.token ?? '',
      authenticationProvider: this.authenticationProvider ?? 'LOCAL',
      name: this.name ?? 'User',
      zipCode: this.zipCode ?? -1,
      birthdate: this.birthdate ?? '1970-01-01',
    }
  }
}
